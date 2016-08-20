package com.myapp.yuleapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.myapp.yuleapp.R;
import com.myapp.yuleapp.adapter.ListItemDecoration;
import com.myapp.yuleapp.adapter.WeiXinsAdapter;
import com.myapp.yuleapp.domain.WeiXinBean;
import com.myapp.yuleapp.listener.OnItemClickListener;
import com.myapp.yuleapp.ui.activity.CommonDetailActivity;
import com.myapp.yuleapp.utils.ConstantsUtil;
import com.myapp.yuleapp.utils.GsonUtil;
import com.myapp.yuleapp.utils.SharedPrefUtil;
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
 * 创建日期: 2016/6/30.22:30
 * 描述:
 **/
public class WeixinFragment extends BaseFragment {
    private SwipeRefreshLayout srl_weixin;
    private RecyclerView rv_weixin;
    private RequestQueue requestQueue;
    private LoadingView lv_weixin;
    private WeiXinBean weiXinBean;
    private WeiXinsAdapter adapter;
    private int page = 1;
    private boolean isLoading;
    @Override
    protected View initView(LayoutInflater inflater) {
        View view = View.inflate(getActivity(), R.layout.fragment_weixin, null);
        init(view);
        return view;
    }

    private void init(View view) {
        srl_weixin = (SwipeRefreshLayout) view.findViewById(R.id.srl_weixin);
        rv_weixin = (RecyclerView) view.findViewById(R.id.rv_weixin);
        lv_weixin = (LoadingView) view.findViewById(R.id.lv_weixin);
        lv_weixin.setClickCallBack(new LoadingView.ClickCallBack() {
            @Override
            public void onClick() {
                getData(1);
            }
        });
        rv_weixin.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_weixin.setLayoutManager(layoutManager);
        rv_weixin.setItemAnimator(new DefaultItemAnimator());
        rv_weixin.addItemDecoration(new ListItemDecoration());
        rv_weixin.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {
                    boolean isRefreshing = srl_weixin.isRefreshing();
                    if (isRefreshing) {
                        adapter.notifyItemRemoved(adapter.getItemCount());
                        return;
                    }
                    if (!isLoading && totalItemCount <= (lastVisibleItemPosition + 1)) {
                        getData(page++);
                    }
                }
            }
        });
        srl_weixin.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(1);
            }
        });
    }
    @Override
    protected void initData(Bundle savedInstanceState) {
        getData(1);
    }
    private void getData(int page) {
        requestQueue = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest(ConstantsUtil.URL_WEIXIN + ConstantsUtil.URL_COMMON+"&page="+page+"&num=20", RequestMethod.GET);
        request.setConnectTimeout(NoHttp.getDefaultConnectTimeout());
        request.setReadTimeout(NoHttp.getDefaultReadTimeout());
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        request.setTag(this);
        request.setCancelSign(this);
        requestQueue.add(ConstantsUtil.WHAT_WEIXIN, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
               // lv_weixin.showLoadingView();
                isLoading = true;
                srl_weixin.setRefreshing(true);
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.get());
                    String result = object.getString("showapi_res_body");
                    weiXinBean = GsonUtil.gson(result, WeiXinBean.class);
                    initDatas(weiXinBean.newslist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                lv_weixin.setVisibility(View.VISIBLE);
                lv_weixin.showErrorView();
            }

            @Override
            public void onFinish(int what) {
                isLoading = false;
                srl_weixin.setRefreshing(false);
                lv_weixin.setVisibility(View.INVISIBLE);
            }
        });

    }
    private void initDatas(final List<WeiXinBean.WeiXin> weiXins){
        if (adapter == null){
            adapter = new WeiXinsAdapter(getActivity(),weiXins);
        }else {
            adapter.addData(weiXins);
        }
        rv_weixin.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String hadRead = SharedPrefUtil.getString(context, "readWeiXin", "");
                String title =  weiXins.get(position).title;
                if (!hadRead.contains(title)) {
                    hadRead = hadRead + title + ",";
                    SharedPrefUtil.saveString(context, "readWeiXin", hadRead);
                }
                changeReadState(view);
                Intent intent = new Intent();
                intent.setClass(context, CommonDetailActivity.class);
                intent.putExtra("url", weiXins.get(position).url);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }
    private void changeReadState(View v){
        TextView tv_weixin_title = (TextView) v.findViewById(R.id.tv_weixin_title);
        TextView tv_weixin_description = (TextView) v.findViewById(R.id.tv_weixin_description);
        TextView  tv_weixin_ctime = (TextView) v.findViewById(R.id.tv_weixin_ctime);
        tv_weixin_title.setTextColor(getResources().getColor(R.color.colorgray));
        tv_weixin_description.setTextColor(getResources().getColor(R.color.colorgray));
        tv_weixin_ctime.setTextColor(getResources().getColor(R.color.colorgray));
    }

    @Override
    public void onDestroy() {
        if (requestQueue != null)
            requestQueue.cancelBySign(this);
        super.onDestroy();
    }
}
