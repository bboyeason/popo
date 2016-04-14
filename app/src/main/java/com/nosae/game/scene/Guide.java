package com.nosae.game.scene;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.widget.RelativeLayout;

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
        ObjectAnimator objAnimatorY1;
        ObjectAnimator objAnimatorY2;
        ObjectAnimator objAnimatorY3;
        final AnimatorSet animatorSet;
        final ImageView imageView = (ImageView) findViewById(R.id.imageView2);
        final ImageView imageViewHand = (ImageView) findViewById(R.id.imageViewHand);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

        Intent intent = getIntent();
        stage = intent.getIntExtra(GameParams.STAGE, 0);
        try {
            switch (stage) {
                case 1:
                    imageView.setImageResource(R.drawable.guide01);
                    imageViewHand.setImageResource(R.drawable.guide_hand);
                    objAnimatorX = ObjectAnimator.ofFloat(imageViewHand, "translationX", 260 * GameParams.density * GameParams.scaleWidth / imageView.getDrawable().getIntrinsicWidth(), 180 * GameParams.density * GameParams.scaleWidth / imageView.getDrawable().getIntrinsicWidth());
                    objAnimatorY1 = ObjectAnimator.ofFloat(imageViewHand, "translationY",360 * GameParams.density * GameParams.scaleHeight / imageView.getDrawable().getIntrinsicHeight(), 290 * GameParams.density * GameParams.scaleHeight / imageView.getDrawable().getIntrinsicHeight());
                    objAnimatorY2 = ObjectAnimator.ofFloat(imageViewHand, "translationY",290 * GameParams.density * GameParams.scaleHeight / imageView.getDrawable().getIntrinsicHeight(), 190 * GameParams.density * GameParams.scaleHeight / imageView.getDrawable().getIntrinsicHeight());
                    objAnimatorY3 = ObjectAnimator.ofFloat(imageViewHand, "translationY",190 * GameParams.density * GameParams.scaleHeight / imageView.getDrawable().getIntrinsicHeight(), 90 * GameParams.density * GameParams.scaleHeight / imageView.getDrawable().getIntrinsicHeight());
                    animatorSet = new AnimatorSet();
                    animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
                    animatorSet.play(objAnimatorX).with(objAnimatorY1);
                    animatorSet.play(objAnimatorY2).after(objAnimatorY1).after(1000);
                    animatorSet.play(objAnimatorY3).after(objAnimatorY2).after(1000);
                    animatorSet.setDuration(1500);
                    animatorSet.start();
//                    objAnimatorY1.addListener(new Animator.AnimatorListener() {
//                        @Override
//                        public void onAnimationStart(Animator animation) {
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            imageViewHand.setImageResource(R.drawable.guide_hand_point);
//                        }
//
//                        @Override
//                        public void onAnimationCancel(Animator animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animator animation) {
//
//                        }
//                    });
//                    objAnimatorY2.addListener(new Animator.AnimatorListener() {
//                        @Override
//                        public void onAnimationStart(Animator animation) {
//                            imageViewHand.setImageResource(R.drawable.guide_hand);
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            imageViewHand.setImageResource(R.drawable.guide_hand_point);
//                        }
//
//                        @Override
//                        public void onAnimationCancel(Animator animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animator animation) {
//
//                        }
//                    });
                    objAnimatorY3.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            imageViewHand.setImageResource(R.drawable.guide_hand);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            imageViewHand.setImageResource(R.drawable.guide_hand_point);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

                    break;
                case 2:
                    imageView.setImageResource(R.drawable.guide02);
                    imageViewHand.setImageResource(R.drawable.guide_hand);
                    objAnimatorX = ObjectAnimator.ofFloat(imageViewHand, "translationX", 100 * GameParams.density * GameParams.scaleWidth / imageView.getDrawable().getIntrinsicWidth(), 100 * GameParams.density * GameParams.scaleWidth / imageView.getDrawable().getIntrinsicWidth());
                    objAnimatorY1 = ObjectAnimator.ofFloat(imageViewHand, "translationY", 400 * GameParams.density * GameParams.scaleHeight / imageView.getDrawable().getIntrinsicHeight(), 280 * GameParams.density * GameParams.scaleHeight / imageView.getDrawable().getIntrinsicHeight());
                    animatorSet = new AnimatorSet();
                    animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
                    animatorSet.play(objAnimatorX).with(objAnimatorY1);
                    animatorSet.setDuration(1500);
                    animatorSet.start();
                    animatorSet.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            imageViewHand.setImageResource(R.drawable.guide_hand_point);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    break;
                case 3:
                    imageView.setImageResource(R.drawable.guide03);
                    imageViewHand.setImageResource(R.drawable.guide_hand);
                    objAnimatorX = ObjectAnimator.ofFloat(imageViewHand, "translationX", 250 * GameParams.density * GameParams.scaleWidth / imageView.getDrawable().getIntrinsicWidth(), 180 * GameParams.density * GameParams.scaleWidth / imageView.getDrawable().getIntrinsicWidth());
                    objAnimatorY1 = ObjectAnimator.ofFloat(imageViewHand, "translationY", 400 * GameParams.density * GameParams.scaleHeight / imageView.getDrawable().getIntrinsicHeight(), 320 * GameParams.density * GameParams.scaleHeight / imageView.getDrawable().getIntrinsicHeight());
                    animatorSet = new AnimatorSet();
                    animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
                    animatorSet.play(objAnimatorX).with(objAnimatorY1);
                    animatorSet.setDuration(1500);
                    animatorSet.start();
                    break;
                case 4:
                    imageView.setImageResource(R.drawable.guide04);
                    imageViewHand.setBackgroundResource(R.drawable.hand_shake_animation);
                    imageViewHand.setLayoutParams(params);
                    ((AnimationDrawable) imageViewHand.getBackground()).start();
                    break;
                case 5:
                    imageView.setImageResource(R.drawable.guide05);
                    imageViewHand.setBackgroundResource(R.drawable.hand_shake_animation);
                    imageViewHand.setLayoutParams(params);
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
                        imageView.setImageDrawable(null);
                        imageViewHand.setImageDrawable(null);
                        System.gc();
                    }
                });
            }
        });
    }
}
