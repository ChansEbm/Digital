package com.szbb.pro.entity.order;

import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.support.annotation.IntDef;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;

import com.szbb.pro.BR;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.widget.CircleView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Created by ChanZeeBm on 2015/12/28.
 */
public class MyOrderBean extends BaseBean {

    public final static int NEWORDER = 1;
    public final static int SERVICED = 2;
    public final static int COUNTINT = 3;

    @IntDef(value = {NEWORDER, SERVICED, COUNTINT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface type {

    }

    private CondEntity cond;

    private List<ListEntity> list;

    public CondEntity getCond() {
        return cond;
    }

    public void setCond(CondEntity cond) {
        this.cond = cond;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public static class CondEntity {
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public static class ListEntity extends BaseBean {
        private String orderid;
        private String sn;
        private String drop_time = "";//新工单才有数据
        private Spanned new_order_drop_time_desc;//自己加的字段 用于格式化字体颜色(新工单)
        private String appoint_time;
        private Spanned appoint_time_desc;//自己加的字段 用于格式化字体颜色(待服务)
        private String is_sign_in;
        private String sign_in_time;
        private String order_status_desc;
        private String has_handle_desc;
        private String complete_time;
        private Spanned sign_in_desc;//自己加的字段 用于描述签到状态
        private String order_handle_desc;
        private Spanned order_handle_desc_in_color;
        private String order_type;
        private String service_type;
        private String nickname;
        private String tel;
        private String address;
        private String lat;
        private String lng;
        private String service_pro;
        private String service_pro_brand;
        private String service_pro_stantard;
        private String service_pro_model;
        private String is_more_pro;
        private String unread = "0";
        private String identifier;

        @BindingAdapter({"app:unreadTag"})
        public static void unreadTag(CircleView circleView, String unread) {
            assert circleView != null;
            if (TextUtils.isEmpty(unread) || TextUtils.equals("0", unread)) {
                circleView.setVisibility(View.INVISIBLE);
            } else {
                circleView.setVisibility(View.VISIBLE);
                circleView.setCircleText(unread);
            }
        }

        @Bindable
        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        @Bindable
        public String getSn() {
            return "工单:" + sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        @Bindable
        public String getAppoint_time() {
            return appoint_time;
        }

        public void setAppoint_time(String appoint_time) {
            this.appoint_time = appoint_time;
        }

        @Bindable
        public String getIs_sign_in() {
            return is_sign_in;
        }

        public void setIs_sign_in(String is_sign_in) {
            this.is_sign_in = is_sign_in;
        }

        @Bindable
        public String getSign_in_time() {
            return sign_in_time;
        }

        public void setSign_in_time(String sign_in_time) {
            this.sign_in_time = sign_in_time;
        }

        @Bindable
        public String getOrder_status_desc() {

            return order_status_desc;
        }

        public void setOrder_status_desc(String order_status_desc) {
            this.order_status_desc = order_status_desc;
        }

        @Bindable
        public String getOrder_handle_desc() {
            return order_handle_desc;
        }

        public void setOrder_handle_desc(String order_handle_desc) {
            this.order_handle_desc = order_handle_desc;
        }

        @Bindable
        public String getOrder_type() {
            return order_type;
        }

        public void setOrder_type(String order_type) {
            this.order_type = order_type;
        }

        @Bindable
        public String getService_type() {
            return service_type;
        }

        public void setService_type(String service_type) {
            this.service_type = service_type;
        }

        @Bindable
        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        @Bindable
        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        @Bindable
        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @Bindable
        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        @Bindable
        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        @Bindable
        public String getService_pro() {
            return service_pro;
        }

        public void setService_pro(String service_pro) {
            this.service_pro = service_pro;
        }

        @Bindable
        public String getService_pro_brand() {
            return service_pro_brand;
        }

        public void setService_pro_brand(String service_pro_brand) {
            this.service_pro_brand = service_pro_brand;
        }

        @Bindable
        public String getService_pro_stantard() {
            return service_pro_stantard;
        }

        public void setService_pro_stantard(String service_pro_stantard) {
            this.service_pro_stantard = service_pro_stantard;
        }

        @Bindable
        public String getService_pro_model() {
            return service_pro_model;
        }

        public void setService_pro_model(String service_pro_model) {
            this.service_pro_model = service_pro_model;
        }

        @Bindable
        public String getIs_more_pro() {
            return is_more_pro;
        }

        public void setIs_more_pro(String is_more_pro) {
            this.is_more_pro = is_more_pro;
        }

        @Bindable
        public String getDrop_time() {
            return drop_time;
        }

        public void setDrop_time(String drop_time) {
            this.drop_time = drop_time;
        }

        @Bindable
        public Spanned getNew_order_drop_time_desc() {
            long ct = System.currentTimeMillis();
            long dt = Long.parseLong(getDrop_time() + "000");
            String result = AppTools.convertMillions(dt - ct);
            new_order_drop_time_desc = Html.fromHtml("<font color=\"#a0a0a0\">此工单将于</font><font " +
                    "color=\"#ff9000\">" + result + "</font><font color=\"#a0a0a0\">后过时</font>");
            return new_order_drop_time_desc;
        }

        @Bindable
        public String getHas_handle_desc() {
            return has_handle_desc;
        }

        public void setHas_handle_desc(String has_handle_desc) {
            this.has_handle_desc = has_handle_desc;
        }

        @Bindable
        public Spanned getAppoint_time_desc() {
            return appoint_time_desc;
        }

        public void setAppoint_time_desc(Spanned appoint_time_desc) {
            this.appoint_time_desc = appoint_time_desc;
        }

        @Bindable
        public Spanned getSign_in_desc() {
            long ct = System.currentTimeMillis();
            long at = Long.parseLong(getAppoint_time() + "000");
            String result = AppTools.convertMillions(at - ct);
            String isSignIn = getIs_sign_in();
            if (TextUtils.equals(isSignIn, "0")) {
                if (TextUtils.equals("-1", result)) {
                    this.sign_in_desc = Html.fromHtml("<font color=\"#ff9000\">签到已超时</font>");
                } else {
                    this.sign_in_desc = Html.fromHtml("<font " +
                            "color=\"#a0a0a0\">距上门服务还有</font><font " +
                            "color=\"#ff9000" +
                            "\">" + result + "</font>");
                }
            } else if (TextUtils.equals(isSignIn, "1")) {
                this.sign_in_desc = Html.fromHtml("<font color=\"#a0a0a0\">已于" + sign_in_time +
                        "</font><font " +
                        "color=\"#ff9000\">签到成功</font>");
            } else if (TextUtils.equals(isSignIn, "2")) {
                this.sign_in_desc = Html.fromHtml("<font color=\"#ff9000\">签到失败</font>");
            } else if (TextUtils.equals(isSignIn, "3")) {
                this.sign_in_desc = Html.fromHtml("<font color=\"#ff9000\">签到已超时</font>");
            }
            return sign_in_desc;
        }

        @Bindable
        public String getComplete_time() {
            return complete_time;
        }

        public void setComplete_time(String complete_time) {
            this.complete_time = complete_time;
        }

        @Bindable
        public Spanned getOrder_handle_desc_in_color() {
            order_handle_desc_in_color = Html.fromHtml("<font color='#ff9000'>" +
                    getOrder_handle_desc()
                    + "</font>");
            return order_handle_desc_in_color;
        }

        public String getSearchField() {
            return sn + service_pro_brand + service_pro_stantard + service_pro_model + nickname + tel + address;
        }

        @Bindable
        public String getUnread() {
            return unread;
        }

        public void setUnread(String unread) {
            this.unread = unread;
            notifyPropertyChanged(BR.unread);
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }
    }
}
