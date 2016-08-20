package com.myapp.yuleapp.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * 版权: ft626 版权所有(c) 2016
 * 作者: wjh
 * 版本: 1.0
 * 创建日期: 2016/7/3.15:48
 * 描述:
 **/
public class AppUtil {
    private static Stack<Activity> activityStack;
    private static AppUtil appUtil;
    public AppUtil(){}

    public static AppUtil getAppUtil() {
        if (appUtil == null) {
            appUtil = new AppUtil();
        }
        return appUtil;
    }
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

}
