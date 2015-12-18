package com.nosae.game.scene;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Message;
import android.widget.Toast;

import com.nosae.game.popo.Events;
import com.nosae.game.popo.GameEntry;
import com.nosae.game.popo.GameParams;
import com.nosae.game.popo.R;
import com.nosae.game.popo.Text;

import lbs.DrawableGameComponent;

import com.nosae.game.objects.FishCollection;

import com.nosae.game.objects.GameObj;
import com.nosae.game.objects.Life1;
import com.nosae.game.objects.Quiz;
import com.nosae.game.objects.Score;
import com.nosae.game.objects.TimerBar2;
import com.nosae.game.role.Popo;
import com.nosae.game.role.Stage2_fish;
import com.nosae.game.settings.DebugConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by eason on 2015/10/25.
 */
public class Stage2 extends DrawableGameComponent {

    public GameEntry mGameEntry;

    private GameObj mBackground;
    private Bitmap mBackGroundImage;

    private GameObj mSceneTitle;
    private Bitmap mSceneTitleImage;

    private Score mScore;

    private GameObj mLifeIcon;
    private Bitmap mLifeImage;
    private Life1 mLife1;

    public TimerBar2 mTimerBar;
    public Bitmap mTimerBarImage;

    private Popo mPopoObj;
    private Bitmap mPopoImage;

    private GameObj mStaff;

    private Bitmap mStaffImage;

    private Quiz mQuiz;
    private Bitmap mQuizImage;
    public static boolean isQuizHit = true;

    private Text mFpsText;

    int f, j;

    private int[][] mFishTable_1 = {
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

    private final List<OnStageCompleteListener> mOnStageCompleteListeners;

    public void registerOnStageCompleteListener(OnStageCompleteListener listener) {
        synchronized (mOnStageCompleteListeners) {
            if (!mOnStageCompleteListeners.contains(listener))
                mOnStageCompleteListeners.add(listener);
        }
    }

    public void unregisterOnStageCompleteListener(OnStageCompleteListener listener) {
        synchronized (mOnStageCompleteListeners) {
            if (mOnStageCompleteListeners.size() > 0)
                if (mOnStageCompleteListeners.contains(listener))
                    mOnStageCompleteListeners.remove(listener);
        }
    }

    private void NotifyStageCompleted() {
        GameParams.isClearStage2 = true;
        if (mOnStageCompleteListeners.size() > 0) {
            for (OnStageCompleteListener l : mOnStageCompleteListeners) {
                l.OnStageComplete(this);
            }
        }
    }

    public Stage2(GameEntry gameEntry) {
        DebugConfig.d("Stage2 Constructor");
        this.mGameEntry = gameEntry;
        mOnStageCompleteListeners = new ArrayList<>();
        GameParams.msgHandler = new MsgHandler(this);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case Events.CREATE_FISH:
                if (GameParams.isGameOver || GameParams.isClearStage2)
                    return;
                stage2CreateFish(mFishTable_1, mFishTableColor, mFishTableSyllable);

                if (GameParams.onOff) {
                    Message m = new Message();
                    m.what = Events.CREATE_FISH;
                    GameParams.msgHandler.sendMessageDelayed(m, mRandom.nextInt(GameParams.stage2FishRebirthMax) + GameParams.stage2FishRebirthMin);
                }
                break;
            case Events.CREATE_OBJECT:
                if (GameParams.isGameOver || GameParams.isClearStage2)
                    return;

                CreateSpecialObjects(GameParams.specialObjectTable);
                if (GameParams.onOff) {
                    Message m = new Message();
                    m.what = Events.CREATE_OBJECT;
                    GameParams.msgHandler.sendMessageDelayed(m, mRandom.nextInt(5000) + 5000);
                }
                break;
        }
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

        GameParams.stage2TotalScore = 0;
        GameParams.isClearStage2 = false;
    }

    private  void CreateSpecialObjects(int[][] objectTable) {
        stage2CreateFish(objectTable, null, null);
    }

