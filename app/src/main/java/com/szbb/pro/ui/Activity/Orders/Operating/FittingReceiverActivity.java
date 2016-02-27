package com.szbb.pro.ui.Activity.Orders.Operating;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.FittingReceiverLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.EventBus.AreaEvent;
import com.szbb.pro.entity.Fittings.CustomerAddressBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.LogTools;
import com.szbb.pro.ui.Activity.Locate.ProvinceActivity;

import de.greenrobot.event.EventBus;

public class FittingReceiverActivity extends BaseAty<CustomerAddressBean, Object> {
    private FittingReceiverLayout fittingReceiverLayout;
    private AppCompatEditText edtReceiver;
    private AppCompatEditText edtPhone;
    private AppCompatEditText edtDetailAddress;
    private TextView tvLocation;

    private final int toUser = AppKeyMap.CUPCAKE;
    private final int toMechanic = AppKeyMap.DONUT;
    private int toggleFlag = toUser;

    private String orderId = "";
    private String provinceId;
    private String cityId;
    private String districtId;
    private String streetId;
    private String areaId;

    private CustomerAddressBean.DataEntity dataEntity = new CustomerAddressBean.DataEntity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fittingReceiverLayout = (FittingReceiverLayout) viewDataBinding;
        if (getIntent() == null)
            AppTools.removeSingleActivity(this);
        orderId = getIntent().getStringExtra("orderId");
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.title_receiver);
        EventBus.getDefault().register(this);
        edtReceiver = (AppCompatEditText) fittingReceiverLayout.tInputReceiver.getEditText();
        edtPhone = (AppCompatEditText) fittingReceiverLayout.tInputPhone.getEditText();
        edtDetailAddress = (AppCompatEditText) fittingReceiverLayout.tInputDetailAddress
                .getEditText();
        tvLocation = fittingReceiverLayout.tvLocation;
    }

    @Override
    protected void initEvents() {
        fittingReceiverLayout.tInputReceiver.setHint(getString(R.string.receiver));
        fittingReceiverLayout.tInputPhone.setHint(getString(R.string.phone_num));
        fittingReceiverLayout.tInputDetailAddress.setHint(getString(R.string.detail_address));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_fitting_receiver;
    }

    @Override
    protected void onClick(int id, View view) {
        AppTools.hideSoftInputMethod(view);//隐藏键盘
        switch (id) {
            case R.id.btn_toggle:
                getInfoByToggleFlag();
                break;
            case R.id.flyt_real_location:
                start(ProvinceActivity.class);
                break;
            case R.id.btn_confirm:
                LogTools.w(fittingReceiverLayout.getReceiver() == null);
                if (checkNecessary()) {
                    Intent intent = new Intent().putExtra("dataEntity", dataEntity);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }

    @Override
    public void onJsonObjectSuccess(CustomerAddressBean customerAddressBean, NetworkParams
            paramsCode) {
        if (paramsCode == NetworkParams.CUPCAKE || paramsCode == NetworkParams.DONUT) {
            areaId = customerAddressBean.getData().getArea_ids();
            fittingReceiverLayout.setReceiver(customerAddressBean.getData());
            toggle();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(AreaEvent areaEvent) {
        Bundle bundle = areaEvent.getIntent().getExtras();
        provinceId = bundle.getString("provinceId", "");
        String province = bundle.getString("province", "");
        cityId = bundle.getString("cityId", "");
        String city = bundle.getString("city", "");
        districtId = bundle.getString("districtId", "");
        String district = bundle.getString("district", "");
        streetId = bundle.getString("streetId", "");
        String street = bundle.getString("street", "");
        this.areaId = provinceId;
        if (!cityId.isEmpty()) {
            areaId += "," + cityId;
        }
        if (!districtId.isEmpty()) {
            areaId += "," + districtId;
        }
        if (!streetId.isEmpty()) {
            areaId += "," + streetId;
        }
        tvLocation.setText(province + "-" + city + "-" + district + "-" + street);
    }

    private void getInfoByToggleFlag() {
        switch (toggleFlag) {
            case toUser:
                networkModel.getCustomerAddress(orderId, NetworkParams.CUPCAKE);
                break;
            case toMechanic:
                networkModel.getMechanicAddress(NetworkParams.DONUT);
                break;
        }
    }

    private int toggle() {
        toggleFlag = toggleFlag == toUser ? toMechanic : toUser;
        switch (toggleFlag) {
            case toUser:
                fittingReceiverLayout.btnToggle.setText(R.string.fitting_sent_to_user);
                break;
            case toMechanic:
                fittingReceiverLayout.btnToggle.setText(R.string.fitting_sent_to_mechanic);
                break;
        }
        return toggleFlag;
    }

    private boolean checkNecessary() {
        fittingReceiverLayout.tInputDetailAddress.setErrorEnabled(false);
        fittingReceiverLayout.tInputPhone.setErrorEnabled(false);
        fittingReceiverLayout.tInputReceiver.setErrorEnabled(false);

        String receiver = edtReceiver.getText().toString();
        if (TextUtils.isEmpty(receiver)) {
            fittingReceiverLayout.tInputReceiver.setErrorEnabled(true);
            fittingReceiverLayout.tInputReceiver.setError(getString(R.string.invalid_receiver));
            return false;
        }
        String phone = edtPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            fittingReceiverLayout.tInputPhone.setErrorEnabled(true);
            fittingReceiverLayout.tInputPhone.setError(getString(R.string.find_user));
            return false;
        }
        if (TextUtils.isEmpty(areaId)) {
            AppTools.showNormalSnackBar(fittingReceiverLayout.getRoot(), getString(R.string
                    .organization_input_location));
            return false;
        }
        String address = edtDetailAddress.getText().toString();
        if (TextUtils.isEmpty(address)) {
            fittingReceiverLayout.tInputDetailAddress.setErrorEnabled(true);
            fittingReceiverLayout.tInputDetailAddress.setError(getString(R.string
                    .organization_input_detail_location));
            return false;
        }
        dataEntity.setAddress(address);
        dataEntity.setArea_ids(areaId);
        dataEntity.setArea_ids_desc(tvLocation.getText().toString());
        dataEntity.setNickname(receiver);
        dataEntity.setTelephone(phone);
        return true;
    }
}
