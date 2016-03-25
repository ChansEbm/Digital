package com.szbb.pro.entity.Order;

import com.szbb.pro.entity.Base.BaseBean;

import java.util.List;

/**
 * Created by ChanZeeBm on 2016/3/5.
 */
public class OrderMsgBean extends BaseBean {

    /**
     * message_id : 67
     * role : user
     * type : 1
     * nickname : Herbie
     * addtime : 2016-03-17 09:58
     * content : 你好
     * img :
     * img_width : 200
     * img_height : 200
     */

    private InfoEntity info;
    /**
     * message_id : 24
     * role : user
     * type : 1
     * nickname : Herbie
     * addtime : 2016-03-11 12:21
     * content : 我是阿辉
     * img :
     * img_width : 200
     * img_height : 200
     */

    private List<ListEntity> list;

    public void setInfo(InfoEntity info) {
        this.info = info;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public InfoEntity getInfo() {
        return info;
    }

    public List<ListEntity> getList() {
        return list;
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

        public void setMessage_id(String message_id) {
            this.message_id = message_id;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public void setImg_width(String img_width) {
            this.img_width = img_width;
        }

        public void setImg_height(String img_height) {
            this.img_height = img_height;
        }

        public String getMessage_id() {
            return message_id;
        }

        public String getRole() {
            return role;
        }

        public int getType() {
            return type;
        }

        public String getNickname() {
            return nickname;
        }

        public String getAddtime() {
            return addtime;
        }

        public String getContent() {
            return content;
        }

        public String getImg() {
            return img;
        }

        public String getImg_width() {
            return img_width;
        }

        public String getImg_height() {
            return img_height;
        }
    }

    public static class ListEntity {
        private String message_id;
        private String role;
        private String type;
        private String nickname;
        private String addtime;
        private String content;
        private String img;
        private String img_width;
        private String img_height;

        public void setMessage_id(String message_id) {
            this.message_id = message_id;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public void setImg_width(String img_width) {
            this.img_width = img_width;
        }

        public void setImg_height(String img_height) {
            this.img_height = img_height;
        }

        public String getMessage_id() {
            return message_id;
        }

        public String getRole() {
            return role;
        }

        public String getType() {
            return type;
        }

        public String getNickname() {
            return nickname;
        }

        public String getAddtime() {
            return addtime;
        }

        public String getContent() {
            return content;
        }

        public String getImg() {
            return img;
        }

        public String getImg_width() {
            return img_width;
        }

        public String getImg_height() {
            return img_height;
        }
    }
}
