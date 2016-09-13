package com.oushangfeng.ounews.module.video.ui;

import android.net.Uri;
import android.os.Build;
import android.view.MenuItem;
import android.widget.Toast;

import com.oushangfeng.ounews.R;
import com.oushangfeng.ounews.annotation.ActivityFragmentInject;
import com.oushangfeng.ounews.base.BaseActivity;
import com.oushangfeng.ounews.module.video.presenter.IVideoPlayPresenter;
import com.oushangfeng.ounews.module.video.presenter.IVideoPlayPresenterImpl;
import com.oushangfeng.ounews.module.video.view.IVideoPlayView;
import com.oushangfeng.ounews.utils.RxBus;
import com.oushangfeng.ounews.utils.ViewUtil;
import com.oushangfeng.ounews.utils.ijkplayer.IjkController;
import com.oushangfeng.ounews.utils.ijkplayer.widget.media.IjkVideoView;
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

    private IjkVideoView mIjkVideoView;
    private IjkController mIjkController;

    @Override
    protected void initView() {

        //   主题设置了<item name="android:windowIsTranslucent">true</item>不能自动旋转屏幕了，这里强制开启就可以了 -_-
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        ViewUtil.setFullScreen(this);

        String videoUrl = getIntent().getStringExtra("videoUrl");
        String name = getIntent().getStringExtra("videoName");

        mIjkVideoView = (IjkVideoView) findViewById(R.id.ijk_video_view);

        mLoadingView = (ThreePointLoadingView) findViewById(R.id.tpl_view);
        mLoadingView.setOnClickListener(this);

        mPresenter = new IVideoPlayPresenterImpl(this, videoUrl, name);

    }

    @Override
    protected void onStop() {
        super.onStop();
        RxBus.get().post("Bg", true);
    }

    @Override
    public void playVideo(String path, String name) {

        RxBus.get().post("Bg", false);

        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
            mIjkVideoView.setVideoURI(Uri.parse(path));

            mIjkController = new IjkController(this, name);
            mIjkVideoView.setMediaController(mIjkController);

            mIjkVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer mp) {
                    hideProgress();

                    mIjkVideoView.start();
                    mIjkController.show();
                    mIjkVideoView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            RxBus.get().post("Bg", true);
                        }
                    }, 1000);
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

    }

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
        if (mIjkController != null) {
            mIjkController.onDestroy();
        }
        mIjkVideoView.release(true);
    }

    @Override
    public void onBackPressed() {
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
    public void showProgress() {
        mLoadingView.play();
    }

    @Override
    public void hideProgress() {
        mLoadingView.stop();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mLoadingView.setBackground(null);
        } else {
            mLoadingView.setBackgroundDrawable(null);
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

}
