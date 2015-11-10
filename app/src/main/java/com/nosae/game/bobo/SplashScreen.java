package com.nosae.game.bobo;

/**
 * Created by eason on 2015/10/14.
 */
import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.nosae.game.settings.DebugConfig;

public class SplashScreen extends Activity {
    private final static String TAG = "BoBo SplashScreen";

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
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (GameParams.videoPlayer.isRuningVideo)
            GameParams.videoPlayer.Pause();
    }

    public void LoadContent() {
        GameParams.videoPlayer = new VideoPlayer(this);
        GameParams.videoPlayer.Play();

    }
}
