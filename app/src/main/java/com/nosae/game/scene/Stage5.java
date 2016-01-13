package com.nosae.game.scene;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.nosae.game.objects.FishCollection;
import com.nosae.game.objects.GameObj;
import com.nosae.game.objects.GameObj.State;
import com.nosae.game.objects.Life1;
import com.nosae.game.objects.Score;
import com.nosae.game.objects.TimerBar2;
import com.nosae.game.popo.Events;
import com.nosae.game.popo.GameEntry;
import com.nosae.game.popo.GameParams;
import com.nosae.game.popo.MainActivity;
import com.nosae.game.popo.R;
import com.nosae.game.popo.Text;
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
public class Stage5 extends DrawableGameComponent {

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private final GameEntry mGameEntry;

    private GameObj mBackground;
    private Bitmap mBackGroundImage;
    private GameObj mForeground;
    private Bitmap mForeGroundImage;

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
    public ArrayList<NormalFish> mCakes = new ArrayList<>();
    private int f;
    private Rect stackRect;
    private int[][] mFishTable = {
            {
                    R.drawable.e_fish_01,
                    R.drawable.e_fish_02
            },
            { 10,  5 }, /* 1. Animation column */
            {  2,  4 }, /* 2. Animation row */
            {  5,  0 }, /* 3. Max index */
            {  6,  1 }, /* 4. Death animation start */
            { 14, 19 }, /* 5. Death animation end */
            {  0, 20 }, /* 6. Touch Score */
            { -1, -1 }, /* 7. Arrival Score */
            {  0,  0 }, /* 8. Timer add */
            { -1,  0 }, /* 9. Life add */
            {  0,  0 }, /* 10. Is cake */
    };

    private int[][] mCakeTable = {
            {
                    R.drawable.e_cake_01,
                    R.drawable.e_cake_02,
                    R.drawable.e_cake_03,
                    R.drawable.e_cake_04,
                    R.drawable.e_cake_05
            },
            { 1, 1, 1, 1, 1 }, /* 1. Animation column */
            { 1, 1, 1, 1, 1 }, /* 2. Animation row */
            { 0, 0, 0, 0, 0 }, /* 3. Max index */
            { 0, 0, 0, 0, 0 }, /* 4. Death animation start */
            { 0, 0, 0, 0, 0 }, /* 5. Death animation end */
            { 0, 0, 0, 0, 0 }, /* 6. Touch Score */
            { 0, 0, 0, 0, 0 }, /* 7. Arrival Score */
            { 0, 0, 0, 0, 0 }, /* 8. Timer add */
            { 0, 0, 0, 0, 0 }, /* 9. Life add */
            { 1, 1, 1, 1, 1 }, /* 10. Is cake */
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
        GameParams.isClearStage5 = true;
        if (mOnStageCompleteListeners.size() > 0) {
            for (OnStageCompleteListener l : mOnStageCompleteListeners) {
                l.OnStageComplete(this);
            }
        }
    }

    public Stage5(GameEntry gameEntry) {
        DebugConfig.d("Stage5 Constructor");
        this.mGameEntry = gameEntry;
        mOnStageCompleteListeners = new ArrayList<>();
        GameParams.msgHandler = new MsgHandler(this);
    }

    private void CreateObjects(int[][] objectTable, boolean inOrder) {
        if (GameParams.loadingMask.isAlive || mGameEntry.mMainActivity.mToggleButton.isChecked())
            return;
        int width, height;
        int speed;
        int random;
        Random _random = new Random();
        if (!inOrder)
            random = _random.nextInt(objectTable[0].length);
        else
            random = mCakes.size();
        speed = _random.nextInt(GameParams.stage5RandomSpeed) + GameParams.stage5RandomSpeed;
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
        if (objectTable.length > 10)
            mObj.isStackable = (objectTable[10][random] == 1);
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

                for (int i = mCakes.size() -1 ; i >= 0; i--) {
                    mSubObj = mCakes.get(i);
                    if (i == mCakes.size() - 1)
                        stackRect = mSubObj.destRect;
                    mSubObj.setX(mPopoObj.getX() + mPopoObj.srcWidth / 2 - mSubObj.halfWidth);
                    mSubObj.setY(mPopoObj.getY());
                    for (int j = i; j >= 0; j--)
                        mSubObj.addY((int) (-mCakes.get(j).srcHeight + 5 * GameParams.density));
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case Events.CREATE_FISH:
                if (GameParams.isGameOver || GameParams.isClearStage5)
                    return;

                CreateObjects(mFishTable, false);
                if (GameParams.onOff) {
                    Message m = new Message();
                    m.what = Events.CREATE_FISH;
                    GameParams.msgHandler.sendMessageDelayed(m, mRandom.nextInt(GameParams.stage5FishRebirthMax) + GameParams.stage5FishRebirthMin);
                }
                break;
            case Events.CREATE_OBJECT:
                if (GameParams.isGameOver || GameParams.isClearStage5)
                    return;

                CreateObjects(GameParams.specialObjectTable, false);
                if (GameParams.onOff) {
                    Message m = new Message();
                    m.what = Events.CREATE_OBJECT;
                    GameParams.msgHandler.sendMessageDelayed(m, mRandom.nextInt(5000) + 5000);
                }
                break;
            case Events.CREATE_CAKE:
                if (GameParams.isGameOver || GameParams.isClearStage5)
                    return;

                CreateObjects(mCakeTable, true);
                if (GameParams.onOff) {
                    Message m = new Message();
                    m.what = Events.CREATE_CAKE;
                    GameParams.msgHandler.sendMessageDelayed(m, mRandom.nextInt(GameParams.stage5CakeRebirthMax) + GameParams.stage5CakeRebirthMin);
                }
                break;
        }
    }