    private void stage2CreateFish(int[][] objectTable, Quiz.quizColor[] colorTable, Quiz.quizSyllable[] SyllableTable) {
        int width, height;
        int speed;
        int random;
        Random mRandom = new Random();
        random = mRandom.nextInt(objectTable[0].length);
        Bitmap fishImage;
        try {
            fishImage = BitmapFactory.decodeResource(GameParams.res, objectTable[0][random]);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            Toast.makeText(mGameEntry.mMainActivity, "OutOfMemoryError!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        width = fishImage.getWidth() / objectTable[1][random];
        height = fishImage.getHeight() / objectTable[2][random];
        DebugConfig.d("width: " + width + ", height: " + height);
        speed = mRandom.nextInt(GameParams.stage2FishRandomSpeed) + GameParams.stage2FishRandomSpeed;
        mFishObj = new Stage2_fish(fishImage, 0, 0, width, height, 0, 0, width, height, (int) (speed * GameParams.density), Color.WHITE, 90);

        mFishObj.randomTop();
        mFishObj.setCol(objectTable[1][random]);
        mFishObj.setMaxIndex(objectTable[3][random]);
        mFishObj.setDeathIndexStart(objectTable[4][random]);
        mFishObj.setDeathIndexEnd(objectTable[5][random]);
        if (colorTable != null && SyllableTable != null) {
            mFishObj.setColor(mFishTableColor[random]);
            mFishObj.setSyllable(mFishTableSyllable[random]);
        } else {
            mFishObj.setTimerAdd(objectTable[8][random]);
            mFishObj.setLifeAdd(objectTable[9][random]);
        }
        mFishObj.isAlive = true;
        mFishCollections.add(mFishObj);
        DebugConfig.d("create fish: " + mFishCollections.size());
    }

    public static void FishGeneration(boolean produce) {
        GameParams.onOff = produce;
        if (GameParams.onOff) {
            Message msg = new Message();
            msg.what = Events.CREATE_FISH;
            GameParams.msgHandler.sendMessageDelayed(msg, 150);

            msg = new Message();
            msg.what = Events.CREATE_OBJECT;
            GameParams.msgHandler.sendMessageDelayed(msg, 5000);
        } else {
            GameParams.msgHandler.removeMessages(Events.CREATE_FISH);
            GameParams.msgHandler.removeMessages(Events.CREATE_OBJECT);
        }
    }

    @Override
    protected void LoadContent() {
        super.LoadContent();
        DebugConfig.d("Stage2 LoadContent()");
        int width, height;

        if (mBackground == null) {
            // Load background image
            mBackGroundImage = BitmapFactory.decodeResource(GameParams.res,
                    R.drawable.b_backimage);
            mBackground = new GameObj(0, 0, GameParams.scaleWidth / mBackGroundImage.getWidth(), GameParams.scaleHeight / mBackGroundImage.getHeight(), 0, 0, 0, 0, 0, 0, 0);
        }
        mBackground.isAlive = true;

        if (mSceneTitle == null) {
            mSceneTitleImage = BitmapFactory.decodeResource(GameParams.res, R.drawable.b_stage_title);
            mSceneTitle = new GameObj(GameParams.halfWidth, (int) (10 * GameParams.density), mSceneTitleImage.getWidth(), mSceneTitleImage.getHeight(), 0, 0, mSceneTitleImage.getWidth(), mSceneTitleImage.getHeight(), 0, 0, 0);
        }

        if (mScore == null)
            mScore = new Score((int) (20 * GameParams.density), (int) (20 * GameParams.density));

        if (mLifeIcon == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            mLifeImage = BitmapFactory.decodeResource(GameParams.res, R.drawable.b_life, options);
            mLifeIcon = new GameObj(mScore.destRect.left, mScore.getY() + mScore.height + (int) (5 * GameParams.density), mLifeImage.getWidth(), mLifeImage.getHeight(), 0, 0, mLifeImage.getWidth(), mLifeImage.getHeight(), 0, 0, 0);
        }

        if (mLife1 == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap numBitmap = BitmapFactory.decodeResource(GameParams.res, R.drawable.s_0, options);
            mLife1 = new Life1(mLifeIcon.destRect.right + (int) (10 * GameParams.density), mLifeIcon.destRect.bottom - mLifeIcon.halfHeight - (numBitmap.getHeight() >> 1), numBitmap.getWidth(), numBitmap.getHeight(), 0, 0, numBitmap.getWidth() * 2, numBitmap.getHeight() * 2);
            numBitmap.recycle();
        }
        Life1.setLife(GameParams.stage2Life);

        if (mTimerBar == null) {
            mTimerBarImage = BitmapFactory.decodeResource(GameParams.res, R.drawable.timer_bar);
            if (mTimerBarImage != null) {
                width = mTimerBarImage.getWidth();
                height = mTimerBarImage.getHeight() / 9;
                mTimerBar = new TimerBar2(mScore.edge_X_right + 5, mScore.getY() + (mLife1.destRect.bottom - mScore.getY()) / 2 - (height >> 1), width, height, 0, 0, width, height, 0, 0, 0);
            }
        }
        if (mTimerBar != null) {
            mTimerBar.setTimer(GameParams.stage2RunningTime);
            mTimerBar.setStartFrame((int) GameEntry.totalFrames);
        }

        if (mPopoObj == null) {
            mPopoImage = BitmapFactory.decodeResource(GameParams.res, R.drawable.popo);
            Bitmap mRule2Image = BitmapFactory.decodeResource(GameParams.res, R.drawable.b_role2);
            mPopoObj = new Popo(mPopoImage, GameParams.halfWidth - mPopoImage.getWidth() / 2, GameParams.scaleHeight - mPopoImage.getHeight(), mPopoImage.getWidth(), mPopoImage.getHeight(), 0, 0, mPopoImage.getWidth(), mPopoImage.getHeight(), 0, 0, 0);
            mPopoObj.role2 = mPopoObj.new Role2(mRule2Image, mPopoObj.getX() - mRule2Image.getWidth(), GameParams.scaleHeight - mRule2Image.getHeight(), mRule2Image.getWidth(), mRule2Image.getHeight(), 0, 0, mRule2Image.getWidth(), mRule2Image.getHeight(), 0, 0, 0);
        }
        mPopoObj.isAlive = true;

        if (mStaff == null) {
            mStaffImage = BitmapFactory.decodeResource(GameParams.res, R.drawable.b_staff);
            mStaff = new GameObj(0, GameParams.scaleHeight - mPopoImage.getHeight() - mStaffImage.getHeight(), GameParams.scaleWidth, mStaffImage.getHeight(), 0, 0, mStaffImage.getWidth(), mStaffImage.getHeight(), 0, 0, 0);
        }

        if (mQuiz == null) {
            mQuizImage = BitmapFactory.decodeResource(GameParams.res, R.drawable.b_quiz);
            width = mQuizImage.getWidth() / 5;
            height = mQuizImage.getHeight() / 3;
            mQuiz = new Quiz(GameParams.halfWidth + GameParams.halfWidth / 2 - width / 2,
                    mPopoObj.destRect.top + mPopoObj.destHeight / 2 - height / 2,
                    width, height, 0, 0, width, height, 0, 0, 0);
        }
        mQuiz.randomQuiz();

        FishGeneration(true);
    }

    @Override
    protected void UnloadContent() {
        super.UnloadContent();
    }

    @Override
    protected void Update() {
        super.Update();
        if (DebugConfig.isFpsDebugOn) {
            mFpsText.message = "actual FPS: " + mGameEntry.actualFPS + " FPS (" + mGameEntry.fps
                    + ") " + (int) GameEntry.totalFrames;
        }

//        if (mPopoObj != null){
            /* FIXME: Role animation */
//        }

        if (mScore != null) {
            mScore.setTotalScore(GameParams.stage2TotalScore);
        }

        if (mLife1 != null) {
            mLife1.updateLife();
            mLife1.action();
            if (Life1.getLife() <= 0)
                GameParams.isGameOver = true;
        }

        if (mTimerBar != null) {
            mTimerBar.action((int) GameEntry.totalFrames);
            if (mTimerBar.isTimeout)
                GameParams.isGameOver = true;
        }

        if (mQuiz != null && isQuizHit) {
            mQuiz.randomQuiz();
            isQuizHit = false;
        }

        for (f = mFishCollections.size() -1 ; f >= 0; f--) {
            mSubFishObj = (Stage2_fish) mFishCollections.get(f);
            mSubFishObj.Animation();
            if (!GameParams.colorMaskBreakStage.isAlive) {
                if (mSubFishObj.smartMoveDown(GameParams.screenRect.height() - mPopoObj.srcHeight)) {
                    mFishCollections.remove(mSubFishObj);
                }
            }
//            if (!mPopoObj.isAlive)
//                isGameOver = true;

            if (!mSubFishObj.isAlive)
                mFishCollections.remove(mSubFishObj);
        }
        if (GameParams.isGameOver || !mPopoObj.isAlive) {
            GameParams.colorMaskGameOver.Action((int) GameEntry.totalFrames);
        } else if (!GameParams.isGameOver && GameParams.stage2TotalScore >= GameParams.stage2BreakScore) {
            if (GameParams.colorMaskBreakStage.state == GameObj.State.step1) {
                FishGeneration(false);
                SharedPreferences settings = mGameEntry.mMainActivity.getSharedPreferences(GameParams.STAGES_COMPLETED, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(GameParams.STAGE2_COMPLETED, true);
                editor.apply();
            }
            if (GameParams.colorMaskBreakStage.Action((int) GameEntry.totalFrames))
                NotifyStageCompleted();
        }
    }

    @Override
    protected void Draw() {
        super.Draw();
        Canvas mSubCanvas;

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

        if (mSceneTitle != null) {
            mSubCanvas.drawBitmap(mSceneTitleImage, mSceneTitle.srcRect, mSceneTitle.destRect, mSceneTitle.paint);
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

        if (mPopoObj != null) {
            mPopoObj.draw(mSubCanvas);
        }

        if (mStaff != null) {
            mSubCanvas.drawBitmap(mStaffImage, mStaff.srcRect, mStaff.destRect, mStaff.paint);
        }

        if (mQuiz != null) {
            mSubCanvas.drawBitmap(mQuizImage, mQuiz.srcRect, mQuiz.destRect, mQuiz.paint);
        }

        for (f = mFishCollections.size() -1 ; f >= 0; f--) {
            mSubFishObj = (Stage2_fish) mFishCollections.get(f);
            if (mFishObj != null && mFishObj.isAlive) {
//                mSubCanvas.save();

//                mSubCanvas.rotate(mSubFishObj.theta - 90, mSubFishObj.getX()
//                        + Aircraft.halfWidth, mSubFishObj.getY()
//                        + Aircraft.halfHeight);
                mSubCanvas.drawBitmap(mSubFishObj.image, mSubFishObj.srcRect,
                        mSubFishObj.destRect, mSubFishObj.paint);

//                mSubCanvas.restore();
            }

        }

        if ((GameParams.isGameOver || !mPopoObj.isAlive) && GameParams.colorMaskGameOver.isAlive)
        {
            mSubCanvas.drawRect(GameParams.colorMaskGameOver.destRect, GameParams.colorMaskGameOver.paint);
            mSubCanvas.drawText(GameParams.colorMaskGameOver.text.message, GameParams.colorMaskGameOver.text.x, mGameEntry.mMainActivity.mRestartButton.getTop() - 30, GameParams.colorMaskGameOver.text.paint);
        } else if (!(GameParams.isGameOver || !mPopoObj.isAlive) && GameParams.colorMaskBreakStage.isAlive) {
            mSubCanvas.drawRect(GameParams.colorMaskBreakStage.destRect, GameParams.colorMaskBreakStage.paint);
        }
    }

    @Override
    public void Dispose() {
        super.Dispose();
        if (mFishCollections != null)
            mFishCollections.clear();
        FishGeneration(false);
    }
}
