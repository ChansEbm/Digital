package com.szbb.pro.ui.fragment.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.pwittchen.prefser.library.Prefser;
import com.szbb.pro.AppKeyMap;
import com.szbb.pro.R;
import com.szbb.pro.VipLayout;
import com.szbb.pro.base.BaseFgm;
import com.szbb.pro.dialog.DialDialog;
import com.szbb.pro.dialog.MessageDialog;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.entity.base.Events;
import com.szbb.pro.entity.vip.CheckUpdateBean;
import com.szbb.pro.entity.vip.VipInfoBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.eum.PhotoPopupOpts;
import com.szbb.pro.impl.OnPhotoOptsSelectListener;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.MiscUtils;
import com.szbb.pro.tools.PermissionTools;
import com.szbb.pro.ui.activity.vip.personal_info.PersonalInfoActivity;
import com.szbb.pro.ui.activity.vip.system_msg.AccountCementActivity;
import com.szbb.pro.ui.activity.vip.feedback.FeedBackActivity;
import com.szbb.pro.ui.activity.vip.wallet.WalletActivity;
import com.szbb.pro.ui.activity.vip.WebViewActivity;
import com.szbb.pro.ui.activity.vip.WorkHistoryActivity;
import com.szbb.pro.widget.PopupWindow.PhotoPopupWindow;
import com.szbb.pro.widget.PopupWindow.SharePopupWindow;

import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import de.greenrobot.event.EventBus;

/**
 * Created by ChanZeeBm on 2015/9/16.
 */
public class VIPFragment
        extends BaseFgm<BaseBean, BaseBean>
        implements
        OnPhotoOptsSelectListener {
    private VipLayout vipLayout;
    private Events events = new Events();

    private PhotoPopupWindow window;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault()
                .register(this);
    }

    @Override
    protected void initViews() {
        vipLayout = (VipLayout) viewDataBinding;
        window = new PhotoPopupWindow(getActivity());
    }

    @Override
    protected void initEvents() {
        events.setOnClickListener(this);
        vipLayout.setEvent(events);
        networkModel.workerInfo(NetworkParams.CUPCAKE);
        vipLayout.sdvVipAvatar.setOnClickListener(this);
        window.setOnPhotoOptsSelectListener(this);

        vipLayout.versionInfo.setText(MiscUtils.getAppVersion(getContext()));
    }

    private void initInfoData(VipInfoBean vipInfoBean) {
        final VipInfoBean.WorkerDataEntity worker_data = vipInfoBean.getWorker_data();
        vipLayout.sdvVipAvatar.setImageURI(Uri.parse(worker_data.getThumb()));
        vipLayout.setVip(worker_data);
    }

    @Override
    protected void noNetworkStatus() {

    }

    @Override
    protected void onClick(int id, View view) {
        Intent intent = null;
        switch (id) {
            case R.id.rylt_wallet://我的钱包
                start(WalletActivity.class);
                break;
            case R.id.rylt_history_acce://历史工单
                start(WorkHistoryActivity.class);
                break;
            case R.id.rylt_personal_info://个人信息
                start(PersonalInfoActivity.class);
                break;
            case R.id.rylt_wechat_phone://微信电话
                break;
            case R.id.rylt_tell_firend://告诉朋友
                SharePopupWindow sharePopupWindow = new SharePopupWindow(getActivity());
                sharePopupWindow.showAtDefaultLocation();
                break;
            case R.id.rylt_feedback://意见反馈
                start(FeedBackActivity.class);
                break;
            case R.id.rylt_customer_phone://客服电话
                if (PermissionTools.alreadyHasPermission(getActivity(),
                        AppKeyMap.CUPCAKE,
                        Manifest
                                .permission
                                .CALL_PHONE)) {
                    DialDialog dialDialog = new DialDialog(getContext(),
                            null);
                    dialDialog.call("4008309995");
                }
                break;
            case R.id.rylt_warranty_price://联保价格
                intent = new Intent().setClass(getActivity(),
                        WebViewActivity.class);
                intent.putExtra("url",
                        AppKeyMap.HEAD_JOINTPRICE)
                        .putExtra("title",
                                getString(R
                                        .string.vip_repairing_price));
                break;
//            case R.id.rylt_essentials://接单必读
//                startActivity(new Intent().putExtra("type",
//                                                    "2")
//                                          .setClass(getActivity(),
//                                                    AccountCementActivity
//                                                            .class));
//                break;
            case R.id.rylt_check_update:
                networkModel.versions(MiscUtils.getAppVersion(getContext()),
                        NetworkParams.FROYO);//检查版本
                break;
            case R.id.rylt_about://关于
                intent = new Intent().setClass(getActivity(),
                        WebViewActivity.class);
                intent.putExtra("url",
                        AppKeyMap.HEAD_ABOUT_US)
                        .putExtra("title",
                                getString(R
                                        .string.vip_about));
                break;
            case R.id.sdv_vip_avatar://修改头像
                window.setHideTakePhotoButton();
                window.showAtDefaultLocation();
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.fgm_vip;
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        if (paramsCode == NetworkParams.CUPCAKE) {
            VipInfoBean vipInfoBean = (VipInfoBean) baseBean;
            Prefser prefser = new Prefser(AppTools.getSharePreferences());
            prefser.put("VipInfo",
                    vipInfoBean);
            initInfoData(vipInfoBean);
        } else if (paramsCode == NetworkParams.DONUT) {
            networkModel.workerInfo(NetworkParams.CUPCAKE);
        } else if (paramsCode == NetworkParams.FROYO) {
            final CheckUpdateBean checkUpdateBean = (CheckUpdateBean) baseBean;
            boolean isNeedUpdate = checkUpdateBean.getVersion_code()
                    .equals("1");
            if (isNeedUpdate) {
                final MessageDialog messageDialog = new MessageDialog(getContext());
                messageDialog.setTitle("发现新版本")
                        .setPositiveButton("下载",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent it = new Intent();
                                        it.setAction("android.intent.action.VIEW");
                                        Uri uri = Uri.parse(checkUpdateBean
                                                .getUrl());
                                        it.setData(uri);
                                        startActivity(it);
                                        messageDialog.dismiss();
                                    }
                                })
                        .setNegativeButton("取消",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        messageDialog.dismiss();
                                    }
                                })
                        .setMessage("发现了新版本,需要下载吗")
                        .show();
            } else {
                AppTools.showNormalSnackBar(parentView,
                        "当前为最新版本!");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault()
                .unregister(this);
    }

    public void onEvent(VipInfoBean vipInfoBean) {
        networkModel.workerInfo(NetworkParams.CUPCAKE);
    }


    @Override
    public void onOptsSelect(PhotoPopupOpts opts) {
        switch (opts) {
            case ALBUM:
                FunctionConfig functionConfig = new FunctionConfig.Builder().setCropHeight(300)
                        .setCropWidth(300)
                        .setEnableCamera(true)
                        .build();
                GalleryFinal.openGallerySingle(AppKeyMap.DONUT,
                        functionConfig,
                        this);
                break;
            case TAKE_PHOTO:
                break;
        }
    }

    @Override
    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
        super.onHanlderSuccess(reqeustCode,
                resultList);
        final String photoPath = resultList.get(0)
                .getPhotoPath();
        networkModel.editAvatar(photoPath,
                NetworkParams.DONUT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == AppKeyMap.CUPCAKE && grantResults[0] == PackageManager
                .PERMISSION_GRANTED) {
            AppTools.CALL("4008309995");
        }
    }
}
