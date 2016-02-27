package com.szbb.pro.ui.Activity.Vip.Wallet;

import android.os.Bundle;
import android.view.View;

import com.szbb.pro.R;
import com.szbb.pro.WalletLayout;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.entity.Vip.WalletBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.ui.Activity.Vip.Wallet.BankCardActivity;

public class WalletActivity extends BaseAty<WalletBean, BaseBean> {
    private WalletLayout walletLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        walletLayout = (WalletLayout) viewDataBinding;
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.title_wallet);
    }

    @Override
    protected void initEvents() {
        networkModel.wallet(NetworkParams.CUPCAKE);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_wallet;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.btn_bank_card:
                start(BankCardActivity.class);
                break;
            case R.id.btn_withdraw:
                break;
        }
    }

    @Override
    public void onJsonObjectSuccess(WalletBean walletBean, NetworkParams paramsCode) {
        walletLayout.setWallet(walletBean.getData());
    }
}
