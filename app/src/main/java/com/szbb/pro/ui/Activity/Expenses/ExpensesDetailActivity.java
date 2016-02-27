package com.szbb.pro.ui.Activity.Expenses;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.szbb.pro.ExpensesDetailLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonExpandableAdapter;
import com.szbb.pro.adapters.ViewHolder;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.entity.Expenses.ExpenseDetailBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.ArrayListViewTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChanZeeBm on 2015/11/14.
 */
public class ExpensesDetailActivity extends BaseAty<ExpenseDetailBean, BaseBean> {
    private ExpensesDetailLayout expensesDetailLayout;

    private ExpandableListView expandableListView;
    private CommonExpandableAdapter<ExpenseDetailBean.ListEntity, ExpenseDetailBean.ListEntity
            .CateListEntity> commonExpandableAdapter;
    private List<ExpenseDetailBean.ListEntity> parentList = new ArrayList<>();
    private List<List<ExpenseDetailBean.ListEntity.CateListEntity>> childList = new ArrayList<>();
    private String orderId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        expensesDetailLayout = (ExpensesDetailLayout) viewDataBinding;
        orderId = getIntent().getStringExtra("orderId");
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.title_expenses_detail);
        expandableListView = expensesDetailLayout.expandableListView;
        commonExpandableAdapter = new CommonExpandableAdapter<ExpenseDetailBean.ListEntity,
                ExpenseDetailBean.ListEntity.CateListEntity>(parentList, childList, this, R
                .layout.item_expenses_detail_parent, R.layout.item_expenses_detail_child) {

            @Override
            public void group(ViewDataBinding viewDataBinding, ViewHolder viewHolder,
                              ExpenseDetailBean.ListEntity listEntity) {
                viewHolder.setText(R.id.textView, listEntity.getCate_name());
            }

            @Override
            public void child(ViewDataBinding viewDataBinding, ViewHolder viewHolder,
                              ExpenseDetailBean.ListEntity.CateListEntity t) {
                viewHolder.setText(R.id.tv_name, t.getCost_name());
                viewHolder.setText(R.id.tv_cost, t.getCost_money());

            }
        };
    }

    @Override
    protected void initEvents() {
        expandableListView.setAdapter(commonExpandableAdapter);
        networkModel.orderSettlement(orderId, NetworkParams.CUPCAKE);
        commonExpandableAdapter.setGroupClickable(true);
        expandableListView.setGroupIndicator(null);
        expandableListView.setChildDivider(null);
        expandableListView.setChildDivider(getResources().getDrawable(R.color.color_transparent));
        expandableListView.setDivider(getResources().getDrawable(R.color.color_transparent));
        expandableListView.setDividerHeight(0);
    }

    private void expand() {
        ArrayListViewTools.measureAbsListViewHeight(expandableListView);
        ArrayListViewTools.expandGroup(expandableListView, true);
    }

    @Override
    protected void noNetworkStatus() {

    }

    @Override
    protected int getContentView() {
        return R.layout.aty_expenses_detail;
    }

    @Override
    protected void onClick(int id, View view) {

    }

    @Override
    public void onJsonObjectSuccess(ExpenseDetailBean expenseDetailBean, NetworkParams paramsCode) {
        super.onJsonObjectSuccess(expenseDetailBean, paramsCode);
        final List<ExpenseDetailBean.ListEntity> list = expenseDetailBean.getList();
        this.parentList.addAll(list);
        for (ExpenseDetailBean.ListEntity listEntity : list) {
            this.childList.add(listEntity.getCate_list());
        }
        commonExpandableAdapter.notifyDataSetChanged();
        expand();
    }

}
