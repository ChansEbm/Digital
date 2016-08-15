package com.szbb.pro.ui.activity.grab;

import android.view.View;

import com.github.pwittchen.prefser.library.Prefser;
import com.szbb.pro.GrabAgreementLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.entity.grab.GrabAgreementBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;

/**
 * Created by KenChan on 16/7/11.
 */
public class GrabAgreementActivity
        extends BaseAty<GrabAgreementBean, BaseBean> {
    private GrabAgreementLayout layout;

    @Override
    protected void initViews() {
        layout = (GrabAgreementLayout) viewDataBinding;
    }

    @Override
    protected void initEvents() {
        defaultTitleBar(this).setTitle("抢单协议");
        networkModel.isReadProtocol(NetworkParams.CUPCAKE);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_agreement;
    }

    @Override
    protected void onClick(int id, View view) {
        if (id == R.id.button) {
            Prefser prefser = new Prefser(AppTools.getSharePreferences());
            prefser.put("isNeedShowGrabAgreement", !layout.checkbox.isChecked());
            AppTools.removeSingleActivity(this);
        }
    }


    @Override
    public void onJsonObjectSuccess(GrabAgreementBean grabAgreementBean, NetworkParams
            paramsCode) {
        layout.setAgree(grabAgreementBean);
    }
}
