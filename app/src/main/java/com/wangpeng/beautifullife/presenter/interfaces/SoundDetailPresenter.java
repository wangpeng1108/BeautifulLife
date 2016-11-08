package com.wangpeng.beautifullife.presenter.interfaces;

import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

/**
 * Created by d1yf.132 on 2016/8/9.
 */
public interface SoundDetailPresenter {

    void onResume();
    void onDestory();
    void init();
    void setTitle(String title);
    void setSounderImg(String pic);
    void setMenuItemClick(MenuItem item);
    void setCheckBoxChange(CompoundButton v,boolean checked);
    void setOnClick(View v);
}
