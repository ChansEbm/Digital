package com.szbb.pro.tools;

import android.content.Context;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ChanZeeBm on 2015/10/16.
 */
public class ParserTools {
    /**
     * 得到assets文件文本
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(context.getResources()
                    .getAssets().open(fileName));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从jsonarray解析jsonObject
     *
     * @param jsonArray
     * @return
     */
    public static List<JSONObject> parseJson(@NonNull JSONArray jsonArray) {
        List<JSONObject> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.optJSONObject(i));
        }
        return list;
    }

    /**
     * 从jsonObject解析数个jsonObject
     *
     * @param jsonObject
     * @return
     */
    public static List<JSONObject> parseJson(@NonNull JSONObject jsonObject) {
        List<JSONObject> list = new ArrayList<>();
        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            list.add(jsonObject.optJSONObject(iterator.next()));
        }
        return list;
    }

    private static JSONObject jsonObject;

    /**
     * 初始化jsonObject
     *
     * @param jsonObject
     */
    public static void initializeJsonObject(@NonNull JSONObject jsonObject) {
        ParserTools.jsonObject = jsonObject;
    }

    /**
     * 取值(String)
     *
     * @param key
     * @return
     */
    public static String optStringValue(@NonNull String key) {
        if (jsonObject == null) {
            throw new NullPointerException("please initialize jsonObject first");
        }
        return jsonObject.optString(key);
    }
}
