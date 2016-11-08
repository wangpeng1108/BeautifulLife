package com.wangpeng.beautifullife.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.wangpeng.beautifullife.MyApplication;
import com.wangpeng.beautifullife.R;
import com.wangpeng.beautifullife.activity.DetailActivity;
import com.wangpeng.beautifullife.entity.Sujin;
import com.wangpeng.beautifullife.presenter.DetailPresenterImpl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d1yf.132 on 2016/8/13.
 */
public class SujinListFragment extends Fragment implements OnItemClickListener,ViewPager.OnPageChangeListener{
    private List<Sujin> list = new ArrayList<Sujin>();
    private static final String TAG = "SujinListFragment";
    private SimpleStringRecyclerViewAdapter adapter;
    private ConvenientBanner convenientBanner;
    private List<Sujin> bannerData = new ArrayList<>();
    private TextView bannertitle;
    private ContentLoadingProgressBar progressBar;
    private Toolbar toolbar;
    //private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sujin_list,container,false);
        //imageView = (ImageView)view.findViewById(R.id.backdrop);
        progressBar = (ContentLoadingProgressBar)view.findViewById(R.id.progress);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recyclerview);
        bannertitle = (TextView)view.findViewById(R.id.title);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(" ");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        initBannerData(view);
        setupRecyclerView(rv);
        rv.setHasFixedSize(true);


        return view;
    }

    private void initBannerData(final View view) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"Banner数据开始获取");
                StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://isujin.com",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Document document = Jsoup.parse(response);
                                Elements elses = document.select("div.post");
                                Log.d(TAG, elses.size() + "");
                                int size = elses.size()>5?5:elses.size();
                                if(elses.size()>0)
                                    for (int i = 0; i < size; i++) {
                                        //Log.d(TAG,elses.get(i).html());
                                        Sujin sujin = new Sujin();
                                        sujin.setLink(elses.get(i).select("a").attr("href"));
                                        sujin.setTitle(elses.get(i).select("a").attr("title"));
                                        sujin.setDate(elses.get(i).select("p").first().text());
                                        sujin.setDesc(elses.get(i).select("p").remove(1).text());
                                        sujin.setTotleread(elses.get(i).select("p").last().text().split(" ")[1]);
                                        sujin.setImg(elses.get(i).select("img").attr("src"));
                                        bannerData.add(sujin);
                                    }
                                initBanner(view);
                                //toolbar.setTitle(" ");
                                //Log.d(TAG,"done");
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Snackbar.make(view,"网络连接失败！",Snackbar.LENGTH_SHORT).show();
                    }
                });
                MyApplication.getHttpQueue().add(stringRequest);
            }
        });
    }

    private void initBanner(View view) {
        convenientBanner = (ConvenientBanner)view.findViewById(R.id.convenientBanner);
        convenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, bannerData)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .startTurning(5000)
                .setOnPageChangeListener(this)
                .setOnItemClickListener(this);
        bannertitle.setText(bannerData.get(0).getTitle());
        bannertitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_URL, bannerData.get(position).getLink());
        intent.putExtra(DetailActivity.EXTRA_TYPE,DetailPresenterImpl.SUJIN_ARTICLE);
        intent.putExtra(DetailActivity.EXTRA_TITLE,bannerData.get(position).getTitle());
        getActivity().startActivity(intent);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(bannerData==null)return;
        int size = (position-bannerData.size())%bannerData.size();
        if(size<0){
            size = (bannerData.size()+size)%bannerData.size();
        }
        if(bannerData.size()>0&&size>=0&&size<bannerData.size()&&bannerData.get(size)!=null) {
            bannertitle.setText(bannerData.get(size).getTitle());
            bannertitle.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        bannertitle.setVisibility(View.VISIBLE);
    }

    public class LocalImageHolderView implements Holder<Sujin> {
        private ImageView imageView;
        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, Sujin data) {
            Glide.with(getActivity()).load(data.getImg()).centerCrop().into(imageView);
        }
    }

    private void setupRecyclerView(final RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        progressBar.show();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"RecycleView数据开始获取");
                StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://isujin.com/archive",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Document document = Jsoup.parse(response);
                                String year = "",month="",day="";
                                int monthindex= 0;
                                Elements start = document.select("div#archives");
                                Elements articles = start.select(".al_year");
                                //Log.d(TAG,"共"+articles.size()+"条数据");
                                for(int i=0;i<articles.size();i++){
                                    year=articles.get(i).text().substring(0,4);
                                    //System.out.println(articles.get(i).);
                                    Elements allmonths = start.select("ul.al_mon_list");
                                    Elements thisyearMonth = allmonths.get(i).select("span.al_mon").not("em");
                                    //Log.d(TAG,"共"+thisyearMonth.size()+"个月");
                                    for(int j=0;j<thisyearMonth.size();j++){
                                        month = thisyearMonth.get(j).text().substring(0,2);
                                        Elements allArticles = start.select("ul.al_post_list");
                                        Elements thisMonthArticles = allArticles.get(monthindex).select("li");
                                        //System.out.println("总共："+allArticles.size()+",这个月共："+thisMonthArticles.size()+"篇文章");
                                        for(int k=0;k<thisMonthArticles.size();k++) {
                                            day=thisMonthArticles.get(k).text().substring(0,2);
                                            Sujin sujin = new Sujin();
                                            sujin.setDate(year+"年"+month+"月"+day+"日");
                                            sujin.setTitle(thisMonthArticles.get(k).select("a").last().text());
                                            sujin.setLink(thisMonthArticles.get(k).select("a").first().attr("href"));
                                            list.add(sujin);
                                        }
                                        monthindex++;
                                    }
                                }
                                adapter = new SimpleStringRecyclerViewAdapter(getActivity(),list);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                //toolbar.setTitle(" ");
                                //Log.d(TAG,"done");
                                progressBar.hide();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("TAG", error.getMessage(), error);
                        progressBar.hide();
                        Snackbar.make(recyclerView,"网络连接失败！",Snackbar.LENGTH_SHORT).show();
                    }
                });
                MyApplication.getHttpQueue().add(stringRequest);
            }
        });

    }

    static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<Sujin> mValues;
        private Context _context;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public Sujin mSujin;
            public CardView cardView;
            public final View mView;
            private String link;
            public final TextView title,date;

            public ViewHolder(View view) {
                super(view);
                cardView = (CardView)view.findViewById(R.id.sujin_card);
                mView = view;
                title = (TextView) view.findViewById(R.id.title);
                date = (TextView) view.findViewById(R.id.date);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + title.getText();
            }
        }

        public Sujin getValueAt(int position) {
            return mValues.get(position);
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<Sujin> items) {
            //context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
           // mBackground = mTypedValue.resourceId;
            mValues = items;
            _context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(_context)
                    .inflate(R.layout.list_sujin_simple_item, parent, false);
            //view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mSujin = mValues.get(position);
            holder.title.setText(holder.mSujin.getTitle());
            holder.date.setText(holder.mSujin.getDate());
            holder.link = holder.mSujin.getLink();

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_URL, holder.link);
                    intent.putExtra(DetailActivity.EXTRA_TYPE,DetailPresenterImpl.SUJIN_ARTICLE);
                    intent.putExtra(DetailActivity.EXTRA_TITLE,holder.mSujin.getTitle());
                    context.startActivity(intent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(convenientBanner!=null){
            convenientBanner.startTurning(4000);
        }
    }

    @Override
    public void onPause() {
        if(convenientBanner!=null){
            convenientBanner.stopTurning();
        }
        super.onPause();
    }
}
