package com.oushangfeng.ounews.base;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ClassName: BaseObservable<p>
 * Author: oubowu<p>
 * Fuction: Observable基类，因为网络请求和数据库查询所用到Observable都是io线程处理操作，在主线程进行UI展示<p>
 * CreateDate: 2016/8/24 13:30<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class BaseObservable<T> extends Observable<T> {

    protected BaseObservable(OnSubscribe<T> f) {
        super(f);
        this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io());
    }

}
