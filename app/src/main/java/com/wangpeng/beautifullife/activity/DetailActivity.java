package com.wangpeng.beautifullife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.wangpeng.beautifullife.R;
import com.wangpeng.beautifullife.presenter.DetailPresenterImpl;
import com.wangpeng.beautifullife.presenter.interfaces.DetailPresenter;
import com.wangpeng.beautifullife.view.DetailView;

public class DetailActivity extends AppCompatActivity implements DetailView{

    public static final String EXTRA_URL = "url",EXTRA_TYPE="type",EXTRA_TITLE="title";
    private DetailPresenter presenter;
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView content,mTitle,subtitle;
    private String url;
    private String title;
    private int type;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        type = intent.getIntExtra(EXTRA_TYPE,0);
        url = intent.getStringExtra(EXTRA_URL);
        title = intent.getStringExtra(EXTRA_TITLE);

        presenter = new DetailPresenterImpl(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
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
    public void initContent(String content) {
        presenter.setContent(content);
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public CollapsingToolbarLayout getToolBar() {
        return collapsingToolbar;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        presenter.setMenuItemClick(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public TextView getmTitle() {
        if(mTitle==null)
            mTitle = (TextView)findViewById(R.id.title);
        return mTitle;
    }

    @Override
    public TextView getSubtitle() {
        if(subtitle==null)
            subtitle = (TextView)findViewById(R.id.subtitle);
        return subtitle;
    }

    public TextView getContent(){
        if(content==null)
            content=(TextView)findViewById(R.id.content);
        return content;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public String getArticleTitle() {
        return title;
    }
}
