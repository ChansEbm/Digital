package com.szbb.pro.ui.activity.vip.Wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.szbb.pro.R;
import com.szbb.pro.WithdrawLayout;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.dialog.MessageDialog;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.entity.Vip.CreditCardBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;

/**
 * 提现页面
 */
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
        defaultTitleBar(this).setTitle(R.string.wallet_withdraw);
        edtMoney = withdrawLayout.edtMoney;
    }

    @Override
    protected void initEvents() {
        networkModel.myCard(NetworkParams.CUPCAKE);
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
            case R.id.positive:
                ;
                break;
        }
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        if (paramsCode == NetworkParams.CUPCAKE) {
            CreditCardBean creditCardBean = (CreditCardBean) baseBean;
            withdrawLayout.setBank(creditCardBean.getData());
        } else if (paramsCode == NetworkParams.DONUT) {
            final MessageDialog dialog = new MessageDialog(this);
            dialog.setTitle("提现成功").setMessage("提现成功,详情可留意,钱包-交易明细-提现").setNegativeButton
                    (getString(R.string.confirm), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent().setClass(WithdrawActivity.this,
                                    WalletActivity.class).addFlags(Intent
                                    .FLAG_ACTIVITY_SINGLE_TOP).addFlags(Intent
                                    .FLAG_ACTIVITY_CLEAR_TOP));
                            dialog.dismiss();
                        }
                    }).show();
        }
    }
}
