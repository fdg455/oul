package com.oushangfeng.ounews.module.video.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.oushangfeng.ounews.R;
import com.oushangfeng.ounews.annotation.ActivityFragmentInject;
import com.oushangfeng.ounews.base.BaseActivity;
import com.oushangfeng.ounews.module.video.presenter.IVideoPlayPresenter;
import com.oushangfeng.ounews.module.video.presenter.IVideoPlayPresenterImpl;
import com.oushangfeng.ounews.module.video.view.IVideoPlayView;
import com.oushangfeng.ounews.utils.VideoPlayController1;
import com.oushangfeng.ounews.utils.ViewUtil;
import com.oushangfeng.ounews.widget.ThreePointLoadingView;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;

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
    private View mBgView;

    private BroadcastReceiver mBatInfoReceiver;

    private float mDownX;
    private float mDownY;

    private PLVideoView mVideoPlayView;
    private VideoPlayController1 mMediaController;

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

        mVideoPlayView = (PLVideoView) findViewById(R.id.PLVideoView);

        mPresenter = new IVideoPlayPresenterImpl(this, videoUrl);

    }

    @Override
    public void playVideo(String path) {

        mMediaController = new VideoPlayController1(this, mVideoPlayView, mBgView);
        mVideoPlayView.setMediaController(mMediaController);
        mVideoPlayView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);
        mVideoPlayView.setVideoPath(path);
        mVideoPlayView.setOnPreparedListener(new PLMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(PLMediaPlayer plMediaPlayer) {
                mVideoPlayView.setVisibility(View.VISIBLE);
                hideProgress();
            }
        });
        mVideoPlayView.setOnTouchListener(this);
        mVideoPlayView.setOnErrorListener(new PLMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(PLMediaPlayer plMediaPlayer, int i) {
                hideProgress();
                toast("视频播放出错了╮(╯Д╰)╭");
                return true;
            }
        });

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
                if (mVideoPlayView != null && mMediaController != null && mVideoPlayView
                        .isPlaying() && intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                    // 屏幕关闭并且视频正在播放的时候，暂停播放
                    mVideoPlayView.pause();
                    mMediaController.hide();
                } else if (mVideoPlayView != null && intent.getAction()
                        .equals(Intent.ACTION_SCREEN_ON)) {
                    // 亮屏的时候，显示控制器
                    mMediaController.show();
                    mVideoPlayView.start();
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
        mVideoPlayView.setVisibility(View.INVISIBLE);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_bg && mMediaController != null) {
            if (mMediaController.isShowing()) {
                mMediaController.hide();
            } else {
                mMediaController.show();
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
        mVideoPlayView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);
        mMediaController.hide();
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
                if (mMediaController != null && Math.abs(mDownX - event.getX()) <= 50 && Math
                        .abs(mDownY - event.getY()) <= 50) {
                    // 解决与背景点击事件的冲突
                    if (mMediaController.isShowing()) {
                        mMediaController.hide();
                    } else {
                        mMediaController.show();
                    }
                }
                break;
        }
        return true;
    }

}
