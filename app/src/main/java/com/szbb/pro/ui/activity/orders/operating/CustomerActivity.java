package com.szbb.pro.ui.activity.orders.operating;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.R;
import com.szbb.pro.entity.eventbus.ChatListEvent;
import com.szbb.pro.entity.order.OrderMsgListBean;
import com.szbb.pro.eum.PhotoPopupOpts;
import com.szbb.pro.service.ResetUnreadService;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.ChatManager;
import com.szbb.pro.tools.LogTools;
import com.szbb.pro.tools.PermissionTools;
import com.szbb.pro.widget.PopupWindow.PhotoPopupWindow;
import com.szbb.pro.widget.deleter.CameraCallback;
import com.szbb.pro.widget.deleter.CameraUtils;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMElem;
import com.tencent.TIMImageElem;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMTextElem;
import com.tencent.TIMValueCallBack;

import java.util.Collections;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;


/**
 * Created by KenChan on 16/7/23.
 */
public class CustomerActivity
        extends CustomerServiceActivity
        implements CameraCallback, TIMValueCallBack<TIMMessage>, BGARefreshLayout
        .BGARefreshLayoutDelegate {

    private TIMConversation conversation;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customerServiceLayout.ibtnPic.setOnClickListener(this);
        customerServiceLayout.ibtnSend.setOnClickListener(this);
        AppTools.defaultRefresh(customerServiceLayout.include.refreshLayout,
                                this);
        EventBus.getDefault()
                .register(this);
        if (ChatManager.isLogin()) {
            String identifier = getIntent().getStringExtra("identifier");
            LogTools.w(identifier);
            if (TextUtils.equals(identifier,
                                 "0") || TextUtils.isEmpty(identifier)) {
                Toast.makeText(CustomerActivity.this,
                               "获取客服会话失败!!",
                               Toast.LENGTH_SHORT)
                     .show();
                finish();
            }
            conversation = TIMManager
                    .getInstance()
                    .getConversation(TIMConversationType.C2C,
                                     identifier);
            conversation.getLocalMessage(10,
                                         null,
                                         new TIMValueCallBack<List<TIMMessage>>() {
                                             @Override
                                             public void onError (int i, String s) {

                                             }

                                             @Override
                                             public void onSuccess (List<TIMMessage> timMessages) {
                                                 //倒序一下list
                                                 Collections.reverse(timMessages);
                                                 onEvent(new ChatListEvent(timMessages));
                                             }
                                         });
        } else {
            Toast.makeText(CustomerActivity.this,
                           "IM登录失败,正在尝试重新登录",
                           Toast.LENGTH_SHORT)
                 .show();
            ChatManager.login();
            finish();
        }


    }

    @Override
    protected void onClick (int id, View view) {
        super.onClick(id,
                      view);
        switch (id) {
            case R.id.ibtn_send:
                String msg = customerServiceLayout.editText.getText()
                                                           .toString();
                TIMTextElem timTextElem = new TIMTextElem();
                timTextElem.setText(msg);
                sendMessage(timTextElem);
                customerServiceLayout.editText.setText("");
                break;
            case R.id.ibtn_pic:
                PhotoPopupWindow photoPopupWindow = new PhotoPopupWindow(this);
                photoPopupWindow.setOnPhotoOptsSelectListener(this);
                photoPopupWindow.showAtDefaultLocation();
                break;
        }
    }

    @Override
    public void onOptsSelect (PhotoPopupOpts opts) {
        switch (opts) {
            case TAKE_PHOTO:
                if (PermissionTools.alreadyHasPermission(this,
                                                         AppKeyMap.CUPCAKE,
                                                         Manifest.permission.CAMERA)) {
                    CameraUtils.openCamera(getApplicationContext(),
                                           this,
                                           AppKeyMap.CUPCAKE);
                }
                break;
            case ALBUM:
                GalleryFinal.openGallerySingle(0,
                                               this);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult (int requestCode,
                                            @NonNull String[] permissions,
                                            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                                         permissions,
                                         grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            CameraUtils.openCamera(getApplicationContext(),
                                   this,
                                   AppKeyMap.CUPCAKE);
        }
    }

    @Override
    public void onHanlderSuccess (int requestCode, List<PhotoInfo> resultList) {
        super.onHanlderSuccess(requestCode,
                               resultList);
        String photoPath = resultList.get(0)
                                     .getPhotoPath();
        TIMImageElem imageElem = new TIMImageElem();
        imageElem.setPath(photoPath);
        sendMessage(imageElem);
    }

    @Override
    public void onPhotoSuccess (String path, int requestCode) {
//        ChatManager.setChattingFlag(orderId);
        TIMImageElem imageElem = new TIMImageElem();
        imageElem.setPath(path);
        sendMessage(imageElem);
    }

    private void sendMessage (TIMElem timElem) {
        TIMMessage timMessage = new TIMMessage();
        timMessage.addElement(timElem);
        timMessage.addElement(ChatManager.getTIMTextElem(ChatManager.OI));
        timMessage.addElement(ChatManager.getTIMTextElem(ChatManager.NICKNAMEELEM));
        timMessage.addElement(ChatManager.getTIMTextElem(ChatManager.UUIDELEM));
        conversation.sendMessage(timMessage,
                                 this);
    }

    //收到消息
    public void onEvent (ChatListEvent event) {
        Observable.from(event.getList())
                  .filter(new Func1<TIMMessage, Boolean>() {
                      @Override
                      public Boolean call (TIMMessage timMessage) {
                          String identifier = getIntent().getStringExtra("identifier");
                          return timMessage.getSender()
                                           .equals(identifier) || timMessage.isSelf();
                      }
                  })
                  .subscribe(new Action1<TIMMessage>() {
                      @Override
                      public void call (TIMMessage timMessage) {
                          OrderMsgListBean bean = new OrderMsgListBean();
                          bean.setSelf(timMessage.isSelf());
                          bean.setTimMessage(timMessage);
                          TIMElem element = timMessage.getElement(0);
                          bean.setTimElem(element);
                          CustomerActivity.this.list.add(bean);
                          multiAdapter.notifyDataSetChanged();
                          scrollToBottom();
                      }
                  });
    }


    @Override
    public void onError (int i, String s) {

    }

    //自己发送消息成功
    @Override
    public void onSuccess (TIMMessage timMessage) {
        OrderMsgListBean bean = new OrderMsgListBean();
        bean.setSelf(timMessage.isSelf());
        bean.setTimMessage(timMessage);
        TIMElem messageElement = timMessage.getElement(0);
        bean.setTimElem(messageElement);
        list.add(bean);
        multiAdapter.notifyDataSetChanged();
        scrollToBottom();
    }

    @Override
    protected void onStop () {
        super.onStop();
        //重置一下未读
        Intent intent = new Intent(this,
                                   ResetUnreadService.class).putExtra("orderId",
                                                                      orderId);
        startService(intent);
    }

    private void scrollToBottom () {
        recyclerView.scrollToPosition(multiAdapter.getItemCount() - 1);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing (BGARefreshLayout bgaRefreshLayout) {
        if (!list.isEmpty()) {
            TIMMessage timMessage = list.get(0)
                                        .getTimMessage();
            conversation.getLocalMessage(10,
                                         timMessage,
                                         new TIMValueCallBack<List<TIMMessage>>() {
                                             @Override
                                             public void onError (int i, String s) {

                                             }

                                             @Override
                                             public void onSuccess (List<TIMMessage> timMessages) {
                                                 if (timMessages.isEmpty()) {
                                                     Toast.makeText(CustomerActivity.this,
                                                                    "历史信息加载完毕",
                                                                    Toast.LENGTH_SHORT)
                                                          .show();
                                                     customerServiceLayout.include.refreshLayout
                                                             .endRefreshing();
                                                     return;
                                                 }
                                                 Collections.reverse(timMessages);
                                                 List<OrderMsgListBean> chatRecord = ChatManager
                                                         .getChatRecord(timMessages);
                                                 if (CustomerActivity.this.list.isEmpty()) {
                                                     CustomerActivity.this.list.addAll(chatRecord);
                                                 } else {
                                                     CustomerActivity.this.list.addAll(0,
                                                                                       chatRecord);
                                                 }
                                                 multiAdapter.notifyDataSetChanged();
                                                 customerServiceLayout.include.refreshLayout
                                                         .endRefreshing();
                                             }
                                         });
        }

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore (BGARefreshLayout bgaRefreshLayout) {
        return false;
    }

}
