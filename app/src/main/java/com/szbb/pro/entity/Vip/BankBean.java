package com.szbb.pro.entity.vip;

import com.szbb.pro.entity.base.BaseBean;

import java.util.List;

/**
 * Created by ChanZeeBm on 2016/2/26.
 */
public class BankBean extends BaseBean {

    /**
     * bank_id : 659004514
     * bank_name : 中国银行
     */

    private List<BankListEntity> bank_list;


    private List<CityListEntity> city_list;

    public void setBank_list(List<BankListEntity> bank_list) {
        this.bank_list = bank_list;
    }

    public void setCity_list(List<CityListEntity> city_list) {
        this.city_list = city_list;
    }

    public List<BankListEntity> getBank_list() {
        return bank_list;
    }

    public List<CityListEntity> getCity_list() {
        return city_list;
    }

    public static class BankListEntity {
        private String bank_id;
        private String bank_name;

        public void setBank_id(String bank_id) {
            this.bank_id = bank_id;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getBank_id() {
            return bank_id;
        }

        public String getBank_name() {
            return bank_name;
        }
    }

    public static class CityListEntity {
        private String city_name;
        /**
         * city_id : 110100
         * city_name : 北京
         */

        private List<ChildsEntity> childs;

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public void setChilds(List<ChildsEntity> childs) {
            this.childs = childs;
        }

        public String getCity_name() {
            return city_name;
        }

        public List<ChildsEntity> getChilds() {
            return childs;
        }

        public static class ChildsEntity {
            private String city_id;
            private String city_name;

            public void setCity_id(String city_id) {
                this.city_id = city_id;
            }

            public void setCity_name(String city_name) {
                this.city_name = city_name;
            }

            public String getCity_id() {
                return city_id;
            }

            public String getCity_name() {
                return city_name;
            }
        }
    }
}
