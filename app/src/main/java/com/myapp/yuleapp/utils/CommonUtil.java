package com.myapp.yuleapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

import com.myapp.yuleapp.fragment.CartoonFragment;
import com.myapp.yuleapp.fragment.JoysDImgFragment;
import com.myapp.yuleapp.fragment.JoysFragment;
import com.myapp.yuleapp.fragment.JoysImgFragment;
import com.myapp.yuleapp.fragment.JoysTxtFragment;
import com.myapp.yuleapp.fragment.NewsFragment;
import com.myapp.yuleapp.fragment.WeixinFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * 版权: ft626 版权所有(c) 2016
 * 作者: wjh
 * 版本: 1.0
 * 创建日期: 2016/6/21.21:37
 * 描述:
 **/
public class CommonUtil {
    private static HashMap<Integer, Fragment> mFragments = new HashMap<>();
    private static HashMap<Integer, Fragment> mJoys = new HashMap<>();
    public static Fragment getFragment(int position) {
        Fragment fragment = null;
        fragment = mFragments.get(position);
        if (fragment == null) {
            if (position == 0) {
                fragment = new NewsFragment();
            } else if (position == 1) {
                fragment = new JoysFragment();
            } else if (position == 2) {
                fragment = new CartoonFragment();
            } else if (position ==3) {
                fragment = new WeixinFragment();
            }
            if (fragment != null) {
                mFragments.put(position, fragment);
            }
        }
        return fragment;
    }
    public static Fragment getJoy(int position) {
        Fragment fragment = null;
        fragment = mJoys.get(position);
        if (fragment == null) {
            if (position == 0) {
                fragment = new JoysTxtFragment();
            } else if (position == 1) {
                fragment = new JoysImgFragment();
            } else if (position == 2) {
                fragment = new JoysDImgFragment();
            }
            if (fragment != null) {
                mJoys.put(position, fragment);
            }
        }
        return fragment;
    }

    public static String getDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    private void autoRefresh(final SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        }, 600);
    }

    public static int getNetworkState(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return 0;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        NetworkInfo netWorkInfo = info[i];
                        if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            return 1;
                        } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            String extraInfo = netWorkInfo.getExtraInfo();
                            if ("cmwap".equalsIgnoreCase(extraInfo) || "cmwap:gsm".equalsIgnoreCase(extraInfo)) {
                                return 2;
                            }
                            return 3;
                        }
                    }
                }
            }
        }
        return 0;
    }
    public static boolean hasNetWork(Context context){
         return getNetworkState(context) == 0 ? false : true;
    }
}
