package com.wangpeng.beautifullife.view;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

/**
 * Created by d1yf.132 on 2016/8/9.
 */
public interface SoundDetailView {
    void initTitle(String title);

    AppCompatActivity getActivity();
    Toolbar getToolBar();
    String getBack();
    String getUrl();
    String getArticleTitle();
    void showSnakeBar(String string);
    void setBackGround(Bitmap bitmap);
    RelativeLayout getRelativeLayout();
    void startService();
}
