package com.szbb.pro.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChanZeeBm on 2015/9/19.
 */
public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {
    private List<T> list;
    private int resId;
    private Context context;

    public CommonRecyclerAdapter(Context context, int resId, List<T> list) {
        if (list == null) {
            Logger.e("list is null!!!!!");
            list = new ArrayList<>();
        }
        this.list = list;
        this.resId = resId;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RecyclerViewHolder(LayoutInflater.from(context).inflate(resId, viewGroup,
                false));
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder recyclerViewHolder, int position) {
        onBind(recyclerViewHolder, position, list.get(position));
    }

    public abstract void onBind(RecyclerViewHolder recyclerViewHolder, int position, T t);

    @Override
    public int getItemCount() {
        return list.size();
    }
}
