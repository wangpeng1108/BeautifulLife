package com.wangpeng.beautifullife.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
 * Created by d1yf.132 on 2016/8/15.
 */
public class NianhuaListFragment extends Fragment {
    private List<Sujin> list = new ArrayList<Sujin>();
    private static final String TAG = "SujinListFragment";
    private SimpleStringRecyclerViewAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sujin_list,container,false);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recyclerview);

        //setupRecyclerView(rv);
        //rv.setHasFixedSize(true);


        return view;
    }

    private void setupRecyclerView(final RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"RecycleView数据开始获取");
                StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://isujin.com/archive",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Document document = Jsoup.parse(response);
                                Elements elses = document.select("div.post");
                                Log.d(TAG, elses.size() + "");
                                if(elses.size()>0)
                                    //Log.d(TAG,"图片："+elses.get(0).select("img").attr("src"));
                                    //Glide.with(getActivity()).load(elses.get(0).select("img").attr("src")).centerCrop().into(imageView);
                                    for (int i = 0; i < elses.size(); i++) {
                                        //System.out.println("链接："+elses.get(i).select("a").attr("href"));
                                        //System.out.println("标题："+elses.get(i).select("a").attr("title"));
                                        //System.out.println("日期："+elses.get(i).select("p").first().text());
                                        //System.out.println("简介："+elses.get(i).select("p").remove(1).text());
                                        //System.out.println("其他："+elses.get(i).select("p").last().text());
                                        Sujin sujin = new Sujin();
                                        sujin.setLink(elses.get(i).select("a").attr("href"));
                                        sujin.setTitle(elses.get(i).select("a").attr("title"));
                                        sujin.setDate(elses.get(i).select("p").first().text());
                                        sujin.setDesc(elses.get(i).select("p").remove(1).text());
                                        sujin.setTotleread(elses.get(i).select("p").last().text().split(" ")[1]);
                                        list.add(sujin);
                                    }
                                adapter = new SimpleStringRecyclerViewAdapter(getActivity(),list);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                Log.d(TAG,"done");
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
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
            public final TextView title,desc,date,totleread;

            public ViewHolder(View view) {
                super(view);
                cardView = (CardView)view.findViewById(R.id.sujin_card);
                mView = view;
                title = (TextView) view.findViewById(R.id.title);
                desc = (TextView) view.findViewById(R.id.desc);
                date = (TextView) view.findViewById(R.id.date);
                totleread = (TextView) view.findViewById(R.id.totleread);

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
                    .inflate(R.layout.list_nianhua_item, parent, false);
            //view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mSujin = mValues.get(position);
            holder.title.setText(holder.mSujin.getTitle());
            holder.date.setText(holder.mSujin.getDate());
            holder.desc.setText(holder.mSujin.getDesc());
            holder.totleread.setText(holder.mSujin.getTotleread());
            holder.link = holder.mSujin.getLink();

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_URL, holder.link);
                    intent.putExtra(DetailActivity.EXTRA_TYPE, DetailPresenterImpl.SUJIN_ARTICLE);
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
}
