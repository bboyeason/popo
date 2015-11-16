package com.nosae.game.objects;

import com.nosae.game.settings.DebugConfig;

import java.util.Random;

/**
 * Created by eason on 2015/10/29.
 */
public class Quiz extends GameObj{

    public class QuizItem {
        public quizColor color;
        public quizSyllable syllable;
    }
    public enum quizColor {red, yellow, blue}
    public enum quizSyllable {Do, Re, Mi, Fa, So}

    private quizColor[] colors = {quizColor.red, quizColor.yellow, quizColor.blue};
    private quizSyllable[] syllables = {quizSyllable.Do, quizSyllable.Re, quizSyllable.Mi, quizSyllable.Fa, quizSyllable.So};

    public static QuizItem quizTable[];
    public static int currentQuiz;
    private static int oldQuiz;
    public static boolean isQuizHit = false;

    public int width = 0;
    public int height = 0;
    public int halfWidth = 0;
    public int halfHeight = 0;

    private int col = 5;
    private static Random mRandom;

    public Quiz(int destX, int destY, int destWidth, int destHeight, int srcX, int srcY, int srcWidth, int srcHeight, int speed, int color, int theta) {
        super(destX, destY, destWidth, destHeight, srcX, srcY, srcWidth, srcHeight, speed, color, theta);

        mRandom = new Random();

        this.width = srcWidth;
        this.height = srcHeight;
        this.halfWidth = this.width >> 1;
        this.halfHeight = this.height >> 1;

        quizTable = new QuizItem[15];
        for (int n = 0; n < quizTable.length; n++) {
            quizTable[n] = new QuizItem();
            quizTable[n].color = colors[n / 5];
            quizTable[n].syllable = syllables[n % 5];
//            DebugConfig.d("quizTable[" + n + "]: " + quizTable[n].color + ", " + quizTable[n].syllable);
        }

        initQuiz();
    }

    private void initQuiz() {
        oldQuiz = mRandom.nextInt(14);
    }

    public void randomQuiz() {
//        if (!isQuizHit)
//            return;
//        isQuizHit = false;
        int quiz = mRandom.nextInt(14);
        currentQuiz = quiz;
        if (currentQuiz != oldQuiz) {
            index = currentQuiz;
            oldQuiz = currentQuiz;
            DebugConfig.d("Current quiz: " + quizTable[index].color + ", " + quizTable[index].syllable);
            setAnimationIndex(col);
        } else {
            DebugConfig.d("Same quiz, random select again.");
            randomQuiz();
        }
    }
}
