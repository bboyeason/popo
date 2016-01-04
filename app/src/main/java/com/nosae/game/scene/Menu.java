package com.nosae.game.scene;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nosae.game.popo.GameParams;
import com.nosae.game.popo.MainActivity;
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
    private Button mStage1Button;
    private Button mStage2Button;
    private Button mStage3Button;
    private Button mStage4Button;
    private Button mStage5Button;
    private Button mLoadButton;
    private Button mExitButton;
    private int DELAY_ACTIVITY = 100;

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

        mSettingsButton = (Button) findViewById(R.id.settingsButton);
        mKarnofskyScaleButton = (Button) findViewById(R.id.karnofskyButton);
        mStage1Button = (Button) findViewById(R.id.stage1);
        mStage2Button = (Button) findViewById(R.id.stage2);
        mStage3Button = (Button) findViewById(R.id.stage3);
        mStage4Button = (Button) findViewById(R.id.stage4);
        mStage5Button = (Button) findViewById(R.id.stage5);
        mLoadButton = (Button) findViewById(R.id.loadButton);
        mExitButton = (Button) findViewById(R.id.exitButton);
        mSettingsButton.setSoundEffectsEnabled(false);
        mKarnofskyScaleButton.setSoundEffectsEnabled(false);
        mStage1Button.setSoundEffectsEnabled(false);
        mLoadButton.setSoundEffectsEnabled(false);
        mExitButton.setSoundEffectsEnabled(false);
        mStage2Button.getBackground().setAlpha(128);
        mStage3Button.getBackground().setAlpha(128);
        mStage4Button.getBackground().setAlpha(128);
        mStage5Button.getBackground().setAlpha(128);
        mStage2Button.setEnabled(false);
        mStage3Button.setEnabled(false);
        mStage4Button.setEnabled(false);
        mStage5Button.setEnabled(false);
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

        mStage1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStage1Button.setEnabled(false);
                mStage2Button.setEnabled(false);
                mStage3Button.setEnabled(false);
                mStage4Button.setEnabled(false);
                mStage5Button.setEnabled(false);
                Music.playSound();
                Animation amScale = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f, mStage1Button.getWidth() / 2, mStage1Button.getHeight() / 2);
                amScale.setDuration(100);
                mStage1Button.startAnimation(amScale);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(Menu.this, MainActivity.class);
                        i.putExtra(GameParams.STAGE, 1);
                        startActivity(i);
                        this.finish();
                    }

                    private void finish() {
                        if (mMusic != null)
                            mMusic.Pause();
                    }
                }, DELAY_ACTIVITY);
            }
        });

        mStage2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStage1Button.setEnabled(false);
                mStage2Button.setEnabled(false);
                mStage3Button.setEnabled(false);
                mStage4Button.setEnabled(false);
                mStage5Button.setEnabled(false);
                Music.playSound();
                Animation amScale = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f, mStage2Button.getWidth() / 2, mStage2Button.getHeight() / 2);
                amScale.setDuration(100);
                mStage2Button.startAnimation(amScale);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(Menu.this, MainActivity.class);
                        i.putExtra(GameParams.STAGE, 2);
                        startActivity(i);
                        this.finish();
                    }

                    private void finish() {
                        if (mMusic != null)
                            mMusic.Pause();
                    }
                }, DELAY_ACTIVITY);
            }
        });

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mStage3Button.getLayoutParams();
        layoutParams.topMargin = (int) (GameParams.scaleHeight * 0.56);
        mStage3Button.setLayoutParams(layoutParams);
        mStage3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStage1Button.setEnabled(false);
                mStage2Button.setEnabled(false);
                mStage3Button.setEnabled(false);
                mStage4Button.setEnabled(false);
                mStage5Button.setEnabled(false);
                Music.playSound();
                Animation amScale = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f, mStage3Button.getWidth() / 2, mStage3Button.getHeight() / 2);
                amScale.setDuration(100);
                mStage3Button.startAnimation(amScale);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(Menu.this, MainActivity.class);
                        i.putExtra(GameParams.STAGE, 3);
                        startActivity(i);
                        this.finish();
                    }

                    private void finish() {
                        if (mMusic != null)
                            mMusic.Pause();
                    }
                }, DELAY_ACTIVITY);
            }
        });

        mStage4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStage1Button.setEnabled(false);
                mStage2Button.setEnabled(false);
                mStage3Button.setEnabled(false);
                mStage4Button.setEnabled(false);
                mStage5Button.setEnabled(false);
                Music.playSound();
                Animation amScale = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f, mStage4Button.getWidth() / 2, mStage4Button.getHeight() / 2);
                amScale.setDuration(100);
                mStage4Button.startAnimation(amScale);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(Menu.this, MainActivity.class);
                        i.putExtra(GameParams.STAGE, 4);
                        startActivity(i);
                        this.finish();
                    }

                    private void finish() {
                        if (mMusic != null)
                            mMusic.Pause();
                    }
                }, DELAY_ACTIVITY);
            }
        });

        mStage5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStage1Button.setEnabled(false);
                mStage2Button.setEnabled(false);
                mStage3Button.setEnabled(false);
                mStage4Button.setEnabled(false);
                mStage5Button.setEnabled(false);
                Music.playSound();
                Animation amScale = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f, mStage5Button.getWidth() / 2, mStage5Button.getHeight() / 2);
                amScale.setDuration(100);
                mStage5Button.startAnimation(amScale);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(Menu.this, MainActivity.class);
                        i.putExtra(GameParams.STAGE, 5);
                        startActivity(i);
                        this.finish();
                    }

                    private void finish() {
                        if (mMusic != null)
                            mMusic.Pause();
                    }
                }, DELAY_ACTIVITY);
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
        // if API 19 or later, we can use mBouncer.resume()
        mStage1Button.setEnabled(true);
        SharedPreferences settings = getSharedPreferences(GameParams.STAGES_COMPLETED, 0);
        if (settings.getBoolean(GameParams.STAGE1_COMPLETED, false)) {
            mStage2Button.getBackground().setAlpha(255);
            mStage2Button.setEnabled(true);
        }
        if (settings.getBoolean(GameParams.STAGE2_COMPLETED, false)) {
            mStage3Button.getBackground().setAlpha(255);
            mStage3Button.setEnabled(true);
        }
        if (settings.getBoolean(GameParams.STAGE3_COMPLETED, false)) {
            mStage4Button.getBackground().setAlpha(255);
            mStage4Button.setEnabled(true);
        }
        if (settings.getBoolean(GameParams.STAGE4_COMPLETED, false)) {
            mStage5Button.getBackground().setAlpha(255);
            mStage5Button.setEnabled(true);
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
