package com.szbb.pro.ui.Activity.Orders.Operating;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.FittingApplyLayout;
import com.szbb.pro.ItemFittingApplyLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.dialog.InputDialog;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.entity.Fittings.CustomerAddressBean;
import com.szbb.pro.entity.Fittings.FittingWareHouseBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.eum.PhotoPopupOpts;
import com.szbb.pro.impl.InputCallBack;
import com.szbb.pro.impl.OnAddPictureDoneListener;
import com.szbb.pro.impl.OnPhotoOptsSelectListener;
import com.szbb.pro.model.MarkPictureModel;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.widget.PopupWindow.TakePhotoPopupWindow;

import org.solovyev.android.views.llm.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 先申请配件
 */
public class FittingApplyActivity extends BaseAty<BaseBean, FittingWareHouseBean
        .AcceListEntity> implements OnPhotoOptsSelectListener, OnAddPictureDoneListener,
        InputCallBack {
    private FittingApplyLayout fittingApplyLayout;
    private SparseArray<FittingWareHouseBean.AcceListEntity> acceListEntitySparseArray
            = null;
    private FittingWareHouseBean.AcceListEntity acceListEntity = new FittingWareHouseBean
            .AcceListEntity();
    private SparseArray<String> picsPath = new SparseArray<>();

    private String detailId = "";//用于最后提交
    private String orderId = "";//用于获取客户默认地址
    private ArrayList<String> alreadyAdd = new ArrayList<>();

    private RecyclerView recyclerView;
    private TakePhotoPopupWindow takePhotoPopupWindow;

    private InputDialog inputDialog;

    private int flag = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fittingApplyLayout = (FittingApplyLayout) viewDataBinding;
        if (getIntent() == null)
            AppTools.removeSingleActivity(this);
        acceListEntitySparseArray = getIntent().getExtras().getSparseParcelableArray
                ("acceListEntitySparseArray");//如果是直接从配件申请进来,则肯定为空
        acceListEntity = getIntent().getExtras().getParcelable("acceListEntity");//获取从配件申请页面进来的值
        if (acceListEntitySparseArray == null && acceListEntity != null) {//如果是直接从配件申请进来 则执行该步骤
            acceListEntitySparseArray = new SparseArray<>();
            acceListEntitySparseArray.put(0, acceListEntity);
        }
        detailId = getIntent().getExtras().getString("detailId");
        orderId = getIntent().getExtras().getString("orderId");
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.title_fitting_apply);

        fillSource();//填充数据源
        commonBinderAdapter = new CommonBinderAdapter<FittingWareHouseBean.AcceListEntity>(this,
                R.layout.item_fitting_list_ware_house, list) {

            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, FittingWareHouseBean.AcceListEntity acceListEntity) {
                ItemFittingApplyLayout itemShopCarLayout = (ItemFittingApplyLayout) viewDataBinding;
                itemShopCarLayout.simpleDraweeView.setImageURI(Uri.parse
                        (acceListEntity.getAcce_thumb()));
                itemShopCarLayout.setFitting(acceListEntity);
            }
        };
        recyclerView = fittingApplyLayout.recyclerView;

        takePhotoPopupWindow = new TakePhotoPopupWindow(this);
    }

    @Override
    protected void initEvents() {
        recyclerView.setAdapter(commonBinderAdapter);
        recyclerView.addItemDecoration(AppTools.defaultHorizontalDecoration());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        networkModel.getCustomerAddress(orderId, NetworkParams.CUPCAKE);//获取客户默认地址
        takePhotoPopupWindow.setOnPhotoOptsSelectListener(this);
        initAllSimpleDraweeView();
    }

    private void initAllSimpleDraweeView() {
        fittingApplyLayout.font.simpleDraweeView.getHierarchy().setPlaceholderImage(R.mipmap
                .ic_font_side);
        fittingApplyLayout.font.simpleDraweeView.setTag("front");
        fittingApplyLayout.back.simpleDraweeView.getHierarchy().setPlaceholderImage(R.mipmap
                .ic_back_side);
        fittingApplyLayout.back.simpleDraweeView.setTag("back");
        fittingApplyLayout.model.simpleDraweeView.getHierarchy().setPlaceholderImage(R.mipmap
                .ic_model);
        fittingApplyLayout.model.simpleDraweeView.setTag("model");
        fittingApplyLayout.add.simpleDraweeView.setOnClickListener(this);
        fittingApplyLayout.add.simpleDraweeView.setTag("add");
    }

    private void fillSource() {
        int allCount = 0;
        list.clear();
        for (int i = 0; i < acceListEntitySparseArray.size(); i++) {
            list.add(acceListEntitySparseArray.valueAt(i));
            allCount += acceListEntitySparseArray.valueAt(i).getCount();
        }
        fittingApplyLayout.tvNum.setText(String.valueOf(allCount));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_fitting_apply;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.simpleDraweeView:
                String tag = (String) view.getTag();
                caseByTag(tag);
                break;
            case R.id.btn_submit:
                progressUpload();
                break;
            case R.id.btn_edit:
                startActivityForResult(new Intent().setClass(this, FittingReceiverActivity.class)
                        .putExtra
                                ("orderId", orderId), AppKeyMap.CUPCAKE);
                break;
            case R.id.llyt_report:
                inputDialog = new InputDialog(this);
                inputDialog.setTitle(getString(R.string.fitting_leave_supplier_message));
                inputDialog.setInputCallBack(this);
                inputDialog.show();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppKeyMap.CUPCAKE && resultCode == RESULT_OK) {
            fittingApplyLayout.setCustomer(data.<CustomerAddressBean
                    .DataEntity>getParcelableExtra("dataEntity"));
        }
    }

    private void progressUpload() {
        List<String> acces = new ArrayList<>();
        List<String> otherAcces = new ArrayList<>();
        List<String> fileThumbs = new ArrayList<>();
        for (int i = 0; i < picsPath.size(); i++) {
            alreadyAdd.add(picsPath.valueAt(i));
        }
        for (String str : alreadyAdd) {
            fileThumbs.add(str);
        }
        for (int i = 0; i < acceListEntitySparseArray.size(); i++) {
            final FittingWareHouseBean.AcceListEntity acceListEntity = acceListEntitySparseArray
                    .valueAt(i);
            String id = acceListEntity.getAcce_id();
            String number = acceListEntity.getCount() + "";
            String acce = id + "," + number;
            if (acceListEntity.isExtra()) {
                otherAcces.add(acce);
                continue;
            }
            acces.add(acce);
        }
        final CustomerAddressBean.DataEntity customer = fittingApplyLayout.getCustomer();
        String applicant = customer.getNickname();
        String applicantTell = customer.getTelephone();
        String areaIds = customer.getArea_ids();
        String address = customer.getAddress();
        String remarks = fittingApplyLayout.tvMessage.getText().toString();
        networkModel.requireAcce(detailId, acces, otherAcces, fileThumbs, remarks, applicant,
                applicantTell, areaIds, address, NetworkParams.DONUT);
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
        takePhotoPopupWindow.showAtDefaultLocation();
    }

    @Override
    public void onJsonObjectSuccess(BaseBean t, NetworkParams paramsCode) {
        if (paramsCode == NetworkParams.CUPCAKE) {//means get default client info
            CustomerAddressBean customerAddressBean = (CustomerAddressBean) t;
            fittingApplyLayout.setCustomer(customerAddressBean.getData());
        } else if (paramsCode == NetworkParams.DONUT) {
            startActivity(new Intent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent
                    .FLAG_ACTIVITY_SINGLE_TOP).setClass(this, OrderDetailActivity.class));
        }
    }

    @Override
    public void onOptsSelect(PhotoPopupOpts opts) {
        if (flag == -1)
            return;
        if (alreadyAdd.size() == 2) {
            AppTools.showNormalSnackBar(parentView, "不可添加更多图片");
            return;
        }
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
                GalleryFinal.openCamera(flag, this);
                break;
        }
    }


    @Override
    public void onHanlderSuccess(int requestCode, List<PhotoInfo> resultList) {
        String filePath;
        filePath = resultList.get(0).getPhotoPath();
        switch (requestCode) {
            case AppKeyMap.CUPCAKE://means  font side from album
                fittingApplyLayout.font.simpleDraweeView.setImageURI(Uri.parse("file://" +
                        filePath));
                picsPath.put(0, filePath);
                break;
            case AppKeyMap.DONUT://means  back side from album
                fittingApplyLayout.back.simpleDraweeView.setImageURI(Uri.parse("file://" +
                        filePath));
                picsPath.put(1, filePath);
                break;
            case AppKeyMap.FROYO://means  model side from album
                fittingApplyLayout.model.simpleDraweeView.setImageURI(Uri.parse("file://" +
                        filePath));
                picsPath.put(2, filePath);
                break;
            default://means add pic
                MarkPictureModel markPictureModel = new MarkPictureModel();
                markPictureModel.setIsNeedDeleteIcon(true);
                markPictureModel.setOnAddPictureDoneListener(this);
                for (PhotoInfo photoInfo : resultList) {
                    markPictureModel.addSinglePictureInLinearLayoutByLocal(this,
                            fittingApplyLayout.llytUploadPic, photoInfo.getPhotoPath());
                    if (!alreadyAdd.contains(photoInfo.getPhotoPath())) {
                        alreadyAdd.add(photoInfo.getPhotoPath());
                        picsPath.put(photoInfo.getPhotoId(), photoInfo.getPhotoPath());
                    }

                }
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
        fittingApplyLayout.tvMessage.setText(word);
    }
}
