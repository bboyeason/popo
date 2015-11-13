package com.nosae.game.sence;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.*;
import android.widget.Toast;

import com.nosae.game.bobo.Events;
import com.nosae.game.bobo.GameEntry;
import com.nosae.game.bobo.GameParams;
import com.nosae.game.bobo.R;
import com.nosae.game.bobo.Text;

import java.util.Random;

import lbs.DrawableGameComponent;
import lbs.FishCollection;

import com.nosae.game.objects.Life1;
import com.nosae.game.role.Bobo;
import com.nosae.game.objects.ColorMask;
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

    private GameObj mSenceTitle;
    private Bitmap mSenceTitleImage;

    private NormalFish mFishObj;
    private NormalFish mSubFishObj;

    public Bobo mBoboObj;
    private Bitmap mBoboImage;

    private Text mFpsText;

    private Score mScore;

    private ColorMask mColorMask;

    public TimerBar2 mTimerBar;
    public Bitmap mTimerBarImage;

    private GameObj mLifeIcon;
    private Bitmap mLifeImage;
    private Life1 mLife1;

    public static FishCollection mFishCollections;

    private int f, j;
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

    private int[][] mFishTable2 = {
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



    public static Handler mHandler;
    public static HandlerThread mHandlerThread;

    public static int mTotalScore;
    public static boolean onOff = true;
    public static boolean isGameOver = false;
    public static boolean isClearStage1 = false;

    public Stage1(GameEntry mGameEntry) {
        DebugConfig.d("Stage1 Constructor");
        this.mGameEntry = mGameEntry;
        mFishCollections = new FishCollection();
        mRandom = new Random();
    }

    @Override
    public void Dispose() {
        DebugConfig.d("Stage1 Dispose()");
        //TODO stage switch animation
        super.Dispose();
    }

    @Override
    protected void Initialize() {
        DebugConfig.d("Stage1 Initialize()");
        if (mHandlerThread == null) {
            mHandlerThread = new HandlerThread("Stage1_fish_generator",
                    android.os.Process.THREAD_PRIORITY_BACKGROUND);
            mHandlerThread.start();
            DebugConfig.d("Create thread");
        }

        mColorMask = new ColorMask(Color.RED, 0);
        mColorMask.isAlive = false;

        mTotalScore = 0;

        mHandler = new Handler(mHandlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Events.CREATEFISH:
                        if (isGameOver || isClearStage1)
                            return;
                        createFish(mFishTable1);

                        if (onOff) {
                            Message m = new Message();
                            m.what = Events.CREATEFISH;
                            // TODO msg.obj = something;
                            //if (msg.obj != null) {
                            mHandler.sendMessageDelayed(m, mRandom.nextInt(GameParams.stage1FishRebirthMax) + GameParams.stage1FishRebirthMin);
                            //}
                        }
                        break;
                    case Events.CREATESTAR:
                        if (isGameOver || isClearStage1)
                            return;

                        createFish(mFishTable2);
                        if (onOff) {
                            Message m = new Message();
                            m.what = Events.CREATESTAR;
                            mHandler.sendMessageDelayed(m, mRandom.nextInt(5000) + 5000);
                        }
                        break;
                    case Events.CREATELIFE:
                        break;
                }

            }
        };


        super.Initialize();
    }

    private void FishGeneration() {

        onOff = true;
        Message msg = new Message();
        msg.what = Events.CREATEFISH;
        // TODO msg.obj = something;
        mHandler.sendMessage(msg);
//        msg = null;
        msg = new Message();
        msg.what = Events.CREATESTAR;
        mHandler.sendMessageDelayed(msg, 5000);
    }
    protected void createFish(int[][] fishTable){
        int width, height;

        int speed = 5;
        int random;
//        for (int i = 0; i < mMaximum; i++) {
            random = mRandom.nextInt(fishTable[0].length);
//            Bitmap fishImage = GameParams.decodeSampledBitmapFromResource(fishTable[0][random], (int) (50 * fishTable[1][random] / GameParams.density), (int) (50 * fishTable[2][random] / GameParams.density));
        Bitmap fishImage = null;
        try {
            fishImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res, fishTable[0][random]);
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
            mFishObj = new NormalFish(fishImage, 0, 0, width, height, 0, 0, width, height, (int) (speed * GameParams.density), Color.WHITE, 90);

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
            fishImage = null;
//                mFishObj = null;

//        }
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
            mBackground.isAlive = true;
        }
        // Random fish generator
        FishGeneration();

        if (mSenceTitle == null) {
            mSenceTitleImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res, R.drawable.a_stage_title);
            mSenceTitle = new GameObj(GameParams.halfWidth, (int) (10 * GameParams.density), mSenceTitleImage.getWidth(), mSenceTitleImage.getHeight(), 0, 0, mSenceTitleImage.getWidth(), mSenceTitleImage.getHeight(), 0, 0, 0);
        }

        if (mBoboObj == null) {
            mBoboImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res,
                    R.drawable.bobo);
            width = mBoboImage.getWidth() / 4;
            height = mBoboImage.getHeight() / 5;
            Bobo.width = width;
            Bobo.height = height;
            Bobo.halfWidth = width >> 1;
            Bobo.halfHeight = height >> 1;
            mBoboObj = new Bobo(mBoboImage, GameParams.halfWidth - width/2, GameParams.scaleHeight - height, width, height, 0, 0, width, height, 0, Color.WHITE, 90);
            mBoboObj.setCol(4);
            mBoboObj.setMaxIndex(20);
            mBoboObj.isAlive = true;
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
            mLife1 = new Life1(mLifeIcon.destRect.right + (int) (10 * GameParams.density), mLifeIcon.destRect.bottom - mLifeIcon.halfHeight - (numBitmap.getHeight() >> 1), numBitmap.getWidth(), numBitmap.getHeight(), 0, 0, numBitmap.getWidth(), numBitmap.getHeight());
            Life1.setLife(5);
            numBitmap.recycle();
            numBitmap = null;
        }

        if (mTimerBar == null) {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 4;
//            mTimerBarImage = GameParams.decodeSampledBitmapFromResource(R.drawable.timer_bar, (int) (GameParams.density * GameParams.halfWidth), (int) (GameParams.density * 120));
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
            mTimerBar.setTimer(30);
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
        if (mBackground.isAlive) {
            //TODO maybe background rolling
        }

        if (mBoboObj != null) {
//            mBoboObj.Animation();
        }

//        DebugConfig.d("size " + mFishCollections.size());
        for (f = mFishCollections.size() -1 ; f >= 0; f--) {
            mSubFishObj = (NormalFish) mFishCollections.get(f);
            mSubFishObj.Animation();
            if (mSubFishObj.smartMoveDown(GameParams.screenRect.height() - mBoboObj.srcHeight)) {
//                DebugConfig.d("Arrive screen bottom, remove it.");
                if (!isGameOver) {
                    if (mSubFishObj.getArrivalScore() > 0) {
                        mTotalScore += mSubFishObj.getArrivalScore();
                    } else if (mSubFishObj.getArrivalScore() < 0) {
                        Life1.addLife(mSubFishObj.getArrivalScore());
                    }
                    if (mTotalScore < 0 || Life1.getLife() <= 0) {
                        mTotalScore = 0;
                        mBoboObj.isAlive = false;
                    }
                }
                mFishCollections.remove(mSubFishObj);
                mSubFishObj.recycle();
            }
            if (!mBoboObj.isAlive)
                isGameOver = true;

            if (!mSubFishObj.isAlive) {
                mFishCollections.remove(mSubFishObj);
                mSubFishObj.recycle();
            }
        }
        if (DebugConfig.isFpsDebugOn) {
            mFpsText.message = "actual FPS: " + (int) mGameEntry.actualFPS + " FPS (" + (int) mGameEntry.fps
                    + ") " + (int) mGameEntry.totalFrames;
        }

        mScore.setTotalScore(mTotalScore);

        if (mLife1 != null) {
            mLife1.updateLife();
            if (Life1.getLife() <= 0)
                isGameOver = true;
        }

        mTimerBar.action((int) GameEntry.totalFrames);
        if (mTimerBar.isTimeout)
            isGameOver = true;

        if (isGameOver || !mBoboObj.isAlive) {
            mColorMask.Action((int) mGameEntry.totalFrames);
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

        if (mSenceTitle != null) {
            mSubCanvas.drawBitmap(mSenceTitleImage, mSenceTitle.srcRect, mSenceTitle.destRect, mSenceTitle.paint);
        }

        for (f = mFishCollections.size() -1 ; f >= 0; f--) {
            mSubFishObj = (NormalFish) mFishCollections.get(f);
            if (mSubFishObj.isAlive) {
                mSubCanvas.drawBitmap(mSubFishObj.image, mSubFishObj.srcRect,
                        mSubFishObj.destRect, mSubFishObj.paint);
            }

        }

        if (mBoboObj != null && mBoboObj.isAlive) {
//            mSubCanvas.drawBitmap(mBoboObj.boboImage, mBoboObj.srcRect, mBoboObj.destRect,
//                    mBoboObj.paint);
//            DebugConfig.d("srcRect: " + mGoldenFishObj.srcRect.left + ", " + mGoldenFishObj.srcRect.top + ", " + mGoldenFishObj.srcRect.right + ", " + mGoldenFishObj.srcRect.bottom);
//            DebugConfig.d("destRect: " + mGoldenFishObj.destRect.left + ", " + mGoldenFishObj.destRect.top + ", " + mGoldenFishObj.destRect.right + ", " + mGoldenFishObj.destRect.bottom);
        }

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

        if ((isGameOver || !mBoboObj.isAlive) && mColorMask.isAlive)
        {
            // Mask
            mSubCanvas.drawRect(mColorMask.destRect, mColorMask.paint);

            // Text
            mSubCanvas.drawText(mColorMask.text.message, mColorMask.text.x, mGameEntry.mMainActivity.mRestartButton.getTop() - 30, mColorMask.text.paint);
        }
        super.Draw();
    }


}
