package com.szbb.pro.ui.Activity.Vip.Wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.R;
import com.szbb.pro.WalletLayout;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.dialog.MessageDialog;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.entity.Vip.WalletBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.model.WalletMode;

public class WalletActivity extends BaseAty<WalletBean, BaseBean> {
    private WalletLayout walletLayout;
    private MessageDialog messageDialog;

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

    /**
     * @param id
     * @param view
     */
    @Override
    protected void onClick(int id, View view) {
        final WalletBean.DataEntity wallet = walletLayout.getWallet();
        WalletMode walletMode = new WalletMode();
        switch (id) {
            case R.id.btn_bank_card:
                walletMode.payPasswordLogic(this, wallet, AppKeyMap.CUPCAKE);//表示输入完密码后跳到银行卡页面
//                if (!wallet.isHasPayPassword()) {
//                    start(PayPasswordActivity.class);
//                    return;
//                }
//                if (!wallet.isBindCard()) {
//                    showMessageDialog();
//                } else {
//                    startActivity(new Intent().setClass(this, InputPayPasswordActivity.class)
//                            .putExtra("flag", AppKeyMap.CUPCAKE));//CUPCAKE 代表输入密码完毕后跳转到提现页面
//                }
                break;
            case R.id.btn_withdraw:
                walletMode.payPasswordLogic(this, wallet, AppKeyMap.DONUT);//表示输入完密码后跳到提现页面
//                if (!wallet.isHasPayPassword()) {
//                    start(PayPasswordActivity.class);
//                    return;
//                }
//                if (!wallet.isBindCard()) {
//                    showMessageDialog();
//                } else {
//                    startActivity(new Intent().setClass(this, InputPayPasswordActivity.class)
//                            .putExtra("flag", AppKeyMap.DONUT));//CUPCAKE 代表输入密码完毕后跳转到提现页面
//                }
                break;
            case R.id.tv_detail:
                start(TransactionDetailActivity.class);
                break;
            case R.id.negative:
                messageDialog.dismiss();
                break;
            case R.id.positive:
                messageDialog.dismiss();
                startActivity(new Intent().setClass(this, InputPayPasswordActivity.class)
                        .putExtra("flag", AppKeyMap.CUPCAKE));
                break;
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        networkModel.wallet(NetworkParams.CUPCAKE);
    }

    @Override
    public void onJsonObjectSuccess(WalletBean walletBean, NetworkParams paramsCode) {
        walletLayout.setWallet(walletBean.getData());
    }
}
