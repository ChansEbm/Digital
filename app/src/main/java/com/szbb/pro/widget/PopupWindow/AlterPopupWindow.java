package com.szbb.pro.widget.PopupWindow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.szbb.pro.R;
import com.szbb.pro.databinding.PopupAlterBinding;
import com.szbb.pro.eum.AlterPopupOpts;
import com.szbb.pro.impl.OnAlterPopupWindowOptsClickListener;

/**
 * Created by ChanZeeBm on 2015/11/16.
 */
public class AlterPopupWindow extends BasePopupWindow {
    private OnAlterPopupWindowOptsClickListener listener;
    private PopupAlterBinding popupAlterBinding;

    public AlterPopupWindow(@NonNull Context context) {
        super(context);
        popupAlterBinding = (PopupAlterBinding) viewDataBinding;
        popupAlterBinding.btnAppointmentAlter.setOnClickListener(this);
        popupAlterBinding.btnAppointmentHistory.setOnClickListener(this);
        popupAlterBinding.btnAppointmentReturn.setOnClickListener(this);

    }

    public void onClick(View v) {
        if (listener != null) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_appointment_alter:
                    listener.onOptsClick(AlterPopupOpts.ALTER);//修改订单
                    break;
                case R.id.btn_appointment_return:
                    listener.onOptsClick(AlterPopupOpts.RETURN);//返回
                    break;
                case R.id.btn_appointment_history:
                    listener.onOptsClick(AlterPopupOpts.HISTORY);//预约历史
                    break;
            }
        }
        dismiss();
    }


    @Override
    public int getPopupLayout() {
        return R.layout.popup_alter;
    }


    public void setOnAlterPopupWindowOptsClickListener(OnAlterPopupWindowOptsClickListener listener) {
        this.listener = listener;
    }


}
