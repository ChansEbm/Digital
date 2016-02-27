package com.szbb.pro.entity.Appointment;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.szbb.pro.entity.Base.BaseBean;

import java.util.List;

/**
 * Created by ChanZeeBm on 2015/11/16.
 */
public class AppointmentHistoryItemBean extends BaseBean {

    private List<ListEntity> list;

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity extends BaseObservable {
        private String nickname;
        private String tel;
        private String appoint_time;
        private String result;
        private String remarks;

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public void setAppoint_time(String appoint_time) {
            this.appoint_time = appoint_time;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        @Bindable
        public String getNickname() {
            return nickname;
        }

        @Bindable
        public String getTel() {
            return tel;
        }

        @Bindable
        public String getAppoint_time() {
            return appoint_time;
        }

        @Bindable
        public String getResult() {
            return result;
        }

        @Bindable
        public String getRemarks() {
            return remarks;
        }
    }
}
