package com.szbb.pro.tools;

import android.support.annotation.IntDef;
import android.text.TextUtils;

import com.github.pwittchen.prefser.library.Prefser;
import com.szbb.pro.AppKeyMap;
import com.szbb.pro.entity.login.AuthBean;
import com.szbb.pro.entity.order.OrderMsgListBean;
import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMTextElem;
import com.tencent.TIMUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KenChan on 16/7/23.
 */
public class ChatManager {

    public static final int OI = 0x01;
    public static final int NICKNAMEELEM = 0x02;
    public static final int UUIDELEM = 0x03;
    private static String chattingFlag = "";

    @IntDef({OI, NICKNAMEELEM, UUIDELEM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ElemRange {
    }

    private static final int SIG = 0x001;
    private static final int IDENTIFIER = 0x002;

    @IntDef({SIG, IDENTIFIER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SigRange {

    }

    public static void login() {
        TIMUser user = new TIMUser();
        user.setAccountType("6474");
        user.setIdentifier(getLoginSig(IDENTIFIER));
        user.setAppIdAt3rd("6474");

        TIMManager.getInstance()
                  .login(1400012398,
                         user,
                         getLoginSig(SIG),
                         new TIMCallBack() {
                             @Override
                             public void onError(int i, String s) {

                             }

                             @Override
                             public void onSuccess() {

                             }
                         });
    }

    private static String getLoginSig(@SigRange int type) {
        Prefser prefser = new Prefser(AppTools.getSharePreferences());
        switch (type) {
            case SIG:
                return prefser.get(AppKeyMap.SIG,
                                   String.class,
                                   "");
            case IDENTIFIER:
                return prefser.get(AppKeyMap.IDENTIFIER,
                                   String.class,
                                   "");
        }
        return "";
    }

    public static boolean isLogin() {
        return !TextUtils.isEmpty(TIMManager.getInstance()
                                            .getLoginUser());
    }

    public static List<OrderMsgListBean> getChatRecord(List<TIMMessage> messages) {
        List<OrderMsgListBean> list = new ArrayList<>();
        OrderMsgListBean bean = new OrderMsgListBean();

        for (TIMMessage message : messages) {
            bean.setTimMessage(message);
            bean.setSelf(message.isSelf());
            bean.setTimElem(message.getElement(0));
            list.add(bean);
        }
        return list;
    }

    public static void setChattingFlag(String chattingFlag) {
        ChatManager.chattingFlag = TextUtils.isEmpty(chattingFlag) ? "" : chattingFlag;
    }

    public static String getChattingFlag() {
        return chattingFlag;
    }

    public static TIMTextElem getTIMTextElem(@ElemRange int type) {
        TIMTextElem elem = new TIMTextElem();
        switch (type) {
            case OI:
                if (TextUtils.isEmpty(chattingFlag)) {
                    LogTools.e(
                            "ChattingFlag is in a null reference,please invoke setChattingFlat() " +
                                    "method first");
                    return elem;
                }
                elem.setText(chattingFlag);
                break;
            case NICKNAMEELEM:
                AuthBean authBean = new Prefser(AppTools.getSharePreferences()).get("authInfo",
                                                                                    AuthBean.class,
                                                                                    new AuthBean());
                String nickname = authBean.getWorker_info()
                                          .getNickname();
                elem.setText(nickname);
                break;
            case UUIDELEM:
                elem.setText(MiscUtils.getUUID());
                break;
        }
        return elem;
    }


}
