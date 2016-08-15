package com.szbb.pro.entity.login;

import com.szbb.pro.entity.base.BaseBean;

/**
 * Created by ChanZeeBm on 2015/12/26.
 */
public class AuthBean extends BaseBean {


    private String auth;
    private int is_complete_info;
    private int is_check;
    private String userId;
    private String uid;
    private WorkerInfoBean worker_info;
    private String identifier = "";
    private String sig = "";

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public int getIs_complete_info() {
        return is_complete_info;
    }

    public void setIs_complete_info(int is_complete_info) {
        this.is_complete_info = is_complete_info;
    }

    public int getIs_check() {
        return is_check;
    }

    public void setIs_check(int is_check) {
        this.is_check = is_check;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public WorkerInfoBean getWorker_info() {
        return worker_info;
    }

    public void setWorker_info(WorkerInfoBean worker_info) {
        this.worker_info = worker_info;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public static class WorkerInfoBean {
        private String nickname;
        private String worker_area_ids;
        private String worker_area_ids_desc;
        private String worker_address;
        private String lat;
        private String lng;
        private String card_no;
        private String thumb;
        private String card_front;
        private String card_back;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getWorker_area_ids() {
            return worker_area_ids;
        }

        public void setWorker_area_ids(String worker_area_ids) {
            this.worker_area_ids = worker_area_ids;
        }

        public String getWorker_area_ids_desc() {
            return worker_area_ids_desc;
        }

        public void setWorker_area_ids_desc(String worker_area_ids_desc) {
            this.worker_area_ids_desc = worker_area_ids_desc;
        }

        public String getWorker_address() {
            return worker_address;
        }

        public void setWorker_address(String worker_address) {
            this.worker_address = worker_address;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getCard_no() {
            return card_no;
        }

        public void setCard_no(String card_no) {
            this.card_no = card_no;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getCard_front() {
            return card_front;
        }

        public void setCard_front(String card_front) {
            this.card_front = card_front;
        }

        public String getCard_back() {
            return card_back;
        }

        public void setCard_back(String card_back) {
            this.card_back = card_back;
        }
    }
}
