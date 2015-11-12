package com.nosae.game.sence;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nosae.game.bobo.GameParams;
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

        mSettingsButton = (Button) findViewById(R.id.settingsButton);
        mKarnofskyScaleButton = (Button) findViewById(R.id.karnofskyButton);
        mStartButton = (Button) findViewById(R.id.startButton);
        mLoadButton = (Button) findViewById(R.id.loadButton);
        mExitButton = (Button) findViewById(R.id.exitButton);
        if (mMusic == null) {
//            mMusic.player.release();
            mMusic = new Music(this, R.raw.menu,2);
            mMusic.Play();
        }

        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(Menu.this, SplashScreen.class);
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

            }
        });

        logoInit();
    }

    private void logoInit() {
        DebugConfig.d("Menu logo init");
        mImageView = new ImageView(this);
        mImageView.setBackgroundResource(R.drawable.logo_1);

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

        ObjectAnimator objAnimator2 = ObjectAnimator.ofFloat(mImageView, "translationX", fromX, toX, fromX);
//        objAnimator2.setInterpolator(new AccelerateDecelerateInterpolator());
        objAnimator2.setRepeatCount(ObjectAnimator.INFINITE);
        objAnimator1.setRepeatMode(ObjectAnimator.REVERSE);

        ObjectAnimator objAnimator3 = ObjectAnimator.ofFloat(mImageView, "translationY", fromY, toY);
        objAnimator3.setRepeatCount(ObjectAnimator.INFINITE);
//        objAnimator1.setRepeatMode(ObjectAnimator.REVERSE);

        AnimatorSet bouncer = new AnimatorSet();
        bouncer.play(objAnimator1).with(objAnimator2).with(objAnimator3);
        bouncer.setDuration(6000);
        bouncer.start();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mMusic.player.isPlaying()) {
            mMusic.Play();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMusic.player.isPlaying())
            mMusic.Pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
