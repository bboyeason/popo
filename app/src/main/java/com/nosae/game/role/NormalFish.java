package com.nosae.game.role;

import android.graphics.Bitmap;

import com.nosae.game.objects.FishObj;

import lbs.Game;

/**
 * Created by eason on 2015/10/23.
 */
public class NormalFish extends FishObj {

    public int width = 0;
    public int height = 0;
    public int halfWidth = 0;
    public int halfHeight = 0;
    public boolean isStackable = false;
    public float life = 0;

    public NormalFish(Bitmap mFishImage, int destX, int destY, int destWidth, int destHeight, int srcX, int srcY, int srcWidth, int srcHeight, int speed, int color, int theta) {
        super(mFishImage, destX, destY, destWidth, destHeight, srcX, srcY, srcWidth, srcHeight, speed, color, theta);
        width = srcWidth;
        height = srcHeight;
        halfWidth = width >> 1;
        halfHeight = height >> 1;
    }
    public void setLife(int life) {
        this.life = life * Game.setFPS;
    }
    public void Animation() {
        Animation(0);
    }
    public void Animation(long frameTime) {
        if (index == 0)
            offset = 1;
        else if (index == maxIndex)
            offset = -1;
        FishAnimation(readyToDeath, frameTime);
    }
}
