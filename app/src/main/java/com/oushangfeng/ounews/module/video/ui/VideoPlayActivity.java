package com.oushangfeng.ounews.module.video.ui;

import android.content.res.Configuration;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.oushangfeng.ijkplayer.IjkController;
import com.oushangfeng.ijkplayer.widget.media.IRenderView;
import com.oushangfeng.ijkplayer.widget.media.IjkVideoView;
import com.oushangfeng.ounews.R;
import com.oushangfeng.ounews.annotation.ActivityFragmentInject;
import com.oushangfeng.ounews.base.BaseActivity;
import com.oushangfeng.ounews.module.video.presenter.IVideoPlayPresenter;
import com.oushangfeng.ounews.module.video.presenter.IVideoPlayPresenterImpl;
import com.oushangfeng.ounews.module.video.view.IVideoPlayView;
import com.oushangfeng.ounews.utils.ViewUtil;
import com.oushangfeng.ounews.widget.ThreePointLoadingView;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;

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
public class VideoPlayActivity extends BaseActivity<IVideoPlayPresenter> implements IVideoPlayView, IjkController.ITrackHolder {

    private ThreePointLoadingView mLoadingView;
    //    private View mBgView;

    //    private BroadcastReceiver mBatInfoReceiver;

    //    private float mDownX;
    //    private float mDownY;

    //    private PLVideoView mVideoPlayView;
    //    private VideoPlayController1 mMediaController;

    private IjkVideoView mIjkVideoView;
    private IjkController mIjkController;

    @Override
    protected void initView() {

        //   主题设置了<item name="android:windowIsTranslucent">true</item>不能自动旋转屏幕了，这里强制开启就可以了 -_-
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        ViewUtil.setFullScreen(this);

        String videoUrl = getIntent().getStringExtra("videoUrl");

        //        mBgView = findViewById(R.id.rl_bg);
        //        mBgView.setOnTouchListener(this);

        mIjkVideoView = (IjkVideoView) findViewById(R.id.ijk_video_view);

        mLoadingView = (ThreePointLoadingView) findViewById(R.id.tpl_view);
        mLoadingView.setOnClickListener(this);

        //        mVideoPlayView = (PLVideoView) findViewById(R.id.PLVideoView);

        mPresenter = new IVideoPlayPresenterImpl(this, videoUrl);

    }

