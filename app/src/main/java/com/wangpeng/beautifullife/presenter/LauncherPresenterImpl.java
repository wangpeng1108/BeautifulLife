package com.wangpeng.beautifullife.presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.wangpeng.beautifullife.activity.MainActivity;
import com.wangpeng.beautifullife.R;
import com.wangpeng.beautifullife.fragments.Cheeses;
import com.wangpeng.beautifullife.presenter.interfaces.LauncherPresenter;
import com.wangpeng.beautifullife.view.LauncherView;

/**
 * Created by d1yf.132 on 2016/8/9.
 */
public class LauncherPresenterImpl implements LauncherPresenter {
    LauncherView launcherView;
    private Bitmap backImg=null;
    public LauncherPresenterImpl(LauncherView launcherView){
        this.launcherView=launcherView;
        new Thread(){public void run(){getBackImg();}}.start();
    }
    @Override
    public void onResume() {
        if(launcherView!=null){
            //// TODO: 2016/8/9 预定于操作,每次亮屏都执行

        }
    }

    @Override
    public void onDestory() {
        if(backImg!=null){
            backImg.recycle();
            backImg=null;
        }
        this.launcherView=null;
        Log.d("Launcher","退出了");
    }

    @Override
    public void getBackImg() {
        //网络获取背景图
        //backImg= BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.cheese_2);
        //Toast.makeText(launcherView.getActivity(),(backImg==null)+"",Toast.LENGTH_SHORT).show();
        if(backImg==null){
            mHander.postDelayed(new Runnable() {
                @Override
                public void run() {
                    backImg = BitmapFactory.decodeResource(launcherView.getActivity().getResources(), Cheeses.getRandomCheeseDrawable());
                    launcherView.setBack(backImg);
                }
            },1000);
        }
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gotoMainPage();
    }
    static Handler mHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    break;
                case 2:
                    break;
            }
        }
    };

    @Override
    public void gotoMainPage(){
        if(launcherView==null||launcherView.getActivity()==null){return;}
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(launcherView.getActivity(),MainActivity.class);
        launcherView.getActivity().startActivity(intent);
        launcherView.getActivity().finish();
    }

}
