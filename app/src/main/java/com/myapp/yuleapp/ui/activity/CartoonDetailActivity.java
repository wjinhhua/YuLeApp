package com.myapp.yuleapp.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.myapp.yuleapp.R;
import com.myapp.yuleapp.domain.CarToonDetailBean;
import com.myapp.yuleapp.utils.AppUtil;
import com.myapp.yuleapp.utils.ConstantsUtil;
import com.myapp.yuleapp.utils.GsonUtil;
import com.myapp.yuleapp.view.ProgressWebView;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 版权: ft626 版权所有(c) 2016
 * 作者: wjh
 * 版本: 1.0
 * 创建日期: 2016/7/3.11:19
 * 描述:
 **/
public class CartoonDetailActivity extends AppCompatActivity {
    private String id;
    private RequestQueue requestQueue;
    private ProgressWebView wv_cartoon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtil.getAppUtil().addActivity(this);
        setContentView(R.layout.activity_detail_cartoon);
        wv_cartoon = (ProgressWebView) findViewById(R.id.wb_cartoon);
        getData();
    }

    private void getData() {
        id = getIntent().getStringExtra("id");
        requestQueue = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest(ConstantsUtil.URL_CARTOON_DETAIL + ConstantsUtil.URL_COMMON+"&id="+id, RequestMethod.GET);
        request.setConnectTimeout(NoHttp.getDefaultConnectTimeout());
        request.setReadTimeout(NoHttp.getDefaultReadTimeout());
        request.setTag(this);
        request.setCancelSign(this);
        requestQueue.add(ConstantsUtil.WHAT_CARTOON_DETIL, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.get());
                    String result = object.getString("showapi_res_body");
                    CarToonDetailBean detailBean = GsonUtil.gson(result, CarToonDetailBean.class);
                    initWebView(detailBean.img);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            }

            @Override
            public void onFinish(int what) {

            }
        });

    }

    private void loadurl(final WebView view, final String url) {
        view.loadUrl(url);
    }
    private void initWebView(String url) {
        WebSettings settings = wv_cartoon.getSettings();
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadWithOverviewMode(true);
        wv_cartoon.setWebViewClient(new WebViewClient() {
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
        loadurl(wv_cartoon, url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (wv_cartoon!= null && wv_cartoon.canGoBack())
            wv_cartoon.goBack();
    }

    @Override
    public void onDestroy() {
        if (requestQueue != null)
            requestQueue.cancelBySign(this);
        super.onDestroy();
        AppUtil.getAppUtil().finishActivity(this);
    }
}
