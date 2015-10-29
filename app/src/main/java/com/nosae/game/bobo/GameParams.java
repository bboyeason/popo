package com.nosae.game.bobo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Vibrator;

import com.nosae.game.objects.Music;
import com.nosae.game.settings.DebugConfig;

/**
 * Created by eason on 2015/10/19.
 */
public class GameParams {

    public static Resources res = null;

    public static Rect screenRect;
    public static int boundary = 200;
    public static Rect screenRectBoundary;
    public static int scaleWidth = 0;
    public static int scaleHeight = 0;
    public static int halfWidth = 0;
    public static int halfHeight = 0;


    public static int fishRandomSpeed = 5;
    public static int fishRebirthMin = 100;
    public static int fishRebirthMax = 700;

    public static int goldenFishRebirthMin = 1000;
    public static int goldenFishRebirthMax = 1400;

    public static final int stage1BreakScore = 5000;

    public static float[] Cosine = new float[360];
    public static float[] Sine = new float[360];

    public static Music music;
    public static Vibrator vibrator = null;

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
    public static boolean isCollision(Rect a, Rect b)
    {
        if (a.left < b.right)
            if (a.right > b.left)
                if (a.top < b.bottom)
                    if (a.bottom > b.top)
                        return true;

        return false;
    }

    public static boolean isInScreen(Rect obj)
    {
        return isCollision(obj, screenRect);
    }

    // �D����
    public static int getTheta(double XDistance, double YDistance)
    {
        // ���i���H0
        if (XDistance == 0) XDistance = 1;

        // ����
        int theta = (int)(Math.atan(YDistance / XDistance) * 180 / Math.PI);

        // �H���ഫ
        if (XDistance >= 0 && theta <= 0)
            theta += 360;
        else if (XDistance <= 0)
            theta += 180;

        return theta;
    }

    // For load scaled down image into memory
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
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }
            DebugConfig.d("inSampleSize:" + inSampleSize);
            return inSampleSize;
        }
}
