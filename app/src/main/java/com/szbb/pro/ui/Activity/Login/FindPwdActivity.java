package com.szbb.pro.ui.Activity.Login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.szbb.pro.FindPwdLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;

/**
 * Created by ChanZeeBm on 2015/10/15.
 */
public class FindPwdActivity extends BaseAty<BaseBean,BaseBean> {
    private FindPwdLayout findPwdLayout;
    private EditText edtFindPwdUser;
    private EditText edtFindPwdVerificationCode;
    private EditText edtFindPwdPwd;
    private EditText edtFindPwdConfirmPwd;
    private Button btnGetVerificationCode;
    private Button btnSubmit;
    private CountDownTimer countDownTimer;

    private String phone = "";
    private String verificationCode = "";
    private String pwd = "";
    private String pwdConfirm = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findPwdLayout = (FindPwdLayout) viewDataBinding;
    }

    @Override
    protected void initViews() {
        edtFindPwdUser = findPwdLayout.tInputPhone.getEditText();
        edtFindPwdVerificationCode = findPwdLayout.tInputVerificationCode.getEditText();
        edtFindPwdPwd = findPwdLayout.tInputPwd.getEditText();
        edtFindPwdConfirmPwd = findPwdLayout.tInputPwdConfirm.getEditText();
        btnGetVerificationCode = findPwdLayout.btnVerificationCode;
        btnSubmit = findPwdLayout.btnSubmit;
    }

    @Override
    protected void initEvents() {
        defaultTitleBar(this).setTitle(R.string.login_find_pwd);
        findPwdLayout.tInputPhone.setHint(getString(R.string.reg_phone));
        findPwdLayout.tInputVerificationCode.setHint(getString(R.string.reg_phone_msg));
        findPwdLayout.tInputPwd.setHint(getString(R.string.reg_pwd));
        findPwdLayout.tInputPwdConfirm.setHint(getString(R.string.reg_confirm_pwd));
        btnGetVerificationCode.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    protected void noNetworkStatus() {

    }

    @Override
    protected int getContentView() {
        return R.layout.aty_find_pwd;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.btn_verification_code:
                if (checkUser()) {
                    networkModel.phoneCode(phone, null);
                    startCountDown();
                }
                break;
            case R.id.btn_submit:
                if (checkUser() && checkPwd()) {
                    networkModel.findPwd(phone, verificationCode, pwd, pwdConfirm, NetworkParams
                            .CUPCAKE);
                }
                break;
        }
    }

    private boolean checkUser() {
        phone = edtFindPwdUser.getText().toString();
        return AppTools.verifyPhone(edtFindPwdUser, phone);
    }

    private boolean checkPwd() {
        pwd = edtFindPwdPwd.getText().toString();
        pwdConfirm = edtFindPwdConfirmPwd.getText().toString();
        verificationCode = edtFindPwdVerificationCode.getText().toString();
        return AppTools.verifyPwd(edtFindPwdPwd, pwd, pwdConfirm) && AppTools.verifyVerificationCode
                (edtFindPwdVerificationCode, verificationCode);
    }

    private void startCountDown() {
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(60000, 10) {
                @Override
                public void onTick(long millisUntilFinished) {
                    btnGetVerificationCode.setEnabled(false);
                    btnGetVerificationCode.setTextColor(getResources().getColor(R.color
                            .color_text_light_gravy));
                    btnGetVerificationCode.setText((millisUntilFinished / 1000) + getResources()
                            .getString(R.string.reg_verification_code_again));
                }

                @Override
                public void onFinish() {
                    btnGetVerificationCode.setEnabled(true);
                    btnGetVerificationCode.setTextColor(getResources().getColor(R.color
                            .theme_primary));
                    btnGetVerificationCode.setText(getResources().getString(R.string
                            .reg_reCode));
                }
            };
        }
        countDownTimer.start();
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        super.onJsonObjectSuccess(baseBean, paramsCode);
        if (paramsCode != null && paramsCode == NetworkParams.CUPCAKE) {
            start(LoginActivity.class, Intent.FLAG_ACTIVITY_SINGLE_TOP, Intent
                    .FLAG_ACTIVITY_CLEAR_TOP);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null)
            countDownTimer.cancel();
        countDownTimer = null;
    }

}
