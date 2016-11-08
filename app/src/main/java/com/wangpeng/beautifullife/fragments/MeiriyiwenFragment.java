package com.wangpeng.beautifullife.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.wangpeng.beautifullife.MyApplication;
import com.wangpeng.beautifullife.R;
import com.wangpeng.beautifullife.utils.base.BaseUtils;
import com.wangpeng.beautifullife.utils.base.MySharedPreferences;
import com.wangpeng.beautifullife.view.MainView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import in.srain.cube.util.LocalDisplay;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler2;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

/**
 * Created by d1yf.132 on 2016/8/12.
 */
public class MeiriyiwenFragment extends Fragment {
    private TextView title,author,content;
    private CollapsingToolbarLayout collapsingToolbar;
    private NestedScrollView scrollView;
    private ImageView imageView;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    public static final int UPDATE=0;
    private static final String TAG="MeiriyiwenFragment";
    private ContentLoadingProgressBar progressBar;
    private boolean goon=true;
    private PtrFrameLayout mPtrFrame;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_meiriyiwen_article,container,false);
        initView(view);
        initData(view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        setHasOptionsMenu(true);
        inflater.inflate(R.menu.meiriyiwen_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_next:
                initData(content);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData(final View view) {
        progressBar.show();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://www.meiriyiwen.com/random/iphone",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Document doc = Jsoup.parse(response);
                                //Log.d(TAG,(doc==null?"doc为空":doc.title()));
                                Elements mTitle = doc.getElementsByTag("h2");
                                //Log.d(TAG,mTitle==null?"mTitle为空":"文章标题："+mTitle.text());
                                title.setText(mTitle.text());
                                Elements mAuthor = doc.getElementsByClass("articleAuthorName");
                                //Log.d(TAG,mAuthor==null?"mTitle为空":"文章作者："+mAuthor.text());
                                author.setText(mAuthor.text());
                                Elements mContent = doc.getElementsByClass("articleContent");
                                //Log.d(TAG,mContent==null?"mContent":"文章内容："+mContent.html());

                                content.setText(mContent.html().replaceAll("<p>","\t\t\t").replaceAll("</p>",""));
                                toolbar.setTitle(mTitle.text());
                                collapsingToolbar.setTitle(mTitle.text());
                                progressBar.hide();
                                //Log.d(TAG,"获取成功！");
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("TAG", error.getMessage(), error);
                        progressBar.hide();
                        Snackbar.make(view,"网络请求出错",Snackbar.LENGTH_SHORT).show();
                    }
                });
                MyApplication.getHttpQueue().add(stringRequest);
                downImg();


            }
        });
    }
    private void downImg(){
        new Thread(){public void run(){
            int id = new Random().nextInt(99)+1;
            final String path = "http://meiriyiwen.com/images/new_feed/bg_"+id+".jpg";
            DrawableTypeRequest drawableTypeRequest = Glide.with(getActivity()).load(path);//.centerCrop();//.into(imageView);
            try {
                BaseUtils utils = new BaseUtils(getActivity());
                int[] screen = utils.getScreen();
                File file = Glide.with(getActivity()).load(path).downloadOnly(screen[0],screen[1]).get();
                if(!goon)return;
                MySharedPreferences.setString(getActivity(),"lastLauncheredPic",file.getAbsolutePath());
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("path",file.getAbsolutePath());
                msg.what=0;
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        }.start();
    }

    private void initView(final View view){
        title = (TextView)view.findViewById(R.id.title);
        author = (TextView)view.findViewById(R.id.author);
        content = (TextView)view.findViewById(R.id.content);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        imageView = (ImageView) view.findViewById(R.id.backdrop);
        toolbar.setTitle(" ");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.floatbutton);
        collapsingToolbar =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!progressBar.isShown())
                initData(view);
            }
        });
        progressBar = (ContentLoadingProgressBar)view.findViewById(R.id.progress);
        scrollView  = (NestedScrollView)view.findViewById(R.id.scrollView);
        //scrollView.add

        mPtrFrame = (PtrFrameLayout) view.findViewById(R.id.ptr_frame);
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
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData(view);
                        mPtrFrame.refreshComplete();
                    }
                }, 1500);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData(view);
                        mPtrFrame.refreshComplete();
                    }
                }, 1500);
            }
        });
    }

    public Handler mHandler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what){
                case UPDATE:
                    //Log.d(TAG,"收到发送的内容");
                    Bundle bundle = msg.getData();
                    //Log.d(TAG,bundle.getString("path"));
                    Glide.with(getActivity()).load(bundle.getString("path")).centerCrop().into(imageView);
                    //System.out.println("加载第一个图片");
                    ((MainView)getActivity()).setDrawerHeadImage(bundle.getString("path"));
                    //System.out.println("设置菜单图片");
                    Log.d(TAG,"背景加载成功！");
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        goon=true;
    }

    @Override
    public void onDestroyView() {
        goon=false;
        super.onDestroyView();
    }
}
