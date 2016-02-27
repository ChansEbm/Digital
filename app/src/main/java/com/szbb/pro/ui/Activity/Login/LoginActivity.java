package com.szbb.pro.ui.Activity.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.databinding.AtyLoginBinding;
import com.szbb.pro.dialog.MessageDialog;
import com.szbb.pro.entity.Login.AuthBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.LogTools;
import com.szbb.pro.ui.Activity.Main.MainActivity;

/**
 * Created by ChanZeeBm on 2015/9/9.
 */
public class LoginActivity extends BaseAty<AuthBean, AuthBean> {
    private AtyLoginBinding binding;
    private TextInputLayout textInputLayoutLoginUser;//账号名
    private TextInputLayout textInputLayoutLoginPwd;//密码
    private TextView tvLoginReg, tvLoginFindPwd;//注册，找回密码
    private Button btnLoginLog;//登录

    private MessageDialog messageDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = (AtyLoginBinding) viewDataBinding;
    }

    @Override
    protected void initViews() {
        textInputLayoutLoginUser = binding.textInputLayoutLoginUser;
        textInputLayoutLoginPwd = binding.textInputLayoutLoginPwd;
        tvLoginReg = binding.tvLoginReg;
        tvLoginFindPwd = binding.tvLoginFindPwd;
        btnLoginLog = binding.btnLoginLog;
        messageDialog = new MessageDialog(this);
    }

    @Override
    protected void initEvents() {
        //设置登录框Hint
        textInputLayoutLoginUser.setHint(getResources().getString(R.string.phone_num));
        //设置密码框Hint
        textInputLayoutLoginPwd.setHint(getResources().getString(R.string.pwd));
        tvLoginReg.setOnClickListener(this);
        tvLoginFindPwd.setOnClickListener(this);
        btnLoginLog.setOnClickListener(this);

        String userName = AppTools.getStringSharedPreferences("loginUser", null);
        String pwd = AppTools.getStringSharedPreferences("loginPwd", null);
        if (!TextUtils.isEmpty(userName) &&
                !TextUtils.isEmpty(pwd)) {
            if (textInputLayoutLoginUser != null && textInputLayoutLoginUser.getEditText() != null)
                textInputLayoutLoginUser.getEditText().setText(userName);
            textInputLayoutLoginPwd.getEditText().setText(pwd);

        }
    }

    @Override
    protected void noNetworkStatus() {

    }

    @Override
    protected int getContentView() {
        return R.layout.aty_login;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.btn_login_log:
                AppTools.hideSoftInputMethod(textInputLayoutLoginUser);
                if (checkUser() && checkPwd()) {
                    //验证登录
                    String userName = textInputLayoutLoginUser.getEditText().getText().toString();
                    String pwd = textInputLayoutLoginPwd.getEditText().getText().toString();
                    networkModel.login(userName, pwd, null);
                }
                break;
            case R.id.tv_login_reg:
                start(RegisterActivity.class);
                break;
            case R.id.tv_login_find_pwd:
                start(FindPwdActivity.class);
                break;
            case R.id.positive:
                AppTools.removeAllActivitys();
                break;
            case R.id.negative:
                messageDialog.dismiss();
                break;
        }
    }

    //验证用户名合法程度
    private boolean checkUser() {
        LogTools.e(textInputLayoutLoginUser.getEditText().getText().toString());
        return AppTools.verifyPhone(textInputLayoutLoginUser, textInputLayoutLoginUser
                .getEditText().getText().toString());
    }

    //验证密码准确性
    private boolean checkPwd() {
        String pwd = textInputLayoutLoginPwd.getEditText().getText().toString();
        return AppTools.verifyPwd(textInputLayoutLoginPwd, pwd);
    }

    @Override
    public void onJsonObjectSuccess(AuthBean authBean, NetworkParams paramsCode) {
        AppTools.putStringSharedPreferences(AppKeyMap.AUTH, authBean.getAuth());
        if (AppKeyMap.IS_DEBUG) {
            start(MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK, Intent
                    .FLAG_ACTIVITY_NEW_TASK);
        } else {
            if (authBean.getIs_complete_info() == 0) {
                start(CompleteInfoActivity.class);
            } else if (authBean.getIs_complete_info() == 1) {
                if (authBean.getIs_check() == 0) {
                    messageDialog.setTitle(getString(R.string.reviewing)).setMessage(getString(R
                            .string.your_profile_is_reviewing)).setPositiveButton(getString(R
                            .string.positive), this).setNegativeButton(getString(R.string
                            .negative), this).show();
                } else {
                    start(MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK, Intent
                            .FLAG_ACTIVITY_NEW_TASK);
                    AppTools.putStringSharedPreferences("loginUser", textInputLayoutLoginUser
                            .getEditText().getText().toString());
                    AppTools.putStringSharedPreferences("loginPwd", textInputLayoutLoginPwd
                            .getEditText().getText().toString());
                }
            }
        }
    }
}
