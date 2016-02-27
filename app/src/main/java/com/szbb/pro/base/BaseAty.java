package com.szbb.pro.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.MultiAdapter;
import com.szbb.pro.broadcast.UpdateUIBroadcast;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.BinderOnItemClickListener;
import com.szbb.pro.impl.OkHttpResponseListener;
import com.szbb.pro.impl.UpdateUIListener;
import com.szbb.pro.model.NetworkModel;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.LogTools;
import com.szbb.pro.tools.TitleBarTools;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by ChanZeeBm on 2015/9/7.
 */
public abstract class BaseAty<E, T> extends AppCompatActivity implements View.OnClickListener,
        BinderOnItemClickListener, OkHttpResponseListener<E>, UpdateUIListener, GalleryFinal
                .OnHanlderResultCallback {

    protected CommonBinderAdapter<T> commonBinderAdapter;
    protected MultiAdapter<T> multiAdapter;
    protected RecyclerView.LayoutManager layoutManager;
    protected ArrayList<T> list = new ArrayList<>();
    protected TitleBarTools titleBarTools;
    protected ViewDataBinding viewDataBinding;
    protected NetworkModel networkModel;
    protected View parentView;
    protected UpdateUIBroadcast uiBroadcast;
    private BaseApplication baseApplication;
    private boolean isFirstRunnable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            isFirstRunnable = savedInstanceState.getBoolean("isFirstRunnable");
        }
        if (getContentView() != 0) {
            viewDataBinding = DataBindingUtil.setContentView(this, getContentView());
            parentView = viewDataBinding.getRoot();
        } else {
            throw new IllegalStateException("not invoke setContentView");
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            //            getWindow().addFlags(WindowManager.LayoutParams
            // .FLAG_TRANSLUCENT_NAVIGATION);//虚拟底部
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//状态栏
        }
        networkModel = new NetworkModel(this, viewDataBinding.getRoot());
        networkModel.setResultCallBack(this);
        baseApplication = (BaseApplication) getApplication();
        AppTools.addActivity(this);
        uiBroadcast = new UpdateUIBroadcast();
        uiBroadcast.setListener(this);
        AppTools.registerBroadcast(uiBroadcast, AppKeyMap.NO_NETWORK_ACTION);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isFirstRunnable) {
            initViews();
            initEvents();
            isFirstRunnable = !isFirstRunnable;
        }
    }

    protected TitleBarTools titleBarTools(AppCompatActivity activity) {
        titleBarTools = new TitleBarTools(activity);
        return titleBarTools;
    }

    protected TitleBarTools defaultTitleBar(AppCompatActivity activity) {
        titleBarTools = new TitleBarTools(activity);
        return titleBarTools.defaultToolBar(this);
    }


    protected void start(Bundle bundle, Class<?> targetClz) {
        start(new Intent().setClass(this, targetClz).putExtras(bundle));
    }

    protected void start(Class<?> targetClz, Integer... flags) {
        Intent intent = new Intent().setClass(this, targetClz);
        for (Integer flag : flags) {
            intent.addFlags(flag);
        }
        start(intent);
    }

    protected void start(Class<?> cls) {
        start(new Intent().setClass(this, cls));
    }

    private void start(Intent intent) {
        startActivity(intent);
    }

    protected <T extends View> T getViewById(int id) {
        return (T) viewDataBinding.getRoot().findViewById(id);
    }

    protected abstract void initViews();

    protected abstract void initEvents();

    protected void noNetworkStatus() {
        AppTools.showSettingSnackBar(parentView, getString(R.string.no_network_is_detected));
    }

    protected abstract int getContentView();

    protected abstract void onClick(int id, View view);

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isFirstRunnable", isFirstRunnable);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.nav) {
            LogTools.i("close activity");
            AppTools.removeSingleActivity(this);
        }
        onClick(id, v);
    }

    @Override
    public void onBinderItemClick(View view, int pos) {

    }

    @Override
    public void onBinderItemLongClick(View view, int pos) {

    }

    @Override
    public void onHanlderFailure(int requestCode, String errorMsg) {

    }

    @Override
    public void onHanlderSuccess(int requestCode, List<PhotoInfo> resultList) {
    }


    @Override
    public void onError(String error, NetworkParams paramsCode) {
        LogTools.e("error:" + error + ",NetworkParams:" + paramsCode);
        AppTools.showNormalSnackBar(viewDataBinding.getRoot(), getString(R.string
                .connect_server_error));
    }

    @Override
    public void onJsonArrayResponse(List<E> t, NetworkParams paramsCode) {

    }

    @Override
    public void onJsonObjectResponse(E e, NetworkParams paramsCode) {
        BaseBean baseBean = (BaseBean) e;
        final int errorCode = baseBean.getErrorcode();
        if (errorCode == 1) {
            LogTools.e("参数错误");
        } else {
            showMsgSnackBar(baseBean.getMsg());
            if (errorCode == 0) {
                onJsonObjectSuccess(e, paramsCode);
            }
        }
    }

    public void onJsonObjectSuccess(E e, NetworkParams paramsCode) {

    }


    private void showMsgSnackBar(String msg) {
        AppTools.showNormalSnackBar(parentView, msg);
    }

    @Override
    public void uiUpData(Intent intent) {
        if (intent.getAction().equals(AppKeyMap.NO_NETWORK_ACTION))
            noNetworkStatus();
    }

}


