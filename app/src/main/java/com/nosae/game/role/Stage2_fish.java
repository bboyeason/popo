package com.nosae.game.role;

import android.graphics.Bitmap;

import com.nosae.game.objects.FishObj;
import com.nosae.game.objects.Quiz;

/**
 * Created by eason on 2015/10/31.
 */
public class Stage2_fish extends FishObj {
    public Quiz.quizColor color;
    public Quiz.quizSyllable syllable;
//    public boolean readyToDeath = false;

    public Stage2_fish(Bitmap fishImage, int destX, int destY, int destWidth, int destHeight, int srcX, int srcY, int srcWidth, int srcHeight, int speed, int color, int theta) {
        super(fishImage, destX, destY, destWidth, destHeight, srcX, srcY, srcWidth, srcHeight, speed, color, theta);
    }

    public Quiz.quizColor getColor() {
        return color;
    }

    public void setColor(Quiz.quizColor color) {
        this.color = color;
    }

    public Quiz.quizSyllable getSyllable() {
        return syllable;
    }

    public void setSyllable(Quiz.quizSyllable syllable) {
        this.syllable = syllable;
    }

    public void Animation() {
        if (index == 0)
            offset = 1;
        else if (index == maxIndex)
            offset = -1;
        FishAnimation(readyToDeath);
    }
}
