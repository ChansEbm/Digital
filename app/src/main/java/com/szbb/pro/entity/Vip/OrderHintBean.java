package com.szbb.pro.entity.vip;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.szbb.pro.entity.base.BaseBean;

import java.util.List;

/**
 * Created by ChanZeeBm on 2016/2/26.
 */
public class OrderHintBean extends BaseBean {

    /**
     * type : 1
     */

    private CondEntity cond;
    /**
     * title : 你的工单赚了100万
     * is_order : 1
     * sn : A160127141821123
     * content : 你的工单赚了100万,你的工单赚了100万,你的工单赚了100万,
     * addtime : 2016-01-27 15:22
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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public static class ListEntity extends BaseObservable {
        private String title;
        private String is_order;
        private String sn;
        private String content;
        private String addtime;
        private String orderid;

        @Bindable
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Bindable
        public String getIs_order() {
            return is_order;
        }

        public void setIs_order(String is_order) {
            this.is_order = is_order;
        }

        @Bindable
        public String getSn() {
            return "工单号:" + sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        @Bindable
        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Bindable
        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }
    }
}
