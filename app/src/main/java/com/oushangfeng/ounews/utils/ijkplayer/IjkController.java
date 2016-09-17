package com.oushangfeng.ounews.utils.ijkplayer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.oushangfeng.ounews.R;
import com.oushangfeng.ounews.utils.ijkplayer.utils.MeasureHelper;
import com.oushangfeng.ounews.utils.ijkplayer.utils.StringHelper;
import com.oushangfeng.ounews.utils.ijkplayer.utils.ViewHelper;
import com.oushangfeng.ounews.utils.ijkplayer.widget.media.IMediaController;
import com.oushangfeng.ounews.utils.ijkplayer.widget.media.IjkVideoView;
import com.oushangfeng.ounews.utils.ijkplayer.widget.view.ProgressView;
import com.socks.library.KLog;

import java.lang.ref.WeakReference;

import tv.danmaku.ijk.media.player.misc.ITrackInfo;

/**
 * Created by Oubowu on 2016/9/8 0008 12:20.
 */
public class IjkController implements IMediaController, View.OnTouchListener, View.OnClickListener, ProgressView.OnProgressChangeListener {

    public static final int PLAY_PROGRESS = 0;
    public static final int SHOW = 1;
    public static final int HIDE_DELAY = 2;

    private final Activity mContext;
    private final IjkControllerHandler mHandler;
    private View mView;
    private View mAnchor;
    private PopupWindow mPopupWindow;
    private LinearLayout mLlTopBar;
    private ImageView mIvBack;
    private TextView mTvTitle;
    //    private TextView mTvDecode;
    //    private ImageView mIvTrack;
    private LinearLayout mLlBottomBar;
    private ImageView mIvClock;
    private ImageView mIvPrevious;
    private ImageView mIvPlay;
    private ImageView mIvNext;
    private ImageView mIvRatio;
    private TextView mTvCurrentTime;
    private ProgressView mPvPlay;
    private TextView mTvTotalTime;
    private ImageView mIvRotate;
    private ProgressView mPvBrightnessVolume;
    private LinearLayout mLlBrightnessVolume;
    private ImageView mIvBrightnessVolume;
    private TextView mTvBrightnessVolume;
    private ImageView mIvLockOutside;

    private MediaController.MediaPlayerControl mPlayer;

    private AnimatorListener mAnimatorListener;

    private boolean mIsLock;

    private IjkVideoView mIjkVideoView;

    private boolean mAnimationStart;

    private AudioManager mAudioManager;
    private boolean mActionDown;
    private boolean mActionMove;


    private float mStartY;
    private float mLastY;

    private WindowManager.LayoutParams mParams;
    private int mTouchSlop;

    private float mBrightness = -1;

    public IjkController(Activity context, String name) {
        mContext = context;

        initPopupWindow(name);

        mHandler = new IjkControllerHandler(mContext.getMainLooper(), this);

    }

