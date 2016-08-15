package com.szbb.pro.ui.activity.login;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.szbb.pro.AppKeyMap;
import com.szbb.pro.CompleteInfoLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.broadcast.UpdateUIBroadcast;
import com.szbb.pro.dialog.InputDialog;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.entity.eventbus.AreaEvent;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.eum.PhotoPopupOpts;
import com.szbb.pro.impl.InputCallBack;
import com.szbb.pro.impl.OnPhotoOptsSelectListener;
import com.szbb.pro.impl.UpdateUIListener;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.BitmapCompressTool;
import com.szbb.pro.tools.CameraTools;
import com.szbb.pro.tools.LogTools;
import com.szbb.pro.ui.activity.locate.LocationActivity;
import com.szbb.pro.ui.activity.locate.ProvinceActivity;
import com.szbb.pro.widget.PopupWindow.PhotoPopupWindow;

import java.util.List;

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
    private TextView tvCompleteInfoLocation;//所在地区
    private TextView tvCompleteInfoDetailAddress;//详细地址
    private EditText edtCompleteInfoPickLocation;//选取坐标
    private EditText edtCompleteInfoCitizenID;//身份证号码
    private InputDialog inputDialog;
    private UpdateUIBroadcast broadcast;
    private PhotoPopupWindow photoPopupWindow;
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
    private CameraTools cameraTools = new CameraTools();


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
        photoPopupWindow = new PhotoPopupWindow(this);

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
        tvCompleteInfoLocation = completeInfoLayout.tvLocation;
        tvCompleteInfoDetailAddress = completeInfoLayout.tvDetailAddress;
        edtCompleteInfoPickLocation = completeInfoLayout.edtPickLocation;
        edtCompleteInfoCitizenID = completeInfoLayout.tvRealCitizenId;

        broadcast = new UpdateUIBroadcast();
        broadcast.setListener(this);
        AppTools.registerBroadcast(broadcast, AppKeyMap.LOCATION_ACTION);

    }

    @Override
    protected void initEvents() {
        inputDialog.setInputCallBack(this);
        photoPopupWindow.setOnPhotoOptsSelectListener(this);
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
                photoPopupWindow.showAtDefaultLocation();
                break;
            case R.id.rylt_real_name:
                inputDialog = new InputDialog(this, false);
                inputDialog.setInputCallBack(this);
                inputDialog.setTitle(getResources().getString(R.string.organizing_real_name));
                inputDialog.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);
                inputDialog.setParams(NetworkParams.CUPCAKE);//callback means real name
                inputDialog.show();
                break;
            case R.id.rylt_address:
                inputDialog = new InputDialog(this, false);
                inputDialog.setTitle(getResources().getString(R.string.organizing__real_address));
                inputDialog.setInputCallBack(this);
                inputDialog.setParams(NetworkParams.DONUT);//callback means address
                inputDialog.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);
                inputDialog.show();
                break;
            case R.id.rylt_citizen_id:
                inputDialog = new InputDialog(this, true);
                inputDialog.setInputCallBack(this);
                inputDialog.setTitle(getResources().getString(R.string.organizing_citizen_ID));
                inputDialog.setParams(NetworkParams.FROYO);//callback means citizenId
                inputDialog.show();
                break;
            case R.id.rylt_skill:
//                Intent intent = new Intent().setClass(this, SkillActivity.class);
//                startActivityForResult(intent, AppKeyMap.KITKAT);
                break;
            case R.id.rylt_region:
                Toast.makeText(CompleteInfoActivity.this, "为方便系统给您派发附近工单，请准确设置您所在地区", Toast
                        .LENGTH_SHORT).show();
                start(ProvinceActivity.class);
                break;
            case R.id.rylt_location:
                int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
//                if(permission!=PackageManager.PERMISSION_GRANTED){
                LogTools.w(permission);
//                }
                startActivityForResult(new Intent().putExtra("flag", AppKeyMap.CUPCAKE).putExtra
                        ("title", getString(R.string.now_position)).setClass
                        (this, LocationActivity.class), AppKeyMap.LOLLIPOP);

