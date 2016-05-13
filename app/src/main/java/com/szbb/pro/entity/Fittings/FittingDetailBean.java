package com.szbb.pro.entity.fittings;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.tools.AppTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChanZeeBm on 2016/2/18.
 */
public class FittingDetailBean extends BaseBean {
    /**
     * orderid : 217
     * detailid : 296
     * serviceid : 27
     * acceid : 79
     * sn : 160128037782
     * addtime : 2016-01-28 10:27
     * fact_shipping_type : 暂无
     * fact_shipping_num : 暂无
     * fact_shipping_time : 暂无
     * applicant : Herbie
     * applicant_tell : 13590592613
     * address : 江湾一路十八号
     * factory : 广州市悦悦电器有限公司
     * factory_logo : http://192.168.200.69:8081/Public/uploads/pics/2016-01/569360cfe78da.jpg
     * exe_type : 2
     * exe_status : 1
     * exe_desc : 回寄配件等待入库
     * acce_list : [{"detail_id":"176","is_normal":"0","acce_id":"107","acce_name":"SDF",
     * "acce_type":"压缩机","acce_brand":"松下","acce_model":"hfdug3444","acce_remark":"",
     * "acce_thumb":"http://192.168.200.69:8081/Public/uploads/api/2016-01/56a08047d01cd.jpg",
     * "acce_num":"1"}]
     * acce_count : 2
     * shipping_type : 狙击
     * shipping_num : 咕咕咕
     * shipping_paytype : 到付
     * shipping_money : 5.00
     * acce_photos : [{"name":"56a97c85c36b9.jpg","url":"http://192.168.200
     * .69:8081/Public/uploads/api/2016-01/56a97c85c36b9.jpg"}]
     * remarks :
     * fact_reply_desc : 厂家回复内容
     */

    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity extends BaseObservable implements Parcelable {

        private String orderid;
        private String detailid;
        private String serviceid;
        private String acceid;
        private String sn;
        private String addtime;
        private String fact_shipping_type;
        private String fact_shipping_num;
        private String fact_shipping_time;
        private String applicant;
        private String applicant_tell;
        private String address;
        private String factory;
        private String factory_logo;
        private String exe_type;
        private String exe_status;
        private String exe_desc;
        private String acce_count;
        private String shipping_type;
        private String shipping_num;
        private String shipping_paytype;
        private String shipping_money;
        private String remarks;
        private String fact_reply_desc;
        private int totalCount = 0;
        /**
         * detail_id : 176
         * is_normal : 0
         * acce_id : 107
         * acce_name : SDF
         * acce_type : 压缩机
         * acce_brand : 松下
         * acce_model : hfdug3444
         * acce_remark :
         * acce_thumb : http://192.168.200.69:8081/Public/uploads/api/2016-01/56a08047d01cd.jpg
         * acce_num : 1
         */

        private ArrayList<AcceListEntity> acce_list;
        /**
         * name : 56a97c85c36b9.jpg
         * url : http://192.168.200.69:8081/Public/uploads/api/2016-01/56a97c85c36b9.jpg
         */

        private List<AccePhotosEntity> acce_photos;

        @Bindable
        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        @Bindable
        public String getDetailid() {
            return detailid;
        }

        public void setDetailid(String detailid) {
            this.detailid = detailid;
        }

        @Bindable
        public String getServiceid() {
            return serviceid;
        }

        public void setServiceid(String serviceid) {
            this.serviceid = serviceid;
        }

        @Bindable
        public String getAcceid() {
            return acceid;
        }

        public void setAcceid(String acceid) {
            this.acceid = acceid;
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
        public String getFact_shipping_type() {
            return fact_shipping_type;
        }

        public void setFact_shipping_type(String fact_shipping_type) {
            this.fact_shipping_type = fact_shipping_type;
        }

        @Bindable
        public String getFact_shipping_num() {
            return fact_shipping_num;
        }

        public void setFact_shipping_num(String fact_shipping_num) {
            this.fact_shipping_num = fact_shipping_num;
        }

        @Bindable
        public String getFact_shipping_time() {
            this.fact_shipping_time = AppTools.formatTime(fact_shipping_time, true);
            return fact_shipping_time;
        }

        public void setFact_shipping_time(String fact_shipping_time) {
            this.fact_shipping_time = fact_shipping_time;
        }

        @Bindable
        public String getApplicant() {
            return applicant;
        }

        public void setApplicant(String applicant) {
            this.applicant = applicant;
        }

        @Bindable
        public String getApplicant_tell() {
            return applicant_tell;
        }

        public void setApplicant_tell(String applicant_tell) {
            this.applicant_tell = applicant_tell;
        }

        @Bindable
        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @Bindable
        public String getFactory() {
            return factory;
        }

        public void setFactory(String factory) {
            this.factory = factory;
        }

        @Bindable
        public String getFactory_logo() {
            return factory_logo;
        }

        public void setFactory_logo(String factory_logo) {
            this.factory_logo = factory_logo;
        }

        @Bindable
        public String getExe_type() {
            return exe_type;
        }

        public void setExe_type(String exe_type) {
            this.exe_type = exe_type;
        }

        @Bindable
        public String getExe_status() {
            return exe_status;
        }

        public void setExe_status(String exe_status) {
            this.exe_status = exe_status;
        }

        @Bindable
        public String getExe_desc() {
            return exe_desc;
        }

        public void setExe_desc(String exe_desc) {
            this.exe_desc = exe_desc;
        }

        @Bindable
        public String getAcce_count() {
            return acce_count;
        }

        public void setAcce_count(String acce_count) {
            this.acce_count = acce_count;
        }

        @Bindable
        public String getShipping_type() {
            return shipping_type;
        }

        public void setShipping_type(String shipping_type) {
            this.shipping_type = shipping_type;
        }

        @Bindable
        public String getShipping_num() {
            return shipping_num;
        }

        public void setShipping_num(String shipping_num) {
            this.shipping_num = shipping_num;
        }

        @Bindable
        public String getShipping_paytype() {
            return shipping_paytype;
        }

        public void setShipping_paytype(String shipping_paytype) {
            this.shipping_paytype = shipping_paytype;
        }

        @Bindable
        public String getShipping_money() {
            return shipping_money;
        }

        public void setShipping_money(String shipping_money) {
            this.shipping_money = shipping_money;
        }

        @Bindable
        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        @Bindable
        public String getFact_reply_desc() {
            return fact_reply_desc;
        }

        public void setFact_reply_desc(String fact_reply_desc) {
            this.fact_reply_desc = fact_reply_desc;
        }

        public ArrayList<AcceListEntity> getAcce_list() {
            return acce_list;
        }

        public void setAcce_list(ArrayList<AcceListEntity> acce_list) {
            this.acce_list = acce_list;
        }

        public List<AccePhotosEntity> getAcce_photos() {
            return acce_photos;
        }

        public void setAcce_photos(List<AccePhotosEntity> acce_photos) {
            this.acce_photos = acce_photos;
        }

        @Bindable
        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public static class AcceListEntity extends BaseObservable implements Parcelable {
            public static final Parcelable.Creator<AcceListEntity> CREATOR = new Parcelable
                    .Creator<AcceListEntity>() {
                public AcceListEntity createFromParcel(Parcel source) {
                    return new AcceListEntity(source);
                }

                public AcceListEntity[] newArray(int size) {
                    return new AcceListEntity[size];
                }
            };
            private String detail_id;
            private String is_normal;
            private String acce_id;
            private String acce_name;
            private String acce_type;
            private String acce_brand;
            private String acce_model;
            private String acce_remark = "";
            private String acce_thumb;
            private String acce_num;
            private boolean isExtra = false;

            public AcceListEntity() {
            }

            protected AcceListEntity(Parcel in) {
                this.detail_id = in.readString();
                this.is_normal = in.readString();
                this.acce_id = in.readString();
                this.acce_name = in.readString();
                this.acce_type = in.readString();
                this.acce_brand = in.readString();
                this.acce_model = in.readString();
                this.acce_remark = in.readString();
                this.acce_thumb = in.readString();
                this.acce_num = in.readString();
                this.isExtra = in.readByte() != 0;
            }

            @Bindable
            public String getDetail_id() {
                return detail_id;
            }

            public void setDetail_id(String detail_id) {
                this.detail_id = detail_id;
            }

            @Bindable
            public String getIs_normal() {
                return is_normal;
            }

            public void setIs_normal(String is_normal) {
                this.is_normal = is_normal;
                this.isExtra = is_normal.equals("1");
            }

            @Bindable
            public String getAcce_id() {
                return acce_id;
            }

            public void setAcce_id(String acce_id) {
                this.acce_id = acce_id;
            }

            @Bindable
            public String getAcce_name() {
                return acce_name;
            }

            public void setAcce_name(String acce_name) {
                this.acce_name = acce_name;
            }

            @Bindable
            public String getAcce_type() {
                return acce_type;
            }

            public void setAcce_type(String acce_type) {
                this.acce_type = acce_type;
            }

            @Bindable
            public String getAcce_brand() {
                return acce_brand;
            }

            public void setAcce_brand(String acce_brand) {
                this.acce_brand = acce_brand;
            }

            @Bindable
            public String getAcce_model() {
                return "规格:" + acce_model;
            }

            public void setAcce_model(String acce_model) {
                this.acce_model = acce_model;
            }

            @Bindable
            public String getAcce_remark() {
                return acce_remark;
            }

            public void setAcce_remark(String acce_remark) {
                this.acce_remark = acce_remark;
            }

            @Bindable
            public String getAcce_thumb() {
                return acce_thumb;
            }

            public void setAcce_thumb(String acce_thumb) {
                this.acce_thumb = acce_thumb;
            }

            @Bindable
            public String getAcce_num() {
                return acce_num;
            }

            public void setAcce_num(String acce_num) {
                this.acce_num = acce_num;
            }

            @Bindable
            public boolean isExtra() {
                return is_normal.equals("1");
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.detail_id);
                dest.writeString(this.is_normal);
                dest.writeString(this.acce_id);
                dest.writeString(this.acce_name);
                dest.writeString(this.acce_type);
                dest.writeString(this.acce_brand);
                dest.writeString(this.acce_model);
                dest.writeString(this.acce_remark);
                dest.writeString(this.acce_thumb);
                dest.writeString(this.acce_num);
                dest.writeByte(isExtra ? (byte) 1 : (byte) 0);
            }
        }

        public static class AccePhotosEntity extends BaseObservable {
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.orderid);
            dest.writeString(this.detailid);
            dest.writeString(this.serviceid);
            dest.writeString(this.acceid);
            dest.writeString(this.sn);
            dest.writeString(this.addtime);
            dest.writeString(this.fact_shipping_type);
            dest.writeString(this.fact_shipping_num);
            dest.writeString(this.fact_shipping_time);
            dest.writeString(this.applicant);
            dest.writeString(this.applicant_tell);
            dest.writeString(this.address);
            dest.writeString(this.factory);
            dest.writeString(this.factory_logo);
            dest.writeString(this.exe_type);
            dest.writeString(this.exe_status);
            dest.writeString(this.exe_desc);
            dest.writeString(this.acce_count);
            dest.writeString(this.shipping_type);
            dest.writeString(this.shipping_num);
            dest.writeString(this.shipping_paytype);
            dest.writeString(this.shipping_money);
            dest.writeString(this.remarks);
            dest.writeString(this.fact_reply_desc);
            dest.writeInt(this.totalCount);
            dest.writeTypedList(acce_list);
            dest.writeList(this.acce_photos);
        }

