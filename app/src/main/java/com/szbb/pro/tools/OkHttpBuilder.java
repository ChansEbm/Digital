package com.szbb.pro.tools;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;
import com.szbb.pro.AppKeyMap;
import com.szbb.pro.R;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.OkHttpResponseListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ChanZeeBm on 12/3/2015.
 */
public class OkHttpBuilder {

    /**
     * attach the params into full url
     *
     * @param fullUrl the latest url in use
     * @param params  the params what we need to push
     * @return the fullUrl with params
     */
    public static String attachHttpGetParam(String fullUrl, Map<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder(fullUrl);
        for (String key : params.keySet()) {
            stringBuilder.append("&")
                         .append(key)
                         .append("=")
                         .append(params.get(key
                                           ));
        }
        return stringBuilder.toString();
    }

    /**
     * get content type
     *
     * @param mediaType the type need to convert
     * @return {@code MediaType}
     */
    private static MediaType getContentType(com.szbb.pro.eum.MediaType mediaType) {
        switch (mediaType) {
            case JPG:
                return MediaType.parse(AppKeyMap.CONTENT_JPG);
            case MP3:
                return MediaType.parse(AppKeyMap.CONTENT_MP3);
            case OCT:
                return MediaType.parse(AppKeyMap.CONTENT_OCT);
            case PNG:
                return MediaType.parse(AppKeyMap.CONTENT_PNG);
            case TXT:
                return MediaType.parse(AppKeyMap.CONTENT_TXT);
            default:
                return MediaType.parse(AppKeyMap.CONTENT_OCT);
        }
    }

    /**
     * Get Action
     */
    public static class GET {
        //url
        private String fullUrl = "";
        //params
        private Map<String, String> params = null;
        //entityClass
        private Class entityClass;
        private AppCompatActivity appCompatActivity;

        public GET(AppCompatActivity appCompatActivity) {
            this.appCompatActivity = appCompatActivity;
        }

        /**
         * 设置要访问的URL
         *
         * @param url 接口url
         * @return this
         */
        public GET url(String url) {
            this.fullUrl = AppKeyMap.HEAD_API_LOGIN + url;
            return this;
        }


        /**
         * 添加请求的参数(only string)
         *
         * @param params 参数
         * @return this
         */
        public GET params(Map<String, String> params) {
            this.params = params;
            return this;
        }

        /**
         * set entity class
         *
         * @param entityClass the entity class need to set
         * @return this
         */
        public GET entityClass(Class entityClass) {
            this.entityClass = entityClass;
            return this;
        }


        /**
         * enqueue the result by OkhttpUtil get() method
         *
         * @param rootView               the parent view
         * @param okHttpResponseListener the result callback
         * @param networkParams          network params
         */
        public void enqueue(View rootView, OkHttpResponseListener
                okHttpResponseListener, NetworkParams networkParams) {
            if (!AppTools.isNetworkConnected()) {
                AppTools.showSettingSnackBar(rootView,
                                             appCompatActivity.getResources()
                                                              .getString
                                                                      (R.string.no_network_is_detected));
                return;
            }

            //show dialog
            AppTools.showLoadingDialog(appCompatActivity);
            //judgement url is empty or not
            if (TextUtils.isEmpty(fullUrl) || entityClass == null) {
                throw new NullPointerException("url is unavailable or not invoke entity");
            }
            if (params != null) {
                // attach the params in url
                fullUrl = attachHttpGetParam(fullUrl,
                                             params);
            }
            new OkHttpUtil().setClz(entityClass)
                            .GET(fullUrl,
                                 networkParams)
                            .setOkHttpResponseListener
                                    (okHttpResponseListener);
        }

    }

    /**
     * Post Action
     */
    public static class POST {
        //fullUrl
        private String fullUrl = "";
        // params
        private Map<String, String> params = null;
        //entity class
        private Class entityClass;
        //requestBody
        private RequestBody requestBody = null;
        //mediaType
        private MediaType mediaType = getContentType(com.szbb.pro.eum.MediaType.OCT);

