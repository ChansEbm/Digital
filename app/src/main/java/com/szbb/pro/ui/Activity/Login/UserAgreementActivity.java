package com.szbb.pro.ui.Activity.Login;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.databinding.AtyUserAgreementBinding;

/**
 * Created by ChanZeeBm on 2015/10/15.
 */
//用户协议
public class UserAgreementActivity extends BaseAty {
    private AtyUserAgreementBinding binding;
    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = (AtyUserAgreementBinding) viewDataBinding;
    }

    @Override
    protected void initViews() {
        titleBarTools(this).setNavigationIcon(R.mipmap.ic_back).setNavigationListener(this)
                .setTitle(R.string.user_agreement);
        textView = (TextView) binding.getRoot().findViewById(R.id.text);
    }

    @Override
    protected void initEvents() {
        textView.setText(R.string.user_agreement_text);
    }

    @Override
    protected void noNetworkStatus() {

    }

    @Override
    protected int getContentView() {
        return R.layout.aty_user_agreement;
    }

    @Override
    protected void onClick(int id, View view) {
    }
}
