package com.nosae.game.scene;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.widget.Toast;

import com.nosae.game.bobo.Events;
import com.nosae.game.bobo.GameEntry;
import com.nosae.game.bobo.GameParams;
import com.nosae.game.bobo.R;
import com.nosae.game.bobo.Text;
import com.nosae.game.objects.FishCollection;
import com.nosae.game.objects.FishObj;
import com.nosae.game.objects.GameObj;
import com.nosae.game.objects.Life1;
import com.nosae.game.objects.Score;
import com.nosae.game.objects.TimerBar2;
import com.nosae.game.role.NormalFish;
import com.nosae.game.settings.DebugConfig;

import java.util.Random;

import lbs.DrawableGameComponent;

/**
 * Created by eason on 2015/11/9.
 */
public class Stage3 extends DrawableGameComponent {

    public static Handler mHandler;
    public static HandlerThread mHandlerThread;
    public static final String THREADNAME = "Stage3_object_generator";

    private final GameEntry mGameEntry;

    private GameObj mBackground;
    private Bitmap mBackGroundImage;

    private GameObj mSceneTitle;
    private Bitmap mSceneTitleImage;

    private Score mScore;

    private Text mFpsText;

    public TimerBar2 mTimerBar;
    public Bitmap mTimerBarImage;

    private GameObj mLifeIcon;
    private Bitmap mLifeImage;
    private Life1 mLife1;

    public static boolean isGameOver = false;
    public static boolean onOff;
    private Random mRandom;

    private NormalFish mObj;
    private NormalFish mSubObj;
    public static FishCollection mObjCollections;
    private int[][] mObjTable_1 = {
            {
                    R.drawable.s_0,
                    R.drawable.s_1,
                    R.drawable.s_2,
                    R.drawable.s_3,
                    R.drawable.s_4,
                    R.drawable.s_5,
                    R.drawable.s_6,
                    R.drawable.s_7,
                    R.drawable.s_8,
                    R.drawable.s_9,
            },
            { 0, 10, 10, 10, 20, 20 ,20, 0, 0, 0 } // Score
    };

    private int[][] mObjTable_2 = {
            {
                    R.drawable.sea_star,
                    R.drawable.jellyfish
            },
            { 10, 10 }, /* Animation column */
            {  3,  3 }, /* Animation row */
            {  0,  0 },  /* Max index */
            {  1,  1 }, /* Death animation start */
            { 27, 27 }, /* Death animation end */
            {  0,  0 }, /* Touch Score */
            {  0,  0 }, /* Arrival Score */
            { 10,  0 }, /* Timer add (seconds) */
            {  0,  1 } /* Life add */
    };
    private Rect mLimitRect;

    public Stage3(GameEntry gameEntry) {
        DebugConfig.d("Stage3 Constructor");
        this.mGameEntry = gameEntry;
    }

