package com.nosae.game.role;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.nosae.game.objects.FishObj;

/**
 * Created by eason on 2015/10/23.
 */
public class Popo extends FishObj {

    public class Role2 extends FishObj {
        private Bitmap role2Image;

        public Role2(Bitmap role2Image, int destX, int destY, int destWidth, int destHeight, int srcX, int srcY, int srcWidth, int srcHeight, int speed, int color, int theta) {
            super(role2Image, destX, destY, destWidth, destHeight, srcX, srcY, srcWidth, srcHeight, speed, color, theta);
            this.role2Image = role2Image;
        }
        public void draw(Canvas canvas) {
            canvas.drawBitmap(role2Image, srcRect, destRect, null);
        }
    }
    public static int width = 0;
    public static int height = 0;
    public static int halfWidth = 0;
    public static int halfHeight = 0;
    public Bitmap popoImage;
    public Role2 role2;

    public Popo(Bitmap fishImage, int destX, int destY, int destWidth, int destHeight, int srcX, int srcY, int srcWidth, int srcHeight, int speed, int color, int theta) {
        super(fishImage, destX, destY, destWidth, destHeight, srcX, srcY, srcWidth, srcHeight, speed, color, theta);
        this.popoImage = fishImage;
        role2 = null;
    }

    public void setMaxIndex(int maxIndex) {
        this.maxIndex = maxIndex;
    }
    public void Animation() {
        if (index == 0)
            offset = 1;
        else if (index == maxIndex)
            offset = -1;
        FishAnimation(readyToDeath);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(popoImage, srcRect, destRect, null);
        role2.draw(canvas);
    }
}
