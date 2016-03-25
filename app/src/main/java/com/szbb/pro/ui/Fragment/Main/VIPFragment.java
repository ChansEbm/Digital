package com.szbb.pro.ui.Fragment.Main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.pwittchen.prefser.library.Prefser;
import com.szbb.pro.AppKeyMap;
import com.szbb.pro.R;
import com.szbb.pro.VipLayout;
import com.szbb.pro.base.BaseFgm;
import com.szbb.pro.dialog.DialDialog;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.entity.Base.Events;
import com.szbb.pro.entity.Vip.VipInfoBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.LogTools;
import com.szbb.pro.ui.Activity.Vip.PersonalInfo.PersonalInfoActivity;
import com.szbb.pro.ui.Activity.Vip.SystemMsg.AccountCementActivity;
import com.szbb.pro.ui.Activity.Vip.SystemMsg.FeedBackActivity;
import com.szbb.pro.ui.Activity.Vip.SystemMsg.SystemMsgActivity;
import com.szbb.pro.ui.Activity.Vip.Wallet.WalletActivity;
import com.szbb.pro.ui.Activity.Vip.WebViewActivity;
import com.szbb.pro.ui.Activity.Vip.WorkHistoryActivity;
import com.szbb.pro.widget.PopupWindow.SharePopupWindow;

import de.greenrobot.event.EventBus;

/**
 * Created by ChanZeeBm on 2015/9/16.
 */
public class VIPFragment extends BaseFgm<VipInfoBean, BaseBean> {
    private VipLayout vipLayout;
    private Events events = new Events();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initViews() {
        vipLayout = (VipLayout) viewDataBinding;
    }

    @Override
    protected void initEvents() {
        events.setOnClickListener(this);
        vipLayout.setEvent(events);
        networkModel.workerInfo(NetworkParams.CUPCAKE);
//        VipInfoBean vipInfoBean = new Prefser(AppTools.getSharePreferences()).get("VipInfo",
//                VipInfoBean.class, null);
//        if (vipInfoBean == null) networkModel.workerInfo(NetworkParams.CUPCAKE);
//        else {
//            initInfoData(vipInfoBean);
//        }
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
            case R.id.rylt_system_msg://系统消息
                start(SystemMsgActivity.class);
                break;
            case R.id.rylt_wallet://我的钱包
                start(WalletActivity.class);
                break;
            case R.id.rylt_history_acce://历史工单
                start(WorkHistoryActivity.class);
                break;
            case R.id.rylt_personal_info://个人信息
                start(PersonalInfoActivity.class);
                break;
//            case R.id.rylt_mechanism_auth://机构认证
//                break;
//            case R.id.rylt_service_group://我的维修群
//                break;
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
                DialDialog dialDialog = new DialDialog(getContext(), null);
                dialDialog.call("400-8565-656");
                break;
            case R.id.rylt_warranty_price://联保价格
                intent = new Intent().setClass(getActivity(), WebViewActivity.class);
                intent.putExtra("url", AppKeyMap.HEAD_JOINTPRICE).putExtra("title", getString(R
                        .string.vip_repairing_price));
                break;
            case R.id.rylt_essentials://接单必读
                startActivity(new Intent().putExtra("type", "2").setClass(getActivity(),
                        AccountCementActivity
                                .class));
                break;
            case R.id.rylt_about://关于
                intent = new Intent().setClass(getActivity(), WebViewActivity.class);
                intent.putExtra("url", AppKeyMap.HEAD_ABOUT_US).putExtra("title", getString(R
                        .string.vip_about));
                break;
        }
        if (intent != null)
            startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.fgm_vip;
    }

    @Override
    public void onJsonObjectSuccess(VipInfoBean t, NetworkParams paramsCode) {
        Prefser prefser = new Prefser(AppTools.getSharePreferences());
        prefser.put("VipInfo", t);
        initInfoData(t);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public void onEvent(VipInfoBean vipInfoBean) {
        LogTools.v("fffff");
        networkModel.workerInfo(NetworkParams.CUPCAKE);
    }


}
