package com.nosae.game.objects;

import android.graphics.Color;

import com.nosae.game.popo.GameParams;
import com.nosae.game.popo.Text;

/**
 * Created by eason on 2015/10/27.
 */
public class ColorMask extends GameObj {

    private int startFrame;
    private int delayFrame = 300;
    public Text text;
    private boolean breakStage = false;
    private final static String STRING = "Game Over";

    public ColorMask(int color, int alpha) {
        super(0, 0, GameParams.scaleWidth, GameParams.scaleHeight, color, alpha);

        text = new Text(GameParams.halfWidth - 100, GameParams.halfHeight, 36, STRING, Color.BLACK);
    }

    public ColorMask(int color, int alpha, boolean breakStage) {
        super(0, 0, GameParams.scaleWidth, GameParams.scaleHeight, color, alpha);
        this.breakStage = breakStage;
    }

    public ColorMask(int x, int y, int destWidth, int destHeight, int color, int alpha) {
        super(x, y, destWidth, destHeight, color, alpha);
    }

    public boolean Action(int frameTime) {
        switch (state) {
            case step1:
                if (!isAlive)
                    isAlive = true;
                startFrame = frameTime;
                state = State.step2;

                break;
            case step2:
                if (breakStage)
                    return FadeIn(2, 255);
                else if (FadeIn())
                    state = State.step3;

                break;
            case step3:
                if (FadeOut())
                    state = State.step2;

                if (frameTime - startFrame > delayFrame) {
                    // always display
//                        isAlive = false;
                    return true;
                }

                break;
        }
        return false;
    }

    public boolean FadeIn() {
        return FadeIn(5, 192);
    }

    public boolean FadeIn(int _alpha, int _bound) {
        alpha += _alpha;
        if (alpha > _bound) {
            alpha = _bound;
//                GameParams.playSound(Sound.alert);
            return true;
        }

        paint.setAlpha(alpha);
        return false;
    }

    public boolean FadeOut() {
        alpha -= 5;
        if (alpha < 0) {
            alpha = 0;
            return true;
        }

        paint.setAlpha(alpha);
        return false;
    }
}
