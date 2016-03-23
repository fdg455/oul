package com.oushangfeng.ounews.module.photo.model;

import com.oushangfeng.ounews.bean.SinaPhotoList;
import com.oushangfeng.ounews.callback.RequestCallback;
import com.oushangfeng.ounews.http.HostType;
import com.oushangfeng.ounews.http.manager.RetrofitManager;
import com.socks.library.KLog;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * ClassName: IPhotoListInteractorImpl<p>
 * Author: oubowu<p>
 * Fuction: 图片列表Model层接口实现<p>
 * CreateDate: 2016/2/21 3:48<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class IPhotoListInteractorImpl
        implements IPhotoListInteractor<List<SinaPhotoList.DataEntity.PhotoListEntity>> {
    @Override
    public Subscription requestPhotoList(final RequestCallback<List<SinaPhotoList.DataEntity.PhotoListEntity>> callback, String id, int startPage) {
        return RetrofitManager.getInstance(HostType.SINA_NEWS_PHOTO)
                .getSinaPhotoListObservable(id, startPage).doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        callback.beforeRequest();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread()).doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        KLog.e("错误时处理：" + throwable + " --- " + throwable
                                .getLocalizedMessage());
                    }
                }).flatMap(
                        new Func1<SinaPhotoList, Observable<SinaPhotoList.DataEntity.PhotoListEntity>>() {
                            @Override
                            public Observable<SinaPhotoList.DataEntity.PhotoListEntity> call(SinaPhotoList sinaPhotoList) {
                                return Observable.from(sinaPhotoList.data.list);
                            }
                        }).toSortedList(
                        new Func2<SinaPhotoList.DataEntity.PhotoListEntity, SinaPhotoList.DataEntity.PhotoListEntity, Integer>() {
                            @Override
                            public Integer call(SinaPhotoList.DataEntity.PhotoListEntity photoListEntity, SinaPhotoList.DataEntity.PhotoListEntity photoListEntity2) {
                                return photoListEntity2.pubDate > photoListEntity.pubDate ? 1 : photoListEntity.pubDate == photoListEntity2.pubDate ? 0 : -1;
                            }
                        })
                .subscribe(new Subscriber<List<SinaPhotoList.DataEntity.PhotoListEntity>>() {
                    @Override
                    public void onCompleted() {
                        callback.requestComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.requestError(e + "\n" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<SinaPhotoList.DataEntity.PhotoListEntity> photoListEntities) {
                        callback.requestSuccess(photoListEntities);
                    }
                });
    }

}
