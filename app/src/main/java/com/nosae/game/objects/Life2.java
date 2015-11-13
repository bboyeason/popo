package com.nosae.game.objects;

/**
 * Created by eason on 2015/10/31.
 */
public class Life2 extends GameObj {
    public int width = 0;
    public int height = 0;
    private int column = 5;
    private static int mLife = 5;
    public Life2(int destX, int destY, int destWidth, int destHeight, int srcX, int srcY, int srcWidth, int srcHeight, int speed, int color, int theta) {
        super(destX, destY, destWidth, destHeight, srcX, srcY, srcWidth, srcHeight, speed, color, theta);

        this.width = srcWidth;
        this.height = srcHeight;
    }

    public static int getLife() {
        return mLife;
    }

    public static void setLife(int mLife) {
        Life2.mLife = mLife;
    }

    public static void addLife(int life) {
        Life2.mLife += life;

        if (Life2.mLife > 5)
            Life2.mLife = 5;
        else if (Life2.mLife < 0)
            Life2.mLife = 0;
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
