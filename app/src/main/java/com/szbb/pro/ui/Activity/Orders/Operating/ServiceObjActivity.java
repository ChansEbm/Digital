package com.szbb.pro.ui.activity.orders.operating;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.szbb.pro.ItemServiceObjLayout;
import com.szbb.pro.R;
import com.szbb.pro.ServiceObjLayout;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.dialog.MessageDialog;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.entity.Order.OrderDetailBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.LogTools;

public class ServiceObjActivity extends BaseAty<BaseBean, OrderDetailBean.ListEntity.ServiceListEntity> {
    private String detailId = "";
    private RecyclerView recyclerView;
    private ServiceObjLayout serviceObjLayout;
    private MessageDialog messageDialog;

    private String serviceId = "";//需要提交的serviceId
    private String serviceName = "";//需要显示出来的service名字

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceObjLayout = (ServiceObjLayout) viewDataBinding;
        if (getIntent() != null) {
            OrderDetailBean.ListEntity listEntity = getIntent().getParcelableExtra("listEntity");
            detailId = listEntity.getDetailid();
            LogTools.w("detailid:" + detailId);
            list.addAll(listEntity.getService_list());
        }
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.order_detail_service_option);
        recyclerView = serviceObjLayout.recyclerView;
        commonBinderAdapter = new CommonBinderAdapter<OrderDetailBean.ListEntity
                .ServiceListEntity>(this, R.layout.item_service_obj, list) {

            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, OrderDetailBean.ListEntity.ServiceListEntity serviceListEntity) {
                ((ItemServiceObjLayout) viewDataBinding).setService(serviceListEntity);
            }
        };
    }

    @Override
    protected void initEvents() {
        commonBinderAdapter.setBinderOnItemClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(AppTools.defaultHorizontalDecoration());
        recyclerView.setAdapter(commonBinderAdapter);
    }

    @Override
    protected void noNetworkStatus() {
        super.noNetworkStatus();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_service_obj;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.negative:
                messageDialog.dismiss();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBinderItemClick(View view, final int pos) {
        super.onBinderItemClick(view, pos);

        final OrderDetailBean.ListEntity.ServiceListEntity serviceListEntity = list.get(pos);
        messageDialog = new MessageDialog(this);
        String serviceName = serviceListEntity.getService_name();
        messageDialog.setTitle(serviceName);
        String serviceDesc = serviceListEntity.getService_desc();
        String name = "\n" + "【服务项目是为您结算费用的重要标准，在提交报告后将无法修改。为保证通过审核，请按真实情况选择。如对服务项目内容有疑问，请与我们的客服联系确认。】";
        this.serviceName = serviceName;
        messageDialog.setMessage(serviceDesc + name);
        messageDialog.setPositiveButton(getString(R.string.positive), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceObjActivity.this.serviceId = serviceListEntity.getService_id();
                networkModel.choiceFaultService(detailId, serviceId, null
                );
                messageDialog.dismiss();

            }
        });
        messageDialog.setNegativeButton(getString(R.string.negative), this);
        messageDialog.show();
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        setResult(RESULT_OK, new Intent().putExtra("serviceId", serviceId));
        setResult(RESULT_OK, new Intent().putExtra("serviceName", serviceName));
        AppTools.removeSingleActivity(ServiceObjActivity.this);
    }
}
