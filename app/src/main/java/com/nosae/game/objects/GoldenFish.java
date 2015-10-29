package com.nosae.game.objects;

import android.graphics.Bitmap;

/**
 * Created by eason on 2015/10/22.
 */
public class GoldenFish extends FishObj {

    public static int width = 0;

    public static int height = 0;

    public static int halfWidth = 0;
    public static int halfHeight = 0;

    public GoldenFish(Bitmap fishImage, int destX, int destY, int destWidth, int destHeight, int srcX, int srcY, int srcWidth, int srcHeight, int speed, int color, int theta) {
        super(fishImage, destX, destY, destWidth, destHeight, srcX, srcY, srcWidth, srcHeight, speed, color, theta);
    }

    public void Animation() {
//        offset = 1;
        if (offset == 1)
            offset = -1;
        else
            offset = 1;
        FishAnimation(readyToDeath);
    }
}
