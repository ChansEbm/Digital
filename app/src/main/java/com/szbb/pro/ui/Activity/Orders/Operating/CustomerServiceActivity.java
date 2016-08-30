package com.szbb.pro.ui.activity.orders.operating;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.github.pwittchen.prefser.library.Prefser;
import com.szbb.pro.AppKeyMap;
import com.szbb.pro.CustomerServiceLayout;
import com.szbb.pro.R;
import com.szbb.pro.RoleServiceLayout;
import com.szbb.pro.RoleUserLayout;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.adapters.MultiAdapter;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.dialog.DialDialog;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.entity.order.OrderMsgListBean;
import com.szbb.pro.entity.vip.VipInfoBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.eum.PhotoPopupOpts;
import com.szbb.pro.impl.OnPhotoOptsSelectListener;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.ChatManager;
import com.szbb.pro.tools.PermissionTools;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMImage;
import com.tencent.TIMImageElem;
import com.tencent.TIMTextElem;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 联系客服
 */
public class CustomerServiceActivity
        extends BaseAty<BaseBean, OrderMsgListBean>
        implements OnPhotoOptsSelectListener {

    protected CustomerServiceLayout customerServiceLayout;
    protected RecyclerView recyclerView;

    protected ImageView ivBackground;
    protected PhotoView zoomPhotoView;
    protected PhotoView photoView;
    protected EditText editText;
    protected MultiAdapter<OrderMsgListBean> multiAdapter;
    protected String avatarUrl;
    protected Animation in;
    protected Animation out;
    protected View parent;
    protected String orderId = "";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customerServiceLayout = (CustomerServiceLayout) viewDataBinding;
        orderId = getIntent().getStringExtra("orderId");
        if (!TextUtils.isEmpty(orderId)) {
            ChatManager.setChattingFlag(orderId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_call,
                                  menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (item.getItemId() == R.id.menu_call) {
            if (PermissionTools.alreadyHasPermission(this,
                                                     AppKeyMap.FROYO,
                                                     Manifest.permission.CALL_PHONE)) {
                String servicePhone = getIntent().getStringExtra("servicePhone");
                new DialDialog(this,
                               null).call(servicePhone);
            }
        }
        return true;
    }

    @Override
    protected void initViews () {
        defaultTitleBar(this).setTitle(R.string.title_customer_service);
        recyclerView = customerServiceLayout.include.recyclerView;
        ivBackground = customerServiceLayout.ivBg;
        zoomPhotoView = customerServiceLayout.photoView;
        parent = customerServiceLayout.parent;
        editText = customerServiceLayout.editText;

        in = new AlphaAnimation(0.0f,
                                1.0f);
        out = new AlphaAnimation(1.0f,
                                 0.0f);


        VipInfoBean vipInfoBean = new Prefser(AppTools.getSharePreferences()).get("VipInfo",
                                                                                  VipInfoBean.class,
                                                                                  new VipInfoBean
                                                                                          ());
        avatarUrl = vipInfoBean
                .getWorker_data()
                .getThumb();

        multiAdapter = new MultiAdapter<OrderMsgListBean>(this,
                                                          list,
                                                          R.layout.role_user,
                                                          R.layout.role_service) {
            @Override
            public int getItemViewType (int position) {
                if (list.get(position)
                        .isSelf()) {
                    return FIRST_LAYOUT;
                } else {
                    return SECOND_LAYOUT;
                }
            }

            @Override
            public void onBind (ViewDataBinding viewDataBinding,
                                CommonBinderHolder holder,
                                int position,
                                OrderMsgListBean orderMsgListBean) {
                if (viewDataBinding instanceof RoleUserLayout) {
                    dealUser((RoleUserLayout) viewDataBinding,
                             orderMsgListBean.getTimElem());
                    ((RoleUserLayout) viewDataBinding).setUser(orderMsgListBean);
                } else if (viewDataBinding instanceof RoleServiceLayout) {
                    dealService((RoleServiceLayout) viewDataBinding,
                                orderMsgListBean.getTimElem());
                    ((RoleServiceLayout) viewDataBinding).setService(orderMsgListBean);
                }
            }
        };
    }

    @Override
    protected void initEvents () {

        in.setDuration(300);
        out.setDuration(300);

        recyclerView.setAdapter(multiAdapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                                               .sizeResId(R.dimen.large_margin_15dp)
                                               .colorResId(R.color.color_transparent)
                                               .build());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected int getContentView () {
        return R.layout.activity_customer_service;
    }

    @Override
    protected void onClick (int id,
                            View view) {
        switch (id) {
            case R.id.role_photoView:
                animToZoomView(view);
                break;
            case R.id.photoView:
            case R.id.iv_bg:
            case R.id.parent:
                animFromPhotoView();
                break;
        }
    }

    protected void animFromPhotoView () {
        ivBackground.startAnimation(out);
        this.zoomPhotoView.animaTo(photoView.getInfo(),
                                   new Runnable() {
                                       @Override
                                       public void run () {
                                           parent.setVisibility(View.GONE);
                                       }
                                   });
    }

    protected void animToZoomView (View view) {
        parent.setVisibility(View.VISIBLE);
        photoView = (PhotoView) view;
        String tag = (String) view.getTag();
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        Glide.with(this)
             .load(Uri.parse(tag))
             .into(zoomPhotoView);
        final Info info = photoView.getInfo();
        zoomPhotoView.animaFrom(info);
        ivBackground.startAnimation(in);
    }

    @Override
    protected void noNetworkStatus () {
        super.noNetworkStatus();
        customerServiceLayout.include.refreshLayout.endLoadingMore();
        customerServiceLayout.include.refreshLayout.endRefreshing();
    }


    @Override
    public void onJsonObjectSuccess (BaseBean baseBean,
                                     NetworkParams paramsCode) {
        super.onJsonObjectSuccess(baseBean,
                                  paramsCode);

    }

    @Override
    public void onOptsSelect (PhotoPopupOpts opts) {
    }

    @Override
    public void onHanlderSuccess (int requestCode,
                                  List<PhotoInfo> resultList) {
        super.onHanlderSuccess(requestCode,
                               resultList);
    }


    @Override
    public void onRequestPermissionsResult (int requestCode,
                                            @NonNull String[] permissions,
                                            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                                         permissions,
                                         grantResults);
        if (permissions[0].equals(Manifest.permission.CALL_PHONE) && grantResults[0] ==
                                                                     PackageManager
                                                                             .PERMISSION_GRANTED) {
            AppTools.CALL(getIntent().getStringExtra("servicePhone"));
        }
    }

    private void dealUser (RoleUserLayout roleUserLayout, TIMElem timElem) {
        roleUserLayout.sdvAvatar.setImageURI(Uri.parse(avatarUrl));
        TIMElemType type = timElem.getType();
        switch (type) {
            case Text:
                TIMTextElem timTextElem = (TIMTextElem) timElem;
                roleUserLayout.tvMsgTxt.setText(timTextElem.getText());
                roleUserLayout.llytUserPic.setVisibility(View.GONE);
                roleUserLayout.llytUserText.setVisibility(View.VISIBLE);
                break;
            case Image:
                TIMImageElem timImageElem = (TIMImageElem) timElem;
                roleUserLayout.llytUserText.setVisibility(View.GONE);
                roleUserLayout.llytUserPic.setVisibility(View.VISIBLE);
                ArrayList<TIMImage> imageList = timImageElem.getImageList();
                if (!imageList.isEmpty()) {
                    String url = imageList
                            .get(0)
                            .getUrl();
                    Glide.with(this)
                         .load(url)
                         .override(300, 300)
                         .into(roleUserLayout.rolePhotoView);
                    roleUserLayout.rolePhotoView.setTag(url);
                }
                break;
        }
    }

    private void dealService (RoleServiceLayout layout, TIMElem timElem) {
        TIMElemType type = timElem.getType();
        switch (type) {
            case Text:
                TIMTextElem timTextElem = (TIMTextElem) timElem;
                layout.tvMsgTxt.setText(timTextElem.getText());
                layout.llytServicePic.setVisibility(View.GONE);
                layout.llytServiceMsg.setVisibility(View.VISIBLE);
                break;
            case Image:
                TIMImageElem timImageElem = (TIMImageElem) timElem;
                layout.llytServiceMsg.setVisibility(View.GONE);
                layout.llytServicePic.setVisibility(View.VISIBLE);
                String url = timImageElem.getImageList()
                                         .get(0)
                                         .getUrl();
                Glide.with(this)
                     .load(url)
                     .override(300, 300)
                     .into(layout.rolePhotoView);
                layout.rolePhotoView.setTag(url);
                break;
        }
    }
}
