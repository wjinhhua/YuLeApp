package com.myapp.yuleapp.view;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.myapp.yuleapp.R;
import com.myapp.yuleapp.utils.CommonUtil;
import com.myapp.yuleapp.utils.ConstantsUtil;

/**
 * 版权: ft626 版权所有(c) 2016
 * 作者: wjh
 * 版本: 1.0
 * 创建日期: 2016/6/29.20:44
 * 描述:
 **/
public class LoadingView extends FrameLayout {
    public int state = ConstantsUtil.LOADINGVIEW_STATE_UNKOWN;
    private Context ct;
    private View view_loading;
    private View view_error;
    private View view_empty;

    public LoadingView(Context context) {
        super(context);
        this.ct = context;
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ct = context;
        init();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.ct = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.ct = context;
        init();
    }

    private void init() {
        view_loading = createLoadingView();
        if (view_loading != null) {
            this.addView(view_loading, new FrameLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        view_error = createErrorView();
        if (view_error != null) {
            this.addView(view_error, new FrameLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        view_empty = createEmptyView();
        if (view_empty != null) {
            this.addView(view_empty, new FrameLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        show();
    }

    private View createEmptyView() {
        View view = View.inflate(ct, R.layout.loadingview_empty,
                null);
        return view;
    }

    private View createErrorView() {
        View view = View.inflate(ct, R.layout.loadingview_error,
                null);
        Button bt_loading_error = (Button) view.findViewById(R.id.bt_loading_error);
        bt_loading_error.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (CommonUtil.hasNetWork(ct)){
                    back.onClick();
                }else {
                    AlertDialog.Builder builder=new AlertDialog.Builder(ct);
                    builder.setTitle("网络设置提示").setMessage("网络连接不可用,是否进行设置?").setPositiveButton("设置", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Intent intent=null;
                            //判断手机系统的版本  即API大于10 就是3.0或以上版本
                            if(android.os.Build.VERSION.SDK_INT>10){
                                intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                            }else{
                                intent = new Intent();
                                ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
                                intent.setComponent(component);
                                intent.setAction("android.intent.action.VIEW");
                            }
                            ct.startActivity(intent);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                        }
                    }).show();
                }
            }
        });
        return view;
    }
    private ClickCallBack back;
    public void setClickCallBack(ClickCallBack callBack) {
        LoadingView.this.back  =callBack;
    }
    public interface ClickCallBack{
        void onClick();
    }
    private View createLoadingView() {
        View view = View.inflate(ct,
                R.layout.loadingview_loading, null);
        return view;
    }

    private void show() {
        if (view_loading != null) {
            view_loading.setVisibility(state == ConstantsUtil.LOADINGVIEW_STATE_UNKOWN
                    || state == ConstantsUtil.LOADINGVIEW_STATE_LOADING ? View.VISIBLE : View.INVISIBLE);
        }
        if (view_error != null) {
            view_error.setVisibility(state == ConstantsUtil.LOADINGVIEW_STATE_ERROR ? View.VISIBLE
                    : View.INVISIBLE);
        }
        if (view_empty != null) {
            view_empty.setVisibility(state == ConstantsUtil.LOADINGVIEW_STATE_EMPTY ? View.VISIBLE
                    : View.INVISIBLE);
        }
    }

    public void showLoadingView() {
       state = ConstantsUtil.LOADINGVIEW_STATE_LOADING;
       show();
    }

    public void showErrorView() {
        state = ConstantsUtil.LOADINGVIEW_STATE_ERROR;
        show();
    }
    public void showEmptyView() {
        state = ConstantsUtil.LOADINGVIEW_STATE_EMPTY;
        show();
    }

}
