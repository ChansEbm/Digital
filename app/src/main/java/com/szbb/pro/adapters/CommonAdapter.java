package com.szbb.pro.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by ChanZeeBm on 2015/9/8.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    private List<T> list;
    private Context context;
    private int resId;

    public CommonAdapter(List<T> list, Context context, int resId) {
        this.list = list;
        this.context = context;
        this.resId = resId;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public T getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.get(i).hashCode();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = getViewHolder(view, viewGroup, position);
        convert(viewHolder.getPosition(),viewHolder,getItem(position));
        return viewHolder.getConvertView();
    }

    public abstract void convert(int position,ViewHolder holder,T t);

    private ViewHolder getViewHolder( View convertView, ViewGroup viewGroup, int position) {
        return ViewHolder.get(context, convertView, viewGroup, resId, position);
    }
}
