package com.szbb.pro.ui.Activity.Vip.Wallet;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.szbb.pro.R;
import com.szbb.pro.WithdrawLayout;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.dialog.InputDialog;
import com.szbb.pro.dialog.MessageDialog;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.entity.Vip.BankCardBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.InputCallBack;
import com.szbb.pro.tools.AppTools;

public class WithdrawActivity extends BaseAty<BaseBean, BaseBean> implements InputCallBack {
    private WithdrawLayout withdrawLayout;
    private InputDialog inputDialog;
    private EditText edtMoney;
    private MessageDialog messageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        withdrawLayout = (WithdrawLayout) viewDataBinding;
    }

    @Override
    protected void initViews() {
        edtMoney = withdrawLayout.edtMoney;
        inputDialog = new InputDialog(this);
    }

    @Override
    protected void initEvents() {
        inputDialog.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        inputDialog.setTitle("请输入提现密码");
        inputDialog.setInputCallBack(this);

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
                inputDialog.show();
                break;
            case R.id.positive:
                AppTools.removeSingleActivity(this);
                break;
        }
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        if (paramsCode == NetworkParams.CUPCAKE) {
            BankCardBean bankBean = (BankCardBean) baseBean;
            if (!bankBean.getData().hasPayPassword()) {
                start(PayPasswordActivity.class);
                AppTools.removeSingleActivity(this);
                return;
            }
            withdrawLayout.setBank(bankBean.getData());
        } else if (paramsCode == NetworkParams.DONUT) {
            messageDialog = new MessageDialog(this);
            messageDialog.setTitle(getString(R.string.notice));
            messageDialog.setPositiveButton(getString(R.string.confirm), this);
        }
    }

    @Override
    public void inputWord(String word, NetworkParams networkParams) {
        String outMoney = edtMoney.getText().toString();
        networkModel.withdrawals(outMoney, word, NetworkParams.DONUT);
    }
}
