package com.szbb.pro.ui.activity.vip.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.szbb.pro.BankCardLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.entity.vip.CreditCardBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.InputCallBack;
import com.szbb.pro.tools.AppTools;

import de.greenrobot.event.EventBus;

/**
 * 我的银行卡
 */
public class CreditCardActivity extends BaseAty<BaseBean, BaseBean> implements InputCallBack {
    private BankCardLayout bankCardLayout;
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bankCardLayout = (BankCardLayout) viewDataBinding;
        password = getIntent().getStringExtra("payPassword");
        EventBus.getDefault().register(this);
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
            final CreditCardBean.DataEntity card = bankCardLayout.getCard();
            final boolean hasPayPassword = card.hasPayPassword();
            boolean bindCard = card.isBindCard();
            if (!hasPayPassword) {
                start(PayPasswordActivity.class);
                return true;
            }
            if (bindCard) {
                AppTools.showNormalSnackBar(parentView, "目前只支持添加一张银行卡");
                return false;
            } else {
                startActivity(new Intent().setClass(this, AddCreditCardActivity.class).putExtra
                        ("payPassword", password));
                return true;
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
        switch (id) {
            case R.id.btn_delete:
                start(DelCreditCardActivity.class);
                break;
        }
    }

    public void onEvent(String event) {
        if (TextUtils.equals(event, "ok"))
            networkModel.myCard(NetworkParams.CUPCAKE);
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        if (paramsCode == NetworkParams.CUPCAKE) {
            setCardData((CreditCardBean) baseBean);
        } else if (paramsCode == NetworkParams.DONUT) {
            startActivity(new Intent().setClass(this, AddCreditCardActivity.class).putExtra
                    ("payPassword", password));
        }
    }

    private void setCardData(CreditCardBean creditCardBean) {
        CreditCardBean.DataEntity data = creditCardBean.getData();
        if (!data.isBindCard()) {
            bankCardLayout.swipeLayout.setVisibility(View.GONE);
            bankCardLayout.imageView.setVisibility(View.VISIBLE);
        } else {
            bankCardLayout.swipeLayout.setVisibility(View.VISIBLE);
            bankCardLayout.imageView.setVisibility(View.GONE);
        }
        bankCardLayout.setCard(data);
    }

    @Override
    public void inputWord(String word, NetworkParams networkParams) {
        this.password = word;
        networkModel.checkPayPassword(word, NetworkParams.DONUT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
