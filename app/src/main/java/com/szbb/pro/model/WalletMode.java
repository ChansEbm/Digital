package com.szbb.pro.model;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.R;
import com.szbb.pro.dialog.MessageDialog;
import com.szbb.pro.entity.Vip.WalletBean;
import com.szbb.pro.ui.Activity.Vip.Wallet.InputPayPasswordActivity;
import com.szbb.pro.ui.Activity.Vip.Wallet.PayPasswordActivity;

/**
 * Created by ChanZeeBm on 2016/3/21.
 */
public class WalletMode implements View.OnClickListener {
    private Context context;
    private MessageDialog messageDialog;

    public void payPasswordLogic(Context context, WalletBean.DataEntity wallet, int flag) {
        this.context = context;
        Intent intent = new Intent();
        if (!wallet.isHasPayPassword()) {
            intent = new Intent(context, PayPasswordActivity.class);//没设置过密码 跳到设置密码页面
            context.startActivity(intent);
            return;
        }
        if (!wallet.isBindCard()) {
            showMessageDialog();
        } else {
            context.startActivity(intent.putExtra("flag", flag).setClass(context,
                    InputPayPasswordActivity.class));
        }
    }

    private void showMessageDialog() {
        messageDialog = new MessageDialog(context);
        messageDialog.setTitle(context.getString(R.string.notice)).setPositiveButton
                (context.getString(R.string.positive), this).setNegativeButton
                (context.getString(R
                        .string.negative), this).setMessage(context.getString(R.string
                .wallet_no_credit_card));
        messageDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.negative:
                messageDialog.dismiss();
                break;
            case R.id.positive:
                messageDialog.dismiss();
                context.startActivity(new Intent().setClass(context, InputPayPasswordActivity.class)
                        .putExtra("flag", AppKeyMap.CUPCAKE));
                break;
        }
    }
}
