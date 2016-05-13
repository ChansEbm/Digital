package com.szbb.pro.ui.activity.vip.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.github.pwittchen.prefser.library.Prefser;
import com.szbb.pro.AddBankCardLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.entity.vip.BankBean;
import com.szbb.pro.entity.vip.VipInfoBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.library.dialog.OnTextSetListener;
import com.szbb.pro.library.dialog.WheelViewDialog;
import com.szbb.pro.library.model.AreaBean;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.LogTools;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class AddCreditCardActivity extends BaseAty<BaseBean, BaseBean> implements
        OnTextSetListener {
    private AddBankCardLayout addBankCardLayout;
    private EditText edtAccount;
    private EditText edtReAccount;
    private TextInputLayout tInputAccount;
    private TextInputLayout tInputReAccount;

    private String payPassword = "";

    private WheelViewDialog citysDialog;
    private WheelViewDialog banksDialog;

    private String cityId = "-1";
    private BankBean bankBean = new BankBean();
    private String bankId = "-1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addBankCardLayout = (AddBankCardLayout) viewDataBinding;
        payPassword = getIntent().getStringExtra("payPassword");
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.title_add_credit_card);
        citysDialog = new WheelViewDialog(this, WheelViewDialog.TWO_LINKAGE);
        banksDialog = new WheelViewDialog(this, WheelViewDialog.ONE_LEVEL);
        tInputAccount = addBankCardLayout.tInputEnterAccount;
        tInputReAccount = addBankCardLayout.tInputReEnterAccount;

        Prefser prefser = new Prefser(AppTools.getSharePreferences());
        VipInfoBean infoBean = prefser.get("VipInfo", VipInfoBean.class, new VipInfoBean());
        addBankCardLayout.tvAccountHolder.setText(infoBean.getWorker_data().getNickname());
    }

    @Override
    protected void initEvents() {
        addBankCardLayout.tInputEnterAccount.setHint(getString(R.string.please_input_bank_account));
        addBankCardLayout.tInputReEnterAccount.setHint(getString(R.string
                .please_re_enter_bank_account));
        edtAccount = tInputAccount.getEditText();
        edtReAccount = tInputReAccount.getEditText();
        citysDialog.setPositiveButton(getString(R.string.confirm), this);
        citysDialog.setTitle(R.string.please_input_account_city);

        banksDialog.setPositiveButton(getString(R.string.confirm), this);
        banksDialog.setTitle(R.string.please_input_account_attr);

        networkModel.bankList(NetworkParams.CUPCAKE);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_bank_card;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.flyt_account_attr:
                banksDialog.show();
                break;
            case R.id.flyt_account_city:
                citysDialog.show();
                break;
            case R.id.button:
                checkNecessaryAndProgress();
                break;
            case R.id.frameLayout:
                AppTools.hideSoftInputMethod(view);
                break;
        }
    }

    private void checkNecessaryAndProgress() {
        if (TextUtils.equals(bankId, "-1")) {
            AppTools.showNormalSnackBar(parentView, getString(R.string.please_input_account_attr));
            return;
        }
        if (TextUtils.equals(cityId, "-1")) {
            AppTools.showNormalSnackBar(parentView, getString(R.string
                    .please_input_account_city));
            return;
        }
        String account = edtAccount.getText().toString();
        if (TextUtils.isEmpty(account)) {
            tInputAccount.setErrorEnabled(true);
            tInputAccount.setError(getString(R.string.invalid_account));
            return;
        }
        tInputAccount.setErrorEnabled(false);
        String reAccount = edtReAccount.getText().toString();
        if (TextUtils.isEmpty(reAccount)) {
            tInputReAccount.setErrorEnabled(true);
            tInputReAccount.setError(getString(R.string.invalid_account));
            return;
        }
        tInputReAccount.setErrorEnabled(false);
        if (!TextUtils.equals(account, reAccount)) {
            AppTools.showNormalSnackBar(parentView, getString(R.string.account_not_match));
            return;
        }
        networkModel.addCard(account, bankId, cityId, payPassword, NetworkParams.DONUT);
    }


    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        if (paramsCode == NetworkParams.CUPCAKE) {
            this.bankBean = (BankBean) baseBean;
            initCity();
            initBankList();
        } else if (paramsCode == NetworkParams.DONUT) {
            EventBus.getDefault().post("ok");
            startActivity(new Intent().setClass(this, WalletActivity.class).addFlags(Intent
                    .FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
        }
    }

    private void initBankList() {
        List<BankBean.BankListEntity> bank_list = bankBean.getBank_list();
        ArrayList<String> areaBeans = new ArrayList<>();
        for (BankBean.BankListEntity bankListEntity : bank_list) {
            areaBeans.add(bankListEntity.getBank_name());
        }
        banksDialog.setData(areaBeans);
    }

    private void initCity() {
        List<BankBean.CityListEntity> allCityData = bankBean.getCity_list();
        ArrayList<AreaBean> finalData = new ArrayList<>();
        for (BankBean.CityListEntity allData : allCityData) {
            AreaBean provinceBean = new AreaBean();
            provinceBean.setName(allData.getCity_name());
            LogTools.w(allData.getCity_name());
            List<BankBean.CityListEntity.ChildsEntity> childs = allData.getChilds();
            List<AreaBean> citys = new ArrayList<>();
            for (BankBean.CityListEntity.ChildsEntity child : childs) {
                AreaBean cityBean = new AreaBean();
                cityBean.setName(child.getCity_name());
                LogTools.i(child.getCity_name());
                citys.add(cityBean);
            }
            provinceBean.setArea(citys);
            finalData.add(provinceBean);
        }
        citysDialog.setData(finalData);

    }


    @Override
    public void onTextSet(WheelViewDialog wheelViewDialog, String text, int[] pos) {
        if (wheelViewDialog == citysDialog) {
            cityId = this.bankBean.getCity_list().get(pos[0]).getChilds().get(pos[1]).getCity_id();
            addBankCardLayout.tvAccountCity.setText(text);
        } else if (wheelViewDialog == banksDialog) {
            bankId = this.bankBean.getBank_list().get(pos[0]).getBank_id();
            addBankCardLayout.tvAccountAttr.setText(text);
        }
        wheelViewDialog.dismiss();
    }
}
