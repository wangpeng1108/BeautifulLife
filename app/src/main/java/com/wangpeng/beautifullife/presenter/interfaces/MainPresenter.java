package com.wangpeng.beautifullife.presenter.interfaces;

import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by d1yf.132 on 2016/8/9.
 */
public interface MainPresenter {
    void onResume();
    void onDestory();
    void initDrawerContent(NavigationView navigationView);
    void setDrawerHeaderImg(String img);
    void setMenuClick(MenuItem item);
    void beforeOptionMenu(Menu menu);
    void allViewClick(View v);
    void setFragment(int fragmentId);
}
