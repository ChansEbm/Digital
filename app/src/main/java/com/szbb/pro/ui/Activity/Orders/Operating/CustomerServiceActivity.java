package com.szbb.pro.ui.activity.orders.operating;

import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
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
import com.facebook.common.util.UriUtil;
import com.github.pwittchen.prefser.library.Prefser;
import com.squareup.picasso.Picasso;
import com.szbb.pro.CustomerServiceLayout;
import com.szbb.pro.R;
import com.szbb.pro.RoleServiceLayout;
import com.szbb.pro.RoleUserLayout;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.adapters.MultiAdapter;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.dialog.DialDialog;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.entity.Order.OrderMsgListBean;
import com.szbb.pro.entity.Vip.VipInfoBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.eum.PhotoPopupOpts;
import com.szbb.pro.impl.OnPhotoOptsSelectListener;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.ViewUtils;
import com.szbb.pro.widget.PopupWindow.PhotoPopupWindow;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 联系客服
 */
public class CustomerServiceActivity extends BaseAty<BaseBean, OrderMsgListBean.ListEntity>
        implements
        BGARefreshLayout.BGARefreshLayoutDelegate, OnPhotoOptsSelectListener {

    private CustomerServiceLayout customerServiceLayout;
    private RecyclerView recyclerView;

    private ImageView ivBackground;
    private PhotoView zoomPhotoView;
    private PhotoView photoView;
    private EditText editText;
    private MultiAdapter<OrderMsgListBean.ListEntity> multiAdapter;
    private String avatarUrl;
    private Animation in;
    private Animation out;
    private View parent;
    private String orderId = "";

    private PhotoPopupWindow photoPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customerServiceLayout = (CustomerServiceLayout) viewDataBinding;
        orderId = getIntent().getStringExtra("orderId");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_call, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_call) {
            String servicePhone = getIntent().getStringExtra("servicePhone");
            new DialDialog(this, null).call(servicePhone);
        }
        return true;
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.title_customer_service);
        recyclerView = customerServiceLayout.include.recyclerView;
        ivBackground = customerServiceLayout.ivBg;
        zoomPhotoView = customerServiceLayout.photoView;
        parent = customerServiceLayout.parent;
        editText = customerServiceLayout.editText;

        in = new AlphaAnimation(0.0f, 1.0f);
        out = new AlphaAnimation(1.0f, 0.0f);

        photoPopupWindow = new PhotoPopupWindow(this);

        VipInfoBean vipInfoBean = new Prefser(AppTools.getSharePreferences()).get("VipInfo",
                VipInfoBean
                        .class, new VipInfoBean());
        avatarUrl = vipInfoBean.getWorker_data().getThumb();

        multiAdapter = new MultiAdapter<OrderMsgListBean.ListEntity>
                (this, list, R.layout
                        .role_service, R
                        .layout.role_user) {

            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, OrderMsgListBean.ListEntity listEntity) {
                if (viewDataBinding instanceof RoleUserLayout) {
                    ((RoleUserLayout) viewDataBinding).sdvAvatar.setImageURI(Uri.parse
                            (avatarUrl));
                    ((RoleUserLayout) viewDataBinding).rolePhotoView.setOnClickListener
                            (CustomerServiceActivity.this);
                    ((RoleUserLayout) viewDataBinding).setUser(listEntity);
                } else if (viewDataBinding instanceof RoleServiceLayout) {
                    Uri uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME).path(String
                            .valueOf(R.mipmap.customer_service_avatar)).build();
                    ((RoleServiceLayout) viewDataBinding).sdvAvatar.setImageURI(uri);
                    ((RoleServiceLayout) viewDataBinding).setService(listEntity);
                }
            }

            @Override
            public int getItemViewType(int position) {
                final OrderMsgListBean.ListEntity listEntity = list.get
                        (position);
                final String role = listEntity.getRole();
                if (TextUtils.equals(role, "user")) {
                    return SECOND_LAYOUT;
                } else if (TextUtils.equals(role, "service")) {
                    return FIRST_LAYOUT;
                }
                return R.layout.role_user;
            }
        };
    }

    @Override
    protected void initEvents() {
        AppTools.defaultRefresh(customerServiceLayout.include.refreshLayout, this);
        in.setDuration(300);
        out.setDuration(300);

        recyclerView.setAdapter(multiAdapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .sizeResId(R.dimen.large_margin_15dp).colorResId(R.color.color_transparent).build
                        ());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        photoPopupWindow.setOnPhotoOptsSelectListener(this);
        networkModel.orderMessageList(orderId, "", NetworkParams.CUPCAKE);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_customer_service;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.role_photoView:
                animToZoomView(view);
                break;
            case R.id.photoView:
            case R.id.iv_bg:
            case R.id.parent:
                animFromPhotoView();
                break;
            case R.id.ibtn_send:
                if (!ViewUtils.isEdtEmpty(editText)) {
                    final String content = editText.getText().toString();
                    networkModel.addOrderMessage(orderId, "", content, "1", "", NetworkParams
                            .DONUT);
                    //代表发送新消息
                }
                break;
            case R.id.ibtn_pic:
                photoPopupWindow.showAtDefaultLocation();
                break;
        }
    }

    private void animFromPhotoView() {
        ivBackground.startAnimation(out);
        this.zoomPhotoView.animaTo(photoView.getInfo(), new Runnable() {
            @Override
            public void run() {
                parent.setVisibility(View.GONE);
            }
        });
    }

    private void animToZoomView(View view) {
        parent.setVisibility(View.VISIBLE);
        photoView = (PhotoView) view;
        String tag = (String) view.getTag();
        Picasso.with(this).load(Uri.parse(tag)).into(zoomPhotoView);
        final Info info = photoView.getInfo();
        zoomPhotoView.animaFrom(info);
        ivBackground.startAnimation(in);
    }

    @Override
    protected void noNetworkStatus() {
        super.noNetworkStatus();
        customerServiceLayout.include.refreshLayout.endLoadingMore();
        customerServiceLayout.include.refreshLayout.endRefreshing();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout bgaRefreshLayout) {
        networkModel.orderMessageList(orderId, "", NetworkParams.FROYO);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout bgaRefreshLayout) {
        return false;
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        super.onJsonObjectSuccess(baseBean, paramsCode);
        customerServiceLayout.include.refreshLayout.endLoadingMore();
        customerServiceLayout.include.refreshLayout.endRefreshing();
        if (paramsCode == NetworkParams.CUPCAKE) {//代表获取全部数据
            OrderMsgListBean orderMsgListBean = (OrderMsgListBean) baseBean;
            this.list.addAll(orderMsgListBean.getList());
            multiAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(recyclerView.getAdapter()
                    .getItemCount());//滑动到最后
        } else if (paramsCode == NetworkParams.DONUT) {
            final String message_id = list.get(list.size() - 1).getMessage_id();
            ViewUtils.clearEdt(editText);
            networkModel.orderMessageList(orderId, message_id, NetworkParams.CUPCAKE);
        } else if (paramsCode == NetworkParams.FROYO) {//刷新
            final List<OrderMsgListBean.ListEntity> list = ((OrderMsgListBean) baseBean).getList();
            this.list.clear();
            this.list.addAll(list);
            multiAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onOptsSelect(PhotoPopupOpts opts) {
        switch (opts) {
            case TAKE_PHOTO:
                GalleryFinal.openCamera(0, this);
                break;
            case ALBUM:
                GalleryFinal.openGallerySingle(0, this);
                break;
        }
    }

    @Override
    public void onHanlderSuccess(int requestCode, List<PhotoInfo> resultList) {
        super.onHanlderSuccess(requestCode, resultList);
        final PhotoInfo photoInfo = resultList.get(0);
        final String photoPath = photoInfo.getPhotoPath();
        networkModel.addOrderMessage(orderId, "", "", "2", photoPath, NetworkParams
                .DONUT);
    }
}
