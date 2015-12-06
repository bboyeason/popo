package com.nosae.game.objects;

import com.nosae.game.scene.Stage1;
import com.nosae.game.scene.Stage2;
import com.nosae.game.settings.DebugConfig;

import lbs.Game;

/**
 * Created by eason on 2015/10/29.
 */
public class TimerBar2 extends GameObj {
    public int width = 0;
    public int height = 0;
    private int column = 1;
    private int row = 9;

    private int mStartFrame = 0;
    private int mRunningFrame = 0; // fps * 30 seconds
    public boolean isTimeout = false;

    public TimerBar2(int destX, int destY, int destWidth, int destHeight, int srcX, int srcY, int srcWidth, int srcHeight, int speed, int color, int theta) {
        super(destX, destY, destWidth, destHeight, srcX, srcY, srcWidth, srcHeight, speed, color, theta);
        this.width = srcWidth;
        this.height = srcHeight;
    }

    public void setStartFrame(int startFrame) {
        this.mStartFrame = startFrame;
        DebugConfig.d("Start Frame: " + mStartFrame);
    }

    public void setTimer(int seconds) {
        mRunningFrame = Game.setFPS * seconds;
    }

    public void addTimer(int seconds) {
        mRunningFrame += Game.setFPS * seconds;
    }

    public void action(int totalFrame) {
        if (Stage1.isGameOver || Stage2.isGameOver)
            return;
        int gap;
        int frameGap = totalFrame - mStartFrame;
        if (frameGap < 0)
            mStartFrame = totalFrame;
        if (frameGap <= mRunningFrame) {
            gap = row * (totalFrame - mStartFrame) / mRunningFrame;
            if (gap == 0)
                gap = 1;
//            if (gap <= mTimeTable.length)
//                mTimerBarImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res, mTimeTable[gap - 1]);
            index = gap - 1;
        } else {
            isTimeout = true;
        }

        setAnimationIndex(column);
    }
}
