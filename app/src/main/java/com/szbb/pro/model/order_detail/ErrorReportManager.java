package com.szbb.pro.model.order_detail;

import android.support.annotation.NonNull;

import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.order.OrderDetailBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.OnErrorProductCallback;
import com.szbb.pro.widget.PopupWindow.ErrorProductPopupWindow;

import java.util.List;


/**
 * Created by KenChan on 16/6/17.
 */
public class ErrorReportManager
        implements OnErrorProductCallback {
    private BaseAty baseAty;

    public void showErrorPopupWindow(BaseAty appCompatActivity,
                                     OrderDetailBean.ListEntity errorListEntity) {
        this.baseAty = appCompatActivity;
        if (OrderLogicManager.isReporting(appCompatActivity,
                                          errorListEntity.getLast_handle_type(),
                                          errorListEntity.getLast_handle_status())) {
            return;
        }
        String detailId = errorListEntity.getDetailid();
        ErrorProductPopupWindow errorProductPopupWindow = new ErrorProductPopupWindow
                (appCompatActivity);
        errorProductPopupWindow.setDetailId(detailId);
        errorProductPopupWindow.setOnErrorProductCallback(this);
        errorProductPopupWindow.showAtDefaultLocation();
    }

    @Override
    public void onSubmit(@NonNull String detailId,
                         @NonNull String info,
                         @NonNull List<String> filePaths) {
        baseAty.networkModel.wrongProduct(detailId,
                                          info,
                                          filePaths,
                                          NetworkParams.GINGERBREAD);
    }
}
