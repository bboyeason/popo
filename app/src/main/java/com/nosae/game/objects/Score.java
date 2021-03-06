package com.nosae.game.objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.nosae.game.popo.GameParams;
import com.nosae.game.popo.R;
import com.nosae.game.settings.DebugConfig;

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
    public final int edge_X_right;

    public int totalScore;
    public int width;
    public int height;
    protected Bitmap bitmap;
    public Score(int destX, int destY) {
        super(destX, destY, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        this.destX = destX;
        this.destY = destY;
        bitmap  = GameParams.decodeResource(mScore[0]);
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        edge_X_right = (int) (destX + 3 * width + 4 * GameParams.density);
        bitmap.recycle();
        bitmap = null;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public void drawScore(Canvas canvas) {
        int[] digits = getDigitsOf(totalScore);
//        bitmap = (Bitmap) BitmapFactory.decodeResource(GameParams.res,
//                mScore[0]);
        Bitmap bitmap;
        for (int i = 0; i < digits.length; i++) {
            bitmap = GameParams.decodeResource(mScore[digits[i]]);
            if (bitmap != null)
                canvas.drawBitmap(bitmap, destX + i * bitmap.getWidth() + i * 2 * GameParams.density, destY, null);
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
