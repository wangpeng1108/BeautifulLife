package com.wangpeng.beautifullife.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wangpeng.beautifullife.R;
import com.wangpeng.beautifullife.presenter.LifeDetailPresenterImpl;
import com.wangpeng.beautifullife.presenter.interfaces.LifeDetailPresenter;
import com.wangpeng.beautifullife.view.LifeDetailView;

/**
 * Created by d1yf.132 on 2016/8/18.
 */
public class LifeDetailActivity extends AppCompatActivity implements LifeDetailView{
    private TextView content;
    private Toolbar toolbar;
    private LifeDetailPresenter presenter;
    private ContentLoadingProgressBar progressBar;
    private Intent intent;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_detail);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);//calculateStatusColor(Color.WHITE,(int)alphaValue);
            //getDrawer().setClipToPadding(false);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            //getSupportActionBar().
        }else
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        presenter = new LifeDetailPresenterImpl(this);
        content = (TextView)findViewById(R.id.content);
        intent = getIntent();

        presenter .getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void setContent(String string) {
        if(content==null){
            content = (TextView)findViewById(R.id.content);
        }
        content.setText(string);
    }

    @Override
    public void setmTitle(String string) {
        if(toolbar==null){
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setTitle(string);
        getSupportActionBar().setTitle(string);
    }

    @Override
    public void showProgress() {
        if(progressBar==null){
            progressBar = (ContentLoadingProgressBar)findViewById(R.id.progress);
        }
        progressBar.show();
        progressBar.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        if(progressBar==null){
            progressBar = (ContentLoadingProgressBar)findViewById(R.id.progress);
        }
        progressBar.hide();
        progressBar.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        presenter.onMenuSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Intent getmIntent() {

        return intent;
    }

    @Override
    public void setBackground(String img) {
        if(back==null){
            back = (ImageView)findViewById(R.id.backdrop);
        }
        Glide.with(this).load(img).centerCrop().into(back);
    }

    @Override
    public void setNetError() {
        Snackbar.make(content,"连接网络错误！",Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public void onDestory() {
        presenter.onDestory();
        super.onDestroy();
    }
}
