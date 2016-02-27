package com.szbb.pro.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ChanZeeBm on 2015/9/19.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private View view;
    private SparseArray<View> sparseArray;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        this.view = itemView;
        sparseArray = new SparseArray<>();
    }

    public void setText(int id, String text) {
        TextView textView = getViews(id);
        textView.setText(text);
    }

    public <T extends View> T getViews(int id) {
        View v = sparseArray.get(id);
        if (v == null) {
            v = view.findViewById(id);
            sparseArray.put(id, v);
        }
        return (T) v;
    }
}
