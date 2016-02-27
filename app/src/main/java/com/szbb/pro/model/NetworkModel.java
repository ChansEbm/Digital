package com.szbb.pro.model;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.entity.Appointment.AppointmentHistoryItemBean;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.entity.Expenses.ExpenseDetailBean;
import com.szbb.pro.entity.Expenses.ExpensesResultBean;
import com.szbb.pro.entity.Fittings.CustomerAddressBean;
import com.szbb.pro.entity.Fittings.FittingCostBean;
import com.szbb.pro.entity.Fittings.FittingDetailBean;
import com.szbb.pro.entity.Fittings.FittingWareHouseBean;
import com.szbb.pro.entity.Fittings.MyFittingOrderBean;
import com.szbb.pro.entity.Fittings.OtherFittingBean;
import com.szbb.pro.entity.Login.AreaListBean;
import com.szbb.pro.entity.Login.AuthBean;
import com.szbb.pro.entity.Order.MyOrderBean;
import com.szbb.pro.entity.Order.OrderDetailBean;
import com.szbb.pro.entity.Vip.AccountCementBean;
import com.szbb.pro.entity.Vip.BankBean;
import com.szbb.pro.entity.Vip.BankCardBean;
import com.szbb.pro.entity.Vip.OrderHintBean;
import com.szbb.pro.entity.Vip.VipInfoBean;
import com.szbb.pro.entity.Vip.WalletBean;
import com.szbb.pro.entity.Vip.WorkHistoryBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.OkHttpResponseListener;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.LogTools;
import com.szbb.pro.tools.OkHttpBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ChanZeeBm on 2015/12/26.
 */
public class NetworkModel<E> {
    private AppCompatActivity appCompatActivity;
    private View parentView;
    private OkHttpResponseListener<E> tOkHttpResponseListener;
    private Map<String, String> params = new HashMap<>();

    private Map<String, String> fileMaps = new HashMap<>();//file params and each key
    // carry one filePath
    private List<List<String>> fileLists = new ArrayList<>();// file params and each key could be
    // carry multi filePaths

    public NetworkModel(@NonNull AppCompatActivity appCompatActivity, @NonNull View parentView) {
        this.appCompatActivity = appCompatActivity;
        this.parentView = parentView;
    }

    public void setResultCallBack(OkHttpResponseListener<E> tOkHttpResponseListener) {
        this.tOkHttpResponseListener = tOkHttpResponseListener;
    }

