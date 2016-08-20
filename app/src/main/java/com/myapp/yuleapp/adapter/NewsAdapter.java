package com.myapp.yuleapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myapp.yuleapp.R;
import com.myapp.yuleapp.domain.NewsBean;
import com.myapp.yuleapp.listener.OnItemClickListener;
import com.myapp.yuleapp.utils.SharedPrefUtil;

import java.util.List;

/**
 * 版权: ft626 版权所有(c) 2016
 * 作者: wjh
 * 版本: 1.0
 * 创建日期: 2016/7/1.20:40
 * 描述:
 **/
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NewsBean.News> datas;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private Context c;
    public NewsAdapter(Context context, List<NewsBean.News> pageBeanList) {
        this.c = context;
        this.mInflater = LayoutInflater.from(context);
        this.datas = pageBeanList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // ItemViewHolder holder = new ItemViewHolder(mInflater.inflate(R.layout.recyclerview_news_item, parent, false));
        if (viewType == TYPE_ITEM) {
            View view = mInflater.inflate(R.layout.recyclerview_news_item, parent,
                    false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = mInflater.inflate(R.layout.recyclerview_footer, parent,
                    false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder holder1 = (ItemViewHolder) holder;
            String hadRead = SharedPrefUtil.getString(c, "readNews", "");
            if (hadRead.contains(datas.get(position).title)){
                holder1.tv_news_title.setTextColor(c.getResources().getColor(R.color.colorgray));
                holder1.tv_news_source.setTextColor(c.getResources().getColor(R.color.colorgray));
                holder1.tv_news_pubDate.setTextColor(c.getResources().getColor(R.color.colorgray));
            }
            holder1.tv_news_title.setText(datas.get(position).title);
            holder1.tv_news_source.setText(datas.get(position).source);
            holder1.tv_news_pubDate.setText(datas.get(position).pubDate);
            if (mOnItemClickListener != null) {
                if (!holder.itemView.hasOnClickListeners()) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos = holder.getLayoutPosition();
                            mOnItemClickListener.onItemClick(v, pos);
                        }
                    });
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            int pos = holder.getLayoutPosition();
                            mOnItemClickListener.onItemLongClick(v, pos);
                            return true;
                        }
                    });
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }
    @Override
    public int getItemCount() {
        return datas.size()==0 ? 0:datas.size()+1;
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_news_title;
        private TextView tv_news_source;
        private TextView tv_news_pubDate;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_news_title = (TextView) itemView.findViewById(R.id.tv_news_title);
            tv_news_source = (TextView) itemView.findViewById(R.id.tv_news_source);
            tv_news_pubDate = (TextView) itemView.findViewById(R.id.tv_news_pubDate);
        }
    }
    static  class FootViewHolder extends RecyclerView.ViewHolder {
        public FootViewHolder(View view) {
            super(view);
        }
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void updateData(List<NewsBean.News> newsList) {
        datas.clear();
        datas.addAll(newsList);
        notifyDataSetChanged();
    }

    public void addData(List<NewsBean.News> newsList){
        if (!newsList.get(0).title.equals(datas.get(0).title)){
            datas.addAll(newsList);
        }
        notifyDataSetChanged();
    }
}
