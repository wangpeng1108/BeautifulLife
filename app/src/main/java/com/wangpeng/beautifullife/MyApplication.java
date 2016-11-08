package com.wangpeng.beautifullife;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by d1yf.132 on 2016/8/12.
 */
public class MyApplication extends Application {
    public static RequestQueue queue;// 建立请求队列

    @Override
    public void onCreate() {
        super.onCreate();
        queue = Volley.newRequestQueue(getApplicationContext());
    }

    public static RequestQueue getHttpQueue() {
        return queue;
    }
}
