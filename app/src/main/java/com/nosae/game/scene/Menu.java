package com.nosae.game.scene;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

import com.nosae.game.popo.GameParams;
import com.nosae.game.popo.R;

import com.nosae.game.objects.Music;
import com.nosae.game.settings.DebugConfig;

/**
 * Created by eason on 2015/10/27.
 */
public class Menu extends Activity {
    private Music mMusic;
    private Button mSettingsButton;
    private Button mKarnofskyScaleButton;
    private Button mLoadButton;
    private Button mExitButton;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // acquire screen size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        GameParams.getScreenInfo(dm);
        loadSharePreferences();

        imageView = (ImageView) findViewById(R.id.imageView);
        mSettingsButton = (Button) findViewById(R.id.settingsButton);
        mKarnofskyScaleButton = (Button) findViewById(R.id.karnofskyButton);
        mLoadButton = (Button) findViewById(R.id.loadButton);
        mExitButton = (Button) findViewById(R.id.exitButton);
        mSettingsButton.setSoundEffectsEnabled(false);
        mKarnofskyScaleButton.setSoundEffectsEnabled(false);
        mLoadButton.setSoundEffectsEnabled(false);
        mExitButton.setSoundEffectsEnabled(false);
        if (mMusic == null) {
//            mMusic.player.release();
            mMusic = new Music(this, R.raw.menu, GameParams.musicVolumeRatio);
            mMusic.setLooping(true);
        }

        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Music.playSound();
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(Menu.this, Settings.class);
                        startActivity(i);
                        this.finish();
                    }

                    private void finish() {
                        // TODO Auto-generated method stub
                        if (mMusic != null)
                            mMusic.Pause();
                    }
                });
            }
        });

        mKarnofskyScaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Music.playSound();
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(Menu.this, KarnofskyScale.class);
                        startActivity(i);
                        this.finish();
                    }

                    private void finish() {
                        // TODO Auto-generated method stub
                        if (mMusic != null)
                            mMusic.Pause();
                    }
                });
            }
        });

        mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Music.playSound();
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(Menu.this, Load.class);
                        startActivity(i);
                        this.finish();
                    }

                    private void finish() {
                        // TODO Auto-generated method stub
                        if (mMusic != null)
                            mMusic.Pause();
                    }
                });
            }
        });

        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Music.playSound();
                Menu.this.finish();
//                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, StageSwipe.class);
                startActivity(i);
            }
        });
        logoInit();
    }

    private void loadSharePreferences() {
        SharedPreferences settings = getSharedPreferences(GameParams.PREFS_MUSIC, 0);
        GameParams.isMusicOn = settings.getBoolean(GameParams.PREFS_MUSIC_KEY, true);
        GameParams.setMusicVolume(settings.getInt(GameParams.PREFS_MUSIC_VOLUME_KEY, 100));
        settings = getSharedPreferences(GameParams.PREFS_SOUND, 0);
        GameParams.isSoundOn = settings.getBoolean(GameParams.PREFS_SOUND_KEY, true);
        GameParams.setSoundVolume(settings.getInt(GameParams.PREFS_SOUND_VOLUME_KEY, 100));
    }

    private void logoInit() {
        DebugConfig.d("Menu logo init");

        Music.soundPoolInit();
        GameParams.soundID = GameParams.soundPool.load(this, R.raw.sound_01, 1);

        // Transparent effect
        Animation amAlpha = new AlphaAnimation(0.3f, 1.0f);
        amAlpha.setDuration(6000);

        mSettingsButton.startAnimation(amAlpha);
        mExitButton.startAnimation(amAlpha);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DebugConfig.d("Menu onResume()");
        if (mMusic != null) {
            mMusic.setMusicVolume(GameParams.musicVolumeRatio);
            mMusic.Play();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DebugConfig.d("Menu onPause()");
        if (mMusic != null)
            mMusic.Pause();
        // if API 19 or later, we can use mBouncer.pause()
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DebugConfig.d("Menu onDestroy()");
    }
}
