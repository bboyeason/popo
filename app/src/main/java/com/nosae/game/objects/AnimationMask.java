package com.nosae.game.objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.nosae.game.popo.GameParams;
import com.nosae.game.settings.DebugConfig;

/**
 * Created by eason on 2015/10/27.
 */
public class AnimationMask extends GameObj {

    private int startFrame;
    private int delayFrame;

    public Bitmap bitmap;
    public Rect MaskDestRect;
    public int maxIndex;// Animation frame count
    private int repeat;
    private int id, column, row;

    public AnimationMask(int id, int column, int row, int frameCount, int repeat, int speed, int alpha, int r, int g, int b) {
        super(alpha, r, g, b, speed);
        this.repeat = repeat;
        this.maxIndex = frameCount - 1;
        this.id = id;
        this.column = column;
        this.row = row;
        MaskDestRect = new Rect(0, 0, GameParams.scaleWidth, GameParams.scaleHeight);
    }

    public int getDelayFrame() {
        return delayFrame - startFrame;
    }

    public boolean Action(int frameTime) {
        switch (state) {
            case step1:
                if (!isAlive)
                    isAlive = true;
                startFrame = frameTime;
                state = State.step2;
                bitmap = GameParams.decodeResource(id);
                if (bitmap != null) {
                    srcWidth = bitmap.getWidth() / column;
                    srcHeight = bitmap.getHeight() / row;
                }
                srcRect = new Rect(0, 0, srcWidth, srcHeight);
                destRect = new Rect(GameParams.halfWidth - srcWidth / 2, GameParams.halfHeight - srcHeight / 2, GameParams.halfWidth + srcWidth / 2, GameParams.halfHeight + srcHeight / 2);

                break;
            case step2:
                if (index < maxIndex + 1) {
                    setAnimationIndex(10);
                    if(frameTime % speed == 0)
                        index++;
                } else {
                    if (--repeat > 0) {
                        index = 0;
                    } else {
                        delayFrame = frameTime;
                        isAlive = false;
                        index = 0;
                        release();
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    private void release() {
        if (bitmap != null) {
            bitmap.recycle();
            System.gc();
        }
    }

    public void SetMaskAlpha(int _alpha) {
        if (_alpha < 0)
            _alpha = 0;
        if (_alpha > 255)
            _alpha = 255;
        paint.setAlpha(_alpha);
    }

    public void draw(Canvas canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, srcRect, destRect, null);
        }
    }
}
