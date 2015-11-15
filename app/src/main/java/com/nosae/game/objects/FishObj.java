package com.nosae.game.objects;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.nosae.game.bobo.GameParams;

/**
 * Created by eason on 2015/10/14.
 */
public class FishObj extends GameObj {
//TODO implement OnClick

    private Rect mActRect;
    private Random r = new Random();
    private int touchScore = 0;
    private int arrivalScore = 0;
    private int col = 1;// 1: Not animation, 2~: Animation
    public int maxIndex = 0;// Animation frame count
    public int deathIndexStart = 0;
    public int deathIndexEnd = 0;

    public Bitmap image;
    protected int offset;

    public boolean readyToDeath = false;
    private int timerAdd = 0;
    private int lifeAdd = 0;

    public FishObj(Bitmap fishImage, int destX, int destY, int destWidth, int destHeight,
                   int srcX, int srcY, int srcWidth, int srcHeight,
                   int speed, int color, int theta) {
        super(destX, destY, destWidth, destHeight,
                srcX, srcY, srcWidth, srcHeight,
                speed, color, theta);
        this.image = fishImage;
//        this.mActRect
    }

    public void setMaxIndex(int maxIndex) {
        this.maxIndex = maxIndex;
    }

    public void setTouchScore(int touchScore) {
        this.touchScore = touchScore;
    }

    public int getTouchScore() {
        return touchScore;
    }

    public int getArrivalScore() {
        return arrivalScore;
    }

    public void setArrivalScore(int arrivalScore) {
        this.arrivalScore = arrivalScore;
    }

    public void setTimerAdd(int timerAdd) {
        this.timerAdd = timerAdd;
    }

    public int getTimerAdd() {
        return timerAdd;
    }

    public int getLifeAdd() {
        return lifeAdd;
    }

    public void setLifeAdd(int lifeAdd) {
        this.lifeAdd = lifeAdd;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setDeathIndexStart(int deathIndexStart) {
        this.deathIndexStart = deathIndexStart;
    }

    public void setDeathIndexEnd(int deathIndexEnd) {
        this.deathIndexEnd = deathIndexEnd;
    }

    // TODO collision detect
    public boolean smartMoveDown(int destY)
    {
//        moveLeft(100);
        addY(speed);

        // Check if success arrive destination
        if (getY() + halfHeight > destY)
        {
            setY(destY - halfHeight);
            return true;
        }
        else
            return false;
    }

    public boolean moveDown(int destY)
    {

        addY(speed);

        // Check if success arrive destination
        if (getY() + halfHeight > destY)
        {
            setY(destY - halfHeight);
            return true;
        }
        else
            return false;
    }

    protected boolean moveUp(int destY)
    {
        addY(-speed);

        if (getY() + halfHeight < destY)
        {
            setY(destY - halfHeight);
            return true;
        }
        else
            return false;
    }

    protected boolean moveLeft(int destX)
    {
        addX(-speed);

        if (getX() + halfWidth < destX)
        {
            setX(destX - halfWidth);

            return true;
        }
        else
            return false;
    }

    protected boolean moveRight(int destX)
    {
        addX(speed);

        if (getX() + halfWidth > destX)
        {
            setX(destX - halfWidth);

            return true;
        }
        else
            return false;
    }

    public void random(Rect limitRect) {
//        this.mActRect = limitRect;
//        this.moveTo(mActRect.left + r.nextInt(mActRect.width() - this.getWidth()),
//                mActRect.top + r.nextInt(mActRect.height() - this.getHeight()));
    }

    public void random() {
        this.random(this.mActRect);
    }

    public void randomTop() {
        this.moveTo(GameParams.screenRect.left + r.nextInt(GameParams.scaleWidth - srcWidth), GameParams.screenRect.top - srcHeight + 1);
//        this.moveTo(mActRect.left + r.nextInt(mActRect.width() - this.getWidth()), GameParams.screenRect.top - this.getRect().height() + 1);
    }


    protected void FishAnimation(boolean readyToDeath)
    {
        // Set animation frame index
        if (!readyToDeath) {
            index += offset;

            if (index > maxIndex || index < 0)
                index = 0;
        } else {
            if (index < deathIndexStart)
                index = deathIndexStart;
            else
                index++;

            if (index > deathIndexEnd) {
                index = deathIndexEnd;
                // remove this
                isAlive = false;
            }
        }
        setAnimationIndex(col);
    }

    public void recycle() {
        if (image != null) {
            image.recycle();
            image = null;
        }
    }
}
