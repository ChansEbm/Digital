package com.szbb.pro.ui.Activity.Orders.FittingOrders;

import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.FittingResendDetailLayout;
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
 * 回寄配件详情
 */
public class FittingResendDetailActivity extends BaseAty<BaseBean, FittingDetailBean
        .DataEntity.AcceListEntity> {
    private FittingResendDetailLayout fittingResendLayout;
    private RecyclerView recyclerView;
    private LinearLayout workerLayout;
    private String acceId = "";
    private StatusBar statusBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fittingResendLayout = (FittingResendDetailLayout) viewDataBinding;
        acceId = getIntent().getStringExtra("acceId");
    }

    @Override
    protected void initViews() {
        recyclerView = fittingResendLayout.recyclerView;
        workerLayout = fittingResendLayout.llytWorkerPic;
        statusBar = fittingResendLayout.statusBar;

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
    protected int getContentView() {
        return R.layout.activity_fitting_resend_detail;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.btn_submit:
                progressButtonEvent(view);
                break;
        }
    }

    private void progressButtonEvent(View view) {
        int tag = (int) view.getTag();
        switch (tag) {
            case AppKeyMap.CUPCAKE://means remind send
                networkModel.remindFactSend(acceId, NetworkParams.DONUT);
                break;
            case AppKeyMap.DONUT://means confirm sign for
                networkModel.signForAcce(acceId, NetworkParams.FROYO);
                break;
        }
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        switch (paramsCode) {
            case CUPCAKE:
                placeFirstGetData((FittingDetailBean) baseBean);
                break;
        }
    }

    private void placeFirstGetData(FittingDetailBean fittingDetailBean) {
        final FittingDetailBean.DataEntity data = fittingDetailBean.getData();
        changeButtonEvent(data);
        final List<FittingDetailBean.DataEntity.AcceListEntity> acce_list = data.getAcce_list();

        list.addAll(acce_list);
        commonBinderAdapter.notifyDataSetChanged();

        fittingResendLayout.simpleDraweeView.setImageURI(Uri.parse(data.getFactory_logo()));
        final List<FittingDetailBean.DataEntity.AccePhotosEntity> acce_photos = fittingDetailBean
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
        fittingResendLayout.setDetail(data);
    }

    private void changeButtonEvent(FittingDetailBean.DataEntity data) {
        String exe = data.getExe_status();
        switch (exe) {
            case "0":
                statusBar.setTextArr(R.array.fitting_not_pass);
                statusBar.setProgress(2);
                fittingResendLayout.btnLogisticsTop.setVisibility(View.GONE);
                fittingResendLayout.btnSubmit.setVisibility(View.GONE);
                break;
            case "1":
                statusBar.setTextArr(R.array.fitting_warehousing_nor);
                statusBar.setProgress(0);
                fittingResendLayout.btnLogisticsTop.setVisibility(View.GONE);
                fittingResendLayout.btnSubmit.setVisibility(View.GONE);
                break;
            case "2":
                statusBar.setTextArr(R.array.fitting_already_warehousing);
                statusBar.setProgress(1);
                fittingResendLayout.btnLogisticsTop.setVisibility(View.GONE);
                fittingResendLayout.btnSubmit.setVisibility(View.VISIBLE);
                fittingResendLayout.btnSubmit.setText(R.string.remind_send);
                fittingResendLayout.btnSubmit.setTag(AppKeyMap.CUPCAKE);//means remind send
                break;
            case "3":
                statusBar.setTextArr(R.array.fitting_already_sending);
                statusBar.setProgress(2);
                fittingResendLayout.btnLogisticsTop.setVisibility(View.VISIBLE);
                fittingResendLayout.btnSubmit.setVisibility(View.VISIBLE);
                fittingResendLayout.btnSubmit.setText(R.string.sign_for);
                fittingResendLayout.btnSubmit.setTag(AppKeyMap.DONUT);//means confirm sign for
                break;
            case "4":
                statusBar.setTextArr(R.array.fitting_already_done);
                statusBar.setProgress(3);
                fittingResendLayout.btnLogisticsTop.setVisibility(View.VISIBLE);
                fittingResendLayout.btnSubmit.setVisibility(View.GONE);
                break;
        }
    }
}
