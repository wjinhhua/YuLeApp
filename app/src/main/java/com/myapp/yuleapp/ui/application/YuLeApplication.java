package com.myapp.yuleapp.ui.application;

import android.app.Application;
import android.content.Context;

import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;

/**
 * 版权: ft626 版权所有(c) 2016
 * 作者: wjh
 * 版本: 1.0
 * 创建日期: 2016/6/30.23:44
 * 描述:
 **/
public class YuLeApplication extends Application {
    private static YuLeApplication application;
    @Override
    public void onCreate() {
        super.onCreate();
        NoHttp.initialize(this);
        application = this;
        Logger.setTag("NoHttpDebug");
        Logger.setDebug(true);
    }

    public static Context getGlobalContext(){
        return  application;
    }
}
