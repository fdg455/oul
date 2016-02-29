package com.oushangfeng.ounews.common;

/**
 * ClassName: DataLoadType<p>
 * Author: oubowu<p>
 * Fuction: 数据加载结果的类型<p>
 * CreateDate: 2016/2/21 17:33<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public enum DataLoadType {

    /**
     * 刷新成功
     */
    TYPE_REFRESH_SUCCESS,

    /**
     * 属性失败
     */
    TYPE_REFRESH_FAIL,

    /**
     * 加载更多成功
     */
    TYPE_LOAD_MORE_SUCCESS,

    /**
     * 加载更多失败
     */
    TYPE_LOAD_MORE_FAIL

}
