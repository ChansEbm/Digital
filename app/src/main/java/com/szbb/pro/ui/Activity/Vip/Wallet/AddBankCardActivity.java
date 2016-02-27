package com.szbb.pro.ui.Activity.Vip.Wallet;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.szbb.pro.AddBankCardLayout;
import com.szbb.pro.AppKeyMap;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.entity.Vip.BankBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.eum.WheelOptions;
import com.szbb.pro.impl.AreaCallBack;
import com.szbb.pro.impl.OnWheelMultiOptsCallback;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.widget.PopupWindow.AreaPopupWindow;
import com.szbb.pro.widget.PopupWindow.WheelPopupWindow;

import java.util.ArrayList;
import java.util.List;

public class AddBankCardActivity extends BaseAty<BaseBean, BaseBean> implements
        OnWheelMultiOptsCallback, AreaCallBack {
    private AddBankCardLayout addBankCardLayout;
    private EditText edtAccount;
    private EditText edtReAccount;
    private TextInputLayout tInputAccount;
    private TextInputLayout tInputReAccount;

    private WheelPopupWindow wheelPopupWindow;
    private String payPassword = "";
    private AreaPopupWindow areaPopupWindow;

    private BankBean bankBean = new BankBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addBankCardLayout = (AddBankCardLayout) viewDataBinding;
        payPassword = getIntent().getStringExtra("payPassword");
    }

    @Override
    protected void initViews() {
        wheelPopupWindow = new WheelPopupWindow(this);
        wheelPopupWindow.setOptions(WheelOptions.STANDER);
        wheelPopupWindow.setOnWheelMultiOptsCallback(this);

        tInputAccount = addBankCardLayout.tInputEnterAccount;
        tInputReAccount = addBankCardLayout.tInputReEnterAccount;

        areaPopupWindow = new AreaPopupWindow(this);
    }

    @Override
    protected void initEvents() {
        addBankCardLayout.tInputEnterAccount.setHint(getString(R.string.please_input_bank_account));
        addBankCardLayout.tInputReEnterAccount.setHint(getString(R.string
                .please_re_enter_bank_account));
        edtAccount = tInputAccount.getEditText();
        edtReAccount = tInputReAccount.getEditText();
        areaPopupWindow.setAreaCallBack(this);

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
                wheelPopupWindow.setPopupTitle(getString(R.string.please_input_account_attr));
                wheelPopupWindow.setCurvedData(getResources().getStringArray(R.array.fitting_nor));
                wheelPopupWindow.setParams(AppKeyMap.CUPCAKE);
                break;
            case R.id.flyt_account_city:
                wheelPopupWindow.setPopupTitle(getString(R.string.please_input_account_city));
                areaPopupWindow.showAtDefaultLocation();
                break;
            case R.id.button:
                checkNecessaryAndProgress();
                break;
        }
    }

    private void checkNecessaryAndProgress() {
        String aTag = (String) addBankCardLayout.flytAccountAttr.getTag();
        if (TextUtils.equals(aTag, "NaN")) {
            AppTools.showNormalSnackBar(parentView, getString(R.string.please_input_account_attr));
            return;
        }

        aTag = (String) addBankCardLayout.flytAccountCity.getTag();
        if (TextUtils.equals(aTag, "NaN")) {
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
//        networkModel.addCard(account, "", bankName, bankCity);
    }

    @Override
    public void onWheelSelect(String selectData, WheelOptions wheelOptions, int params, int index) {
        switch (params) {
            case AppKeyMap.CUPCAKE:
                addBankCardLayout.flytAccountAttr.setTag("okay");
                addBankCardLayout.tvAccountAttr.setText(selectData);
                break;
            case AppKeyMap.DONUT:
                addBankCardLayout.flytAccountCity.setTag("okay");
                addBankCardLayout.tvAccountCity.setText(selectData);
                break;
        }
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        if (paramsCode == NetworkParams.CUPCAKE) {
            this.bankBean = (BankBean) baseBean;
            areaPopupWindow.notifyData();
        }
    }

    @Override
    public void onSelect(String provinceName, String cityName, int provinceIndex, int cityIndex) {
        addBankCardLayout.tvAccountCity.setText(provinceName + "-" + cityName);
    }

    @Override
    public List<String> getProvince() {
        final List<BankBean.CityListEntity> city_list = bankBean.getCity_list();
        List<String> list = new ArrayList<>();
        for (BankBean.CityListEntity cityListEntity : city_list) {
            list.add(cityListEntity.getCity_name());
        }
        return list;
    }

    @Override
    public List<List<String>> getCity() {
        final List<BankBean.CityListEntity> city_list = bankBean.getCity_list();
        List<List<String>> citys = new ArrayList<>();
        for (BankBean.CityListEntity cityListEntity : city_list) {
            List<String> city = new ArrayList<>();
            for (BankBean.CityListEntity.ChildsEntity childsEntity : cityListEntity.getChilds()) {
                city.add(childsEntity.getCity_name());
            }
            citys.add(city);
        }
        return citys;
    }
}