    /**
     * user login
     *
     * @param userName      userName
     * @param pwd           password
     * @param networkParams networkParams
     */
    public void login(@NonNull String userName, @NonNull String pwd, NetworkParams
            networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(userName, pwd))
            return;
        params.put("username", userName);
        params.put("password", pwd);
        new OkHttpBuilder.POST(appCompatActivity).entityClass(AuthBean.class).params(params)
                .urlLogin
                        ("login").enqueue
                (networkParams, tOkHttpResponseListener);
    }

    /**
     * get the phone verify code
     *
     * @param phone         phoneNumber
     * @param networkParams networkParams
     */
    public void phoneCode(@NonNull String phone, NetworkParams
            networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(phone))
            return;
        params.put("phone", phone);
        new OkHttpBuilder.POST(appCompatActivity).entityClass(BaseBean.class).params(params)
                .urlLogin("get_phone_code")
                .enqueue
                        (networkParams, tOkHttpResponseListener);
    }

    /**
     * user register
     *
     * @param phone         phoneNum
     * @param code          phone verify code
     * @param password      password
     * @param rePassword    rePassword
     * @param networkParams networkParams
     */
    public void register(@NonNull String phone, @NonNull String code, @NonNull String password,
                         @NonNull String rePassword,
                         NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(phone, code, password, rePassword))
            return;
        params.put("phone", phone);
        params.put("code", code);
        params.put("password", password);
        params.put("repassword", rePassword);
        new OkHttpBuilder.POST(appCompatActivity).entityClass(AuthBean.class).params(params)
                .urlLogin
                        ("register").enqueue
                (networkParams, tOkHttpResponseListener);
    }

    /**
     * get the area data
     *
     * @param pid           parent id
     * @param networkParams networkParams
     */
    public void areaList(@NonNull String pid, NetworkParams networkParams) {
        clearAllParams();
        params.put("auth", getAuth());
        if (!pid.isEmpty())
            params.put("pid", pid);
        new OkHttpBuilder.POST(appCompatActivity).entityClass(AreaListBean.class).urlWorker
                ("getAreaList").params
                (params).enqueue
                (networkParams, tOkHttpResponseListener);
    }

    /**
     * complete worker info
     *
     * @param nickName      nickName
     * @param thumbPath     the thumb pic(head pic)
     * @param workerAreaIds area ids (31,58,24 - province,city,country Ids)
     * @param workerAddress worker address
     * @param lat           latitude
     * @param lng           longitude
     * @param cardNo        citizen ID
     * @param cardFrontPath id pic front
     * @param cardBackPath  id pic back
     * @param networkParams networkParams
     */
    public void saveWorkerInfo(@NonNull String nickName, @NonNull String thumbPath,
                               @NonNull String workerAreaIds, @NonNull String workerAddress,
                               @NonNull String lat, @NonNull String lng, @NonNull String cardNo,
                               @NonNull String cardFrontPath, @NonNull String cardBackPath,
                               NetworkParams
                                       networkParams) {

        fileMaps = new HashMap<>();
        clearAllParams();
        if (isNecessaryFieldEmpty(nickName, thumbPath, workerAreaIds, workerAddress, lat, lat,
                cardFrontPath, cardBackPath))
            return;
        params.put("auth", getAuth());
        params.put("nickname", nickName);
        params.put("worker_area_ids", workerAreaIds);
        params.put("worker_address", workerAddress);
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("card_no", cardNo);

        // the three params do not necessary
        if (!thumbPath.isEmpty())
            fileMaps.put("thumb", thumbPath);
        if (!cardFrontPath.isEmpty())
            fileMaps.put("card_front", cardFrontPath);
        if (!cardBackPath.isEmpty())
            fileMaps.put("card_back", cardBackPath);

        new OkHttpBuilder.POST(appCompatActivity).urlWorker("saveWorkerInfo").params(params,
                fileMaps)
                .entityClass
                        (BaseBean.class)
                .enqueue
                        (networkParams, tOkHttpResponseListener);
    }


    /**
     * find my password
     *
     * @param phone         phone number
     * @param code          phone verify code
     * @param password      new password
     * @param rePassword    reInput new password
     * @param networkParams networkParams
     */
    public void findPwd(@NonNull String phone, @NonNull String code, @NonNull String password,
                        @NonNull String rePassword, NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(phone, code, password, rePassword))
            return;
        params.put("phone", phone);
        params.put("code", code);
        params.put("password", password);
        params.put("repassword", rePassword);
        new OkHttpBuilder.POST(appCompatActivity).urlLogin("forgetPassword").params(params)
                .entityClass
                        (BaseBean.class)
                .enqueue(networkParams, tOkHttpResponseListener);
    }

    /**
     * @param type          type code (1 means newOrder , 2 means pending , 3 means waitingAccount)
     * @param page          page no
     * @param pageSize      data counts in each page
     * @param networkParams networkParams
     */
    public void myOrderList(@NonNull String type, String page, String
            pageSize, NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(type))
            return;
        if (!(TextUtils.isEmpty(page))) {
            params.put("page", page);
        }

        if (!(TextUtils.isEmpty(pageSize))) {
            params.put("pageSize", pageSize);
        }

        params.put("auth", getAuth());
        params.put("type", type);

        new OkHttpBuilder.POST(appCompatActivity).urlOrder("myOrderList").entityClass(MyOrderBean
                .class).params(params)
                .enqueue(networkParams, tOkHttpResponseListener);
    }

    /**
     * @param orderId       orderid
     * @param type          type need to special (0 means appointmentClient's detail,1 means
     *                      operation order's detail)
     * @param networkParams network params
     */
    public void orderDetail(@NonNull String orderId, @NonNull String type, NetworkParams
            networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(orderId))
            return;
        params.put("auth", getAuth());
        params.put("orderid", orderId);
        if (!type.isEmpty())
            params.put("type", type);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("orderDetail").entityClass
                (OrderDetailBean
                        .class).params(params)
                .enqueue(networkParams, tOkHttpResponseListener);
    }

    public void getServiceCode(@NonNull String orderId, NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(orderId))
            return;
        params.put("auth", getAuth());
        params.put("orderid", orderId);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("getServiceCode").entityClass
                (BaseBean.class).params(params)
                .enqueue(networkParams, tOkHttpResponseListener);
    }

    public void orderSettlement(String orderId, NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(orderId))
            return;
        params.put("auth", getAuth());
        params.put("orderid", orderId);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("orderSettlement")
                .entityClass
                        (ExpenseDetailBean.class).params(params)
                .enqueue(networkParams, tOkHttpResponseListener);

    }

    public void completeOrder(String orderId, String code, NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(orderId, code))
            return;
        params.put("auth", getAuth());
        params.put("orderid", orderId);
        params.put("code", code);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("completeOrder").entityClass
                (BaseBean.class).params(params)
                .enqueue(networkParams, tOkHttpResponseListener);
    }

    /**
     * @param orderId         orderid
     * @param result          appointment result 1 means success, 2 means cannot contact client,3
     *                        means cant do the work,4 means cannot  fix the product
     * @param appointmentTime appointment time
     * @param remark          remark
     * @param networkParams   network params
     */
    public void appointOrder(@NonNull String orderId, @NonNull String result, @NonNull String
            appointmentTime, @NonNull String remark, NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(orderId, result))
            return;
        params.put("auth", getAuth());
        params.put("orderid", orderId);
        params.put("result", result);
        if (!appointmentTime.isEmpty()) {
            params.put("appoint_time", appointmentTime);
        }
        if (!remark.isEmpty()) {
            params.put("remarks", remark);
        }
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("appointOrder").entityClass(BaseBean
                .class).params(params)
                .enqueue(networkParams, tOkHttpResponseListener);
    }

    /**
     * sign the appointment
     *
     * @param orderId       the orderId of the order's
     * @param lat           the worker's lat
     * @param lng           the worker's lng
     * @param networkParams networkParams
     */
    public void signAppoint(@NonNull String orderId, double lat, double lng, NetworkParams
            networkParams) {
        clearAllParams();

        if (lat == 0d || lng == 0d || isNecessaryFieldEmpty(orderId)) {
            return;
        }
        params.put("auth", getAuth());
        params.put("orderid", orderId);
        params.put("lat", String.valueOf(lat));
        params.put("lng", String.valueOf(lng));
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("signAppoint").entityClass(BaseBean
                .class).params(params).enqueue(networkParams, tOkHttpResponseListener);
    }

    /**
     * change the appointment
     *
     * @param orderId       the orderId of which order need be change
     * @param appointTime   appoint time
     * @param updateReason  change reason
     * @param remarks       remarks
     * @param networkParams networkParams
     */
    public void changeAppoint(@NonNull String orderId, @NonNull String appointTime, @NonNull String
            updateReason, @NonNull String remarks, NetworkParams
                                      networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(orderId, appointTime, updateReason, remarks))
            return;
        params.put("auth", getAuth());
        params.put("orderid", orderId);
        params.put("appoint_time", appointTime);
        params.put("update_reason", updateReason);
        if (!remarks.isEmpty())
            params.put("remarks", remarks);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("changeAppoint").entityClass(BaseBean
                .class).params(params).enqueue(networkParams, tOkHttpResponseListener);
    }

    /**
     * choose service obj
     *
     * @param detailId      the detail id which protected need be applied service obj
     * @param serviceId     the service's id
     * @param networkParams network params
     */
    public void choiceFaultService(@NonNull String detailId, @NonNull String serviceId,
                                   NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(detailId, serviceId))
            return;
        params.put("auth", getAuth());
        params.put("detailid", detailId);
        params.put("service_id", serviceId);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("choiceFaultService").entityClass
                (BaseBean
                        .class).params(params).enqueue(networkParams, tOkHttpResponseListener);
    }

    /**
     * submit order report(single product)
     *
     * @param detailId      product's id
     * @param isComplete    complete or not ,1 means complete on site,2 otherwise
     * @param thumbs        complete picture
     * @param report        complete report
     * @param networkParams networkParams
     */
    public void submitReport(@NonNull String detailId, @NonNull String isComplete, @NonNull
    List<String> thumbs, String report, NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(detailId, isComplete))
            return;
        params.put("auth", getAuth());
        params.put("detailid", detailId);
        params.put("is_complete", isComplete);
        if (!TextUtils.isEmpty(report)) {
            params.put("report", report);
        }

        new OkHttpBuilder.POST(appCompatActivity).urlOrder("submitReport").entityClass(BaseBean
                .class).params(params, thumbs, "thumb[]").enqueue(networkParams,
                tOkHttpResponseListener);

    }

    /**
     * submit wrong product
     *
     * @param detailId      the product's id
     * @param info          the product's info
     * @param filePaths     the upload pics
     * @param networkParams networkParams
     */
    public void wrongProduct(@NonNull String detailId, @NonNull String info, @NonNull
    List<String> filePaths, NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(detailId, info) || isNecessaryListEmpty(filePaths))
            return;
        params.put("auth", getAuth());
        params.put("detailid", detailId);
        params.put("info", info);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("wrongProduct").entityClass(BaseBean
                .class).params(params,
                filePaths, "thumb[]").enqueue(
                networkParams, tOkHttpResponseListener);
    }

    /**
     * apply expenses
     *
     * @param detailId      the product's id
     * @param money         apply price
     * @param costType      apply type
     * @param costUse       apply name
     * @param remarks       remarks
     * @param filePaths     the pic's path
     * @param networkParams networkParams
     */
    public void applyCost(@NonNull String detailId, @NonNull String money, @NonNull String
            costType, String remarks, List<String> filePaths,
                          NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(detailId, money, costType))
            return;
        params.put("auth", getAuth());
        params.put("detailid", detailId);
        params.put("money", money);
        params.put("cost_type", costType);
        if (!remarks.isEmpty())
            params.put("remarks", remarks);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("applyCost").entityClass(BaseBean
                .class).params(params,
                filePaths, "thumb[]").enqueue(
                networkParams, tOkHttpResponseListener);
    }

    /**
     * get appointment history list
     *
     * @param orderId       the order's id
     * @param networkParams networkParams
     */
    public void appointList(@NonNull String orderId, NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(orderId))
            return;
        params.put("auth", getAuth());
        params.put("orderid", orderId);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("appointList").entityClass
                (AppointmentHistoryItemBean
                        .class).params(params).enqueue(networkParams,
                tOkHttpResponseListener);
    }

    /**
     * request return back order
     *
     * @param orderId       the order's id
     * @param operation     the reason why return the order
     * @param desc          the description about the order
     * @param networkParams networkParams
     */
    public void returnOrder(@NonNull String orderId, @NonNull String operation, String desc,
                            NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(orderId, operation)) {
            return;
        }
        params.put("auth", getAuth());
        params.put("orderid", orderId);
        params.put("operation", operation);
        if (TextUtils.isEmpty(desc))
            params.put("desc", desc);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("returnOrder").entityClass
                (BaseBean
                        .class).params(params).enqueue(networkParams, tOkHttpResponseListener);
    }


    /**
     * fitting ware house data
     *
     * @param orderId       the order's id
     * @param serviceId     the service's id
     * @param partSid       the fitting 's classification(not necessary)
     * @param keyWord       the search key word(not necessary)
     * @param networkParams network params
     */
    public void productAcceList(@NonNull String orderId, @NonNull String serviceId,
                                String partSid, String keyWord,
                                NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(orderId, serviceId)) {
            return;
        }
        params.put("auth", getAuth());
        params.put("orderid", orderId);
        params.put("serviceid", serviceId);
        if (!TextUtils.isEmpty(partSid)) {
            params.put("partsid", partSid);
        }
        if (!TextUtils.isEmpty(keyWord)) {
            params.put("keyword", keyWord);
        }
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("productAcceList").entityClass
                (FittingWareHouseBean
                        .class).params(params).enqueue(networkParams, tOkHttpResponseListener);
    }

    /**
     * get customer default address
     *
     * @param orderId       order's id
     * @param networkParams network params
     */
    public void getCustomerAddress(@NonNull String orderId, NetworkParams networkParams) {
        clearAllParams();
        params.put("auth", getAuth());
        if (isNecessaryFieldEmpty(orderId)) {
            return;
        }
        params.put("orderid", orderId);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("get_customer_address").entityClass
                (CustomerAddressBean
                        .class).params(params).enqueue(networkParams, tOkHttpResponseListener);
    }

    public void getMechanicAddress(NetworkParams networkParams) {
        clearAllParams();
        params.put("auth", getAuth());
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("get_address").entityClass
                (CustomerAddressBean
                        .class).params(params).enqueue(networkParams, tOkHttpResponseListener);
    }

    /**
     * get factory default address
     *
     * @param orderId       order's id
     * @param networkParams network params
     */
    public void getFactoryAddress(@NonNull String orderId, NetworkParams networkParams) {
        clearAllParams();
        params.put("auth", getAuth());
        if (isNecessaryFieldEmpty(orderId)) {
            return;
        }
        params.put("orderid", orderId);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("get_factory_address").entityClass
                (CustomerAddressBean
                        .class).params(params).enqueue(networkParams, tOkHttpResponseListener);
    }


    /**
     * post apply fitting first
     *
     * @param detailId      detail's id
     * @param acces         fitting's detail
     * @param otherAcces    other fitting's detail
     * @param fileThumb     the pics which need to be upload
     * @param remarks       remarks
     * @param applicant     receiver
     * @param applicantTell receiver phone num
     * @param areaIds       receiver's area id
     * @param address       address
     * @param networkParams network params
     */
    public void requireAcce(@NonNull String detailId, @NonNull List<String> acces, @NonNull
    List<String> otherAcces, List<String> fileThumb, String remarks, @NonNull String applicant,
                            @NonNull String applicantTell, @NonNull String areaIds, @NonNull String
                                    address, NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(detailId, applicant, applicantTell, areaIds, address)) {
            return;
        }
        params.put("auth", getAuth());
        params.put("detailid", detailId);
        params.put("applicant", applicant);
        params.put("applicant_tell", applicantTell);
        params.put("area_ids", areaIds);
        params.put("address", address);
        if (!TextUtils.isEmpty(remarks)) {
            params.put("remarks", remarks);
        }
        List<List<String>> allStringParams = new ArrayList<>();
        allStringParams.add(acces);
        allStringParams.add(otherAcces);
        allStringParams.add(fileThumb);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("requireAcce").entityClass(BaseBean
                .class).params(params,
                allStringParams, "acces[]", "other_acces[]", "thumb[]").enqueue(networkParams,
                tOkHttpResponseListener);
    }

    /**
     * other fittings
     *
     * @param detailId      detail's id
     * @param name          new order's name
     * @param number        new order's count
     * @param accCode       new order's QR code
     * @param remarks       remarks
     * @param thumbPaths    thumb's path
     * @param networkParams networkParams
     */
    public void addOtherAcce(@NonNull String detailId, @NonNull String name, @NonNull String
            number, String accCode, @NonNull String remarks, @NonNull List<String> thumbPaths,
                             NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(detailId, name, number, accCode, remarks) ||
                isNecessaryListEmpty(thumbPaths)) {
            return;
        }

        params.put("auth", getAuth());
        params.put("detailid", detailId);
        params.put("name", name);
        params.put("nums", number);
        params.put("acce_code", accCode);
        params.put("remarks", remarks);

        new OkHttpBuilder.POST(appCompatActivity).urlOrder("addOtherAcce").entityClass
                (OtherFittingBean
                        .class).params(params, thumbPaths, "thumb[]").enqueue(networkParams,
                tOkHttpResponseListener);
    }

    public void applyAcce(@NonNull String detailId, @NonNull List<String> acces, List<String>
            otherAcces, List<String> thumbs, String remarks, @NonNull String shippingType,
                          @NonNull String shippingNum, @NonNull String shippingPayType, @NonNull
                          String shippingCost, @NonNull String applicant, @NonNull String
                                  applicantTell, @NonNull String areaIds, String address,
                          NetworkParams networkParams) {
        if (isNecessaryFieldEmpty(shippingType, shippingNum, shippingPayType, shippingCost,
                applicant, applicantTell, areaIds, address))
            return;
        clearAllParams();

        params.put("auth", getAuth());
        params.put("detailid", detailId);
        if (!remarks.isEmpty())
            params.put("remarks", remarks);
        params.put("shipping_type", shippingType);
        params.put("shipping_num", shippingNum);
        params.put("shipping_paytype", shippingPayType);
        params.put("shipping_cost", shippingCost);
        params.put("applicant", applicant);
        params.put("applicant_tell", applicantTell);
        params.put("area_ids", areaIds);
        params.put("address", address);

        fileLists.clear();
        fileLists.add(acces);
        fileLists.add(otherAcces);
        fileLists.add(thumbs);

        new OkHttpBuilder.POST(appCompatActivity).urlOrder("applyAcce").entityClass
                (BaseBean
                        .class).params(params, fileLists, "acces[]", "other_acces[]", "thumb[]")
                .enqueue
                        (networkParams,
                                tOkHttpResponseListener);
    }

    public void acceList(@NonNull String status, String page, String
            pageSize, NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(status))
            return;
        params.put("auth", getAuth());
        params.put("status", status);
        if (!page.isEmpty())
            params.put("page", page);
        if (!pageSize.isEmpty())
            params.put("pageSize", pageSize);

        new OkHttpBuilder.POST(appCompatActivity).urlApiAccessOrder("acceList").entityClass
                (MyFittingOrderBean
                        .class).params(params).enqueue(networkParams,
                tOkHttpResponseListener);
    }

    public void costList(@NonNull String status, String page, String pageSize, NetworkParams
            networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(status))
            return;
        params.put("auth", getAuth());
        params.put("status", status);
        if (!page.isEmpty())
            params.put("page", page);
        if (!pageSize.isEmpty())
            params.put("pageSize", pageSize);
        new OkHttpBuilder.POST(appCompatActivity).urlApiAccessOrder("costList").entityClass
                (FittingCostBean
                        .class).params(params).enqueue(networkParams,
                tOkHttpResponseListener);
    }

    public void acceDetail(@NonNull String acceId, NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(acceId))
            return;
        params.put("auth", getAuth());
        params.put("acceid", acceId);
        new OkHttpBuilder.POST(appCompatActivity).urlApiAccessOrder("acceDetail").entityClass
                (FittingDetailBean
                        .class).params(params).enqueue(networkParams,
                tOkHttpResponseListener);
    }

    public void remindFactSend(@NonNull String acceId, NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(acceId))
            return;
        params.put("auth", getAuth());
        params.put("acceid", acceId);
        new OkHttpBuilder.POST(appCompatActivity).urlApiAccessOrder("remindFactSend").entityClass
                (BaseBean
                        .class).params(params).enqueue(networkParams,
                tOkHttpResponseListener);
    }

    public void signForAcce(@NonNull String acceId, NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(acceId))
            return;
        params.put("auth", getAuth());
        params.put("acceid", acceId);
        new OkHttpBuilder.POST(appCompatActivity).urlApiAccessOrder("signForAcce").entityClass
                (BaseBean
                        .class).params(params).enqueue(networkParams,
                tOkHttpResponseListener);
    }

    public void returnAcce(@NonNull String acceId, List<String> thumb, String remarks, @NonNull
    String
            shippingType, @NonNull String shippingNum, @NonNull String shippingPaytype, @NonNull
                           String shippingCost, NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(acceId, shippingType, shippingNum, shippingPaytype, shippingCost))
            return;
        params.put("auth", getAuth());
        params.put("acceid", acceId);
        params.put("shipping_type", shippingType);
        params.put("shipping_num", shippingNum);
        params.put("shipping_paytype", shippingPaytype);
        params.put("shipping_cost", shippingCost);
        if (!TextUtils.isEmpty(remarks))
            params.put("remarks", acceId);
        new OkHttpBuilder.POST(appCompatActivity).urlApiAccessOrder("returnAcce").entityClass
                (BaseBean
                        .class).params(params, thumb, "thumb[]").enqueue(networkParams,
                tOkHttpResponseListener);

    }

    public void costDetail(@NonNull String costId, NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(costId))
            return;
        params.put("auth", getAuth());
        params.put("costid", costId);
        new OkHttpBuilder.POST(appCompatActivity).urlApiAccessOrder("costDetail").entityClass
                (ExpensesResultBean
                        .class).params(params).enqueue(networkParams,
                tOkHttpResponseListener);
    }

    public void workerInfo(NetworkParams networkParams) {
        clearAllParams();
        params.put("auth", getAuth());
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("workerInfo").entityClass
                (VipInfoBean
                        .class).params(params).enqueue(networkParams,
                tOkHttpResponseListener);
    }

    public void wallet(NetworkParams networkParams) {
        clearAllParams();
        params.put("auth", getAuth());
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("wallet").entityClass
                (WalletBean
                        .class).params(params).enqueue(networkParams,
                tOkHttpResponseListener);
    }

    public void myCard(NetworkParams networkParams) {
        clearAllParams();
        params.put("auth", getAuth());
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("myCard").entityClass
                (BankCardBean
                        .class).params(params).enqueue(networkParams,
                tOkHttpResponseListener);
    }

    public void addCard(@NonNull String creditCard, @NonNull String bankId, @NonNull
    String cityId, @NonNull String payPassword,
                        NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(creditCard, bankId, cityId, payPassword))
            return;
        params.put("auth", getAuth());
        params.put("credit_card", creditCard);
        params.put("bank_id", bankId);
        params.put("city_id", cityId);
        params.put("pay_password", payPassword);
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("addCard").entityClass
                (BaseBean
                        .class).params(params).enqueue(networkParams,
                tOkHttpResponseListener);
    }


    public void bankList(NetworkParams networkParams) {
        clearAllParams();
        params.put("auth", getAuth());
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("bankList").entityClass
                (BankBean
                        .class).params(params).enqueue(networkParams,
                tOkHttpResponseListener);
    }

    public void orderHistory(NetworkParams networkParams) {
        clearAllParams();
        params.put("auth", getAuth());
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("orderHistory").entityClass
                (WorkHistoryBean
                        .class).params(params).enqueue(networkParams,
                tOkHttpResponseListener);
    }

    public void orderHintList(@NonNull String type, NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(type)) {
            return;
        }
        params.put("auth", getAuth());
        params.put("type", type);
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("orderHintList").entityClass
                (OrderHintBean
                        .class).params(params).enqueue(networkParams,
                tOkHttpResponseListener);
    }

    public void announcementList(@NonNull String type, NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(type)) {
            return;
        }
        params.put("auth", getAuth());
        params.put("type", type);
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("announcementList").entityClass
                (AccountCementBean
                        .class).params(params).enqueue(networkParams,
                tOkHttpResponseListener);
    }


    public void setPayPassword(@NonNull String payPassword, @NonNull String rePayPassword,
                               NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(payPassword)) return;
        params.put("auth", getAuth());
        params.put("pay_password", payPassword);
        params.put("re_password", rePayPassword);
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("setPayPassword").entityClass
                (BaseBean
                        .class).params(params).enqueue(networkParams,
                tOkHttpResponseListener);
    }

    public void checkPayPassword(@NonNull String payPassword, NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(payPassword)) return;
        params.put("auth", getAuth());
        params.put("pay_password", payPassword);
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("checkPayPassword").entityClass
                (BaseBean
                        .class).params(params).enqueue(networkParams,
                tOkHttpResponseListener);
    }


    private boolean isNecessaryFieldEmpty(String... strings) {
        for (String string : strings) {
            if (TextUtils.isEmpty(string)) {
                logError();
                return true;
            }
        }
        return false;
    }

    private boolean isNecessaryListEmpty(List<String>... lists) {
        for (List<String> list : lists) {
            if (list.isEmpty()) {
                logError();
                return true;
            }
        }
        return false;
    }

    private void logError() {
        LogTools.e("necessary field is empty");
    }


    private void clearAllParams() {
        params.clear();
    }

    /**
     * @return the user's auth
     */
    private String getAuth() {
        if (AppKeyMap.IS_DEBUG) {
            return "ADUPMwxrV2VRYgBnB2BQZVRrUWUFMwdnVWxQZgZlA2EBYA4zBjNTYFcyUmNXYgZlBTULPQBkAmADPlZnAjADZAAODzcMPFdh==";
        } else {
            if (AppKeyMap.isAuthEmpty()) {
                LogTools.e("Auth is null,plz check");
                return "";
            } else {
                return AppTools.getStringSharedPreferences(AppKeyMap.AUTH,
                        "ADAPMAw2V2RRMQAyB2VQYVRlUWUFYwcwVT1QYwZmA2MBPg5kBmFTMFdoUjBXbQY4BTcLOAA1AmcDOFZhAmADZAAODz4MPQ==");
            }
        }
    }
}
