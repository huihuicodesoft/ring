package cn.com.wh.ring.utils;

import com.google.gson.Gson;

/**
 * Created by Hui on 2017/12/4.
 */

public class GsonUtils {
    private static final Gson gson = new Gson();

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static String toJson(Object src) {
        return gson.toJson(src);
    }
}
