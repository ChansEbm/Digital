package com.szbb.pro.entity;

import android.databinding.BaseObservable;

import java.util.List;

/**
 * Created by ChanZeeBm on 2016/1/18.
 */
public class Tx extends BaseObservable {

    private int errorcode;
    private String msg;


    private DataEntity data;


    private List<ListEntity> list;

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public static class DataEntity {
        private String orderid;
        private String sn;
        private String order_status_desc;
        private String appoint_time;
        private String is_sign_in;
        private String sign_in_time;
        private String distribute_time;
        private String is_all_repair;
        private String is_submit_complete;
        private String is_apply_acce;
        private String is_apply_cost;
        private String nickname;
        private String tel;
        private String est_miles;
        private String address;
        private String lat;
        private String lng;
        private String subsidy;
        private String service_type;
        private String factory_logo;
        private String factory_desc;
        private String factory_technology_tel;
        private String acce_exe_type;
        private String has_acce_cost;
        private String acce_cost_title;
        private String service_pro;
        /**
         * handle_type : 1
         * exe_type : 0
         * exe_status : 1
         * exe_desc : 审核通过
         * title : 远程上门费用5.00元
         * addtime : 1456128701
         */

        private List<AcceCostListEntity> acce_cost_list;

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getOrder_status_desc() {
            return order_status_desc;
        }

        public void setOrder_status_desc(String order_status_desc) {
            this.order_status_desc = order_status_desc;
        }

        public String getAppoint_time() {
            return appoint_time;
        }

        public void setAppoint_time(String appoint_time) {
            this.appoint_time = appoint_time;
        }

        public String getIs_sign_in() {
            return is_sign_in;
        }

        public void setIs_sign_in(String is_sign_in) {
            this.is_sign_in = is_sign_in;
        }

        public String getSign_in_time() {
            return sign_in_time;
        }

        public void setSign_in_time(String sign_in_time) {
            this.sign_in_time = sign_in_time;
        }

        public String getDistribute_time() {
            return distribute_time;
        }

        public void setDistribute_time(String distribute_time) {
            this.distribute_time = distribute_time;
        }

        public String getIs_all_repair() {
            return is_all_repair;
        }

        public void setIs_all_repair(String is_all_repair) {
            this.is_all_repair = is_all_repair;
        }

        public String getIs_submit_complete() {
            return is_submit_complete;
        }

        public void setIs_submit_complete(String is_submit_complete) {
            this.is_submit_complete = is_submit_complete;
        }

        public String getIs_apply_acce() {
            return is_apply_acce;
        }

        public void setIs_apply_acce(String is_apply_acce) {
            this.is_apply_acce = is_apply_acce;
        }

        public String getIs_apply_cost() {
            return is_apply_cost;
        }

        public void setIs_apply_cost(String is_apply_cost) {
            this.is_apply_cost = is_apply_cost;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getEst_miles() {
            return est_miles;
        }

        public void setEst_miles(String est_miles) {
            this.est_miles = est_miles;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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

        public String getSubsidy() {
            return subsidy;
        }

        public void setSubsidy(String subsidy) {
            this.subsidy = subsidy;
        }

        public String getService_type() {
            return service_type;
        }

        public void setService_type(String service_type) {
            this.service_type = service_type;
        }

        public String getFactory_logo() {
            return factory_logo;
        }

        public void setFactory_logo(String factory_logo) {
            this.factory_logo = factory_logo;
        }

        public String getFactory_desc() {
            return factory_desc;
        }

        public void setFactory_desc(String factory_desc) {
            this.factory_desc = factory_desc;
        }

        public String getFactory_technology_tel() {
            return factory_technology_tel;
        }

        public void setFactory_technology_tel(String factory_technology_tel) {
            this.factory_technology_tel = factory_technology_tel;
        }

        public String getAcce_exe_type() {
            return acce_exe_type;
        }

        public void setAcce_exe_type(String acce_exe_type) {
            this.acce_exe_type = acce_exe_type;
        }

        public String getHas_acce_cost() {
            return has_acce_cost;
        }

        public void setHas_acce_cost(String has_acce_cost) {
            this.has_acce_cost = has_acce_cost;
        }

        public String getAcce_cost_title() {
            return acce_cost_title;
        }

        public void setAcce_cost_title(String acce_cost_title) {
            this.acce_cost_title = acce_cost_title;
        }

        public String getService_pro() {
            return service_pro;
        }

        public void setService_pro(String service_pro) {
            this.service_pro = service_pro;
        }

        public List<AcceCostListEntity> getAcce_cost_list() {
            return acce_cost_list;
        }

        public void setAcce_cost_list(List<AcceCostListEntity> acce_cost_list) {
            this.acce_cost_list = acce_cost_list;
        }

        public static class AcceCostListEntity {
            private String handle_type;
            private String exe_type;
            private String exe_status;
            private String exe_desc;
            private String title;
            private String addtime;

            public String getHandle_type() {
                return handle_type;
            }

            public void setHandle_type(String handle_type) {
                this.handle_type = handle_type;
            }

            public String getExe_type() {
                return exe_type;
            }

            public void setExe_type(String exe_type) {
                this.exe_type = exe_type;
            }

            public String getExe_status() {
                return exe_status;
            }

            public void setExe_status(String exe_status) {
                this.exe_status = exe_status;
            }

            public String getExe_desc() {
                return exe_desc;
            }

            public void setExe_desc(String exe_desc) {
                this.exe_desc = exe_desc;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }
        }
    }

    public static class ListEntity {
        private String detailid;
        private String serviceid;
        private String productid;
        private String name;
        private String brand;
        private String stantard;
        private String model;
        private String fault_lable;
        private String fault_desc;
        private String product_thumb;
        private String service_id;
        private String service_name;
        private String acce_exe_type;
        private String last_handle_type;
        private String last_handle_status;
        private String last_handle_desc;
        private String complete_report;
        /**
         * service_id : 81
         * service_name : 调试
         * service_cost : 50.00
         */

        private List<ServiceListEntity> service_list;
        /**
         * name : 56cac475e4ce2.jpg
         * url : http://appbaba.jios.org:8081/Public/uploads/api/2016-02/56cac475e4ce2.jpg
         */

        private List<CompletePhotosEntity> complete_photos;

        public String getDetailid() {
            return detailid;
        }

        public void setDetailid(String detailid) {
            this.detailid = detailid;
        }

        public String getServiceid() {
            return serviceid;
        }

        public void setServiceid(String serviceid) {
            this.serviceid = serviceid;
        }

        public String getProductid() {
            return productid;
        }

        public void setProductid(String productid) {
            this.productid = productid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getStantard() {
            return stantard;
        }

        public void setStantard(String stantard) {
            this.stantard = stantard;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getFault_lable() {
            return fault_lable;
        }

        public void setFault_lable(String fault_lable) {
            this.fault_lable = fault_lable;
        }

        public String getFault_desc() {
            return fault_desc;
        }

        public void setFault_desc(String fault_desc) {
            this.fault_desc = fault_desc;
        }

        public String getProduct_thumb() {
            return product_thumb;
        }

        public void setProduct_thumb(String product_thumb) {
            this.product_thumb = product_thumb;
        }

        public String getService_id() {
            return service_id;
        }

        public void setService_id(String service_id) {
            this.service_id = service_id;
        }

        public String getService_name() {
            return service_name;
        }

        public void setService_name(String service_name) {
            this.service_name = service_name;
        }

        public String getAcce_exe_type() {
            return acce_exe_type;
        }

        public void setAcce_exe_type(String acce_exe_type) {
            this.acce_exe_type = acce_exe_type;
        }

        public String getLast_handle_type() {
            return last_handle_type;
        }

        public void setLast_handle_type(String last_handle_type) {
            this.last_handle_type = last_handle_type;
        }

        public String getLast_handle_status() {
            return last_handle_status;
        }

        public void setLast_handle_status(String last_handle_status) {
            this.last_handle_status = last_handle_status;
        }

        public String getLast_handle_desc() {
            return last_handle_desc;
        }

        public void setLast_handle_desc(String last_handle_desc) {
            this.last_handle_desc = last_handle_desc;
        }

        public String getComplete_report() {
            return complete_report;
        }

        public void setComplete_report(String complete_report) {
            this.complete_report = complete_report;
        }

        public List<ServiceListEntity> getService_list() {
            return service_list;
        }

        public void setService_list(List<ServiceListEntity> service_list) {
            this.service_list = service_list;
        }

        public List<CompletePhotosEntity> getComplete_photos() {
            return complete_photos;
        }

        public void setComplete_photos(List<CompletePhotosEntity> complete_photos) {
            this.complete_photos = complete_photos;
        }

        public static class ServiceListEntity {
            private String service_id;
            private String service_name;
            private String service_cost;

            public String getService_id() {
                return service_id;
            }

            public void setService_id(String service_id) {
                this.service_id = service_id;
            }

            public String getService_name() {
                return service_name;
            }

            public void setService_name(String service_name) {
                this.service_name = service_name;
            }

            public String getService_cost() {
                return service_cost;
            }

            public void setService_cost(String service_cost) {
                this.service_cost = service_cost;
            }
        }

        public static class CompletePhotosEntity {
            private String name;
            private String url;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
