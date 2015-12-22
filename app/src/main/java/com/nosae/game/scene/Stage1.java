package com.nosae.game.scene;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.*;
import android.widget.Toast;

import com.nosae.game.popo.Events;
import com.nosae.game.popo.GameEntry;
import com.nosae.game.popo.GameParams;
import com.nosae.game.popo.R;
import com.nosae.game.popo.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lbs.DrawableGameComponent;
import com.nosae.game.objects.FishCollection;

import com.nosae.game.objects.Life1;
import com.nosae.game.role.Popo;
import com.nosae.game.objects.GameObj;
import com.nosae.game.role.NormalFish;
import com.nosae.game.objects.Score;
import com.nosae.game.objects.TimerBar2;
import com.nosae.game.settings.DebugConfig;

/**
 * Created by eason on 2015/10/19.
 */
public class Stage1 extends DrawableGameComponent {
    public GameEntry mGameEntry;

    private GameObj mBackground;
    private Bitmap mBackGroundImage;

    private GameObj mSceneTitle;
    private Bitmap mSceneTitleImage;

    private NormalFish mSubFishObj;

    public Popo mPopoObj;

    private Text mFpsText;

    private Score mScore;

    public TimerBar2 mTimerBar;
    public Bitmap mTimerBarImage;

    private GameObj mLifeIcon;
    private Bitmap mLifeImage;
    private Life1 mLife1;

    public static FishCollection mFishCollections;

