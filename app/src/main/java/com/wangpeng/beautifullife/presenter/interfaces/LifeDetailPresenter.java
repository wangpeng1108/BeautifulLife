package com.wangpeng.beautifullife.presenter.interfaces;

import android.view.MenuItem;

/**
 * Created by d1yf.132 on 2016/8/18.
 */
public interface LifeDetailPresenter {
    void getData();
    void onMenuSelected(MenuItem item);
    void onResume();
    void onDestory();
}
