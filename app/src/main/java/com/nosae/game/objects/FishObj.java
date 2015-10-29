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
    private int score = 0;
    private int col = 1;// 1: Not animation, 2~: Animation
    public int maxIndex = 0;// Animation frame count
    public int deathIndexStart = 0;
    public int deathIndexEnd = 0;

    public Bitmap image;
    protected int offset;
    public boolean flipRight = false;
    public boolean flipLeft = true;
    public boolean readyToDeath = false;
    private int timerAdd = 0;

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

    public void setScore(int score) {
        this.score = score;
    }

    public void setTimerAdd(int timerAdd) {
        this.timerAdd = timerAdd;
    }

    public int getTimerAdd() {
        return timerAdd;
    }

    public int getScore() {
        return score;
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
        // ���W
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
    // ������V�U����
    public boolean moveDown(int destY)
    {

        // ���W
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

    // ������V�W����
    protected boolean moveUp(int destY)
    {
        // ����
        addY(-speed);

        // �O�_��F�ت��a
        if (getY() + halfHeight < destY)
        {
            setY(destY - halfHeight);
            return true;
        }
        else
            return false;
    }

    // �V������
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

    // �V�k����
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
        setAnimationIndex(col);
        if (!readyToDeath) {
            if (index >= 0)
                index++;
            if (index > maxIndex || index < 0)
                index = 0;
        } else {
            if (index < deathIndexStart)
                index = deathIndexStart;
            else
                index++;

            if (index == deathIndexEnd) {
                // remove this
                isAlive = false;
            }
        }
//        if (index == 0) {
//            flipRight = true;
//            flipLeft = false;
//        } else if (index >= maxIndex) {
//            flipRight = false;
//            flipLeft = true;
//        }
//        if (flipRight) {
//            index++;
//        } else if (flipLeft) {
//            index--;
//        }

        // �C�C�^�_�������Aoffset��e�氵�W��
/*
        if (offset == 0)
        {
            if (index > 10)
                index--;
            else if (index < 10)
                index++;
        }
        else
            index += offset;

        // �k�s
        offset = 0;

        // ����ʵe���޶W�X�d��
        if (index < 0)
            index = 0;
        else if (index > maxIndex)
            index = maxIndex;
*/
    }

//    public FishObj(Drawable drawable, Rect mActRect) {
//        super(drawable);
//        this.mActRect = mActRect;
//    }

 /*   public void setScore(int n) {
        score = n;
    }

    public int getScore() {
        return score;
    }
    *//**
     * 物件移動到隨機區域
     *//*


    public void randomSpeed() {
        y_Speed = r.nextInt(GameParams.fishRandomSpeed) + GameParams.fishRandomSpeed;
    }*/

    /**
     * Set speed for specific fish
     * @param speed
     */
/*    public void speed(int speed) {
        y_Speed = speed;
    }

    public void update() {
        updateMove();
    }

    private void updateMove() {
        this.moveTo(mActRect.left + 100, mActRect.top);
    }

    public void Animation()
    {
        setAnimationIndex(GameObj.srcCol);

        if (index >= 0 && index < maxIndex)
            index++;
        else
            index--;
//            isAlive = false;
        DebugConfig.d("index: " + index + ", " + GameObj.srcCol + ", " + GameObj.srcRow);

    }*/

/*    public static int getCol() {
        return col;
    }

    public void setCol(int col) {
        FishObj.col = col;
    }*/
/*
    public void setSrcCol(int col) {
        GameObj.srcCol = col;
    }

    public void setSrcRow(int row) {
        GameObj.srcRow = row;
    }*/
}
