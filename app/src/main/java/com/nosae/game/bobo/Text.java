package com.nosae.game.bobo;

/**
 * Created by eason on 2015/10/13.
 */
import android.graphics.Paint;
import android.graphics.Typeface;

public class Text {
    public int x;
    public int y;
    public int size;
    public String message;
    public Paint paint;
    public boolean isVisible;
    public int startFrame;
    public int delayFrame;

    public Text(int x, int y, int size, String message, int color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.message = message;

        paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(size);
        paint.setTypeface(Typeface.MONOSPACE);
    }
}
