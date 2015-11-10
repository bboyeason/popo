package com.nosae.game.objects;

import com.nosae.game.sence.Stage2;

/**
 * Created by eason on 2015/10/31.
 */
public class Life extends GameObj {
    public int width = 0;
    public int height = 0;
    private int column = 5;
    private static int mLife = 5;
    public Life(int destX, int destY, int destWidth, int destHeight, int srcX, int srcY, int srcWidth, int srcHeight, int speed, int color, int theta) {
        super(destX, destY, destWidth, destHeight, srcX, srcY, srcWidth, srcHeight, speed, color, theta);

        this.width = srcWidth;
        this.height = srcHeight;
    }

    public static int getLife() {
        return mLife;
    }

    public static void setLife(int mLife) {
        Life.mLife = mLife;
    }

    public static void addLife(int life) {
        Life.mLife += life;

        if (Life.mLife > 5)
            Life.mLife = 5;
        else if (Life.mLife < 0)
            Life.mLife = 0;
    }

    public void updateLife() {
        // png 1~0
        // index = getLife();

        // png 0~9
        if (getLife() == 0) {
            index = 9;
        } else {
            index = getLife() - 1;
        }
        setAnimationIndex(column);
    }

}
