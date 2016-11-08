package com.wangpeng.beautifullife.presenter;

import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.wangpeng.beautifullife.R;
import com.wangpeng.beautifullife.fragments.MyFragmentManager;
import com.wangpeng.beautifullife.presenter.interfaces.MainPresenter;
import com.wangpeng.beautifullife.view.MainView;

import java.util.List;

/**
 * Created by d1yf.132 on 2016/8/9.
 */
public class MainPresenterImpl implements MainPresenter {
    private MainView mainView;

    public MainPresenterImpl(MainView mainView){
        this.mainView=mainView;
        Log.d("Launcher","主界面初始化了");
        init();
    }

    private void init() {
        //setDrawerHeaderImg("http://meiriyiwen.com/images/new_feed/bg_1.jpg");
    }

    @Override
    public void onResume() {
        if(mainView==null){
            //// TODO: 2016/8/9 初始化的一些操作 
        }
    }

    @Override
    public void onDestory() {
        mainView=null;
    }


    @Override
    public void initDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mainView.getDrawer().closeDrawers();
                        setFragment(menuItem.getItemId());
                        return true;
                    }
                });
    }

    @Override
    public void setDrawerHeaderImg(String img) {
        Log.d("MainPresenter",(mainView==null)+"");
        mainView.setDrawerHeadImage(img);
    }

    @Override
    public void setMenuClick(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mainView.getDrawer().openDrawer(GravityCompat.START);
                return;

        }
    }

    @Override
    public void beforeOptionMenu(Menu menu) {
        if(menu==null)return;
        switch (AppCompatDelegate.getDefaultNightMode()) {

        }
    }

    @Override
    public void allViewClick(View v) {

    }

    @Override
    public void setFragment(int fragmentId){
        MenuItem item = ((MenuItem)(mainView.getDrawer()).findViewById(fragmentId));
        if(item!=null)item.setChecked(true);
        List<Fragment> fragments = mainView.getActivity().getSupportFragmentManager().getFragments();
        if(fragments!=null&&fragments.size()>0){
            Log.d("Fragments Size",fragments.size()+"");
        }

        switch (fragmentId) {
            case R.id.drawer_about://关于
                if (fragments==null||fragments.size()==0||(fragments.size() > 0 && fragmentId!=mainView.getCurrSelectedDrawerId())) {
                    mainView.getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentFrame, Fragment
                                    .instantiate(mainView.getActivity(), MyFragmentManager.ABOUT.getFragment()))
                            .commit();

                }else return;
                break;
            case R.id.drawer_copyright://免责

                break;
            case R.id.nav_mryw:
                if (fragments==null||fragments.size()==0||(fragments.size() > 0 && fragmentId!=mainView.getCurrSelectedDrawerId())) {
                    mainView.getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentFrame, Fragment
                                    .instantiate(mainView.getActivity(), MyFragmentManager.MEIRIYIWEN.getFragment()))
                            .commit();
                }else return;
                break;
            case R.id.nav_sujin:
                if (fragments==null||fragments.size()==0||(fragments.size() > 0 && fragmentId!=mainView.getCurrSelectedDrawerId())) {
                    mainView.getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentFrame, Fragment
                                    .instantiate(mainView.getActivity(), MyFragmentManager.SUJIN.getFragment()))
                            .commit();
                }else return;
                break;
            case R.id.nav_sound:
                if (fragments==null||fragments.size()==0||(fragments.size() > 0 && fragmentId!=mainView.getCurrSelectedDrawerId())) {
                    mainView.getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentFrame, Fragment
                                    .instantiate(mainView.getActivity(), MyFragmentManager.SOUND.getFragment()))
                            .commit();
                }else return;
                break;
            case R.id.nav_life:
                if (fragments==null||fragments.size()==0||(fragments.size() > 0 && fragmentId!=mainView.getCurrSelectedDrawerId())) {
                    mainView.getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentFrame, Fragment
                                    .instantiate(mainView.getActivity(), MyFragmentManager.LIFE.getFragment()))
                            .commit();
                }else return;
                break;
        }
        mainView.setDrawerSelectedId(fragmentId);

    }

    private void setNightMode(@AppCompatDelegate.NightMode int nightMode) {
        AppCompatDelegate.setDefaultNightMode(nightMode);

        if (Build.VERSION.SDK_INT >= 11) {
            mainView.getActivity().recreate();
        }
    }


}
