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
//    public int width = 0;
//    public int height = 0;
    private int column = 5;
    private static int mLife = 5;
    private int destX;

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
    }

    public static int getLife() {
        return mLife;
    }

    public static void setLife(int mLife) {
        Life1.mLife = mLife;
    }

    public static void addLife(int life) {
        Life1.mLife += life;

        if (Life1.mLife > 5)
            Life1.mLife = 5;
        else if (Life1.mLife < 0)
            Life1.mLife = 0;
    }

    public void updateLife() {
        mNumberImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res, mNumberTable[getLife()], mOptions);
    }

    public void draw(Canvas canvas) {
        if (mNumberImage != null) {
        canvas.drawBitmap(mNumberImage, srcRect, destRect, null);
        }
    }
}
