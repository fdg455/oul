package com.oushangfeng.ounews.module.video.view;

import com.oushangfeng.ounews.base.BaseView;

/**
 * ClassName: IVideoPlayView<p>
 * Author: oubowu<p>
 * Fuction: 视频播放视图接口<p>
 * CreateDate: 2016/2/23 21:31<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface IVideoPlayView extends BaseView{

    void playVideo(String path);

    void registerScreenBroadCastReceiver();

}
