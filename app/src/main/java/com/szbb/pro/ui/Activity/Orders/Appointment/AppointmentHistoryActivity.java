package com.szbb.pro.ui.activity.orders.appointment;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.szbb.pro.AppointmentHistoryLayout;
import com.szbb.pro.BR;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.Appointment.AppointmentHistoryItemBean;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

/**
 * 预约历史
 */
public class AppointmentHistoryActivity extends BaseAty<BaseBean, AppointmentHistoryItemBean
        .ListEntity> {
    private AppointmentHistoryLayout appointmentHistoryLayout;
    private RecyclerView recyclerView;
    private String orderId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointmentHistoryLayout = (AppointmentHistoryLayout) viewDataBinding;
        if (getIntent() == null)
            AppTools.removeSingleActivity(this);
        else
            orderId = getIntent().getStringExtra("orderId");
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.title_appointment_history);
        recyclerView = appointmentHistoryLayout.recyclerView;


        commonBinderAdapter = new CommonBinderAdapter<AppointmentHistoryItemBean.ListEntity>
                (this, R.layout
                        .item_appointment_history, list) {
            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, AppointmentHistoryItemBean.ListEntity listEntity) {
                viewDataBinding.setVariable(BR.history, listEntity);
                viewDataBinding.executePendingBindings();
            }
        };

    }

    @Override
    protected void initEvents() {
        recyclerView.setAdapter(commonBinderAdapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .sizeResId(R.dimen.large_margin_15dp).colorResId(android.R.color
                        .transparent).build());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        networkModel.appointList(orderId, NetworkParams.CUPCAKE);
    }

    @Override
    protected void noNetworkStatus() {

    }


    @Override
    protected int getContentView() {
        return R.layout.aty_appointment_history;
    }

    @Override
    protected void onClick(int id, View view) {

    }


    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        AppointmentHistoryItemBean appointmentHistoryItemBean = (AppointmentHistoryItemBean)
                baseBean;
        this.list.clear();
        this.list.addAll(appointmentHistoryItemBean.getList());
        commonBinderAdapter.notifyDataSetChanged();
    }
}
