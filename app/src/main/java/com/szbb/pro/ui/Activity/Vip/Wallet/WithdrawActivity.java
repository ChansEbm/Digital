package com.szbb.pro.ui.Activity.Vip.Wallet;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.szbb.pro.R;
import com.szbb.pro.WithdrawLayout;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;

public class WithdrawActivity extends BaseAty<BaseBean, BaseBean> {
    private WithdrawLayout withdrawLayout;
    private EditText edtMoney;
    private String payPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        withdrawLayout = (WithdrawLayout) viewDataBinding;
        payPassword = getIntent().getStringExtra("payPassword");
    }

    @Override
    protected void initViews() {
        edtMoney = withdrawLayout.edtMoney;
    }

    @Override
    protected void initEvents() {
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_withdraw;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.button:
                String outMoney = edtMoney.getText().toString();
                if (outMoney.isEmpty()) {
                    AppTools.showNormalSnackBar(parentView, "请输入提现金额");
                    return;
                }
                networkModel.withdrawals(outMoney, payPassword, NetworkParams.DONUT);
                break;

        }
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {

    }
}
