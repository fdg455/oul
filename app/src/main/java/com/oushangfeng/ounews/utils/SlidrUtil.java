package com.oushangfeng.ounews.utils;

import android.app.Activity;
import android.support.v4.content.ContextCompat;

import com.oushangfeng.ounews.R;
import com.oushangfeng.ounews.utils.slidr.Slidr;
import com.oushangfeng.ounews.utils.slidr.model.SlidrConfig;
import com.oushangfeng.ounews.utils.slidr.model.SlidrInterface;
import com.oushangfeng.ounews.utils.slidr.model.SlidrPosition;

/**
 * ClassName：SlidrUtil<p>
 * Author：Oubowu<p>
 * Fuction：滑动返回默认配置工具类<p>
 * CreateDate：2015/7/9 16:27<p>
 * UpdateAuthor：<p>
 * UpdateDate：<p>
 */
public class SlidrUtil {

    /**
     * 滑动返回配置工具，只需配置状态栏颜色和toolbar颜色即可,
     *
     * @param activity       绑定的Activity
     * @param statusBarColor 状态栏颜色
     * @param toolBarColor   toolbar颜色
     * @param enableEdge 是否开启边缘侧滑
     * @return SlidrInterface 可以用于SlidrInterface.lock()取消滑动返回,SlidrInterface.unlock()恢复滑动返回
     */
    public static SlidrInterface initSlidrDefaultConfig(Activity activity, int statusBarColor, int toolBarColor, boolean enableEdge) {

        SlidrPosition position = SlidrPosition.LEFT;
        SlidrConfig config = new SlidrConfig.Builder().primaryColor(statusBarColor)
                .secondaryColor(toolBarColor).position(position).velocityThreshold(100f)// 速度阀值
                .distanceThreshold(0.3f)//划出多少退出
                        //.touchSize(100f)//该库没用到此参数
                .edge(enableEdge)//从边划出
                .edgeSize(1f).sensitivity(1f).build();

        return Slidr.attach(activity, config);
    }

    /**
     * 滑动返回默认配置工具
     *
     * @param activity  绑定的Activity
     * @param enableEdge 是否开启边缘侧滑
     * @return SlidrInterface 可以用于SlidrInterface.lock()取消滑动返回,SlidrInterface.unlock()恢复滑动返回
     */
    public static SlidrInterface initSlidrDefaultConfig(Activity activity, boolean enableEdge) {

        SlidrPosition position = SlidrPosition.LEFT;
        SlidrConfig config = new SlidrConfig.Builder()
                .primaryColor(ContextCompat.getColor(activity, R.color.primary))
                .secondaryColor(ContextCompat.getColor(activity, R.color.primary_dark))
                .position(position).velocityThreshold(100f)// 速度阀值
                .distanceThreshold(0.3f)//划出多少退出
                        //.touchSize(100f)//该库没用到此参数
                .edge(enableEdge)//从边划出
                .edgeSize(1f).sensitivity(1f).build();

        return Slidr.attach(activity, config);
    }


}
