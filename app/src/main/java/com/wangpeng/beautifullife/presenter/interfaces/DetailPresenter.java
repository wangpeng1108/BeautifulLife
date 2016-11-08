package com.wangpeng.beautifullife.presenter.interfaces;

import android.view.MenuItem;

/**
 * Created by d1yf.132 on 2016/8/9.
 */
public interface DetailPresenter {

    void onResume();
    void onDestory();
    void init();
    void setTitle(String title);
    void setContent(String content);
    void setBackground(String pic);
    void setMenuItemClick(MenuItem item);
}
