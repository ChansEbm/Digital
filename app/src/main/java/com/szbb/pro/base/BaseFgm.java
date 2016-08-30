package com.szbb.pro.base;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.MultiAdapter;
import com.szbb.pro.biz.NetworkBiz;
import com.szbb.pro.broadcast.UpdateUIBroadcast;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.BinderOnItemClickListener;
import com.szbb.pro.impl.OkHttpResponseListener;
import com.szbb.pro.impl.UpdateUIListener;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.LogTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by ChanZeeBm on 2015/9/7.
 */
public abstract class BaseFgm<E, T>
        extends Fragment
        implements View.OnClickListener,
        BinderOnItemClickListener, OkHttpResponseListener<E>, UpdateUIListener, GalleryFinal
                .OnHanlderResultCallback {
    protected CommonBinderAdapter<T> commonBinderAdapter;
    protected MultiAdapter<T> multiAdapter;
    protected List<T> list = new ArrayList<>();
    protected ViewDataBinding viewDataBinding;
    protected NetworkBiz networkModel;
    protected UpdateUIBroadcast uiBroadcast;
    protected View parentView;
    protected HashMap<String, Object> permissions = new HashMap<>();

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiBroadcast = new UpdateUIBroadcast();
        uiBroadcast.setListener(this);
        AppTools.registerBroadcast(uiBroadcast,
                                   AppKeyMap.NO_NETWORK_ACTION);
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater,
                                                  getContentView(),
                                                  null,
                                                  false);
        parentView = viewDataBinding.getRoot();
        return parentView;

    }

    @Override public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        networkModel = new NetworkBiz(getActivity());
        networkModel.setResultCallBack(this);
        initViews();
        initEvents();
    }

    @Override
    public void onStart () {
        super.onStart();
    }

    protected abstract void initViews ();

    protected abstract void initEvents ();

    protected abstract void noNetworkStatus ();

    protected abstract void onClick (int id, View view);

    protected abstract int getContentView ();


    @Override
    public void onClick (View v) {
        int id = v.getId();
        onClick(id,
                v);
    }

    protected void start (Bundle bundle, Class<?> targetClz) {
        start(new Intent().setClass(getActivity(),
                                    targetClz)
                          .putExtras(bundle));
    }

    protected void start (Class<?> targetClz, Integer... flags) {
        Intent intent = new Intent().setClass(getActivity(),
                                              targetClz);
        for (Integer flag : flags) {
            intent.addFlags(flag);
        }
        start(intent);
    }

    protected void start (Class<?> cls) {
        start(new Intent().setClass(getActivity(),
                                    cls));
    }

    private void start (Intent intent) {
        startActivity(intent);
    }

    protected View getViewById (int id) {
        return viewDataBinding.getRoot()
                              .findViewById(id);
    }

    @Override
    public void onBinderItemClick (View view, int pos) {

    }

    @Override
    public void onBinderItemLongClick (View view, int pos) {

    }

    @Override
    public void onError (String error, NetworkParams paramsCode) {
        LogTools.e("error response here");
        noNetworkStatus();
        if (!(getActivity() == null) && isAdded()) {
            AppTools.showActionSnackBar(viewDataBinding.getRoot(),
                                        getString(R.string.connect_server_error),
                                        null,
                                        null);
        }
    }

    @Override
    public void onJsonArrayResponse (List<E> t, NetworkParams paramsCode) {
        LogTools.w("jsonArray response here");

    }

    @Override
    public void onJsonObjectResponse (E t, NetworkParams paramsCode) {
        if (t instanceof BaseBean) {
            BaseBean baseBean = (BaseBean) t;
            final int errorCode = baseBean.getErrorcode();
            if (errorCode == 1) {
                LogTools.e("参数错误");
            } else {
                if (errorCode == 0) {
                    onJsonObjectSuccess(t,
                                        paramsCode);
                } else {
                    showMsgSnackBar(baseBean.getMsg());
                    onError();
                }
            }
        }
    }

    public void onJsonObjectSuccess (E t, NetworkParams paramsCode) {

    }

    public void onError () {

    }

    private void showMsgSnackBar (String msg) {
        if (getActivity() != null) {
            View activityView = getActivity().findViewById(R.id.flyt_fragment);
            View thisRoot = viewDataBinding.getRoot();
            View finalView = activityView == null ? thisRoot : activityView;
            AppTools.showNormalSnackBar(thisRoot,
                                        msg);
        }
    }

    @Override
    public void uiUpData (Intent intent) {
        if (intent.getAction()
                  .equals(AppKeyMap.NO_NETWORK_ACTION)) {
            LogTools.w("no net work");
            noNetworkStatus();
        }
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
    }

    @Override
    public void onHanlderSuccess (int reqeustCode, List<PhotoInfo> resultList) {

    }

    @Override
    public void onHanlderFailure (int requestCode, String errorMsg) {

    }
}
