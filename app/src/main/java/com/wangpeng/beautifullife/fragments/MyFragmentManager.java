package com.wangpeng.beautifullife.fragments;

import android.support.v4.app.Fragment;

/**
 * Created by d1yf.132 on 2016/8/11.
 */
public enum MyFragmentManager {
    ABOUT(AboutFragment.class),CHEESELIST(CheeseFragment.class),MEIRIYIWEN(MeiriyiwenFragment.class),
    SUJIN(SujinListFragment.class),SOUND(MSoundFragment.class),LIFE(LifeFragment.class);

    final Class<? extends Fragment> fragment;

    MyFragmentManager(Class<? extends Fragment> fragment) {
        this.fragment = fragment;
    }

    public String getFragment(){
        return this.fragment.getName();
    }

    static AboutFragment aboutFragmentInstance;
    static CheeseFragment cheeseFragmentInstance;
    static MeiriyiwenFragment meiriyiwenFragmentInstance;
    static SujinListFragment sujinFragmentInstance;
    static MSoundFragment soundFragmentInstance;
    static LifeFragment lifeFragmentInstance;
    public static Fragment getInstance(MyFragmentManager fragment){
        if(fragment==ABOUT){
            if(aboutFragmentInstance==null)
                aboutFragmentInstance=new AboutFragment();
            return aboutFragmentInstance;
        }else if(fragment==MEIRIYIWEN){
            if(meiriyiwenFragmentInstance==null)
                meiriyiwenFragmentInstance=new MeiriyiwenFragment();
            return meiriyiwenFragmentInstance;
        }else if(fragment==SUJIN){
            if(sujinFragmentInstance==null)
                sujinFragmentInstance=new SujinListFragment();
            return sujinFragmentInstance;
        }else if(fragment==SOUND){
            if(soundFragmentInstance==null)
                soundFragmentInstance = new MSoundFragment();
            return soundFragmentInstance;
        }else if(fragment==LIFE){
            if(lifeFragmentInstance==null)
                lifeFragmentInstance = new LifeFragment();
            return lifeFragmentInstance;
        }else
            return aboutFragmentInstance;
    }
}
