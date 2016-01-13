package com.nosae.game.scene;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.nosae.game.popo.Events;
import com.nosae.game.popo.GameEntry;
import com.nosae.game.popo.GameParams;
import com.nosae.game.popo.MainActivity;
import com.nosae.game.popo.R;
import com.nosae.game.popo.Text;
import com.nosae.game.objects.FishCollection;
import com.nosae.game.objects.GameObj;
import com.nosae.game.objects.GameObj.State;
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
public class Stage3 extends DrawableGameComponent {

    private final GameEntry mGameEntry;

    private GameObj mBackground;
    private Bitmap mBackGroundImage;
    private Bitmap mForeGroundImage;

    private Score mScore;

    private Text mFpsText;

    public TimerBar2 mTimerBar;
    public Bitmap mTimerBarImage;

    private Popo mPopoObj;
    private Bitmap mPopoImage;

    private GameObj mLifeIcon;
    private Bitmap mLifeImage;
    private Life1 mLife1;

    private Random mRandom;

    private NormalFish mSubObj;
    public static FishCollection mObjCollections;

    private int[][] mHidingTable = {
            {14, 355, 56, 410},
            {0, 468, 95, 562},
            {337,204 ,383, 296},
            {330, 355, 396, 392},
            {288, 493, 325, 547}
    };
    public static ArrayList<GameObj> mHidingObj;
    private Rect mLimitRect;

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
        GameParams.isClearStage3 = true;
        if (mOnStageCompleteListeners.size() > 0) {
            for (OnStageCompleteListener l : mOnStageCompleteListeners) {
                l.OnStageComplete(this);
            }
        }
    }

    public Stage3(GameEntry gameEntry) {
        DebugConfig.d("Stage3 Constructor");
        this.mGameEntry = gameEntry;
        mOnStageCompleteListeners = new ArrayList<>();
        GameParams.msgHandler = new MsgHandler(this);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case Events.CREATE_OBJECT:
                if (GameParams.isGameOver || GameParams.isClearStage3)
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
    /*
    public void CreateObj(int[][] objectTable) {
        int width, height;
        Bitmap objImage;

        for (int i = 0; i < objectTable[0].length; i++) {
            try {
                objImage = BitmapFactory.decodeResource(GameParams.res, objectTable[0][i]);
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
    */
    private void CreateSpecialObjects(int[][] objectTable) {
        if (GameParams.loadingMask.isAlive || mGameEntry.mMainActivity.mToggleButton.isChecked())
            return;
        int width, height;
        int random;
        Random _random = new Random();
        random = _random.nextInt(objectTable[0].length);
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
        NormalFish mObj = new NormalFish(objImage, 0, 0, width, height, 0, 0, width, height, 0, Color.WHITE, 90);
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
        mHidingObj = new ArrayList<>();
        mObjCollections = new FishCollection();
        GameParams.isClearStage3 = false;
        GameParams.gameOverMask.state = State.step1;
    }
    public static void ObjectGeneration(boolean produce) {
        GameParams.onOff = produce;
        if (GameParams.onOff) {
            Message msg = new Message();
            msg.what = Events.CREATE_OBJECT;
            GameParams.msgHandler.sendMessageDelayed(msg, 5000);
        } else {
            GameParams.msgHandler.removeMessages(Events.CREATE_OBJECT);
        }
    }
    @Override
    protected void LoadContent() {
        super.LoadContent();
        DebugConfig.d("Stage3 LoadContent()");
        int width, height;

        if (mBackground == null) {
            mBackGroundImage = GameParams.decodeSampledBitmapFromResource(R.drawable.c_original, GameParams.scaleWidth, GameParams.scaleHeight);
            mBackground = new GameObj(0, 0, GameParams.scaleWidth, GameParams.scaleHeight, 0, 0, mBackGroundImage.getWidth(), mBackGroundImage.getHeight(), 0, 0, 0);
        }
        mBackground.isAlive = true;

        BitmapFactory.Options dimensions = new BitmapFactory.Options();
        dimensions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(GameParams.res, R.drawable.c_testing, dimensions);
        width =  dimensions.outWidth;
        height = dimensions.outHeight;
        DebugConfig.d("Background image size: " + width + " x " + height + " pixels");
        if (mForeGroundImage == null) {
            mForeGroundImage = BitmapFactory.decodeResource(GameParams.res, R.drawable.c_testing);
            GameObj _obj;
            for (int[] aHidingTable : mHidingTable) {
                _obj = new GameObj();
                _obj.srcRect = new Rect((aHidingTable[0] * GameParams.densityDpi / 160), (aHidingTable[1] * GameParams.densityDpi / 160), (aHidingTable[2] * GameParams.densityDpi / 160), (aHidingTable[3] * GameParams.densityDpi / 160));
                _obj.destRect = new Rect((aHidingTable[0] * GameParams.scaleWidth / width), (aHidingTable[1] * GameParams.scaleHeight / height), (aHidingTable[2] * GameParams.scaleWidth / width), (aHidingTable[3] * GameParams.scaleHeight / height));
                mHidingObj.add(_obj);
            }
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
        // Can't assign GameParams.screenRect to mLimitRect,
        // or modify mLimitRect.top will also modify GameParams.screenRect.top
        mLimitRect = new Rect(GameParams.screenRect);
        mLimitRect.top = mLifeIcon.destRect.bottom;

        if (mLife1 == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap numBitmap = BitmapFactory.decodeResource(GameParams.res, R.drawable.s_0, options);
            mLife1 = new Life1(mLifeIcon.destRect.right + (int) (10 * GameParams.density), mLifeIcon.destRect.bottom - mLifeIcon.halfHeight - (numBitmap.getHeight() >> 1), numBitmap.getWidth(), numBitmap.getHeight(), 0, 0, numBitmap.getWidth() * 2, numBitmap.getHeight() * 2);
            numBitmap.recycle();
        }
        Life1.setLife(GameParams.stage3Life);

        if (mTimerBar == null) {
            mTimerBarImage = BitmapFactory.decodeResource(GameParams.res, R.drawable.timer_bar);
            if (mTimerBarImage != null) {
                width = mTimerBarImage.getWidth();
                height = mTimerBarImage.getHeight() / GameParams.timerBarRowCount;
                mTimerBar = new TimerBar2(mScore.edge_X_right + 5, mScore.getY() + (mLife1.destRect.bottom - mScore.getY()) / 2 - (height >> 1), width, height, 0, 0, width, height, 0, 0, 0);
            }
        }
        if (mTimerBar != null) {
            mTimerBar.setTimer(GameParams.stage3RunningTime);
            mTimerBar.setStartFrame((int) GameEntry.totalFrames);
        }

        if (mPopoObj == null) {
            mPopoImage = BitmapFactory.decodeResource(GameParams.res, R.drawable.c_popo_01);
            mPopoObj = new Popo(mPopoImage, 0, GameParams.scaleHeight - mPopoImage.getHeight(), mPopoImage.getWidth(), mPopoImage.getHeight(), 0, 0, mPopoImage.getWidth(), mPopoImage.getHeight(), 0, 0, 0);
        }
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

        for (int f = mObjCollections.size() -1 ; f >= 0; f--) {
            mSubObj = (NormalFish) mObjCollections.get(f);
            mSubObj.Animation();

            if (!mSubObj.isAlive)
                mObjCollections.remove(mSubObj);
        }

        if (!GameParams.loadingMask.isAlive) {
            if (mScore != null)
                mScore.setTotalScore(GameParams.stage3TotalScore);

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
            } else if (!GameParams.isGameOver && GameParams.stage3TotalScore >= GameParams.stage3BreakScore) {
                if (GameParams.breakStageMask.state == GameObj.State.step1) {
                    ObjectGeneration(false);
                    SharedPreferences settings = mGameEntry.mMainActivity.getSharedPreferences(GameParams.STAGES_COMPLETED, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean(GameParams.STAGE3_COMPLETED, true);
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
        if (mBackground.isAlive)
        {
            for (int i = 0; i < mHidingObj.size(); i++) {
                mSubCanvas.drawBitmap(mForeGroundImage, mHidingObj.get(i).srcRect, mHidingObj.get(i).destRect, null);
            }
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

        if (!GameParams.loadingMask.isAlive) {
            for (int f = mObjCollections.size() -1 ; f >= 0; f--) {
                mSubObj = (NormalFish) mObjCollections.get(f);
                if (mSubObj.isAlive) {
                    mSubCanvas.drawBitmap(mSubObj.image, mSubObj.srcRect, mSubObj.destRect, mSubObj.paint);
                }
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

    @Override
    public void Dispose() {
        super.Dispose();
        if (mObjCollections != null)
            mObjCollections.clear();
        ObjectGeneration(false);
    }
}
