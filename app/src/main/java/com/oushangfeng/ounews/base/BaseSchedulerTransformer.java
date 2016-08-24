package com.oushangfeng.ounews.base;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/24 0024.
 * 定义了一个默认的线程模型，大多数情况下，我们都会在 io 线程发起 request，在主线程处理 response
 */
public class BaseSchedulerTransformer<T> implements Observable.Transformer<T, T> {

    @Override
    public Observable<T> call(Observable<T> tObservable) {
        return tObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io());
    }

}
