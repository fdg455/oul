package com.oushangfeng.ounews.module.video.presenter;

import com.oushangfeng.ounews.base.BasePresenterImpl;
import com.oushangfeng.ounews.bean.NeteastVideoSummary;
import com.oushangfeng.ounews.common.DataLoadType;
import com.oushangfeng.ounews.module.video.model.IVideoListInteractor;
import com.oushangfeng.ounews.module.video.model.IVideoListInteractorImpl;
import com.oushangfeng.ounews.module.video.view.IVideoListView;
import com.socks.library.KLog;

import java.util.List;

/**
 * ClassName: IVideoListPresenterImpl<p>
 * Author: oubowu<p>
 * Fuction: 视频列表代理接口实现<p>
 * CreateDate: 2016/2/23 17:09<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class IVideoListPresenterImpl
        extends BasePresenterImpl<IVideoListView, List<NeteastVideoSummary>>
        implements IVideoListPresenter {

    private IVideoListInteractor<List<NeteastVideoSummary>> mVideoListInteractor;

    private String mId;
    private int mStartPage;

    private boolean mIsRefresh = true;
    private boolean mHasInit;

    public IVideoListPresenterImpl(IVideoListView view, String id, int startPage) {
        super(view);
        mId = id;
        mStartPage = startPage;
        mVideoListInteractor = new IVideoListInteractorImpl();
        mSubscription = mVideoListInteractor.requestVideoList(this, id, startPage);
    }

    @Override
    public void beforeRequest() {
        if (!mHasInit) {
            mView.showProgress();
        }
    }

    @Override
    public void requestError(String e) {
        super.requestError(e);
        mView.updateVideoList(null,
                mIsRefresh ? DataLoadType.TYPE_REFRESH_FAIL : DataLoadType.TYPE_LOAD_MORE_FAIL);
    }


    @Override
    public void refreshData() {
        mStartPage = 0;
        mIsRefresh = true;
        mSubscription = mVideoListInteractor.requestVideoList(this, mId, mStartPage);
    }

    @Override
    public void loadMoreData() {
        KLog.e("加载更多数据: " + mId + ";" + mStartPage);
        mIsRefresh = false;
        mSubscription = mVideoListInteractor.requestVideoList(this, mId, mStartPage);
    }

    @Override
    public void requestSuccess(List<NeteastVideoSummary> data) {
        KLog.e("请求成功: ");
        mHasInit = true;
        if (data != null && data.size() > 0) {
            mStartPage += 10;
        }
        mView.updateVideoList(data,
                mIsRefresh ? DataLoadType.TYPE_REFRESH_SUCCESS : DataLoadType.TYPE_LOAD_MORE_SUCCESS);
    }
}
