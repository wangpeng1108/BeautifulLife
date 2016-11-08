package com.wangpeng.beautifullife.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import com.wangpeng.beautifullife.activity.LifeDetailActivity;
import com.wangpeng.beautifullife.entity.MLife;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import in.srain.cube.util.LocalDisplay;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import in.srain.cube.views.ptr.PtrHandler2;
import in.srain.cube.views.ptr.PtrDefaultHandler2;

/**
 * Created by d1yf.132 on 2016/8/17.
 */
public class LifeFragment extends Fragment {

    private List<MLife> list = new ArrayList<MLife>();
    private static final String TAG = "MSoundFragment";
    private SimpleStringRecyclerViewAdapter adapter;
    private RecyclerView rv;
    private ContentLoadingProgressBar progressBar;
    private PtrFrameLayout mPtrFrame;
    private int page=1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_life,container,false);
        initViews(view);
        rv = (RecyclerView) view.findViewById(R.id.life_recycleview);

        setupRecyclerView(rv);;
        rv.setHasFixedSize(true);
        return view;
    }

    private void initViews(View root){
        progressBar = (ContentLoadingProgressBar)root.findViewById(R.id.progress);
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("生活");
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
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG,"RecycleView数据开始获取");
                                StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://book.meiriyiwen.com?page="+page,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Document document = Jsoup.parse(response);
                                                Elements start = document.select("table.content");
                                                Elements articles = start.select("tr.books");
                                                //Log.d(TAG,"共"+articles.size()+"条数据");
                                                for(int i=0;i<articles.size();i++){
                                                    MLife life = new MLife();
                                                    life.setTitle(articles.get(i).select("div.book-name").text());
                                                    life.setImg("http://book.meiriyiwen.com"+articles.get(i).select("img").attr("src"));
                                                    life.setUrl("http://book.meiriyiwen.com"+articles.get(i).select("a").last().attr("href"));
                                                    life.setDesc(articles.get(i).select("div.book-author").text());
                                                    list.add(life);
                                                }
                                                //adapter = new SimpleStringRecyclerViewAdapter(getActivity(),list);
                                                //rv.setAdapter(adapter);
                                                adapter.notifyDataSetChanged();
                                                Log.d(TAG,"done");
                                                progressBar.hide();
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("TAG", error.getMessage(), error);
                                        Snackbar.make(rv,"网络连接失败！",Snackbar.LENGTH_SHORT).show();
                                        progressBar.hide();
                                    }
                                });
                                MyApplication.getHttpQueue().add(stringRequest);
                                //new HttpHelper("http://voice.meiriyiwen.com").getHtml();
                            }
                        });
                        mPtrFrame.refreshComplete();
                    }
                }, 0);
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

        progressBar.show();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"RecycleView数据开始获取");
                StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://book.meiriyiwen.com",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Document document = Jsoup.parse(response);
                                Elements start = document.select("table.content");
                                Elements articles = start.select("tr.books");
                                //Log.d(TAG,"共"+articles.size()+"条数据");
                                for(int i=0;i<articles.size();i++){
                                    MLife life = new MLife();
                                    life.setTitle(articles.get(i).select("div.book-name").text());
                                    life.setImg("http://book.meiriyiwen.com"+articles.get(i).select("img").attr("src"));
                                    life.setUrl("http://book.meiriyiwen.com"+articles.get(i).select("a").last().attr("href"));
                                    life.setDesc(articles.get(i).select("div.book-author").text());
                                    list.add(life);
                                }
                                adapter = new SimpleStringRecyclerViewAdapter(getActivity(),list);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                Log.d(TAG,"done");
                                progressBar.hide();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Snackbar.make(recyclerView,"网络连接失败！",Snackbar.LENGTH_SHORT).show();
                        progressBar.hide();
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
        private List<MLife> mValues;
        private Context _context;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public MLife mSujin;
            public final View mView;
            private String link;
            private ImageView imageView;
            public final TextView title,desc;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                title = (TextView) view.findViewById(R.id.title);
                imageView = (ImageView) view.findViewById(R.id.bookimg);
                desc = (TextView)view.findViewById(R.id.desc);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + title.getText();
            }
        }

        public MLife getValueAt(int position) {
            return mValues.get(position);
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<MLife> items) {
            //context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            // mBackground = mTypedValue.resourceId;
            mValues = items;
            _context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(_context)
                    .inflate(R.layout.list_life_item, parent, false);
            //view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mSujin = mValues.get(position);
            holder.title.setText(holder.mSujin.getTitle());
            holder.desc.setText(holder.mSujin.getDesc());
            Glide.with(_context).load(holder.mSujin.getImg()).centerCrop().into(holder.imageView);
            holder.link = holder.mSujin.getUrl();

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Snackbar.make(v,"获取中，请稍等",Snackbar.LENGTH_SHORT).show();
                    final Context context = v.getContext();
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "数据开始获取:"+holder.link);
                            //new HttpHelper(holder.link).getHtml();
                            final List<String> urls = new ArrayList<String>();
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, holder.mSujin.getUrl(),
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Document document = Jsoup.parse(response);
                                            Elements elements = document.select("div.chapter-list").select("a");
                                            //System.out.println("共"+elements.size()+"条数据");
                                            final String[] items = new String[elements.size()];
                                            for(int i=0;i<elements.size();i++){
                                                items[i]=elements.get(i).text();
                                                urls.add("http://book.meiriyiwen.com"+elements.get(i).attr("href"));
                                            }
                                            new AlertDialog.Builder(context).setTitle(holder.mSujin.getTitle())
                                                    .setItems(items, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //Snackbar.make(v,items[which]+","+urls.get(which),Snackbar.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(context,LifeDetailActivity.class);
                                                            intent.putExtra("link",urls.get(which));
                                                            intent.putExtra("title",items[which]);
                                                            context.startActivity(intent);
                                                        }
                                                    }).show();
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Snackbar.make(v, "连接网络失败！", Snackbar.LENGTH_SHORT).show();
                                }
                            });
                            MyApplication.getHttpQueue().add(stringRequest);
                        }
                    });
                }
            });
        }
        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}
