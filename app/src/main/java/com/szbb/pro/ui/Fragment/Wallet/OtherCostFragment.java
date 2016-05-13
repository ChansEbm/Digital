package com.szbb.pro.ui.fragment.wallet;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.szbb.pro.ItemOtherCostLayout;
import com.szbb.pro.MainRecyclerNoneLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseFgm;
import com.szbb.pro.entity.vip.OtherCostBean;
import com.szbb.pro.eum.NetworkParams;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

/**
 * Created by Administrator on 2/28/2016.
 */
public class OtherCostFragment extends BaseFgm<OtherCostBean, OtherCostBean.ListEntity> {
    private MainRecyclerNoneLayout recyclerLayout;
    private RecyclerView recyclerView;

    @Override
    protected void initViews() {
        recyclerLayout = (MainRecyclerNoneLayout) viewDataBinding;
        recyclerView = recyclerLayout.recyclerView;
        commonBinderAdapter = new CommonBinderAdapter<OtherCostBean.ListEntity>(getActivity(), R.layout.item_other_cost, list) {
            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int position, OtherCostBean.ListEntity listEntity) {
                ((ItemOtherCostLayout) viewDataBinding).setCost(listEntity);
            }
        };
    }

    @Override
    protected void initEvents() {
        recyclerView.setAdapter(commonBinderAdapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).sizeResId(R.dimen.large_margin_15dp).colorResId(R.color.color_transparent).build());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        networkModel.otherCostList(NetworkParams.CUPCAKE);
    }

    @Override
    protected void noNetworkStatus() {

    }

    @Override
    protected void onClick(int id, View view) {

    }

    @Override
    protected int getContentView() {
        return R.layout.main_recycler_view_none_titlebar;
    }

    @Override
    public void onJsonObjectSuccess(OtherCostBean t, NetworkParams paramsCode) {
        List<OtherCostBean.ListEntity> list = t.getList();
        if (list.isEmpty())
            recyclerLayout.emptyView.setVisibility(View.VISIBLE);
        this.list.clear();
        this.list.addAll(list);
        commonBinderAdapter.notifyDataSetChanged();
    }
}
