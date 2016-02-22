package com.nosae.game.scene;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

import com.nosae.game.popo.GameParams;
import com.nosae.game.popo.MainActivity;
import com.nosae.game.popo.R;
import com.nosae.game.settings.DebugConfig;

/**
 * Created by eason on 2015/10/27.
 */
public class Guide extends Activity {
    private static Guide sInstance;

    private static int stage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);
        sInstance = this;

        ObjectAnimator objAnimatorX;
        ObjectAnimator objAnimatorY;
        AnimatorSet animatorSet;
        ImageView imageView = (ImageView) findViewById(R.id.imageView2);
        ImageView imageViewHand = (ImageView) findViewById(R.id.imageViewHand);

        Intent intent = getIntent();
        stage = intent.getIntExtra(GameParams.STAGE, 0);
        try {
            switch (stage) {
                case 1:
                    imageView.setImageResource(R.drawable.guide01);
                    imageViewHand.setImageResource(R.drawable.guide_hand);
                    objAnimatorX = ObjectAnimator.ofFloat(imageViewHand, "translationX", imageViewHand.getX(), -100 * GameParams.densityDpi / 160);
                    objAnimatorY = ObjectAnimator.ofFloat(imageViewHand, "translationY", imageViewHand.getY(), -40 * GameParams.densityDpi / 160);
                    objAnimatorY.setInterpolator(new AccelerateDecelerateInterpolator());
                    animatorSet = new AnimatorSet();
                    animatorSet.play(objAnimatorX).with(objAnimatorY);
                    animatorSet.setDuration(1500);
                    animatorSet.start();
                    break;
                case 2:
                    imageView.setImageResource(R.drawable.guide02);
                    imageViewHand.setImageResource(R.drawable.guide_hand);
                    objAnimatorX = ObjectAnimator.ofFloat(imageViewHand, "translationX", imageViewHand.getX(), -140 * GameParams.densityDpi / 160);
                    objAnimatorY = ObjectAnimator.ofFloat(imageViewHand, "translationY", imageViewHand.getY(), -220 * GameParams.densityDpi / 160);
                    objAnimatorY.setInterpolator(new AccelerateDecelerateInterpolator());
                    animatorSet = new AnimatorSet();
                    animatorSet.play(objAnimatorX).with(objAnimatorY);
                    animatorSet.setDuration(1500);
                    animatorSet.start();
                    break;
                case 3:
                    imageView.setImageResource(R.drawable.guide03);
                    imageViewHand.setImageResource(R.drawable.guide_hand);
                    objAnimatorX = ObjectAnimator.ofFloat(imageViewHand, "translationX", imageViewHand.getX(), -60 * GameParams.densityDpi / 160);
                    objAnimatorY = ObjectAnimator.ofFloat(imageViewHand, "translationY", imageViewHand.getY(), -190 * GameParams.densityDpi / 160);
                    objAnimatorY.setInterpolator(new AccelerateDecelerateInterpolator());
                    animatorSet = new AnimatorSet();
                    animatorSet.play(objAnimatorX).with(objAnimatorY);
                    animatorSet.setDuration(1500);
                    animatorSet.start();
                    break;
                case 4:
                    imageView.setImageResource(R.drawable.guide04);
                    imageViewHand.setBackgroundResource(R.drawable.hand_shake_animation);
                    ((AnimationDrawable) imageViewHand.getBackground()).start();
                    break;
                case 5:
                    imageView.setImageResource(R.drawable.guide05);
                    imageViewHand.setBackgroundResource(R.drawable.hand_shake_animation);
                    ((AnimationDrawable) imageViewHand.getBackground()).start();
                    break;
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(sInstance, MainActivity.class);
                        i.putExtra(GameParams.STAGE, stage);
                        startActivity(i);
                        this.finish();
                    }

                    private void finish() {
                        //TODO stop music
                    }
                });
            }
        });
    }
}