    private void initPopupWindow(String name) {

        mView = LayoutInflater.from(mContext).inflate(R.layout.pop_ijkplayer_controller, null);

        mLlTopBar = (LinearLayout) mView.findViewById(R.id.ll_top_bar);

        mView.findViewById(R.id.ll_bar).setOnClickListener(this);

        mIvBack = (ImageView) mView.findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(this);

        mTvTitle = (TextView) mView.findViewById(R.id.tv_title);
        if (!TextUtils.isEmpty(name)) {
            mTvTitle.setText(name);
        }

        //        mTvDecode = (TextView) mView.findViewById(R.id.tv_decode);
        //        mTvDecode.setOnClickListener(this);

        //        mIvTrack = (ImageView) mView.findViewById(R.id.iv_track);
        //        mIvTrack.setOnClickListener(this);

        mLlBottomBar = (LinearLayout) mView.findViewById(R.id.ll_bottom_bar);
        mLlBottomBar.setOnClickListener(this);

        mIvClock = (ImageView) mView.findViewById(R.id.iv_lock);
        mIvClock.setOnClickListener(this);

        mIvPrevious = (ImageView) mView.findViewById(R.id.iv_previous);
        mIvPrevious.setOnClickListener(this);

        mIvPlay = (ImageView) mView.findViewById(R.id.iv_play);
        mIvPlay.setOnClickListener(this);

        mIvNext = (ImageView) mView.findViewById(R.id.iv_next);
        mIvNext.setOnClickListener(this);

        mIvRatio = (ImageView) mView.findViewById(R.id.iv_ratio);
        mIvRatio.setOnClickListener(this);

        mTvCurrentTime = (TextView) mView.findViewById(R.id.tv_current_time);

        mPvPlay = (ProgressView) mView.findViewById(R.id.pv_play);
        mPvPlay.setOnProgressChangeListener(this);

        mTvTotalTime = (TextView) mView.findViewById(R.id.tv_total_time);

        mIvRotate = (ImageView) mView.findViewById(R.id.iv_rotate);
        mIvRotate.setOnClickListener(this);

        mPvBrightnessVolume = (ProgressView) mView.findViewById(R.id.pv_brightness_volume);

        mLlBrightnessVolume = (LinearLayout) mView.findViewById(R.id.ll_brightness_volume);

        mIvBrightnessVolume = (ImageView) mView.findViewById(R.id.iv_brightness_volume);

        mTvBrightnessVolume = (TextView) mView.findViewById(R.id.tv_brightness_volume);

        mIvLockOutside = (ImageView) mView.findViewById(R.id.iv_lock_outside);
        mIvLockOutside.setOnClickListener(this);

        mPopupWindow = new PopupWindow();
        mPopupWindow.setAnimationStyle(R.style.pop_anim_style);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setContentView(mView);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mView.setOnTouchListener(this);
        mView.setOnClickListener(this);

        ((ViewGroup.MarginLayoutParams) mLlTopBar.getLayoutParams()).topMargin = MeasureHelper.getNavigationBarHeight(mContext);

        ((ViewGroup.MarginLayoutParams) mIvLockOutside.getLayoutParams()).topMargin += MeasureHelper.getNavigationBarHeight(mContext);
        mIvLockOutside.setSelected(true);

        mAnimatorListener = new AnimatorListener();

        // 调节音量
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

        // 调节亮度
        mParams = mContext.getWindow().getAttributes();

        mTouchSlop = ViewConfiguration.get(mContext).getScaledPagingTouchSlop();

        KLog.e(getScreenBrightness(mContext));

    }

    public int getScreenBrightness(Activity activity) {
        int value = 0;
        ContentResolver cr = activity.getContentResolver();
        try {
            value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {

        }
        return value;
    }

    @Override
    public void onProgressChange(float progress) {
        mHandler.removeMessages(PLAY_PROGRESS);
        mHandler.removeMessages(HIDE_DELAY);

        final float currentPosition = progress * mPlayer.getDuration() / 1000L;

        mPlayer.seekTo((int) currentPosition);
        mPlayer.pause();

        mTvCurrentTime.setText(StringHelper.generateTime((long) currentPosition, false));

    }

    @Override
    public void onProgressEnd() {
        mHandler.sendEmptyMessageDelayed(PLAY_PROGRESS, 0);
        mHandler.sendEmptyMessageDelayed(HIDE_DELAY, 4000);
        mPlayer.start();
    }

    @Override
    public void hide() {
        if (isShowing()) {

            if (mAnimationStart) {
                return;
            }

            mPvBrightnessVolume.setVisibility(View.INVISIBLE);
            mLlBrightnessVolume.setVisibility(View.INVISIBLE);
            mIvBrightnessVolume.setVisibility(View.INVISIBLE);

            mHandler.removeMessages(PLAY_PROGRESS);
            mHandler.removeMessages(HIDE_DELAY);

            mLlTopBar.animate().translationYBy(-mLlTopBar.getHeight()).setDuration(300).setListener(mAnimatorListener.setState(false));

            mLlBottomBar.animate().translationYBy(mLlBottomBar.getHeight()).setDuration(300);

        }
    }

    @Override
    public boolean isShowing() {
        return mPopupWindow != null && mPopupWindow.isShowing();
    }

    /**
     * IjkVideoView在setMediaController会调用attachMediaController,把它的父布局传递进来作为我们弹窗参考的布局
     *
     * @param view
     */
    @Override
    public void setAnchorView(View view) {
        if (view != mAnchor) {
            mAnchor = view;
        }
    }

    /**
     * IjkVideoView在setMediaController会调用attachMediaController,然后传递isInPlaybackState()来告知我们现在播放器是不是播放的状态,
     * 此时我们的布局就需要做处理
     *
     * @param enabled
     */
    @Override
    public void setEnabled(boolean enabled) {
        mIvPlay.setSelected(enabled);
        mIvPlay.setActivated(false);
    }

    /**
     * IjkVideoView在setMediaController会调用attachMediaController,然后mMediaController.setMediaPlayer(this)把自己传递进来，因为它本身实现了MediaPlayerControl接口，
     * 所以就可以通过mPlayer继续播放器的各种操作了
     *
     * @param player
     */
    @Override
    public void setMediaPlayer(MediaController.MediaPlayerControl player) {
        mPlayer = player;
        if (mPlayer instanceof IjkVideoView) {
            mIjkVideoView = (IjkVideoView) mPlayer;
            // 貌似无效，切换不了设置，测试了禁用了还是会自动转
            mIjkVideoView.toggleAutoRotate(false);
        }
    }

    @Override
    public void show(int timeout) {

        mLlBrightnessVolume.setVisibility(View.INVISIBLE);

        Log.e("TAG", "IjkController-304行-show(): " + mPlayer.getCurrentPosition() + ";" + mPlayer.getDuration());

        mHandler.sendMessageDelayed(mHandler.obtainMessage(SHOW), timeout);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(HIDE_DELAY), 4000);
    }

