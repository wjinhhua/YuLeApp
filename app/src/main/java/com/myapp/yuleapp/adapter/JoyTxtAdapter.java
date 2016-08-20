package com.myapp.yuleapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myapp.yuleapp.R;
import com.myapp.yuleapp.domain.JoyTxtBean;
import com.myapp.yuleapp.domain.WeiXinBean;
import com.myapp.yuleapp.listener.OnItemClickListener;

import java.util.List;

/**
 * 版权: ft626 版权所有(c) 2016
 * 作者: wjh
 * 版本: 1.0
 * 创建日期: 2016/7/1.20:40
 * 描述:
 **/
public class JoyTxtAdapter extends RecyclerView.Adapter<JoyTxtAdapter.ItemViewHolder> {
    private List<JoyTxtBean.JoyTxt> datas;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public JoyTxtAdapter(Context context, List<JoyTxtBean.JoyTxt> joyTxtList) {
        this.mInflater = LayoutInflater.from(context);
        this.datas = joyTxtList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder holder = new ItemViewHolder(mInflater.inflate(R.layout.recyclerview_joytxt_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        holder.tv_joytxt_title.setText(datas.get(position).title);
        holder.tv_joytxt_ct.setText(datas.get(position).ct);
        if (mOnItemClickListener != null) {
            if (!holder.itemView.hasOnClickListeners()) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // int pos = holder.getPosition();
                        mOnItemClickListener.onItemClick(v, position);
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                      //  int pos = holder.getPosition();
                        mOnItemClickListener.onItemLongClick(v, position);
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
        private TextView tv_joytxt_title;
        private TextView tv_joytxt_ct;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_joytxt_title = (TextView) itemView.findViewById(R.id.tv_joytxt_title);
            tv_joytxt_ct = (TextView) itemView.findViewById(R.id.tv_joytxt_ct);
        }
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void updateData(List<JoyTxtBean.JoyTxt> joyTxtList) {
        datas.clear();
        datas.addAll(joyTxtList);
        notifyDataSetChanged();
    }

    public void addData(List<JoyTxtBean.JoyTxt> joyTxtList){
        datas.addAll(joyTxtList);
        notifyDataSetChanged();
    }
}
