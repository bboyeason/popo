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

import com.nosae.game.bobo.GameParams;
import com.nosae.game.bobo.MainActivity;
import com.nosae.game.bobo.R;

import com.nosae.game.bobo.SplashScreen;
import com.nosae.game.objects.Music;
import com.nosae.game.settings.DebugConfig;

/**
 * Created by eason on 2015/10/27.
 */
public class Menu extends Activity {
    private Music mMusic;
    private ImageView mImageView;
    private Button mSettingsButton;
    private Button mKarnofskyScaleButton;
    private Button mStartButton;
    private Button mLoadButton;
    private Button mExitButton;
    private AnimatorSet mBouncer;

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
        mStartButton = (Button) findViewById(R.id.startButton);
        mLoadButton = (Button) findViewById(R.id.loadButton);
        mExitButton = (Button) findViewById(R.id.exitButton);
        mSettingsButton.setSoundEffectsEnabled(false);
        mKarnofskyScaleButton.setSoundEffectsEnabled(false);
        mStartButton.setSoundEffectsEnabled(false);
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
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mStartButton.getLayoutParams();
        layoutParams.topMargin = (int) (GameParams.scaleHeight * 0.56);
        mStartButton.setLayoutParams(layoutParams);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Music.playSound();
                Animation amScale = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f, mStartButton.getWidth() / 2, mStartButton.getHeight() / 2);
                amScale.setDuration(100);
                mStartButton.startAnimation(amScale);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(Menu.this, MainActivity.class);
                        startActivity(i);
                        this.finish();
                    }

                    private void finish() {
                        // TODO Auto-generated method stub
                        if (mMusic != null)
                            mMusic.Pause();
                    }
                }, 100);
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

        mImageView = new ImageView(this);
//        mImageView.setBackgroundResource(R.drawable.logo_1);
        mImageView.setBackgroundResource(R.drawable.logo_animation_list);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.menu_root);
        layout.addView(mImageView, 0);

        float fromX, toX, fromY, toY;
        fromX = GameParams.scaleWidth;
        toX = -GameParams.scaleWidth;
        fromY = GameParams.scaleHeight - mImageView.getBackground().getIntrinsicHeight();
        toY = fromY;
        // Move effect
        Animation amTranslate = new TranslateAnimation(fromX, toX, fromY, toY);
        amTranslate.setDuration(6000);
        amTranslate.setRepeatCount(-1);

        // Transparent effect
        Animation amAlpha = new AlphaAnimation(0.3f, 1.0f);
        amAlpha.setDuration(6000);
//        amAlpha.setRepeatCount(-1);

        // Animation effect combine
        AnimationSet amSet = new AnimationSet(false);
        amSet.addAnimation(amTranslate);
        amSet.addAnimation(amAlpha);

//        mImageView.startAnimation(amSet);

        mSettingsButton.startAnimation(amAlpha);
        mExitButton.startAnimation(amAlpha);

        ObjectAnimator objAnimator1 = ObjectAnimator.ofFloat(mImageView, "alpha", 0.3f, 1.0f);
        objAnimator1.setRepeatCount(ObjectAnimator.INFINITE);
//        objAnimator1.setRepeatMode(ObjectAnimator.REVERSE);

        ObjectAnimator objAnimator2 = ObjectAnimator.ofFloat(mImageView, "translationX", fromX, toX);
//        objAnimator2.setInterpolator(new AccelerateDecelerateInterpolator());
        objAnimator2.setRepeatCount(ObjectAnimator.INFINITE);
//        objAnimator2.setRepeatMode(ObjectAnimator.REVERSE);

        ObjectAnimator objAnimator3 = ObjectAnimator.ofFloat(mImageView, "translationY", fromY, toY);
        objAnimator3.setRepeatCount(ObjectAnimator.INFINITE);
//        objAnimator1.setRepeatMode(ObjectAnimator.REVERSE);

        objAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
//                DebugConfig.d("the animation value is " + value);
            }
        });

        AnimationDrawable animDrawable = (AnimationDrawable) mImageView.getBackground();
        animDrawable.start();
        mBouncer = new AnimatorSet();
//        mBouncer.play(objAnimator1).with(objAnimator2).with(objAnimator3);
        mBouncer.play(objAnimator2).with(objAnimator3);
        mBouncer.setDuration(6000);
        mBouncer.start();

//        AnimatorSet as = new AnimatorSet();
//        as.playSequentially(mBouncer);
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
        if (mBouncer != null && !mBouncer.isRunning())
            mBouncer.start();
        // if API 19 or later, we can use mBouncer.resume()
    }

    @Override
    protected void onPause() {
        super.onPause();
        DebugConfig.d("Menu onPause()");
        if (mMusic != null)
            mMusic.Pause();
        if (mBouncer != null && mBouncer.isRunning())
            mBouncer.cancel();
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
        if (mBouncer != null)
            mBouncer.cancel();
    }
}