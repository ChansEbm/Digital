package com.szbb.pro.ui.Activity.Login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.facebook.drawee.view.SimpleDraweeView;
import com.szbb.pro.AppKeyMap;
import com.szbb.pro.CompleteInfoLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.broadcast.UpdateUIBroadcast;
import com.szbb.pro.dialog.InputDialog;
import com.szbb.pro.dialog.MessageDialog;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.entity.EventBus.AreaEvent;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.eum.PhotoPopupOpts;
import com.szbb.pro.impl.InputCallBack;
import com.szbb.pro.impl.OnPhotoOptsSelectListener;
import com.szbb.pro.impl.UpdateUIListener;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.LogTools;
import com.szbb.pro.ui.Activity.Locate.LocationActivity;
import com.szbb.pro.ui.Activity.Locate.ProvinceActivity;
import com.szbb.pro.widget.PopupWindow.TakePhotoPopupWindow;

import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import de.greenrobot.event.EventBus;

/**
 * Created by ChanZeeBm on 2015/10/16.
 */
public class CompleteInfoActivity extends BaseAty<BaseBean, BaseBean> implements UpdateUIListener,
        InputCallBack,
        OnPhotoOptsSelectListener {
    private final int AVATAR = 0X010;
    private final int ID_FONT = 0X020;
    private final int ID_BACK = 0x030;
    private CompleteInfoLayout completeInfoLayout;
    private SimpleDraweeView avatarSimpleDraweeView;//头像
    private SimpleDraweeView citizenIdFrontSimpleDraweeView;//身份证正面
    private SimpleDraweeView citizenIdBackSimpleDraweeView;//身份证背面
    private EditText edtCompleteInfoRealName;//真实姓名
    private EditText edtCompleteInfoLocation;//所在地区
    private EditText edtCompleteInfoDetailAddress;//详细地址
    private EditText edtCompleteInfoCitizenID;//身份证号码
    private EditText edtCompleteInfoSkill;//技能选择
    private InputDialog inputDialog;
    private UpdateUIBroadcast broadcast;
    private TakePhotoPopupWindow takePhotoPopupWindow;
    private Intent intent;
    private String avatarPath = "";
    private String idFontPath = "";
    private String idBackPath = "";
    private String areaId = "";
    private String realName = "";
    private String detailAddress = "";
    private String citizenID = "";
    private double lat = 0d;
    private double lng = 0d;

    private int photoFlag = 0;

    private MessageDialog messageDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        completeInfoLayout = (CompleteInfoLayout) viewDataBinding;
        intent = getIntent();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initViews() {
        titleBarTools(this).setTitle(R.string.title_organizing);
        inputDialog = new InputDialog(this);
        takePhotoPopupWindow = new TakePhotoPopupWindow(this);

        if (intent != null)
            if (intent.getBooleanExtra("isNeedSnackBar", false)) {
                AppTools.showNormalSnackBar(completeInfoLayout.getRoot(), getResources().getString(R
                        .string
                        .reg_please_complete_your_info_first));
            }

        avatarSimpleDraweeView = completeInfoLayout.sdvAvatar;
        citizenIdFrontSimpleDraweeView = completeInfoLayout.sdvCitizenIdFontSide;
        citizenIdBackSimpleDraweeView = completeInfoLayout.sdvCitizenIdBackSide;
        edtCompleteInfoRealName = completeInfoLayout.edtRealName;
        edtCompleteInfoLocation = completeInfoLayout.edtLocation;
        edtCompleteInfoDetailAddress = completeInfoLayout.edtDetailAddress;
        edtCompleteInfoCitizenID = completeInfoLayout.tvRealCitizenId;
        edtCompleteInfoSkill = completeInfoLayout.edtSkill;

        broadcast = new UpdateUIBroadcast();
        broadcast.setListener(this);
        AppTools.registerBroadcast(broadcast, AppKeyMap.LOCATION_ACTION);

        messageDialog = new MessageDialog(this);
    }

    @Override
    protected void initEvents() {
        completeInfoLayout.ryltUploadAvatar.setOnClickListener(this);
        completeInfoLayout.ryltRealName.setOnClickListener(this);
        completeInfoLayout.ryltRegion.setOnClickListener(this);
        completeInfoLayout.ryltAddress.setOnClickListener(this);
        completeInfoLayout.tvRealCitizenId.setOnClickListener(this);
        completeInfoLayout.sdvCitizenIdFontSide.setOnClickListener(this);
        completeInfoLayout.sdvCitizenIdBackSide.setOnClickListener(this);
        completeInfoLayout.ryltSkill.setOnClickListener(this);
        completeInfoLayout.button.setOnClickListener(this);
        completeInfoLayout.ivLocation.setOnClickListener(this);
        inputDialog.setInputCallBack(this);
        takePhotoPopupWindow.setOnPhotoOptsSelectListener(this);
    }

    @Override
    protected void noNetworkStatus() {

    }

    @Override
    protected int getContentView() {
        return R.layout.aty_complete_info;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.rylt_upload_avatar:
                photoFlag = AVATAR;
                takePhotoPopupWindow.showAtLocation(completeInfoLayout.getRoot(), Gravity.BOTTOM,
                        0, 0);
                break;
            case R.id.rylt_real_name:
                inputDialog.setTitle(getResources().getString(R.string.organizing_real_name));
                inputDialog.setParams(NetworkParams.CUPCAKE);//callback means real name
                inputDialog.show();
                break;
            case R.id.rylt_address:
                inputDialog.setTitle(getResources().getString(R.string.organizing__real_address));
                inputDialog.setParams(NetworkParams.DONUT);//callback means address
                inputDialog.show();
                break;
            case R.id.rylt_citizen_id:
                inputDialog.setTitle(getResources().getString(R.string.organizing_citizen_ID));
                inputDialog.setParams(NetworkParams.FROYO);//callback means citizenId
                inputDialog.show();
                break;
            case R.id.rylt_skill:
                Intent intent = new Intent().setClass(this, SkillActivity.class);
//                startActivityForResult(intent, AppKeyMap.KITKAT);
                break;
            case R.id.rylt_region:
                start(ProvinceActivity.class);
                break;
            case R.id.iv_location:
                startActivityForResult(new Intent().putExtra("flag", AppKeyMap.CUPCAKE).putExtra
                                ("title", getString(R.string.now_position)).setClass
                                (this, LocationActivity.class),
                        AppKeyMap.LOLLIPOP);
                break;
            case R.id.sdv_citizen_id_font_side:
                photoFlag = ID_FONT;
                takePhotoPopupWindow.showAtLocation(completeInfoLayout.getRoot(), Gravity.BOTTOM,
                        0, 0);
                break;
            case R.id.sdv_citizen_id_back_side:
                photoFlag = ID_BACK;
                takePhotoPopupWindow.showAtLocation(completeInfoLayout.getRoot(), Gravity.BOTTOM,
                        0, 0);
                break;
            case R.id.button:
                if (checkNecessary()) {
                    networkModel.saveWorkerInfo(realName, avatarPath, areaId, detailAddress, lat +
                                    "", lng + "",
                            citizenID, idFontPath, idBackPath, NetworkParams.CUPCAKE);
                }
                break;
        }
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
        this.areaId = provinceId;
        LogTools.w(province + "" + city + "" + district + "" + street + "");
        if (!cityId.isEmpty()) {
            areaId += "," + cityId;
        }
        if (!districtId.isEmpty()) {
            areaId += "," + districtId;
        }
        if (!streetId.isEmpty()) {
            areaId += "," + streetId;
        }
        edtCompleteInfoLocation.setText(province + "" + city + "" + district + "" + street + "");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppKeyMap.LOLLIPOP) {
            if (resultCode == RESULT_OK) {
                this.lat = data.getDoubleExtra("lat", 0d);
                this.lng = data.getDoubleExtra("lng", 0d);
            }
        }
    }

    @Override
    public void uiUpData(Intent intent) {
        if (intent.getExtras().getInt("flag") == AppKeyMap.GINGERBREAD) {//如果广播是选择地址
            Bundle bundle = intent.getExtras();
            edtCompleteInfoLocation.setText(bundle.getString("province") + "-" + bundle.getString
                    ("city") + "-" + bundle.getString("district"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppTools.unregisterBroadcast(broadcast);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void inputWord(String word, NetworkParams networkParams) {
        switch (networkParams) {
            case CUPCAKE://means real name
                if (AppTools.verifyChineseName(completeInfoLayout.getRoot(), word)) {
                    edtCompleteInfoRealName.setText(word);
                }
                break;
            case DONUT://means address
                edtCompleteInfoDetailAddress.setText(word);
                break;
            case FROYO://means citizen id
                if (AppTools.verifyCitizenId(completeInfoLayout.getRoot(), word))
                    edtCompleteInfoCitizenID.setText(word);
                break;
        }
    }

    @Override
    public void onOptsSelect(PhotoPopupOpts opts) {
        FunctionConfig functionConfig = new FunctionConfig.Builder().setEnablePreview(true)
                .setEnableCrop(true).build();
        switch (opts) {
            case TAKE_PHOTO:
                if (photoFlag == AVATAR) {
                    //avatar and from camera
                    GalleryFinal.openCamera(AppKeyMap.CUPCAKE, functionConfig, this);
                } else if (photoFlag == ID_FONT) {
                    //id front side and  from camera
                    GalleryFinal.openCamera(AppKeyMap.DONUT, functionConfig, this);
                } else if (photoFlag == ID_BACK) {
                    //id back side and from camera
                    GalleryFinal.openCamera(AppKeyMap.FROYO, functionConfig, this);
                }
                break;
            case ALBUM:
                if (photoFlag == AVATAR) {//avatar and from album
                    GalleryFinal.openGalleryMuti(AppKeyMap.CUPCAKE, 1, this);
                } else if (photoFlag == ID_FONT) {//id front side and from album
                    GalleryFinal.openGalleryMuti(AppKeyMap.DONUT, 1, this);
                } else if (photoFlag == ID_BACK) {//id back side and from album
                    GalleryFinal.openGalleryMuti(AppKeyMap.FROYO, 1, this);
                }
                break;
        }
    }

    private boolean checkNecessary() {

        this.realName = edtCompleteInfoRealName.getText().toString();
        String location = edtCompleteInfoLocation.getText().toString();
        this.detailAddress = edtCompleteInfoDetailAddress.getText().toString();
        this.citizenID = edtCompleteInfoCitizenID.getText().toString();
        if (TextUtils.isEmpty(avatarPath)) {
            AppTools.showNormalSnackBar(parentView, getString(R.string
                    .please_upload_avatar));
            return false;
        }
        if (realName.isEmpty()) {
            AppTools.showNormalSnackBar(parentView, getString(R.string
                    .organizing_please_input_real_name));
            return false;
        }
        if (location.isEmpty()) {
            AppTools.showNormalSnackBar(parentView, getString(R.string
                    .organization_input_location));
            return false;
        }
        if (detailAddress.isEmpty()) {
            AppTools.showNormalSnackBar(parentView, getString(R.string
                    .organization_input_detail_location));
            return false;
        }
        if (TextUtils.isEmpty(idFontPath)) {
            AppTools.showNormalSnackBar(parentView, getString(R.string
                    .please_upload_citizen_font));
            return false;
        }
        if (TextUtils.isEmpty(idBackPath)) {
            AppTools.showNormalSnackBar(parentView, getString(R.string
                    .please_upload_citizen_back));
            return false;
        }
        if (citizenID.isEmpty()) {
            AppTools.showNormalSnackBar(parentView, getString(R.string
                    .organizing_please_input_correct_citizen_ID));
            return false;
        }

        if (lat == 0d || lng == 0d) {
            AppTools.showNormalSnackBar(parentView, getString(R.string
                    .organizing_please_choose_lat_lng));
            return false;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AppTools.showNormalSnackBar(completeInfoLayout.getRoot(), getString(R.string
                    .organizing_please_complete_your_profile_first));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        super.onJsonObjectSuccess(baseBean, paramsCode);
        switch (paramsCode) {
            case CUPCAKE://submit success
                start(LoginActivity.class, Intent.FLAG_ACTIVITY_SINGLE_TOP, Intent
                        .FLAG_ACTIVITY_CLEAR_TOP);
                break;
        }
    }

    @Override
    public void onHanlderSuccess(int requestCode, List<PhotoInfo> resultList) {
        super.onHanlderSuccess(requestCode, resultList);
        PhotoInfo photoInfo = resultList.get(0);
        switch (requestCode) {
            case AppKeyMap.CUPCAKE://avatar
                avatarPath = photoInfo.getPhotoPath();
                avatarSimpleDraweeView.setImageURI(Uri.parse("file://" + avatarPath));
                break;
            case AppKeyMap.DONUT://id front
                idFontPath = photoInfo.getPhotoPath();
                citizenIdFrontSimpleDraweeView.setImageURI(Uri.parse("file://" + idFontPath));
                break;
            case AppKeyMap.FROYO://id back
                idBackPath = photoInfo.getPhotoPath();
                citizenIdBackSimpleDraweeView.setImageURI(Uri.parse("file://" + idBackPath));
                break;
        }
    }

}