//                    Toast.makeText(CompleteInfoActivity.this, "由于您没有允许本APP的定位权限,请通过手机的安全管理来打开!", Toast.LENGTH_LONG).show();
                break;
            case R.id.sdv_citizen_id_font_side:
                photoFlag = ID_FONT;
                photoPopupWindow.showAtDefaultLocation();
                break;
            case R.id.sdv_citizen_id_back_side:
                photoFlag = ID_BACK;
                photoPopupWindow.showAtDefaultLocation();
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
        tvCompleteInfoLocation.setText(province + "" + city + "" + district + "" + street + "");
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
                if (lat != 0d && lng != 0d)
                    this.edtCompleteInfoPickLocation.setText(R.string
                            .organization_input_chosen);
            }
        } else {
            //place picture which with take photo action
            placeImageInSwitch(requestCode, resultCode);
        }
    }

    private void placeImageInSwitch(int requestCode, int resultCode) {
        String photoPath = cameraTools.getPhotoPath(resultCode);
        if (TextUtils.isEmpty(photoPath))//如果路径为空,则有可能用户点击了取消拍照或者返回,不方面继续往下走了
        {
            return;
        }
        if (requestCode == AVATAR) {//如果点击的是头像,则进行头像路径的记录和显示拍出来的照片
            avatarPath = photoPath;
            avatarSimpleDraweeView.setImageURI(Uri.parse("file://" + avatarPath));
        } else if (requestCode == ID_FONT) {//如果填写的是身份证前面,.....(同上)
            idFontPath = photoPath;
            citizenIdFrontSimpleDraweeView.setImageURI(Uri.parse("file://" + idFontPath));
        } else if (requestCode == ID_BACK) {//如果填写的是身份证背面,.....(同上)
            idBackPath = photoPath;
            citizenIdBackSimpleDraweeView.setImageURI(Uri.parse("file://" + idBackPath));
        }
    }

    @Override
    public void uiUpData(Intent intent) {
        if (intent.getExtras().getInt("flag") == AppKeyMap.GINGERBREAD) {//如果广播是选择地址
            Bundle bundle = intent.getExtras();
            tvCompleteInfoLocation.setText(bundle.getString("province") + "-" + bundle.getString
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
                tvCompleteInfoDetailAddress.setText(word);
                break;
            case FROYO://means citizen id
                if (AppTools.verifyCitizenId(completeInfoLayout.getRoot(), word))
                    edtCompleteInfoCitizenID.setText(word);
                break;
        }
    }

    @Override
    public void onOptsSelect(PhotoPopupOpts opts) {
//        FunctionConfig functionConfig = new FunctionConfig.Builder().setEnablePreview(true)
//                .setEnableCrop(true).setCropHeight(500).setCropWidth(500).build();
        switch (opts) {
            case TAKE_PHOTO:
                cameraTools = new CameraTools();
                cameraTools.takePhoto(this, photoFlag);
//                if (photoFlag == AVATAR) {
//                    //avatar and from camera
//
//                } else if (photoFlag == ID_FONT) {
//                    //id front side and  from camera
//                    GalleryFinal.openCamera(AppKeyMap.DONUT, functionConfig, this);
//                } else if (photoFlag == ID_BACK) {
//                    //id back side and from camera
//                    GalleryFinal.openCamera(AppKeyMap.FROYO, functionConfig, this);
//                }
                break;
            case ALBUM:
                if (photoFlag == AVATAR) {//avatar and from album
                    GalleryFinal.openGallerySingle(AppKeyMap.CUPCAKE, this);
                } else if (photoFlag == ID_FONT) {//id front side and from album
                    GalleryFinal.openGallerySingle(AppKeyMap.DONUT, this);
                } else if (photoFlag == ID_BACK) {//id back side and from album
                    GalleryFinal.openGallerySingle(AppKeyMap.FROYO, this);
                }
                break;
        }
    }

    private boolean checkNecessary() {

        this.realName = edtCompleteInfoRealName.getText().toString();
        String location = tvCompleteInfoLocation.getText().toString();
        this.detailAddress = tvCompleteInfoDetailAddress.getText().toString();
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

        if (!citizenID.matches(AppKeyMap.CITIZEN_ID_REGEX)) {
            AppTools.showNormalSnackBar(parentView, getString(R.string
                    .invalid_citizenID));
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
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        super.onJsonObjectSuccess(baseBean, paramsCode);
        switch (paramsCode) {
            case CUPCAKE://submit success
                Toast.makeText(CompleteInfoActivity.this, "注册成功，请等待审核通过", Toast
                        .LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        start(LoginActivity.class, Intent.FLAG_ACTIVITY_SINGLE_TOP, Intent
                                .FLAG_ACTIVITY_CLEAR_TOP);
                    }
                }, 1000);
                break;
        }
    }

    @Override
    public void onHanlderSuccess(int requestCode, List<PhotoInfo> resultList) {
        super.onHanlderSuccess(requestCode, resultList);
        PhotoInfo photoInfo = resultList.get(0);
        String photoPath = photoInfo.getPhotoPath();
        BitmapCompressTool.getRadioBitmap(photoPath, 500, 500);
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
