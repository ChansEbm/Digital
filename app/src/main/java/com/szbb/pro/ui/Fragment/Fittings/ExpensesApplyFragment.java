package com.szbb.pro.ui.fragment.fittings;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.szbb.pro.FittingOrderLayout;
import com.szbb.pro.ItemFgmFittingCostLayout;
import com.szbb.pro.ItemFittingOrderLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseFgm;
import com.szbb.pro.drawerlayouttoggles.ContentDisplaceDrawerToggle;
import com.szbb.pro.entity.eventbus.FittingNavEvent;
import com.szbb.pro.entity.fittings.FittingCostBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.ui.activity.expenses.ExpensesResultActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import de.greenrobot.event.EventBus;

/**
 * Created by ChanZeeBm on 2015/11/2.
 */
public class ExpensesApplyFragment extends BaseFgm<FittingCostBean, FittingCostBean
        .ListEntity> implements BGARefreshLayout
        .BGARefreshLayoutDelegate {
    private FittingOrderLayout fittingOrderLayout;
    private RecyclerView refreshRecyclerView;
    private RecyclerView menuRecyclerView;
    private BGARefreshLayout refreshLayout;
    private DrawerLayout drawerLayout;
    private CommonBinderAdapter<String> menuAdapter;
    private FittingCostBean fittingCostBean;
    private boolean isFirst = true;


    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initViews () {
        fittingOrderLayout = (FittingOrderLayout) viewDataBinding;
        refreshRecyclerView = fittingOrderLayout.include.recyclerView;
        refreshLayout = fittingOrderLayout.include.refreshLayout;
        menuRecyclerView = fittingOrderLayout.rvMenu;
        drawerLayout = fittingOrderLayout.drawerLayout;

        initMenuAdapter();
        initContentAdapter();
    }


    @Override
    protected void initEvents () {
        AppTools.defaultRefresh(refreshLayout, this);
        menuAdapter.setBinderOnItemClickListener(this);
        commonBinderAdapter.setBinderOnItemClickListener(this);

        menuRecyclerView.setAdapter(menuAdapter);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        menuRecyclerView.addItemDecoration(AppTools.defaultHorizontalDecoration());

        initRefreshRecyclerView();

        drawerLayout.setDrawerListener(new DisplaceDrawerToggle());
        drawerLayout.setScrimColor(Color.TRANSPARENT);

        networkModel.costList("0", "", "", NetworkParams.CUPCAKE);
    }

    @Override
    protected void noNetworkStatus () {
        refreshLayout.endRefreshing();
        refreshLayout.endLoadingMore();
    }

    @Override
    protected void onClick (int id, View view) {

    }

    private void initRefreshRecyclerView () {
        refreshRecyclerView.setAdapter(commonBinderAdapter);
        refreshRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder
                                                      (getContext())
                                                      .sizeResId(R.dimen.large_margin_15dp)
                                                      .color(getResources().getColor
                                                              (R.color.color_bg_gravy))
                                                      .build());
    }


    private void initContentAdapter () {
        commonBinderAdapter = new CommonBinderAdapter<FittingCostBean.ListEntity>(getContext()
                , R.layout.item_fgm_fitting_cost, list) {
            @Override
            public void onBind (ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, FittingCostBean.ListEntity listEntity) {
                ((ItemFgmFittingCostLayout) viewDataBinding).setCost(listEntity);
            }
        };
    }

    private void initMenuAdapter () {
        menuAdapter = new CommonBinderAdapter<String>(getActivity(), R.layout.item_fitting_order,
                                                      getResources
                                                              ().getStringArray(
                                                              R.array.expense_apply)) {
            @Override
            public void onBind (ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, String s) {
                ItemFittingOrderLayout itemFittingOrderLayout = (ItemFittingOrderLayout)
                        viewDataBinding;
                if (position == 0 && isFirst) {
                    itemFittingOrderLayout.textView.setTextColor(getResources().getColor(R.color
                                                                                                 .theme_primary));
                    isFirst = false;
                }
                itemFittingOrderLayout.textView.setText(s);
            }
        };


    }

    public void onEvent (FittingNavEvent fittingNavEvent) {
        if (fittingNavEvent.getCurrentPos() == 1) {
            drawerToggle();
        } else {
            drawerLayout.closeDrawer(menuRecyclerView);
        }
    }

    private void drawerToggle () {
        if (drawerLayout.isDrawerOpen(menuRecyclerView)) {
            drawerLayout.closeDrawer(menuRecyclerView);
        } else {
            drawerLayout.openDrawer(menuRecyclerView);
        }
    }

    @Override
    protected int getContentView () {
        return R.layout.fragment_fitting_order;
    }

    @Override
    public void onBinderItemClick (View view, int pos) {
        View v = (View) view.getParent();
        switch (v.getId()) {
            case R.id.rv_menu:
                switchMenuPos(pos);
                resetAllTextColor();
                setChosenColor(view);
                break;
            case R.id.recyclerView:
                switchContent(pos);
                break;
        }
    }

    private void setChosenColor (View view) {
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setTextColor(getResources().getColor(R.color.theme_primary));
    }

    private void resetAllTextColor () {
        for (int i = 0;
             i < menuRecyclerView.getChildCount();
             i++) {
            final TextView textView = (TextView) menuRecyclerView.getChildAt(i)
                                                                 .findViewById(R.id
                                                                                       .textView);
            textView.setTextColor(getResources().getColor(R.color.color_text_dark));
        }
    }

    private void switchContent (int pos) {
        final String costid = list.get(pos).getCostid();
        startActivity(new Intent().setClass(getActivity(), ExpensesResultActivity.class).putExtra
                ("costId", costid));
    }

    private void switchMenuPos (int pos) {
        switch (pos) {
            case 0:
                networkModel.costList("0", "", "", NetworkParams.CUPCAKE);
                break;
            case 1:
                networkModel.costList("1", "", "", NetworkParams.CUPCAKE);
                break;
            case 2:
                networkModel.costList("2", "", "", NetworkParams.CUPCAKE);
                break;
            case 3:
                networkModel.costList("3", "", "", NetworkParams.CUPCAKE);
                break;

        }
    }

    @Override
    public void onJsonObjectSuccess (FittingCostBean t, NetworkParams paramsCode) {
        super.onJsonObjectSuccess(t, paramsCode);
        this.fittingCostBean = t;
        if (paramsCode == NetworkParams.CUPCAKE) {
            list.clear();
        }
        list.addAll(t.getList());
        if (list.isEmpty()) {
            fittingOrderLayout.include.emptyView.setVisibility(View.VISIBLE);
        }
        commonBinderAdapter.notifyDataSetChanged();
        refreshLayout.endLoadingMore();
        refreshLayout.endRefreshing();
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing (BGARefreshLayout bgaRefreshLayout) {
        final int status = fittingCostBean.getCond().getStatus();
        networkModel.costList(status + "", "", "", NetworkParams.CUPCAKE);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore (BGARefreshLayout bgaRefreshLayout) {
        if (fittingCostBean.getIsNext() == 1) {
            final int page = fittingCostBean.getPage();
            final int status = fittingCostBean.getCond().getStatus();
            networkModel.costList(status + "", (page + 1) + "", "", NetworkParams.DONUT);
        } else {
            AppTools.showNormalSnackBar(parentView, getString(R.string.no_more));
            return false;
        }
        return true;
    }

    class DisplaceDrawerToggle extends ContentDisplaceDrawerToggle {
        public DisplaceDrawerToggle () {
            super(getActivity(), drawerLayout, fittingOrderLayout.flytContant, GravityCompat.START);
        }

        @Override
        public void onDrawerOpened (View arg0) {
            super.onDrawerOpened(arg0);
            menuRecyclerView.bringToFront();
            drawerLayout.requestLayout();
        }
    }
}
