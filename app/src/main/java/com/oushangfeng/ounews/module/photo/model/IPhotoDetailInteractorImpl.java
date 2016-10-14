package com.oushangfeng.ounews.module.photo.model;

import com.oushangfeng.ounews.base.BaseSubscriber;
import com.oushangfeng.ounews.bean.SinaPhotoDetail;
import com.oushangfeng.ounews.callback.RequestCallback;
import com.oushangfeng.ounews.http.HostType;
import com.oushangfeng.ounews.http.manager.RetrofitManager;

import rx.Subscription;

/**
 * ClassName: IPhotoDetailInteractorImpl<p>
 * Author: oubowu<p>
 * Fuction: 图片详情的Model层接口实现<p>
 * CreateDate: 2016/2/22 17:47<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class IPhotoDetailInteractorImpl implements IPhotoDetailInteractor<SinaPhotoDetail> {
    @Override
    public Subscription requestPhotoDetail(final RequestCallback<SinaPhotoDetail> callback, String id) {
        return RetrofitManager.getInstance(HostType.SINA_NEWS_PHOTO).getSinaPhotoDetailObservable(id)
                .subscribe(new BaseSubscriber<>(callback));
    }
}
