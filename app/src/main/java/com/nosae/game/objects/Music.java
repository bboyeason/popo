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
    public MediaPlayer player;
    public boolean isComplete;
    public static Context context;

    public Music(Context context, int music, int volume) {
        this.context = context;
        player = MediaPlayer.create(context, music);

        // (0~10)
        player.setVolume(volume, volume);

        isComplete = false;

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer arg0) {
                isComplete = true;
            }
        });
    }

    public static void soundPoolInit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        } else {
            createOldSoundPool();
        }
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
        AudioManager am = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);//实例化AudioManager对象
        float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//返回当前AudioManager对象的最大音量值
        float audioCurrentVolumn = am.getStreamVolume(AudioManager.STREAM_MUSIC);//返回当前AudioManager对象的音量值
        float volumnRatio = audioCurrentVolumn/audioMaxVolumn;
        GameParams.soundPool.play(
                sound,          //播放的音乐id
                volumnRatio,    //左声道音量
                volumnRatio,    //右声道音量
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



//import java.util.HashMap;
//		import java.util.Map;
//		import java.util.Random;
//		 
//		import com.llkan.R;
//		 
//		import android.content.Context;
//		import android.media.AudioManager;
//		import android.media.MediaPlayer;
//		import android.media.SoundPool;
//		 
///**
//  * 声音控制类
//  * @author wyf
//  *
//  */
//public class SoundPlayer {
//		 
//		    private static MediaPlayer music;
//		    private static SoundPool soundPool;
//		     
//		    private static boolean musicSt = true; //音乐开关
//		    private static boolean soundSt = true; //音效开关
//		    private static Context context;
//		     
//		    private static final int[] musicId = {R.raw.bg,R.raw.bg1,R.raw.bg2,R.raw.bg3};
//		    private static Map<Integer,Integer> soundMap; //音效资源id与加载过后的音源id的映射关系表
//		     
//		    /**
//      * 初始化方法
//      * @param c
//      */
//		    public static void init(Context c)
//		    {
//		        context = c;
//		 
//		        initMusic();
//		         
//		        initSound();
//		    }
//		     
//		    //初始化音效播放器
//		    private static void initSound()
//		    {
//		        soundPool = new SoundPool(10,AudioManager.STREAM_MUSIC,100);
//		         
//		        soundMap = new HashMap<Integer,Integer>();
//		        soundMap.put(R.raw.itemboom, soundPool.load(context, R.raw.itemboom, 1));
//		        soundMap.put(R.raw.sel, soundPool.load(context, R.raw.sel, 1));
//		    }
//		     
//		    //初始化音乐播放器
//		    private static void initMusic()
//		    {
//		        int r = new Random().nextInt(musicId.length);
//		        music = MediaPlayer.create(context,musicId[r]);
//		        music.setLooping(true);
//		    }
//		     
//		    /**
//      * 播放音效
//      * @param resId 音效资源id
//      */
//		    public static void playSound(int resId)
//		    {
//		        if(soundSt == false)
//		            return;
//		         
//		        Integer soundId = soundMap.get(resId);
//		        if(soundId != null)
//		            soundPool.play(soundId, 1, 1, 1, 0, 1);
//		    }
//		 
//		    /**
//      * 暂停音乐
//      */
//		    public static void pauseMusic()
//		    {
//		        if(music.isPlaying())
//		            music.pause();
//		    }
//		     
//		    /**
//      * 播放音乐
//      */
//		    public static void startMusic()
//		    {
//		        if(musicSt)
//		            music.start();
//		    }
//		     
//		    /**
//      * 切换一首音乐并播放
//      */
//		    public static void changeAndPlayMusic()
//		    {
//		        if(music != null)
//		            music.release();
//		        initMusic();
//		        startMusic();
//		    }
//		     
//		    /**
//      * 获得音乐开关状态
//      * @return
//      */
//		    public static boolean isMusicSt() {
//		        return musicSt;
//		    }
//		     
//		    /**
//      * 设置音乐开关
//      * @param musicSt
//      */
//		    public static void setMusicSt(boolean musicSt) {
//		        SoundPlayer.musicSt = musicSt;
//		        if(musicSt)
//		            music.start();
//		        else
//		            music.stop();
//		    }
//		 
//		    /**
//      * 获得音效开关状态
//      * @return
//      */
//		    public static boolean isSoundSt() {
//		        return soundSt;
//		    }
//		 
//		    /**
//      * 设置音效开关
//      * @param soundSt
//      */
//		    public static void setSoundSt(boolean soundSt) {
//		        SoundPlayer.soundSt = soundSt;
//		    }
//		     
//		    /**
//      * 发出‘邦’的声音
//      */
//		    public static void boom()
//		    {
//		        playSound(R.raw.itemboom);
//		    }
//		}