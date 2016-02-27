package com.szbb.pro.entity.Fittings;

import com.szbb.pro.entity.Base.BaseBean;

/**
 * Created by ChanZeeBm on 2016/2/3.
 */
public class OtherFittingBean extends BaseBean {


    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        private int other_acce_id;
        private String other_acce_name;
        private String other_acce_remarks;
        private String other_acce_nums;
        private String other_acce_thumb;

        public void setOther_acce_id(int other_acce_id) {
            this.other_acce_id = other_acce_id;
        }

        public void setOther_acce_name(String other_acce_name) {
            this.other_acce_name = other_acce_name;
        }

        public void setOther_acce_remarks(String other_acce_remarks) {
            this.other_acce_remarks = other_acce_remarks;
        }

        public void setOther_acce_nums(String other_acce_nums) {
            this.other_acce_nums = other_acce_nums;
        }

        public void setOther_acce_thumb(String other_acce_thumb) {
            this.other_acce_thumb = other_acce_thumb;
        }

        public int getOther_acce_id() {
            return other_acce_id;
        }

        public String getOther_acce_name() {
            return other_acce_name;
        }

        public String getOther_acce_remarks() {
            return other_acce_remarks;
        }

        public String getOther_acce_nums() {
            return other_acce_nums;
        }

        public String getOther_acce_thumb() {
            return other_acce_thumb;
        }
    }
}
