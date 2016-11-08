package com.wangpeng.beautifullife.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wangpeng.beautifullife.R;
import com.wangpeng.beautifullife.presenter.interfaces.MainPresenter;
import com.wangpeng.beautifullife.presenter.MainPresenterImpl;
import com.wangpeng.beautifullife.utils.base.HttpHelper;
import com.wangpeng.beautifullife.view.MainView;

/**
 * TODO
 */
public class MainActivity extends AppCompatActivity implements MainView, View.OnClickListener {
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_id";
    private DrawerLayout mDrawerLayout;
    private MainPresenter presenter;
    private ImageView drawerBack;
    private NavigationView navigationView;
    private int currSelectedDrawerId=R.id.nav_mryw;//主界面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);//calculateStatusColor(Color.WHITE,(int)alphaValue);
            getDrawer().setClipToPadding(false);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            //getSupportActionBar().
        }else
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        presenter = new MainPresenterImpl(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        /*mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,toolbar,R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(getString(R.string.app_name));
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);*/


        if (savedInstanceState != null) {
            currSelectedDrawerId = savedInstanceState.getInt(STATE_SELECTED_POSITION);
        }
        presenter.setFragment(currSelectedDrawerId);
              new HttpHelper("http://static.meiriyiwen.com/20160816.mp3").getHtml();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.meiriyiwen_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        presenter.beforeOptionMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        presenter.setMenuClick(item);
        return super.onOptionsItemSelected(item);
    }


    public void setupDrawerContent(NavigationView navigationView) {
        presenter.initDrawerContent(navigationView);
    }

    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public DrawerLayout getDrawer() {
        if(mDrawerLayout==null){
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        }
        return mDrawerLayout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt(STATE_SELECTED_POSITION,currSelectedDrawerId);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onClick(View v) {
        presenter.allViewClick(v);
    }

    public void setDrawerSelectedId(int id){
        currSelectedDrawerId = id;
    }

    @Override
    public void setDrawerHeadImage(String img) {
        if(drawerBack==null){
            navigationView = (NavigationView)getDrawer().findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            drawerBack = (ImageView)headerView.findViewById(R.id.drawer_header_backimg);
        }
        Glide.with(this).load(img).centerCrop().into(drawerBack);
    }

    public int getCurrSelectedDrawerId(){
        return currSelectedDrawerId;
    }

    long backTime=0L;
    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            Log.d("MainActivity","关闭了左侧菜单");
            return;
        }
        long timenow = System.currentTimeMillis();
        if(timenow-backTime>2000){
            backTime=timenow;
            Snackbar.make(mDrawerLayout,"再按一次退出",Snackbar.LENGTH_SHORT).show();
        }else{
            this.finish();
        }

    }
}
