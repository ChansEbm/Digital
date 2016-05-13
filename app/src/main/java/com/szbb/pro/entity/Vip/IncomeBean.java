package com.szbb.pro.entity.vip;

import android.databinding.BaseObservable;

import com.szbb.pro.entity.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2/28/2016.
 */
public class IncomeBean extends BaseBean {

    /**
     * inid : 1
     * sn : A15616d51fd5
     * money : 91.00
     * remain_money : 91.00
     * addtime : 2016-01-15 15:18
     */

    private List<ListEntity> list;

    public List<ListEntity> getList() {
        return list;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public static class ListEntity extends BaseObservable {
        private String inid;
        private String sn;
        private String money;
        private String orderid;
        private String remain_money;
        private String addtime;

        public String getInid() {
            return inid;
        }

        public void setInid(String inid) {
            this.inid = inid;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getRemain_money() {
            return remain_money;
        }

        public void setRemain_money(String remain_money) {
            this.remain_money = remain_money;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getOrderId() {
            return orderid;
        }

        public void setOrderId(String orderId) {
            this.orderid = orderId;
        }
    }
}
