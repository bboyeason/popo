package com.nosae.game.scene;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;

import com.nosae.game.popo.GameParams;
import com.nosae.game.popo.R;
import com.nosae.game.settings.DebugConfig;

/**
 * Created by eason on 2015/10/27.
 */
public class Settings extends Activity {

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        ToggleButton mMusicToggleButton = (ToggleButton) findViewById(R.id.music_togglebutton);
        ToggleButton mSoundToggleButton = (ToggleButton) findViewById(R.id.sound_togglebutton);
        mSoundToggleButton.setVisibility(View.INVISIBLE);//temp

        SeekBar mMusicSeekBar = (SeekBar) findViewById(R.id.music_seekBar);
        SeekBar mSoundSeekBar = (SeekBar) findViewById(R.id.sound_seekBar);

        SharedPreferences settings = getSharedPreferences(GameParams.PREFS_MUSIC, 0);
        GameParams.isMusicOn = settings.getBoolean(GameParams.PREFS_MUSIC_KEY, true);
        mMusicToggleButton.setChecked(GameParams.isMusicOn);

        settings = getSharedPreferences(GameParams.PREFS_SOUND, 0);
//        GameParams.isSoundOn = settings.getBoolean(GameParams.PREFS_SOUND_KEY, true);
        GameParams.isSoundOn = GameParams.isMusicOn;//temp
        mSoundToggleButton.setChecked(GameParams.isSoundOn);

        settings = getSharedPreferences(GameParams.PREFS_MUSIC, 0);
        GameParams.setMusicVolume(settings.getInt(GameParams.PREFS_MUSIC_VOLUME_KEY, 100));
        mMusicSeekBar.setProgress((int) (GameParams.musicVolumeRatio * 100));

        settings = getSharedPreferences(GameParams.PREFS_SOUND, 0);
        GameParams.setSoundVolume(settings.getInt(GameParams.PREFS_SOUND_VOLUME_KEY, 100));
        mSoundSeekBar.setProgress((int) (GameParams.soundVolumeRatio * 100));

        mMusicToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GameParams.isMusicOn = isChecked;
                GameParams.isSoundOn = isChecked;//temp
                DebugConfig.d("Settings: music => " + GameParams.isMusicOn);
                SharedPreferences settings = getSharedPreferences(GameParams.PREFS_MUSIC, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(GameParams.PREFS_MUSIC_KEY, GameParams.isMusicOn);
                editor.apply();
            }
        });

        mSoundToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GameParams.isSoundOn = isChecked;
                DebugConfig.d("Settings: sound => " + GameParams.isSoundOn);
                SharedPreferences settings = getSharedPreferences(GameParams.PREFS_SOUND, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(GameParams.PREFS_SOUND_KEY, GameParams.isSoundOn);
                editor.apply();
            }
        });

        mMusicSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                GameParams.setMusicVolume(seekBar.getProgress());
                DebugConfig.d("Music volume: " + GameParams.musicVolumeRatio);
                SharedPreferences settings = getSharedPreferences(GameParams.PREFS_MUSIC, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt(GameParams.PREFS_MUSIC_VOLUME_KEY, (int) (GameParams.musicVolumeRatio * 100));
                editor.apply();
            }
        });

        mSoundSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                GameParams.setSoundVolume(seekBar.getProgress());
                DebugConfig.d("Sound volume: " + GameParams.soundVolumeRatio);
                SharedPreferences settings = getSharedPreferences(GameParams.PREFS_SOUND, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt(GameParams.PREFS_SOUND_VOLUME_KEY, (int) (GameParams.soundVolumeRatio * 100));
                editor.apply();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
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
