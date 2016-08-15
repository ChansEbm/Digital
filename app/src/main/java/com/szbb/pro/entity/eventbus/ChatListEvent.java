package com.szbb.pro.entity.eventbus;

import com.tencent.TIMMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KenChan on 16/7/25.
 */
public class ChatListEvent {
    private List<TIMMessage> list = new ArrayList<>();

    public ChatListEvent(List<TIMMessage> list) {
        this.list = list;
    }

    public List<TIMMessage> getList() {
        return list;
    }
}