    public void CreateObj(int[][] objectTable) {
        int width, height;
        Bitmap objImage;

        for (int i = 0; i < objectTable[0].length; i++) {
            try {
                objImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res, objectTable[0][i]);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                Toast.makeText(mGameEntry.mMainActivity, "OutOfMemoryError!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            width = objImage.getWidth();
            height = objImage.getHeight();
            mObj = new NormalFish(objImage, 0, 0, width, height, 0, 0, width, height, 0, Color.WHITE, 90);
            mObj.random(mLimitRect);
            mObj.setTouchScore(objectTable[1][i]);
            mObj.isAlive = true;
            mObjCollections.add(mObj);
        }
    }

    private void CreateSpecialObj(int[][] objectTable) {
        int width, height;
        int random;
        Random _random = new Random();
        random = _random.nextInt(objectTable[0].length);
        Bitmap objImage = null;
        try {
            objImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res, objectTable[0][random]);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            Toast.makeText(mGameEntry.mMainActivity, "OutOfMemoryError!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        width = objImage.getWidth() / objectTable[1][random];
        height = objImage.getHeight() / objectTable[2][random];
        mObj = new NormalFish(objImage, 0, 0, width, height, 0, 0, width, height, 0, Color.WHITE, 90);
        mObj.random(mLimitRect);
        mObj.setCol(objectTable[1][random]);
        mObj.setMaxIndex(objectTable[3][random]);
        mObj.setDeathIndexStart(objectTable[4][random]);
        mObj.setDeathIndexEnd(objectTable[5][random]);
        mObj.setTimerAdd(objectTable[8][random]);
        mObj.setLifeAdd(objectTable[9][random]);
        mObj.isAlive = true;
        mObjCollections.add(mObj);
    }

    @Override
    protected void Initialize() {
        super.Initialize();
        DebugConfig.d("Stage3 Initialize()");
        GameParams.stage3TotalScore = 0;
        mRandom = new Random();

        mObjCollections = new FishCollection();

        if (mHandlerThread == null) {
            mHandlerThread = new HandlerThread(THREADNAME,
                    android.os.Process.THREAD_PRIORITY_BACKGROUND);
            mHandlerThread.start();
            DebugConfig.d("Create thread: " + THREADNAME);
        }

        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Events.CREATESTAR:
                        if (isGameOver || GameParams.isClearStage3)
                            return;

                        CreateSpecialObj(mObjTable_2);
                        if (onOff) {
                            Message m = new Message();
                            m.what = Events.CREATESTAR;
                            mHandler.sendMessageDelayed(m, mRandom.nextInt(5000) + 5000);
                        }
                        break;
                }
            }
        };
    }
    public static void ObjectGeneration(boolean produce) {
        onOff = produce;
        if (onOff) {
            Message msg = new Message();
            msg.what = Events.CREATESTAR;
            mHandler.sendMessageDelayed(msg, 5000);
        } else {
            mHandler.removeMessages(Events.CREATESTAR);
        }
    }
    @Override
    protected void LoadContent() {
        super.LoadContent();
        DebugConfig.d("Stage3 LoadContent()");
        int width, height;

        if (mBackground == null) {
            mBackGroundImage = GameParams.decodeSampledBitmapFromResource(R.drawable.a_background, GameParams.scaleWidth, GameParams.scaleHeight);
            mBackground = new GameObj(0, 0, GameParams.scaleWidth, GameParams.scaleHeight, 0, 0, mBackGroundImage.getWidth(), mBackGroundImage.getHeight(), 0, 0, 0);
            mBackground.isAlive = true;
        }

        if (mSceneTitle == null) {
            mSceneTitleImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res, R.drawable.a_stage_title);
            mSceneTitle = new GameObj(GameParams.halfWidth, (int) (10 * GameParams.density), mSceneTitleImage.getWidth(), mSceneTitleImage.getHeight(), 0, 0, mSceneTitleImage.getWidth(), mSceneTitleImage.getHeight(), 0, 0, 0);
        }

        if (DebugConfig.isFpsDebugOn) {
            mFpsText = new Text(GameParams.halfWidth - 50, 100, 12, "FPS", Color.BLUE);
        }

        if (mScore == null)
            mScore = new Score((int) (20 * GameParams.density), (int) (20 * GameParams.density));

        if (mLifeIcon == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            mLifeImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res, R.drawable.b_life, options);
            mLifeIcon = new GameObj(mScore.destRect.left, mScore.getY() + mScore.height + (int) (5 * GameParams.density), mLifeImage.getWidth(), mLifeImage.getHeight(), 0, 0, mLifeImage.getWidth(), mLifeImage.getHeight(), 0, 0, 0);
        }
        // Can't assign GameParams.screenRect to mLimitRect,
        // or modify mLimitRect.top will also modify GameParams.screenRect.top
        mLimitRect = new Rect(GameParams.screenRect);
        mLimitRect.top = mLifeIcon.destRect.bottom;

        if (mLife1 == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap numBitmap = (Bitmap) BitmapFactory.decodeResource(GameParams.res, R.drawable.s_0, options);
            mLife1 = new Life1(mLifeIcon.destRect.right + (int) (10 * GameParams.density), mLifeIcon.destRect.bottom - mLifeIcon.halfHeight - (numBitmap.getHeight() >> 1), numBitmap.getWidth(), numBitmap.getHeight(), 0, 0, numBitmap.getWidth() * 2, numBitmap.getHeight() * 2);
            Life1.setLife(GameParams.stage3Life);
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
            mTimerBar.setTimer(GameParams.stage3RunningTime);
        }

        CreateObj(mObjTable_1);
        ObjectGeneration(true);
    }

    @Override
    protected void Update() {
        super.Update();
        if (DebugConfig.isFpsDebugOn) {
            mFpsText.message = "actual FPS: " + (int) mGameEntry.actualFPS + " FPS (" + (int) mGameEntry.fps
                    + ") " + (int) mGameEntry.totalFrames;
        }
        if (mScore != null)
            mScore.setTotalScore(GameParams.stage3TotalScore);

        if (mLife1 != null) {
            mLife1.updateLife();
            mLife1.action();
            if (Life1.getLife() <= 0)
                isGameOver = true;
        }

        if (mTimerBar != null) {
            mTimerBar.action((int) GameEntry.totalFrames);
            if (mTimerBar.isTimeout)
                isGameOver = true;
        }

        for (int f = mObjCollections.size() -1 ; f >= 0; f--) {
            mSubObj = (NormalFish) mObjCollections.get(f);
            mSubObj.Animation();

            if (!mSubObj.isAlive)
                mObjCollections.remove(mSubObj);
        }
    }

    @Override
    protected void Draw() {
        super.Draw();
        Canvas mSubCanvas = mGameEntry.canvas;
        if (mBackground.isAlive)
        {
            mSubCanvas.drawBitmap(mBackGroundImage, mBackground.srcRect, mBackground.destRect, null);
        }

        if (mSceneTitle != null) {
            mSubCanvas.drawBitmap(mSceneTitleImage, mSceneTitle.srcRect, mSceneTitle.destRect, null);
        }


        if (DebugConfig.isFpsDebugOn) {
            mSubCanvas.drawText(mFpsText.message, mFpsText.x, mFpsText.y, mFpsText.paint);
        }

        if (mScore != null)
            mScore.drawScore(mSubCanvas);

        if (mLifeIcon != null) {
            mSubCanvas.drawBitmap(mLifeImage, mLifeIcon.srcRect, mLifeIcon.destRect, null);
        }

        if (mLife1 != null) {
            mLife1.draw(mSubCanvas);
        }

        if (mTimerBar != null) {
            mSubCanvas.drawBitmap(mTimerBarImage, mTimerBar.srcRect, mTimerBar.destRect, null);
        }

        for (int f = mObjCollections.size() -1 ; f >= 0; f--) {
            mSubObj = (NormalFish) mObjCollections.get(f);
            if (mSubObj.isAlive) {
                mSubCanvas.drawBitmap(mSubObj.image, mSubObj.srcRect, mSubObj.destRect, mSubObj.paint);
            }

        }
    }
}