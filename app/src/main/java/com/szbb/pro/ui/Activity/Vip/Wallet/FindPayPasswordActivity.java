package com.szbb.pro.ui.activity.vip.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.jungly.gridpasswordview.GridPasswordView;
import com.szbb.pro.FindCreditPasswordLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.ViewUtils;

public class FindPayPasswordActivity extends BaseAty<BaseBean, BaseBean> {
    private FindCreditPasswordLayout findCreditPasswordLayout;
    private TextInputLayout tInputVerifyCode;

    private EditText edtVerifyCode;
    private EditText edtPhone;
    private GridPasswordView gpvPayPassword;
    private GridPasswordView gpvRePayPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findCreditPasswordLayout = (FindCreditPasswordLayout) viewDataBinding;
    }

    @Override
    protected void initViews() {
        tInputVerifyCode = findCreditPasswordLayout.tInputVerifyCode;
        gpvPayPassword = findCreditPasswordLayout.gpvPayPassword;
        gpvRePayPassword = findCreditPasswordLayout.gpvRepayPassword;
        edtVerifyCode = tInputVerifyCode.getEditText();
        edtPhone = findCreditPasswordLayout.edtPhoneNum;
    }

    @Override
    protected void initEvents() {
        defaultTitleBar(this).setTitle(R.string.login_find_pwd);
        tInputVerifyCode.setHint(getString(R.string.please_input_verification_code));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_find_credit_password;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.btn_verify_code:
                String phone = edtPhone.getText().toString();
                if (AppTools.verifyPhone(parentView, phone)) {
                    networkModel.getPhoneCode(phone, NetworkParams.CUPCAKE);
                }
                break;
            case R.id.btn_submit:
                checkNecessaryAndProgress();
                break;
        }
    }

    private void checkNecessaryAndProgress() {
        String phone = edtPhone.getText().toString();
        if (!AppTools.verifyPhone(parentView, phone)) {
            return;
        }
        String verifyCode = edtVerifyCode.getText().toString();
        if (!AppTools.verifyVerificationCode(parentView, verifyCode)) {
            return;
        }
        String pwd = gpvPayPassword.getPassWord();
        String rePwd = gpvRePayPassword.getPassWord();
        if (!AppTools.verifyPwd(parentView, pwd, rePwd)) {
            return;
        }
        networkModel.resetPayPassword(phone, verifyCode, pwd, rePwd, NetworkParams.DONUT);
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        if (paramsCode == NetworkParams.CUPCAKE) {
            ViewUtils.startCountDown(findCreditPasswordLayout.btnVerifyCode, "后重新获取验证码", "重新获取", 60000);
        } else if (paramsCode == NetworkParams.DONUT) {
            start(WalletActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent
                    .FLAG_ACTIVITY_SINGLE_TOP);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewUtils.endCountDown();
    }
}
