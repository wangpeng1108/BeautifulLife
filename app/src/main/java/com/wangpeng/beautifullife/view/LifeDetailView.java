package com.wangpeng.beautifullife.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by d1yf.132 on 2016/8/18.
 */
public interface LifeDetailView {

    void setContent(String string);
    void setNetError();
    void setmTitle(String String);
    void showProgress();
    void hideProgress();
    void onResume();
    void onDestory();
    void setBackground(String img);
    Intent getmIntent();
    AppCompatActivity getActivity();
}
