package com.nosae.game.sence;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.*;

import com.nosae.game.bobo.Events;
import com.nosae.game.bobo.GameEntry;
import com.nosae.game.bobo.GameParams;
import com.nosae.game.bobo.R;
import com.nosae.game.bobo.Text;

import java.util.Random;

import lbs.DrawableGameComponent;
import lbs.FishCollection;
import com.nosae.game.role.Bobo;
import com.nosae.game.objects.ColorMask;
import com.nosae.game.objects.GameObj;
import com.nosae.game.role.GoldenFish;
import com.nosae.game.role.NormalFish;
import com.nosae.game.objects.Score;
import com.nosae.game.objects.TimerBar;
import com.nosae.game.settings.DebugConfig;

/**
 * Created by eason on 2015/10/19.
 */
public class Stage1 extends DrawableGameComponent {
    public GameEntry mGameEntry;

    private GameObj mBackground;
    private Bitmap mBackGroundImage;

    private NormalFish mFishObj;
    private NormalFish mSubFishObj;

    private GoldenFish mGoldenFishObj;
    private Bitmap mGoldenFishImage;

    public Bobo mBoboObj;
    private Bitmap mBoboImage;

    private Text mFpsText;

    private Score mScore;

    private ColorMask mColorMask;
    public TimerBar mTimerBar;
    public static FishCollection mFishCollections;
    private int f, j;
    private Random mRandom;
    private int[][] mFishTable = {
            {
                    R.drawable.fish_01,
                    R.drawable.fish_02,
                    R.drawable.fish_03,
                    R.drawable.fish_04,
                    R.drawable.fish_05,
                    R.drawable.fish_06,
                    R.drawable.fish_07,
                    R.drawable.fish_08,
                    R.drawable.fish_09,
                    R.drawable.fish_10,
                    R.drawable.goldenfish
            },
            { 24, 27, 25, 25, 27, 24, 28, 30, 24, 16, 1}, /* Animation column */
            { 1, 1, 1, 1 , 1, 1, 1, 1, 1, 1, 1}, /* Animation row */
            { 12, 15, 15, 15, 14, 15, 14, 20, 15, 9, 0},  /* Max index */
            { 13, 16, 16, 16 ,15, 16, 15, 21, 16, 10, 0}, /* Death animation start */
            { 23, 26, 24, 24, 26, 23, 27, 29, 23, 15, 0}, /* Death animation end */
            { 200, 100, 200, 200, 100, -600, 100, 300, -800, 100, 0}, /* Score */
            { 0, 0, 0, 0 ,0 , 0, 0, 0, 0, 0, 2} /* Timer add (seconds) */
    };
    private int mGoldFish = R.drawable.goldenfish;

    public static Handler mHandler;
    public static HandlerThread mHandlerThread;
    private int mMaximum = 1000;


