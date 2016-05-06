package com.szbb.pro.ui.fragment.wallet;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.szbb.pro.ItemWithDrawLayout;
import com.szbb.pro.MainRecyclerNoneLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseFgm;
import com.szbb.pro.entity.Vip.WithdrawBean;
import com.szbb.pro.eum.NetworkParams;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

/**
 * Created by Administrator on 2/28/2016.
 */
public class WithdrawFragment extends BaseFgm<WithdrawBean, WithdrawBean.ListEntity> {
    MainRecyclerNoneLayout recyclerLayout;
    private RecyclerView recyclerView;

    @Override
    protected void initViews() {
        recyclerLayout = (MainRecyclerNoneLayout) viewDataBinding;
        recyclerView = recyclerLayout.recyclerView;
        commonBinderAdapter = new CommonBinderAdapter<WithdrawBean.ListEntity>(getActivity(), R.layout.item_withdraw, list) {
            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int position, WithdrawBean.ListEntity listEntity) {
                ((ItemWithDrawLayout) viewDataBinding).setWithdraw(listEntity);
            }
        };
    }

    @Override
    protected void initEvents() {
        recyclerView.setAdapter(commonBinderAdapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).sizeResId(R.dimen.large_margin_15dp).colorResId(R.color.color_transparent).build());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        networkModel.withdrawalsList(NetworkParams.CUPCAKE);
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
    public void onJsonObjectSuccess(WithdrawBean t, NetworkParams paramsCode) {
        List<WithdrawBean.ListEntity> list = t.getList();
        if (list.isEmpty())
            recyclerLayout.emptyView.setVisibility(View.VISIBLE);
        this.list.clear();
        this.list.addAll(list);
        commonBinderAdapter.notifyDataSetChanged();

    }
}
