package com.szbb.pro.tools;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.szbb.pro.eum.JsonType;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.OkHttpResponseListener;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by ChanZeeBm on 2015/12/3.
 *
 * @param <T>
 */
public class OkHttpUtil<T> implements Callback {
    //okhttpClient
    private static OkHttpClient okHttpClient = new OkHttpClient();
    //数据回来后的回调
    private OkHttpResponseListener<T> okHttpResponseListener;
    //Entity Class
    private Class<T> clz;
    private Call call;

    //判断码
    private final static int OBJECT = 0x001;
    private final static int ARRAY = 0x002;
    private final static int ERROR = 0x003;

    private NetworkParams networkParams = NetworkParams.CUPCAKE;

    static {
        //初始化超时时间 30s
        okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
    }

    /**
     * @param url 获取数据的URl
     * @return 返回this
     */
    public OkHttpUtil GET (String url, NetworkParams networkParams) {
        Request request = new Request.Builder().url(url).get().build();
        this.networkParams = networkParams;
        call = okHttpClient.newCall(request);
        call.enqueue(this);
        return this;
    }

    /**
     * @param url         获取数据的URl
     * @param requestBody 构建体
     * @return this
     */
    public OkHttpUtil POST (String url, RequestBody requestBody, NetworkParams networkParams) {
        Request request = new Request.Builder().url(url).post(requestBody).build();
        this.networkParams = networkParams;
        call = okHttpClient.newCall(request);
        call.enqueue(this);
        return this;
    }

    /**
     * @param request okhttp的失败回调
     * @param e       错误
     */
    @Override
    public void onFailure (Request request, IOException e) {
        AppTools.dismissLoadingDialog();
        e.printStackTrace();
        if (okHttpResponseListener != null) {
            sendErrorMessage(request.toString());
        } else {
            cancelCall();
        }
    }

    private void cancelCall () {if (call != null) { call.cancel(); }}

    /**
     * @param response 返回的数据
     * @throws IOException IO错误
     */
    @Override
    public void onResponse (Response response) throws IOException {
        AppTools.dismissLoadingDialog();
        //如果数据获取成功
        if (response.isSuccessful()) {
            //获取返回的字符串
            String jsonStr = response.body().string();
            LogTools.w(jsonStr);
            LogTools.json(jsonStr);
            if (TextUtils.equals("<\\/html>", jsonStr)) {
                sendErrorMessage(response.toString());
                return;
            }

            //解析json
            parseJson(jsonStr);
        } else {
            if (okHttpResponseListener != null) {
                sendErrorMessage(response.toString());
            }
            cancelCall();
        }
    }

    /**
     * 解析数据
     *
     * @param jsonStr json字符串
     */
    private void parseJson (String jsonStr) {
        //新建Message
        Message msg = Message.obtain();
        //如果设置了回调
        if (okHttpResponseListener != null) {
            //判断类型
            JsonType type = type(jsonStr);
            if (type != null) {
                switch (type) {
                    case JSON_OBJECT:
                        try {
                            T t = new Gson().fromJson(jsonStr, clz);
                            msg.obj = t;
                            msg.what = OBJECT;
                            handler.sendMessage(msg);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        break;
                    case JSON_ARRAY:
                        try {
                            List<T> tList = new Gson().fromJson(jsonStr, new TypeToken<List<T>>() {
                            }.getType());
                            msg.obj = tList;
                            msg.what = ARRAY;
                            handler.sendMessage(msg);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }

                        break;
                    default:
                        cancelCall();
                        throw new IllegalArgumentException("response not jsonObject either " +
                                                           "JsonArray");
                }
            }
        }
    }


    /**
     * @param clz 设置Entity Class
     * @return this
     */
    public OkHttpUtil setClz (Class clz) {
        this.clz = (Class<T>) clz;
        return this;
    }

    private JsonType type (String response) {
        //获取第一个字符
        String charFirst = response.substring(0, 1);
        //如果是"{"则返回的是JsonObject
        if (TextUtils.equals("{", charFirst)) {
            return JsonType.JSON_OBJECT;
            //否则是JsonArray
        } else if (TextUtils.equals("[", charFirst)) {
            return JsonType.JSON_ARRAY;
        }
        return null;
    }

    //创建handler
    private Handler handler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            //判断msg.what
            switch (msg.what) {
                //如果是JsonObject 则回调onJsonObjectResponse
                case OBJECT:
                    T t = (T) msg.obj;
                    okHttpResponseListener.onJsonObjectResponse(t, networkParams);
                    break;
                //如果是JsonArray 则回调onJsonArrayResponse
                case ARRAY:
                    List<T> list = (List<T>) msg.obj;
                    okHttpResponseListener.onJsonArrayResponse(list, networkParams);
                    break;
                //否则回调错误处理方法
                case ERROR:
                    cancelCall();
                    okHttpResponseListener.onError((String) msg.obj, networkParams);
                    break;
            }
        }
    };


    /**
     * 发送错误代码
     *
     * @param error 错误代码
     */
    private void sendErrorMessage (String error) {
        Message message = Message.obtain();
        message.obj = "ErrorCode:" + error;
        message.what = ERROR;
        handler.sendMessage(message);
    }


    /**
     * 设置回调
     *
     * @param okHttpResponseListener 回调
     */
    public void setOkHttpResponseListener (OkHttpResponseListener<T> okHttpResponseListener) {
        this.okHttpResponseListener = okHttpResponseListener;
    }
}
