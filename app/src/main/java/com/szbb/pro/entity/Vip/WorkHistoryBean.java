package com.szbb.pro.entity.vip;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.szbb.pro.entity.base.BaseBean;

import java.util.List;

/**
 * Created by ChanZeeBm on 2016/2/25.
 */
public class WorkHistoryBean extends BaseBean {

    /**
     * sn : A160220141355014
     * order_cost : 0.00
     * complete_time : 2016.02.20
     * status_desc : 已完成
     */

    private List<ListEntity> list;

    public List<ListEntity> getList() {
        return list;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public static class ListEntity extends BaseObservable {
        private String sn;
        private String order_cost;
        private String complete_time;
        private String status_desc;
        private String orderid;

        @Bindable
        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        @Bindable
        public String getOrder_cost() {
            return order_cost;
        }

        public void setOrder_cost(String order_cost) {
            this.order_cost = order_cost;
        }

        @Bindable
        public String getComplete_time() {
            return complete_time;
        }

        public void setComplete_time(String complete_time) {
            this.complete_time = complete_time;
        }

        @Bindable
        public String getStatus_desc() {
            return status_desc;
        }

        public void setStatus_desc(String status_desc) {
            this.status_desc = status_desc;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }
    }
}
