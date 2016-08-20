package com.myapp.yuleapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.myapp.yuleapp.R;
import com.myapp.yuleapp.domain.JoyImgBean;
import com.myapp.yuleapp.domain.JoyTxtBean;
import com.myapp.yuleapp.listener.OnItemClickListener;

import java.util.List;

/**
 * 版权: ft626 版权所有(c) 2016
 * 作者: wjh
 * 版本: 1.0
 * 创建日期: 2016/7/1.20:40
 * 描述:
 **/
public class JoyImgAdapter extends RecyclerView.Adapter<JoyImgAdapter.ItemViewHolder> {
    private List<JoyImgBean.JoyImg> datas;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private Context context;

    public JoyImgAdapter(Context context, List<JoyImgBean.JoyImg> joyImgList) {
        this.mInflater = LayoutInflater.from(context);
        this.datas = joyImgList;
        this.context = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder holder = new ItemViewHolder(mInflater.inflate(R.layout.recyclerview_joyimg_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        holder.tv_joyimg_title.setText(datas.get(position).title);
        Glide.clear(holder.iv_joyimg);
        Glide.with(context).load(datas.get(position).img).centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .crossFade()
                .into(holder.iv_joyimg);
        holder.tv_joytimg_ct.setText(datas.get(position).ct);
        if (mOnItemClickListener != null) {
            if (!holder.iv_joyimg.hasOnClickListeners()) {
                holder.iv_joyimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getPosition();
                        mOnItemClickListener.onItemClick(v, pos);
                    }
                });
                holder.iv_joyimg.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = holder.getPosition();
                        mOnItemClickListener.onItemLongClick(v, pos);
                        return true;
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_joyimg_title;
        private ImageView iv_joyimg;
        private TextView tv_joytimg_ct;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_joyimg_title = (TextView) itemView.findViewById(R.id.tv_joyimg_title);
            iv_joyimg = (ImageView) itemView.findViewById(R.id.iv_joyimg);
            tv_joytimg_ct = (TextView) itemView.findViewById(R.id.tv_joytimg_ct);
        }
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void updateData(List<JoyImgBean.JoyImg> joyImgList) {
        datas.clear();
        datas.addAll(joyImgList);
        notifyDataSetChanged();
    }

    public void addData(List<JoyImgBean.JoyImg> joyImgList){
        datas.addAll(joyImgList);
        notifyDataSetChanged();
    }

}