    private int f;
    private Random mRandom;
    private int[][] mFishTable1 = {
            {
                    R.drawable.a_fish_01,
                    R.drawable.a_fish_01,
                    R.drawable.a_fish_hamburger,
                    R.drawable.a_fish_hotdog,
                    R.drawable.a_fish_donut
            },
            {  3,  3, 10, 10, 10 }, /* Animation column */
            {  2,  2,  2,  2,  2 }, /* Animation row */
            {  5,  5,  0,  0,  0 }, /* Max index */
            {  5,  5,  1,  1,  1 }, /* Death animation start */
            {  5,  5, 18, 18, 18 }, /* Death animation end */
            { -1, -1, 10, 20, 30 }, /* Touch Score */
            { 10, 20, -1, -2, -2 }, /* Arrival Score */
            {  0,  0,  0,  0,  0 }, /* Timer add (seconds) */
            {  0,  0,  0,  0,  0 } /* Life add */
    };

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
        GameParams.isClearStage1 = true;
        if (mOnStageCompleteListeners.size() > 0) {
            for (OnStageCompleteListener l : mOnStageCompleteListeners) {
                l.OnStageComplete(this);
            }
        }
    }

    public Stage1(GameEntry mGameEntry) {
        DebugConfig.d("Stage1 Constructor");
        this.mGameEntry = mGameEntry;
        mOnStageCompleteListeners = new ArrayList<>();
        GameParams.msgHandler = new MsgHandler(this);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case Events.CREATE_FISH:
                if (GameParams.isGameOver || GameParams.isClearStage1)
                    return;
                createFish(mFishTable1);

                if (GameParams.onOff) {
                    Message m = new Message();
                    m.what = Events.CREATE_FISH;
                    GameParams.msgHandler.sendMessageDelayed(m, mRandom.nextInt(GameParams.stage1FishRebirthMax) + GameParams.stage1FishRebirthMin);
                }
                break;
            case Events.CREATE_OBJECT:
                if (GameParams.isGameOver || GameParams.isClearStage1)
                    return;

                createFish(GameParams.specialObjectTable);
                if (GameParams.onOff) {
                    Message m = new Message();
                    m.what = Events.CREATE_OBJECT;
                    GameParams.msgHandler.sendMessageDelayed(m, mRandom.nextInt(5000) + 5000);
                }
                break;
            case Events.CREATE_CAKE:
                break;
        }
    }

    @Override
    protected void Initialize() {
        DebugConfig.d("Stage1 Initialize()");
        mFishCollections = new FishCollection();
        mRandom = new Random();

        GameParams.stage1TotalScore = 0;
        GameParams.isClearStage1 = false;
        super.Initialize();
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

    protected void createFish(int[][] fishTable) {
        int width, height;
        int speed;
        int random;
        random = mRandom.nextInt(fishTable[0].length);
        Bitmap fishImage;
        try {
            fishImage = BitmapFactory.decodeResource(GameParams.res, fishTable[0][random]);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            Toast.makeText(mGameEntry.mMainActivity, "OutOfMemoryError!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        width = fishImage.getWidth() / fishTable[1][random];
        height = fishImage.getHeight() / fishTable[2][random];
        DebugConfig.d("Image ID: " + random + "=> width: " + width + ", height: " + height);

        speed = mRandom.nextInt(GameParams.stage1FishRandomSpeed) + GameParams.stage1FishRandomSpeed;
        NormalFish mFishObj = new NormalFish(fishImage, 0, 0, width, height, 0, 0, width, height, (int) (speed * GameParams.density), Color.WHITE, 90);

        mFishObj.randomTop();
        mFishObj.setCol(fishTable[1][random]);
        mFishObj.setMaxIndex(fishTable[3][random]);
        mFishObj.setDeathIndexStart(fishTable[4][random]);
        mFishObj.setDeathIndexEnd(fishTable[5][random]);
        mFishObj.setTouchScore(fishTable[6][random]);
        mFishObj.setArrivalScore(fishTable[7][random]);
        mFishObj.setTimerAdd(fishTable[8][random]);
        mFishObj.setLifeAdd(fishTable[9][random]);
        mFishObj.isAlive = true;
        mFishCollections.add(mFishObj);
        DebugConfig.d("create fish: " + mFishCollections.size());
    }

    @Override
    protected void LoadContent() {
        super.LoadContent();
        DebugConfig.d("Stage1 LoadContent()");
        int width, height;

        if (mBackground == null) {
            // Load background image
            mBackGroundImage = GameParams.decodeSampledBitmapFromResource(R.drawable.a_background, GameParams.scaleWidth, GameParams.scaleHeight);
            mBackground = new GameObj(0, 0, GameParams.scaleWidth, GameParams.scaleHeight, 0, 0, mBackGroundImage.getWidth(), mBackGroundImage.getHeight(), 0, 0, 0);
        }
        mBackground.isAlive = true;
        // Random fish generator
        FishGeneration(true);

        if (mSceneTitle == null) {
            mSceneTitleImage = BitmapFactory.decodeResource(GameParams.res, R.drawable.a_stage_title);
            mSceneTitle = new GameObj(GameParams.halfWidth, (int) (10 * GameParams.density), mSceneTitleImage.getWidth(), mSceneTitleImage.getHeight(), 0, 0, mSceneTitleImage.getWidth(), mSceneTitleImage.getHeight(), 0, 0, 0);
        }

        if (mPopoObj == null) {
            Bitmap mPopoImage = BitmapFactory.decodeResource(GameParams.res,
                    R.drawable.bobo);
            width = mPopoImage.getWidth() / 4;
            height = mPopoImage.getHeight() / 5;
            Popo.width = width;
            Popo.height = height;
            Popo.halfWidth = width >> 1;
            Popo.halfHeight = height >> 1;
            mPopoObj = new Popo(mPopoImage, GameParams.halfWidth - width/2, GameParams.scaleHeight - height, width, height, 0, 0, width, height, 0, Color.WHITE, 90);
        }
        mPopoObj.setCol(4);
        mPopoObj.setMaxIndex(20);
        mPopoObj.isAlive = true;

        if (DebugConfig.isFpsDebugOn) {
            mFpsText = new Text(GameParams.halfWidth - 50, 100, 12, "FPS", Color.BLUE);
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
        Life1.setLife(GameParams.stage1Life);

        if (mTimerBar == null) {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 4;
//            mTimerBarImage = GameParams.decodeSampledBitmapFromResource(R.drawable.timer_bar, (int) (GameParams.density * GameParams.halfWidth), (int) (GameParams.density * 120));
            mTimerBarImage = BitmapFactory.decodeResource(GameParams.res, R.drawable.timer_bar);
            if (mTimerBarImage != null) {
                width = mTimerBarImage.getWidth();
                height = mTimerBarImage.getHeight() / 9;
                mTimerBar = new TimerBar2(mScore.edge_X_right + 5, mScore.getY() + (mLife1.destRect.bottom - mScore.getY()) / 2 - (height >> 1), width, height, 0, 0, width, height, 0, 0, 0);
            }
        }
        if (mTimerBar != null) {
            mTimerBar.setTimer(GameParams.stage1RunningTime);
            mTimerBar.setStartFrame((int) GameEntry.totalFrames);
        }
    }

    @Override
    protected void UnloadContent() {
        DebugConfig.d("Stage1 UnloadContent()");

        super.UnloadContent();
    }

    @Override
    protected void Update() {
//        DebugConfig.d("Stage1 Update()");
//        if (mBackground.isAlive) {
            //TODO maybe background rolling
//        }

//        if (mPopoObj != null) {
//            mPopoObj.Animation();
//        }

//        DebugConfig.d("size " + mFishCollections.size());
        for (f = mFishCollections.size() -1 ; f >= 0; f--) {
            mSubFishObj = (NormalFish) mFishCollections.get(f);
            mSubFishObj.Animation();
            if (!GameParams.colorMaskBreakStage.isAlive) {
                if (mSubFishObj.smartMoveDown(GameParams.screenRect.height() - mPopoObj.srcHeight)) {
//                DebugConfig.d("Arrive screen bottom, remove it.");
                    if (!GameParams.isGameOver) {
                        if (mSubFishObj.getArrivalScore() > 0) {
                            GameParams.stage1TotalScore += mSubFishObj.getArrivalScore();
                        } else if (mSubFishObj.getArrivalScore() < 0) {
                            Life1.addLife(mSubFishObj.getArrivalScore());
                        }
                    }
                    mFishCollections.remove(mSubFishObj);
                    mSubFishObj.recycle();
                }
            }
            if (!mSubFishObj.isAlive) {
                mFishCollections.remove(mSubFishObj);
                mSubFishObj.recycle();
            }
        }
        if (GameParams.stage1TotalScore < 0 || Life1.getLife() <= 0) {
//            GameParams.stage1TotalScore = 0;
            mPopoObj.isAlive = false;
        }
        if (!mPopoObj.isAlive)
            GameParams.isGameOver = true;

        if (DebugConfig.isFpsDebugOn) {
            mFpsText.message = "actual FPS: " + mGameEntry.actualFPS + " FPS (" + mGameEntry.fps
                    + ") " + (int) GameEntry.totalFrames;
        }
        if (mScore != null)
            mScore.setTotalScore(GameParams.stage1TotalScore);

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

        if (GameParams.isGameOver || !mPopoObj.isAlive) {
            GameParams.colorMaskGameOver.Action((int) GameEntry.totalFrames);
        } else if (!(GameParams.isGameOver || !mPopoObj.isAlive) && GameParams.stage1TotalScore >= GameParams.stage1BreakScore) {
            if (GameParams.colorMaskBreakStage.state == GameObj.State.step1) {
                FishGeneration(false);
                SharedPreferences settings = mGameEntry.mMainActivity.getSharedPreferences(GameParams.STAGES_COMPLETED, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(GameParams.STAGE1_COMPLETED, true);
                editor.apply();
            }
            if (GameParams.colorMaskBreakStage.Action((int) GameEntry.totalFrames))
                NotifyStageCompleted();
        }
        super.Update();
    }

    @Override
    protected void Draw() {
        Canvas mSubCanvas = mGameEntry.canvas;
        if (mBackground.isAlive)
        {
            mSubCanvas.drawBitmap(mBackGroundImage,
                    mBackground.srcRect,
                    mBackground.destRect,
                    null);
/*
            for (f = 0; f <= mBackground.destWidth; f++)
            {
                for (j = -1; j <= mBackground.destHeight; j++)
                {
                    mSubCanvas.drawBitmap(mBackGroundImage,
                            f * mBackGroundImage.getWidth() + mBackground.destRect.left,
                            j * mBackGroundImage.getHeight() + mBackground.destRect.top,
                            null);
                }
            }
*/
        }

        if (mSceneTitle != null) {
            mSubCanvas.drawBitmap(mSceneTitleImage, mSceneTitle.srcRect, mSceneTitle.destRect, mSceneTitle.paint);
        }

        for (f = mFishCollections.size() -1 ; f >= 0; f--) {
            mSubFishObj = (NormalFish) mFishCollections.get(f);
            if (mSubFishObj.isAlive) {
                mSubCanvas.drawBitmap(mSubFishObj.image, mSubFishObj.srcRect,
                        mSubFishObj.destRect, mSubFishObj.paint);
            }

        }

//        if (mPopoObj != null && mPopoObj.isAlive) {
//            mSubCanvas.drawBitmap(mPopoObj.popoImage, mPopoObj.srcRect, mPopoObj.destRect,
//                    mPopoObj.paint);
//            DebugConfig.d("srcRect: " + mGoldenFishObj.srcRect.left + ", " + mGoldenFishObj.srcRect.top + ", " + mGoldenFishObj.srcRect.right + ", " + mGoldenFishObj.srcRect.bottom);
//            DebugConfig.d("destRect: " + mGoldenFishObj.destRect.left + ", " + mGoldenFishObj.destRect.top + ", " + mGoldenFishObj.destRect.right + ", " + mGoldenFishObj.destRect.bottom);
//        }

        if (DebugConfig.isFpsDebugOn) {
            mSubCanvas.drawText(mFpsText.message, mFpsText.x, mFpsText.y, mFpsText.paint);
        }

        if (mScore != null)
            mScore.drawScore(mSubCanvas);

        if (mLifeIcon != null) {
            mSubCanvas.drawBitmap(mLifeImage, mLifeIcon.srcRect, mLifeIcon.destRect, mLifeIcon.paint);
        }

        if (mLife1 != null) {
//            mSubCanvas.drawBitmap(mLifeNumber, mLife2.srcRect, mLife2.destRect, mLife2.paint);
            mLife1.draw(mSubCanvas);
        }

        if (mTimerBar != null) {
            mSubCanvas.drawBitmap(mTimerBarImage, mTimerBar.srcRect, mTimerBar.destRect, null);
        }

        if ((GameParams.isGameOver || !mPopoObj.isAlive) && GameParams.colorMaskGameOver.isAlive)
        {
            mSubCanvas.drawRect(GameParams.colorMaskGameOver.destRect, GameParams.colorMaskGameOver.paint);
            mSubCanvas.drawText(GameParams.colorMaskGameOver.text.message, GameParams.colorMaskGameOver.text.x, mGameEntry.mMainActivity.mRestartButton.getTop() - 30, GameParams.colorMaskGameOver.text.paint);
        } else if (!(GameParams.isGameOver || !mPopoObj.isAlive) && GameParams.colorMaskBreakStage.isAlive) {
            mSubCanvas.drawRect(GameParams.colorMaskBreakStage.destRect, GameParams.colorMaskBreakStage.paint);
        }
        super.Draw();
    }

    @Override
    public void Dispose() {
        DebugConfig.d("Stage1 Dispose()");
        //TODO stage switch animation
        super.Dispose();
        if (mFishCollections != null)
            mFishCollections.clear();
        FishGeneration(false);
    }
}
