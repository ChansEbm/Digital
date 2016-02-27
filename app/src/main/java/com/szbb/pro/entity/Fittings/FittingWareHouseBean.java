package com.szbb.pro.entity.Fittings;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.szbb.pro.entity.Base.BaseBean;

import java.util.List;

/**
 * Created by ChanZeeBm on 2016/1/26.
 */
public class FittingWareHouseBean extends BaseBean {

    /**
     * parts_id : 0
     * keyword :
     */

    private CondEntity cond;
    /**
     * parts_id : 14
     * parts_name : 压缩机
     */

    private List<PartsListEntity> parts_list;
    /**
     * acce_id : 107
     * acce_name : SDF
     * acce_model : hfdug3444
     * acce_thumb : /Public/uploads/pics/2015-11/563c1dec08fcf.jpg
     */

    private List<AcceListEntity> acce_list;

    public void setCond(CondEntity cond) {
        this.cond = cond;
    }

    public void setParts_list(List<PartsListEntity> parts_list) {
        this.parts_list = parts_list;
    }

    public void setAcce_list(List<AcceListEntity> acce_list) {
        this.acce_list = acce_list;
    }

    public CondEntity getCond() {
        return cond;
    }

    public List<PartsListEntity> getParts_list() {
        return parts_list;
    }

    public List<AcceListEntity> getAcce_list() {
        return acce_list;
    }

    public static class CondEntity {
        private int parts_id;
        private String keyword;

        public void setParts_id(int parts_id) {
            this.parts_id = parts_id;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public int getParts_id() {
            return parts_id;
        }

        public String getKeyword() {
            return keyword;
        }
    }

    public static class PartsListEntity {
        private String parts_id;
        private String parts_name;

        public void setParts_id(String parts_id) {
            this.parts_id = parts_id;
        }

        public void setParts_name(String parts_name) {
            this.parts_name = parts_name;
        }

        public String getParts_id() {
            return parts_id;
        }

        public String getParts_name() {
            return parts_name;
        }
    }

    public static class AcceListEntity extends BaseObservable implements Parcelable {
        private String acce_id;
        private String acce_name;
        private String acce_model;
        private String acce_thumb;
        private int count = 0;
        private boolean isExtra = false;

        public AcceListEntity() {
        }

        public void setAcce_id(String acce_id) {
            this.acce_id = acce_id;
        }

        public void setAcce_name(String acce_name) {
            this.acce_name = acce_name;
        }

        public void setAcce_model(String acce_model) {
            this.acce_model = acce_model;
        }

        public void setAcce_thumb(String acce_thumb) {
            this.acce_thumb = acce_thumb;
        }

        public String getAcce_id() {
            return acce_id;
        }

        @Bindable
        public String getAcce_name() {
            return acce_name;
        }

        @Bindable
        public String getAcce_model() {
            return "规格 :" + acce_model;
        }

        public String getAcce_thumb() {
            return acce_thumb;
        }

        @Bindable
        public boolean isExtra() {
            return isExtra;
        }

        public void setIsExtra(boolean isExtra) {
            this.isExtra = isExtra;
        }

        @Bindable
        public int getCount() {
            return count <= 0 ? 0 : count;
        }

        public void setCount(int count) {
            this.count = count;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.acce_id);
            dest.writeString(this.acce_name);
            dest.writeString(this.acce_model);
            dest.writeString(this.acce_thumb);
            dest.writeInt(this.count);
            dest.writeByte(isExtra ? (byte) 1 : (byte) 0);
        }

        protected AcceListEntity(Parcel in) {
            this.acce_id = in.readString();
            this.acce_name = in.readString();
            this.acce_model = in.readString();
            this.acce_thumb = in.readString();
            this.count = in.readInt();
            this.isExtra = in.readByte() != 0;
        }

        public static final Parcelable.Creator<AcceListEntity> CREATOR = new Parcelable.Creator<AcceListEntity>() {
            public AcceListEntity createFromParcel(Parcel source) {
                return new AcceListEntity(source);
            }

            public AcceListEntity[] newArray(int size) {
                return new AcceListEntity[size];
            }
        };
    }
}
