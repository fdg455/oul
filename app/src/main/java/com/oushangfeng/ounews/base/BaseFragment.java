package com.oushangfeng.ounews.base;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oushangfeng.ounews.R;
import com.oushangfeng.ounews.annotation.ActivityFragmentInject;
import com.oushangfeng.ounews.app.App;
import com.oushangfeng.ounews.utils.RxBus;
import com.oushangfeng.ounews.widget.refresh.RefreshLayout;
import com.squareup.leakcanary.RefWatcher;

import rx.Observable;
import rx.functions.Action1;

/**
 * ClassName: BaseFragment<p>
 * Author:oubowu<p>
 * Fuction: Fragment基类<p>
 * CreateDate:2016/2/14 19:52<p>
 * UpdateUser:<p>
 * UpdateDate:<p>
 */
public abstract class BaseFragment<T extends BasePresenter> extends Fragment
        implements BaseView, View.OnClickListener {

    // 将代理类通用行为抽出来
    protected T mPresenter;

    protected View mFragmentRootView;
    protected int mContentViewId;

    /**
     * 监听AppbarLayout偏移量
     */
    private Observable<Boolean> mAppbarOffsetObservable;
    private RefreshLayout mRefreshLayout;

    // 是否处理RefreshLayout与AppbarLayout的冲突
    private boolean mHandleRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (null == mFragmentRootView) {
            if (getClass().isAnnotationPresent(ActivityFragmentInject.class)) {
                ActivityFragmentInject annotation = getClass()
                        .getAnnotation(ActivityFragmentInject.class);
                mContentViewId = annotation.contentViewId();
                mHandleRefreshLayout = annotation.handleRefreshLayout();
            } else {
                throw new RuntimeException(
                        "Class must add annotations of ActivityFragmentInitParams.class");
            }
            mFragmentRootView = inflater.inflate(mContentViewId, container, false);

            if (mHandleRefreshLayout) {
                initRefreshLayoutEvent();
            }

            initView(mFragmentRootView);
        }

        return mFragmentRootView;
    }

    /**
     * 订阅事件处理RefreshLayout
     */
    private void initRefreshLayoutEvent() {
        mRefreshLayout = (RefreshLayout) mFragmentRootView.findViewById(R.id.refresh_layout);
        if (mRefreshLayout != null) {
            mAppbarOffsetObservable = RxBus.get().register("enableRefreshLayout", Boolean.class);
            mAppbarOffsetObservable.subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean enable) {
                    mRefreshLayout.setRefreshable(enable);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ViewGroup parent = (ViewGroup) mFragmentRootView.getParent();
        if (null != parent) {
            parent.removeView(mFragmentRootView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        if (mHandleRefreshLayout && mRefreshLayout != null && mAppbarOffsetObservable != null) {
            RxBus.get().unregister("enableRefreshLayout", mAppbarOffsetObservable);
        }
        // 使用 RefWatcher 监控 Fragment
        RefWatcher refWatcher = App.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    public BaseFragment() {
    }

    protected abstract void initView(View fragmentRootView);

    /**
     * 当通过changeFragment()显示时会被调用(类似于onResume)
     */
    public void onChange() {
    }

    protected void showSnackbar(String msg) {
        Snackbar.make(mFragmentRootView, msg, Snackbar.LENGTH_SHORT).show();
    }

    protected void showSnackbar(int id) {
        Snackbar.make(mFragmentRootView, id, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 继承BaseView抽出显示信息通用行为
     *
     * @param msg
     */
    @Override
    public void toast(String msg) {
        showSnackbar(msg);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onClick(View v) {

    }
}
