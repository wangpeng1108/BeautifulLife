package com.wangpeng.beautifullife.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wangpeng.beautifullife.R;

/**
 * Created by d1yf.132 on 2016/8/11.
 */
public class AboutFragment extends Fragment {
    private TextView version;
    private Toolbar toolbar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about,container,false);
        version = (TextView)view.findViewById(R.id.appversion);
        version.setText(getString(R.string.version)+getString(R.string.versionNum));
        toolbar = (Toolbar)view.findViewById(R.id.base_toolbar);
        if(toolbar!=null) {
            toolbar.setTitle(getString(R.string.menu_about));
            if(((AppCompatActivity) getActivity()).getSupportActionBar()!=null){
                ((AppCompatActivity) getActivity()).setSupportActionBar(null);
            }
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
            //toolbar.setSubtitle(getString(R.string.version)+getString(R.string.versionNum));

        }else{
            Log.d("About","About里面的toobar为空");
        }
        return view;
    }
}
