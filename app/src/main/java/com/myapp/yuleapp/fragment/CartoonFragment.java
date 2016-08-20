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
import com.myapp.yuleapp.adapter.CartoonsAdapter;
import com.myapp.yuleapp.adapter.ListItemDecoration;
import com.myapp.yuleapp.domain.CarToonBean;
import com.myapp.yuleapp.listener.OnItemClickListener;
import com.myapp.yuleapp.ui.activity.CartoonDetailActivity;
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
 * 创建日期: 2016/6/29.22:46
 * 描述:
 **/
public class CartoonFragment extends BaseFragment {
    private SwipeRefreshLayout srl_cartoon;
    private RecyclerView rv_cartoon;
    private RequestQueue requestQueue;
    private LoadingView lv_cartoon;
    private CarToonBean carToonBean;
    private CartoonsAdapter adapter;
    private int page = 1;
    private boolean isLoading;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = View.inflate(getActivity(), R.layout.fragment_cartoon, null);
        init(view);
        return view;
    }

    private void init(View view) {
        srl_cartoon = (SwipeRefreshLayout) view.findViewById(R.id.srl_cartoon);
        rv_cartoon = (RecyclerView) view.findViewById(R.id.rv_cartoon);
        lv_cartoon = (LoadingView) view.findViewById(R.id.lv_cartoon);
        lv_cartoon.setClickCallBack(new LoadingView.ClickCallBack() {
            @Override
            public void onClick() {
                getData(1);
            }
        });
        rv_cartoon.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_cartoon.setLayoutManager(layoutManager);
        rv_cartoon.setItemAnimator(new DefaultItemAnimator());
        rv_cartoon.addItemDecoration(new ListItemDecoration());
        rv_cartoon.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {
                    boolean isRefreshing = srl_cartoon.isRefreshing();
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
        srl_cartoon.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        Request<String> request = NoHttp.createStringRequest(ConstantsUtil.URL_CARTOON_LIST + ConstantsUtil.URL_COMMON+"&page="+page, RequestMethod.GET);
        request.setConnectTimeout(NoHttp.getDefaultConnectTimeout());
        request.setReadTimeout(NoHttp.getDefaultReadTimeout());
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        request.setTag(this);
        request.setCancelSign(this);
        requestQueue.add(ConstantsUtil.WHAT_CARTOON_LIST, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
                // lv_weixin.showLoadingView();
                isLoading = true;
                srl_cartoon.setRefreshing(true);
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.get());
                    String result = object.getString("showapi_res_body");
                    carToonBean = GsonUtil.gson(result, CarToonBean.class);
                    initDatas(carToonBean.pagebean.contentlist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                lv_cartoon.setVisibility(View.VISIBLE);
                lv_cartoon.showErrorView();
            }

            @Override
            public void onFinish(int what) {
                isLoading = false;
                srl_cartoon.setRefreshing(false);
                lv_cartoon.setVisibility(View.INVISIBLE);
            }
        });

    }


    private void initDatas(final List<CarToonBean.CarToon> carToons){
        if (adapter == null){
            adapter = new CartoonsAdapter(getActivity(),carToons);
        }else {
            adapter.addData(carToons);
        }
        rv_cartoon.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String hadRead = SharedPrefUtil.getString(context, "readCartoon", "");
                String title =  carToons.get(position).title;
                if (!hadRead.contains(title)) {
                    hadRead = hadRead + title + ",";
                    SharedPrefUtil.saveString(context, "readCartoon", hadRead);
                }
               TextView tv_cartoon_title = (TextView) view.findViewById(R.id.tv_cartoon_title);
                tv_cartoon_title.setTextColor(getResources().getColor(R.color.colorgray));
                Intent intent = new Intent();
                intent.setClass(context, CartoonDetailActivity.class);
                intent.putExtra("id", carToons.get(position).id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

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
