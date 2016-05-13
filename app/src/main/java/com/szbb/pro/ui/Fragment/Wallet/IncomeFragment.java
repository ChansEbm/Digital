package com.szbb.pro.ui.fragment.wallet;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.szbb.pro.ItemIncomeLayout;
import com.szbb.pro.MainRecyclerNoneLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseFgm;
import com.szbb.pro.entity.vip.IncomeBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.ui.activity.orders.operating.OrderDetailActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

/**
 * Created by Administrator on 2/28/2016.
 */
public class IncomeFragment extends BaseFgm<IncomeBean, IncomeBean.ListEntity> {
    private MainRecyclerNoneLayout mainRecyclerLayout;
    private RecyclerView recyclerView;

    @Override
    protected void initViews() {
        mainRecyclerLayout = (MainRecyclerNoneLayout) viewDataBinding;
        recyclerView = mainRecyclerLayout.recyclerView;
        commonBinderAdapter = new CommonBinderAdapter<IncomeBean.ListEntity>(getActivity(), R
                .layout.item_income, list) {
            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, IncomeBean.ListEntity listEntity) {
                ((ItemIncomeLayout) viewDataBinding).setIncome(listEntity);
            }
        };
    }

    @Override
    protected void initEvents() {
        recyclerView.setAdapter(commonBinderAdapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .sizeResId(R.dimen.large_margin_15dp).colorResId(R.color.color_transparent).build
                        ());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        commonBinderAdapter.setBinderOnItemClickListener(this);
        networkModel.incomeList(NetworkParams.CUPCAKE);
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
    public void onJsonObjectSuccess(IncomeBean t, NetworkParams paramsCode) {
        List<IncomeBean.ListEntity> list = t.getList();
        if (list.isEmpty())
            mainRecyclerLayout.emptyView.setVisibility(View.VISIBLE);
        this.list.clear();
        this.list.addAll(list);
        commonBinderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBinderItemClick(View view, int pos) {
        final String orderId = list.get(pos).getOrderId();
        startActivity(new Intent(getActivity(), OrderDetailActivity.class).putExtra("orderId", orderId));
    }
}
