package com.szbb.pro.ui.Activity.Orders.FittingOrders;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.FittingApplyDetailLayout;
import com.szbb.pro.ItemFittingListDetailLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.entity.Fittings.FittingDetailBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.model.MarkPictureModel;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.widget.StatusBar;

import org.solovyev.android.views.llm.LinearLayoutManager;

import java.util.List;

/**
 * 配件申请订单详情 A模式
 */
public class FittingApplyDetailActivity extends BaseAty<BaseBean, FittingDetailBean
        .DataEntity.AcceListEntity> {
    private FittingApplyDetailLayout fittingApplyDetailLayout;
    private RecyclerView recyclerView;
    private LinearLayout workerLayout;
    private String acceId = "";
    private StatusBar statusBar;
    private FittingDetailBean detailBean = new FittingDetailBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fittingApplyDetailLayout = (FittingApplyDetailLayout) viewDataBinding;
        acceId = getIntent().getStringExtra("acceId");
    }

    @Override
    protected void initViews() {
        recyclerView = fittingApplyDetailLayout.recyclerView;
        workerLayout = fittingApplyDetailLayout.llytWorkerPic;
        statusBar = fittingApplyDetailLayout.statusBar;

        commonBinderAdapter = new CommonBinderAdapter<FittingDetailBean.DataEntity
                .AcceListEntity>(this, R.layout.item_fitting_list_fitting_detail, list) {
            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, FittingDetailBean.DataEntity.AcceListEntity acceListEntity) {
                ItemFittingListDetailLayout layout = (ItemFittingListDetailLayout) viewDataBinding;
                layout.simpleDraweeView.setImageURI(Uri.parse(acceListEntity.getAcce_thumb()));
                layout.setDetail(acceListEntity);
            }
        };
    }

    @Override
    protected void initEvents() {
        defaultTitleBar(this).setTitle(R.string.fitting_detail);
        recyclerView.setAdapter(commonBinderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(AppTools.defaultHorizontalDecoration());
        networkModel.acceDetail(acceId, NetworkParams.CUPCAKE);
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        switch (paramsCode) {
            case CUPCAKE:
                detailBean = (FittingDetailBean) baseBean;
                placeFirstGetData();
                break;
            case DONUT://代表确认签收配件
                networkModel.acceDetail(acceId, NetworkParams.CUPCAKE);//重新获取数据
                break;
        }
    }

    private void placeFirstGetData() {
        final FittingDetailBean.DataEntity data = detailBean.getData();
        changeButtonEvent(data);
        final List<FittingDetailBean.DataEntity.AcceListEntity> acce_list = data.getAcce_list();
        list.clear();
        list.addAll(acce_list);
        commonBinderAdapter.notifyDataSetChanged();

        fittingApplyDetailLayout.simpleDraweeView.setImageURI(Uri.parse(data.getFactory_logo()));
        final List<FittingDetailBean.DataEntity.AccePhotosEntity> acce_photos = detailBean
                .getData().getAcce_photos();
        workerLayout.removeAllViews();
        MarkPictureModel markPictureModel = new MarkPictureModel();
        markPictureModel.setIsNeedDeleteIcon(false);
        for (FittingDetailBean.DataEntity.AccePhotosEntity acce_photo : acce_photos) {
            markPictureModel.addSinglePictureInLinearLayoutByNetwork(this, workerLayout,
                    acce_photo.getUrl());
        }

        int count = 0;
        try {
            for (FittingDetailBean.DataEntity.AcceListEntity acceListEntity : acce_list) {
                count += Integer.valueOf(acceListEntity.getAcce_num().trim());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        data.setTotalCount(count);
        fittingApplyDetailLayout.setDetail(data);
    }

    private void changeButtonEvent(FittingDetailBean.DataEntity data) {
        String exe = data.getExe_status();
        switch (exe) {
            case "0":
                statusBar.setTextArr(R.array.fitting_not_pass);
                statusBar.setProgress(2);
                fittingApplyDetailLayout.btnEdit.setVisibility(View.VISIBLE);
                fittingApplyDetailLayout.btnSubmit.setVisibility(View.GONE);
                break;
            case "1":
                statusBar.setTextArr(R.array.fitting_nor);
                statusBar.setProgress(0);
                fittingApplyDetailLayout.btnEdit.setVisibility(View.VISIBLE);
                fittingApplyDetailLayout.btnSubmit.setVisibility(View.GONE);
                break;
            case "2":
                statusBar.setTextArr(R.array.fitting_checking);
                statusBar.setProgress(1);
                fittingApplyDetailLayout.btnSubmit.setVisibility(View.GONE);
                break;
            case "3":
                statusBar.setTextArr(R.array.fitting_sending);
                statusBar.setProgress(2);
                fittingApplyDetailLayout.btnLogistics.setVisibility(View.VISIBLE);
                fittingApplyDetailLayout.btnSubmit.setTag(AppKeyMap.CUPCAKE);//代表确认签收
                fittingApplyDetailLayout.btnSubmit.setText(R.string.sign_for);
                break;
            case "4":
                statusBar.setTextArr(R.array.fitting_receipting);
                statusBar.setProgress(3);
                fittingApplyDetailLayout.btnLogistics.setVisibility(View.VISIBLE);
                fittingApplyDetailLayout.btnSubmit.setTag(AppKeyMap.DONUT);//代表回寄配件
                fittingApplyDetailLayout.btnSubmit.setText(R.string.resend_fitting);
                break;
            case "5":
                statusBar.setTextArr(R.array.fitting_resending);
                statusBar.setProgress(4);
                fittingApplyDetailLayout.btnSubmit.setVisibility(View.GONE);
                break;
            case "6":
                statusBar.setTextArr(R.array.fitting_done);
                statusBar.setProgress(5);
                fittingApplyDetailLayout.btnSubmit.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_fitting_apply_detail;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.btn_submit:
                progressButtonEvent(view);
                break;
            case R.id.btn_logistics:
                break;
            case R.id.btn_edit:
                break;
        }
    }

    private void progressButtonEvent(View view) {
        int tag = (int) view.getTag();
        switch (tag) {
            case AppKeyMap.CUPCAKE://代表确认签收
                networkModel.signForAcce(acceId, NetworkParams.DONUT);
                break;
            case AppKeyMap.DONUT://代表回寄配件
                final Intent intent = new Intent().putParcelableArrayListExtra("list", list)
                        .putExtra("acceId", acceId)
                        .setClass(this, FittingResendByWorkerActivity.class);
                startActivity(intent);
                break;
        }
    }
}