        private AppCompatActivity appCompatActivity;

        private boolean isNeedLoadingDialog = true;//是否需要加载框(临时的)

        public POST(@NonNull AppCompatActivity appCompatActivity) {
            this.appCompatActivity = appCompatActivity;
        }

        /**
         * the url which we need to browse
         *
         * @param loginUrl
         * @return this
         */
        public POST urlLogin(String loginUrl) {
            this.fullUrl = AppKeyMap.HEAD_API_LOGIN + loginUrl;
            return this;
        }


        public POST urlWorker(String workerUrl) {
            this.fullUrl = AppKeyMap.HEAD_API_WORKER + workerUrl;
            return this;
        }

        public POST urlOrder(String orderUrl) {
            this.fullUrl = AppKeyMap.HEAD_API_ORDER + orderUrl;
            return this;
        }

        public POST urlApiAccessOrder(String apiAccessOrderUrl) {
            this.fullUrl = AppKeyMap.HEAD_API_ACCE_ORDER + apiAccessOrderUrl;
            return this;
        }



        public POST urlApiNearOrder(String nearOrderUrl) {
            this.fullUrl = AppKeyMap.HEAD_API_NEAR_ORDER + nearOrderUrl;
            return this;
        }

        public POST urlAPIMember(String memberUrl) {
            this.fullUrl = AppKeyMap.HEAD_API_MEMBER + memberUrl;
            return this;
        }

        public POST urlAPIPush(String pushUrl) {
            this.fullUrl = AppKeyMap.HEAD_API_PUSH + pushUrl;
            return this;
        }


        public POST urlPages(String pagesUrl) {
            this.fullUrl = AppKeyMap.HEAD_API_PAGES + pagesUrl;
            return this;
        }

        public POST urlCorrect() {
            this.fullUrl = AppKeyMap.HEAD_API_BUG_COLLECT;
            return this;
        }

        /**
         * the params what we need to upload
         *
         * @param params params
         * @return this
         */
        public POST params(@NonNull Map<String, String> params) {
            //FormEncodingBuilder
            FormEncodingBuilder builder = new FormEncodingBuilder();
            Set<String> keys = params.keySet();//把参数添加到构建体
            //ergodic the params and put them into builder
            for (String key : keys) {
                builder.add(key,
                            params.get(key));
            }
            //build a builder
            requestBody = builder.build();
            return this;
        }

        /**
         * 只有一个key 并且该key可以对应多个文件的上传方式
         *
         * @param params  参数
         * @param files   要添加的文件路径
         * @param fileKey 跟后台约定的文件List的key
         * @return this
         */
        public POST params(@NonNull Map<String, String> params, @NonNull List<String> files,
                           @NonNull String fileKey) {
            List<List<String>> lists = new ArrayList<>();
            lists.add(files);
            params(params,
                   lists,
                   fileKey);
            return this;
        }


        /**
         * 允许不同key 并且每个key可能对应多个文件的上传方式,也可以上传字符串数组
         *
         * @param params   普通参数
         * @param files    不同文件类型的List
         * @param fileKeys 文件类型的key
         * @return this
         */
        public POST params(@NonNull Map<String, String> params, @NonNull List<List<String>> files,
                           @NonNull String...
                                   fileKeys) {
            //multiBuilder
            MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
            //get the keys what in the params
            Set<String> keys = params.keySet();
            //the default mediaType
            mediaType = MediaType.parse(AppKeyMap.CONTENT_OCT);
            //put the standard params into builder
            for (String key : keys) {
                builder.addFormDataPart(key,
                                        params.get(key));
            }
            for (int i = 0;
                 i < fileKeys.length;
                 i++) {//循环key
                List<String> list = files.get(i);//根据key获得要添加的文件
                for (String filePath : list) {//遍历文件
                    //get the lastIndex in path
                    int suffixIndex = filePath.lastIndexOf(".");
                    //get the suffix
                    if (suffixIndex != -1) {//当有后缀的时候,识别为一个文件
                        String fileSuffix = filePath.substring(suffixIndex,
                                                               filePath.length());
                        builder.addFormDataPart(fileKeys[i],
                                                "file_" + fileSuffix,
                                                RequestBody
                                                        .create(mediaType,
                                                                new File(filePath)));
                        //以添加的key为后台key,批量添加文件到后台
                    } else {//如果没后缀,则当字符串数组上传
                        builder.addFormDataPart(fileKeys[i],
                                                filePath);
                    }
                }
            }
            //build a builder
            requestBody = builder.build();
            return this;
        }

