package com.szbb.pro.entity.vip;

import android.databinding.BaseObservable;

import com.szbb.pro.entity.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2/28/2016.
 */
public class OtherCostBean extends BaseBean {

    /**
     * ocostid : 5
     * money : +20.00
     * status_desc : 奖励
     * addtime : 2016-02-19 10:44
     */

    private List<ListEntity> list;

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity extends BaseObservable {
        private String ocostid;
        private String money;
        private String status_desc;
        private String addtime;

        public void setOcostid(String ocostid) {
            this.ocostid = ocostid;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public void setStatus_desc(String status_desc) {
            this.status_desc = status_desc;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getOcostid() {
            return ocostid;
        }

        public String getMoney() {
            return money;
        }

        public String getStatus_desc() {
            return status_desc;
        }

        public String getAddtime() {
            return addtime;
        }
    }
}
