package com.szbb.pro.entity.order;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.szbb.pro.BR;
import com.szbb.pro.R;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.eum.ButtonType;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.ui.activity.expenses.ExpensesResultActivity;
import com.szbb.pro.ui.activity.orders.operating.a_mode.FittingApplyDetailActivity;
import com.szbb.pro.ui.activity.orders.operating.b_mode.FittingResendDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChanZeeBm on 2016/1/6.
 */
public class OrderDetailBean extends BaseBean {

    private DataEntity data;

    private List<ListEntity> list;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public static class DataEntity extends BaseObservable {

        private String orderid;
        private String customer_service_phone;
        private String sn;
        private String order_status_desc;
        private String appoint_time = "";
        private String is_all_repair = "";//后台字段
        private String is_submit_complete = "";
        private String diffTime;//预约时间跟现在时间的时间差
        private String formatAppointTime = "";
        private String is_sign_in;
        private String sign_in_time;
        private Spanned sign_in_desc;
        private String distribute_time;
        private String nickname;
        private String tel;
        private String est_miles;
        private String address;
        private String lat;
        private String lng;
        private String service_type;
        private String service_pro;
        private String type_and_pro;//服务类型跟产品名字的拼接,用于类似 “冰箱(上门维修)”
        private String factory_logo;
        private String factory_desc;
        private String factory_technology_tel;
        private String acce_exe_type;
        private List<AcceCostListEntity> acce_cost_list;
        private String subsidy;
        private Spanned subsidy_desc;
        private String acce_cost_title;

        private String service_evaluation;
        private String order_settlement;

        public DataEntity() {

        }

        @BindingAdapter(value = {"app:signInText"})
        public static void setSignInText(Button button, String signIn) {
            if (button == null || TextUtils.isEmpty(signIn))
                return;
            switch (signIn) {
                case "0":
                    button.setText("签到");
                    break;
                case "1":
                    button.setText("签到成功");
                    button.setEnabled(false);
                    break;
                case "2":
                case "3":
                    button.setText("签到失败");
                    button.setEnabled(false);
                    break;
            }
        }

        @BindingAdapter(value = {"app:signInBackground"})
        public static void setSignInBackground(Button button, String sign) {
            if (button == null || TextUtils.isEmpty(sign))
                return;
            if (TextUtils.equals("0", sign))
                button.setBackgroundResource(R.drawable.bg_cyan_frame);
            else {
                button.setBackground(null);
                button.setEnabled(false);
            }

        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        @Bindable
        public String getAppoint_time() {
            return appoint_time;
        }

        public void setAppoint_time(String appoint_time) {
            this.appoint_time = AppTools.formatTime(appoint_time, true);
        }

        public String getIs_sign_in() {
            return is_sign_in;
        }

        public void setIs_sign_in(String is_sign_in) {
            this.is_sign_in = is_sign_in;
        }

        @Bindable
        public String getDistribute_time() {
            return distribute_time;
        }

        public void setDistribute_time(String distribute_time) {
            this.distribute_time = distribute_time;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        @Bindable
        public String getEst_miles() {
            return est_miles + "公里";
        }

        public void setEst_miles(String est_miles) {
            this.est_miles = est_miles;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getService_type() {
            return service_type;
        }

        public void setService_type(String service_type) {
            this.service_type = service_type;
        }

        public String getFactory_logo() {
            return factory_logo;
        }

        public void setFactory_logo(String factory_logo) {
            this.factory_logo = factory_logo;
        }

        public String getFactory_desc() {
            return factory_desc;
        }

        public void setFactory_desc(String factory_desc) {
            this.factory_desc = factory_desc;
        }

        public String getFactory_technology_tel() {
            return factory_technology_tel;
        }

        public void setFactory_technology_tel(String factory_technology_tel) {
            this.factory_technology_tel = factory_technology_tel;
        }

        public String getAcce_exe_type() {
            return acce_exe_type;
        }

        public void setAcce_exe_type(String acce_exe_type) {
            this.acce_exe_type = acce_exe_type;
        }

        @Bindable
        public String getDiffTime() {
            return AppTools.formatPHPDiffTime(appoint_time);
        }

        @Bindable
        public String getOrder_status_desc() {
            return order_status_desc;
        }

        public void setOrder_status_desc(String order_status_desc) {
            this.order_status_desc = order_status_desc;
        }

        public String getService_pro() {
            return service_pro;
        }

        public void setService_pro(String service_pro) {
            this.service_pro = service_pro;
        }

        @Bindable
        public String getType_and_pro() {
            this.type_and_pro = service_pro + "(" + service_type + ")";
            return type_and_pro;
        }

        public void setType_and_pro(String type_and_pro) {
            this.type_and_pro = type_and_pro;
        }

        public boolean isSignIn() {
            return TextUtils.equals("1", is_sign_in);
        }

        public String getIs_all_repair() {
            return is_all_repair;
        }

        public void setIs_all_repair(String is_all_repair) {
            this.is_all_repair = is_all_repair;
        }

        public boolean isAllRepair() {
            return TextUtils.equals(is_all_repair, "1");
        }

        @Bindable
        public String getFormatAppointTime() {
            appoint_time = TextUtils.isEmpty(appoint_time) ? "" : appoint_time;
            return AppTools.formatTime(appoint_time, true);
        }

        public void setFormatAppointTime(String formatAppointTime) {
            this.formatAppointTime = formatAppointTime;
        }

        @Bindable
        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public boolean getIs_submit_complete() {
            return is_submit_complete.equals("1");
        }

        public void setIs_submit_complete(String is_submit_complete) {
            this.is_submit_complete = is_submit_complete;
        }

        @Bindable
        public List<AcceCostListEntity> getAcce_cost_list() {
            return acce_cost_list;
        }

        public void setAcce_cost_list(List<AcceCostListEntity> acce_cost_list) {
            this.acce_cost_list = acce_cost_list;
        }

        @Bindable
        public Spanned getSign_in_desc() {
            if (TextUtils.isEmpty(getAppoint_time())) {
                return null;
            }
            long ct = System.currentTimeMillis();
            long at = Long.parseLong(getAppoint_time() + "000");
            String result = AppTools.convertMillions(at - ct);
            String isSignIn = getIs_sign_in();
            if (TextUtils.equals(isSignIn, "0")) {
                if (!TextUtils.equals("-1", result))
                    this.sign_in_desc = Html.fromHtml("<font " +
                            "color=\"#a0a0a0\">距上门服务还有</font><font " +
                            "color=\"#ff9000" +
                            "\">" + result + "</font>");
                else
                    this.sign_in_desc = Html.fromHtml("<font " +
                            "color=\"#a0a0a0\">距上门服务还有</font><font " +
                            "color=\"#ff9000" +
                            "\">" + "0个小时" + "</font>");
            } else if (TextUtils.equals(isSignIn, "1"))
                this.sign_in_desc = Html.fromHtml("<font color=\"#a0a0a0\">已于" + getSign_in_time() +
                        "</font><font " +
                        "color=\"#ff9000\">签到成功</font>");
            else if (TextUtils.equals(isSignIn, "2"))
                this.sign_in_desc = Html.fromHtml("<font color=\"#ff9000\">签到失败</font>");
            else if (TextUtils.equals(isSignIn, "3")) {
                this.sign_in_desc = Html.fromHtml("<font color=\"#ff9000\">签到已超时</font>");
            }
            return sign_in_desc;
        }

        public String getSign_in_time() {
            return sign_in_time;
        }

        public void setSign_in_time(String sign_in_time) {
            this.sign_in_time = sign_in_time;
        }

        @Bindable
        public String getSubsidy() {
            return subsidy;
        }

        public void setSubsidy(String subsidy) {
            this.subsidy = subsidy;
        }

        @Bindable
        public String getAcce_cost_title() {
            return acce_cost_title;
        }

        public void setAcce_cost_title(String acce_cost_title) {
            this.acce_cost_title = acce_cost_title;
        }

        @Bindable
        public String getService_evaluation() {
            return service_evaluation;
        }

        public void setService_evaluation(String service_evaluation) {
            this.service_evaluation = service_evaluation;
        }

        @Bindable
        public String getOrder_settlement() {
            return order_settlement;
        }

        public void setOrder_settlement(String order_settlement) {
            this.order_settlement = order_settlement;
        }

        @Bindable
        public Spanned getSubsidy_desc() {
            subsidy_desc = Html.fromHtml("完单可获补贴<font color=\"#f74c31\">" + getSubsidy() +
                    "</font>元");
            return subsidy_desc;
        }

        public String getCustomer_service_phone() {
            return customer_service_phone;
        }

        public void setCustomer_service_phone(String customer_service_phone) {
            this.customer_service_phone = customer_service_phone;
        }


        public static class AcceCostListEntity {
            private String handle_type;
            private String exe_type;
            private String exe_status;
            private String exe_desc;
            private String title;
            private String addtime;
            private String acid;

            public String getHandle_type() {
                return handle_type;
            }

            public void setHandle_type(String handle_type) {
                this.handle_type = handle_type;
            }

            public String getExe_type() {
                return exe_type;
            }

            public void setExe_type(String exe_type) {
                this.exe_type = exe_type;
            }

            public String getExe_status() {
                return exe_status;
            }

            public void setExe_status(String exe_status) {
                this.exe_status = exe_status;
            }

            public String getExe_desc() {
                return exe_desc;
            }

            public void setExe_desc(String exe_desc) {
                this.exe_desc = exe_desc;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public Class<?> getIntentFlag() {
                if (TextUtils.equals("0", getExe_type())) {//费用申请
                    return ExpensesResultActivity.class;
                } else if (TextUtils.equals("1", getExe_type())) {//A模式
                    return FittingApplyDetailActivity.class;
                } else if (TextUtils.equals("2", getExe_type())) {//B模式
                    return FittingResendDetailActivity.class;
                }
                return null;
            }

            public String getAcid() {
                return acid;
            }

            public void setAcid(String acid) {
                this.acid = acid;
            }
        }

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
        private String detailid;
        private String service_id;//服务器返回的id
        private String serviceid = "-1";//保存用户选择的id
        private String service_name = "请选择";
        private String this_service_name;
        private String name;
        private String brand;
        private String stantard;
        private String model;
        private String service_type;
        private String fault_lable;
        private String fault_desc;
        private String product_thumb;
        private String last_handle_type;
        private String last_handle_status;
        private String last_handle_desc;
        private String last_handle_statue_chinese = "";
        private String service_desc = "";
        private String acce_exe_type = "";
        private ButtonType buttonType = ButtonType.NAN;
        private String report = "";
        private ArrayList<ServiceListEntity> service_list;
        private ArrayList<CompletePhotosEntity> complete_photos;
        private ArrayList<String> addPics = new ArrayList<>();
        private String complete_report;
        private String productNum;

        public ListEntity() {
        }

        protected ListEntity(Parcel in) {
            this.detailid = in.readString();
            this.service_id = in.readString();
            this.serviceid = in.readString();
            this.service_name = in.readString();
            this.this_service_name = in.readString();
            this.name = in.readString();
            this.brand = in.readString();
            this.stantard = in.readString();
            this.model = in.readString();
            this.fault_lable = in.readString();
            this.fault_desc = in.readString();
            this.product_thumb = in.readString();
            this.last_handle_type = in.readString();
            this.last_handle_status = in.readString();
            this.last_handle_desc = in.readString();
            this.last_handle_statue_chinese = in.readString();
            this.acce_exe_type = in.readString();
            int tmpButtonType = in.readInt();
            this.buttonType = tmpButtonType == -1 ? null : ButtonType.values()[tmpButtonType];
            this.report = in.readString();
            this.service_list = new ArrayList<ServiceListEntity>();
            in.readList(this.service_list, getClass().getClassLoader());
            this.complete_photos = new ArrayList<CompletePhotosEntity>();
            in.readList(this.complete_photos, getClass().getClassLoader());
            this.addPics = in.createStringArrayList();
            this.complete_report = in.readString();
        }

        @BindingAdapter(value = {"app:compoundDrawable"})
        public static void setCompoundDrawable(TextView textView, String lastHandleType) {
            if (TextUtils.equals(lastHandleType, "3") || TextUtils.equals
                    (lastHandleType, "4")) {
                textView.setCompoundDrawables(null, null, null, null);
            } else {
                Drawable drawable = textView.getResources().getDrawable(R.mipmap.ic_arrow_right);
                if (drawable != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable
                            .getMinimumHeight());
                    textView.setCompoundDrawables(null, null, drawable, null);
                }
            }
        }

        public String getDetailid() {
            return detailid;
        }

        public void setDetailid(String detailid) {
            this.detailid = detailid;
        }

        public String getServiceid() {
            return serviceid;
        }

        public void setServiceid(String serviceid) {
            this.serviceid = serviceid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getStantard() {
            return stantard;
        }

        public void setStantard(String stantard) {
            this.stantard = stantard;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getFault_lable() {
            return fault_lable;
        }

        public void setFault_lable(String fault_lable) {
            this.fault_lable = fault_lable;
        }

        public String getFault_desc() {
            return fault_desc;
        }

        public void setFault_desc(String fault_desc) {
            this.fault_desc = fault_desc;
        }

        public String getProduct_thumb() {
            return product_thumb;
        }

        public void setProduct_thumb(String product_thumb) {
            this.product_thumb = product_thumb;
        }

        @Bindable
        public String getLast_handle_type() {
            return last_handle_type;
        }

        public void setLast_handle_type(String last_handle_type) {
            this.last_handle_type = last_handle_type;
        }

        public String getLast_handle_status() {
            return last_handle_status;
        }

        public void setLast_handle_status(String last_handle_status) {
            this.last_handle_status = last_handle_status;
        }

        @Bindable
        public String getLast_handle_desc() {
            return last_handle_desc;
        }

        public void setLast_handle_desc(String last_handle_desc) {
            this.last_handle_desc = last_handle_desc;
        }

        public ArrayList<ServiceListEntity> getService_list() {
            return service_list;
        }

        public void setService_list(ArrayList<ServiceListEntity> service_list) {
            this.service_list = service_list;
        }

        @Bindable
        public String getLast_handle_statue_chinese() {
            this.last_handle_statue_chinese = OperateLastHandle.getLastHandle(last_handle_type,
                    last_handle_status, acce_exe_type);
            return last_handle_statue_chinese;
        }

        public String getAcce_exe_type() {
            return acce_exe_type;
        }

        public void setAcce_exe_type(String acce_exe_type) {
            this.acce_exe_type = acce_exe_type;
        }

        public boolean canDone() {
            return TextUtils.equals(last_handle_type, "3") || TextUtils.equals(last_handle_type,
                    "4");
        }

        public boolean canNext() {
            return (TextUtils.equals(last_handle_type, "1") && TextUtils.equals
                    (last_handle_status, "2")) ||
                    (TextUtils.equals(last_handle_type, "1") && TextUtils.equals
                            (last_handle_status, "3")) ||
                    (TextUtils.equals(last_handle_type, "2") && TextUtils.equals(acce_exe_type,
                            "1") && TextUtils.equals(last_handle_status, "5")) ||
                    (TextUtils.equals(last_handle_type, "2") && TextUtils.equals(acce_exe_type,
                            "1") && TextUtils.equals(last_handle_status, "6")) ||
                    (TextUtils.equals(last_handle_type, "2") && TextUtils.equals(acce_exe_type,
                            "2") && TextUtils.equals(last_handle_status, "3"));
        }

        @Bindable
        public String getService_name() {
            return TextUtils.isEmpty(service_name) ? "请选择" : service_name;
        }

        public void setService_name(String service_name) {
            this.service_name = service_name;
        }

        public String getService_id() {
            return service_id;
        }

        public void setService_id(String service_id) {
            this.service_id = service_id;
        }

        @Bindable
        public String getThis_service_name() {
            return TextUtils.isEmpty(this_service_name) ? "请选择" : this_service_name;
        }

        public void setThis_service_name(String this_service_name) {
            this.this_service_name = this_service_name;
        }

        public ButtonType getButtonType() {
            return buttonType;
        }

        public void setButtonType(ButtonType buttonType) {
            this.buttonType = buttonType;
        }

        public List<CompletePhotosEntity> getComplete_photos() {
            return complete_photos;
        }

        public void setComplete_photos(List<CompletePhotosEntity> complete_photos) {
            this.complete_photos = (ArrayList<CompletePhotosEntity>) complete_photos;
        }

        public List<String> getAddPics() {
            return addPics;
        }

        public void setAddPics(List<String> addPics) {
            this.addPics = (ArrayList<String>) addPics;
        }

        @Bindable
        public String getReport() {
            return report;
        }

        public void setReport(String report) {
            this.report = report;
            notifyPropertyChanged(BR.report);
        }

        @Bindable
        public String getComplete_report() {
            return complete_report;
        }

        public void setComplete_report(String complete_report) {
            this.complete_report = complete_report;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.detailid);
            dest.writeString(this.service_id);
            dest.writeString(this.serviceid);
            dest.writeString(this.service_name);
            dest.writeString(this.this_service_name);
            dest.writeString(this.name);
            dest.writeString(this.brand);
            dest.writeString(this.stantard);
            dest.writeString(this.model);
            dest.writeString(this.fault_lable);
            dest.writeString(this.fault_desc);
            dest.writeString(this.product_thumb);
            dest.writeString(this.last_handle_type);
            dest.writeString(this.last_handle_status);
            dest.writeString(this.last_handle_desc);
            dest.writeString(this.last_handle_statue_chinese);
            dest.writeString(this.acce_exe_type);
            dest.writeInt(this.buttonType == null ? -1 : this.buttonType.ordinal());
            dest.writeString(this.report);
            dest.writeList(this.service_list);
            dest.writeList(this.complete_photos);
            dest.writeStringList(this.addPics);
            dest.writeString(this.complete_report);
        }

        @Bindable
        public String getService_type() {
            return service_type;
        }

        public void setService_type(String service_type) {
            this.service_type = service_type;
        }

        @Bindable
        public String getProductNum() {
            return productNum;
        }

        public void setProductNum(String productNum) {
            this.productNum = productNum;
        }

        public String getService_desc() {
            return service_desc;
        }

        public void setService_desc(String service_desc) {
            this.service_desc = service_desc;
        }

        public static class ServiceListEntity extends BaseObservable implements Parcelable {

            private String service_id;
            private String service_name;
            private String service_cost;
            private String service_desc;


            public ServiceListEntity() {
            }

            public String getService_id() {
                return service_id;
            }

            public void setService_id(String service_id) {
                this.service_id = service_id;
            }

            @Bindable
            public String getService_name() {
                return service_name;
            }

            public void setService_name(String service_name) {
                this.service_name = service_name;
            }

            @Bindable
            public String getService_cost() {
                return service_cost + " 元";
            }

            public void setService_cost(String service_cost) {
                this.service_cost = service_cost;
            }

            public String getService_desc() {
                return service_desc;
            }

            public void setService_desc(String service_desc) {
                this.service_desc = service_desc;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.service_id);
                dest.writeString(this.service_name);
                dest.writeString(this.service_cost);
                dest.writeString(this.service_desc);
            }

            protected ServiceListEntity(Parcel in) {
                this.service_id = in.readString();
                this.service_name = in.readString();
                this.service_cost = in.readString();
                this.service_desc = in.readString();
            }

            public static final Creator<ServiceListEntity> CREATOR = new Creator<ServiceListEntity>() {
                @Override
                public ServiceListEntity createFromParcel(Parcel source) {
                    return new ServiceListEntity(source);
                }

                @Override
                public ServiceListEntity[] newArray(int size) {
                    return new ServiceListEntity[size];
                }
            };
        }

        public static class CompletePhotosEntity {
            private String name;
            private String url;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }

    static class OperateLastHandle {
        private static String getLastHandle(@NonNull String lastType, @NonNull String lastStatus,
                                            @NonNull String accExeType) {
            switch (lastType) {
                case "0":
                    return "暂无操作";
                case "3":
                    return "维修完成";
                case "4":
                    return "无法维修";
                case "5":
                    return "提交错误报告,等待审核";
                case "1":
                    return getFittingApplyStatus(lastStatus);
                case "2":
                    return getOperateStatus(lastStatus, accExeType);
                default:
                    return "无";
            }
        }

        /**
         * 当上次工单为费用申请的时候， 上次工单的最终状态
         *
         * @param lastStatus 上次处理状态
         * @return 费用申请的最终状态
         */
        private static String getFittingApplyStatus(String lastStatus) {
            switch (lastStatus) {
                case "1":
                    return "费用单申请中,等待后台审核";
                case "2":
                    return "费用单申请成功";
                case "3":
                    return "费用单申请失败";
                default:
                    return "";
            }
        }

        /**
         * 当上次工单状态为 操作配件单的时候 获取上次操作的最终结果
         *
         * @param lastStatus 上次处理状态
         * @param accExeType 配件单执行流程码
         * @return 最终上次操作工单的状态
         */
        private static String getOperateStatus(String lastStatus, String accExeType) {
            switch (accExeType) {
                case "1":
                    return getOperateFirstStatus(lastStatus);
                case "2":
                    return getOperateSecondStatus(lastStatus);
                default:
                    return "";
            }
        }

        /**
         * 当 "技工先申请配件,厂家发货,技工收货后才返回旧配件" 的时候(参考接口文档)
         *
         * @param lastStatus 上次处理状态
         * @return 申请配件状态
         */
        private static String getOperateFirstStatus(String lastStatus) {
            switch (lastStatus) {
                case "1":
                    return "申请配件等待厂家审核";
                case "2":
                    return "厂家审核通过等待发货";
                case "3":
                    return "厂家已发货等待签收";
                case "4":
                    return "已签收等待回寄旧配件";
                case "5":
                    return "厂家已退回";
                case "6":
                    return "厂家审核不通过";
                default:
                    return "";
            }
        }

        /**
         * 当 "技工回寄配件,同时填写收货信息,厂家收到旧配件后才发出新配件" 的时候(参考接口文档)
         *
         * @param lastStatus 上次处理状态
         * @return 申请配件状态
         */
        private static String getOperateSecondStatus(String lastStatus) {
            switch (lastStatus) {
                case "1":
                    return "返回配件等待厂家确认";
                case "2":
                    return "厂家已发新配件等待收货";
                case "3":
                    return "技工已签收";
                default:
                    return "";
            }
        }

    }
}
