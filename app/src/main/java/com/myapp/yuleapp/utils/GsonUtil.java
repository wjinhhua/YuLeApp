package com.myapp.yuleapp.utils;

import com.google.gson.Gson;

/**
 * 版权: ft626 版权所有(c) 2016
 * 作者: wjh
 * 版本: 1.0
 * 创建日期: 2016/6/30.23:35
 * 描述:
 **/
public class GsonUtil {
    public static <T> T gson(String result, Class<T> cls) {
        if (result != null) {
            return gsonToBean(result, cls);
        }
        return null;

    }
    private static <T> T gsonToBean(String result, Class<T> cls) {
        Gson gson = new Gson();
        T t = gson.fromJson(result, cls);
        return t;
    }

}
