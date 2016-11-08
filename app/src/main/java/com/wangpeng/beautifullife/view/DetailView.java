package com.wangpeng.beautifullife.view;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by d1yf.132 on 2016/8/9.
 */
public interface DetailView {
    void initTitle(String title);
    void initContent(String content);

    AppCompatActivity getActivity();
    CollapsingToolbarLayout getToolBar();
    TextView getmTitle();
    TextView getSubtitle();
    TextView getContent();
    int getType();
    String getUrl();
    String getArticleTitle();
}
