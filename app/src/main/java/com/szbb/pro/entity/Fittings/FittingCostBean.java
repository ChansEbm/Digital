package com.szbb.pro.entity.Fittings;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.szbb.pro.entity.Base.BaseBean;

import java.util.List;

/**
 * Created by ChanZeeBm on 2016/2/18.
 */
public class FittingCostBean extends BaseBean {

    /**
     * status : 0
     */

    private CondEntity cond;
    /**
     * costid : 61
     * cost_status : 审核通过
     * cost_use : 1
     * cost_type : 2
     * cost_type_desc : 购买配件费用
     * money : 1.00
     * addtime : 2016-01-28 10:58
     * remarks :
     * name : CRT
     * brand : 三星
     * stantard : 32寸以下
     * model : 959NF
     * address : 人民路97号马哥孛罗酒店
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
        private int status;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    public static class ListEntity extends BaseObservable {
        private String costid;
        private String cost_status;
        private String cost_use;
        private String cost_type;
        private String cost_type_desc;
        private String money;
        private String addtime;
        private String remarks;
        private String name;
        private String brand;
        private String stantard;
        private String model;
        private String address;

        @Bindable
        public String getCostid() {
            return costid;
        }

        public void setCostid(String costid) {
            this.costid = costid;
        }

        @Bindable
        public String getCost_status() {
            return cost_status;
        }

        public void setCost_status(String cost_status) {
            this.cost_status = cost_status;
        }

        @Bindable
        public String getCost_use() {
            return cost_use;
        }

        public void setCost_use(String cost_use) {
            this.cost_use = cost_use;
        }

        @Bindable
        public String getCost_type() {
            return cost_type;
        }

        public void setCost_type(String cost_type) {
            this.cost_type = cost_type;
        }

        @Bindable
        public String getCost_type_desc() {
            return cost_type_desc+ ": ";
        }

        public void setCost_type_desc(String cost_type_desc) {
            this.cost_type_desc = cost_type_desc ;
        }

        @Bindable
        public String getMoney() {
            return "¥" + money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        @Bindable
        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        @Bindable
        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
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
        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
