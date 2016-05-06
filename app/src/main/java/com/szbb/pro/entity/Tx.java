package com.szbb.pro.entity;

import android.databinding.BaseObservable;

import java.util.List;

/**
 * Created by ChanZeeBm on 2016/1/18.
 */
public class Tx extends BaseObservable {

    /**
     * errorcode : 0
     * msg : 操作成功
     * list : [{"cate_name":"维修费用","cate_list":[{"cost_name":"液晶电视1","cost_money":"30.00"}]},
     * {"cate_name":"上门费用","cate_list":[{"cost_name":"上门费用","cost_money":"0.00"}]},
     * {"cate_name":"配件邮寄费用","cate_list":[{"cost_name":"申通","cost_money":"8.00"}]},
     * {"cate_name":"费用申请","cate_list":[{"cost_name":"太远了","cost_money":"5.00"}]}]
     * total_money : 43.00
     * remark_list : [{"remark":"备注:+++1"},{"remark":"备注:+++2"},{"remark":"备注:+++3"}]
     */

    private int errorcode;
    private String msg;
    private String total_money;
    /**
     * cate_name : 维修费用
     * cate_list : [{"cost_name":"液晶电视1","cost_money":"30.00"}]
     */

    private List<ListEntity> list;
    /**
     * remark : 备注:+++1
     */

    private List<RemarkListEntity> remark_list;

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public void setRemark_list(List<RemarkListEntity> remark_list) {
        this.remark_list = remark_list;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public String getMsg() {
        return msg;
    }

    public String getTotal_money() {
        return total_money;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public List<RemarkListEntity> getRemark_list() {
        return remark_list;
    }

    public static class ListEntity {
        private String cate_name;
        /**
         * cost_name : 液晶电视1
         * cost_money : 30.00
         */

        private List<CateListEntity> cate_list;

        public void setCate_name(String cate_name) {
            this.cate_name = cate_name;
        }

        public void setCate_list(List<CateListEntity> cate_list) {
            this.cate_list = cate_list;
        }

        public String getCate_name() {
            return cate_name;
        }

        public List<CateListEntity> getCate_list() {
            return cate_list;
        }

        public static class CateListEntity {
            private String cost_name;
            private String cost_money;

            public void setCost_name(String cost_name) {
                this.cost_name = cost_name;
            }

            public void setCost_money(String cost_money) {
                this.cost_money = cost_money;
            }

            public String getCost_name() {
                return cost_name;
            }

            public String getCost_money() {
                return cost_money;
            }
        }
    }

    public static class RemarkListEntity {
        private String remark;

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getRemark() {
            return remark;
        }
    }
}
