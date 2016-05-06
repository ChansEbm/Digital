package com.szbb.pro.ui.activity.vip.Wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jungly.gridpasswordview.GridPasswordView;
import com.szbb.pro.AppKeyMap;
import com.szbb.pro.InputPayPasswordLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;

public class InputPayPasswordActivity extends BaseAty<BaseBean, BaseBean> {
    private InputPayPasswordLayout payPasswordLayout;
    private GridPasswordView gridPasswordView;
    private String password = "";
    private int flag = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payPasswordLayout = (InputPayPasswordLayout) viewDataBinding;
        flag = getIntent().getIntExtra("flag", -1);
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.title_input_pay_pwd);
        gridPasswordView = payPasswordLayout.gridPasswordView;
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_input_pay_password;
    }

    @Override
    protected void onClick(int id, View view) {
        AppTools.hideSoftInputMethod(parentView);
        switch (id) {
            case R.id.btn_confirm:
                password = gridPasswordView.getPassWord();
                if (password.length() < 6) {
                    AppTools.showNormalSnackBar(parentView, getString(R.string.please_check_pwd));
                    return;
                }
                networkModel.checkPayPassword(password, NetworkParams.CUPCAKE);
                break;
            case R.id.tv_forgot_pwd:
                start(FindCreditPasswordActivity.class);
                break;
        }
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        switch (flag) {
            case AppKeyMap.CUPCAKE://代表添加银行卡
                startActivity(new Intent().setClass(this, CreditCardActivity.class).putExtra
                        ("payPassword", password));
                break;
            case AppKeyMap.DONUT://代表提现
                startActivity(new Intent().setClass(this, WithdrawActivity.class).putExtra
                        ("payPassword", password));
                break;
        }
        AppTools.removeSingleActivity(this);
    }
}