    public int mTotalScore = 0;
    public static boolean onOff = true;
    public static boolean isGameOver = false;
    public boolean isClearStage1 = false;

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
//            mHandlerThread.setDaemon(true);
            DebugConfig.d("Create thread");
        }

        mGameEntry.totalFrames = 0;

        mColorMask = new ColorMask(Color.RED, 0);
        mColorMask.isAlive = false;

        mTimerBar = new TimerBar();

        mHandler = new Handler(mHandlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Events.CREATEFISH:
                        if (isGameOver || isClearStage1)
                            return;
                        createFish();

                        if (onOff) {
                            Message m = new Message();
                            m.what = Events.CREATEFISH;
                            // TODO msg.obj = something;
                            //if (msg.obj != null) {
                            mHandler.sendMessageDelayed(m, mRandom.nextInt(GameParams.fishRebirthMax) + GameParams.fishRebirthMin);
                            //}
                        }
                        break;

                }

            }
        };


        super.Initialize();
    }

 /*   public static void ThreadSwitch(boolean onoff) {
        if (mHandlerThread == null)
            return;
        try {
            if (onoff) {
                    mHandlerThread.wait();
            } else {
                    mHandlerThread.notify();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/
    private void FishGeneration() {

//        mHandler.post(createFishes);
//        mHandler.post(createGoldenFish);
        onOff = true;
        Message msg = new Message();
        msg.what = Events.CREATEFISH;
        // TODO msg.obj = something;
        //if (msg.obj != null) {
        mHandler.sendMessage(msg);
        //}
//        DebugConfig.d("send message");

    }
    protected void createFish(){
        int width, height;

        int speed = 5;
        int random;
//        for (int i = 0; i < mMaximum; i++) {
            random = mRandom.nextInt(mFishTable[0].length);
            Bitmap fishImage = GameParams.decodeSampledBitmapFromResource(mFishTable[0][random], (int) (50 * mFishTable[1][random] / GameParams.density), (int) (50 * mFishTable[2][random] / GameParams.density));

            width = fishImage.getWidth() / mFishTable[1][random];
            height = fishImage.getHeight() / mFishTable[2][random];
            DebugConfig.d("Image ID: " + random + "=> width: " + width + ", height: " + height);
            speed = mRandom.nextInt(GameParams.fishRandomSpeed) + GameParams.fishRandomSpeed;
            mFishObj = new NormalFish(fishImage, 0, 0, width, height, 0, 0, width, height, speed, Color.WHITE, 90);

            mFishObj.randomTop();
            mFishObj.setCol(mFishTable[1][random]);
            mFishObj.setMaxIndex(mFishTable[3][random]);
            mFishObj.setDeathIndexStart(mFishTable[4][random]);
            mFishObj.setDeathIndexEnd(mFishTable[5][random]);
            mFishObj.setScore(mFishTable[6][random]);
            mFishObj.setTimerAdd(mFishTable[7][random]);
            mFishObj.isAlive = true;
            mFishCollections.add(mFishObj);
            DebugConfig.d("create fish: " + mFishCollections.size());
            fishImage = null;
//                mFishObj = null;

//        }
    }

/*    protected Runnable createFishes = new Runnable() {
        @Override
        public void run() {
            int width, height;

            int speed = 5;
            int random;
            for (int i = 0; i < mMaximum; i++) {
                random = mRandom.nextInt(mFishTable[0].length);
                mFishImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res,
                        mFishTable[0][random]);
                width = mFishImage.getWidth() / mFishTable[1][random];
                height = mFishImage.getHeight() / mFishTable[2][random];
//                DebugConfig.d("width: " + mFishImage.getWidth());
                speed = mRandom.nextInt(GameParams.fishRandomSpeed) + GameParams.fishRandomSpeed;
                mFishObj = new NormalFish(mFishImage, 0, 0, width, height, 0, 0, width, height, speed, Color.WHITE, 90, 100);

                mFishObj.randomTop();
                mFishObj.setCol(mFishTable[1][random]);
                mFishObj.setMaxIndex(mFishTable[3][random]);
                mFishObj.setDeathIndexStart(mFishTable[4][random]);
                mFishObj.setDeathIndexEnd(mFishTable[5][random]);
                mFishObj.isAlive = true;
                mFishCollections.add(mFishObj);
                DebugConfig.d("create fish: " + mFishCollections.size());
                mFishImage = null;
//                mFishObj = null;
                try {
                    mHandlerThread.sleep(mRandom.nextInt(GameParams.fishRebirthMax) + GameParams.fishRebirthMin);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    DebugConfig.d("Thread interrupted: " + e.getMessage());
                }

            }
        }
    };*/



    private void UpdateScore() {

    }

    @Override
    protected void LoadContent() {
        super.LoadContent();
        if (mBackground == null) {
            // Load background image

            mBackGroundImage = GameParams.decodeSampledBitmapFromResource(R.drawable.background, GameParams.scaleWidth, GameParams.scaleHeight);
            mBackground = new GameObj(0, 0, GameParams.scaleWidth, GameParams.scaleHeight, 0, 0, mBackGroundImage.getWidth(), mBackGroundImage.getHeight(), 0, 0, 0);
            mBackground.isAlive = true;


            // Random fish generator
            FishGeneration();

        }

        if (mBoboObj == null) {
            int width, height;
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
//        mBoboObj.randomTop();
            mBoboObj.isAlive = true;
        }

        if (DebugConfig.isFpsDebugOn) {
            mFpsText = new Text(GameParams.halfWidth - 50, 100, 12, "FPS", Color.BLUE);
        }

        mScore = new Score(10, 10);

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
        Resources rs = GameParams.res;

        if (mGameEntry.totalFrames == 10) {
//            int width, height;
//            mGoldenFishImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res,
//                    R.drawable.aircraft);
//            width = mGoldenFishImage.getWidth() / 4;
//            height = mGoldenFishImage.getHeight() / 5;
//            GoldenFish.width = width;
//            GoldenFish.height = height;
//            GoldenFish.halfWidth = width >> 1;
//            GoldenFish.halfHeight = height >> 1;
//            mGoldenFishObj = new GoldenFish(mGoldenFishImage, 0, 0, width, height, 0, 0, width, height, 5, Color.WHITE, 90);
//            mGoldenFishObj.setCol(4);
//            mGoldenFishObj.randomTop();
//            mGoldenFishObj.isAlive = true;

//            mFishCollections.add(mGoldenFishObj);
        }
        if (mGoldenFishObj != null) {
//            mGoldenFishObj.Animation();
//            mGoldenFishObj.moveDown(GameParams.screenRect.height() + mGoldenFishObj.srcHeight);
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
                    mTotalScore += mSubFishObj.getScore();
                    if (mTotalScore < 0) {
                        mTotalScore = 0;
                        mBoboObj.isAlive = false;
                    }
                }
                mFishCollections.remove(mSubFishObj);
            }
            if (!mBoboObj.isAlive)
                isGameOver = true;

            if (!mSubFishObj.isAlive)
                mFishCollections.remove(mSubFishObj);


            // GoldenFish out of screen bottom
