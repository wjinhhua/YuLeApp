package com.myapp.yuleapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.myapp.yuleapp.R;
import com.myapp.yuleapp.utils.AppUtil;

/**
 * 版权: ft626 版权所有(c) 2016
 * 作者: wjh
 * 版本: 1.0
 * 创建日期: 2016/7/3.14:45
 * 描述:
 **/
public class JoyTxtDetailActivity extends AppCompatActivity {
    private TextView tv_joytxt_title_detail, tv_joytxt_text_detail;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtil.getAppUtil().addActivity(this);
        setContentView(R.layout.activity_detail_joytxt);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        tv_joytxt_title_detail = (TextView) findViewById(R.id.tv_joytxt_title_detail);
        tv_joytxt_text_detail = (TextView) findViewById(R.id.tv_joytxt_text_detail);
        String title = intent.getStringExtra("title");
        String text = intent.getStringExtra("text");
        tv_joytxt_title_detail.setText(title);
        tv_joytxt_text_detail.setText(text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppUtil.getAppUtil().finishActivity(this);
    }
}
