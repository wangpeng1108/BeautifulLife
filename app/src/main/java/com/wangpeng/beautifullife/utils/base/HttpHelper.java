package com.wangpeng.beautifullife.utils.base;

import android.os.Environment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.*;
import com.wangpeng.beautifullife.MyApplication;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by d1yf.132 on 2016/8/12.
 */
public class HttpHelper {
    private String html=null;
    private String url=null;
    private Object[] objs;
    String path = Environment.getExternalStorageDirectory()
            .getAbsolutePath()
            + File.separator
            + "闻所未闻"
            + File.separator + "html" + File.separator+"temp.txt";

    File file = new File(path);

    private static final String TAG="TAG";
    public HttpHelper(String url,Object...paras){
        this.url=url;
        objs=paras;
    }

    public String getHtml(){
        html=null;
        Log.d(TAG,"开始获取:"+url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        html = response;
                        file.getParentFile().mkdirs();
                        try {
                            file.createNewFile();
                            FileWriter fw  = new FileWriter(file);
                            fw.write(response);
                            fw.flush();
                            fw.close();
                            Log.d(TAG,"写入到本地成功");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG,"done");

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        if(file.exists()) {
            try {
                Document document = Jsoup.parse(file, "UTF-8");
                String year = "",month="",day="";
                int monthindex= 0;
                Elements start = document.select("div.chapter-list");
                Elements articles = start.select("tr.books");
                Log.d(TAG,"共"+articles.size()+"条数据");
                for(int i=0;i<articles.size();i++){
                    System.out.println("标题："+articles.get(i).select("div.book-name").text());
                    System.out.println("图片：http://book.meiriyiwen.com"+articles.get(i).select("img").attr("src"));
                    System.out.println("链接：http://book.meiriyiwen.com"+articles.get(i).select("a").last().attr("href"));
                    System.out.println("简介："+articles.get(i).select("div.book-author").text());
                }
            }catch (Exception e){}
        }else
        MyApplication.getHttpQueue().add(stringRequest);
        Log.d(TAG,"OK");
        return html;
    }


}
