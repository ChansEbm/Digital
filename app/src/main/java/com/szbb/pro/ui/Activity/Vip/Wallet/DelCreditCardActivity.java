package com.szbb.pro.ui.activity.vip.Wallet;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.szbb.pro.DelCreditCardLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;

import de.greenrobot.event.EventBus;

public class DelCreditCardActivity extends BaseAty<BaseBean, BaseBean> {
    private DelCreditCardLayout delCreditCardLayout;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delCreditCardLayout = (DelCreditCardLayout) viewDataBinding;
    }

    @Override
    protected void initViews() {
        editText = delCreditCardLayout.edtPwd;
    }

    @Override
    protected void initEvents() {
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_del_credit_card;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.btn_find_pwd:
                break;
            case R.id.btn_confirm:
                checkNecessaryAndProgress();
                break;
        }
    }

    private void checkNecessaryAndProgress() {
        String pwd = editText.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            AppTools.showNormalSnackBar(parentView, "请输入提现密码");
            return;
        }
        networkModel.delCard(pwd, NetworkParams.CUPCAKE);
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        EventBus.getDefault().post("ok");
        AppTools.removeSingleActivity(this);
    }
}
