package com.oushangfeng.ounews.utils;

import android.os.SystemClock;
import android.util.Log;

/**
 * ClassName: ClickUtils<p>
 * Author:oubowu<p>
 * Fuction: 快速点击处理类<p>
 * CreateDate:2016/2/14 13:15<p>
 * UpdateUser:<p>
 * UpdateDate:<p>
 */
public class ClickUtils {

    private static final String TAG = ClickUtils.class.getSimpleName();
    private static long lastClickTime = 0L;
    private static final boolean isDebug = true;
    private static final String BLANK_LOG = "\t";
    /**
     * 用于处理频繁点击问题, 如果两次点击小于500毫秒则不予以响应
     *
     * @return true:是连续的快速点击
     */
    public static boolean isFastDoubleClick() {
        long nowTime = SystemClock.elapsedRealtime();//从开机到现在的毫秒数（手机睡眠(sleep)的时间也包括在内）

        if (isDebug){
            Log.d(TAG,"nowTime:" + nowTime);
            Log.d(TAG, "lastClickTime:" + lastClickTime);
            Log.d(TAG,"时间间隔:"+(nowTime - lastClickTime));
        }
        if ((nowTime - lastClickTime) < 250) {

            if (isDebug){
                Log.d(TAG,"快速点击");
                Log.d(TAG, BLANK_LOG);
            }
            return true;
        } else {
            lastClickTime = nowTime;

            if (isDebug){
                Log.d(TAG,"lastClickTime:" + lastClickTime);
                Log.d(TAG,"不是快速点击");
                Log.d(TAG,BLANK_LOG);
            }
            return false;
        }
    }
}
