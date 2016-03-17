package com.szbb.pro.ui.Activity.Vip.PersonalInfo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.github.pwittchen.prefser.library.Prefser;
import com.szbb.pro.PersonalInfoLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.Vip.VipInfoBean;
import com.szbb.pro.tools.AppTools;

import java.util.ArrayList;

/**
 *
 */
public class PersonalInfoActivity extends BaseAty {
    private PersonalInfoLayout personalInfoLayout;
    private Prefser prefser;
    private VipInfoBean vipInfoBean;
    private ArrayList<String> picSources = new ArrayList<>();
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        personalInfoLayout = (PersonalInfoLayout) viewDataBinding;
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.vip_personal_info);
        prefser = new Prefser(AppTools.getSharePreferences());
        vipInfoBean = prefser.get("VipInfo", VipInfoBean.class, new VipInfoBean());
    }

    @Override
    protected void initEvents() {
        final VipInfoBean.WorkerDataEntity worker_data = vipInfoBean.getWorker_data();
        personalInfoLayout.setInfo(worker_data);
        personalInfoLayout.sdvFont.setImageURI(Uri.parse(worker_data
                .getCard_front()));
        personalInfoLayout.sdvBack.setImageURI(Uri.parse(worker_data
                .getCard_back()));
        picSources.add(worker_data.getCard_front());
        picSources.add(worker_data.getCard_back());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_personal_info;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.llyt_citizen_pic:
            case R.id.sdv_font:
                startActivity(new Intent().setClass(this, PreviewActivity.class)
                        .putStringArrayListExtra("source", picSources));
                break;
            case R.id.sdv_back:
                startActivity(new Intent().setClass(this, PreviewActivity.class)
                        .putStringArrayListExtra("source", picSources).putExtra
                                ("specialPosition", 1));
                break;
            case R.id.llyt_receiver_info:
                start(ReceiverInfoActivity.class);
                break;
            case R.id.llyt_change_pwd:
                start(ChangePasswordActivity.class);
                break;
        }
    }
}
