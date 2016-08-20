package com.myapp.yuleapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.myapp.yuleapp.R;
import com.myapp.yuleapp.adapter.JoyTxtAdapter;
import com.myapp.yuleapp.adapter.ListItemDecoration;
import com.myapp.yuleapp.domain.JoyTxtBean;
import com.myapp.yuleapp.listener.OnItemClickListener;
import com.myapp.yuleapp.ui.activity.JoyTxtDetailActivity;
import com.myapp.yuleapp.utils.CommonUtil;
import com.myapp.yuleapp.utils.ConstantsUtil;
import com.myapp.yuleapp.utils.GsonUtil;
import com.myapp.yuleapp.view.LoadingView;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.CacheMode;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 版权: ft626 版权所有(c) 2016
 * 作者: wjh
 * 版本: 1.0
 * 创建日期: 2016/7/2.11:13
 * 描述:
 **/
public class JoysTxtFragment extends BaseFragment {
    private SwipeRefreshLayout srl_joy_common;
    private RecyclerView rv_joy_common;
    private RequestQueue requestQueue;
    private LoadingView lv_joy_common;
    private JoyTxtBean joyTxtBean;
    private JoyTxtAdapter adapter;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = View.inflate(getActivity(), R.layout.fragment_joys_common, null);
        init(view);
        return view;
    }

    private void init(View view) {
        srl_joy_common = (SwipeRefreshLayout) view.findViewById(R.id.srl_joy_common);
        rv_joy_common = (RecyclerView) view.findViewById(R.id.rv_joy_common);
        lv_joy_common = (LoadingView) view.findViewById(R.id.lv_joy_common);
        rv_joy_common.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_joy_common.setLayoutManager(layoutManager);
        rv_joy_common.setItemAnimator(new DefaultItemAnimator());
        rv_joy_common.addItemDecoration(new ListItemDecoration());
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        getData();
    }

    private void getData() {
        requestQueue = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest(ConstantsUtil.URL_JOYS_TXT + ConstantsUtil.URL_COMMON +
                "&time="+ CommonUtil.getDate(),RequestMethod.GET);
        request.setConnectTimeout(NoHttp.getDefaultConnectTimeout());
        request.setReadTimeout(NoHttp.getDefaultReadTimeout());
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        request.setTag(this);
        request.setCancelSign(this);
        requestQueue.add(ConstantsUtil.WHAT_JOY_TXT, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.get());
                    String result = object.getString("showapi_res_body");
                    joyTxtBean = GsonUtil.gson(result, JoyTxtBean.class);
                    initDatas(joyTxtBean.contentlist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                lv_joy_common.showErrorView();
            }

            @Override
            public void onFinish(int what) {
                lv_joy_common.setVisibility(View.INVISIBLE);
            }
        });

    }
    private void initDatas(final List<JoyTxtBean.JoyTxt> joyTxtList){
        if (adapter == null){
            adapter = new JoyTxtAdapter(getActivity(),joyTxtList);
        }else {
            adapter.addData(joyTxtList);
        }
        rv_joy_common.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.setClass(context, JoyTxtDetailActivity.class);
                intent.putExtra("title", joyTxtList.get(position).title);
                intent.putExtra("text", joyTxtList.get(position).text);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        srl_joy_common.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    @Override
    public void onDestroy() {
        if (requestQueue != null)
            requestQueue.cancelBySign(this);
        super.onDestroy();
    }
}
