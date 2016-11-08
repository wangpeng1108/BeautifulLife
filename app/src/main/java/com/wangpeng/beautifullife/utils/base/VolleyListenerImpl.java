package com.wangpeng.beautifullife.utils.base;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by d1yf.132 on 2016/8/13.
 */
public class VolleyListenerImpl extends VolleyListenerInterface {

    public VolleyListenerImpl(Context context, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(context,listener,errorListener);

    }

    @Override
    public void onMyError(VolleyError error) {

    }

    @Override
    public void onMySuccess(String result) {

    }


}
