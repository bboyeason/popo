package com.nosae.game.sence;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.nosae.game.bobo.Events;
import com.nosae.game.bobo.GameEntry;
import com.nosae.game.bobo.GameParams;
import com.nosae.game.bobo.R;
import com.nosae.game.bobo.Text;

import lbs.DrawableGameComponent;
import lbs.FishCollection;

import com.nosae.game.objects.GameObj;
import com.nosae.game.objects.Life1;
import com.nosae.game.objects.Quiz;
import com.nosae.game.objects.Score;
import com.nosae.game.objects.TimerBar2;
import com.nosae.game.role.Bobo;
import com.nosae.game.role.Stage2_fish;
import com.nosae.game.settings.DebugConfig;

import java.util.Random;

/**
 * Created by eason on 2015/10/25.
 */
public class Stage2 extends DrawableGameComponent {

    public static Handler mHandler;
    public static HandlerThread mHandlerThread;
    public static final String THREADNAME = "Stage2_fish_generator";
    public GameEntry mGameEntry;
    private Canvas mSubCanvas;

    private GameObj mBackground;
    private Bitmap mBackGroundImage;

    private GameObj mSenceTitle;
    private Bitmap mSenceTitleImage;

    private Score mScore;

    private GameObj mLifeIcon;
    private Bitmap mLifeImage;
    private Life1 mLife1;

    public TimerBar2 mTimerBar;
    public Bitmap mTimerBarImage;

    private Bobo mBoboObj;
    private Bitmap mBoboImage;

    private Bitmap mRule2Image;
    private GameObj mStaff;

    private Bitmap mStaffImage;

    private Quiz mQuiz;
    private Bitmap mQuizImage;
    public static boolean isQuizHit = true;

    private Text mFpsText;

    int f, j;

    public static boolean isGameOver = false;
    public static boolean isClearStage2 = false;
    public static int mTotalScore;

    private int[][] mFishTable = {
            {
                    R.drawable.b_fish_red_do,
                    R.drawable.b_fish_red_re,
                    R.drawable.b_fish_red_mi,
                    R.drawable.b_fish_red_fa,
                    R.drawable.b_fish_red_so,
                    R.drawable.b_fish_yellow_do,
                    R.drawable.b_fish_yellow_re,
                    R.drawable.b_fish_yellow_mi,
                    R.drawable.b_fish_yellow_fa,
                    R.drawable.b_fish_yellow_so,
                    R.drawable.b_fish_blue_do,
                    R.drawable.b_fish_blue_re,
                    R.drawable.b_fish_blue_mi,
                    R.drawable.b_fish_blue_fa,
                    R.drawable.b_fish_blue_so
            },
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, /* Animation column */
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, /* Animation row */
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, /* Max index */
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, /* Death animation start */
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, /* Death animation end */
    };
    /* Color, red:0, yellow:1, blue:2 */
    private Quiz.quizColor mFishTableColor[] = {
            Quiz.quizColor.red, Quiz.quizColor.red, Quiz.quizColor.red, Quiz.quizColor.red, Quiz.quizColor.red,
            Quiz.quizColor.yellow, Quiz.quizColor.yellow, Quiz.quizColor.yellow, Quiz.quizColor.yellow, Quiz.quizColor.yellow,
            Quiz.quizColor.blue, Quiz.quizColor.blue, Quiz.quizColor.blue, Quiz.quizColor.blue, Quiz.quizColor.blue
    };
    /* Syllable */
    private Quiz.quizSyllable mFishTableSyllable[] = {
            Quiz.quizSyllable.Do, Quiz.quizSyllable.Re, Quiz.quizSyllable.Mi, Quiz.quizSyllable.Fa, Quiz.quizSyllable.So,
            Quiz.quizSyllable.Do, Quiz.quizSyllable.Re, Quiz.quizSyllable.Mi, Quiz.quizSyllable.Fa, Quiz.quizSyllable.So,
            Quiz.quizSyllable.Do, Quiz.quizSyllable.Re, Quiz.quizSyllable.Mi, Quiz.quizSyllable.Fa, Quiz.quizSyllable.So
    };

