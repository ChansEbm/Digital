package com.szbb.pro.entity.Fittings;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.szbb.pro.entity.Base.BaseBean;

/**
 * Created by ChanZeeBm on 2016/2/2.
 * 获取技工或者客户默认地址
 */
public class CustomerAddressBean extends BaseBean {

    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity extends BaseObservable implements Parcelable {
        private String nickname = "";
        private String area_ids = "";
        private String area_ids_desc = "";
        private String address = "";
        private String telephone = "";


        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setArea_ids(String area_ids) {
            this.area_ids = area_ids;
        }

        public void setArea_ids_desc(String area_ids_desc) {
            this.area_ids_desc = area_ids_desc;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        @Bindable
        public String getNickname() {
            return nickname;
        }

        public String getArea_ids() {
            return area_ids;
        }

        @Bindable
        public String getArea_ids_desc() {
            return area_ids_desc;
        }

        @Bindable
        public String getAddress() {
            return address;
        }

        @Bindable
        public String getTelephone() {
            return telephone;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.nickname);
            dest.writeString(this.area_ids);
            dest.writeString(this.area_ids_desc);
            dest.writeString(this.address);
            dest.writeString(this.telephone);
        }

        public DataEntity() {
        }

        protected DataEntity(Parcel in) {
            this.nickname = in.readString();
            this.area_ids = in.readString();
            this.area_ids_desc = in.readString();
            this.address = in.readString();
            this.telephone = in.readString();
        }

        public static final Parcelable.Creator<DataEntity> CREATOR = new Parcelable.Creator<DataEntity>() {
            public DataEntity createFromParcel(Parcel source) {
                return new DataEntity(source);
            }

            public DataEntity[] newArray(int size) {
                return new DataEntity[size];
            }
        };
    }
}
