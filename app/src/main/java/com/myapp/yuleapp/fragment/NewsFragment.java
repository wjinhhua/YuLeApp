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

import com.myapp.yuleapp.adapter.ListItemDecoration;
import com.myapp.yuleapp.adapter.NewsAdapter;
import com.myapp.yuleapp.R;
import com.myapp.yuleapp.domain.NewsBean;
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
 * 创建日期: 2016/6/29.22:46
 * 描述:
 **/
public class NewsFragment extends BaseFragment {
    private SwipeRefreshLayout srl_news;
    private RecyclerView rv_news;
    private RequestQueue requestQueue;
    private LoadingView lv_news;
    private NewsBean newsBean;
    private NewsAdapter adapter;
    private int page = 1;
    private boolean isLoading;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = View.inflate(getActivity(), R.layout.fragment_news, null);
        init(view);
        return view;
    }

    private void init(View view) {
        srl_news = (SwipeRefreshLayout) view.findViewById(R.id.srl_news);
        rv_news = (RecyclerView) view.findViewById(R.id.rv_news);
        lv_news = (LoadingView) view.findViewById(R.id.lv_news);
        lv_news.setClickCallBack(new LoadingView.ClickCallBack() {
            @Override
            public void onClick() {
                getData(1);
            }
        });
        rv_news.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_news.setLayoutManager(layoutManager);
        rv_news.setItemAnimator(new DefaultItemAnimator());
        rv_news.addItemDecoration(new ListItemDecoration());
        rv_news.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {
                    boolean isRefreshing = srl_news.isRefreshing();
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
        srl_news.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        Request<String> request = NoHttp.createStringRequest(ConstantsUtil.URL_NEWS + ConstantsUtil.URL_COMMON+"&page="+page, RequestMethod.GET);
        request.setConnectTimeout(NoHttp.getDefaultConnectTimeout());
        request.setReadTimeout(NoHttp.getDefaultReadTimeout());
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        request.setTag(this);
        request.setCancelSign(this);
        requestQueue.add(ConstantsUtil.WHAT_NEWS, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
                // lv_news.showLoadingView();
                isLoading = true;
                srl_news.setRefreshing(true);
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                   try {
                       JSONObject object = new JSONObject(response.get());
                       String result = object.getString("showapi_res_body");
                       newsBean = GsonUtil.gson(result, NewsBean.class);
                       initDatas(newsBean.pagebean.contentlist);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                lv_news.setVisibility(View.VISIBLE);
                lv_news.showErrorView();
            }

            @Override
            public void onFinish(int what) {
                isLoading = false;
                srl_news.setRefreshing(false);
                lv_news.setVisibility(View.INVISIBLE);
            }
        });

    }
    private void initDatas(final List<NewsBean.News> newses){
        if (adapter == null){
            adapter = new NewsAdapter(getActivity(),newses);
        }else {
            adapter.addData(newses);
        }
        rv_news.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String hadRead = SharedPrefUtil.getString(context, "readNews", "");
                String title =  newses.get(position).title;
                if (!hadRead.contains(title)) {
                    hadRead = hadRead + title + ",";
                    SharedPrefUtil.saveString(context, "readNews", hadRead);
                }
                changeReadState(view);
                Intent intent = new Intent();
                intent.setClass(context, CommonDetailActivity.class);
                intent.putExtra("url", newses.get(position).link);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }

    private void changeReadState(View v){
        TextView tv_news_title = (TextView) v.findViewById(R.id.tv_news_title);
        TextView tv_news_source = (TextView) v.findViewById(R.id.tv_news_source);
        TextView  tv_news_pubDate = (TextView) v.findViewById(R.id.tv_news_pubDate);
        tv_news_title.setTextColor(getResources().getColor(R.color.colorgray));
        tv_news_source.setTextColor(getResources().getColor(R.color.colorgray));
        tv_news_pubDate.setTextColor(getResources().getColor(R.color.colorgray));
    }

    @Override
    public void onDestroy() {
        if (requestQueue != null)
            requestQueue.cancelBySign(this);
        super.onDestroy();
    }
}
