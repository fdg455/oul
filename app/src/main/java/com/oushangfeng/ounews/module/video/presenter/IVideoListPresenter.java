package com.oushangfeng.ounews.module.video.presenter;

import com.oushangfeng.ounews.base.BasePresenter;

/**
 * ClassName: IVideoListPresenter<p>
 * Author: oubowu<p>
 * Fuction: 视频列表代理接口<p>
 * CreateDate: 2016/2/23 17:09<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface IVideoListPresenter extends BasePresenter{

    void refreshData();

    void loadMoreData();

}
