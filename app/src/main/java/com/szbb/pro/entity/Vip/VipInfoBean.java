package com.szbb.pro.entity.Vip;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.szbb.pro.entity.Base.BaseBean;

/**
 * Created by ChanZeeBm on 2016/2/24.
 */
public class VipInfoBean extends BaseBean {

    /**
     * nickname : Herbie
     * thumb : http://appbaba.jios.org:8081/Public/uploads/api/2016-01/56a049853ea30.jpg
     * level : 1(ts)
     * order_count : 0
     * status : 已审核
     * score : 999(ts)
     * zan : 5(ts)
     * cai : 2(ts)
     * telephone : 13590592613
     * worker_area_ids : 440000,440600,440604,440604012
     * worker_area_ids_desc : 广东-佛山-禅城-祖庙街道
     * worker_address : 江湾一路十八号
     * card_no : 445122199012121547
     * card_front : http://appbaba.jios.org:8081/Public/uploads/api/2016-01/568a3beae939a.jpg
     * card_back : http://appbaba.jios.org:8081/Public/uploads/api/2016-01/568a3beaeb6b7.jpg
     * has_address : 1
     * addressee_data : {"addressee":"张三","phone":"13590592613","area_ids":"440000,440600,440604,
     * 440604012","area_ids_desc":"广东-佛山-禅城-祖庙街道","detail_address":"江湾一路","postcodes":"528000"}
     */

    private WorkerDataEntity worker_data;
    /**
     * app_service_phone : 1234562323
     */

    private ConfigDataEntity config_data;

    public WorkerDataEntity getWorker_data() {
        return worker_data;
    }

    public void setWorker_data(WorkerDataEntity worker_data) {
        this.worker_data = worker_data;
    }

    public ConfigDataEntity getConfig_data() {
        return config_data;
    }

    public void setConfig_data(ConfigDataEntity config_data) {
        this.config_data = config_data;
    }

    public static class WorkerDataEntity extends BaseObservable {
        private String nickname;
        private String thumb;
        private String level;
        private String order_count;
        private String status;
        private String score;
        private String zan;
        private String cai;
        private String telephone;
        private String worker_area_ids;
        private String worker_area_ids_desc;
        private String worker_address;
        private String card_no;
        private String card_front;
        private String card_back;
        private String has_address;
        /**
         * addressee : 张三
         * phone : 13590592613
         * area_ids : 440000,440600,440604,440604012
         * area_ids_desc : 广东-佛山-禅城-祖庙街道
         * detail_address : 江湾一路
         * postcodes : 528000
         */

        private AddresseeDataEntity addressee_data;

        @Bindable
        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        @Bindable
        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        @Bindable
        public String getOrder_count() {
            return order_count;
        }

        public void setOrder_count(String order_count) {
            this.order_count = order_count;
        }

        @Bindable
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Bindable
        public String getScore() {
            return "积分:" + score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        @Bindable
        public String getZan() {
            return zan;
        }

        public void setZan(String zan) {
            this.zan = zan;
        }

        @Bindable
        public String getCai() {
            return cai;
        }

        public void setCai(String cai) {
            this.cai = cai;
        }

        @Bindable
        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        @Bindable
        public String getWorker_area_ids() {
            return worker_area_ids;
        }

        public void setWorker_area_ids(String worker_area_ids) {
            this.worker_area_ids = worker_area_ids;
        }

        @Bindable
        public String getWorker_area_ids_desc() {
            return worker_area_ids_desc;
        }

        public void setWorker_area_ids_desc(String worker_area_ids_desc) {
            this.worker_area_ids_desc = worker_area_ids_desc;
        }

        @Bindable
        public String getWorker_address() {
            return worker_address;
        }

        public void setWorker_address(String worker_address) {
            this.worker_address = worker_address;
        }

        @Bindable
        public String getCard_no() {
            return card_no;
        }

        public void setCard_no(String card_no) {
            this.card_no = card_no;
        }

        @Bindable
        public String getCard_front() {
            return card_front;
        }

        public void setCard_front(String card_front) {
            this.card_front = card_front;
        }

        @Bindable
        public String getCard_back() {
            return card_back;
        }

        public void setCard_back(String card_back) {
            this.card_back = card_back;
        }

        @Bindable
        public String getHas_address() {
            return has_address;
        }

        public void setHas_address(String has_address) {
            this.has_address = has_address;
        }

        @Bindable
        public AddresseeDataEntity getAddressee_data() {
            return addressee_data;
        }

        public void setAddressee_data(AddresseeDataEntity addressee_data) {
            this.addressee_data = addressee_data;
        }

        public static class AddresseeDataEntity extends BaseObservable {
            private String addressee;
            private String phone;
            private String area_ids;
            private String area_ids_desc;
            private String detail_address;
            private String postcodes;

            @Bindable
            public String getAddressee() {
                return addressee;
            }

            public void setAddressee(String addressee) {
                this.addressee = addressee;
            }

            @Bindable
            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            @Bindable
            public String getArea_ids() {
                return area_ids;
            }

            public void setArea_ids(String area_ids) {
                this.area_ids = area_ids;
            }

            @Bindable
            public String getArea_ids_desc() {
                return area_ids_desc;
            }

            public void setArea_ids_desc(String area_ids_desc) {
                this.area_ids_desc = area_ids_desc;
            }

            @Bindable
            public String getDetail_address() {
                return detail_address;
            }

            public void setDetail_address(String detail_address) {
                this.detail_address = detail_address;
            }

            @Bindable
            public String getPostcodes() {
                return postcodes;
            }

            public void setPostcodes(String postcodes) {
                this.postcodes = postcodes;
            }
        }
    }

    public static class ConfigDataEntity extends BaseObservable {
        private String app_service_phone;

        @Bindable
        public String getApp_service_phone() {
            return app_service_phone;
        }

        public void setApp_service_phone(String app_service_phone) {
            this.app_service_phone = app_service_phone;
        }
    }
}
