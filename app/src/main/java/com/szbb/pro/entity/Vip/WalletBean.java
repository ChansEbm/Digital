package com.szbb.pro.entity.Vip;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;

import com.szbb.pro.entity.Base.BaseBean;

/**
 * Created by ChanZeeBm on 2016/2/25.
 */
public class WalletBean extends BaseBean {


    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity extends BaseObservable {
        private String money;
        private String quality_money;
        private String income_money;
        private String unsettled_money;
        private String withdrawing_money;
        private String withdrawials_money;
        private String is_bind_card;
        private String has_pay_password;

        public boolean isBindCard() {
            return TextUtils.equals("1", getIs_bind_card());
        }

        public boolean isHasPayPassword() {
            return TextUtils.equals("1", getHas_pay_password());
        }

        public String getIs_bind_card() {
            return is_bind_card;
        }

        public void setIs_bind_card(String is_bind_card) {
            this.is_bind_card = is_bind_card;
        }

        public String getHas_pay_password() {
            return has_pay_password;
        }

        public void setHas_pay_password(String has_pay_password) {
            this.has_pay_password = has_pay_password;
        }

        @Bindable
        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        @Bindable
        public String getQuality_money() {
            return quality_money;
        }

        public void setQuality_money(String quality_money) {
            this.quality_money = quality_money;
        }

        @Bindable
        public String getIncome_money() {
            return income_money;
        }

        public void setIncome_money(String income_money) {
            this.income_money = income_money;
        }

        @Bindable
        public String getUnsettled_money() {
            return unsettled_money;
        }

        public void setUnsettled_money(String unsettled_money) {
            this.unsettled_money = unsettled_money;
        }

        @Bindable
        public String getWithdrawing_money() {
            return withdrawing_money;
        }

        public void setWithdrawing_money(String withdrawing_money) {
            this.withdrawing_money = withdrawing_money;
        }

        @Bindable
        public String getWithdrawials_money() {
            return withdrawials_money;
        }

        public void setWithdrawials_money(String withdrawials_money) {
            this.withdrawials_money = withdrawials_money;
        }
    }
}
