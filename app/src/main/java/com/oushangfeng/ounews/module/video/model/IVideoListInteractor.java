package com.oushangfeng.ounews.module.video.model;

import com.oushangfeng.ounews.callback.RequestCallback;

import rx.Subscription;

/**
 * ClassName: IVideoListInteractor<p>
 * Author: oubowu<p>
 * Fuction: 视频列表Model层接口<p>
 * CreateDate: 2016/2/23 17:10<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface IVideoListInteractor<T> {

    Subscription requestVideoList(RequestCallback<T> callback, String id, int startPage);

}
