package com.szbb.pro.tools;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.szbb.pro.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration;

/**
 * Created by Administrator on 2015/10/17.
 */
public class DefaultDecorationTools {
    public static RecyclerView.ItemDecoration defaultHorizontalDecoration(Context context) {
        return new HorizontalDividerItemDecoration.Builder(context).color(context.getResources()
                .getColor(R.color.color_bg_line)).build();
    }

    public static RecyclerView.ItemDecoration defaultVerticalDecoration(Context context) {
        return new VerticalDividerItemDecoration.Builder(context).color(context.getResources()
                .getColor(R.color.color_bg_line)).build();
    }
}
