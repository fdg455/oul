package com.oushangfeng.ounews.module.news.model;

import com.oushangfeng.ounews.callback.RequestCallback;

import rx.Subscription;

/**
 * ClassName: INewsChannelInteractor<p>
 * Author: oubowu<p>
 * Fuction: 新闻管理的model层接口<p>
 * CreateDate: 2016/2/20 14:03<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface INewsChannelInteractor<T> {

    /**
     * 初始化查询或增删频道或排序，更新数据库
     *
     * @param callback
     * @param channelName
     * @param selectState null时为初始化查询，true为选中插入数据库，false为移除出数据库
     * @return
     */
    Subscription channelDbOperate(RequestCallback<T> callback, String channelName, Boolean selectState);

    /**
     * 拖拽时候更新数据库
     *
     * @param callback
     * @param fromPos
     * @param toPos
     * @return
     */
    Subscription channelDbSwap(RequestCallback callback, int fromPos, int toPos);
}
