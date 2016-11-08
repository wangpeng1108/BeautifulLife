package com.wangpeng.beautifullife.view;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by d1yf.132 on 2016/8/9.
 */
public interface LauncherView {
    AppCompatActivity getActivity();
    ImageView getBack();
    void setBack(Bitmap bitmap);
}
