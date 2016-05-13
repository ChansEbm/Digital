package com.szbb.pro.ui.activity.expenses;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.szbb.pro.ExpensesResultLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.entity.expenses.ExpensesResultBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.model.MarkPictureModel;
import com.szbb.pro.ui.activity.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 费用申请详情页面
 */
public class ExpensesResultActivity extends BaseAty<BaseBean, BaseBean> {
    private ExpensesResultLayout resultLayout;
    private String costId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resultLayout = (ExpensesResultLayout) viewDataBinding;
        costId = getIntent().getStringExtra("costId");
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.title_expenses_apply_detail);
    }

    @Override
    protected void initEvents() {
        networkModel.costDetail(costId, NetworkParams.CUPCAKE);
    }

    @Override
    protected void noNetworkStatus() {

    }

    @Override
    protected int getContentView() {
        return R.layout.aty_expenses_result;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.button:
                applyCost();
                break;
        }

    }

    private void applyCost() {
        final ExpensesResultBean.DataEntity result = resultLayout.getResult();
        final String detailid = result.getDetailid();
        final String money = result.getMoney();
        final String cost_type = result.getCost_type();
        final String remarks = result.getRemarks();
        networkModel.applyCost(detailid, money, cost_type, remarks, new
                ArrayList<String>(), NetworkParams.DONUT);
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams
            paramsCode) {
        ExpensesResultBean resultBean = (ExpensesResultBean) baseBean;
        if (paramsCode == NetworkParams.CUPCAKE) {
            addPic(resultBean);
            resultLayout.setResult(resultBean.getData());
        } else if (paramsCode == NetworkParams.DONUT)
            startActivity(new Intent().setClass(this, MainActivity.class).addFlags(Intent
                    .FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
    }

    private void addPic(ExpensesResultBean resultBean) {
        final List<ExpensesResultBean.DataEntity.CostImgEntity> cost_img = resultBean.getData
                ().getCost_img();
        MarkPictureModel markPictureModel = new MarkPictureModel();
        for (ExpensesResultBean.DataEntity.CostImgEntity costImgEntity : cost_img) {
            markPictureModel.savePicturePath(costImgEntity.getUrl());
        }
        markPictureModel.addSinglePictureInLinearLayout(this, resultLayout.llytUploadPic, true);
    }
}
