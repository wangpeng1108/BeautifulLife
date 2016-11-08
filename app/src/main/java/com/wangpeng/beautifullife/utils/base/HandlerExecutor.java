package com.wangpeng.beautifullife.utils.base;

import android.os.Handler;

import java.util.concurrent.Executor;

/**
 * Created by d1yf.132 on 2016/8/13.
 */
public class HandlerExecutor {
    private final Executor mPoster;
    public HandlerExecutor(final Handler handler){
        mPoster=new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
    }

    public HandlerExecutor(Executor executor){
        mPoster=executor;
    }

    public void post(Runnable runnable){

        //mResponsePoster.execute(runnable);
    }


}
