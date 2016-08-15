package com.szbb.pro.ui.fragment.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.github.pwittchen.prefser.library.Prefser;
import com.github.pwittchen.prefser.library.TypeToken;
import com.szbb.pro.base.BaseFgm;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.entity.eventbus.ChatListEvent;
import com.szbb.pro.entity.order.MyOrderBean;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.ChatManager;
import com.szbb.pro.tools.LogTools;
import com.tencent.TIMMessage;
import com.tencent.TIMTextElem;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/7/28.
 */

public abstract class OrderSearchBaseFragment<E, T> extends BaseFgm<E, T> {
    //保存新工单列表到本地的key
    public static final String NEWORDERKEY = "nol";
    //保存待服务列表到本地的key
    public static final String SERVICEKEY = "sl";
    //保存待结算列表到本地的key
    public static final String WAITACCOUNTKEY = "wal";

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault()
                     .isRegistered(this)) {
            EventBus.getDefault()
                    .register(this);
        }
    }

    public abstract void searchResult (List<T> list);

    protected void saveListToLocal (String saveKey) {
        Prefser prefser = new Prefser(AppTools.getSharePreferences());
        prefser.put(saveKey,
                    list);
    }

    protected List<MyOrderBean.ListEntity> getLocalList (String saveKey) {
        return new Prefser(AppTools.getSharePreferences()).get(saveKey,
                                                               new TypeToken<List<MyOrderBean
                                                                       .ListEntity>>() {
                                                                   @Override
                                                                   public Type getType () {
                                                                       return super.getType();
                                                                   }
                                                               },
                                                               new ArrayList<MyOrderBean
                                                                       .ListEntity>());
    }

    public void onEvent (ChatListEvent chatListEvent) {
        final List<TIMMessage> timMessageList = chatListEvent.getList();
        ListIterator<MyOrderBean.ListEntity> listEntityListIterator = (ListIterator<MyOrderBean
                .ListEntity>) list.listIterator();
        for (TIMMessage message : timMessageList) {
            TIMTextElem element = (TIMTextElem) message.getElement(1);
            String timeId = element.getText();
            if (TextUtils.equals(ChatManager.getChattingFlag(),
                                 timeId)) { continue; }
            while (listEntityListIterator.hasNext()) {
                MyOrderBean.ListEntity entity = listEntityListIterator.next();
                String orderid = entity.getOrderid();
                if (TextUtils.equals(orderid,
                                     timeId)) {
                    LogTools.w(message.getElementCount() / 4);
                    int unread = Integer.valueOf(entity.getUnread());
                    int incomming = (int) (message.getElementCount() / 4);
                    entity.setUnread(String.valueOf(unread + incomming));
                }
            }
        }
        commonBinderAdapter.notifyDataSetChanged();
    }

}
