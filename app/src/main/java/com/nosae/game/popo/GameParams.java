package com.nosae.game.popo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.SoundPool;
import android.os.Vibrator;
import android.util.DisplayMetrics;

import com.nosae.game.objects.AnimationMask;
import com.nosae.game.objects.ColorMask;
import com.nosae.game.objects.Music;
import com.nosae.game.settings.DebugConfig;

import lbs.DrawableGameComponent.MsgHandler;

/**
 * Created by eason on 2015/10/19.
 */
public class GameParams {

    public static final String PREFS_MUSIC = "Music";
    public static final String PREFS_MUSIC_KEY = "isMusicOn";
    public static final String PREFS_MUSIC_VOLUME_KEY = "MusicVolume";
    public static final String PREFS_SOUND = "Sound";
    public static final String PREFS_SOUND_KEY = "isSoundOn";
    public static final String PREFS_SOUND_VOLUME_KEY = "SoundVolume";
    public static final String STAGES_COMPLETED = "StageCompleted";
    public static final String STAGE1_COMPLETED = "Stage1Completed";
    public static final String STAGE2_COMPLETED = "Stage2Completed";
    public static final String STAGE3_COMPLETED = "Stage3Completed";
    public static final String STAGE4_COMPLETED = "Stage4Completed";
    public static final String STAGE5_COMPLETED = "Stage5Completed";

    public static final String STAGE = "stage";
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
    public static int stage4RandomSpeed = 4;
    public static int stage5RandomSpeed = 4;

    public static int stage1FishRebirthMin = 100;
    public static int stage1FishRebirthMax = 700;
    public static int stage2FishRebirthMin = 700;
    public static int stage2FishRebirthMax = 1000;
    public static int stage4FishRebirthMin = 700;
    public static int stage4FishRebirthMax = 1000;
    public static int stage5FishRebirthMin = 700;
    public static int stage5FishRebirthMax = 1000;
    public static int stage5CakeRebirthMax = 5000;
    public static int stage5CakeRebirthMin = 15000;

    public static int goldenFishRebirthMin = 1000;
    public static int goldenFishRebirthMax = 1400;

    public static final int timerBarRowCount = 10;
    public static final int stage1RunningTime = 120;
    public static final int stage2RunningTime = 60;
    public static final int stage3RunningTime = 120;
    public static final int stage4RunningTime = 120;
    public static final int stage5RunningTime = 150;
    public static final int stage1Life = 5;
    public static final int stage2Life = 5;
    public static final int stage3Life = 5;
    public static final int stage4Life = 5;
    public static final int stage5Life = 5;
    public static final int stage1BreakScore = 500;
    public static final int stage2BreakScore = 100;
    public static final int stage3BreakScore = 90;
    public static final int stage4BreakScore = 100;
    public static final int stage5BreakScore = 5;

    public static float[] Cosine = new float[360];
    public static float[] Sine = new float[360];

    public static Music music = null;
    public static SoundPool soundPool = null;
    public static boolean isMusicOn = true;
    public static boolean isSoundOn = true;
    public static float musicVolumeRatio = 1.0f;
    public static float soundVolumeRatio = 1.0f;
    public static int soundID = 0;
    public static Vibrator vibrator = null;
    public static VideoPlayer videoPlayer = null;
    public static int stage1TotalScore = 0;
    public static int stage2TotalScore = 0;
    public static int stage3TotalScore = 0;
    public static int stage4TotalScore = 0;
    public static int stage5TotalScore = 0;
    public static boolean isClearStage1 = false;
    public static boolean isClearStage2 = false;
    public static boolean isClearStage3 = false;
    public static boolean isClearStage4 = false;
    public static boolean isClearStage5 = false;
    public static int[][] specialObjectTable = {
            {
                    R.drawable.sea_star,
                    R.drawable.jellyfish
            },
            { 10, 10 }, /* Animation column */
            {  3,  3 }, /* Animation row */
            {  0,  0 },  /* Max index */
            {  1,  1 }, /* Death animation start */
            { 27, 27 }, /* Death animation end */
            {  0,  0 }, /* Touch Score */
            {  0,  0 }, /* Arrival Score */
            { 10,  0 }, /* Timer add (seconds) */
            {  0,  1 } /* Life add */
    };
    public static AnimationMask gameOverMask;
    public static AnimationMask loadingMask;
    public static AnimationMask breakStageMask;
    public static boolean isGameOver = false;
    public static boolean onOff = true;
    public static MsgHandler msgHandler;

    public static void setMusicVolume(float volume) {
        musicVolumeRatio = volume / 100.0f;
    }

    public static void setSoundVolume(float volume) {
        soundVolumeRatio = volume / 100.0f;
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

    public static boolean isCollisionFromTop(Rect a, Rect b) {
        if (a.left < b.right)
            if (a.right > b.left)
                if (Math.abs(a.top - b.bottom) <= 10)
                        return true;

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

    public static Bitmap decodeResource(int resId) {
        return decodeSampledBitmapFromResource(resId, 0, 0);
    }

    // For load scaled down image into memory
    public static Bitmap decodeSampledBitmapFromResource(int resId, int reqWidth, int reqHeight) {
        return decodeSampledBitmapFromResource(res, resId, reqWidth, reqHeight);
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        Bitmap bitmap = null;
        if (reqWidth == 0 && reqHeight == 0) {
            try {
                bitmap = BitmapFactory.decodeResource(res, resId);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                DebugConfig.e(e.getMessage());
            }
        } else {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            try {
                bitmap = BitmapFactory.decodeResource(res, resId, options);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                DebugConfig.e(e.getMessage());
            }
        }
        return bitmap;
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
