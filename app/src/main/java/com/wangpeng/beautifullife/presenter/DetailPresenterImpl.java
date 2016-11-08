package com.wangpeng.beautifullife.presenter;

import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.wangpeng.beautifullife.MyApplication;
import com.wangpeng.beautifullife.R;
import com.wangpeng.beautifullife.presenter.interfaces.DetailPresenter;
import com.wangpeng.beautifullife.view.DetailView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by d1yf.132 on 2016/8/9.
 */
public class DetailPresenterImpl implements DetailPresenter {
    DetailView detailView;
    //private boolean initable = true;
    public static final int SUJIN_ARTICLE=0;
    public DetailPresenterImpl(DetailView view){
        this.detailView = view;
    }

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

    private void initData(){
        switch (detailView.getType()){
            case SUJIN_ARTICLE:
                    Log.d("Detail","开始获取数据");
                    setTitle(detailView.getArticleTitle());
                StringRequest request = new StringRequest(Request.Method.GET, detailView.getUrl(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //System.out.println(response);
                                Document document = Jsoup.parse(response);
                                Element articles = document.select("div.content").first();
                                setContent(articles.html().replaceAll("<p>","\t\t\t").replaceAll("</p>","").replaceAll("<br>","\n").replaceAll("<br/>","\n"));
                                String pic = document.select("a[href$=.jpg]").first().attr("href");
                                //System.out.println("图");
                                //System.out.println(pic);
                                setBackground(pic);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                MyApplication.getHttpQueue().add(request);

                //new HttpHelper(detailView.getUrl()).getHtml();

                break;
        }
    }

    @Override
    public void onDestory() {
        this.detailView=null;
    }

    @Override
    public void setTitle(String title) {
        detailView.getToolBar().setTitle(" ");
        detailView.getmTitle().setText(title);
    }

    @Override
    public void setContent(String content) {
        if(detailView!=null&&detailView.getContent()!=null)
        detailView.getContent().setText(content.indexOf("BGM")>0?content.substring(0,content.indexOf("BGM")):content);
    }

    @Override
    public void setBackground(String pic) {
        if(detailView==null||detailView.getActivity()==null)return;
        final ImageView imageView = (ImageView) detailView.getActivity().findViewById(R.id.backdrop);
        if(imageView!=null)
        Glide.with(detailView.getActivity()).load(pic).centerCrop().into(imageView);
    }

    @Override
    public void setMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                detailView.getActivity().finish();
                return;
        }
    }
}
