package com.nosae.game.scene;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Message;
import android.widget.Toast;

import com.nosae.game.popo.Events;
import com.nosae.game.popo.GameEntry;
import com.nosae.game.popo.GameParams;
import com.nosae.game.popo.R;
import com.nosae.game.popo.Text;
import com.nosae.game.objects.FishCollection;
import com.nosae.game.objects.GameObj;
import com.nosae.game.objects.Life1;
import com.nosae.game.objects.Score;
import com.nosae.game.objects.TimerBar2;
import com.nosae.game.role.NormalFish;
import com.nosae.game.role.Popo;
import com.nosae.game.settings.DebugConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lbs.DrawableGameComponent;

/**
 * Created by eason on 2015/11/9.
 */
public class Stage4 extends DrawableGameComponent {

    private SensorManager mSensorManager;
    private Sensor mSensor;

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

    private Popo mPopoObj;

    private Random mRandom;

    private NormalFish mSubObj;
    public static FishCollection mObjCollections;

    private int[][] mFishTable = {
            {
                    R.drawable.d_fish_01,
                    R.drawable.d_fish_02,
                    R.drawable.d_fish_03,
                    R.drawable.d_fish_04
            },
            {  1,  1,  1,  1 }, /* Animation column */
            {  1,  1,  1,  1 }, /* Animation row */
            {  0,  0,  0,  0 }, /* Max index */
            {  0,  0,  0,  0 }, /* Death animation start */
            {  0,  0,  0,  0 }, /* Death animation end */
            { 10, 20,  0,  0 }, /* Touch Score */
            { -1, -1, -1, -1 }, /* Arrival Score */
            {  0,  0,  0,  0 }, /* Timer add */
            {  0,  0, -1, -2 } /* Life add */
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
        GameParams.isClearStage4 = true;
        if (mOnStageCompleteListeners.size() > 0) {
            for (OnStageCompleteListener l : mOnStageCompleteListeners) {
                l.OnStageComplete(this);
            }
        }
    }

