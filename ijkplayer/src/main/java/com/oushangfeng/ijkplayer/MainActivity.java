package com.oushangfeng.ijkplayer;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.oushangfeng.ijkplayer.utils.Settings;
import com.oushangfeng.ijkplayer.widget.media.IRenderView;
import com.oushangfeng.ijkplayer.widget.media.IjkVideoView;
import com.oushangfeng.ijkplayer.widget.view.DropView;
import com.oushangfeng.ijkplayer.widget.view.ProgressView;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;

public class MainActivity extends AppCompatActivity implements IjkController.ITrackHolder {

    private IjkVideoView mVideoView;
    private Settings mSettings;

    private DropView mDrop;
    private ProgressView mProgressView;
    private IjkVideoView mIjkVideoView;
    private IjkController mIjkController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //        setSupportActionBar(toolbar);


//        String key = getString(R.string.pref_key_using_media_codec_auto_rotate);
//        final SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
//        editor.putBoolean(key, false).apply();

//        mSettings = new Settings(this);
        mIjkVideoView = (IjkVideoView) findViewById(R.id.video_view);
        // init player
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        mIjkVideoView.setVideoURI(Uri.parse("http://flv2.bn.netease.com/videolib3/1608/25/YUABK7505/HD/YUABK7505-mobile.mp4"));

        // AndroidMediaController controller = new AndroidMediaController(this, true);
        //        controller.setSupportActionBar(getSupportActionBar());
        //        videoView.setMediaController(controller);

        mIjkController = new IjkController(this);
        mIjkVideoView.setMediaController(mIjkController);

        /*mIjkVideoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                Log.e("TAG", "MainActivity-43行-onInfo(): " + what + ";" + extra);
                return false;
            }
        });*/

        mIjkVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                mIjkVideoView.setVisibility(View.VISIBLE);
                mIjkVideoView.start();
                mIjkController.show();
            }
        });

        /*mIjkVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                Log.e("TAG","MainActivity-80行-onCompletion(): "+mIjkVideoView.getDuration()+";"+mIjkVideoView.getCurrentPosition());
            }
        });*/

        // mDrop = (DropView) findViewById(R.id.drop);
        // mProgressView = (ProgressView) findViewById(R.id.progress_view);
        // mProgressView.attachToDropView(mDrop);
        // mProgressView.setProgress(60);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
