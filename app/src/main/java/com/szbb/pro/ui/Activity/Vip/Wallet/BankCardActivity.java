package com.szbb.pro.ui.Activity.Vip.Wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.szbb.pro.BankCardLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.dialog.InputDialog;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.entity.Vip.BankCardBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.InputCallBack;

public class BankCardActivity extends BaseAty<BankCardBean, BaseBean> implements InputCallBack {
    private BankCardLayout bankCardLayout;
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bankCardLayout = (BankCardLayout) viewDataBinding;
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.wallet_bank_card);
    }

    @Override
    protected void initEvents() {
        networkModel.myCard(NetworkParams.CUPCAKE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add) {
            final BankCardBean.DataEntity card = bankCardLayout.getCard();
            final boolean hasPayPassword = card.hasPayPassword();
            if (!hasPayPassword) {
                start(PayPasswordActivity.class);
            } else {
                InputDialog inputDialog = new InputDialog(this);
                inputDialog.setTitle(getString(R.string.title_set_withdraw_pwd));
                inputDialog.setParams(NetworkParams.CUPCAKE);
                inputDialog.setInputCallBack(this);
                inputDialog.show();
            }
        }
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_bank_card;
    }


    @Override
    protected void onClick(int id, View view) {

    }


    @Override
    public void onJsonObjectSuccess(BankCardBean bankCardBean, NetworkParams paramsCode) {
        if (paramsCode == NetworkParams.CUPCAKE) {
            bankCardLayout.setCard(bankCardBean.getData());
        } else if (paramsCode == NetworkParams.DONUT) {
            startActivity(new Intent().setClass(this, AddBankCardActivity.class).putExtra
                    ("payPassword", password));
        }
    }

    @Override
    public void inputWord(String word, NetworkParams networkParams) {
        this.password = word;
        networkModel.checkPayPassword(word, NetworkParams.DONUT);
    }
}
