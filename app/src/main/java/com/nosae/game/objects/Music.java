package com.nosae.game.objects;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

import com.nosae.game.bobo.GameParams;
import com.nosae.game.settings.DebugConfig;

/**
 * Created by eason on 2015/10/27.
 */
public class Music{
    private static float volumeRatio;
    public MediaPlayer player;
    public static int volume;
    public boolean isComplete;
    public static Context context;

    public Music(Context context, int music, int volume) {
        this.context = context;
        player = MediaPlayer.create(context, music);

        setVolume(volume);
        isComplete = false;

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer arg0) {
                isComplete = true;
            }
        });
    }

    public void setVolume(int volume) {
        // (0~10)
        this.volume = volume;
        player.setVolume(volume, volume);
    }

    public void setVolumeRatio(float volumeRatio) {
        this.volumeRatio = volumeRatio;
    }

    public static void soundPoolInit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        } else {
            createOldSoundPool();
        }

        AudioManager audioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
        float audioMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float audioCurrentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumeRatio = audioCurrentVolume/audioMaxVolume;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected static void createNewSoundPool() {
        DebugConfig.d("createNewSoundPool");
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        GameParams.soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }

    @SuppressWarnings("deprecation")
    protected static void createOldSoundPool() {
        DebugConfig.d("createOldSoundPool");
        GameParams.soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    }

    public static void playSound() {
        if (!GameParams.isSoundOn)
            return;
        playSound(GameParams.soundID, 0);
    }

    //播放声音,参数sound是播放音效的id，参数number是播放音效的次数
    public static void playSound(int sound, int number) {
        if (!GameParams.isSoundOn)
            return;

        GameParams.soundPool.play(
                sound,          //播放的音乐id
                volumeRatio,    //左声道音量
                volumeRatio,    //右声道音量
                1,              //优先级，0为最低
                number,         //循环次数，0无不循环，-1无永远循环
                1               //回放速度 ，该值在0.5-2.0之间，1为正常速度
        );
    }

    public void Play() {
//        DebugConfig.d("Music Play().isComplete: " + isComplete);
        if (GameParams.isMusicOn)
            if (!isComplete)
                if (!player.isPlaying())
                    player.start();
    }

    public void Stop() {
        if (player != null && player.isPlaying()) {
            player.stop();
            isComplete = false;
        }
    }

    public void Pause() {
        if (player != null && player.isPlaying())
            player.pause();
    }

    public void setLooping(boolean looping) {
        if (player != null) {
            player.setLooping(looping);
        }
    }
}