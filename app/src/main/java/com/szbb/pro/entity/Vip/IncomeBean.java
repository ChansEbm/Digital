package com.szbb.pro.entity.Vip;

import android.databinding.BaseObservable;

import com.szbb.pro.entity.Base.BaseBean;

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

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity extends BaseObservable {
        private String inid;
        private String sn;
        private String money;
        private String remain_money;
        private String addtime;

        public void setInid(String inid) {
            this.inid = inid;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public void setRemain_money(String remain_money) {
            this.remain_money = remain_money;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getInid() {
            return inid;
        }

        public String getSn() {
            return sn;
        }

        public String getMoney() {
            return money;
        }

        public String getRemain_money() {
            return remain_money;
        }

        public String getAddtime() {
            return addtime;
        }
    }
}
