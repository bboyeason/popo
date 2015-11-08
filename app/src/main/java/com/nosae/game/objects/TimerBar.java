package com.nosae.game.objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

import com.nosae.game.bobo.GameEntry;
import com.nosae.game.bobo.GameParams;
import com.nosae.game.bobo.R;

import com.nosae.game.sence.Stage1;
import com.nosae.game.sence.Stage2;

import lbs.Game;

/**
 * Created by eason on 2015/10/28.
 */
public class TimerBar extends GameObj {

    private Bitmap mTimerBarImage;
    private int mStartFrame = 0;
    private int mRunningFrame = 0; // fps * 30 seconds
    public boolean isTimeout = false;
    private ColorMask mTimerBar;
    private int destX;
    private int destY;
    public static Integer[] mTimeTable = {
            R.drawable.time_9,
            R.drawable.time_8,
            R.drawable.time_7,
            R.drawable.time_6,
            R.drawable.time_5,
            R.drawable.time_4,
            R.drawable.time_3,
            R.drawable.time_2,
            R.drawable.time_1,
            R.drawable.time_0
    };

    public TimerBar(int gamingTime) {
        super(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

        mStartFrame = (int) GameEntry.totalFrames;
        mRunningFrame = Game.setFPS * gamingTime;

        mTimerBar = new ColorMask(Color.BLUE, 0);
        mTimerBar.isAlive = true;

        mTimerBarImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res, mTimeTable[9]);

        srcWidth = mTimerBarImage.getWidth();
        srcHeight = mTimerBarImage.getHeight();
        destWidth = srcWidth / 2;
        destHeight = srcHeight;
        this.destX = GameParams.halfWidth - destWidth / 2;
        srcRect = new Rect(0, 0, srcWidth , srcHeight);
        destRect = new Rect(this.destX, 0, this.destX + destWidth , destHeight);
    }

    public void addTime(int senonds) {
        mStartFrame += senonds * 30;

    }

    public void action(int totalFrame) {
        if (Stage1.isGameOver || Stage2.isGameOver)
            return;
        int gap;
        int frameGap = totalFrame - mStartFrame;
        if (frameGap < 0)
            mStartFrame = totalFrame;
        if (frameGap <= mRunningFrame) {
            gap = 10 * (totalFrame - mStartFrame) / mRunningFrame;
            if (gap == 0)
                gap = 1;
            if (gap <= mTimeTable.length)
                mTimerBarImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res, mTimeTable[gap - 1]);

        } else {
            isTimeout = true;
        }
    }

    public void draw(Canvas canvas) {
//        if (mTimerBarImage != null) {
            canvas.drawBitmap(mTimerBarImage, srcRect, destRect, null);
//        }
    }
}
