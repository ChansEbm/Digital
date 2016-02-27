package com.szbb.pro.ui.Activity.Vip.Wallet;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.szbb.pro.PayPasswordLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;

public class PayPasswordActivity extends BaseAty<BaseBean, BaseBean> {

    private PayPasswordLayout payPasswordLayout;
    private EditText pwdEditText;
    private EditText rePwdEditText;
    private TextInputLayout pwdTextInputLayout;
    private TextInputLayout rePwdTextInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payPasswordLayout = (PayPasswordLayout) viewDataBinding;
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.title_set_withdraw_pwd);
        pwdTextInputLayout = payPasswordLayout.tInputPwd;
        rePwdTextInputLayout = payPasswordLayout.tInputPwdConfirm;

        pwdEditText = pwdTextInputLayout.getEditText();
        rePwdEditText = rePwdTextInputLayout.getEditText();
    }

    @Override
    protected void initEvents() {
        pwdTextInputLayout.setHint("请输入密码");
        rePwdTextInputLayout.setHint("请确认密码");
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
        String pwd = pwdEditText.getText().toString();
        String rePwd = rePwdEditText.getText().toString();
        if (AppTools.verifyPwd(pwdTextInputLayout, pwd, rePwd)) {
            networkModel.setPayPassword(pwd, rePwd, NetworkParams.CUPCAKE);
        }
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        Toast.makeText(PayPasswordActivity.this, baseBean.getMsg(), Toast.LENGTH_SHORT).show();
        AppTools.removeSingleActivity(this);
    }

}
