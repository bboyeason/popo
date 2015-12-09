package com.nosae.game.scene;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.widget.Toast;

import com.nosae.game.popo.Events;
import com.nosae.game.popo.GameEntry;
import com.nosae.game.popo.GameParams;
import com.nosae.game.popo.R;
import com.nosae.game.popo.Text;
import com.nosae.game.objects.ColorMask;
import com.nosae.game.objects.FishCollection;
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
public class Stage4 extends DrawableGameComponent {

    public static Handler mHandler;
    public static HandlerThread mHandlerThread;
    public static final String THREADNAME = "Stage4_object_generator";

    private final GameEntry mGameEntry;

    private GameObj mBackground;
    private Bitmap mBackGroundImage;

    private GameObj mSceneTitle;
    private Bitmap mSceneTitleImage;

    private Score mScore;

    private Text mFpsText;
    private ColorMask mColorMask;

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

    public Stage4(GameEntry gameEntry) {
        DebugConfig.d("Stage4 Constructor");
        this.mGameEntry = gameEntry;
    }

    private void CreateSpecialObjects(int[][] objectTable) {
        int width, height;
        int speed;
        int random;
        Random _random = new Random();
        random = _random.nextInt(objectTable[0].length);
        speed = _random.nextInt(GameParams.stage4RandomSpeed) + GameParams.stage4RandomSpeed;
        Bitmap objImage;
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
        mObj = new NormalFish(objImage, 0, 0, width, height, 0, 0, width, height, (int) (speed * GameParams.density), Color.WHITE, 90);
        mObj.randomTop();
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
        DebugConfig.d("Stage4 Initialize()");
        GameParams.stage4TotalScore = 0;
        mRandom = new Random();
        mColorMask = new ColorMask(Color.RED, 0);
        mColorMask.isAlive = false;

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
                        if (isGameOver || GameParams.isClearStage4)
                            return;

                        CreateSpecialObjects(GameParams.specialObjectTable);
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
        DebugConfig.d("Stage4 LoadContent()");
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

        if (mLife1 == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap numBitmap = (Bitmap) BitmapFactory.decodeResource(GameParams.res, R.drawable.s_0, options);
            mLife1 = new Life1(mLifeIcon.destRect.right + (int) (10 * GameParams.density), mLifeIcon.destRect.bottom - mLifeIcon.halfHeight - (numBitmap.getHeight() >> 1), numBitmap.getWidth(), numBitmap.getHeight(), 0, 0, numBitmap.getWidth() * 2, numBitmap.getHeight() * 2);
            Life1.setLife(GameParams.stage4Life);
            numBitmap.recycle();
        }

        if (mTimerBar == null) {
            mTimerBarImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res, R.drawable.timer_bar);
            if (mTimerBarImage != null) {
                width = mTimerBarImage.getWidth() / 1;
                height = mTimerBarImage.getHeight() / 9;
                mTimerBar = new TimerBar2(mScore.edge_X_right + 5, mScore.getY() + (mLife1.destRect.bottom - mScore.getY()) / 2 - (height >> 1), width, height, 0, 0, width, height, 0, 0, 0);
                if (mTimerBar != null)
                    mTimerBar.setStartFrame((int) GameEntry.totalFrames);
            }
        }
        if (mTimerBar != null) {
            mTimerBar.setTimer(GameParams.stage4RunningTime);
        }

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
            mScore.setTotalScore(GameParams.stage4TotalScore);

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
            if (mSubObj.smartMoveDown(GameParams.screenRect.height())) {
                if (!isGameOver) {
                    if (GameParams.stage1TotalScore < 0 || Life1.getLife() <= 0) {
                        GameParams.stage1TotalScore = 0;
                    }
                }
                mObjCollections.remove(mSubObj);
                mSubObj.recycle();
            }

            if (!mSubObj.isAlive) {
                mObjCollections.remove(mSubObj);
                mSubObj.recycle();
            }
        }

        if (isGameOver) {
            mColorMask.Action((int) mGameEntry.totalFrames);
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

        if ((isGameOver) && mColorMask.isAlive)
        {
            mSubCanvas.drawRect(mColorMask.destRect, mColorMask.paint);
            mSubCanvas.drawText(mColorMask.text.message, mColorMask.text.x, mGameEntry.mMainActivity.mRestartButton.getTop() - 30, mColorMask.text.paint);
        }
    }
}
