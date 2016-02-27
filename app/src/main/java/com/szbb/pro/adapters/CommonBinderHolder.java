package com.szbb.pro.adapters;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.szbb.pro.impl.BinderOnItemClickListener;

/**
 * Created by ChanZeeBm on 2015/10/12.
 */
public class CommonBinderHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnLongClickListener {
    private ViewDataBinding dataBinding;
    private BinderOnItemClickListener listener;

    public CommonBinderHolder(ViewDataBinding binding, View itemView, BinderOnItemClickListener
            listener) {
        super(itemView);
        this.dataBinding = binding;
        this.listener = listener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public ViewDataBinding getBinding() {
        return dataBinding;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onBinderItemClick(v, getAdapterPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (listener != null) {
            listener.onBinderItemLongClick(v, getAdapterPosition());
        }
        return true;
    }

}
