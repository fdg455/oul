package com.oushangfeng.ounews.base;

import android.support.annotation.CallSuper;

import com.oushangfeng.ounews.callback.RequestCallback;

import rx.Subscription;

/**
 * ClassName: BasePresenterBasePresenterImpl<p>
 * Author:oubowu<p>
 * Fuction: 代理的基类实现<p>
 * CreateDate:2016/2/14 1:45<p>
 * UpdateUser:<p>
 * UpdateDate:<p>
 */
public class BasePresenterImpl<T extends BaseView, V> implements BasePresenter, RequestCallback<V> {

    protected Subscription mSubscription;
    protected T mView;

    public BasePresenterImpl(T view) {
        mView = view;
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onDestroy() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mView = null;
    }


    @Override
    public void beforeRequest() {
        mView.showProgress();
    }

    @CallSuper
    @Override
    public void requestError(String msg) {
        mView.hideProgress();
    }

    @Override
    public void requestComplete() {
        mView.hideProgress();
    }

    @Override
    public void requestSuccess(V data) {

    }
}
