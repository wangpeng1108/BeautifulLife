package com.wangpeng.beautifullife.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wangpeng.beautifullife.R;
import com.wangpeng.beautifullife.activity.DetailActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by d1yf.132 on 2016/8/11.
 */
public class CheeseListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private List<String> list;
    private int lastVisiableItem=0;
    LinearLayoutManager mLayoutManager;
    private SimpleStringRecyclerViewAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_cheese_list,container,false);
        RecyclerView rv = (RecyclerView) swipeRefreshLayout.findViewById(R.id.recyclerview);

        swipeRefreshLayout.setColorSchemeColors(R.color.red,R.color.gold,R.color.cyan,R.color.purple,R.color.brown);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressViewOffset(false,0,(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,24,getResources().getDisplayMetrics()));

        rv.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        setupRecyclerView(rv);

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("list","lastVisiableItem+1:"+(lastVisiableItem+1)+",adaptercount:"+adapter.getItemCount());
                if(newState==RecyclerView.SCROLL_STATE_IDLE&&(lastVisiableItem+1==adapter.getItemCount())){
                    swipeRefreshLayout.setRefreshing(true);
                    //网络处理
                    mHander.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<String> l = getRandomSublist(Cheeses.sCheeseStrings, 10);
                            list.addAll(l);
                            adapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                            Log.d("list","done");
                        }
                    },3000);
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisiableItem= mLayoutManager.findLastVisibleItemPosition();
                Log.d("lastItem",lastVisiableItem+"");
                swipeRefreshLayout.setEnabled(mLayoutManager.findFirstCompletelyVisibleItemPosition()==0);
                Log.d("enable",swipeRefreshLayout.isEnabled()+","+(mLayoutManager.findFirstCompletelyVisibleItemPosition()==0));
            }
        });
        return swipeRefreshLayout;
    }
    private void setupRecyclerView(final RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        //new Thread(){public void run(){

            mHander.postDelayed(new Runnable() {
                @Override
                public void run() {
                    list = getRandomSublist(Cheeses.sCheeseStrings, 30);
                    adapter = new  SimpleStringRecyclerViewAdapter(getActivity(),list);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            },1000);

            //recyclerView
        //}}.start();

    }


    static Handler mHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };

    private List<String> getRandomSublist(String[] array, int amount) {
        ArrayList<String> list = new ArrayList<>(amount);
        Random random = new Random();
        while (list.size() < amount) {
            list.add(array[random.nextInt(array.length)]);
        }
        return list;
    }

    @Override
    public void onRefresh() {

    }

    static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<String> mValues;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public String mBoundString;

            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                mTextView = (TextView) view.findViewById(android.R.id.text1);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public String getValueAt(int position) {
            return mValues.get(position);
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<String> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mBoundString = mValues.get(position);
            holder.mTextView.setText(mValues.get(position));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_URL, holder.mBoundString);

                    context.startActivity(intent);
                }
            });

            //new Thread(){public void run(){
                final int img = Cheeses.getRandomCheeseDrawable();
                mHander.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(holder.mImageView.getContext())
                                .load(img)
                                .fitCenter()
                                .into(holder.mImageView);
                    }
                },1000);
            //}}.start();
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}
