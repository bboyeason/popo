package com.nosae.game.sence;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;

import com.nosae.game.bobo.GameParams;
import com.nosae.game.bobo.R;
import com.nosae.game.settings.DebugConfig;

/**
 * Created by eason on 2015/10/27.
 */
public class Settings extends Activity {

    private static Switch musicSwitch;
    private static Switch soundSwitch;
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
        musicSwitch = (Switch) findViewById(R.id.music_switch);
        soundSwitch = (Switch) findViewById(R.id.sound_switch);

        SharedPreferences settings = getSharedPreferences(GameParams.PREFS_MUSIC, 0);
        boolean isMusicOn = settings.getBoolean("isMusicOn", true);
        musicSwitch.setChecked(isMusicOn);
        GameParams.isMusicOn = isMusicOn;

        settings = getSharedPreferences(GameParams.PREFS_SOUND, 0);
        boolean isSoundOn = settings.getBoolean("isSoundOn", true);
        soundSwitch.setChecked(isSoundOn);
    }

    @Override
    protected void onPause() {
        super.onPause();
        boolean isMusicOn = musicSwitch.isChecked();
        DebugConfig.d("Settings onPause(): Music => " + isMusicOn);
        SharedPreferences settings = getSharedPreferences(GameParams.PREFS_MUSIC, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("isMusicOn", isMusicOn);
        GameParams.isMusicOn = isMusicOn;
        editor.commit();

        boolean isSoundOn = soundSwitch.isChecked();
        DebugConfig.d("Settings onPause(): Sound => " + isSoundOn);
        settings = getSharedPreferences(GameParams.PREFS_SOUND, 0);
        editor = settings.edit();
        editor.putBoolean("isSoundOn", isSoundOn);
        GameParams.isSoundOn = isSoundOn;
        editor.commit();
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
