package com.szbb.pro.entity.Vip;

import android.databinding.Bindable;
import android.text.TextUtils;

import com.szbb.pro.entity.Base.BaseBean;

/**
 * Created by ChanZeeBm on 2016/2/25.
 */
public class CreditCardBean extends BaseBean {

    /**
     * is_bind_card : 0
     * credit_card :
     * bank_name :
     * bank_city :
     */

    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity extends BaseBean {
        private String is_bind_card;
        private String has_pay_password;
        private String credit_card;
        private String bank_name;
        private String bank_city;
        private String cardtype;


        public String getIs_bind_card() {
            return is_bind_card;
        }

        public void setIs_bind_card(String is_bind_card) {
            this.is_bind_card = is_bind_card;
        }

        @Bindable
        public String getCredit_card() {
            return credit_card;
        }

        public void setCredit_card(String credit_card) {
            this.credit_card = credit_card;
        }

        @Bindable
        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        @Bindable
        public String getBank_city() {
            return bank_city;
        }

        public void setBank_city(String bank_city) {
            this.bank_city = bank_city;
        }

        public String getHas_pay_password() {
            return has_pay_password;
        }

        public void setHas_pay_password(String has_pay_password) {
            this.has_pay_password = has_pay_password;
        }

        @Bindable
        public String getCardtype() {
            return cardtype;
        }

        public void setCardtype(String cardtype) {
            this.cardtype = cardtype;
        }

        public boolean isBindCard() {
            return TextUtils.equals(getIs_bind_card(), "1");
        }

        public boolean hasPayPassword() {
            return TextUtils.equals(getHas_pay_password(), "1");
        }
    }
}
