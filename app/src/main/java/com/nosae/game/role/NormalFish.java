package com.nosae.game.role;

import android.graphics.Bitmap;

import com.nosae.game.objects.FishObj;

/**
 * Created by eason on 2015/10/23.
 */
public class NormalFish extends FishObj {

    // ��
    public int width = 0;

    // �e
    public int height = 0;

    public int halfWidth = 0;
    public int halfHeight = 0;
//    public Bitmap fishImage;


    public NormalFish(Bitmap mFishImage, int destX, int destY, int destWidth, int destHeight, int srcX, int srcY, int srcWidth, int srcHeight, int speed, int color, int theta) {
        super(mFishImage, destX, destY, destWidth, destHeight, srcX, srcY, srcWidth, srcHeight, speed, color, theta);
        width = srcWidth;
        height = srcHeight;
        halfWidth = width >> 1;
        halfHeight = height >> 1;
    }

    public void Animation() {
//        offset = 1;
//        if (offset == 1)
//            offset = -1;
//        else
//            offset = 1;
        FishAnimation(readyToDeath);

    }
}
