package com.nosae.game.scene;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

import com.nosae.game.objects.Music;
import com.nosae.game.popo.GameParams;
import com.nosae.game.popo.MainActivity;
import com.nosae.game.popo.R;
import com.nosae.game.settings.DebugConfig;

/**
 * Created by eason on 2016/1/4.
 */
public class StageSwipe extends FragmentActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Button mSettingsButton;
    private Button mExitButton;
    private static int DELAY_ACTIVITY = 100;
    private static StageSwipe sInstance;
    private Music mMusic;
    private static int STAGE_COUNT = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stage_swipe);
        sInstance = this;

        mSettingsButton = (Button) findViewById(R.id.settingsButton);
        mExitButton = (Button) findViewById(R.id.exitButton);
        mSettingsButton.setSoundEffectsEnabled(false);
        mExitButton.setSoundEffectsEnabled(false);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        if (mMusic == null) {
            mMusic = new Music(this, R.raw.stage_selection, GameParams.musicVolumeRatio);
            mMusic.setLooping(true);
        }

        // Transparent effect
        Animation amAlpha = new AlphaAnimation(0.3f, 1.0f);
        amAlpha.setDuration(6000);

        mSettingsButton.startAnimation(amAlpha);
        mExitButton.startAnimation(amAlpha);
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Music.playSound();
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(sInstance, Settings.class);
                        startActivity(i);
                        this.finish();
                    }

                    private void finish() {
                        // TODO music off
                    }
                });
            }
        });

        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Music.playSound();
                StageSwipe.this.finish();
//                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return STAGE_COUNT;
        }
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.stage_swipe_fragment, container, false);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
            ImageView imageViewIcon = (ImageView) rootView.findViewById(R.id.imageView_icon);
            ObjectAnimator objAnimator = ObjectAnimator.ofFloat(imageViewIcon, "translationY", -50, 50);
            objAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            objAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            objAnimator.setRepeatMode(ObjectAnimator.REVERSE);
            objAnimator.setDuration(1500);
            objAnimator.start();

            try {
                switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                    case 1:
                        imageView.setBackgroundResource(R.drawable.stage01);
                        imageViewIcon.setImageResource(R.drawable.stage01_icon_01);
                        break;
                    case 2:
                        imageView.setBackgroundResource(R.drawable.stage02);
                        imageViewIcon.setImageResource(R.drawable.stage02_icon_01);
                        break;
                    case 3:
                        imageView.setBackgroundResource(R.drawable.stage03);
                        imageViewIcon.setImageResource(R.drawable.stage03_icon_01);
                        break;
                    case 4:
                        imageView.setBackgroundResource(R.drawable.stage04);
                        imageViewIcon.setImageResource(R.drawable.stage04_icon_01);
                        break;
                    case 5:
                        imageView.setBackgroundResource(R.drawable.stage05);
                        imageViewIcon.setImageResource(R.drawable.stage05_icon_01);
                        break;
                    case 6:
                        imageView.setBackgroundResource(R.drawable.stage06);
                        imageViewIcon.setImageResource(R.drawable.stage06_icon_01);
                        break;
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DebugConfig.d("Select stage: " + getArguments().getInt(ARG_SECTION_NUMBER));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i;
                            if (getArguments().getInt(ARG_SECTION_NUMBER) == 6) {
                                i = new Intent(sInstance, KarnofskyScale.class);
                            } else {
                                i = new Intent(sInstance, Guide.class);
                            }
                            i.putExtra(GameParams.STAGE, getArguments().getInt(ARG_SECTION_NUMBER));
                            startActivity(i);
                            this.finish();
                        }

                        private void finish() {
                            //TODO stop music
                        }
                    }, DELAY_ACTIVITY);
                }
            });
            return rootView;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMusic != null) {
            mMusic.setMusicVolume(GameParams.musicVolumeRatio);
            mMusic.Play();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMusic != null)
            mMusic.Pause();
    }
}
