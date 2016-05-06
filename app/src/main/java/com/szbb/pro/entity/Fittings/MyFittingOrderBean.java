package com.szbb.pro.entity.Fittings;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.szbb.pro.entity.Base.BaseBean;

import java.util.List;

/**
 * Created by ChanZeeBm on 2016/2/17.
 */
public class MyFittingOrderBean extends BaseBean {


    private CondEntity cond;
    /**
     * acceid : 76
     * exe_type : 2
     * exe_status : 3
     * exe_desc : 配件单完成
     * address : 广东-佛山-禅城-石湾镇街道以一体机以一句与击溃与积极汇集一个哥哥一样
     * addtime : 2016-01-27 14:12
     * acce_name : 开关电容
     * acce_brand : 雷虹电子有限公司
     * acce_type : 电路板配件
     * name : 液晶电视1
     * brand : 三星
     * stantard : 56-65寸
     * model : UA65JU5900
     */

    private List<ListEntity> list;

    public CondEntity getCond() {
        return cond;
    }

    public void setCond(CondEntity cond) {
        this.cond = cond;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public static class CondEntity {
        private int type;
        private int status;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    public static class ListEntity extends BaseObservable {
        private String acceid;
        private String exe_type;
        private String exe_status;
        private String exe_desc;
        private String address;
        private String addtime;
        private String acce_name;
        private String acce_brand;
        private String acce_type;
        private String acce_model;
        private String name;
        private String brand;
        private String stantard;
        private String model;

        public String getAcceid() {
            return acceid;
        }

        public void setAcceid(String acceid) {
            this.acceid = acceid;
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

        @Bindable
        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        @Bindable
        public String getAcce_name() {
            return acce_name;
        }

        public void setAcce_name(String acce_name) {
            this.acce_name = acce_name;
        }

        @Bindable
        public String getAcce_brand() {
            return acce_brand;
        }

        public void setAcce_brand(String acce_brand) {
            this.acce_brand = acce_brand;
        }

        public String getAcce_type() {
            return acce_type;
        }

        public void setAcce_type(String acce_type) {
            this.acce_type = acce_type;
        }

        @Bindable
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Bindable
        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        @Bindable
        public String getStantard() {
            return stantard;
        }

        public void setStantard(String stantard) {
            this.stantard = stantard;
        }

        @Bindable
        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        @Bindable
        public String getAcce_model() {
            return acce_model;
        }

        public void setAcce_model(String acce_model) {
            this.acce_model = acce_model;
        }
    }
}
