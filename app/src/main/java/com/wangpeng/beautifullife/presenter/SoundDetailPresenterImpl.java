package com.wangpeng.beautifullife.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.wangpeng.beautifullife.MyApplication;
import com.wangpeng.beautifullife.R;
import com.wangpeng.beautifullife.presenter.interfaces.SoundDetailPresenter;
import com.wangpeng.beautifullife.service.SoundService;
import com.wangpeng.beautifullife.utils.base.FastBlur;
import com.wangpeng.beautifullife.view.SoundDetailView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by d1yf.132 on 2016/8/9.
 */
public class SoundDetailPresenterImpl implements SoundDetailPresenter {
    SoundDetailView detailView;
    public SoundDetailPresenterImpl(SoundDetailView view){
        this.detailView = view;
    }
    private SoundService soundService;
    private String url;
    @Override
    public void init() {
        initData();
    }

    @Override
    public void onResume() {
        if(this.detailView!=null){
            //// TODO: 2016/8/9 处理初始化操作

        }
    }

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            soundService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            //这里我们实例化audioService,通过binder来实现
            soundService = ((SoundService.AudioBinder)binder).getService();

        }
    };

    private void initData(){
        Log.d("Detail","开始获取数据");
        setSounderImg(detailView.getBack());
        setTitle(detailView.getArticleTitle());
        StringRequest request = new StringRequest(Request.Method.GET, detailView.getUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Document document = Jsoup.parse(response);
                        url = document.select("audio").first().attr("src");
                        System.out.println(url);
                        Intent it = new Intent(Intent.ACTION_VIEW);

                        it.setData(Uri.parse(url));
                        detailView.getActivity().startActivity(it);

                        //soundService = new SoundService();
                        //soundService.initPlayer(url);
                    //System.out.println(audio);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                detailView.showSnakeBar("连接网络失败！");
            }
        });

        MyApplication.getHttpQueue().add(request);
    }

    @Override
    public void onDestory() {
        this.detailView=null;
    }

    @Override
    public void setTitle(String title) {
        detailView.getToolBar().setTitle(title);
    }

    private void blur(Bitmap bkg, View view) {
        float scaleFactor = 1;
        float radius = 20;
        scaleFactor = 8;
        radius = 2;
        if(view ==null)return;
        int width = (int) (view.getMeasuredWidth()/scaleFactor);
        int height = (int) (view.getMeasuredHeight()/scaleFactor);
        if(width<=0||height<=0)return;
        Bitmap overlay = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        //System.out.println(view.getMeasuredWidth()/scaleFactor+","+view.getMeasuredHeight()/scaleFactor);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft()/scaleFactor, -view.getTop()/scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int)radius, true);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.JELLY_BEAN)
        //view.setBackground(new BitmapDrawable(detailView.getActivity().getResources(), overlay));
        detailView.setBackGround(overlay);
    }

    @Override
    public void setSounderImg(String pic) {
        final CircleImageView imageView = (CircleImageView) detailView.getActivity().findViewById(R.id.authorimg);
        //System.out.println(pic);
        Glide.with(detailView.getActivity()).load(pic).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                blur(resource,detailView.getRelativeLayout());
                imageView.setImageBitmap(resource);
            }
        });
    }

    @Override
    public void setMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                detailView.getActivity().finish();
                return;
        }
    }
    @Override
    public void setCheckBoxChange(CompoundButton v,boolean checked) {
        if(v.getId()==R.id.play){
            System.out.println(checked);
            if(checked){
                if(url!=null){
                    System.out.println("准备播放");
                    Intent intent = new Intent();
                    intent.setClass(detailView.getActivity(), SoundService.class);
                    detailView.getActivity().startService(intent);
                    detailView.getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);

                    soundService.play();
                }
            }else{if(soundService!=null)
                soundService.pause();
            }
        }else if(v.getId()==R.id.favor){

        }
    }

    @Override
    public void setOnClick(View v) {
        if(v.getId()==R.id.next){

        }else if(v.getId()==R.id.stop){
            if(soundService!=null)
            soundService.pause();
            Intent intent = new Intent();
            intent.setClass(detailView.getActivity(), SoundService.class);
            detailView.getActivity().unbindService(conn);
            detailView.getActivity().stopService(intent);
        }
    }
}
