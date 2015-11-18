package com.nosae.game.objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.nosae.game.bobo.GameParams;
import com.nosae.game.bobo.R;

/**
 * Created by eason on 2015/10/31.
 */
public class Life1 extends GameObj {
    private static int mLife = 5;
    private static int oldLife = mLife;
    private int scaleTotal = 10;
    private static int scaleOffset = 0;
    private int scaleCount = 0;

    private Bitmap mNumberImage;
    BitmapFactory.Options mOptions;
    private static Integer[] mNumberTable = {
            R.drawable.s_0,
            R.drawable.s_1,
            R.drawable.s_2,
            R.drawable.s_3,
            R.drawable.s_4,
            R.drawable.s_5,
            R.drawable.s_6,
            R.drawable.s_7,
            R.drawable.s_8,
            R.drawable.s_9
    };

    public Life1(int destX, int destY, int destWidth, int destHeight, int srcX, int srcY, int srcWidth, int srcHeight) {
        super(destX, destY, destWidth, destHeight, srcX, srcY, srcWidth, srcHeight, 0, 0, 0);
        mOptions = new BitmapFactory.Options();
        mOptions.inSampleSize = 2;
        mNumberImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res, mNumberTable[getLife()], mOptions);
    }

    public static int getLife() {
        return mLife;
    }

    public static void setLife(int mLife) {
        Life1.mLife = mLife;
    }

    public static void addLife(int life) {
        scaleOffset = 1;
        mLife += life;

        if (mLife > 5)
            mLife = 5;
        else if (mLife < 0)
            mLife = 0;
    }

    public void updateLife() {
        if (getLife() != oldLife) {
            mNumberImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res, mNumberTable[getLife()], mOptions);
            oldLife = getLife();
        }
    }

    public void action() {
        if (scaleOffset == 0)
            return;

        if (scaleOffset == 1) {
            scale(1, 1);
            scaleCount ++;
        } else if (scaleOffset == -1) {
            scale(-1, -1);
            scaleCount--;
        }

        if (scaleCount >= scaleTotal)
            scaleOffset = -1;
        else if (scaleCount <= 0)
            scaleOffset = 0;
    }

    public void draw(Canvas canvas) {
        if (mNumberImage != null) {
        canvas.drawBitmap(mNumberImage, srcRect, destRect, null);
        }
    }
}
