package com.oushangfeng.ounews.http;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * ClassName: HostType<p>
 * Author:oubowu<p>
 * Fuction: 请求数据host的类型<p>
 * CreateDate:2016/2/13 23:50<p>
 * UpdateUser:<p>
 * UpdateDate:<p>
 */
public class HostType {

    /**
     * 多少种Host类型
     */
    public static final int TYPE_COUNT = 3;

    /**
     * 网易新闻视频的host
     */
    @HostTypeChecker
    public static final int NETEASE_NEWS_VIDEO = 1;

    /**
     * 新浪图片的host
     */
    @HostTypeChecker
    public static final int SINA_NEWS_PHOTO = 2;

    /**
     * 天气查询的host
     */
    @HostTypeChecker
    public static final int WEATHER_INFO = 3;

    /**
     * 替代枚举的方案，使用IntDef保证类型安全
     */
    @IntDef({NETEASE_NEWS_VIDEO, SINA_NEWS_PHOTO, WEATHER_INFO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HostTypeChecker {

    }

}
