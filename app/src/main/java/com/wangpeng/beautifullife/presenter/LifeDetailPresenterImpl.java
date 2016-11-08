package com.wangpeng.beautifullife.presenter;

import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wangpeng.beautifullife.MyApplication;
import com.wangpeng.beautifullife.presenter.interfaces.LifeDetailPresenter;
import com.wangpeng.beautifullife.view.LifeDetailView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Random;

/**
 * Created by d1yf.132 on 2016/8/18.
 */
public class LifeDetailPresenterImpl implements LifeDetailPresenter {
    private LifeDetailView lifeDetailView;
    private boolean goon=true;

    public LifeDetailPresenterImpl(LifeDetailView detailView){
        this.lifeDetailView = detailView;
    }

    @Override
    public void getData() {
        String url = lifeDetailView.getmIntent().getStringExtra("link");
        String title = lifeDetailView.getmIntent().getStringExtra("title");
        lifeDetailView.setmTitle(title);
        lifeDetailView.showProgress();
        int id = new Random().nextInt(99)+1;
        final String path = "http://meiriyiwen.com/images/new_feed/bg_"+id+".jpg";
        if(!goon)return;
        lifeDetailView.setBackground(path);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println(response);
                        Document document = Jsoup.parse(response);
                        Element articles = document.select("div.articleContent").first();
                        lifeDetailView.setContent(articles.html().replaceAll("<p>","\t\t\t").replaceAll("</p>",""));

                        lifeDetailView.hideProgress();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                lifeDetailView.setNetError();lifeDetailView.hideProgress();
            }
        });

        MyApplication.getHttpQueue().add(request);

    }

    @Override
    public void onMenuSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                lifeDetailView.getActivity().finish();
                return;
        }
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestory() {
        lifeDetailView=null;
        goon=false;
    }
}
