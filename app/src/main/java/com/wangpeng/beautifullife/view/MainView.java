package com.wangpeng.beautifullife.view;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by d1yf.132 on 2016/8/9.
 */
public interface MainView {
    void setupDrawerContent(NavigationView navigationView);
    AppCompatActivity getActivity();
    DrawerLayout getDrawer();
    void setDrawerSelectedId(int selectedId);
    void setDrawerHeadImage(String img);
    int getCurrSelectedDrawerId();

}
