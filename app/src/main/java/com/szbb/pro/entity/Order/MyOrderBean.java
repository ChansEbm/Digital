package com.szbb.pro.entity.Order;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.tools.AppTools;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ChanZeeBm on 2015/12/28.
 */
public class MyOrderBean extends BaseBean {

    private List<ListEntity> list = new ArrayList<>();
    /**
     * type : 2
     */

    private CondEntity cond;


    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public void setCond(CondEntity cond) {
        this.cond = cond;
    }

    public CondEntity getCond() {
        return cond;
    }

    public static class ListEntity extends BaseObservable {
        private String orderid;
        private String nickname;
        private String tel;
        private String appoint_time;
        private String drop_time;
        private String appoint_drop_time;
        private String address;
        private String lat;
        private String lng;
        private String service_type;
        private String factory_desc;
        private String complete_time = "";
        private String fullType = "";
        private boolean isCommon = false;

        private List<DetailListEntity> detail_list = new ArrayList<>();

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public void setService_type(String service_type) {
            this.service_type = service_type;
        }

        public void setFactory_desc(String factory_desc) {
            this.factory_desc = factory_desc;
        }

        public void setDetail_list(List<DetailListEntity> detail_list) {
            this.detail_list = detail_list;
        }

        public String getOrderid() {
            return orderid;
        }

        @Bindable
        public String getNickname() {
            return nickname;
        }

        @Bindable
        public String getTel() {
            return tel;
        }

        @Bindable
        public String getAddress() {
            return address;
        }

        @Bindable
        public String getLat() {
            return lat;
        }

        @Bindable
        public String getLng() {
            return lng;
        }

        @Bindable
        public String getService_type() {
            return service_type;
        }

        @Bindable
        public String getFactory_desc() {
            return factory_desc;
        }

        public List<DetailListEntity> getDetail_list() {
            return detail_list;
        }

        @Bindable
        public String getAppoint_time() {
            return AppTools.formatTime(new Date(Long.parseLong(appoint_time + "000")));
        }

        public void setAppoint_time(String appoint_time) {
            this.appoint_time = appoint_time;
        }


        public boolean isCommon() {
            return isCommon;
        }

        public void setIsCommon(boolean isCommon) {
            this.isCommon = isCommon;
        }

        @Bindable
        public String getFullType() {
            this.fullType = "";
            return fullType + detail_list.get(0).getStantard() + detail_list.get(0).getName() + "" +
                    "(" +
                    service_type + ")";
        }

        public void setFullType(String fullType) {
            this.fullType = fullType;
        }

        public String getComplete_time() {
            return complete_time;
        }

        public void setComplete_time(String complete_time) {
            this.complete_time = complete_time;
        }

        @Bindable
        public String getDrop_time() {
            long dt = Long.parseLong(this.drop_time + "000");
            long rt = System.currentTimeMillis();
            return AppTools.convertMillions((dt - rt));
        }

        public void setDrop_time(String drop_time) {
            this.drop_time = drop_time;
        }

        @Bindable
        public String getAppoint_drop_time() {
            long dt = Long.parseLong(this.appoint_time + "000");
            long rt = System.currentTimeMillis();
            return AppTools.convertMillions((dt - rt));
        }

        public static class DetailListEntity extends BaseObservable {
            private String name;
            private String brand;
            private String stantard;
            private String model;
            private String fault_lable;
            private String fault_desc;

            public void setName(String name) {
                this.name = name;
            }

            public void setBrand(String brand) {
                this.brand = brand;
            }

            public void setStantard(String stantard) {
                this.stantard = stantard;
            }

            public void setModel(String model) {
                this.model = model;
            }

            public void setFault_lable(String fault_lable) {
                this.fault_lable = fault_lable;
            }

            public void setFault_desc(String fault_desc) {
                this.fault_desc = fault_desc;
            }

            @Bindable
            public String getName() {
                return name;
            }

            @Bindable
            public String getBrand() {
                return brand;
            }

            @Bindable
            public String getStantard() {
                return stantard;
            }

            @Bindable
            public String getModel() {
                return model;
            }

            @Bindable
            public String getFault_lable() {
                return fault_lable;
            }

            @Bindable
            public String getFault_desc() {
                return fault_desc;
            }

        }
    }

    public static class CondEntity {
        private int type;

        public void setType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }
}
