package com.wangpeng.beautifullife.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wangpeng.beautifullife.R;

import java.util.ArrayList;
import java.util.List;

public class CheeseFragment extends Fragment implements View.OnClickListener{
    FloatingActionButton fab;
    TabLayout tabLayout;
    ViewPager viewPager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.include_list_viewpager,container,false);
        initViews(view);
        return view;
    }

    private void initViews(View root){
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        final ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(ab!=null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        viewPager = (ViewPager) root.findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        fab = (FloatingActionButton) root.findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(this);
        }

        tabLayout = (TabLayout) root.findViewById(R.id.tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }
    }
    private void setupViewPager(ViewPager viewPager){
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new CheeseListFragment(), "第一页");
        adapter.addFragment(new CheeseListFragment(), "第二页");
        //adapter.addFragment(new CheeseListFragment(), "第三页");
        adapter.notifyDataSetChanged();
        Log.d("CheeseFragment","Add");
        viewPager.setAdapter(adapter);
    }



    @Override
    public void onClick(View v) {
        Snackbar.make(v, "这是一个SnakeBar，很有趣", Snackbar.LENGTH_LONG).setAction("关闭", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("这是一个弹窗")
                        .setMessage("你永远叫不醒一个装睡的人，但是我可以\n\t\t\t\t\t\t\t\t--快递小哥")
                        .setNegativeButton("取消",null)
                        .setPositiveButton("确定",null).show();
            }
        }).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
