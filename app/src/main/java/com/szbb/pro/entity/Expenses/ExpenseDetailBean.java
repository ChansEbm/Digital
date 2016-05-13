package com.szbb.pro.entity.expenses;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.szbb.pro.BR;
import com.szbb.pro.entity.base.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChanZeeBm on 2015/11/14.
 */
public class ExpenseDetailBean extends BaseBean {

    /**
     * list : [{"cate_name":"维修费用","cate_list":[{"cost_name":"单门电冰箱","cost_money":"16.00"}]},
     * {"cate_name":"上门费用","cate_list":[{"cost_name":"上门费用","cost_money":"0.00"}]},
     * {"cate_name":"配件邮寄费用","cate_list":[{"cost_name":"","cost_money":"8.00"}]},
     * {"cate_name":"内部补贴","cate_list":[{"cost_name":"上门太远","cost_money":"27.00"}]}]
     * total_money : 91.00
     */

    private String total_money;
    /**
     * cate_name : 维修费用
     * cate_list : [{"cost_name":"单门电冰箱","cost_money":"16.00"}]
     */

    private List<ListEntity> list;

    private List<RemarkListEntity> remark_list;

    @Bindable
    public String getTotal_money() {
        return "￥" + total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public List<RemarkListEntity> getRemark_list() {
        return remark_list;
    }

    public void setRemark_list(List<RemarkListEntity> remark_list) {
        this.remark_list = remark_list;
    }

    public static class ListEntity extends BaseObservable implements Parcelable {

        public static final Parcelable.Creator<ListEntity> CREATOR = new Parcelable
                .Creator<ListEntity>() {
            public ListEntity createFromParcel(Parcel source) {
                return new ListEntity(source);
            }

            public ListEntity[] newArray(int size) {
                return new ListEntity[size];
            }
        };
        private String cate_name;
        /**
         * cost_name : 单门电冰箱
         * cost_money : 16.00
         */

        private List<CateListEntity> cate_list;

        public ListEntity() {
        }

        protected ListEntity(Parcel in) {
            this.cate_name = in.readString();
            this.cate_list = new ArrayList<CateListEntity>();
            in.readList(this.cate_list, List.class.getClassLoader());
        }

        @Bindable
        public String getCate_name() {
            return cate_name;
        }

        public void setCate_name(String cate_name) {
            this.cate_name = cate_name;
            notifyPropertyChanged(BR.cate_name);
        }

        public List<CateListEntity> getCate_list() {
            return cate_list;
        }

        public void setCate_list(List<CateListEntity> cate_list) {
            this.cate_list = cate_list;

        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.cate_name);
            dest.writeList(this.cate_list);
        }

        public static class CateListEntity extends BaseObservable implements Parcelable {

            public static final Creator<CateListEntity> CREATOR = new Creator<CateListEntity>() {
                public CateListEntity createFromParcel(Parcel source) {
                    return new CateListEntity(source);
                }

                public CateListEntity[] newArray(int size) {
                    return new CateListEntity[size];
                }
            };
            private String cost_name;
            private String cost_money;

            public CateListEntity() {
            }

            protected CateListEntity(Parcel in) {
                this.cost_name = in.readString();
                this.cost_money = in.readString();
            }

            @Bindable
            public String getCost_name() {
                return cost_name;
            }

            public void setCost_name(String cost_name) {
                this.cost_name = cost_name;
                notifyPropertyChanged(BR.cate_name);
            }

            @Bindable
            public String getCost_money() {
                return "¥" + cost_money;
            }

            public void setCost_money(String cost_money) {
                this.cost_money = cost_money;
                notifyPropertyChanged(BR.cost_money);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.cost_name);
                dest.writeString(this.cost_money);
            }
        }
    }

    public static class RemarkListEntity extends BaseObservable {
        private String remark;

        @Bindable
        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