    @Override
    public void playVideo(String path) {

        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
            mIjkVideoView.setVideoURI(Uri.parse(path));
            mIjkController = new IjkController(this);
            mIjkVideoView.setMediaController(mIjkController);

            mIjkVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer mp) {
                    hideProgress();
                    mIjkVideoView.start();
                    mIjkController.show();
                }
            });

            mIjkVideoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(IMediaPlayer mp, int what, int extra) {
                    hideProgress();
                    toast("视频播放出错了╮(╯Д╰)╭");
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(VideoPlayActivity.this, "需要Build Variants选择适合的CPU的播放器的包编译哦！", Toast.LENGTH_SHORT).show();
        }


        //        mMediaController = new VideoPlayController1(this, mVideoPlayView, mBgView);
        //        mVideoPlayView.setMediaController(mMediaController);
        //        mVideoPlayView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);
        //        mVideoPlayView.setVideoPath(path);
        //        mVideoPlayView.setOnPreparedListener(new PLMediaPlayer.OnPreparedListener() {
        //            @Override
        //            public void onPrepared(PLMediaPlayer plMediaPlayer) {
        //                mVideoPlayView.setVisibility(View.VISIBLE);
        //                hideProgress();
        //            }
        //        });
        //        mVideoPlayView.setOnTouchListener(this);
        //        mVideoPlayView.setOnErrorListener(new PLMediaPlayer.OnErrorListener() {
        //            @Override
        //            public boolean onError(PLMediaPlayer plMediaPlayer, int i) {
        //                hideProgress();
        //                toast("视频播放出错了╮(╯Д╰)╭");
        //                return true;
        //            }
        //        });

    }

    //    // 注册监听屏幕亮闭屏
    //    @Override
    //    public void registerScreenBroadCastReceiver() {
    //        final IntentFilter filter = new IntentFilter();
    //        // 屏幕灭屏广播
    //        filter.addAction(Intent.ACTION_SCREEN_OFF);
    //        filter.addAction(Intent.ACTION_SCREEN_ON);
    //        mBatInfoReceiver = new BroadcastReceiver() {
    //            @Override
    //            public void onReceive(final Context context, final Intent intent) {
    //                if (mVideoPlayView != null && mMediaController != null && mVideoPlayView
    //                        .isPlaying() && intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
    //                    // 屏幕关闭并且视频正在播放的时候，暂停播放
    //                    mVideoPlayView.pause();
    //                    mMediaController.hide();
    //                } else if (mVideoPlayView != null && intent.getAction()
    //                        .equals(Intent.ACTION_SCREEN_ON)) {
    //                    // 亮屏的时候，显示控制器
    //                    mMediaController.show();
    //                    mVideoPlayView.start();
    //                }
    //
    //            }
    //        };
    //        registerReceiver(mBatInfoReceiver, filter);
    //    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mIjkVideoView.isPlaying()) {
            mIjkVideoView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mIjkVideoView.isPlaying()) {
            mIjkVideoView.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //        unregisterReceiver(mBatInfoReceiver);
        if (mIjkController != null) {
            mIjkController.onDestroy();
        }
        mIjkVideoView.release(true);
    }

    @Override
    public void onBackPressed() {
        //        mVideoPlayView.setVisibility(View.INVISIBLE);
        //        finish();
        if (mIjkController != null && mIjkController.isShowing()) {
            mIjkController.hide();
        } else {
            finish();
        }
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
    public void onClick(View v) {
        //        if (v.getId() == R.id.rl_bg && mMediaController != null) {
        //            if (mMediaController.isShowing()) {
        //                mMediaController.hide();
        //            } else {
        //                mMediaController.show();
        //            }
        //        }
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
        //        mVideoPlayView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);
        //        mMediaController.hide();

        if (mIjkVideoView.getCurrentAspectRatio() == IRenderView.AR_MATCH_PARENT) {
            // 解决比例不正确的问题
            mIjkVideoView.setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);
            mIjkVideoView.post(new Runnable() {
                @Override
                public void run() {
                    mIjkVideoView.setAspectRatio(IRenderView.AR_MATCH_PARENT);
                }
            });
        }

    }

    @Override
    public ITrackInfo[] getTrackInfo() {
        return mIjkVideoView == null ? null : mIjkVideoView.getTrackInfo();
    }

    @Override
    public int getSelectedTrack(int trackType) {
        return mIjkVideoView == null ? -1 : mIjkVideoView.getSelectedTrack(trackType);
    }

    @Override
    public void selectTrack(int stream) {
        if (mIjkVideoView != null) {
            mIjkVideoView.selectTrack(stream);
        }
    }

    @Override
    public void deselectTrack(int stream) {
        if (mIjkVideoView != null) {
            mIjkVideoView.deselectTrack(stream);
        }
    }

    //    @Override
    //    public boolean onTouch(View v, MotionEvent event) {
    //        switch (event.getAction()) {
    //            case MotionEvent.ACTION_DOWN:
    //                mDownX = event.getX();
    //                mDownY = event.getY();
    //                break;
    //            case MotionEvent.ACTION_MOVE:
    //                if (Math.abs(mDownX - event.getX()) > 50 || Math.abs(mDownY - event.getY()) > 50) {
    //                    // 移动超过一定距离，ACTION_UP取消这次事件
    //                    mDownX = Integer.MAX_VALUE;
    //                    mDownY = Integer.MAX_VALUE;
    //                }
    //                break;
    //            case MotionEvent.ACTION_UP:
    //                if (mMediaController != null && Math.abs(mDownX - event.getX()) <= 50 && Math
    //                        .abs(mDownY - event.getY()) <= 50) {
    //                    // 解决与背景点击事件的冲突
    //                    if (mMediaController.isShowing()) {
    //                        mMediaController.hide();
    //                    } else {
    //                        mMediaController.show();
    //                    }
    //                }
    //                break;
    //        }
    //        return true;
    //    }

}
