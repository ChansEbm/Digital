package com.szbb.pro.ui.Activity.Vip.Wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jungly.gridpasswordview.GridPasswordView;
import com.szbb.pro.PayPasswordLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;

public class PayPasswordActivity extends BaseAty<BaseBean, BaseBean> {

    private PayPasswordLayout payPasswordLayout;
    private GridPasswordView gdvPwd;
    private GridPasswordView gdvRePwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payPasswordLayout = (PayPasswordLayout) viewDataBinding;
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.title_set_withdraw_pwd);
        gdvPwd = payPasswordLayout.gdvPwd;
        gdvRePwd = payPasswordLayout.gdvRePwd;
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_pay_password;
    }

    @Override
    protected void onClick(int id, View view) {
        progressInfo();
    }

    private void progressInfo() {
        String pwd = gdvPwd.getPassWord();
        String rePwd = gdvRePwd.getPassWord();
        if (AppTools.verifyPwd(parentView, pwd, rePwd)) {
            networkModel.setPayPassword(pwd, rePwd, NetworkParams.CUPCAKE);
        }
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        Toast.makeText(PayPasswordActivity.this, baseBean.getMsg(), Toast.LENGTH_SHORT).show();
        start(WalletActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent
                .FLAG_ACTIVITY_SINGLE_TOP);
    }

}
