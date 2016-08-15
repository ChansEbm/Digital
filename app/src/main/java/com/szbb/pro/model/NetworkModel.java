package com.szbb.pro.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.entity.appointment.AppointmentHistoryItemBean;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.entity.expenses.ExpenseDetailBean;
import com.szbb.pro.entity.expenses.ExpensesResultBean;
import com.szbb.pro.entity.fittings.CustomerAddressBean;
import com.szbb.pro.entity.fittings.ExpressComBean;
import com.szbb.pro.entity.fittings.FittingCostBean;
import com.szbb.pro.entity.fittings.FittingDetailBean;
import com.szbb.pro.entity.fittings.FittingWareHouseBean;
import com.szbb.pro.entity.fittings.MyFittingOrderBean;
import com.szbb.pro.entity.fittings.OtherFittingBean;
import com.szbb.pro.entity.grab.GrabAgreementBean;
import com.szbb.pro.entity.login.AreaListBean;
import com.szbb.pro.entity.login.AuthBean;
import com.szbb.pro.entity.grab.GrabBean;
import com.szbb.pro.entity.order.MyOrderBean;
import com.szbb.pro.entity.order.OrderDetailBean;
import com.szbb.pro.entity.order.OrderMsgBean;
import com.szbb.pro.entity.order.OrderMsgListBean;
import com.szbb.pro.entity.order.OrderTrackingBean;
import com.szbb.pro.entity.vip.AccountCementBean;
import com.szbb.pro.entity.vip.BankBean;
import com.szbb.pro.entity.vip.CheckUpdateBean;
import com.szbb.pro.entity.vip.CreditCardBean;
import com.szbb.pro.entity.vip.FeedbackBean;
import com.szbb.pro.entity.vip.IncomeBean;
import com.szbb.pro.entity.vip.OrderHintBean;
import com.szbb.pro.entity.vip.OtherCostBean;
import com.szbb.pro.entity.vip.VipInfoBean;
import com.szbb.pro.entity.vip.WalletBean;
import com.szbb.pro.entity.vip.WithdrawBean;
import com.szbb.pro.entity.vip.WorkHistoryBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.OkHttpResponseListener;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.DataFormatter;
import com.szbb.pro.tools.LogTools;
import com.szbb.pro.tools.MiscUtils;
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
    private Context context;
    private OkHttpResponseListener<E> tOkHttpResponseListener;
    private Map<String, String> params = new HashMap<>();

    // carry one filePath
    private List<List<String>> fileLists = new ArrayList<>();// file params and each key could be
    // carry multi filePaths

    public NetworkModel (@NonNull AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    public NetworkModel (@NonNull Context context) {
        this.context = context;
        if (context instanceof AppCompatActivity) {
            this.appCompatActivity = (AppCompatActivity) context;
        }
    }

    public void setResultCallBack (OkHttpResponseListener<E> tOkHttpResponseListener) {
        this.tOkHttpResponseListener = tOkHttpResponseListener;
    }

    /**
     * user login
     *
     * @param userName      userName
     * @param pwd           password
     * @param networkParams networkParams
     */
    public void login (@NonNull String userName,
                       @NonNull String pwd,
                       NetworkParams
                               networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(userName,
                                  pwd)) {
            return;
        }
        params.put("username",
                   userName);
        params.put("password",
                   pwd);
        new OkHttpBuilder.POST(appCompatActivity).entityClass(AuthBean.class)
                                                 .params(params)
                                                 .urlLogin
                                                         ("login")
                                                 .enqueue
                                                         (networkParams,
                                                          tOkHttpResponseListener);
    }

    /**
     * get the phone verify code
     *
     * @param phone         phoneNumber
     * @param networkParams networkParams
     */
    public void phoneCode (@NonNull String phone,
                           NetworkParams
                                   networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(phone)) {
            return;
        }
        params.put("phone",
                   phone);
        new OkHttpBuilder.POST(appCompatActivity).entityClass(BaseBean.class)
                                                 .params(params)
                                                 .urlLogin("get_phone_code")
                                                 .enqueue
                                                         (networkParams,
                                                          tOkHttpResponseListener);
    }

    public void checkPhone (String phone,
                            NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(phone)) {
            return;
        }
        params.put("phone",
                   phone);
        new OkHttpBuilder.POST(appCompatActivity).entityClass(BaseBean.class)
                                                 .params(params)
                                                 .urlLogin("checkPhone")
                                                 .enqueue
                                                         (networkParams,
                                                          tOkHttpResponseListener);
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
    public void register (@NonNull String phone,
                          @NonNull String code,
                          @NonNull String password,
                          @NonNull String rePassword,
                          NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(phone,
                                  code,
                                  password,
                                  rePassword)) {
            return;
        }
        params.put("phone",
                   phone);
        params.put("code",
                   code);
        params.put("password",
                   password);
        params.put("repassword",
                   rePassword);
        new OkHttpBuilder.POST(appCompatActivity).entityClass(AuthBean.class)
                                                 .params(params)
                                                 .urlLogin
                                                         ("register")
                                                 .enqueue
                                                         (networkParams,
                                                          tOkHttpResponseListener);
    }

    /**
     * get the area data
     *
     * @param pid           parent id
     * @param networkParams networkParams
     */
    public void areaList (@NonNull String pid,
                          NetworkParams networkParams) {
        clearAllParams();
        params.put("auth",
                   getAuth());
        if (!pid.isEmpty()) {
            params.put("pid",
                       pid);
        }
        new OkHttpBuilder.POST(appCompatActivity).entityClass(AreaListBean.class)
                                                 .urlWorker
                                                         ("getAreaList")
                                                 .params
                                                         (params)
                                                 .enqueue
                                                         (networkParams,
                                                          tOkHttpResponseListener);
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
    public void saveWorkerInfo (@NonNull String nickName,
                                String thumbPath,
                                @NonNull String workerAreaIds,
                                @NonNull String workerAddress,
                                @NonNull String lat,
                                @NonNull String lng,
                                @NonNull String cardNo,
                                String cardFrontPath,
                                String cardBackPath,
                                NetworkParams networkParams) {

        Map<String, String> fileMaps = new HashMap<>();
        clearAllParams();
        if (isNecessaryFieldEmpty(nickName,
                                  workerAreaIds,
                                  workerAddress,
                                  lat,
                                  lat)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("nickname",
                   nickName);
        params.put("worker_area_ids",
                   workerAreaIds);
        params.put("worker_address",
                   workerAddress);
        params.put("lat",
                   lat);
        params.put("lng",
                   lng);
        params.put("card_no",
                   cardNo);

        // the three params do not necessary
        if (!thumbPath.isEmpty()) {
            fileMaps.put("thumb",
                         thumbPath);
        }
        if (!cardFrontPath.isEmpty()) {
            fileMaps.put("card_front",
                         cardFrontPath);
        }
        if (!cardBackPath.isEmpty()) {
            fileMaps.put("card_back",
                         cardBackPath);
        }

        new OkHttpBuilder.POST(appCompatActivity).urlWorker("saveWorkerInfo")
                                                 .params(params,
                                                         fileMaps)
                                                 .entityClass(BaseBean.class)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
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
    public void findPwd (@NonNull String phone,
                         @NonNull String code,
                         @NonNull String password,
                         @NonNull String rePassword,
                         NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(phone,
                                  code,
                                  password,
                                  rePassword)) {
            return;
        }
        params.put("phone",
                   phone);
        params.put("code",
                   code);
        params.put("password",
                   password);
        params.put("repassword",
                   rePassword);
        new OkHttpBuilder.POST(appCompatActivity).urlLogin("forgetPassword")
                                                 .params(params)
                                                 .entityClass
                                                         (BaseBean.class)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }


    /**
     * 附近工单列表
     *
     * @param lat
     * @param lng
     * @param radius
     * @param page
     */
    public void index (String lat, String lng, String radius, String page,
                       NetworkParams networkParams) {
        clearAllParams();
        params.put("auth",
                   getAuth());
        if (!TextUtils.isEmpty(lat)) {
            params.put("lat",
                       lat);
        }
        if (!TextUtils.isEmpty(lng)) {
            params.put("lng",
                       lng);
        }
        if (!TextUtils.isEmpty(radius)) {
            params.put("radius",
                       radius);
        }
        if (!TextUtils.isEmpty(page)) {
            params.put("page",
                       page);
        }

        new OkHttpBuilder.POST(appCompatActivity).setIsNeedLoadingDialog(false)
                                                 .entityClass(GrabBean.class)
                                                 .urlApiNearOrder("index")
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void snapupOrder (@NonNull String orderId, String lat, String lng, NetworkParams
            networkParams) {
        clearAllParams();
        params.put("auth",
                   getAuth());
        params.put("orderid",
                   orderId);
        params.put("lat",
                   lat);
        params.put("lng",
                   lng);
        new OkHttpBuilder.POST(appCompatActivity)
                .entityClass(GrabBean.class)
                .urlApiNearOrder("snapupOrder")
                .params(params)
                .enqueue(networkParams,
                         tOkHttpResponseListener);
    }

    public void isReadProtocol (NetworkParams networkParams) {
        clearAllParams();
        params.put("auth",
                   getAuth());
        new OkHttpBuilder.POST(appCompatActivity)
                .entityClass(GrabAgreementBean.class)
                .urlApiNearOrder("isReadProtocol")
                .params(params)
                .enqueue(networkParams,
                         tOkHttpResponseListener);
    }

    /**
     * @param type          type code (1 means newOrder , 2 means pending , 3 means waitingAccount)
     * @param page          page no
     * @param pageSize      data counts in each page
     * @param networkParams networkParams
     */
    public void myOrderList (@NonNull String type,
                             String page,
                             String pageSize,
                             NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(type)) {
            return;
        }
        if (!(TextUtils.isEmpty(page))) {
            params.put("page",
                       page);
        }

        if (!(TextUtils.isEmpty(pageSize))) {
            params.put("pageSize",
                       pageSize);
        }

        params.put("auth",
                   getAuth());
        params.put("type",
                   type);

        new OkHttpBuilder.POST(appCompatActivity).urlOrder("myOrderList")
                                                 .entityClass(MyOrderBean
                                                                      .class)
                                                 .params(params)
                                                 .setIsNeedLoadingDialog(false)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void resetUnread (String orderId, NetworkParams networkParams) {
        params.clear();
        params.put("auth",
                   getAuth());
        params.put("order_id",
                   orderId);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("resetUnread")
                                                 .entityClass(BaseBean.class)
                                                 .params(params)
                                                 .setIsNeedLoadingDialog(false)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);

    }

    /**
     * @param orderId       orderid
     * @param type          type need to special (0 means appointmentClient's detail,1 means
     *                      operation order's detail)
     * @param networkParams network params
     */
    public void orderDetail (@NonNull String orderId,
                             @NonNull String type,
                             NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(orderId)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("orderid",
                   orderId);
        if (!type.isEmpty()) {
            params.put("type",
                       type);
        }
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("orderDetail")
                                                 .entityClass(OrderDetailBean.class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void getServiceCode (@NonNull String orderId,
                                NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(orderId)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("orderid",
                   orderId);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("getServiceCode")
                                                 .entityClass
                                                         (BaseBean.class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void appointAgain (@NonNull String orderId,
                              @NonNull String appointTime,
                              NetworkParams
                                      networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(orderId,
                                  appointTime)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("orderid",
                   orderId);
        params.put("appoint_time",
                   appointTime);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("appointAgain")
                                                 .entityClass
                                                         (BaseBean.class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void orderSettlement (String orderId,
                                 NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(orderId)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("orderid",
                   orderId);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("orderSettlement")
                                                 .entityClass
                                                         (ExpenseDetailBean.class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);

    }

    public void completeOrder (String orderId,
                               String code,
                               NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(orderId,
                                  code)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("orderid",
                   orderId);
        params.put("code",
                   code);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("completeOrder")
                                                 .entityClass
                                                         (BaseBean.class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    /**
     * @param orderId         orderid
     * @param result          appointment result 1 means success, 2 means cannot contact client,3
     *                        means cant do the work,4 means cannot  fix the product
     * @param appointmentTime appointment time
     * @param remark          remark
     * @param networkParams   network params
     */
    public void appointOrder (@NonNull String orderId,
                              @NonNull String result,
                              @NonNull String
                                      appointmentTime,
                              @NonNull String remark,
                              NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(orderId,
                                  result)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("orderid",
                   orderId);
        params.put("result",
                   result);
        if (!appointmentTime.isEmpty()) {
            params.put("appoint_time",
                       appointmentTime);
        }
        if (!remark.isEmpty()) {
            params.put("remarks",
                       remark);
        }
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("appointOrder")
                                                 .entityClass(BaseBean
                                                                      .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    /**
     * sign the appointment
     *
     * @param orderId       the orderId of the order's
     * @param lat           the worker's lat
     * @param lng           the worker's lng
     * @param networkParams networkParams
     */
    public void signAppoint (@NonNull String orderId,
                             double lat,
                             double lng,
                             NetworkParams
                                     networkParams) {
        clearAllParams();

        if (lat == 0d || lng == 0d || isNecessaryFieldEmpty(orderId)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("orderid",
                   orderId);
        params.put("lat",
                   String.valueOf(lat));
        params.put("lng",
                   String.valueOf(lng));
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("signAppoint")
                                                 .entityClass(BaseBean
                                                                      .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
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
    public void changeAppoint (@NonNull String orderId,
                               @NonNull String appointTime,
                               @NonNull String
                                       updateReason,
                               String remarks,
                               NetworkParams
                                       networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(orderId,
                                  appointTime,
                                  updateReason)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("orderid",
                   orderId);
        params.put("appoint_time",
                   appointTime);
        params.put("update_reason",
                   updateReason);
        if (!remarks.isEmpty()) {
            params.put("remarks",
                       remarks);
        }
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("changeAppoint")
                                                 .entityClass(BaseBean
                                                                      .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    /**
     * choose service obj
     *
     * @param detailId      the detail id which protected need be applied service obj
     * @param serviceId     the service's id
     * @param networkParams network params
     */
    public void choiceFaultService (@NonNull String detailId,
                                    @NonNull String serviceId,
                                    NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(detailId,
                                  serviceId)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("detailid",
                   detailId);
        params.put("service_id",
                   serviceId);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("choiceFaultService")
                                                 .entityClass
                                                         (BaseBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
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
    public void submitReport (@NonNull String detailId,
                              @NonNull String isComplete,
                              @NonNull
                              List<String> thumbs,
                              String report,
                              NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(detailId,
                                  isComplete)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("detailid",
                   detailId);
        params.put("is_complete",
                   isComplete);
        if (!TextUtils.isEmpty(report)) {
            params.put("report",
                       report);
        }

        new OkHttpBuilder.POST(appCompatActivity).urlOrder("submitReport")
                                                 .entityClass(BaseBean
                                                                      .class)
                                                 .params(params,
                                                         thumbs,
                                                         "thumb[]")
                                                 .enqueue(networkParams,
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
    public void wrongProduct (@NonNull String detailId,
                              @NonNull String info,
                              @NonNull
                              List<String> filePaths,
                              NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(detailId,
                                  info) || isNecessaryListEmpty(filePaths)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("detailid",
                   detailId);
        params.put("info",
                   info);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("wrongProduct")
                                                 .entityClass(BaseBean
                                                                      .class)
                                                 .params(params,
                                                         filePaths,
                                                         "thumb[]")
                                                 .enqueue(
                                                         networkParams,
                                                         tOkHttpResponseListener);
    }

    /**
     * apply expenses
     *
     * @param detailId      the product's id
     * @param money         apply price
     * @param costType      apply type
     * @param remarks       remarks
     * @param filePaths     the pic's path
     * @param networkParams networkParams
     */
    public void applyCost (@NonNull String detailId,
                           @NonNull String money,
                           @NonNull String
                                   costType,
                           String remarks,
                           List<String> filePaths,
                           NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(detailId,
                                  money,
                                  costType)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("detailid",
                   detailId);
        params.put("money",
                   money);
        params.put("cost_type",
                   costType);
        if (!remarks.isEmpty()) {
            params.put("remarks",
                       remarks);
        }
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("applyCost")
                                                 .entityClass(BaseBean
                                                                      .class)
                                                 .params(params,
                                                         filePaths,
                                                         "thumb[]")
                                                 .enqueue(
                                                         networkParams,
                                                         tOkHttpResponseListener);
    }

    /**
     * get appointment history list
     *
     * @param orderId       the order's id
     * @param networkParams networkParams
     */
    public void appointList (@NonNull String orderId,
                             NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(orderId)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("orderid",
                   orderId);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("appointList")
                                                 .entityClass
                                                         (AppointmentHistoryItemBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
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
    public void returnOrder (@NonNull String orderId,
                             @NonNull String operation,
                             String desc,
                             NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(orderId,
                                  operation)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("orderid",
                   orderId);
        params.put("operation",
                   operation);
        if (TextUtils.isEmpty(desc)) {
            params.put("desc",
                       desc);
        }
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("returnOrder")
                                                 .entityClass
                                                         (BaseBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
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
    public void productAcceList (@NonNull String orderId,
                                 @NonNull String serviceId,
                                 String partSid,
                                 String keyWord,
                                 NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(orderId,
                                  serviceId)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("orderid",
                   orderId);
        params.put("serviceid",
                   serviceId);
        if (!TextUtils.isEmpty(partSid)) {
            params.put("partsid",
                       partSid);
        }
        if (!TextUtils.isEmpty(keyWord)) {
            params.put("keyword",
                       keyWord);
        }
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("productAcceList")
                                                 .entityClass
                                                         (FittingWareHouseBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    /**
     * get customer default address
     *
     * @param orderId       order's id
     * @param networkParams network params
     */
    public void getCustomerAddress (@NonNull String orderId,
                                    NetworkParams networkParams) {
        clearAllParams();
        params.put("auth",
                   getAuth());
        if (isNecessaryFieldEmpty(orderId)) {
            return;
        }
        params.put("orderid",
                   orderId);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("get_customer_address")
                                                 .entityClass
                                                         (CustomerAddressBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void getMechanicAddress (NetworkParams networkParams) {
        clearAllParams();
        params.put("auth",
                   getAuth());
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("get_address")
                                                 .entityClass
                                                         (CustomerAddressBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    /**
     * get factory default address
     *
     * @param orderId       order's id
     * @param networkParams network params
     */
    public void getFactoryAddress (@NonNull String orderId,
                                   NetworkParams networkParams) {
        clearAllParams();
        params.put("auth",
                   getAuth());
        if (isNecessaryFieldEmpty(orderId)) {
            return;
        }
        params.put("orderid",
                   orderId);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("get_factory_address")
                                                 .entityClass
                                                         (CustomerAddressBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void getExpressCom (@NonNull String expressNum,
                               NetworkParams networkParams) {
        clearAllParams();
        params.put("auth",
                   getAuth());
        if (isNecessaryFieldEmpty(expressNum)) {
            return;
        }
        params.put("expressNum",
                   expressNum);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("getExpressCom")
                                                 .entityClass
                                                         (ExpressComBean.class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
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
    public void requireAcce (@NonNull String detailId,
                             @NonNull List<String> acces,
                             @NonNull
                             List<String> otherAcces,
                             List<String> fileThumb,
                             String remarks,
                             @NonNull String applicant,
                             @NonNull String applicantTell,
                             @NonNull String areaIds,
                             @NonNull String
                                     address,
                             NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(detailId,
                                  applicant,
                                  applicantTell,
                                  areaIds,
                                  address)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("detailid",
                   detailId);
        params.put("applicant",
                   applicant);
        params.put("applicant_tell",
                   applicantTell);
        params.put("area_ids",
                   areaIds);
        params.put("address",
                   address);
        if (!TextUtils.isEmpty(remarks)) {
            params.put("remarks",
                       remarks);
        }
        List<List<String>> allStringParams = new ArrayList<>();
        allStringParams.add(acces);
        allStringParams.add(otherAcces);
        allStringParams.add(fileThumb);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("requireAcce")
                                                 .entityClass(BaseBean
                                                                      .class)
                                                 .params(params,
                                                         allStringParams,
                                                         "acces[]",
                                                         "other_acces[]",
                                                         "thumb[]")
                                                 .enqueue(networkParams,
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
    public void addOtherAcce (@NonNull String detailId,
                              @NonNull String name,
                              @NonNull String
                                      number,
                              String accCode,
                              @NonNull String remarks,
                              @NonNull List<String> thumbPaths,
                              NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(detailId,
                                  name,
                                  number,
                                  remarks)) {
            return;
        }

        params.put("auth",
                   getAuth());
        params.put("detailid",
                   detailId);
        params.put("name",
                   name);
        params.put("nums",
                   number);
        if (!TextUtils.isEmpty(accCode)) {
            params.put("acce_code",
                       accCode);
        }
        params.put("remarks",
                   remarks);

        new OkHttpBuilder.POST(appCompatActivity).urlOrder("addOtherAcce")
                                                 .entityClass
                                                         (OtherFittingBean
                                                                  .class)
                                                 .params(params,
                                                         thumbPaths,
                                                         "thumb[]")
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void applyAcce (@NonNull String detailId,
                           @NonNull List<String> acces,
                           List<String>
                                   otherAcces,
                           List<String> thumbs,
                           String remarks,
                           @NonNull String shippingType,
                           @NonNull String shippingNum,
                           @NonNull String shippingTypeCom,
                           @NonNull String shippingPayType,
                           @NonNull String shippingCost,
                           @NonNull String applicant,
                           @NonNull String applicantTell,
                           @NonNull String areaIds,
                           String address,
                           NetworkParams networkParams) {
        if (isNecessaryFieldEmpty(shippingType,
                                  shippingNum,
                                  shippingTypeCom,
                                  shippingPayType,
                                  shippingCost,
                                  applicant,
                                  applicantTell,
                                  areaIds,
                                  address)) {
            return;
        }
        clearAllParams();

        params.put("auth",
                   getAuth());
        params.put("detailid",
                   detailId);
        if (!remarks.isEmpty()) {
            params.put("remarks",
                       remarks);
        }
        params.put("shipping_type",
                   shippingType);
        params.put("shipping_num",
                   shippingNum);
        params.put("shipping_type_com",
                   shippingTypeCom);
        params.put("shipping_paytype",
                   shippingPayType);
        params.put("shipping_cost",
                   shippingCost);
        params.put("applicant",
                   applicant);
        params.put("applicant_tell",
                   applicantTell);
        params.put("area_ids",
                   areaIds);
        params.put("address",
                   address);

        fileLists.clear();
        fileLists.add(acces);
        fileLists.add(otherAcces);
        fileLists.add(thumbs);

        new OkHttpBuilder.POST(appCompatActivity).urlOrder("applyAcce")
                                                 .entityClass
                                                         (BaseBean
                                                                  .class)
                                                 .params(params,
                                                         fileLists,
                                                         "acces[]",
                                                         "other_acces[]",
                                                         "thumb[]")
                                                 .enqueue
                                                         (networkParams,
                                                          tOkHttpResponseListener);
    }

    public void orderTrace (String orderId,
                            NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(orderId)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("orderid",
                   orderId);
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("orderTrace")
                                                 .entityClass
                                                         (OrderTrackingBean.class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void acceList (@NonNull String status,
                          String page,
                          String
                                  pageSize,
                          NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(status)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("status",
                   status);
        if (!page.isEmpty()) {
            params.put("page",
                       page);
        }
        if (!pageSize.isEmpty()) {
            params.put("pageSize",
                       pageSize);
        }

        new OkHttpBuilder.POST(appCompatActivity).urlApiAccessOrder("acceList")
                                                 .entityClass
                                                         (MyFittingOrderBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void costList (@NonNull String status,
                          String page,
                          String pageSize,
                          NetworkParams
                                  networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(status)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("status",
                   status);
        if (!page.isEmpty()) {
            params.put("page",
                       page);
        }
        if (!pageSize.isEmpty()) {
            params.put("pageSize",
                       pageSize);
        }
        new OkHttpBuilder.POST(appCompatActivity).urlApiAccessOrder("costList")
                                                 .entityClass
                                                         (FittingCostBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void acceDetail (@NonNull String acceId,
                            NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(acceId)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("acceid",
                   acceId);
        new OkHttpBuilder.POST(appCompatActivity).urlApiAccessOrder("acceDetail")
                                                 .entityClass
                                                         (FittingDetailBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void remindFactSend (@NonNull String acceId,
                                NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(acceId)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("acceid",
                   acceId);
        new OkHttpBuilder.POST(appCompatActivity).urlApiAccessOrder("remindFactSend")
                                                 .entityClass
                                                         (BaseBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void signForAcce (@NonNull String acceId,
                             NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(acceId)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("acceid",
                   acceId);
        new OkHttpBuilder.POST(appCompatActivity).urlApiAccessOrder("signForAcce")
                                                 .entityClass
                                                         (BaseBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void returnAcce (@NonNull String acceId,
                            List<String> thumb,
                            String remarks,
                            @NonNull
                            String shippingType,
                            @NonNull String shippingNum,
                            @NonNull String shippingPaytype,
                            @NonNull
                            String shippingCost,
                            NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(acceId,
                                  shippingType,
                                  shippingNum,
                                  shippingPaytype,
                                  shippingCost)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("acceid",
                   acceId);
        params.put("shipping_type",
                   shippingType);
        params.put("shipping_num",
                   shippingNum);
        params.put("shipping_paytype",
                   shippingPaytype);
        params.put("shipping_cost",
                   shippingCost);
        if (!TextUtils.isEmpty(remarks)) {
            params.put("remarks",
                       acceId);
        }
        new OkHttpBuilder.POST(appCompatActivity).urlApiAccessOrder("returnAcce")
                                                 .entityClass
                                                         (BaseBean
                                                                  .class)
                                                 .params(params,
                                                         thumb,
                                                         "thumb[]")
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);

    }

    public void costDetail (@NonNull String costId,
                            NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(costId)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("costid",
                   costId);
        new OkHttpBuilder.POST(appCompatActivity).urlApiAccessOrder("costDetail")
                                                 .entityClass
                                                         (ExpensesResultBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void workerInfo (NetworkParams networkParams) {
        clearAllParams();
        params.put("auth",
                   getAuth());
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("workerInfo")
                                                 .entityClass
                                                         (VipInfoBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void wallet (NetworkParams networkParams) {
        clearAllParams();
        params.put("auth",
                   getAuth());
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("wallet")
                                                 .entityClass
                                                         (WalletBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void myCard (NetworkParams networkParams) {
        clearAllParams();
        params.put("auth",
                   getAuth());
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("myCard")
                                                 .entityClass
                                                         (CreditCardBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void addCard (@NonNull String creditCard,
                         @NonNull String bankId,
                         @NonNull
                         String cityId,
                         @NonNull String payPassword,
                         NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(creditCard,
                                  bankId,
                                  cityId,
                                  payPassword)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("credit_card",
                   creditCard);
        params.put("bank_id",
                   bankId);
        params.put("city_id",
                   cityId);
        params.put("pay_password",
                   payPassword);
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("addCard")
                                                 .entityClass
                                                         (BaseBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }


    public void bankList (NetworkParams networkParams) {
        clearAllParams();
        params.put("auth",
                   getAuth());
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("bankList")
                                                 .entityClass
                                                         (BankBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void orderHistory (NetworkParams networkParams) {
        clearAllParams();
        params.put("auth",
                   getAuth());
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("orderHistory")
                                                 .entityClass
                                                         (WorkHistoryBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void orderHintList (@NonNull String type,
                               NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(type)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("type",
                   type);
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("orderHintList")
                                                 .entityClass
                                                         (OrderHintBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void announcementList (@NonNull String type,
                                  NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(type)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("type",
                   type);
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("announcementList")
                                                 .entityClass
                                                         (AccountCementBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }


    public void setPayPassword (@NonNull String payPassword,
                                @NonNull String rePayPassword,
                                NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(payPassword)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("pay_password",
                   payPassword);
        params.put("re_password",
                   rePayPassword);
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("setPayPassword")
                                                 .entityClass
                                                         (BaseBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void checkPayPassword (@NonNull String payPassword,
                                  NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(payPassword)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("pay_password",
                   payPassword);
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("checkPayPassword")
                                                 .entityClass
                                                         (BaseBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void delCard (String payPassword,
                         NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(payPassword)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("pay_password",
                   payPassword);
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("delCard")
                                                 .entityClass
                                                         (BaseBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void getPhoneCode (@NonNull String phoneNum,
                              NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(phoneNum)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("phone",
                   phoneNum);
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("getPhoneCode")
                                                 .entityClass
                                                         (BaseBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void resetPayPassword (String phone,
                                  String code,
                                  String payPassword,
                                  String
                                          rePayPassword,
                                  NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(phone,
                                  code,
                                  payPassword,
                                  rePayPassword)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("phone",
                   phone);
        params.put("code",
                   code);
        params.put("pay_password",
                   payPassword);
        params.put("re_password",
                   rePayPassword);
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("resetPayPassword")
                                                 .entityClass
                                                         (BaseBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void incomeList (NetworkParams networkParams) {
        clearAllParams();
        params.put("auth",
                   getAuth());
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("incomeList")
                                                 .entityClass
                                                         (IncomeBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void withdrawalsList (NetworkParams networkParams) {
        clearAllParams();
        params.put("auth",
                   getAuth());
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("withdrawalsList")
                                                 .entityClass
                                                         (WithdrawBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void otherCostList (NetworkParams networkParams) {
        clearAllParams();
        params.put("auth",
                   getAuth());
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("otherCostList")
                                                 .entityClass
                                                         (OtherCostBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void withdrawals (@NonNull String outMoney,
                             @NonNull String payPassword,
                             NetworkParams
                                     networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(outMoney,
                                  payPassword)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("out_money",
                   outMoney);
        params.put("pay_password",
                   payPassword);
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("withdrawals")
                                                 .entityClass
                                                         (BaseBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void saveAddressee (@NonNull String addressee,
                               String phone,
                               String areaIds,
                               String
                                       detailAddress,
                               String postCodes,
                               NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(addressee,
                                  phone,
                                  areaIds,
                                  detailAddress,
                                  postCodes)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("addressee",
                   addressee);
        params.put("phone",
                   phone);
        params.put("area_ids",
                   areaIds);
        params.put("detail_address",
                   detailAddress);
        params.put("postcodes",
                   postCodes);
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("saveAddressee")
                                                 .entityClass
                                                         (BaseBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void editPassword (@NonNull String oldPwd,
                              @NonNull String newPwd,
                              @NonNull String
                                      reNewPwd,
                              NetworkParams
                                      networkParams) {

        clearAllParams();
        if (isNecessaryFieldEmpty(oldPwd,
                                  newPwd,
                                  reNewPwd)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("old_password",
                   oldPwd);
        params.put("new_password",
                   newPwd);
        params.put("re_password",
                   reNewPwd);
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("editPassword")
                                                 .entityClass
                                                         (BaseBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void submitFeedback (String content, String options,
                                NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(content)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("content",
                   content);
        params.put("type",
                   options);
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("submitFeedback")
                                                 .entityClass
                                                         (BaseBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }


    public void historyFeedback (NetworkParams networkParams) {
        params.clear();
        params.put("auth",
                   getAuth());
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("historyFeedback")
                                                 .entityClass
                                                         (FeedbackBean.class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void setDevice (@NonNull String registrationId,
                           NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(registrationId)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("registration_id",
                   registrationId);
        params.put("dev_type",
                   "2");
        params.put("dev_model",
                   MiscUtils.getModel(appCompatActivity));

        new OkHttpBuilder.POST(appCompatActivity).urlAPIPush("setDevice")
                                                 .entityClass
                                                         (BaseBean
                                                                  .class)
                                                 .params(params)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void orderMessageList (@NonNull String orderId,
                                  String messageId,
                                  NetworkParams
                                          networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(orderId)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("orderid",
                   orderId);
        if (!messageId.isEmpty()) {
            params.put("message_id",
                       messageId);
        }
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("orderMessageList")
                                                 .entityClass
                                                         (OrderMsgListBean
                                                                  .class)
                                                 .params(params)
                                                 .setIsNeedLoadingDialog(false)
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);
    }

    public void addOrderMessage (@NonNull String orderId,
                                 String messageId,
                                 String content,
                                 String
                                         type,
                                 String filePath,
                                 NetworkParams
                                         networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(orderId)) {
            return;
        }
        params.put("auth",
                   getAuth());
        params.put("orderid",
                   orderId);
        params.put("content",
                   content);
        if (!messageId.isEmpty()) {
            params.put("message_id",
                       messageId);
        }
        if (!type.isEmpty()) {
            params.put("type",
                       type);
        }
        Map<String, String> map = new HashMap<>();
        if (!filePath.isEmpty()) {
            map.put("thumb",
                    filePath);
        }
        new OkHttpBuilder.POST(appCompatActivity).urlOrder("addOrderMessage")
                                                 .entityClass
                                                         (OrderMsgBean
                                                                  .class)
                                                 .setIsNeedLoadingDialog(false)
                                                 .params(params,
                                                         map)
                                                 .enqueue
                                                         (networkParams,
                                                          tOkHttpResponseListener);
    }

    public void versions (@NonNull String version,
                          NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(version)) {
            return;
        }
        params.put("version",
                   version);
        new OkHttpBuilder.POST(appCompatActivity).urlPages("version")
                                                 .entityClass
                                                         (CheckUpdateBean.class)
                                                 .params(params)
                                                 .enqueue
                                                         (networkParams,
                                                          tOkHttpResponseListener);
    }


    public void editAvatar (@NonNull String photoPath,
                            NetworkParams networkParams) {
        clearAllParams();
        if (isNecessaryFieldEmpty(photoPath)) {
            return;
        }
        params.put("auth",
                   getAuth());
        List<String> list = new ArrayList<>();
        list.add(photoPath);
        new OkHttpBuilder.POST(appCompatActivity).urlAPIMember("editAvatar")
                                                 .entityClass
                                                         (BaseBean
                                                                  .class)
                                                 .params(params,
                                                         list,
                                                         "thumb")
                                                 .enqueue(networkParams,
                                                          tOkHttpResponseListener);

    }

    public void collect (String crash, String imei) {
        params.put("crash",
                   crash);
        params.put("tell_type",
                   MiscUtils.getModel(context));
        params.put("system",
                   MiscUtils.getSDKVer());
        params.put("addtime",
                   DataFormatter.getCurrentTime());
        params.put("imei",
                   imei);
        new OkHttpBuilder.POST(appCompatActivity).setIsNeedLoadingDialog(false)
                                                 .urlCorrect()
                                                 .entityClass
                                                         (BaseBean.class)
                                                 .params(params)
                                                 .enqueue(null,
                                                          tOkHttpResponseListener);
    }

    private boolean isNecessaryFieldEmpty (String... strings) {
        for (String string : strings) {
            LogTools.w(string);
            if (TextUtils.isEmpty(string)) {
                logError();
                return true;
            }
        }
        return false;
    }

    private boolean isNecessaryListEmpty (List<String>... lists) {
        for (List<String> list : lists) {
            if (list.isEmpty()) {
                logError();
                return true;
            }
        }
        return false;
    }

    private void logError () {
        LogTools.e("necessary field is empty");
    }


    private void clearAllParams () {
        params.clear();
    }

    /**
     * @return the user's auth
     */
    private String getAuth () {
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