    @Override
    protected void Initialize() {
        super.Initialize();
        DebugConfig.d("Stage5 Initialize()");
        mSensorManager = (SensorManager) mGameEntry.mMainActivity.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        registerListener();

        GameParams.stage5TotalScore = 0;
        GameParams.isClearStage5 = false;
        mRandom = new Random();
        mObjCollections = new FishCollection();
        GameParams.gameOverMask.state = State.step1;
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

            msg = new Message();
            msg.what = Events.CREATE_CAKE;
            GameParams.msgHandler.sendMessageDelayed(msg, GameParams.stage5CakeRebirthMin);
        } else {
            GameParams.msgHandler.removeMessages(Events.CREATE_FISH);
            GameParams.msgHandler.removeMessages(Events.CREATE_OBJECT);
            GameParams.msgHandler.removeMessages(Events.CREATE_CAKE);
        }
    }

    @Override
    protected void LoadContent() {
        super.LoadContent();
        DebugConfig.d("Stage5 LoadContent()");
        int width, height;

        if (mBackground == null) {
            mBackGroundImage = GameParams.decodeSampledBitmapFromResource(R.drawable.background_05_1, GameParams.scaleWidth, GameParams.scaleHeight);
            mBackground = new GameObj(0, 0, GameParams.scaleWidth, GameParams.scaleHeight, 0, 0, mBackGroundImage.getWidth(), mBackGroundImage.getHeight(), 0, 0, 0);
        }
        mBackground.isAlive = true;

        if (mForeground == null) {
            mForeGroundImage = BitmapFactory.decodeResource(GameParams.res, R.drawable.background_05_2);
            mForeground = new GameObj(0, GameParams.scaleHeight - mForeGroundImage.getHeight(), GameParams.scaleWidth, mForeGroundImage.getHeight(), 0, 0, mForeGroundImage.getWidth(), mForeGroundImage.getHeight(), 0, 0, 0);
        }
        mForeground.isAlive = true;

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
        Life1.setLife(GameParams.stage5Life);

        if (mTimerBar == null) {
            mTimerBarImage = BitmapFactory.decodeResource(GameParams.res, R.drawable.timer_bar);
            if (mTimerBarImage != null) {
                width = mTimerBarImage.getWidth();
                height = mTimerBarImage.getHeight() / GameParams.timerBarRowCount;
                mTimerBar = new TimerBar2(mScore.edge_X_right + 5, mScore.getY() + (mLife1.destRect.bottom - mScore.getY()) / 2 - (height >> 1), width, height, 0, 0, width, height, 0, 0, 0);
            }
        }

        if (mTimerBar != null) {
            mTimerBar.setTimer(GameParams.stage5RunningTime);
            mTimerBar.setStartFrame((int) GameEntry.totalFrames);
        }

        if (mPopoObj == null) {
            Bitmap mPopoImage = BitmapFactory.decodeResource(GameParams.res, R.drawable.d_popo_01);
            mPopoObj = new Popo(mPopoImage, GameParams.halfWidth - mPopoImage.getWidth() / 2, GameParams.scaleHeight - mPopoImage.getHeight(), mPopoImage.getWidth(), mPopoImage.getHeight(), 0, 0, mPopoImage.getWidth(), mPopoImage.getHeight(), 0, 0, 0);
        }
        stackRect = mPopoObj.destRect;

        mPopoObj.isAlive = true;
        ObjectGeneration(true);
        if (GameParams.loadingMask != null)
            if (!GameParams.loadingMask.isAlive) {
                GameParams.loadingMask.isAlive = true;
                GameParams.loadingMask.state = GameObj.State.step1;
            }
    }

    @Override
    protected void Update() {
        super.Update();
        if (DebugConfig.isFpsDebugOn) {
            mFpsText.message = "actual FPS: " + mGameEntry.actualFPS + " FPS (" + mGameEntry.fps
                    + ") " + (int) GameEntry.totalFrames;
        }

        for (f = mObjCollections.size() -1 ; f >= 0; f--) {
            mSubObj = (NormalFish) mObjCollections.get(f);
            mSubObj.Animation();
            if (!GameParams.loadingMask.isAlive && !GameParams.breakStageMask.isAlive) {
                if (mSubObj.smartMoveDown(GameParams.screenRect.height())) {
                    mObjCollections.remove(mSubObj);
                    mSubObj.recycle();
                }
            }

            if (!GameParams.gameOverMask.isAlive) {
                if (mSubObj.isStackable) {
                    if (GameParams.isCollisionFromTop(stackRect, mSubObj.destRect)) {
                        if (mCakes.size() < GameParams.stage5BreakScore) {
                            mCakes.add(mSubObj);
                            mObjCollections.remove(mSubObj);
                        }
                    }
                } else {
                    if (GameParams.isCollision(mPopoObj.destRect, mSubObj.destRect)) {
                        if (!mSubObj.readyToDeath) {
                            Life1.addLife(mSubObj.getLifeAdd());
                            mTimerBar.addTimer(mSubObj.getTimerAdd());
                            GameParams.stage5TotalScore += mSubObj.getTouchScore();
                            mSubObj.readyToDeath = true;
                            if (mSubObj.getLifeAdd() < 0)
                                GameParams.vibrator.vibrate(50);
                        }
                    }
                }
            }

            if (!mSubObj.isAlive) {
                mObjCollections.remove(mSubObj);
                mSubObj.recycle();
            }
        }

        if (!GameParams.loadingMask.isAlive) {
            if (mScore != null)
                mScore.setTotalScore(GameParams.stage5TotalScore);

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

            if (GameParams.isGameOver) {
                if (GameParams.gameOverMask.Action((int) GameEntry.totalFrames)) {
                    Message m = new Message();
                    m.what = Events.RESTART_STAGE;
                    m.obj = View.VISIBLE;
                    MainActivity.mMsgHandler.sendMessage(m);
                }
            } else if (!GameParams.isGameOver && mCakes.size() >= GameParams.stage5BreakScore) {
                if (GameParams.breakStageMask.state == GameObj.State.step1) {
                    ObjectGeneration(false);
                    SharedPreferences settings = mGameEntry.mMainActivity.getSharedPreferences(GameParams.STAGES_COMPLETED, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean(GameParams.STAGE5_COMPLETED, true);
                    editor.apply();
                }
                if (GameParams.breakStageMask.Action((int) GameEntry.totalFrames))
                    NotifyStageCompleted();
            }
        } else {
            if (GameParams.loadingMask.Action((int) GameEntry.totalFrames)) {
                mTimerBar.addRunningFrame(GameParams.loadingMask.getDelayFrame());
                Message m = new Message();
                m.what = Events.BREAK_STAGE;
                m.obj = View.VISIBLE;
                MainActivity.mMsgHandler.sendMessage(m);
            }
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

        if (mForeground.isAlive)
        {
            mSubCanvas.drawBitmap(mForeGroundImage, mForeground.srcRect, mForeground.destRect, null);
        }

        for (f = mObjCollections.size() -1 ; f >= 0; f--) {
            mSubObj = (NormalFish) mObjCollections.get(f);
            if (mSubObj.isAlive) {
                mSubCanvas.drawBitmap(mSubObj.image, mSubObj.srcRect, mSubObj.destRect, mSubObj.paint);
            }

        }

        for (f = 0 ; f < mCakes.size(); f++) {
            mSubObj = mCakes.get(f);
            if (mSubObj.isAlive) {
                mSubCanvas.drawBitmap(mSubObj.image, mSubObj.srcRect, mSubObj.destRect, mSubObj.paint);
            }

        }

        if ((GameParams.isGameOver) && GameParams.gameOverMask.isAlive) {
            mSubCanvas.drawRect(GameParams.gameOverMask.MaskDestRect, GameParams.gameOverMask.paint);
            GameParams.gameOverMask.draw(mSubCanvas);
        } else if (!GameParams.isGameOver && GameParams.breakStageMask.isAlive) {
            mSubCanvas.drawRect(GameParams.breakStageMask.MaskDestRect, GameParams.breakStageMask.paint);
            GameParams.breakStageMask.draw(mSubCanvas);
        }

        if (GameParams.loadingMask != null && GameParams.loadingMask.isAlive) {
            mSubCanvas.drawRect(GameParams.loadingMask.MaskDestRect, GameParams.loadingMask.paint);
            GameParams.loadingMask.draw(mSubCanvas);
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
        if (mCakes != null)
            mCakes.clear();
        ObjectGeneration(false);
    }
}
