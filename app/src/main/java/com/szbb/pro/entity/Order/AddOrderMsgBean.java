package com.szbb.pro.entity.Order;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.szbb.pro.entity.Base.BaseBean;

import java.util.List;

/**
 * Created by ChanZeeBm on 2016/3/17.
 */
public class AddOrderMsgBean extends BaseBean {

    private InfoEntity info;

    private List<ListEntity> list;

    public InfoEntity getInfo() {
        return info;
    }

    public void setInfo(InfoEntity info) {
        this.info = info;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public static class InfoEntity {
        private String message_id;
        private String role;
        private int type;
        private String nickname;
        private String addtime;
        private String content;
        private String img;
        private String img_width;
        private String img_height;

        public String getMessage_id() {
            return message_id;
        }

        public void setMessage_id(String message_id) {
            this.message_id = message_id;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getImg_width() {
            return img_width;
        }

        public void setImg_width(String img_width) {
            this.img_width = img_width;
        }

        public String getImg_height() {
            return img_height;
        }

        public void setImg_height(String img_height) {
            this.img_height = img_height;
        }
    }

    public static class ListEntity extends BaseObservable {
        private String message_id;
        private String role;
        private String type;
        private String nickname;
        private String addtime;
        private String content;
        private String img;
        private String img_width;
        private String img_height;


        @Bindable
        public String getMessage_id() {
            return message_id;
        }

        public void setMessage_id(String message_id) {
            this.message_id = message_id;
        }

        @Bindable
        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        @Bindable
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Bindable
        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        @Bindable
        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        @Bindable
        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Bindable
        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        @Bindable
        public String getImg_width() {
            return img_width;
        }

        public void setImg_width(String img_width) {
            this.img_width = img_width;
        }

        @Bindable
        public String getImg_height() {
            return img_height;
        }

        public void setImg_height(String img_height) {
            this.img_height = img_height;
        }
    }
}
