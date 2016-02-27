package com.szbb.pro.tools;

import android.content.Context;
import android.support.annotation.NonNull;

import com.szbb.pro.R;

import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by ChanZeeBm on 2015/11/3.
 */
public class RefreshTools {

    //默认刷新样式
    protected static void defaultRefresh(@NonNull BGARefreshLayout bgaRefreshLayout, Context
            context, BGARefreshLayout.BGARefreshLayoutDelegate bgaRefreshLayoutDelegate) {

        //初始化Holder 分别有3种内置holder可选:BGANormalRefreshViewHolder,BGAStickinessRefreshViewHolder,
        // BGAMoocStyleRefreshViewHolder,第二个参数为是否有加载更多
        BGAMoocStyleRefreshViewHolder holder = new BGAMoocStyleRefreshViewHolder(context, true);
        //设置触发加载更多后的提醒字体
        holder.setLoadingMoreText(context.getResources().getString(R.string.refresh_loading));
        //设置默认刷新图标
        holder.setOriginalImage(R.mipmap.ic_tools);
        //设置渐变颜色
        holder.setUltimateColor(R.color.theme_primary);
        //设置holder
        bgaRefreshLayout.setRefreshViewHolder(holder);
        //设置回调监听器
        bgaRefreshLayout.setDelegate(bgaRefreshLayoutDelegate);
    }

}
