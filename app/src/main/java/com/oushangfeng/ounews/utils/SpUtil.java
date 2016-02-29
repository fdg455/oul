package com.oushangfeng.ounews.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.oushangfeng.ounews.app.App;
import com.oushangfeng.ounews.common.Constant;

/**
 * ClassName：SpUtil<p>
 * Author：Oubowu<p>
 * Fuction：SharedPreferences工具<p>
 * CreateDate：2015/7/9 16:27<p>
 * UpdateAuthor：<p>
 * UpdateDate：<p>
 */
public class SpUtil {

    public static String readString(String key) {
        return getSharedPreferences().getString(key, "");
    }

    public static void writeString(String key, String value) {
        getSharedPreferences().edit().putString(key, value).apply();
    }

    public static boolean readBoolean(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }

    public static void writeBoolean(String key, boolean value) {
        getSharedPreferences().edit().putBoolean(key, value).apply();
    }

    public static int readInt(String key) {
        return getSharedPreferences().getInt(key, 0);
    }

    public static int readInt(String key, int value) {
        return getSharedPreferences().getInt(key, value);
    }

    public static void writeInt(String key, int value) {
        getSharedPreferences().edit().putInt(key, value).apply();
    }

    public static long readLong(String key) {
        return getSharedPreferences().getLong(key, 0);
    }

    public static void writeLong(String key, long value) {
        getSharedPreferences().edit().putLong(key, value).apply();
    }

    public static void remove(String key) {
        getSharedPreferences().edit().remove(key).apply();
    }

    public static void removeAll() {
        getSharedPreferences().edit().clear().apply();
    }

    public static SharedPreferences getSharedPreferences() {
        return App.getContext()
                .getSharedPreferences(Constant.APP_NAME, Context.MODE_PRIVATE);
    }
}
