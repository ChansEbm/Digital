package com.szbb.pro.ui.activity.orders.operating;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.FittingAdditionalLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.dialog.InputDialog;
import com.szbb.pro.entity.fittings.FittingWareHouseBean;
import com.szbb.pro.entity.fittings.OtherFittingBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.eum.PhotoPopupOpts;
import com.szbb.pro.impl.InputCallBack;
import com.szbb.pro.impl.OnAddPictureDoneListener;
import com.szbb.pro.impl.OnPhotoOptsSelectListener;
import com.szbb.pro.biz.MarkPictureBiz;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.CameraTools;
import com.szbb.pro.ui.activity.orders.operating.a_mode.FittingApplyActivity;
import com.szbb.pro.ui.activity.orders.operating.b_mode.FittingResendBModeActivity;
import com.szbb.pro.widget.CountView;
import com.szbb.pro.widget.PopupWindow.PhotoPopupWindow;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 其他配件
 */
public class FittingAdditionalActivity extends BaseAty<OtherFittingBean, Object> implements
        OnPhotoOptsSelectListener,
        OnAddPictureDoneListener, InputCallBack {
    private FittingAdditionalLayout fittingAdditionalLayout;
    private PhotoPopupWindow photoPopupWindow;
    private CountView countView;
    private ArrayList<String> alreadyAdd = new ArrayList<>();
    private SparseArray<String> picsPath = new SparseArray<>();

    private int flag = -1;

    private String name;
    private String number;
    private String code;
    private String description;

    private String detailId = "";
    private String orderId;
    private String serviceId;
    private String accId;
    private CameraTools cameraTools = new CameraTools();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fittingAdditionalLayout = (FittingAdditionalLayout) viewDataBinding;
        if (getIntent() == null)
            AppTools.removeSingleActivity(this);
        orderId = getIntent().getStringExtra("orderId");
        serviceId = getIntent().getStringExtra("serviceId");
        accId = getIntent().getStringExtra("accId");
        detailId = getIntent().getStringExtra("detailId");
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.title_fitting_apply);
        initAllSimpleDraweeView();
        photoPopupWindow = new PhotoPopupWindow(this);
        photoPopupWindow.setOnPhotoOptsSelectListener(this);
        countView = fittingAdditionalLayout.countView;
    }

    @Override
    protected void initEvents() {

    }

    private void initAllSimpleDraweeView() {
        fittingAdditionalLayout.font.simpleDraweeView.getHierarchy().setPlaceholderImage(R.mipmap
                .ic_font_side);
        fittingAdditionalLayout.font.simpleDraweeView.setTag("front");
        fittingAdditionalLayout.back.simpleDraweeView.getHierarchy().setPlaceholderImage(R.mipmap
                .ic_back_side);
        fittingAdditionalLayout.back.simpleDraweeView.setTag("back");
        fittingAdditionalLayout.model.simpleDraweeView.getHierarchy().setPlaceholderImage(R.mipmap
                .ic_model);
        fittingAdditionalLayout.model.simpleDraweeView.setTag("model");
        fittingAdditionalLayout.add.simpleDraweeView.setOnClickListener(this);
        fittingAdditionalLayout.add.simpleDraweeView.setTag("add");
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_fitting_additional;
    }

    @Override
    protected void onClick(int id, View view) {
        InputDialog inputDialog = new InputDialog(this);
        inputDialog.setInputCallBack(this);
        switch (id) {
            case R.id.simpleDraweeView:
                String tag = (String) view.getTag();
                caseByTag(tag);
                break;

            case R.id.flyt_name:
                inputDialog.setTitle(getString(R.string.label_name));
                inputDialog.setParams(NetworkParams.CUPCAKE);
                inputDialog.show();
                break;
            case R.id.flyt_code:
                inputDialog.setInputType(InputType.TYPE_CLASS_TEXT);
                inputDialog.setTitle(getString(R.string.label_code));
                inputDialog.setParams(NetworkParams.FROYO);//citizen reg
                inputDialog.show();
                break;
            case R.id.rylt_description:
                inputDialog.setTitle(getString(R.string.label_description));
                inputDialog.setParams(NetworkParams.DONUT);
                inputDialog.show();
                break;
            case R.id.btn_submit:
                if (checkNecessary()) {
                    networkModel.addOtherAcce(detailId, name, number, code, description,
                            alreadyAdd, NetworkParams.CUPCAKE);
                }
                break;
        }
    }

    private void caseByTag(String tag) {
        switch (tag) {
            case "front":
                flag = AppKeyMap.CUPCAKE;
                break;
            case "back":
                flag = AppKeyMap.DONUT;
                break;
            case "model":
                flag = AppKeyMap.FROYO;
                break;
            case "add":
                flag = AppKeyMap.GINGERBREAD;
                break;
            default:
                return;
        }
        photoPopupWindow.showAtDefaultLocation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_additional, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent().putExtra("orderId", orderId).putExtra("serviceId",
                serviceId).putExtra("accId", accId).putExtra("detailId", detailId)
                .setClass
                        (this, FittingWareHouseActivity.class));
        return true;
    }

    @Override
    public void onOptsSelect(PhotoPopupOpts opts) {
        if (flag == -1)
            return;
        switch (opts) {
            case ALBUM:
                if (flag == AppKeyMap.GINGERBREAD) {
                    FunctionConfig functionConfig = new FunctionConfig.Builder()
                            .setMutiSelectMaxSize(2).setSelected(alreadyAdd).build();
                    GalleryFinal.openGalleryMuti(flag, functionConfig, this);
                } else {
                    GalleryFinal.openGallerySingle(flag, this);
                }
                break;
            case TAKE_PHOTO:
                cameraTools.takePhoto(this, flag);
                break;
        }
    }

    @Override
    public void onHanlderSuccess(int requestCode, List<PhotoInfo> resultList) {
        super.onHanlderSuccess(requestCode, resultList);
        String filePath = resultList.get(0).getPhotoPath();
        switch (requestCode) {
            case AppKeyMap.CUPCAKE://means  font side from album
                fittingAdditionalLayout.font.simpleDraweeView.setImageURI(Uri.parse("file://" +
                        filePath));
                picsPath.put(0, filePath);
                break;
            case AppKeyMap.DONUT://means  back side from album
                fittingAdditionalLayout.back.simpleDraweeView.setImageURI(Uri.parse("file://" +
                        filePath));
                picsPath.put(1, filePath);
                break;
            case AppKeyMap.FROYO://means  model side from album
                fittingAdditionalLayout.model.simpleDraweeView.setImageURI(Uri.parse("file://" +
                        filePath));
                picsPath.put(2, filePath);
                break;
            default://means add pic
                MarkPictureBiz markPictureModel = new MarkPictureBiz();
                markPictureModel.setIsNeedDeleteIcon(true);
                markPictureModel.setOnAddPictureDoneListener(this);
                for (PhotoInfo photoInfo : resultList) {
                    String photoPath = photoInfo.getPhotoPath();
                    if (!alreadyAdd.contains(photoPath)) {
                        alreadyAdd.add(photoPath);
                        picsPath.put(photoPath.hashCode(), photoPath);
                        markPictureModel.savePicturePath(photoPath);
                    }
                }
                markPictureModel.addSinglePictureInLinearLayout(this, fittingAdditionalLayout
                        .llytUploadPic, false);
                break;
        }
    }

    @Override
    public void onAddPictureDone(View picParentView, int childViewCount) {

    }

    @Override
    public void onDeletePictureDone(View deleteView, int childViewCount, int childPosition, int
            tagPos) {
        String tag = (String) deleteView.getTag();
        int index = picsPath.indexOfValue(tag);
        int key = picsPath.keyAt(index);
        alreadyAdd.remove(picsPath.get(key));
        picsPath.delete(key);
    }

    @Override
    public void inputWord(String word, NetworkParams networkParams) {
        switch (networkParams) {
            case CUPCAKE://means name
                fittingAdditionalLayout.tvName.setText(word);
                fittingAdditionalLayout.tvName.setTag("okay");
                break;
            case DONUT://means description
                fittingAdditionalLayout.tvDescription.setText(word);
                fittingAdditionalLayout.tvDescription.setTag("okay");
                break;
            case FROYO://means code
                fittingAdditionalLayout.tvCode.setText(word);
                fittingAdditionalLayout.tvCode.setTag("okay");
                break;
        }
    }

    private boolean checkNecessary() {
        String tag;
        tag = (String) fittingAdditionalLayout.tvName.getTag();
        if (TextUtils.equals(tag, "NaN")) {
            AppTools.showNormalSnackBar(parentView, getString(R.string
                    .label_please_input_name));
            return false;
        }
        name = fittingAdditionalLayout.tvName.getText().toString();

        tag = (String) fittingAdditionalLayout.tvDescription.getTag();
        if (TextUtils.equals(tag, "NaN")) {
            AppTools.showNormalSnackBar(parentView, getString(R.string
                    .label_please_input_description));
            return false;
        }
        description = fittingAdditionalLayout.tvDescription.getText().toString();

        if (TextUtils.equals(countView.getCount(), "0")) {
            AppTools.showNormalSnackBar(parentView, getString(R.string
                    .label_please_input_num));
            return false;
        }
        number = countView.getCount();
        code = fittingAdditionalLayout.tvCode.getText().toString();
        return true;
    }

    @Override
    public void onJsonObjectSuccess(OtherFittingBean t, NetworkParams paramsCode) {
        final OtherFittingBean.DataEntity data = t.getData();
        FittingWareHouseBean.AcceListEntity acceListEntity = new FittingWareHouseBean
                .AcceListEntity();

        acceListEntity.setAcce_id(String.valueOf(data.getOther_acce_id()));
        acceListEntity.setAcce_name(data.getOther_acce_name());
        acceListEntity.setCount(Integer.valueOf(data.getOther_acce_nums()));
        acceListEntity.setAcce_model(data.getOther_acce_remarks());
        acceListEntity.setAcce_thumb(data.getOther_acce_thumb());
        acceListEntity.setIsExtra(true);

        startActivityByAccType(acceListEntity);
    }

    private void startActivityByAccType(FittingWareHouseBean.AcceListEntity acceListEntity) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("detailId", detailId);
        bundle.putString("orderId", orderId);
        bundle.putParcelable("acceListEntity", acceListEntity);
        intent.putExtras(bundle);
        switch (accId) {
            case "1":
                intent.setClass(this, FittingApplyActivity.class);//A模式
                break;
            case "2":
                intent.setClass(this, FittingResendBModeActivity.class);//B模式
                break;
            default:
                return;

        }
        startActivity(intent);
    }
}
