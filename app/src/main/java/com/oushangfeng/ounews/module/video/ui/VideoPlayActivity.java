package com.oushangfeng.ounews.module.video.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.oushangfeng.ounews.R;
import com.oushangfeng.ounews.annotation.ActivityFragmentInject;
import com.oushangfeng.ounews.base.BaseActivity;
import com.oushangfeng.ounews.module.video.presenter.IVideoPlayPresenter;
import com.oushangfeng.ounews.module.video.presenter.IVideoPlayPresenterImpl;
import com.oushangfeng.ounews.module.video.view.IVideoPlayView;
import com.oushangfeng.ounews.utils.VideoPlayController;
import com.oushangfeng.ounews.utils.ViewUtil;
import com.oushangfeng.ounews.widget.ThreePointLoadingView;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;

/**
 * ClassName: VideoPlayActivity<p>
 * Author: oubowu<p>
 * Fuction: 视频播放界面<p>
 * CreateDate: 2016/2/23 21:31<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
@ActivityFragmentInject(contentViewId = R.layout.activity_video_play,
        enableSlidr = true)
public class VideoPlayActivity extends BaseActivity<IVideoPlayPresenter>
        implements IVideoPlayView, View.OnTouchListener {

    private ThreePointLoadingView mLoadingView;
    private VideoView mVideoView;
    private View mBgView;

    private VideoPlayController mPlayController;
    private BroadcastReceiver mBatInfoReceiver;

    private float mDownX;
    private float mDownY;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayController != null) {
            mPlayController.onDestroy();
        }
        unregisterReceiver(mBatInfoReceiver);
    }

    @Override
    protected void initView() {

        //   主题设置了<item name="android:windowIsTranslucent">true</item>不能自动旋转屏幕了，这里强制开启就可以了 -_-
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        ViewUtil.setFullScreen(this);

        String videoUrl = getIntent().getStringExtra("videoUrl");

        mBgView = findViewById(R.id.rl_bg);
        mBgView.setOnTouchListener(this);

        mLoadingView = (ThreePointLoadingView) findViewById(R.id.tpl_view);
        mLoadingView.setOnClickListener(this);

        mVideoView = (VideoView) findViewById(R.id.video_view);
        mVideoView.setZOrderOnTop(true);
        mVideoView.getHolder().setFormat(PixelFormat.TRANSPARENT);

        mPresenter = new IVideoPlayPresenterImpl(this, videoUrl);

    }

    @Override
    public void playVideo(String path) {
        if (Vitamio.isInitialized(getApplicationContext())) {

            mVideoView.setVideoPath(path);

            mPlayController = new VideoPlayController(this, mVideoView, mBgView);

            mVideoView.requestFocus();
            mVideoView.setOnTouchListener(this);

            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setPlaybackSpeed(1.0f);
                    hideProgress();
                }
            });
            mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    hideProgress();
                    toast("视频播放出错了╮(╯Д╰)╭");
                    return true;
                }
            });
        } else {
            toast("播放器还没初始化完哎，等等咯╮(╯Д╰)╭ ");
        }
    }

    // 注册监听屏幕亮闭屏
    @Override
    public void registerScreenBroadCastReceiver() {
        final IntentFilter filter = new IntentFilter();
        // 屏幕灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        mBatInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                if (mVideoView != null && mPlayController != null && mVideoView
                        .isPlaying() && intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                    // 屏幕关闭并且视频正在播放的时候，暂停播放
                    mVideoView.pause();
                    mPlayController.hide();
                } else if (mVideoView != null && mPlayController != null && intent.getAction()
                        .equals(Intent.ACTION_SCREEN_ON)) {
                    // 亮屏的时候，显示控制器
                    mPlayController.show();
                }
            }
        };
        registerReceiver(mBatInfoReceiver, filter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        mVideoView.setVisibility(View.INVISIBLE);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_bg && mPlayController != null) {
            if (mPlayController.isShowing()) {
                mPlayController.hide();
            } else {
                mPlayController.show();
            }
        }
    }


    @Override
    public void showProgress() {
        mLoadingView.play();
    }

    @Override
    public void hideProgress() {
        mLoadingView.stop();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 保持屏幕比例正确
        mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
        mPlayController.hide();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(mDownX - event.getX()) > 50 || Math.abs(mDownY - event.getY()) > 50) {
                    // 移动超过一定距离，ACTION_UP取消这次事件
                    mDownX = Integer.MAX_VALUE;
                    mDownY = Integer.MAX_VALUE;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mPlayController != null && Math.abs(mDownX - event.getX()) <= 50 && Math
                        .abs(mDownY - event.getY()) <= 50) {
                    // 解决与背景点击事件的冲突
                    if (mPlayController.isShowing()) {
                        mPlayController.hide();
                    } else {
                        mPlayController.show();
                    }
                }
                break;
        }
        return true;
    }

}
