package com.nosae.game.objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.nosae.game.bobo.GameParams;
import com.nosae.game.bobo.R;

/**
 * Created by eason on 2015/10/23.
 */
public class Score extends GameObj {

    public static Integer[] mScore = {
            R.drawable.s_0,
            R.drawable.s_1,
            R.drawable.s_2,
            R.drawable.s_3,
            R.drawable.s_4,
            R.drawable.s_5,
            R.drawable.s_6,
            R.drawable.s_7,
            R.drawable.s_8,
            R.drawable.s_9,
    };
    private final int destX;
    private final int destY;

    public int totalScore;
//    public Canvas canvas;
    protected Bitmap bitmap;
    public Score(int destX, int destY) {
        super(destX, destY, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        this.destX = destX;
        this.destY = destY;
//        this.canvas = canvas;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public void drawScore(Canvas canvas) {
        int[] digits = getDigitsOf(totalScore);
//        bitmap = (Bitmap) BitmapFactory.decodeResource(GameParams.res,
//                mScore[0]);
//        DebugConfig.d("drawScore: " + digits[0]);
        Bitmap bitmap;
        for (int i = 0; i < digits.length; i++) {
            bitmap  = (Bitmap) BitmapFactory.decodeResource(GameParams.res, mScore[digits[i]]);
            canvas.drawBitmap(bitmap, destX + i * bitmap.getWidth() ,destY ,null);
        }
    }

    public int[] getDigitsOf(int num)
    {
        int digitCount = Integer.toString(num).length();

        if (num < 0)
            digitCount--;

        int[] result = new int[digitCount];

        while (digitCount-- >0) {
            result[digitCount] = num % 10;
            num /= 10;
        }
        return result;
    }
}
