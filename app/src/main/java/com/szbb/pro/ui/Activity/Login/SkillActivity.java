package com.szbb.pro.ui.activity.login;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.databinding.ItemSkillBinding;
import com.szbb.pro.entity.SkillBean;
import com.szbb.pro.impl.BinderOnItemClickListener;
import com.szbb.pro.model.SkillCheckModel;
import com.szbb.pro.tools.AppTools;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by ChanZeeBm on 2015/10/19.
 */
public class SkillActivity extends BaseAty implements BinderOnItemClickListener, Toolbar
        .OnMenuItemClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {
    private RecyclerView recyclerView;//视图
    private List<SkillBean> list;//数据源
    private ArrayList<String> resultList;//结果集
    //刷新控件
    private BGARefreshLayout bgaRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(getResources().getString(R.string.title_skill)).getToolbar
                ().setOnMenuItemClickListener(this);//默认标题栏
        recyclerView = (RecyclerView) getViewById(R.id.recyclerView);
        bgaRefreshLayout = (BGARefreshLayout) getViewById(R.id.refreshLayout);
        list = new ArrayList<>();
        resultList = new ArrayList<>();
        initAdapter();
    }


    @Override
    protected void initEvents() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(commonBinderAdapter);
        commonBinderAdapter.setBinderOnItemClickListener(this);
        //使用默认刷新样式
        AppTools.defaultRefresh(bgaRefreshLayout, this);
    }

    @Override
    protected void noNetworkStatus() {

    }

    private void initAdapter() {
        for (int i = 0; i < 30; i++) {
            SkillBean skillBean = new SkillBean();
            skillBean.setTextColor(getResources().getColor(R.color.theme_primary));
            skillBean.setRootBackground(getResources().getDrawable(R.drawable
                    .bg_cyan_frame));
            skillBean.setRootGravity(Gravity.CENTER);
            skillBean.setText("洗衣机" + i);
            list.add(skillBean);
        }
        commonBinderAdapter = new CommonBinderAdapter<SkillBean>(this, R.layout.item_skill, list) {
            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, SkillBean skillBean) {
                ItemSkillBinding itemSkillBinding = (ItemSkillBinding) viewDataBinding;
                if (skillBean.isChosen()) {
                    skillBean.setTextColor(getResources().getColor(R.color.color_white));
                    skillBean.setRootBackground(getResources().getDrawable(R.drawable
                            .bg_pure_blue));
                } else {
                    skillBean.setTextColor(getResources().getColor(R.color.theme_primary));
                    skillBean.setRootBackground(getResources().getDrawable(R.drawable
                            .bg_cyan_frame));
                }
                GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams)
                        itemSkillBinding.getRoot().getLayoutParams();
                params.setMargins(10, 30, 10, 10);
                itemSkillBinding.setSkill(skillBean);
            }
        };
    }

    @Override
    protected int getContentView() {
        return R.layout.aty_skill;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {

        }
    }

    @Override
    public void onBinderItemClick(View view, int pos) {
        if (SkillCheckModel.checkCount(list, pos)) {
            SkillBean skillBean = list.get(pos);
            skillBean.setIsChoosen(!skillBean.isChosen());
            commonBinderAdapter.notifyItemChanged(pos);
        } else {
            AppTools.showNormalSnackBar(recyclerView, "最多选择5个技能");
        }
    }

    @Override
    public void onBinderItemLongClick(View view, int pos) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        resultList.clear();
        if (item.getItemId() == R.id.menu_done) {
            for (SkillBean bean : list) {
                if (bean.isChosen())
                    resultList.add(bean.getText());
            }
        }
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("skillResult", resultList);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        AppTools.removeSingleActivity(this);
        return false;
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(final BGARefreshLayout bgaRefreshLayout) {
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(final BGARefreshLayout bgaRefreshLayout) {
        return true;
    }
}
