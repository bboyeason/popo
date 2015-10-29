package com.nosae.game.role;

import android.graphics.Bitmap;

import com.nosae.game.objects.FishObj;

/**
 * Created by eason on 2015/10/23.
 */
public class Bobo extends FishObj {

    public static int width = 0;

    public static int height = 0;

    public static int halfWidth = 0;
    public static int halfHeight = 0;
    public Bitmap image;

    public Bobo(Bitmap fishImage, int destX, int destY, int destWidth, int destHeight, int srcX, int srcY, int srcWidth, int srcHeight, int speed, int color, int theta) {
        super(fishImage, destX, destY, destWidth, destHeight, srcX, srcY, srcWidth, srcHeight, speed, color, theta);
        this.image = fishImage;

    }

    public void setMaxIndex(int maxIndex) {
        this.maxIndex = maxIndex;
    }
    public void Animation() {
//        offset = 1;
        FishAnimation(readyToDeath);
    }
}
