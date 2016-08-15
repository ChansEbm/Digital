package com.szbb.pro.ui.activity.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.pwittchen.prefser.library.Prefser;
import com.szbb.pro.AppKeyMap;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.databinding.AtyLoginBinding;
import com.szbb.pro.dialog.MessageDialog;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.entity.login.AuthBean;
import com.szbb.pro.entity.vip.CheckUpdateBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.service.DownloadService;
import com.szbb.pro.tools.ApkTools;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.ChatManager;
import com.szbb.pro.tools.LogTools;
import com.szbb.pro.tools.MiscUtils;
import com.szbb.pro.ui.activity.main.MainActivity;

import java.io.File;

/**
 * Created by ChanZeeBm on 2015/9/9.
 */
public class LoginActivity extends BaseAty<BaseBean, AuthBean> {
    private AtyLoginBinding binding;
    private TextInputLayout textInputLayoutLoginUser;//账号名
    private TextInputLayout textInputLayoutLoginPwd;//密码
    private TextView tvLoginReg, tvLoginFindPwd;//注册，找回密码
    private Button btnLoginLog;//登录

    private MessageDialog messageDialog;

    private boolean isBeenKick = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = (AtyLoginBinding) viewDataBinding;
        isBeenKick = getIntent().getBooleanExtra("isBeenKick",
                                                 false);
    }

    @Override
    protected void initViews() {
        textInputLayoutLoginUser = binding.textInputLayoutLoginUser;
        textInputLayoutLoginPwd = binding.textInputLayoutLoginPwd;
        tvLoginReg = binding.tvLoginReg;
        tvLoginFindPwd = binding.tvLoginFindPwd;
        btnLoginLog = binding.btnLoginLog;
    }

    @Override
    protected void initEvents() {
        //设置登录框Hint
        textInputLayoutLoginUser.setHint(getResources().getString(R.string.phone_num));
        //设置密码框Hint
        textInputLayoutLoginPwd.setHint(getResources().getString(R.string.pwd));
        tvLoginReg.setOnClickListener(this);
        tvLoginFindPwd.setOnClickListener(this);
        btnLoginLog.setOnClickListener(this);
        autoLogin();
        networkModel.versions(MiscUtils.getAppVersion(this),
                              NetworkParams.GINGERBREAD);
        progressUpdateAPK();
    }

    private void progressUpdateAPK() {
        final String updateApkFile = ApkTools.getUpdateApkFile(this);
        if (!TextUtils.isEmpty(updateApkFile)) {
            final MessageDialog messageDialog = new MessageDialog(this);
            messageDialog.setTitle("发现新版本")
                         .setPositiveButton("安装",
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(Intent.ACTION_VIEW).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                                                                  .setDataAndTypeAndNormalize(Uri.fromFile(new File(updateApkFile)),
                                                                                                                              "application/vnd.android.package-archive");
                                                    startActivity(intent);
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
                         .setMessage("新版本已下载完毕,需要安装吗")
                         .show();
        }
    }

    private void autoLogin() {
        String userName = AppTools.getStringSharedPreferences("loginUser",
                                                              null);
        String pwd = AppTools.getStringSharedPreferences("loginPwd",
                                                         null);
        if (!isBeenKick) {
            if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(pwd)) {
                if (textInputLayoutLoginUser != null && textInputLayoutLoginUser.getEditText() !=
                        null) {
                    textInputLayoutLoginUser.getEditText()
                                            .setText(userName);
                }
                if (textInputLayoutLoginPwd != null && textInputLayoutLoginPwd.getEditText() !=
                        null) {
                    textInputLayoutLoginPwd.getEditText()
                                           .setText(pwd);
                }
                networkModel.login(userName,
                                   pwd,
                                   NetworkParams.FROYO);
            }
        } else {
            if (!TextUtils.isEmpty(userName)) {
                if (textInputLayoutLoginUser != null && textInputLayoutLoginUser.getEditText() !=
                        null) {
                    textInputLayoutLoginUser.getEditText()
                                            .setText(userName);
                }
                textInputLayoutLoginPwd.getEditText()
                                       .requestFocus();
            }
            final MessageDialog messageDialog = new MessageDialog(this);
            messageDialog.setMessage("您的账号在另外一台设备上登录,请注意密码是否泄露!")
                         .setPositiveButton(getString(R
                                                              .string.positive),
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    messageDialog.dismiss();
                                                }
                                            })
                         .setTitle(getString(R.string.notice))
                         .show();
        }
    }

    @Override
    protected void noNetworkStatus() {
        AppTools.showSnackBarAtLocation(parentView,
                                        getString(R.string.no_network_is_detected),
                                        getString(R.string.check_setting),
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                                startActivity(intent);
                                            }
                                        },
                                        Gravity.BOTTOM);
    }

    @Override
    protected int getContentView() {
        return R.layout.aty_login;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.btn_login_log:
                AppTools.hideSoftInputMethod(textInputLayoutLoginUser);
                if (checkUser() && checkPwd()) {
                    //验证登录
                    String userName = textInputLayoutLoginUser.getEditText()
                                                              .getText()
                                                              .toString();
                    String pwd = textInputLayoutLoginPwd.getEditText()
                                                        .getText()
                                                        .toString();
                    networkModel.login(userName,
                                       pwd,
                                       NetworkParams.FROYO);
                }
//                startActivity(new Intent(this, OrderDetailActivity.class).putExtra("orderId",
// "185"));
                break;
            case R.id.tv_login_reg:
                start(RegisterActivity.class);
                break;
            case R.id.tv_login_find_pwd:
                start(FindPwdActivity.class);
                break;
        }
    }

    //验证用户名合法程度
    private boolean checkUser() {
        LogTools.e(textInputLayoutLoginUser.getEditText()
                                           .getText()
                                           .toString());
        return AppTools.verifyPhone(textInputLayoutLoginUser,
                                    textInputLayoutLoginUser
                                            .getEditText()
                                            .getText()
                                            .toString());
    }

    //验证密码准确性
    private boolean checkPwd() {
        String pwd = textInputLayoutLoginPwd.getEditText()
                                            .getText()
                                            .toString();
        return AppTools.verifyPwd(textInputLayoutLoginPwd,
                                  pwd);
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        if (paramsCode == NetworkParams.FROYO) {//登录
            loginSuccess((AuthBean) baseBean);
        } else if (paramsCode == NetworkParams.GINGERBREAD) {//检查版本
            CheckUpdateBean checkUpdateBean = (CheckUpdateBean) baseBean;
            if (!checkUpdateBean.getUrl()
                                .isEmpty() && !ApkTools.getUpdateApkFile(this)
                                                       .isEmpty()) {
                Intent intent = new Intent(LoginActivity.this,
                                           DownloadService.class).putExtra("uri",
                                                                           checkUpdateBean.getUrl
                                                                                   ());
                intent.putExtra("uri",
                                checkUpdateBean.getUrl());
                startService(intent);
            }
        }
    }

    private void loginSuccess(AuthBean authBean) {
        Prefser prefser = new Prefser(AppTools.getSharePreferences());
        saveIdentifier(authBean,
                       prefser);
        ChatManager.login();
        final String registrationId = prefser.get("registrationId",
                                                  String.class,
                                                  "");
        if (!TextUtils.isEmpty(registrationId)) {
            networkModel.setDevice(registrationId,
                                   NetworkParams.ICECREAMSANDWICH);
        }
        if (AppKeyMap.IS_DEBUG) {
            start(MainActivity.class,
                  Intent.FLAG_ACTIVITY_CLEAR_TASK,
                  Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            loginLogic(authBean);//登录逻辑
        }
    }

    private void saveIdentifier(AuthBean authBean, Prefser prefser) {
        prefser.put(AppKeyMap.AUTH,
                    authBean.getAuth());
        //腾讯用户名
        prefser.put(AppKeyMap.IDENTIFIER,
                    authBean.getIdentifier());
        //腾讯密匙
        prefser.put(AppKeyMap.SIG,
                    authBean.getSig());

    }


    private void loginLogic(AuthBean authBean) {
        messageDialog = new MessageDialog(this);
        Prefser prefser = new Prefser(AppTools.getSharePreferences());
        prefser.put("authInfo",
                    authBean);
        if (authBean.getIs_complete_info() == 0) {
            start(CompleteInfoActivity.class);
        } else if (authBean.getIs_complete_info() == 2) {
            messageDialog.setTitle(getString(R.string.reviewing))
                         .setMessage(getString(R
                                                       .string.your_profile_is_reviewing))
                         .setPositiveButton(getString(R
                                                              .string.positive),
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    messageDialog.dismiss();
                                                }
                                            })
                         .show();
        } else if (authBean.getIs_complete_info() == 1) {
            if (authBean.getIs_check() == 1) {
                start(MainActivity.class,
                      Intent.FLAG_ACTIVITY_CLEAR_TASK,
                      Intent
                              .FLAG_ACTIVITY_NEW_TASK);
                AppTools.putStringSharedPreferences("loginUser",
                                                    textInputLayoutLoginUser
                                                            .getEditText()
                                                            .getText()
                                                            .toString());
                AppTools.putStringSharedPreferences("loginPwd",
                                                    textInputLayoutLoginPwd
                                                            .getEditText()
                                                            .getText()
                                                            .toString());
            } else if (authBean.getIs_check() == 0) {
                messageDialog.setTitle(getString(R.string.notice))
                             .setMessage(getString(R
                                                           .string.your_profile_is_disable))
                             .setPositiveButton(getString(R
                                                                  .string.positive),
                                                new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        messageDialog.dismiss();
                                                    }
                                                })
                             .show();
            }
        }
    }


}