        /**
         * 允许添加不同key并且每个key只对应单文件的上传方式
         *
         * @param params params
         * @param files  files
         * @return this
         */
        public POST params(@NonNull Map<String, String> params, @NonNull Map<String, String>
                files) {
            //multiBuilder
            MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
            //get the keys what in the params
            Set<String> keys = params.keySet();
            //the default mediaType
            mediaType = MediaType.parse(AppKeyMap.CONTENT_OCT);
            //put the standard params into builder
            for (String key : keys) {
                builder.addFormDataPart(key,
                                        params.get(key));
            }
            for (String key : files.keySet()) {
                String filePath = files.get(key);
                int suffixIndex = filePath.lastIndexOf(".");
                if (suffixIndex != -1) {
                    String fileSuffix = filePath.substring(suffixIndex,
                                                           filePath.length());
                    builder.addFormDataPart(key,
                                            "file_" + fileSuffix,
                                            RequestBody
                                                    .create(mediaType,
                                                            new File(filePath)));
                }
            }

            //build a builder
            requestBody = builder.build();
            return this;
        }


        /**
         * set an entity class
         *
         * @param entityClass entityClass
         * @return this
         */
        public POST entityClass(Class entityClass) {
            this.entityClass = entityClass;
            return this;
        }

        /**
         * enqueue the result use OkhttpUtil post() method
         *
         * @param okHttpResponseListener the resultCallback
         * @param networkParams          network params
         */
        public void enqueue(NetworkParams networkParams, OkHttpResponseListener
                okHttpResponseListener) {
            if (!AppTools.isNetworkConnected()) {
                if (appCompatActivity != null) {
                    AppTools.showSettingSnackBar(appCompatActivity.getWindow()
                                                                  .getDecorView(),
                                                 appCompatActivity.getString
                                                         (R.string
                                                                  .no_network_is_detected));
                }
                AppTools.sendBroadcast(null,
                                       AppKeyMap.NO_NETWORK_ACTION);
                return;
            }
            if (isNeedLoadingDialog && appCompatActivity != null)
            //show the dialog
            {
                AppTools.showLoadingDialog(appCompatActivity);
            }
            //fullUrl,requestBody and entityClass none even one of them will be null
            if (TextUtils.isEmpty(fullUrl) || requestBody == null || entityClass == null) {
                throw new NullPointerException("url or map is unavailable, or not invoke entity");
            }
            //equeue the result
            new OkHttpUtil().setClz(entityClass)
                            .POST(fullUrl,
                                  requestBody,
                                  networkParams)
                            .setOkHttpResponseListener
                                    (okHttpResponseListener);
        }


        /**
         * init the mediaType
         *
         * @param contentMediaType contentMediaType
         * @return this
         */
        public POST mediaType(com.szbb.pro.eum.MediaType contentMediaType) {
            mediaType = getContentType(contentMediaType);
            return this;
        }

        public POST setIsNeedLoadingDialog(boolean isNeedLoadingDialog) {
            this.isNeedLoadingDialog = isNeedLoadingDialog;
            return this;
        }
    }

}
