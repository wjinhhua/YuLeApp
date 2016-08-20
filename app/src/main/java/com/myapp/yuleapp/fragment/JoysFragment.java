package com.myapp.yuleapp.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.myapp.yuleapp.R;
import com.myapp.yuleapp.utils.CommonUtil;

/**
 * 版权: ft626 版权所有(c) 2016
 * 作者: wjh
 * 版本: 1.0
 * 创建日期: 2016/6/29.22:46
 * 描述:
 **/
public class JoysFragment extends BaseFragment {
    private TabLayout tabLayout_joys;
    private ViewPager viewPage_joys;
    private String titles[] = {"文本笑话","图片笑话","动态图笑话"};
    @Override
    protected View initView(LayoutInflater inflater) {
        View view = View.inflate(getActivity(), R.layout.fragment_joys, null);
        init(view);
        return view;
    }

    private void init(View view) {
        viewPage_joys = (ViewPager) view.findViewById(R.id.viewPage_joys);
            tabLayout_joys = (TabLayout) view.findViewById(R.id.tabLayout_joys);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        initTab();
    }

    public void initTab(){
        for (int i = 0; i < titles.length; i++) {
            tabLayout_joys.addTab(tabLayout_joys.newTab().setText(titles[i]));
        }
        PagerAdapter adapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return CommonUtil.getJoy(position);
            }

            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        };
        viewPage_joys.setAdapter(adapter);
        tabLayout_joys.setupWithViewPager(viewPage_joys);
        tabLayout_joys.setTabsFromPagerAdapter(adapter);
    }

}
