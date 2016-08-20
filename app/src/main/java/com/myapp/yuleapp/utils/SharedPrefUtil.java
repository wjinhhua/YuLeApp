package com.myapp.yuleapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 版权: ft626 版权所有(c) 2016
 * 作者: wjh
 * 版本: 1.0
 * 创建日期: 2016/7/6.13:36
 * 描述:
 **/
public class SharedPrefUtil {
    public final static String NAME = "yule";
    private static SharedPreferences sharedPreferences;
    public static void saveString(Context context, String key, String value) {
        if (sharedPreferences == null)
            sharedPreferences= context.getSharedPreferences(NAME, 0);
        sharedPreferences.edit().putString(key, value).commit();

    }

    public static String getString(Context context, String key, String defValue) {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences(NAME, 0);
        return sharedPreferences.getString(key, defValue);
    }
}
