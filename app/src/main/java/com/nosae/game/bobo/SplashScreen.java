package com.nosae.game.bobo;

/**
 * Created by eason on 2015/10/14.
 */
import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.nosae.game.objects.Music;
import com.nosae.game.settings.DebugConfig;

public class SplashScreen extends Activity {
    private final static String TAG = "BoBo SplashScreen";
    private Music mMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DebugConfig.setTag(TAG);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        LoadContent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (GameParams.videoPlayer.isRuningVideo)
            GameParams.videoPlayer.Resume();

        if (mMusic != null)
            mMusic.Play();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (GameParams.videoPlayer.isRuningVideo)
            GameParams.videoPlayer.Pause();

        if (mMusic != null)
            mMusic.Stop();
    }

    public void LoadContent() {
        GameParams.videoPlayer = new VideoPlayer(this);
        GameParams.videoPlayer.Play();

        if (mMusic == null) {
//            mMusic.player.release();
            mMusic = new Music(this, R.raw.story_music,2);
            mMusic.setLooping(true);
        }
    }
}
