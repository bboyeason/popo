package com.nosae.game.bobo;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Vibrator;
import android.util.DisplayMetrics;

import com.nosae.game.objects.Music;
import com.nosae.game.settings.DebugConfig;

/**
 * Created by eason on 2015/10/19.
 */
public class GameParams {

    public static final String PREFS_MUSIC = "Music";
    public static final String PREFS_SOUND = "Sound";
    public static Resources res = null;

    public static Rect screenRect;
    public static int boundary = 200;
    public static Rect screenRectBoundary;
    public static int scaleWidth = 0;
    public static int scaleHeight = 0;
    public static int halfWidth = 0;
    public static int halfHeight = 0;
    public static float density = 1.5f;
    public static int densityDpi = 0;

    public static int stage1FishRandomSpeed = 4;
    public static int stage2FishRandomSpeed = 3;

    public static int stage1FishRebirthMin = 100;
    public static int stage1FishRebirthMax = 700;
    public static int stage2FishRebirthMin = 700;
    public static int stage2FishRebirthMax = 1000;

    public static int goldenFishRebirthMin = 1000;
    public static int goldenFishRebirthMax = 1400;

    public static final int stage1BreakScore = 500;
    public static final int stage2BreakScore = 100;

    public static float[] Cosine = new float[360];
    public static float[] Sine = new float[360];

    public static Music music = null;
    public static SoundPool soundPool = null;
    public static boolean isMusicOn = true;
    public static boolean isSoundOn = true;
    public static int soundID = 0;
    public static Vibrator vibrator = null;
    public static VideoPlayer videoPlayer = null;


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
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }

    @SuppressWarnings("deprecation")
    protected static void createOldSoundPool() {
        DebugConfig.d("createOldSoundPool");
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    }

    public static void playSound(Context context) {
        if (!isSoundOn)
            return;
        playSound(context, soundID, 0);
    }
    //播放声音,参数sound是播放音效的id，参数number是播放音效的次数
    public static void playSound(Context context, int sound, int number) {
        if (!isSoundOn)
            return;
        AudioManager am = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);//实例化AudioManager对象
        float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//返回当前AudioManager对象的最大音量值
        float audioCurrentVolumn = am.getStreamVolume(AudioManager.STREAM_MUSIC);//返回当前AudioManager对象的音量值
        float volumnRatio = audioCurrentVolumn/audioMaxVolumn;
        soundPool.play(
                sound,          //播放的音乐id
                volumnRatio,    //左声道音量
                volumnRatio,    //右声道音量
                1,              //优先级，0为最低
                number,         //循环次数，0无不循环，-1无永远循环
                1               //回放速度 ，该值在0.5-2.0之间，1为正常速度
        );
    }

    public static boolean outOfScreenBottom(Rect r) {
        if (screenRect.bottom < r.top) {
            return true;
        }
        return false;
    }

    public static boolean outOfScreenTop(Rect r) {
        if (screenRect.top > r.bottom) {
            return true;
        }
        return false;
    }

    // Collision detection
    public static boolean isCollision(Rect a, Rect b) {
        if (a.left < b.right)
            if (a.right > b.left)
                if (a.top < b.bottom)
                    if (a.bottom > b.top)
                        return true;

        return false;
    }

    public static boolean isInScreen(Rect obj) {
        return isCollision(obj, screenRect);
    }

    public static int getTheta(double XDistance, double YDistance) {
        if (XDistance == 0) XDistance = 1;

        int theta = (int) (Math.atan(YDistance / XDistance) * 180 / Math.PI);

        if (XDistance >= 0 && theta <= 0)
            theta += 360;
        else if (XDistance <= 0)
            theta += 180;

        return theta;
    }

    // For load scaled down image into memory
    public static Bitmap decodeSampledBitmapFromResource(int resId, int reqWidth, int reqHeight) {
        return decodeSampledBitmapFromResource(res, resId, reqWidth, reqHeight);
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    || (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        DebugConfig.d("inSampleSize:" + inSampleSize);
        return inSampleSize;
    }

    public static void getScreenInfo(DisplayMetrics dm) {
        scaleWidth = dm.widthPixels;
        scaleHeight = dm.heightPixels;
        halfWidth = scaleWidth >> 1;
        halfHeight = scaleHeight >> 1;
        density = dm.density;
        densityDpi = dm.densityDpi;
        screenRect = new Rect(0, 0, scaleWidth, scaleHeight);
        screenRectBoundary = new Rect(0 - boundary, 0 - boundary, scaleWidth + boundary, scaleHeight + boundary);
        DebugConfig.d("Screen size: " + dm.widthPixels + " x " + dm.heightPixels + ", density: " + dm.density + ", density dpi: " + dm.densityDpi);
    }

}
