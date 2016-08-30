package com.szbb.pro.entity.grab;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;
import android.view.View;

import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.tools.DataFormatter;

import java.util.List;

/**
 * Created by KenChan on 16/6/25.
 */
public class GrabBean
        extends BaseBean {

    @Bindable
    public Spanned getOrderNumsDesc () {
        return Html
                .fromHtml("您所在的城市当日已有" + "<font color=\"#ff99000\">" + order_nums +
                          "</font>单被抢");
    }

    @Bindable
    public Spanned getSignedDesc () {
        return Html.fromHtml("成为神州联保 <font color=\"#ff99000\">签约维修商</font>,优先获得抢单资格");
    }

    /**
     * order_nums : 1637
     * list : [{"orderid":"8589","nickname":"智慧单二","tel":"13450788338","address":"智慧新城T6栋703A",
     * "lat":"23.014388","lng":"113.064882","orno":"A160629111951313",
     * "datetime":"2016年06月29日11时19分51秒","product_list":[{"servicebrand_desc":"樱花",
     * "product_name":"燃气热水器","stantard_desc":"6L","fault_desc":"智慧新城智慧新城智慧新城智慧新城智慧新城智慧新城智慧新城"}],
     * "distance":"6.0公里","service_type":"维修","factory_desc":"佛山爱皮皮科技A","product_name":"燃气热水器",
     * "fault_desc":"智慧新城智慧新城智慧新城智慧新城智慧新城智慧新城智慧新城","product_count":"1","price":"100",
     * "score":"50","recovery_time":21600,"servicebrand_desc":"樱花","stantard_desc":"6L",
     * "nums":"1"},{"orderid":"8588","nickname":"智慧单一","tel":"13450788338",
     * "address":"智慧新城T6栋703A","lat":"23.014388","lng":"113.064882","orno":"A160629111837765",
     * "datetime":"2016年06月29日11时18分37秒","product_list":[{"servicebrand_desc":"樱花",
     * "product_name":"燃气热水器","stantard_desc":"6L","fault_desc":"智慧新城智慧新城智慧新城智慧新城智慧新城智慧新城"},
     * {"servicebrand_desc":"樱花","product_name":"燃气热水器","stantard_desc":"6L",
     * "fault_desc":"智慧新城智慧新城智慧新城智慧新城智慧新城智慧新城智慧新城智慧新城"},{"servicebrand_desc":"樱花",
     * "product_name":"燃气热水器","stantard_desc":"6L","fault_desc":"智慧新城智慧新城智慧新城智慧新城智慧新城智慧新城"}],
     * "distance":"6.0公里","service_type":"维修","factory_desc":"佛山爱皮皮科技A","product_name":"燃气热水器",
     * "fault_desc":"智慧新城智慧新城智慧新城智慧新城智慧新城智慧新城","product_count":"3","price":"100","score":"50",
     * "recovery_time":21600,"servicebrand_desc":"樱花","stantard_desc":"6L","nums":"1"}]
     * cond : {"lat":"23.032906","lng":"113.119747","radius":0,"service_id":1,"cate_id":1,
     * "price_range":""}
     */

    private int order_nums;
    /**
     * lat : 23.032906
     * lng : 113.119747
     * radius : 0
     * service_id : 1
     * cate_id : 1
     * price_range :
     */

    private CondBean cond;
    /**
     * orderid : 8589
     * nickname : 智慧单二
     * tel : 13450788338
     * address : 智慧新城T6栋703A
     * lat : 23.014388
     * lng : 113.064882
     * orno : A160629111951313
     * datetime : 2016年06月29日11时19分51秒
     * product_list : [{"servicebrand_desc":"樱花","product_name":"燃气热水器","stantard_desc":"6L",
     * "fault_desc":"智慧新城智慧新城智慧新城智慧新城智慧新城智慧新城智慧新城"}]
     * distance : 6.0公里
     * service_type : 维修
     * factory_desc : 佛山爱皮皮科技A
     * product_name : 燃气热水器
     * fault_desc : 智慧新城智慧新城智慧新城智慧新城智慧新城智慧新城智慧新城
     * product_count : 1
     * price : 100
     * score : 50
     * recovery_time : 21600
     * servicebrand_desc : 樱花
     * stantard_desc : 6L
     * nums : 1
     */

    private List<ListBean> list;

    @Bindable
    public int getOrder_nums () {
        return order_nums;
    }

    public void setOrder_nums (int order_nums) {
        this.order_nums = order_nums;
    }

    public CondBean getCond () {
        return cond;
    }

    public void setCond (CondBean cond) {
        this.cond = cond;
    }

    public List<ListBean> getList () {
        return list;
    }

    public void setList (List<ListBean> list) {
        this.list = list;
    }

    public static class CondBean
            extends BaseObservable {
        private String lat;
        private String lng;
        private int radius;
        private int service_id;
        private int cate_id;
        private String price_range;

        @Bindable
        public String getLat () {
            return lat;
        }

        public void setLat (String lat) {
            this.lat = lat;
        }

        @Bindable
        public String getLng () {
            return lng;
        }

        public void setLng (String lng) {
            this.lng = lng;
        }

        @Bindable
        public int getRadius () {
            return radius;
        }

        public void setRadius (int radius) {
            this.radius = radius;
        }

        @Bindable
        public int getService_id () {
            return service_id;
        }

        public void setService_id (int service_id) {
            this.service_id = service_id;
        }

        @Bindable
        public int getCate_id () {
            return cate_id;
        }

        public void setCate_id (int cate_id) {
            this.cate_id = cate_id;
        }

        @Bindable
        public String getPrice_range () {
            return price_range;
        }

        public void setPrice_range (String price_range) {
            this.price_range = price_range;
        }
    }

    public static class ListBean
            extends BaseObservable
            implements Parcelable {

        private String orderid;
        private String nickname;
        private String tel;
        private String address;
        private String lat;
        private String lng;
        private String orno;
        private String datetime;
        private String distance;
        private String service_type;
        private String factory_desc;
        private String product_name;
        private String fault_desc;
        private String product_count;
        private String price;
        private String score;
        private int recovery_time;
        private String servicebrand_desc;
        private String stantard_desc;
        private String nums;
        private String is_multi_product;

        /**
         * servicebrand_desc : 樱花
         * product_name : 燃气热水器
         * stantard_desc : 6L
         * fault_desc : 智慧新城智慧新城智慧新城智慧新城智慧新城智慧新城智慧新城
         */

        private List<ProductListBean> product_list;
        private Spanned recovery_time_desc;
        private Spanned distanceDesc;

        @BindingAdapter(value = "app:isMultiProduct")
        public static void isMultiProduct (View view, String is_multi_product) {
            boolean isMultiProduct = is_multi_product.equals("1");
            view.setVisibility(isMultiProduct ? View.VISIBLE : View.GONE);
        }

        public String getOrderid () {
            return orderid;
        }

        @Bindable
        public Spanned getRecovery_time_desc () {
            long grabTime = recovery_time * 1000;
            recovery_time_desc = Html.fromHtml("离回收时间还有" + "<font color=\"red\">" + DataFormatter
                    .convertMillions(grabTime) + "</font>");
            //<font color="red"> DataFormatter.convertMillions(grabTime)</font>
            return recovery_time_desc;
        }

        @Bindable
        public Spanned getDistanceDesc () {
            distanceDesc = Html.fromHtml("距离用户还有" + "<font color=\"#ff9000\">" + distance +
                                         "</font>");
            return distanceDesc;
        }


        @Bindable
        public String getProduct_count () {
            product_count = product_count + "台";
            return product_count;
        }

        @Bindable
        public String getDistance () {
            return distance;
        }

        public void setOrderid (String orderid) {
            this.orderid = orderid;
        }

        @Bindable
        public String getNickname () {
            return nickname;
        }

        public void setNickname (String nickname) {
            this.nickname = nickname;
        }

        @Bindable
        public String getTel () {
            return tel;
        }

        public void setTel (String tel) {
            this.tel = tel;
        }

        @Bindable
        public String getAddress () {
            return address;
        }

        public void setAddress (String address) {
            this.address = address;
        }

        @Bindable
        public String getLat () {
            return lat;
        }

        public void setLat (String lat) {
            this.lat = lat;
        }

        @Bindable
        public String getLng () {
            return lng;
        }

        public void setLng (String lng) {
            this.lng = lng;
        }

        @Bindable
        public String getOrno () {
            return orno;
        }

        public void setOrno (String orno) {
            this.orno = orno;
        }

        @Bindable
        public String getDatetime () {
            return datetime;
        }

        public void setDatetime (String datetime) {
            this.datetime = datetime;
        }


        public void setDistance (String distance) {
            this.distance = distance;
        }

        @Bindable
        public String getService_type () {
            return service_type;
        }

        public void setService_type (String service_type) {
            this.service_type = service_type;
        }

        @Bindable
        public String getFactory_desc () {
            return factory_desc;
        }

        public void setFactory_desc (String factory_desc) {
            this.factory_desc = factory_desc;
        }

        @Bindable
        public String getProduct_name () {
            return product_name;
        }

        public void setProduct_name (String product_name) {
            this.product_name = product_name;
        }

        @Bindable
        public String getFault_desc () {
            return fault_desc;
        }

        public void setFault_desc (String fault_desc) {
            this.fault_desc = fault_desc;
        }


        public void setProduct_count (String product_count) {
            this.product_count = product_count;
        }

        @Bindable
        public String getPrice () {
            return price;
        }

        public void setPrice (String price) {
            this.price = price;
        }

        @Bindable
        public String getScore () {
            return score;
        }

        public void setScore (String score) {
            this.score = score;
        }

        @Bindable
        public int getRecovery_time () {
            return recovery_time;
        }

        public void setRecovery_time (int recovery_time) {
            this.recovery_time = recovery_time;
        }

        @Bindable
        public String getServicebrand_desc () {
            return servicebrand_desc;
        }

        public void setServicebrand_desc (String servicebrand_desc) {
            this.servicebrand_desc = servicebrand_desc;
        }

        @Bindable
        public String getStantard_desc () {
            return stantard_desc;
        }

        public void setStantard_desc (String stantard_desc) {
            this.stantard_desc = stantard_desc;
        }

        @Bindable
        public String getNums () {
            return nums;
        }

        public void setNums (String nums) {
            this.nums = nums;
        }

        public List<ProductListBean> getProduct_list () {
            return product_list;
        }

        public void setProduct_list (List<ProductListBean> product_list) {
            this.product_list = product_list;
        }

        public String getIs_multi_product () {
            return is_multi_product;
        }

        public void setIs_multi_product (String is_multi_product) {
            this.is_multi_product = is_multi_product;
        }

        public static class ProductListBean
                extends BaseObservable
                implements Parcelable {

            private String servicebrand_desc;
            private String product_name;
            private String stantard_desc;
            private String fault_desc;
            private String service_type;

            @Bindable
            public String getServicebrand_desc () {
                return servicebrand_desc;
            }

            public void setServicebrand_desc (String servicebrand_desc) {
                this.servicebrand_desc = servicebrand_desc;
            }

            @Bindable
            public String getProduct_name () {
                return product_name;
            }

            public void setProduct_name (String product_name) {
                this.product_name = product_name;
            }

            @Bindable
            public String getStantard_desc () {
                return stantard_desc;
            }

            public void setStantard_desc (String stantard_desc) {
                this.stantard_desc = stantard_desc;
            }

            @Bindable
            public String getFault_desc () {
                return fault_desc;
            }

            public void setFault_desc (String fault_desc) {
                this.fault_desc = fault_desc;
            }

            @Override
            public int describeContents () {
                return 0;
            }

            @Override
            public void writeToParcel (Parcel dest, int flags) {
                dest.writeString(this.servicebrand_desc);
                dest.writeString(this.product_name);
                dest.writeString(this.stantard_desc);
                dest.writeString(this.fault_desc);
                dest.writeString(this.service_type);
            }

            public ProductListBean () {
            }

            protected ProductListBean (Parcel in) {
                this.servicebrand_desc = in.readString();
                this.product_name = in.readString();
                this.stantard_desc = in.readString();
                this.fault_desc = in.readString();
                this.service_type = in.readString();
            }

            public static final Parcelable.Creator<ProductListBean> CREATOR = new Parcelable
                    .Creator<ProductListBean>() {
                @Override
                public ProductListBean createFromParcel (Parcel source) {
                    return new ProductListBean(source);
                }

                @Override
                public ProductListBean[] newArray (int size) {
                    return new ProductListBean[size];
                }
            };

            @Bindable
            public String getService_type () {
                return service_type;
            }

            public void setService_type (String service_type) {
                this.service_type = service_type;
            }
        }

        @Override
        public int describeContents () {
            return 0;
        }

        @Override
        public void writeToParcel (Parcel dest, int flags) {
            dest.writeString(this.orderid);
            dest.writeString(this.nickname);
            dest.writeString(this.tel);
            dest.writeString(this.address);
            dest.writeString(this.lat);
            dest.writeString(this.lng);
            dest.writeString(this.orno);
            dest.writeString(this.datetime);
            dest.writeString(this.distance);
            dest.writeString(this.service_type);
            dest.writeString(this.factory_desc);
            dest.writeString(this.product_name);
            dest.writeString(this.fault_desc);
            dest.writeString(this.product_count);
            dest.writeString(this.price);
            dest.writeString(this.score);
            dest.writeInt(this.recovery_time);
            dest.writeString(this.servicebrand_desc);
            dest.writeString(this.stantard_desc);
            dest.writeString(this.nums);
            dest.writeString(this.is_multi_product);
            dest.writeTypedList(this.product_list);

        }

        public ListBean () {
        }

        protected ListBean (Parcel in) {
            this.orderid = in.readString();
            this.nickname = in.readString();
            this.tel = in.readString();
            this.address = in.readString();
            this.lat = in.readString();
            this.lng = in.readString();
            this.orno = in.readString();
            this.datetime = in.readString();
            this.distance = in.readString();
            this.service_type = in.readString();
            this.factory_desc = in.readString();
            this.product_name = in.readString();
            this.fault_desc = in.readString();
            this.product_count = in.readString();
            this.price = in.readString();
            this.score = in.readString();
            this.recovery_time = in.readInt();
            this.servicebrand_desc = in.readString();
            this.stantard_desc = in.readString();
            this.nums = in.readString();
            this.is_multi_product = in.readString();
            this.product_list = in.createTypedArrayList(ProductListBean.CREATOR);
            this.recovery_time_desc = in.readParcelable(Spanned.class.getClassLoader());
            this.distanceDesc = in.readParcelable(Spanned.class.getClassLoader());
        }

        public static final Parcelable.Creator<ListBean> CREATOR = new Parcelable
                .Creator<ListBean>() {
            @Override
            public ListBean createFromParcel (Parcel source) {
                return new ListBean(source);
            }

            @Override
            public ListBean[] newArray (int size) {
                return new ListBean[size];
            }
        };
    }
}


//
//    @Bindable
//    public Spanned getRecovery_time_desc() {
//        long grabTime = recovery_time * 1000;
//        recovery_time_desc = Html.fromHtml("离回收时间还有" + "<font color=\"red\">" + DataFormatter
//                .convertMillions(grabTime) + "</font>");
//        //<font color="red"> DataFormatter.convertMillions(grabTime)</font>
//        return recovery_time_desc;
//    }
//
//    @Bindable
//    public Spanned getDistanceDesc() {
//        distanceDesc = Html.fromHtml("距离用户还有" + "<font color=\"#ff9000\">" + distance +
//                                             "</font>");
//        return distanceDesc;
//    }


//    @Bindable
//    public String getProduct_count() {
//        product_count = product_count + "台";
//        return product_count;
//    }

//    @Bindable
//    public String getDistance() {
//        distance = "距离用户" + distance;
//        return distance;
//    }

//    @Bindable
//    public Spanned getOrderNumsDesc() {
//        orderNumsDesc = Html.fromHtml("您所在的城市当日已有" + "<font color=\"#ff99000\">" + order_nums +
//                                              "</font>" + "单被抢");
//        return orderNumsDesc;
//    }
//