    private Stage2_fish mFishObj;
    private Stage2_fish mSubFishObj;
    public static FishCollection mFishCollections;
    private Random mRandom;
    public static boolean onOff = true;

    public Stage2(GameEntry gameEntry) {
        DebugConfig.d("Stage2 Constructor");
        this.mGameEntry = gameEntry;
    }

    @Override
    public void Dispose() {
        super.Dispose();
    }

    @Override
    protected void Initialize() {
        super.Initialize();
        DebugConfig.d("Stage2 Initialize()");
        if (DebugConfig.isFpsDebugOn) {
            mFpsText = new Text(GameParams.halfWidth - 50, 20, 12, "FPS", Color.BLUE);
        }

        mFishCollections = new FishCollection();
        mRandom = new Random();
        mTotalScore = 0;

        if (mHandlerThread == null) {
            mHandlerThread = new HandlerThread(THREADNAME,
                    android.os.Process.THREAD_PRIORITY_BACKGROUND);
            mHandlerThread.start();
//            mHandlerThread.setDaemon(true);
            DebugConfig.d("Create thread: " + THREADNAME);
        }
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Events.CREATEFISH:
                        if (isGameOver || isClearStage2)
                            return;
                        stage2CreateFish();

                        if (onOff) {
                            Message m = new Message();
                            m.what = Events.CREATEFISH;
                            // TODO msg.obj = something;
                            //if (msg.obj != null) {
                            mHandler.sendMessageDelayed(m, mRandom.nextInt(GameParams.stage2FishRebirthMax) + GameParams.stage2FishRebirthMin);
                            //}
                        }
                        break;

                }
            }
        };
    }

    private void stage2CreateFish() {
        int width, height;
        int speed;
        int random;
        Random mRandom = new Random();
        random = mRandom.nextInt(mFishTable[0].length);
        Bitmap fishImage = GameParams.decodeSampledBitmapFromResource(mFishTable[0][random], 60, 60);

        width = fishImage.getWidth() / mFishTable[1][random];
        height = fishImage.getHeight() / mFishTable[2][random];
        DebugConfig.d("width: " + width + ", height: " + height);
        speed = mRandom.nextInt(GameParams.stage2FishRandomSpeed) + GameParams.stage2FishRandomSpeed;
        mFishObj = new Stage2_fish(fishImage, 0, 0, width, height, 0, 0, width, height, speed, Color.WHITE, 90);

        mFishObj.randomTop();
        mFishObj.setCol(mFishTable[1][random]);
        mFishObj.setMaxIndex(mFishTable[3][random]);
        mFishObj.setDeathIndexStart(mFishTable[4][random]);
        mFishObj.setDeathIndexEnd(mFishTable[5][random]);
//        mFishObj.setTouchScore(mFishTable[6][random]);
//        mFishObj.setTimerAdd(mFishTable[7][random]);
        mFishObj.setColor(mFishTableColor[random]);
        mFishObj.setSyllable(mFishTableSyllable[random]);
        mFishObj.isAlive = true;
        mFishCollections.add(mFishObj);
        DebugConfig.d("create fish: " + mFishCollections.size());
    }

    private void FishGeneration() {
        onOff = true;
        Message msg = new Message();
        msg.what = Events.CREATEFISH;
        mHandler.sendMessage(msg);
    }

    @Override
    protected void LoadContent() {
        super.LoadContent();
        DebugConfig.d("Stage2 LoadContent()");
        int width, height;

        if (mBackground == null) {
            // Load background image
            mBackGroundImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res,
                    R.drawable.b_backimage);
            mBackground = new GameObj(0, 0, GameParams.scaleWidth / mBackGroundImage.getWidth(), GameParams.scaleHeight / mBackGroundImage.getHeight(), 0, 0, 0, 0, 0, 0, 0);
            mBackground.isAlive = true;
        }

        if (mSenceTitle == null) {
            mSenceTitleImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res, R.drawable.b_stage_title);
            mSenceTitle = new GameObj(GameParams.halfWidth, (int) (10 * GameParams.density), mSenceTitleImage.getWidth(), mSenceTitleImage.getHeight(), 0, 0, mSenceTitleImage.getWidth(), mSenceTitleImage.getHeight(), 0, 0, 0);
        }

        if (mScore == null)
            mScore = new Score((int) (20 * GameParams.density), (int) (20 * GameParams.density));

        if (mLifeIcon == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            mLifeImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res, R.drawable.b_life, options);
            mLifeIcon = new GameObj(mScore.destRect.left, mScore.getY() + mScore.height + (int) (5 * GameParams.density), mLifeImage.getWidth(), mLifeImage.getHeight(), 0, 0, mLifeImage.getWidth(), mLifeImage.getHeight(), 0, 0, 0);
        }

        if (mLife1 == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap numBitmap = (Bitmap) BitmapFactory.decodeResource(GameParams.res, R.drawable.s_0, options);
            mLife1 = new Life1(mLifeIcon.destRect.right + (int) (10 * GameParams.density), mLifeIcon.destRect.bottom - mLifeIcon.halfHeight - (numBitmap.getHeight() >> 1), numBitmap.getWidth(), numBitmap.getHeight(), 0, 0, numBitmap.getWidth(), numBitmap.getHeight());
            Life1.setLife(5);
            numBitmap.recycle();
            numBitmap = null;
        }

        if (mTimerBar == null) {
            mTimerBarImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res, R.drawable.timer_bar);
            if (mTimerBarImage != null) {
                width = mTimerBarImage.getWidth() / 1;
                height = mTimerBarImage.getHeight() / 9;
                mTimerBar = new TimerBar2(mLife1.destRect.right + 10, mScore.getY() + (mLife1.destRect.bottom - mScore.getY()) / 2 - (height >> 1), width, height, 0, 0, width, height, 0, 0, 0);
                if (mTimerBar != null)
                    mTimerBar.setStartFrame((int) GameEntry.totalFrames);
            }
        }
        if (mTimerBar != null) {
            mTimerBar.setTimer(60);
        }

        if (mBoboObj == null) {
            mBoboImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res, R.drawable.b_role1);
            mRule2Image = (Bitmap) BitmapFactory.decodeResource(GameParams.res, R.drawable.b_role2);
            mBoboObj = new Bobo(mBoboImage, GameParams.halfWidth - mBoboImage.getWidth() / 2, GameParams.scaleHeight - mBoboImage.getHeight(), mBoboImage.getWidth(), mBoboImage.getHeight(), 0, 0, mBoboImage.getWidth(), mBoboImage.getHeight(), 0, 0, 0);
            mBoboObj.role2 = mBoboObj.new Role2(mRule2Image, mBoboObj.getX() - mRule2Image.getWidth(), GameParams.scaleHeight - mRule2Image.getHeight(), mRule2Image.getWidth(), mRule2Image.getHeight(), 0, 0, mRule2Image.getWidth(), mRule2Image.getHeight(), 0, 0, 0);
        }

        if (mStaff == null) {
            mStaffImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res, R.drawable.b_staff);
            mStaff = new GameObj(0, GameParams.scaleHeight - mBoboImage.getHeight() - mStaffImage.getHeight(), GameParams.scaleWidth, mStaffImage.getHeight(), 0, 0, mStaffImage.getWidth(), mStaffImage.getHeight(), 0, 0, 0);
        }

        if (mQuiz == null) {
            mQuizImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res, R.drawable.b_quiz);
            width = mQuizImage.getWidth() / 5;
            height = mQuizImage.getHeight() / 3;
            mQuiz = new Quiz(GameParams.halfWidth + GameParams.halfWidth / 2 - width / 2,
                    mBoboObj.destRect.top + mBoboObj.destHeight / 2 - height / 2,
                    width, height, 0, 0, width, height, 0, 0, 0);
            if (mQuiz != null)
                mQuiz.randomQuiz();
        }

        FishGeneration();
    }

    @Override
    protected void UnloadContent() {
        super.UnloadContent();
    }

    @Override
    protected void Update() {
        super.Update();
        if (DebugConfig.isFpsDebugOn) {
            mFpsText.message = "actual FPS: " + (int) mGameEntry.actualFPS + " FPS (" + (int) mGameEntry.fps
                    + ") " + (int) mGameEntry.totalFrames;
        }

        if (mBoboObj != null){
            /* FIXME: Role animation */
        }

        if (mScore != null) {
            mScore.setTotalScore(mTotalScore);
        }

        if (mLife1 != null) {
            mLife1.updateLife();
            if (Life1.getLife() <= 0)
                isGameOver = true;
        }

        if (mTimerBar != null) {
            mTimerBar.action((int) GameEntry.totalFrames);
            if (mTimerBar.isTimeout)
                isGameOver = true;
        }

        if (mQuiz != null && isQuizHit) {
            mQuiz.randomQuiz();
            isQuizHit = false;
        }

        for (f = mFishCollections.size() -1 ; f >= 0; f--) {
            mSubFishObj = (Stage2_fish) mFishCollections.get(f);
            mSubFishObj.Animation();
            if (mSubFishObj.smartMoveDown(GameParams.screenRect.height() - mBoboObj.srcHeight)) {
                mFishCollections.remove(mSubFishObj);
            }
//            if (!mBoboObj.isAlive)
//                isGameOver = true;

            if (!mSubFishObj.isAlive)
                mFishCollections.remove(mSubFishObj);
        }
    }

    @Override
    protected void Draw() {
        super.Draw();
        mSubCanvas = mGameEntry.canvas;

        mSubCanvas = mGameEntry.canvas;
        // Draw background image
        if (mBackground.isAlive) {
            for (f = 0; f <= mBackground.destWidth; f++) {
                for (j = -1; j <= mBackground.destHeight; j++) {
                    mSubCanvas.drawBitmap(mBackGroundImage,
                            f * mBackGroundImage.getWidth() + mBackground.destRect.left,
                            j * mBackGroundImage.getHeight() + mBackground.destRect.top,
                            null);
                }
            }
        }

        if (DebugConfig.isFpsDebugOn) {
            mSubCanvas.drawText(mFpsText.message, mFpsText.x, mFpsText.y, mFpsText.paint);
        }

        if (mSenceTitle != null) {
            mSubCanvas.drawBitmap(mSenceTitleImage, mSenceTitle.srcRect, mSenceTitle.destRect, mSenceTitle.paint);
        }

        if (mScore != null)
            mScore.drawScore(mSubCanvas);

        if (mLifeIcon != null) {
            mSubCanvas.drawBitmap(mLifeImage, mLifeIcon.srcRect, mLifeIcon.destRect, mLifeIcon.paint);
        }

        if (mLife1 != null) {
            mLife1.draw(mSubCanvas);
        }

        if (mTimerBar != null) {
            mSubCanvas.drawBitmap(mTimerBarImage, mTimerBar.srcRect, mTimerBar.destRect, null);
        }

        if (mBoboObj != null) {
            mBoboObj.draw(mSubCanvas);
        }

        if (mStaff != null) {
            mSubCanvas.drawBitmap(mStaffImage, mStaff.srcRect, mStaff.destRect, mStaff.paint);
        }

        if (mQuiz != null) {
            mSubCanvas.drawBitmap(mQuizImage, mQuiz.srcRect, mQuiz.destRect, mQuiz.paint);
        }

        for (f = mFishCollections.size() -1 ; f >= 0; f--) {
            mSubFishObj = (Stage2_fish) mFishCollections.get(f);
            if (mFishObj.isAlive) {
//                mSubCanvas.save();

//                mSubCanvas.rotate(mSubFishObj.theta - 90, mSubFishObj.getX()
//                        + Aircraft.halfWidth, mSubFishObj.getY()
//                        + Aircraft.halfHeight);
                mSubCanvas.drawBitmap(mSubFishObj.image, mSubFishObj.srcRect,
                        mSubFishObj.destRect, mSubFishObj.paint);

//                mSubCanvas.restore();
            }

        }
    }
}
