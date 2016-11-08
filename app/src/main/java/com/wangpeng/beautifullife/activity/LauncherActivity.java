package com.wangpeng.beautifullife.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wangpeng.beautifullife.R;
import com.wangpeng.beautifullife.fragments.Cheeses;
import com.wangpeng.beautifullife.presenter.LauncherPresenterImpl;
import com.wangpeng.beautifullife.presenter.interfaces.LauncherPresenter;
import com.wangpeng.beautifullife.utils.base.MySharedPreferences;
import com.wangpeng.beautifullife.view.LauncherView;

/**
 * Created by d1yf.132 on 2016/8/9.
 */
public class LauncherActivity extends AppCompatActivity implements LauncherView{
    private ImageView launcherBack;
    private LauncherPresenter presenter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_launcher);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        presenter = new LauncherPresenterImpl(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestory();
        super.onDestroy();
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public ImageView getBack() {
        if(launcherBack==null)
            launcherBack=(ImageView)findViewById(R.id.launcher_back);
        return launcherBack;
    }

    @Override
    public void setBack(Bitmap bitmap){
        //(launcherBack=(launcherBack==null)?getBack():launcherBack).setImageBitmap(bitmap);
        String path = MySharedPreferences.getString(this,"lastLauncheredPic");
        if(path!=null){
            System.out.println(path);
            Glide.with(this).load(path).centerCrop().into(launcherBack = (launcherBack == null) ? getBack() : launcherBack);
        }else{
            Glide.with(this).load(Cheeses.getRandomCheeseDrawable()).centerCrop().into(launcherBack = (launcherBack == null) ? getBack() : launcherBack);
        }
    }
}
