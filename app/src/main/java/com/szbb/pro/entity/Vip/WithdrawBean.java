package com.szbb.pro.entity.Vip;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.widget.TextView;

import com.szbb.pro.R;
import com.szbb.pro.entity.Base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2/28/2016.
 */
public class WithdrawBean extends BaseBean {

    /**
     * outid : 5
     * money : 20.00
     * status : 0
     * status_desc : 提现中
     * addtime : 2016-02-19 10:44
     */

    private List<ListEntity> list;

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity extends BaseObservable {
        private String outid;
        private String money;
        private String status;
        private String status_desc;
        private String addtime;

        public void setOutid(String outid) {
            this.outid = outid;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setStatus_desc(String status_desc) {
            this.status_desc = status_desc;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getOutid() {
            return outid;
        }

        public String getMoney() {
            return money;
        }

        public String getStatus() {
            return status;
        }

        public String getStatus_desc() {
            return status_desc;
        }

        public String getAddtime() {
            return addtime;
        }

        @BindingAdapter({"app:bindColor"})
        public static void setTextColor(TextView textView, String type) {
            int color;
            switch (type) {
                case "0":
                    color = textView.getContext().getResources().getColor(R.color.color_text_org);
                    break;
                case "1":
                    color = textView.getContext().getResources().getColor(android.R.color.holo_green_dark);
                    break;
                case "2":
                    color = textView.getContext().getResources().getColor(R.color.color_text_red);
                    break;
                default:
                    color = textView.getContext().getResources().getColor(R.color.color_text_dark);
                    break;
            }
            textView.setTextColor(color);

        }
    }
}
