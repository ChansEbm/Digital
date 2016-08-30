package com.szbb.pro.entity.vip;

import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.szbb.pro.entity.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/26.
 */
public class PerformanceBondBean extends BaseBean {


    /**
     * id : 2344
     * money : 4444.00
     * status_desc : 工单：A160704094614060 ，系统结算时自动将 4444.00元 转入 质保金
     * add_time : 1470966842
     * order_id : 8732
     */

    private List<ListBean> list;

    public List<ListBean> getList () { return list;}

    public void setList (List<ListBean> list) { this.list = list;}

    public static class ListBean extends BaseBean implements Parcelable {
        private String id;
        private String money;
        private String status_desc;
        private String add_time;
        private String order_id;

        @Bindable
        public String getId () { return id;}

        public void setId (String id) { this.id = id;}

        @Bindable
        public String getMoney () { return money;}

        public void setMoney (String money) { this.money = money;}

        @Bindable
        public String getStatus_desc () { return status_desc;}

        public void setStatus_desc (String status_desc) { this.status_desc = status_desc;}

        @Bindable
        public String getAdd_time () { return add_time;}

        public void setAdd_time (String add_time) { this.add_time = add_time;}

        @Bindable
        public String getOrder_id () { return order_id;}

        public void setOrder_id (String order_id) { this.order_id = order_id;}

        @Override public int describeContents () { return 0; }

        @Override public void writeToParcel (Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.money);
            dest.writeString(this.status_desc);
            dest.writeString(this.add_time);
            dest.writeString(this.order_id);
        }

        public ListBean () {}

        protected ListBean (Parcel in) {
            this.id = in.readString();
            this.money = in.readString();
            this.status_desc = in.readString();
            this.add_time = in.readString();
            this.order_id = in.readString();
        }

        public static final Parcelable.Creator<ListBean> CREATOR = new Parcelable.Creator<ListBean>() {
            @Override public ListBean createFromParcel (Parcel source) {
                return new ListBean(source);
            }

            @Override public ListBean[] newArray (int size) {return new ListBean[size];}
        };
    }
}
