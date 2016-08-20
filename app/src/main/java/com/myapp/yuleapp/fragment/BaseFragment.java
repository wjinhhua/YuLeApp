package com.myapp.yuleapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myapp.yuleapp.ui.application.YuLeApplication;
import com.myapp.yuleapp.view.LoadingView;

/**
 * 版权: ft626 版权所有(c) 2016
 * 作者: wjh
 * 版本: 1.0
 * 创建日期: 2016/6/28.23:11
 * 描述:
 **/
public abstract class BaseFragment extends Fragment {
    protected Context context;
    public View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = initView(inflater);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context =  YuLeApplication.getGlobalContext();
    }
    protected abstract View initView(LayoutInflater inflater);
    protected abstract void initData(Bundle savedInstanceState);

}
