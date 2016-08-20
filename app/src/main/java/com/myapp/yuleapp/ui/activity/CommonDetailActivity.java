package com.myapp.yuleapp.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.myapp.yuleapp.R;
import com.myapp.yuleapp.utils.AppUtil;
import com.myapp.yuleapp.view.ProgressWebView;


/**
 * 版权: ft626 版权所有(c) 2016
 * 作者: wjh
 * 版本: 1.0
 * 创建日期: 2016/7/3.10:22
 * 描述:
 **/
public class CommonDetailActivity extends AppCompatActivity {
    private ProgressWebView wv_news;
    private String url;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtil.getAppUtil().addActivity(this);
        setContentView(R.layout.activity_detail_news);
        wv_news = (ProgressWebView) findViewById(R.id.wb_news);
        initData();
    }

    private void initData() {
        url = getIntent().getStringExtra("url");
        initWebView();
    }
    private void loadurl(final WebView view, final String url) {
        view.loadUrl(url);
    }
    private void initWebView() {
        WebSettings settings = wv_news.getSettings();
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadWithOverviewMode(true);
        wv_news.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                loadurl(view, url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        loadurl(wv_news, url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (wv_news!= null && wv_news.canGoBack())
            wv_news.goBack();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppUtil.getAppUtil().finishActivity(this);
    }
}
