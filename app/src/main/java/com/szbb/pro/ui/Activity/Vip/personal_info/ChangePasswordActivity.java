package com.szbb.pro.ui.activity.vip.personal_info;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.szbb.pro.ChangePasswordLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.ui.activity.login.LoginActivity;

public class ChangePasswordActivity extends BaseAty<BaseBean, BaseBean> {
    private ChangePasswordLayout changePasswordLayout;

    private TextInputLayout tInputOldPwd;
    private TextInputLayout tInputNewPwd;
    private TextInputLayout tInputReNewPwd;

    private EditText edtOldPwd;
    private EditText edtNewPwd;
    private EditText edtReNewPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changePasswordLayout = (ChangePasswordLayout) viewDataBinding;
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.info_change_pwd);

        tInputOldPwd = changePasswordLayout.tInputOldPwd;
        tInputNewPwd = changePasswordLayout.tInputNewPwd;
        tInputReNewPwd = changePasswordLayout.tInputRePwd;

        edtOldPwd = tInputOldPwd.getEditText();
        edtNewPwd = tInputNewPwd.getEditText();
        edtReNewPwd = tInputReNewPwd.getEditText();
    }

    @Override
    protected void initEvents() {
        tInputOldPwd.setHint(getString(R.string.please_input_old_pwd));
        tInputNewPwd.setHint(getString(R.string.reg_pwd));
        tInputReNewPwd.setHint(getString(R.string.reg_confirm_pwd));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.btn_submit:
                checkNecessaryAndProgress();
                break;
        }
    }

    private void checkNecessaryAndProgress() {
        String old = edtOldPwd.getText().toString();
        if (!AppTools.verifyPwd(parentView, old)) {
            return;
        }
        String newPwd = edtNewPwd.getText().toString();
        String reNewPwd = edtReNewPwd.getText().toString();
        if (!AppTools.verifyPwd(parentView, newPwd, reNewPwd)) {
            return;
        }
        networkModel.editPassword(old, newPwd, reNewPwd, NetworkParams.CUPCAKE);
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        start(LoginActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK, Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }
}