    public Stage4(GameEntry gameEntry) {
        DebugConfig.d("Stage4 Constructor");
        this.mGameEntry = gameEntry;
        mOnStageCompleteListeners = new ArrayList<>();
        GameParams.msgHandler = new MsgHandler(this);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case Events.CREATE_FISH:
                if (GameParams.isGameOver || GameParams.isClearStage4)
                    return;

                CreateObjects(mFishTable);
                if (GameParams.onOff) {
                    Message m = new Message();
                    m.what = Events.CREATE_FISH;
                    GameParams.msgHandler.sendMessageDelayed(m, mRandom.nextInt(GameParams.stage4FishRebirthMax) + GameParams.stage4FishRebirthMin);
                }
                break;
            case Events.CREATE_OBJECT:
                if (GameParams.isGameOver || GameParams.isClearStage4)
                    return;

                CreateObjects(GameParams.specialObjectTable);
                if (GameParams.onOff) {
                    Message m = new Message();
                    m.what = Events.CREATE_OBJECT;
                    GameParams.msgHandler.sendMessageDelayed(m, mRandom.nextInt(5000) + 5000);
                }
                break;
        }
    }

    private void CreateObjects(int[][] objectTable) {
        int width, height;
        int speed;
        int random;
        Random _random = new Random();
        random = _random.nextInt(objectTable[0].length);
        speed = _random.nextInt(GameParams.stage4RandomSpeed) + GameParams.stage4RandomSpeed;
        Bitmap objImage;
        try {
            objImage = BitmapFactory.decodeResource(GameParams.res, objectTable[0][random]);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            Toast.makeText(mGameEntry.mMainActivity, "OutOfMemoryError!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        width = objImage.getWidth() / objectTable[1][random];
        height = objImage.getHeight() / objectTable[2][random];
        NormalFish mObj = new NormalFish(objImage, 0, 0, width, height, 0, 0, width, height, (int) (speed * GameParams.density), Color.WHITE, 90);
        mObj.randomTop();
        mObj.setCol(objectTable[1][random]);
        mObj.setMaxIndex(objectTable[3][random]);
        mObj.setDeathIndexStart(objectTable[4][random]);
        mObj.setDeathIndexEnd(objectTable[5][random]);
        mObj.setTouchScore(objectTable[6][random]);
        mObj.setTimerAdd(objectTable[8][random]);
        mObj.setLifeAdd(objectTable[9][random]);
        mObj.isAlive = true;
        mObjCollections.add(mObj);
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
//            DebugConfig.d("onSensorChanged: " + event.values[0]);
            if (mPopoObj != null) {
                mPopoObj.addX((int) -event.values[SensorManager.DATA_X] << 2);
                if (mPopoObj.getX() < GameParams.screenRect.left - (mPopoObj.srcWidth >> 1))
                    mPopoObj.setX(GameParams.screenRect.left - (mPopoObj.srcWidth >> 1));
                else if (mPopoObj.getX() + mPopoObj.destWidth > GameParams.screenRect.right)
                    mPopoObj.setX(GameParams.screenRect.right - mPopoObj.destWidth);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void Initialize() {
        super.Initialize();
        DebugConfig.d("Stage4 Initialize()");
        mSensorManager = (SensorManager) mGameEntry.mMainActivity.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        registerListener();

        GameParams.stage4TotalScore = 0;
        GameParams.isClearStage4 = false;

        mRandom = new Random();

        mObjCollections = new FishCollection();
    }

    public static void ObjectGeneration(boolean produce) {
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
        DebugConfig.d("Stage4 LoadContent()");
        int width, height;

        if (mBackground == null) {
            mBackGroundImage = GameParams.decodeSampledBitmapFromResource(R.drawable.background_04, GameParams.scaleWidth, GameParams.scaleHeight);
            mBackground = new GameObj(0, 0, GameParams.scaleWidth, GameParams.scaleHeight, 0, 0, mBackGroundImage.getWidth(), mBackGroundImage.getHeight(), 0, 0, 0);
        }
        mBackground.isAlive = true;

        if (mSceneTitle == null) {
            mSceneTitleImage = BitmapFactory.decodeResource(GameParams.res, R.drawable.a_stage_title);
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
        Life1.setLife(GameParams.stage4Life);

        if (mTimerBar == null) {
            mTimerBarImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res, R.drawable.timer_bar);
            if (mTimerBarImage != null) {
                width = mTimerBarImage.getWidth();
                height = mTimerBarImage.getHeight() / GameParams.timerBarRowCount;
                mTimerBar = new TimerBar2(mScore.edge_X_right + 5, mScore.getY() + (mLife1.destRect.bottom - mScore.getY()) / 2 - (height >> 1), width, height, 0, 0, width, height, 0, 0, 0);
            }
        }

        if (mTimerBar != null) {
            mTimerBar.setTimer(GameParams.stage4RunningTime);
            mTimerBar.setStartFrame((int) GameEntry.totalFrames);
        }

        if (mPopoObj == null) {
            Bitmap mPopoImage = BitmapFactory.decodeResource(GameParams.res, R.drawable.popo);
            mPopoObj = new Popo(mPopoImage, GameParams.halfWidth - mPopoImage.getWidth() / 2, GameParams.scaleHeight - mPopoImage.getHeight(), mPopoImage.getWidth(), mPopoImage.getHeight(), 0, 0, mPopoImage.getWidth(), mPopoImage.getHeight(), 0, 0, 0);
        }
        mPopoObj.isAlive = true;
        ObjectGeneration(true);
    }

    @Override
    protected void Update() {
        super.Update();
        if (DebugConfig.isFpsDebugOn) {
            mFpsText.message = "actual FPS: " + mGameEntry.actualFPS + " FPS (" + mGameEntry.fps
                    + ") " + (int) GameEntry.totalFrames;
        }
        if (mScore != null)
            mScore.setTotalScore(GameParams.stage4TotalScore);

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

        for (int f = mObjCollections.size() -1 ; f >= 0; f--) {
            mSubObj = (NormalFish) mObjCollections.get(f);
            mSubObj.Animation();
            if (!GameParams.breakStageMask.isAlive) {
                if (mSubObj.smartMoveDown(GameParams.screenRect.height())) {
                    mObjCollections.remove(mSubObj);
                    mSubObj.recycle();
                }
            }

            if (!GameParams.colorMaskGameOver.isAlive) {
                if (GameParams.isCollision(mPopoObj.destRect, mSubObj.destRect)) {
                    if (!mSubObj.readyToDeath) {
                        GameParams.stage4TotalScore += mSubObj.getTouchScore();
                        Life1.addLife(mSubObj.getLifeAdd());
                        mTimerBar.addTimer(mSubObj.getTimerAdd());
                        mSubObj.readyToDeath = true;
                        if (mSubObj.getLifeAdd() < 0)
                            GameParams.vibrator.vibrate(50);
                    }
                }
            }

            if (!mSubObj.isAlive) {
                mObjCollections.remove(mSubObj);
                mSubObj.recycle();
            }
        }

        if (GameParams.isGameOver) {
            GameParams.colorMaskGameOver.Action((int) GameEntry.totalFrames);
        } else if (!GameParams.isGameOver && GameParams.stage4TotalScore >= GameParams.stage4BreakScore) {
            if (GameParams.breakStageMask.state == GameObj.State.step1) {
                ObjectGeneration(false);
                SharedPreferences settings = mGameEntry.mMainActivity.getSharedPreferences(GameParams.STAGES_COMPLETED, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(GameParams.STAGE4_COMPLETED, true);
                editor.apply();
            }
            if (GameParams.breakStageMask.Action((int) GameEntry.totalFrames))
                NotifyStageCompleted();
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

        if (mPopoObj != null) {
            mPopoObj.draw(mSubCanvas);
        }

        for (int f = mObjCollections.size() -1 ; f >= 0; f--) {
            mSubObj = (NormalFish) mObjCollections.get(f);
            if (mSubObj.isAlive) {
                mSubCanvas.drawBitmap(mSubObj.image, mSubObj.srcRect, mSubObj.destRect, mSubObj.paint);
            }

        }

        if ((GameParams.isGameOver) && GameParams.colorMaskGameOver.isAlive)
        {
            mSubCanvas.drawRect(GameParams.colorMaskGameOver.destRect, GameParams.colorMaskGameOver.paint);
            mSubCanvas.drawText(GameParams.colorMaskGameOver.text.message, GameParams.colorMaskGameOver.text.x, mGameEntry.mMainActivity.mRestartButton.getTop() - 30, GameParams.colorMaskGameOver.text.paint);
        } else if (!GameParams.isGameOver && GameParams.breakStageMask.isAlive) {
            mSubCanvas.drawRect(GameParams.breakStageMask.destRect, GameParams.breakStageMask.paint);
        }
    }

    public void registerListener() {
        if (mSensorManager != null)
            mSensorManager.registerListener(sensorEventListener, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void unregisterListener() {
        if (mSensorManager != null)
            mSensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    public void Dispose() {
        super.Dispose();
        if (mObjCollections != null)
            mObjCollections.clear();
        ObjectGeneration(false);
    }
}
