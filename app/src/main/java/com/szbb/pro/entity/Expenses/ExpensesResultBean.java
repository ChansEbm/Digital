package com.szbb.pro.entity.Expenses;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.szbb.pro.entity.Base.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChanZeeBm on 2015/11/19.
 */
//费用申请详情
public class ExpensesResultBean extends BaseBean {

    /**
     * detailid : 285
     * costid : 47
     * cost_status : 审核通过
     * sn : 160127397534
     * addtime : 2016-01-27 14:16
     * cost_use : 你就
     * cost_type : 1
     * cost_type_desc : 远程上门费用
     * money : 23.00
     * cost_img : [{"name":"56a860c586484.jpg","url":"http://192.168.200
     * .69:8081/Public/uploads/api/2016-01/56a860c586484.jpg"}]
     * remarks : 2355655
     * reply :
     */

    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity extends BaseObservable implements Parcelable {

        public static final Creator<DataEntity> CREATOR = new Creator<DataEntity>() {
            public DataEntity createFromParcel(Parcel source) {
                return new DataEntity(source);
            }

            public DataEntity[] newArray(int size) {
                return new DataEntity[size];
            }
        };
        private String detailid;
        private String status;
        private String costid;
        private String cost_status;
        private String sn;
        private String addtime;
        private String cost_use;
        private String cost_type;
        private String cost_type_desc;
        private String money;
        private String remarks;
        private String reply;
        private boolean isNotPass = false;
        /**
         * name : 56a860c586484.jpg
         * url : http://192.168.200.69:8081/Public/uploads/api/2016-01/56a860c586484.jpg
         */

        private List<CostImgEntity> cost_img;

        public DataEntity() {
        }

        protected DataEntity(Parcel in) {
            this.detailid = in.readString();
            this.status = in.readString();
            this.costid = in.readString();
            this.cost_status = in.readString();
            this.sn = in.readString();
            this.addtime = in.readString();
            this.cost_use = in.readString();
            this.cost_type = in.readString();
            this.cost_type_desc = in.readString();
            this.money = in.readString();
            this.remarks = in.readString();
            this.reply = in.readString();
            this.isNotPass = in.readByte() != 0;
            this.cost_img = new ArrayList<CostImgEntity>();
            in.readList(this.cost_img, List.class.getClassLoader());
        }

        public String getDetailid() {
            return detailid;
        }

        public void setDetailid(String detailid) {
            this.detailid = detailid;
        }

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
        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        @Bindable
        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
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
            return cost_type_desc;
        }

        public void setCost_type_desc(String cost_type_desc) {
            this.cost_type_desc = cost_type_desc;
        }

        @Bindable
        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        @Bindable
        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        @Bindable
        public String getReply() {
            return reply;
        }

        public void setReply(String reply) {
            this.reply = reply;
        }

        public List<CostImgEntity> getCost_img() {
            return cost_img;
        }

        public void setCost_img(List<CostImgEntity> cost_img) {
            this.cost_img = cost_img;
        }

        @Bindable
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.isNotPass = TextUtils.equals(status.trim(), "2");
            this.status = status;
        }

        @Bindable
        public boolean isNotPass() {
            return TextUtils.equals(status.trim(), "2");
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.detailid);
            dest.writeString(this.status);
            dest.writeString(this.costid);
            dest.writeString(this.cost_status);
            dest.writeString(this.sn);
            dest.writeString(this.addtime);
            dest.writeString(this.cost_use);
            dest.writeString(this.cost_type);
            dest.writeString(this.cost_type_desc);
            dest.writeString(this.money);
            dest.writeString(this.remarks);
            dest.writeString(this.reply);
            dest.writeByte(isNotPass ? (byte) 1 : (byte) 0);
            dest.writeList(this.cost_img);
        }

        public static class CostImgEntity extends BaseObservable {
            private String name;
            private String url;

            @Bindable
            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            @Bindable
            public String getUrl() {
                return url;
            }


            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
