package com.wangpeng.beautifullife.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wangpeng.beautifullife.utils.base.HttpHelper;
import com.wangpeng.beautifullife.utils.base.VolleyListenerImpl;
import com.wangpeng.beautifullife.utils.base.VolleyRequestUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by d1yf.132 on 2016/8/12.
 */
public class MeiriyiwenUtils {
    private static final String TAG="MeiriyiwenUtils";
    private Context context;

    public MeiriyiwenUtils(Context context){
        this.context = context;
    }

    public String[] getToday(){
        String[] today = new String[3];
        String html = new HttpHelper("http://www.meiriyiwen.com").getHtml();
        //Log.d(TAG, "请求成功了，长度："+html.length());
        Document doc = Jsoup.parse(html);
        //Log.d(TAG,(doc==null?"doc为空":doc.title()));
        Elements mTitle = doc.getElementsByTag("h2");
        //Log.d(TAG,mTitle==null?"mTitle为空":"文章标题："+mTitle.text());
        today[0]=mTitle.text();
        Elements mAuthor = doc.getElementsByClass("articleAuthorName");
        //Log.d(TAG,mAuthor==null?"mTitle为空":"文章作者："+mAuthor.text());
        today[1]=mAuthor.text();
        Elements mContent = doc.getElementsByClass("articleContent");
        //Log.d(TAG,mContent==null?"mContent":"文章内容："+mContent.html());
        today[2]=mContent.html();
        //Log.d("TAG","结束请求");
        return today;
    }

    public void get(){
        String result = "";
        VolleyRequestUtil.RequestGet("http://www.meiriyiwen.com", "mryw",new VolleyListenerImpl(context, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,error.getMessage());
            }
        }));
        Log.d(TAG,"get请求已结束");
    }
}