//            if (GameParams.outOfScreenBottom(mFishCollections.get(f).getRect())) {
//                DebugConfig.d("Out of screen bottom.");
//                //TODO destory
//                mFishCollections.remove(mFishCollections.get(f));
//            }
/*                mFishCollections.get(f).y_Speed = -mFishCollections.get(f).y_Speed;

            } else if (GameParams.outOfScreenTop(mFishCollections.get(f).getRect())) {
                DebugConfig.d("Out of screen top.");

                mFishCollections.get(f).y_Speed = Math.abs(mFishCollections.get(f).y_Speed);

            }
            mFishCollections.get(f).move(0, mFishCollections.get(f).y_Speed);*/


        }
        if (DebugConfig.isFpsDebugOn) {
            mFpsText.message = "actual FPS: " + (int) mGameEntry.actualFPS + " FPS (" + (int) mGameEntry.fps
                    + ") " + (int) mGameEntry.totalFrames;
        }

        mScore.setTotalScore(mTotalScore);

        mTimerBar.action((int) mGameEntry.totalFrames);
        if (mTimerBar.isTimeout)
            isGameOver = true;

        if (isGameOver || !mBoboObj.isAlive) {
            mColorMask.Action((int) mGameEntry.totalFrames);
        }
        super.Update();
    }

    @Override
    protected void Draw() {
//        DebugConfig.d("Stage1 Draw()");
        Canvas mSubCanvas = mGameEntry.canvas;
        // Draw background image
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



        for (f = mFishCollections.size() -1 ; f >= 0; f--) {
            mSubFishObj = (NormalFish) mFishCollections.get(f);
            if (mFishObj.isAlive) {
                mSubCanvas.save();

//                mSubCanvas.rotate(mSubFishObj.theta - 90, mSubFishObj.getX()
//                        + Aircraft.halfWidth, mSubFishObj.getY()
//                        + Aircraft.halfHeight);
                mSubCanvas.drawBitmap(mSubFishObj.image, mSubFishObj.srcRect,
                        mSubFishObj.destRect, mSubFishObj.paint);

                mSubCanvas.restore();
            }

        }


        if (mGoldenFishObj != null && mGoldenFishObj.isAlive) {
            mSubCanvas.save();

            mSubCanvas.drawBitmap(mGoldenFishObj.image, mGoldenFishObj.srcRect, mGoldenFishObj.destRect,
                    mGoldenFishObj.paint);
//            DebugConfig.d("srcRect: " + mGoldenFishObj.srcRect.left + ", " + mGoldenFishObj.srcRect.top + ", " + mGoldenFishObj.srcRect.right + ", " + mGoldenFishObj.srcRect.bottom);
//            DebugConfig.d("destRect: " + mGoldenFishObj.destRect.left + ", " + mGoldenFishObj.destRect.top + ", " + mGoldenFishObj.destRect.right + ", " + mGoldenFishObj.destRect.bottom);
            mSubCanvas.restore();


        }

        if (mBoboObj != null && mBoboObj.isAlive) {
            mSubCanvas.save();

            mSubCanvas.drawBitmap(mBoboObj.boboImage, mBoboObj.srcRect, mBoboObj.destRect,
                    mBoboObj.paint);
//            DebugConfig.d("srcRect: " + mGoldenFishObj.srcRect.left + ", " + mGoldenFishObj.srcRect.top + ", " + mGoldenFishObj.srcRect.right + ", " + mGoldenFishObj.srcRect.bottom);
//            DebugConfig.d("destRect: " + mGoldenFishObj.destRect.left + ", " + mGoldenFishObj.destRect.top + ", " + mGoldenFishObj.destRect.right + ", " + mGoldenFishObj.destRect.bottom);
            mSubCanvas.restore();


        }
//            mFishCollections.get(f).draw(mSubCanvas);
        if (DebugConfig.isFpsDebugOn) {
            mSubCanvas.drawText(mFpsText.message, mFpsText.x, mFpsText.y, mFpsText.paint);
        }

        mSubCanvas.save();
        mScore.drawScore(mSubCanvas);
        mTimerBar.draw(mSubCanvas);
        mSubCanvas.restore();


        if ((isGameOver || !mBoboObj.isAlive) && mColorMask.isAlive)
        {
            // Mask
            mSubCanvas.drawRect(mColorMask.destRect, mColorMask.paint);

            // Text
            mSubCanvas.drawText(mColorMask.warningText.message, mColorMask.warningText.x, mColorMask.warningText.y, mColorMask.warningText.paint);
        }
        super.Draw();
    }


}
