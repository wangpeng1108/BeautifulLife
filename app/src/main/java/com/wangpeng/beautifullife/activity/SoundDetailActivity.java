package com.wangpeng.beautifullife.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.wangpeng.beautifullife.R;
import com.wangpeng.beautifullife.presenter.SoundDetailPresenterImpl;
import com.wangpeng.beautifullife.presenter.interfaces.SoundDetailPresenter;
import com.wangpeng.beautifullife.service.SoundService;
import com.wangpeng.beautifullife.view.SoundDetailView;

public class SoundDetailActivity extends AppCompatActivity implements SoundDetailView,View.OnClickListener,CompoundButton.OnCheckedChangeListener {

    public static final String EXTRA_URL = "url",EXTRA_BACK="back",EXTRA_TITLE="title";
    private SoundDetailPresenter presenter;
    private SeekBar seekBar;
    private Toolbar toolbar;
    private String url;
    private String title;
    private String back;
    private RelativeLayout relativeLayout;

    private CheckBox favor,play;
    private ImageView next,stop;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_detail);

        Intent intent = getIntent();
        back = intent.getStringExtra(EXTRA_BACK);
        url = intent.getStringExtra(EXTRA_URL);
        title = intent.getStringExtra(EXTRA_TITLE);
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
        presenter = new SoundDetailPresenterImpl(this);

        if(toolbar==null){
            toolbar = (Toolbar) findViewById(R.id.base_toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        favor = (CheckBox)findViewById(R.id.favor);
        favor.setOnCheckedChangeListener(this);
        play = (CheckBox)findViewById(R.id.play);
        play.setOnCheckedChangeListener(this);
        next = (ImageView)findViewById(R.id.next);
        next.setOnClickListener(this);
        stop = (ImageView)findViewById(R.id.stop);
        stop.setOnClickListener(this);
        presenter.init();

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestory();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.meiriyiwen_menu, menu);
        return true;
    }

    @Override
    public void initTitle(String title) {
        presenter.setTitle(title);
    }


    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public Toolbar getToolBar() {
        if(toolbar==null){
            toolbar = (Toolbar) findViewById(R.id.base_toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        return toolbar;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        presenter.setMenuItemClick(item);
        return super.onOptionsItemSelected(item);
    }


    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getBack() {
        return back;
    }

    @Override
    public String getArticleTitle() {
        return title;
    }

    public void showSnakeBar(String string){
        if(seekBar==null){
            seekBar=(SeekBar)findViewById(R.id.progress);
        }
        Snackbar.make(seekBar,string,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setBackGround(Bitmap bitmap){
        if(relativeLayout==null){
            relativeLayout=(RelativeLayout)findViewById(R.id.main_content);
        }
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.JELLY_BEAN)
        relativeLayout.setBackground(new BitmapDrawable(getResources(),bitmap));
    }

    @Override
    public RelativeLayout getRelativeLayout() {
        if(relativeLayout==null){
            relativeLayout=(RelativeLayout)findViewById(R.id.main_content);
        }
        return relativeLayout;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        presenter.setCheckBoxChange(compoundButton,b);
    }

    @Override
    public void onClick(View view) {
        presenter.setOnClick(view);
    }
    public void startService(){
        Intent intent = new Intent();
        intent.setClass(this, SoundService.class);

    }
}
