package com.oushangfeng.ounews.module.news.presenter;

import com.oushangfeng.ounews.base.BasePresenter;

/**
 * ClassName: INewsChannelPresenter<p>
 * Author: oubowu<p>
 * Fuction: 新闻频道管理代理接口<p>
 * CreateDate: 2016/2/20 13:59<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface INewsChannelPresenter extends BasePresenter {

    void onItemAddOrRemove(String channelName, boolean selectState);

    void onItemSwap(int fromPos, int toPos);

}
