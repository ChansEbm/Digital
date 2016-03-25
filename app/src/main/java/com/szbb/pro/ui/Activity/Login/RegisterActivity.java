package com.szbb.pro.ui.Activity.Login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.R;
import com.szbb.pro.RegisterLayout;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.entity.Login.AuthBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;

/**
 * Created by ChanZeeBm on 2015/10/14.
 */
//注册
public class RegisterActivity extends BaseAty<BaseBean, BaseBean> implements CompoundButton
        .OnCheckedChangeListener {

    private RegisterLayout registerLayout;

    private CheckBox cbProtocol;//同意霸王条款
    private TextView tvProtocol;//霸王条款
    private Button btnVerification;//获取验证码
    private Button btnSubmit;//提交
    private CountDownTimer countDownTimer;//计时器

    private TextInputLayout tInputPhone;
    private TextInputLayout tInputVerificationCode;
    private TextInputLayout tInputPwd;
    private TextInputLayout tInputPwdConfirm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerLayout = (RegisterLayout) viewDataBinding;
    }

    @Override
    protected void initViews() {
        tInputPhone = registerLayout.tInputPhone;
        tInputVerificationCode = registerLayout.tInputVerificationCode;
        btnVerification = registerLayout.btnVerificationCode;
        tInputPwd = registerLayout.tInputPwd;
        tInputPwdConfirm = registerLayout.tInputPwdConfirm;

        cbProtocol = registerLayout.cbProtocol;
        tvProtocol = registerLayout.tvProtocol;
        btnSubmit = registerLayout.btnSubmit;
        defaultTitleBar(this).setTitle(getResources().getString(R.string.register));
    }

    @Override
    protected void initEvents() {
        cbProtocol.setOnCheckedChangeListener(this);
        btnVerification.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        tvProtocol.setOnClickListener(this);

        tInputPhone.setHint(getResources().getString(R.string.reg_phone));
        tInputVerificationCode.setHint(getResources().getString(R.string.reg_phone_msg));
        tInputPwd.setHint(getResources().getString(R.string.pwd));
        tInputPwdConfirm.setHint(getResources().getString(R.string.reg_confirm_pwd));
    }

    @Override
    protected void noNetworkStatus() {

    }

    @Override
    protected int getContentView() {
        return R.layout.aty_register;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.btn_verification_code:
                if (checkUser()) {
                    networkModel.phoneCode(tInputPhone.getEditText().getText().toString(),
                            NetworkParams.CUPCAKE);
                    startCountDown();
                } else {
                    AppTools.showNormalSnackBar(registerLayout.getRoot(), getResources().getString(R
                            .string.invalid_user));
                }
                break;
            case R.id.btn_submit:
                if (checkUser() && checkPwd() && checkVerifyCode()) {
                    //注册
                    register();
                }
                break;
            case R.id.tv_protocol:
                start(UserAgreementActivity.class);
                break;
        }
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            btnSubmit.setEnabled(true);
        } else {
            btnSubmit.setEnabled(false);
        }
    }

    //验证码倒计时
    private void startCountDown() {
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(60000, 10) {
                @Override
                public void onTick(long millisUntilFinished) {
                    btnVerification.setEnabled(false);
                    btnVerification.setTextColor(getResources().getColor(R.color
                            .color_text_light_gravy));
                    btnVerification.setText((millisUntilFinished / 1000) + getResources()
                            .getString(R.string.reg_verification_code_again));
                }

                @Override
                public void onFinish() {
                    btnVerification.setEnabled(true);
                    btnVerification.setTextColor(getResources().getColor(R.color.theme_primary));
                    btnVerification.setText(getResources().getString(R.string
                            .reg_reCode));
                }
            };
        }
        countDownTimer.start();
    }

    //检查密码准确性
    private boolean checkPwd() {
        String pwd = tInputPwd.getEditText().getText().toString();
        String pwdConfirm = tInputPwdConfirm.getEditText().getText().toString();
        return AppTools.verifyPwd(tInputPwd, pwd, pwdConfirm);
    }

    //验证用户名
    private boolean checkUser() {
        String user = tInputPhone.getEditText().getText().toString();
        return AppTools.verifyPhone(tInputPhone, user);
    }

    private boolean checkVerifyCode() {
        boolean isEmpty = tInputVerificationCode.getEditText().getText().toString().isEmpty();
        if (isEmpty)
            AppTools.showNormalSnackBar(registerLayout.getRoot(), getResources().getString(R.string
                    .please_input_verification_code));
        return !isEmpty;
    }

    private void register() {
        String userName = tInputPhone.getEditText().getText().toString();
        String pwd = tInputPwd.getEditText().getText().toString();
        String rePwd = tInputPwdConfirm.getEditText().getText().toString();
        String verificationCode = tInputVerificationCode.getEditText().getText().toString();
        networkModel.register(userName, verificationCode, pwd, rePwd, NetworkParams.DONUT);
    }

    @Override
    public void onJsonObjectSuccess(BaseBean bean, NetworkParams paramsCode) {
        switch (paramsCode) {
            case CUPCAKE:
                if (bean.getErrorcode() != 0) {
                    btnVerification.setEnabled(true);
                    countDownTimer.cancel();
                }
                break;
            case DONUT:
                Intent intent = new Intent();
                AuthBean authBean = (AuthBean) bean;
                AppTools.putStringSharedPreferences(AppKeyMap.AUTH, authBean.getAuth());
                intent.putExtra("isNeedSnackBar", true).setClass(this, CompleteInfoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
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
