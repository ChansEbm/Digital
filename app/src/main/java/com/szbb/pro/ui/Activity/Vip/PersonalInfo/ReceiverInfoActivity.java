package com.szbb.pro.ui.activity.vip.PersonalInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.github.pwittchen.prefser.library.Prefser;
import com.szbb.pro.R;
import com.szbb.pro.ReceiverInfoLayout;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.entity.EventBus.AreaEvent;
import com.szbb.pro.entity.Vip.VipInfoBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.LogTools;
import com.szbb.pro.ui.activity.locate.ProvinceActivity;
import com.szbb.pro.ui.activity.main.MainActivity;

import de.greenrobot.event.EventBus;

public class ReceiverInfoActivity extends BaseAty<BaseBean, BaseBean> {
    private ReceiverInfoLayout receiverInfoLayout;
    private TextInputLayout tInputReceiver;
    private TextInputLayout tInputPhone;
    private TextInputLayout tInputDetailAddr;
    private TextInputLayout tInputZipCode;

    private EditText edtReceiver;
    private EditText edtPhone;
    private EditText edtDetailAddr;
    private EditText edtZipCode;

    private String areaIds = "";

    private VipInfoBean vipInfoBean = new VipInfoBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiverInfoLayout = (ReceiverInfoLayout) viewDataBinding;
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.receiver_info);
        tInputReceiver = receiverInfoLayout.tInputReceiver;
        tInputPhone = receiverInfoLayout.tInputPhone;
        tInputDetailAddr = receiverInfoLayout.tInputRealAddress;
        tInputZipCode = receiverInfoLayout.tInputZipCode;

        edtReceiver = tInputReceiver.getEditText();
        edtPhone = tInputPhone.getEditText();
        edtDetailAddr = tInputDetailAddr.getEditText();
        edtZipCode = tInputZipCode.getEditText();

        Prefser prefser = new Prefser(AppTools.getSharePreferences());
        vipInfoBean = prefser.get("VipInfo", VipInfoBean.class, new VipInfoBean());
        LogTools.w(vipInfoBean.getWorker_data().getAddressee_data() == null);
        areaIds = vipInfoBean.getWorker_data().getAddressee_data().getArea_ids();
    }

    @Override
    protected void initEvents() {
        tInputReceiver.setHint(getString(R.string.receiver));
        tInputPhone.setHint(getString(R.string.phone_num));
        tInputDetailAddr.setHint(getString(R.string.detail_address));
        tInputZipCode.setHint(getString(R.string.zip_code));

        receiverInfoLayout.setInfo(vipInfoBean.getWorker_data().getAddressee_data());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_receiver_info;
    }

    @Override
    protected void onClick(int id, View view) {
        AppTools.hideSoftInputMethod(parentView);
        switch (id) {
            case R.id.btn_submit:
                checkNecessaryAndProgress();
                break;
            case R.id.llyt_real_location:
                start(ProvinceActivity.class);
                break;
        }
    }

    /**
     *
     */
    private void checkNecessaryAndProgress() {
        String addressee = edtReceiver.getText().toString();
        if (!AppTools.verifyChineseName(parentView, addressee)) {
            return;
        }
        String phone = edtPhone.getText().toString();
        if (!AppTools.verifyPhone(parentView, phone)) {
            return;
        }
        String detailAddress = edtDetailAddr.getText().toString();
        if (TextUtils.isEmpty(detailAddress)) {
            AppTools.showNormalSnackBar(parentView, getString(R.string
                    .please_input_detail_address));
            return;
        }
        String zipCode = edtZipCode.getText().toString();
        if (!AppTools.verifyZipCode(parentView, zipCode)) {
            return;
        }
        networkModel.saveAddressee(addressee, phone, areaIds, detailAddress, zipCode,
                NetworkParams.CUPCAKE);
    }

    public void onEventMainThread(AreaEvent areaEvent) {
        Bundle bundle = areaEvent.getIntent().getExtras();
        String provinceId = bundle.getString("provinceId", "");
        String province = bundle.getString("province", "");
        String cityId = bundle.getString("cityId", "");
        String city = bundle.getString("city", "");
        String districtId = bundle.getString("districtId", "");
        String district = bundle.getString("district", "");
        String streetId = bundle.getString("streetId", "");
        String street = bundle.getString("street", "");
        this.areaIds = provinceId;
        LogTools.w(province + "" + city + "" + district + "" + street + "");
        if (!cityId.isEmpty()) {
            areaIds += "," + cityId;
        }
        if (!districtId.isEmpty()) {
            areaIds += "," + districtId;
        }
        if (!streetId.isEmpty()) {
            areaIds += "," + streetId;
        }
        receiverInfoLayout.tvArea.setText(province + "" + city + "" + district + "" + street + "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        super.onJsonObjectSuccess(baseBean, paramsCode);
        EventBus.getDefault().post(new VipInfoBean());
        startActivity(new Intent().setClass(this, MainActivity.class).addFlags(Intent
                .FLAG_ACTIVITY_SINGLE_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
