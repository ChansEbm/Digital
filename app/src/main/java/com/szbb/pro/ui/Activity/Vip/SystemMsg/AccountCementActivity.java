package com.szbb.pro.ui.Activity.Vip.SystemMsg;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.szbb.pro.ItemAccountCementLayout;
import com.szbb.pro.MainRecyclerLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.Vip.AccountCementBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.ui.Activity.Vip.WebViewActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

public class AccountCementActivity extends BaseAty<AccountCementBean, AccountCementBean
        .ListEntity> {
    private MainRecyclerLayout recyclerLayout;
    private RecyclerView recyclerView;
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerLayout = (MainRecyclerLayout) viewDataBinding;
        type = getIntent().getStringExtra("type");
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this);
        switchTitle();
        recyclerView = recyclerLayout.recyclerView;
        commonBinderAdapter = new CommonBinderAdapter<AccountCementBean.ListEntity>(this, R
                .layout.item_account_cement, list) {
            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, AccountCementBean.ListEntity listEntity) {
                ((ItemAccountCementLayout) viewDataBinding).setCement(listEntity);
            }
        };
    }


    @Override
    protected void initEvents() {
        commonBinderAdapter.setBinderOnItemClickListener(this);

        recyclerView.setAdapter(commonBinderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .sizeResId(R.dimen.large_margin_15dp).colorResId(R.color.color_transparent).build
                        ());

        networkModel.announcementList(type, NetworkParams.CUPCAKE);
    }

    private void switchTitle() {
        switch (type) {
            case "1":
                titleBarTools.setTitle(R.string.label_sys_business_msg);
                break;
            case "2":
                titleBarTools.setTitle(R.string.label_sys_guide);
                break;
            case "3":
                titleBarTools.setTitle(R.string.label_sys_activity_msg);
                break;
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.main_recycler_view;
    }

    @Override
    protected void onClick(int id, View view) {

    }

    @Override
    public void onJsonObjectSuccess(AccountCementBean accountCementBean, NetworkParams paramsCode) {
        final List<AccountCementBean.ListEntity> list = accountCementBean.getList();
        if (list.isEmpty())
            recyclerLayout.emptyView.setVisibility(View.VISIBLE);
        this.list.addAll(list);
        commonBinderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBinderItemClick(View view, int pos) {
        final AccountCementBean.ListEntity listEntity = list.get(pos);
        final String url = listEntity.getUrl();
        startActivity(new Intent().setClass(this, WebViewActivity.class).putExtra("url",
                url).putExtra("title", getString(R.string.title_detail)));
    }
}