        public DataEntity() {
        }

        protected DataEntity(Parcel in) {
            this.orderid = in.readString();
            this.detailid = in.readString();
            this.serviceid = in.readString();
            this.acceid = in.readString();
            this.sn = in.readString();
            this.addtime = in.readString();
            this.fact_shipping_type = in.readString();
            this.fact_shipping_num = in.readString();
            this.fact_shipping_time = in.readString();
            this.applicant = in.readString();
            this.applicant_tell = in.readString();
            this.address = in.readString();
            this.factory = in.readString();
            this.factory_logo = in.readString();
            this.exe_type = in.readString();
            this.exe_status = in.readString();
            this.exe_desc = in.readString();
            this.acce_count = in.readString();
            this.shipping_type = in.readString();
            this.shipping_num = in.readString();
            this.shipping_paytype = in.readString();
            this.shipping_money = in.readString();
            this.remarks = in.readString();
            this.fact_reply_desc = in.readString();
            this.totalCount = in.readInt();
            this.acce_list = in.createTypedArrayList(AcceListEntity.CREATOR);
            this.acce_photos = new ArrayList<AccePhotosEntity>();
            in.readList(this.acce_photos, AccePhotosEntity.class.getClassLoader());
        }

        public static final Parcelable.Creator<DataEntity> CREATOR = new Parcelable.Creator<DataEntity>() {
            @Override
            public DataEntity createFromParcel(Parcel source) {
                return new DataEntity(source);
            }

            @Override
            public DataEntity[] newArray(int size) {
                return new DataEntity[size];
            }
        };
    }
}