    @Override
    public void show() {
        show(0);
    }

    /**
     * IjkVideoView没有调用过这个东西，不知道有啥用处
     *
     * @param view
     */
    @Override
    public void showOnce(View view) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (mIsLock) {
            return false;
        }

        final float y = event.getY();
        final float x = event.getX();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                if (mLastY != y) {
                    mActionDown = true;

                    mStartY = y;

                    mHandler.removeMessages(HIDE_DELAY);

                }

                break;

            case MotionEvent.ACTION_MOVE:


                if (mLastY != y) {

                    final float dy = y - mStartY;

                    if (mStartY == 0) {
                        mStartY = y;
                    }

                    mActionMove = true;

                    if (x > mView.getWidth() / 2) {
                        // 右半区域音量控制

                        int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        final int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

                        if (Math.abs(dy) >= mTouchSlop / 2) {

                            mPvBrightnessVolume.setVisibility(View.VISIBLE);
                            mLlBrightnessVolume.setVisibility(View.VISIBLE);
                            mIvBrightnessVolume.setVisibility(View.VISIBLE);
                            mIvBrightnessVolume.setImageResource(R.drawable.ic_volume_up_white);

                            mStartY = y;

                            mPvBrightnessVolume.setMaxProgress(maxVolume);

                            if (dy < 0) {
                                // 向上滑动，增加音量
                                volume++;
                                volume = Math.min(volume, maxVolume);
                            } else {
                                // 向下滑动，减小音量
                                volume--;
                                volume = Math.max(volume, 0);
                            }

                            mPvBrightnessVolume.setProgress(volume);

                            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
                            mTvBrightnessVolume.setText(String.valueOf(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)));

                        } else {
                            mPvBrightnessVolume.setMaxProgress(maxVolume);
                            mPvBrightnessVolume.setProgress(volume);
                            mTvBrightnessVolume.setText(String.valueOf(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)));
                        }
                    } else {
                        // 左半区域，亮度控制

                        mParams = mContext.getWindow().getAttributes();

                        // 0~1范围
                        // float brightness = mParams.screenBrightness;

                        if (mBrightness == -1) {
                            mBrightness = getScreenBrightness(mContext) / 255.0f;
                        }

                        if (Math.abs(dy) >= mTouchSlop / 2) {

                            mPvBrightnessVolume.setVisibility(View.VISIBLE);
                            mLlBrightnessVolume.setVisibility(View.VISIBLE);
                            mIvBrightnessVolume.setVisibility(View.VISIBLE);
                            mIvBrightnessVolume.setImageResource(R.drawable.ic_brightness_6_white);

                            mStartY = y;

                            mPvBrightnessVolume.setMaxProgress(1);

                            if (dy < 0) {
                                // 向上滑动，增加亮度
                                mBrightness += 0.1;
                                mBrightness = Math.min(mBrightness, 1);
                            } else {
                                // 向下滑动，减小亮度
                                mBrightness -= 0.1;
                                mBrightness = Math.max(mBrightness, 0);
                            }

                            mParams.screenBrightness = mBrightness;
                            mContext.getWindow().setAttributes(mParams);

                            mPvBrightnessVolume.setProgress(mBrightness);
                            mTvBrightnessVolume.setText(String.format("%.1f", mBrightness));

                        } else {
                            mPvBrightnessVolume.setMaxProgress(1);
                            mPvBrightnessVolume.setProgress(mBrightness);
                            mTvBrightnessVolume.setText(String.format("%.1f", mBrightness));
                        }

                    }

                }

                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                mHandler.sendEmptyMessageDelayed(HIDE_DELAY, 4000);

                if (mActionMove == true) {

                    mActionDown = false;
                    mActionMove = false;

                    if (Math.abs(mStartY - y) < mTouchSlop) {
                        return false;
                    }

                    return true;
                }

                mActionDown = false;
                mActionMove = false;

                break;

        }

        mLastY = y;

        return false;
    }

    @Override
    public void onClick(View v) {

        if (mAnimationStart) {
            return;
        }

        if (v == mView && !mIsLock) {
            // 没锁定并且点击时，关闭弹窗
            hide();
            return;
        }

        mHandler.removeMessages(HIDE_DELAY);
        mHandler.sendEmptyMessageDelayed(HIDE_DELAY, 4000);

        int i1 = v.getId();
        if (i1 == R.id.iv_back) {// 2016/9/9 0009 退出
            mContext.finish();

        } /*else if (i1 == R.id.tv_decode) {// TODO: 2016/9/9 0009 软硬解码选择
            if (mIjkVideoView != null) {
                // 貌似无效，切换不了设置
                mIjkVideoView.toggleMediaCodec(true);
            }

        } else if (i1 == R.id.iv_track) {// TODO: 2016/9/9 0009 音轨选择
            if (mIjkVideoView != null) {
                final ITrackHolder holder = (ITrackHolder) mContext;
                int selectedVideoTrack = holder.getSelectedTrack(ITrackInfo.MEDIA_TRACK_TYPE_VIDEO);
                int selectedAudioTrack = holder.getSelectedTrack(ITrackInfo.MEDIA_TRACK_TYPE_AUDIO);
                Log.e("TAG", "IjkController-264行-onClick(): " + selectedAudioTrack + ";" + selectedVideoTrack);

                // mInfoInline = String.format(Locale.US, "# %d: %s", mIndex, mTrackInfo.getInfoInline());
                final ITrackInfo[] trackInfo = holder.getTrackInfo();
                for (int i = 0; i < trackInfo.length; i++) {
                    ITrackInfo info = trackInfo[i];
                    String mInfoInline = String.format(Locale.US, "# %d: %s", i, info.getInfoInline());
                    Log.e("TAG", mInfoInline);
                    // 取消轨道
                    holder.deselectTrack(i);
                }


            }

        }*/ else if (i1 == R.id.iv_lock) {// 2016/9/9 0009 锁定屏幕
            mIsLock = true;

            mLlTopBar.animate().translationYBy(-mLlTopBar.getHeight()).setDuration(300);

            mLlBottomBar.animate().translationYBy(mLlBottomBar.getHeight()).setDuration(300);

            mIvLockOutside.animate().scaleX(1).scaleY(1).setDuration(300);


        } else if (i1 == R.id.iv_lock_outside) {// 2016/9/9 0009 左上角的锁定屏幕

            mIsLock = false;

            mLlTopBar.animate().translationYBy(mLlTopBar.getHeight()).setDuration(300);

            mLlBottomBar.animate().translationYBy(-mLlBottomBar.getHeight()).setDuration(300);

            mIvLockOutside.animate().scaleX(0).scaleY(0).setDuration(300);


        } else if (i1 == R.id.iv_previous) {// TODO: 2016/9/9 0009 上个视频


        } else if (i1 == R.id.iv_play) {
            if (mIvPlay.isActivated()) {
                // 2016/9/11 重播
                mPlayer.seekTo(0);
                mPlayer.start();
                mIvPlay.setSelected(true);
                mIvPlay.setActivated(false);
                mHandler.sendMessageDelayed(mHandler.obtainMessage(PLAY_PROGRESS), 0);
            } else {
                // 2016/9/9 0009 暂停或者播放
                if (mPlayer.isPlaying()) {
                    mPlayer.pause();
                    mHandler.removeMessages(PLAY_PROGRESS);
                } else {
                    mPlayer.start();
                    show();
                }
                mIvPlay.setSelected(!mIvPlay.isSelected());
                mIvPlay.setActivated(false);
            }

        } else if (i1 == R.id.iv_next) {// TODO: 2016/9/9 0009 下个视频


        } else if (i1 == R.id.iv_ratio) {// 2016/9/9 0009 屏幕适配模式切换
            if (mIjkVideoView != null) {
                mLlBrightnessVolume.setVisibility(View.VISIBLE);
                mIvBrightnessVolume.setVisibility(View.GONE);
                final String ratio = mIjkVideoView.toggleAspectRatio();
                mTvBrightnessVolume.setText(ratio);
                int id = R.drawable.ic_transform_white;
                switch (ratio) {
                    case "适应屏幕":
                        id = R.drawable.ic_transform_white;
                        break;
                    case "剪切":
                        id = R.drawable.ic_crop_original_white;
                        break;
                    case "100%":
                        id = R.drawable.ic_zoom_out_map_white;
                        break;
                    /*case "拉伸":
                        id = R.drawable.ic_aspect_ratio_white;
                        break;*/
                }
                mIvRatio.setImageResource(id);
            }

        } else if (i1 == R.id.iv_rotate) {// 2016/9/9 0009 旋转屏幕
            ViewHelper.rotateScreen(mContext);

        }
    }

    public void onDestroy() {
        if (isShowing()) {
            mPopupWindow.dismiss();
            mHandler.removeMessages(PLAY_PROGRESS);
        }
    }

    public interface ITrackHolder {
        ITrackInfo[] getTrackInfo();

        int getSelectedTrack(int trackType);

        void selectTrack(int stream);

        void deselectTrack(int stream);
    }

    private static class IjkControllerHandler extends Handler {

        private WeakReference<IjkController> mReference;
        private boolean mFirstShow = true;

        public IjkControllerHandler(Looper looper, IjkController controller) {
            super(looper);
            this.mReference = new WeakReference<>(controller);
        }

        @Override
        public void handleMessage(Message msg) {
            final IjkController controller = mReference.get();
            if (controller != null) {

                final int currentPosition = (int) Math.ceil(controller.mPlayer.getCurrentPosition() / 1000f);
                final int duration = Math.round(controller.mPlayer.getDuration() / 1000f);

                String currentTime = StringHelper.generateTime(controller.mPlayer.getCurrentPosition(), false);
                final String time = StringHelper.generateTime(controller.mPlayer.getDuration(), true);

                if (currentTime.compareTo(time) == 1) {
                    currentTime = time;
                }

                switch (msg.what) {
                    case PLAY_PROGRESS:

                        controller.mTvCurrentTime.setText(currentTime);

                        Log.e("TAG", "IjkControllerHandler-621行-handleMessage(): " + controller.mPlayer.getBufferPercentage());

                        controller.mPvPlay.setSaveProgress(controller.mPlayer.getBufferPercentage() * 10);
                        controller.mPvPlay.setProgress(1000L * currentPosition / duration);

                        // Log.e("TAG", "IjkControllerHandler-629行-handleMessage(): " + Math.round(currentPosition / 1000.0f) + ";" + Math.round(duration / 1000.0f));

                        // Log.e("TAG", "IjkControllerHandler-632行-handleMessage(): " + controller.mPvPlay.getProgress() + " ; " + controller.mPvPlay.getMaxProgress());

                        if (duration > 0 && currentTime.equals(time)/*currentPosition / 1000.0f == duration / 1000.0f*/) {
                            controller.mIvPlay.setActivated(true);
                            Log.e("TAG", "IjkControllerHandler-631行-handleMessage(): ");
                            controller.mPvPlay.setProgress(1000);
                        } else {
                            controller.mIvPlay.setActivated(false);
                            controller.mIvPlay.setSelected(controller.mPlayer.isPlaying());
                            sendMessageDelayed(obtainMessage(PLAY_PROGRESS), 1000);
                        }
                        break;
                    case SHOW:

                        if (!controller.isShowing()) {

                            controller.mPopupWindow
                                    .showAsDropDown(controller.mAnchor, 0, -controller.mAnchor.getHeight() - MeasureHelper.getNavigationBarHeight(controller.mContext));

                            if (!mFirstShow) {
                                controller.mLlTopBar.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        controller.mLlTopBar.animate().translationYBy(controller.mLlTopBar.getHeight()).setDuration(300)
                                                .setListener(controller.mAnimatorListener.setState(true));
                                        controller.mLlBottomBar.animate().translationYBy(-controller.mLlBottomBar.getHeight()).setDuration(300);
                                    }
                                });
                            } else {
                                controller.mPopupWindow.setAnimationStyle(R.style.pop_no_anim_style);
                            }

                        }
                        controller.mTvCurrentTime.setText(currentTime);
                        controller.mPvPlay.setMaxProgress(1000);

                        controller.mTvTotalTime.setText(time);
                        controller.mHandler.sendMessageDelayed(controller.mHandler.obtainMessage(PLAY_PROGRESS), 0);

                        mFirstShow = false;

                        break;
                    case HIDE_DELAY:

                        controller.hide();

                        break;

                    default:
                        break;
                }
            }
        }

    }

    class AnimatorListener extends AnimatorListenerAdapter {

        private boolean mIsShow;

        public AnimatorListener setState(boolean isShow) {
            mIsShow = isShow;
            return this;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            mAnimationStart = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {

            mAnimationStart = false;

            if (!mIsShow) {
                mPopupWindow.dismiss();
            }
        }

    }

}
