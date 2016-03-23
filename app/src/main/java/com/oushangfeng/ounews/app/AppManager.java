package com.oushangfeng.ounews.app;

import android.util.SparseArray;

import com.socks.library.KLog;

/**
 * ClassName: AppManager<p>
 * Author: oubowu<p>
 * Fuction:  管理主页面几个导航Activity的排序，由于跳转导航的Activity时设置了此标志位FLAG_ACTIVITY_REORDER_TO_FRONT，
 * 跳转的Activity若在栈中有实例会顶到栈顶，NewsActivity作为整个app结束的页面，需要这里管理导航的Activity的排序，
 * 按返回键的时候不是结束导航Activity而是跳转上一个导航Activity<p>
 * CreateDate: 2016/2/17 14:26<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class AppManager {

    private static volatile AppManager sInstance;

    /**
     * 记录Activity的名称，我的思路是Activity用RxBus实现退出，这里用String记录Activity栈的情况而已
     */
    private SparseArray<String> mNavActivityOrder;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (sInstance == null) {
            synchronized (AppManager.class) {
                if (sInstance == null) {
                    sInstance = new AppManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取上一个导航Activity的类
     *
     * @return 上一个导航Activity的类
     */
    public Class getLastNavActivity() throws ClassNotFoundException {
        if (mNavActivityOrder == null || mNavActivityOrder.size() == 0 || mNavActivityOrder
                .size() == 1) {
            return null;
        }
        return Class.forName(mNavActivityOrder.get(mNavActivityOrder.size() - 2));
    }

    /**
     * 获得当前显示的Activity的类
     *
     * @return 当前显示的Activity的类
     * @throws ClassNotFoundException
     */
    public Class getCurrentNavActivity() throws ClassNotFoundException {
        if (mNavActivityOrder == null || mNavActivityOrder.size() == 0) {
            return null;
        }
        return Class.forName(mNavActivityOrder.get(mNavActivityOrder.size() - 1));
    }

    /**
     * 排序导航Activity，有跳转和返回键两种情况
     *
     * @param className 类名
     * @param backPress 是否为返回键的情况
     */
    public void orderNavActivity(String className, boolean backPress) {
        if (mNavActivityOrder == null) {
            mNavActivityOrder = new SparseArray<>();
        }
        // 取到类的索引
        final int index = mNavActivityOrder.indexOfValue(className);
        if (!backPress) {
            // 不是返回键的情况，即点击侧栏跳转的情况
            if (index == -1) {
                // 还没有过数据的时候，保存当前的类型
                mNavActivityOrder.put(mNavActivityOrder.size(), className);
            } else {
                // 有过数据的时候，将该类名索引到最高位
                for (int i = index + 1; i < mNavActivityOrder.size(); i++) {
                    mNavActivityOrder.put(i - 1, mNavActivityOrder.valueAt(i));
                }
                mNavActivityOrder.put(mNavActivityOrder.size() - 1, className);
            }
        } else {
            // 返回键的情况，将位置最高的类名放到最低位
            className = mNavActivityOrder.valueAt(mNavActivityOrder.size() - 1);
            for (int i = index; i >= 0; i--) {
                mNavActivityOrder.put(i + 1, mNavActivityOrder.valueAt(i));
            }
            mNavActivityOrder.put(0, className);
        }

        printOrder();
    }

    private void printOrder() {
        KLog.e("打印: " + mNavActivityOrder.size());
        for (int i = 0; i < mNavActivityOrder.size(); i++) {
            KLog.e("打印：" + mNavActivityOrder.get(i));
        }
        KLog.e("打印结束");
    }

}
