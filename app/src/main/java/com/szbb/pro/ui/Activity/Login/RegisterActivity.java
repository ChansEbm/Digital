package com.szbb.pro.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
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
import com.szbb.pro.dialog.MessageDialog;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.entity.login.AuthBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.ViewUtils;
import com.szbb.pro.ui.activity.vip.WebViewActivity;

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
        ViewUtils.endCountDown();
        btnVerification.setText(R.string.reg_verification_code);
        btnVerification.setEnabled(true);
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
                    networkModel.checkPhone(tInputPhone.getEditText().getText().toString(),
                            NetworkParams.FROYO);
                    ViewUtils.startCountDown(btnVerification, getString(R.string.reg_verification_code_again), getString(R.string
                            .reg_reCode), 60000);
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
                startActivity(new Intent(this, WebViewActivity.class).putExtra("url", AppKeyMap
                        .REGISTERAGREEMENT).putExtra("title", getString(R.string.user_agreement)));
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
            case FROYO:
                networkModel.phoneCode(tInputPhone.getEditText().getText().toString(),
                        NetworkParams.CUPCAKE);
                break;
        }
    }

    @Override
    public void onError(BaseBean baseBean) {
        super.onError(baseBean);
        if (baseBean.getErrorcode() == 3) {
            ViewUtils.endCountDown();
            showErrorDialog();
        }
    }

    private void showErrorDialog() {
        MessageDialog messageDialog = new MessageDialog(this);
        messageDialog.setTitle("该账号已经被注册").setMessage("该帐号已存在，请直接登录。如忘记密码，请通过\"找回密码\"功能设置新密码").setPositiveButton("找回密码", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(FindPwdActivity.class);
                finish();
            }
        }).setNegativeButton("登录", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(LoginActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent.FLAG_ACTIVITY_SINGLE_TOP);
            }
        }).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewUtils.endCountDown();
    }
}
