package com.myapp.yuleapp.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 版权: ft626 版权所有(c) 2016
 * 作者: wjh
 * 版本: 1.0
 * 创建日期: 2016/7/03.10:20
 * 描述:
 **/

public class ProgressWebView extends WebView{
    private WebViewProgressBar progressBar;
    private Handler handler;
    private WebView _this;
    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressBar = new WebViewProgressBar(context);
        progressBar.setLayoutParams(new ViewGroup.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        progressBar.setVisibility(GONE);
        addView(progressBar);
        handler = new Handler();
        _this = this;
        setWebChromeClient(new MyWebChromeClient());
        setWebViewClient(new MyWebClient());

    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if(newProgress == 100){
                progressBar.setProgress(100);
                handler.postDelayed(runnable,200);
            }else if(progressBar.getVisibility() == GONE){
                progressBar.setVisibility(VISIBLE);
            }
            if(newProgress < 5){
                newProgress = 5;
            }
            progressBar.setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }
    private class MyWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            _this.loadUrl(url);
            return true;
        }
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            progressBar.setVisibility(View.GONE);
        }
    };
}