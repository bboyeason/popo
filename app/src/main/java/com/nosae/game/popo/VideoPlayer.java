package com.nosae.game.popo;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.VideoView;

import com.nosae.game.settings.DebugConfig;

/**
 * Created by eason on 2015/11/9.
 */
public class VideoPlayer
{
    public myVideo videoView;
    private int videoPosition = 0;
    private SplashScreen splashScreen;
    public boolean isRuningVideo;
    private static int splashInterval = 100;

    public VideoPlayer(SplashScreen splashScreen) {
        this.splashScreen = splashScreen;
        videoView = new myVideo(this.splashScreen);
        Load(R.raw.story);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            public void onCompletion(MediaPlayer arg0) {
                Destroy();
            }
        });
    }

    public void Load(int movie)
    {
        Uri uri = null;
        try {
            uri = Uri.parse("android.resource://" + splashScreen.getPackageName() + "/" + movie);
            videoView.setVideoURI(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Play()
    {
        isRuningVideo = true;
        splashScreen.setContentView(videoView);
        videoView.requestFocus();
        videoView.start();
    }

    public void Resume() {
        videoView.seekTo(videoPosition);
        videoView.start();
    }

    public void Pause() {
        videoView.pause();
        videoPosition = videoView.getCurrentPosition();
    }

    public void Stop() {
        isRuningVideo = false;
        videoView.stopPlayback();
    }

    public void Destroy() {
        Stop();
        DebugConfig.d("Destroy()");
        videoView.destroyDrawingCache();
        videoView.clearAnimation();
        videoView.clearFocus();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(splashScreen, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                splashScreen.startActivity(i);
                this.finish();
            }

            private void finish() {
                // TODO Auto-generated method stub
            }
        }, splashInterval);
    }

    public class myVideo extends VideoView implements MediaPlayer.OnPreparedListener
    {
        private MediaPlayer mediaPlayer;

        public myVideo(Context context) {
            super(context);
            this.setOnPreparedListener(this);
        }

        public void mute() {
            this.setVolume(0);
        }

        public void unmute() {
            this.setVolume(100);
        }

        private void setVolume(int volume) {
            final int max = 100;
            final double numerator = max - volume > 0 ? Math.log(max - volume) : 0;
            final float _volume = (float) (1 - (numerator / Math.log(max)));
            DebugConfig.d("VideoView setVolume: " + _volume);
            mediaPlayer.setVolume(_volume, _volume);
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            this.mediaPlayer = mp;
            if (GameParams.isMusicOn) {
                unmute();
            }
            else if (!GameParams.isMusicOn) {
                mute();
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(GameParams.scaleWidth, GameParams.scaleHeight);
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {

            return false;
        }
    }
}

