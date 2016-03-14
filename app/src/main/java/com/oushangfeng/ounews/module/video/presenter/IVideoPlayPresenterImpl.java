package com.oushangfeng.ounews.module.video.presenter;

import com.oushangfeng.ounews.base.BasePresenterImpl;
import com.oushangfeng.ounews.module.video.view.IVideoPlayView;

/**
 * ClassName: IVideoPlayPresenterImpl<p>
 * Author: oubowu<p>
 * Fuction: 视频播放代理接口实现<p>
 * CreateDate: 2016/2/23 21:32<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class IVideoPlayPresenterImpl extends BasePresenterImpl<IVideoPlayView, Void>
        implements IVideoPlayPresenter {

    public IVideoPlayPresenterImpl(IVideoPlayView view, String path) {
        super(view);
        mView.registerScreenBroadCastReceiver();
        mView.playVideo(path);
        mView.showProgress();
    }
}
