package com.wangpeng.beautifullife.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class SoundService extends Service implements MediaPlayer.OnCompletionListener{
    MediaPlayer player;
    String url;

    private final IBinder binder = new AudioBinder();
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return binder;
    }
    /**
     * 当Audio播放完的时候触发该动作
     */
    @Override
    public void onCompletion(MediaPlayer player) {
        // TODO Auto-generated method stub
        stopSelf();//结束了，则结束Service
    }

    public void onCreate(){
        super.onCreate();

    }


    public void initPlayer(String url){
        if(url.equals(this.url)){
            return;
        }
        if(player==null) {
            player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        if(player.isPlaying())
            player.stop();
        try {
            player.reset();
            player.setDataSource(url);
            player.getCurrentPosition();
        } catch (IOException e) {
            //Toast.makeText(this,"播放地址无效！",Toast.LENGTH_SHORT).show();
            System.out.println("播放地址无效");
            return;
        }
        player.setOnCompletionListener(this);
    }

    public void play(){
        if(player!=null&&!player.isPlaying()){
            try {
                player.prepare();
                //player.start();
                System.out.println("开始播放");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("播放出错");
            }
        }
    }
    public void pause(){
        if(player!=null&&!player.isPlaying())
            player.pause();
    }

    public boolean isPlaying(){
        return player.isPlaying();
    }

    public int getLength(){
        return player.getDuration();
    }

    public int getCurrent(){
        return player.getCurrentPosition();
    }

    /**
     * 该方法在SDK2.0才开始有的，替代原来的onStart方法
     */
    public int onStartCommand(Intent intent, int flags, int startId){
        if(player!=null&&!player.isPlaying()){
            player.start();
        }
        return START_STICKY;
    }

    public void onDestroy(){
        //super.onDestroy();
        if(player!=null&&player.isPlaying()){
            player.stop();
        }
        player.release();
    }

    //为了和Activity交互，我们需要定义一个Binder对象
    public class AudioBinder extends Binder{

        //返回Service对象
        public SoundService getService(){
            return SoundService.this;
        }
    }

    //后退播放进度
    public void haveFun(){
        if(player.isPlaying() && player.getCurrentPosition()>2500){
            player.seekTo(player.getCurrentPosition()-2500);
        }
    }
}
