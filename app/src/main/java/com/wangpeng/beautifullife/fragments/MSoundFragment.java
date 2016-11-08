package com.wangpeng.beautifullife.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import com.bumptech.glide.Glide;
import com.wangpeng.beautifullife.MyApplication;
import com.wangpeng.beautifullife.R;
import com.wangpeng.beautifullife.activity.SoundDetailActivity;
import com.wangpeng.beautifullife.entity.MSound;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.util.LocalDisplay;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler2;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

/**
 * Created by d1yf.132 on 2016/8/15.
 */
public class MSoundFragment extends Fragment {
    private List<MSound> list = new ArrayList<MSound>();
    private static final String TAG = "MSoundFragment";
    private SimpleStringRecyclerViewAdapter adapter;
    private ContentLoadingProgressBar progressBar;
    private RecyclerView rv;

    private PtrFrameLayout mPtrFrame;
    private int page=1;
    //private ImageView imageView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.include_list_recycleview,container,false);

        //imageView = (ImageView)view.findViewById(R.id.backdrop);
        initViews(view);
        rv = (RecyclerView) view.findViewById(R.id.recyclerview);

        setupRecyclerView(rv);;
        rv.setHasFixedSize(true);
        return view;
    }

    private void initViews(View root){
        progressBar = (ContentLoadingProgressBar)root.findViewById(R.id.progress);
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("静听");
        final ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(ab!=null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        mPtrFrame = (PtrFrameLayout) root.findViewById(R.id.ptr_frame);
        StoreHouseHeader header = new StoreHouseHeader(getContext());
        header.setTextColor(R.color.skyblue);
        header.setPadding(0, LocalDisplay.dp2px(20), 0, LocalDisplay.dp2px(20));
        header.initWithString("Refreshing");

        mPtrFrame.setDurationToCloseHeader(1500);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);

        StoreHouseHeader footer = new StoreHouseHeader(getContext());
        footer.setTextColor(R.color.skyblue);
        footer.setPadding(0, LocalDisplay.dp2px(20), 0, LocalDisplay.dp2px(20));
        footer.initWithString("Loading More");

        mPtrFrame.setFooterView(footer);
        mPtrFrame.addPtrUIHandler(footer);
        mPtrFrame.setPtrHandler(new PtrHandler2() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
                return PtrDefaultHandler2.checkContentCanBePulledUp(frame, content, footer);
            }

            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                page++;
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"RecycleView数据开始获取");
                        StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://voice.meiriyiwen.com/voice/past?page="+page,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Document document = Jsoup.parse(response);
                                        Elements articles = document.select("div.voice_card");
                                        Log.d(TAG, articles.size() + "");
                                        if(articles.size()>0)
                                            //Log.d(TAG,"图片："+elses.get(0).select("img").attr("src"));
                                            //Glide.with(getActivity()).load(elses.get(0).select("img").attr("src")).centerCrop().into(imageView);
                                            for (int i = 0; i < articles.size(); i++) {
                                                MSound sujin = new MSound();
                                                sujin.setUrl("http://voice.meiriyiwen.com"+articles.get(i).select("a").first().attr("href"));
                                                sujin.setTitle(articles.get(i).select("a").last().text());
                                                sujin.setAuthor(articles.get(i).select(".voice_author").first().text());
                                                sujin.setSounder("");
                                                sujin.setImage("http://voice.meiriyiwen.com"+articles.get(i).select("img").attr("src"));
                                                list.add(sujin);
                                            }

                                        adapter.notifyDataSetChanged();
                                        Log.d(TAG,"done");
                                        progressBar.hide();mPtrFrame.refreshComplete();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("TAG", error.getMessage(), error);
                                progressBar.hide();
                                Snackbar.make(rv,"网络连接失败！",Snackbar.LENGTH_SHORT).show();mPtrFrame.refreshComplete();
                            }
                        });
                        MyApplication.getHttpQueue().add(stringRequest);
                        //new HttpHelper("http://voice.meiriyiwen.com").getHtml();
                    }
                });
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();
                    }
                }, 1500);
            }
        });
    }
    private void setupRecyclerView(final RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        progressBar.show();mPtrFrame.setVisibility(View.GONE);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"RecycleView数据开始获取");
                StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://voice.meiriyiwen.com",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Document document = Jsoup.parse(response);
                                Elements articles = document.select("div.voice_card");
                                Log.d(TAG, articles.size() + "");
                                if(articles.size()>0)
                                    //Log.d(TAG,"图片："+elses.get(0).select("img").attr("src"));
                                    //Glide.with(getActivity()).load(elses.get(0).select("img").attr("src")).centerCrop().into(imageView);
                                    for (int i = 0; i < articles.size(); i++) {
                                        MSound sujin = new MSound();
                                        sujin.setUrl("http://voice.meiriyiwen.com"+articles.get(i).select("a").first().attr("href"));
                                        sujin.setTitle(articles.get(i).select("a").last().text());
                                        sujin.setAuthor(articles.get(i).select(".voice_author").first().text());
                                        sujin.setSounder("");
                                        sujin.setImage("http://voice.meiriyiwen.com"+articles.get(i).select("img").attr("src"));
                                        list.add(sujin);
                                    }
                                adapter = new SimpleStringRecyclerViewAdapter(getActivity(),list);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                Log.d(TAG,"done");
                                progressBar.hide();mPtrFrame.setVisibility(View.VISIBLE);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        progressBar.hide();mPtrFrame.setVisibility(View.VISIBLE);
                        Snackbar.make(recyclerView,"网络连接失败！",Snackbar.LENGTH_SHORT).show();
                    }
                });
                MyApplication.getHttpQueue().add(stringRequest);
                //new HttpHelper("http://voice.meiriyiwen.com").getHtml();
            }
        });

    }

    static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<MSound> mValues;
        private Context _context;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public MSound mSujin;
            public final View mView;
            private String link;
            private ImageView imageView;
            public final TextView title,author,sounder;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                title = (TextView) view.findViewById(R.id.title);
                author = (TextView) view.findViewById(R.id.author);
                sounder = (TextView) view.findViewById(R.id.sounder);
                imageView = (ImageView) view.findViewById(R.id.cardback);

            }

            @Override
            public String toString() {
                return super.toString() + " '" + title.getText();
            }
        }

        public MSound getValueAt(int position) {
            return mValues.get(position);
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<MSound> items) {
            //context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            // mBackground = mTypedValue.resourceId;
            mValues = items;
            _context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(_context)
                    .inflate(R.layout.list_msound_item, parent, false);
            //view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mSujin = mValues.get(position);
            holder.title.setText(holder.mSujin.getTitle());
            holder.author.setText(holder.mSujin.getAuthor());
            holder.sounder.setText(holder.mSujin.getSounder());
            Glide.with(_context).load(holder.mSujin.getImage()).centerCrop().into(holder.imageView);
            holder.link = holder.mSujin.getUrl();

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Context context = v.getContext();
                    Intent intent = new Intent(context, SoundDetailActivity.class);
                    intent.putExtra(SoundDetailActivity.EXTRA_URL, holder.link);
                    intent.putExtra(SoundDetailActivity.EXTRA_BACK, holder.mSujin.getImage());
                    intent.putExtra(SoundDetailActivity.EXTRA_TITLE,holder.mSujin.getTitle());
                    context.startActivity(intent);*/
                    StringRequest request = new StringRequest(Request.Method.GET, holder.link,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Document document = Jsoup.parse(response);
                                    String url = document.select("audio").first().attr("src");
                                    System.out.println(url);
                                    Intent it = new Intent(Intent.ACTION_VIEW);
                                    System.out.println("audio");
                                    it.setDataAndType(Uri.parse(url),"video/mp4");
                                    _context.startActivity(it);

                                    //soundService = new SoundService();
                                    //soundService.initPlayer(url);
                                    //System.out.println(audio);

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Snackbar.make(holder.sounder,"网络连接失败！",Snackbar.LENGTH_SHORT).show();
                        }
                    });

                    MyApplication.getHttpQueue().add(request);
                }
            });


        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}
