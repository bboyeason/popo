package com.nosae.game.bobo;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.nosae.game.objects.FishObj;
import com.nosae.game.objects.Life1;
import com.nosae.game.objects.Music;
import com.nosae.game.objects.Quiz;
import com.nosae.game.role.Stage2_fish;
import com.nosae.game.scene.Stage1;
import com.nosae.game.scene.Stage2;
import com.nosae.game.scene.Stage3;
import com.nosae.game.settings.DebugConfig;

/**
 * Created by eason on 2015/10/10.
 */
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    public SurfaceHolder mSurfaceHolder;
    private static Handler mMsgHandler;
    private MainActivity mMainActivity;
    private int f;
    private int mStage2Hit = 0x00;
    private int mStage2HitColor = 0x01;
    private int mStage2HitSyllable = 0x10;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mMainActivity.mToggleButton.isChecked() || Stage1.isGameOver || Stage2.isGameOver)
            return false;
        final float x = event.getX();
        final float y = event.getY();
        final int action = event.getAction();
        FishObj _object;
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                DebugConfig.d("touch down: " + x + ", " + y);
/*                if (GameStateClass.currentState == GameStateClass.GameState.Menu_drawable) {
                    GameStateClass.changeState(GameStateClass.GameState.Stage1, mMainActivity.mGameEntry.mMenuDrawable, mMainActivity.mGameEntry);
                    return false;
                }*/
                switch (GameStateClass.currentState) {
                    case Stage1:
                        if (Stage1.mFishCollections == null)
                            return false;
                        for (f = 0; f < Stage1.mFishCollections.size(); f++) {
                            _object = Stage1.mFishCollections.get(f);
                            if (_object.destRect.contains((int) x, (int) y)) {
                                DebugConfig.d("Hit!!");
                                if (!_object.readyToDeath) {
                                    if (_object.getTouchScore() >= 0) {
                                        GameParams.stage1TotalScore += _object.getTouchScore();
                                        Music.playSound();
                                    } else if (_object.getTouchScore() == -1) {
                                        GameParams.vibrator.vibrate(50);
                                    }
                                    mMainActivity.mGameEntry.mStage1.mTimerBar.addTimer(_object.getTimerAdd());
                                    Life1.addLife(_object.getLifeAdd());
                                }
                                /*if (Stage1.stage1TotalScore < 0) {
                                    Stage1.stage1TotalScore = 0;
                                    mMainActivity.mGameEntry.mStage1.mBoboObj.isAlive = false;
                                }*/
                                _object.readyToDeath = true;
                                break;
                            }
                        }
                        break;
                    case Stage2:
                        if (Stage2.mFishCollections == null)
                            return false;
                        for (f = 0; f < Stage2.mFishCollections.size(); f++) {
                            _object = Stage2.mFishCollections.get(f);
                            if (_object.destRect.contains((int) x, (int) y)) {
                                if (!Stage2.mFishCollections.get(f).readyToDeath) {
                                    if (((Stage2_fish) _object).getColor() == Quiz.quizTable[Quiz.currentQuiz].color) {
                                        DebugConfig.d("Hit color!!!");
                                        GameParams.stage2TotalScore += 10;
                                        mStage2Hit |= mStage2HitColor;
                                    } else if (!(_object.getLifeAdd() > 0 || _object.getTimerAdd() > 0)) {
                                        Life1.addLife(-1);
                                        GameParams.vibrator.vibrate(50);
                                    }
                                    if (((Stage2_fish) _object).getSyllable() == Quiz.quizTable[Quiz.currentQuiz].syllable) {
                                        DebugConfig.d("Hit syllable!!!");
                                        GameParams.stage2TotalScore += 10;
                                        mStage2Hit |= mStage2HitSyllable;
                                    } else if (!(_object.getLifeAdd() > 0 || _object.getTimerAdd() > 0)) {
                                        Life1.addLife(-1);
                                        GameParams.vibrator.vibrate(50);
                                    }

                                    if (mStage2Hit != 0x00) {
                                        // TODO when hit, new quiz
                                        Stage2.isQuizHit = true;
                                        mStage2Hit = 0x00;
                                    }
                                    if (_object.getTimerAdd() > 0)
                                        mMainActivity.mGameEntry.mStage2.mTimerBar.addTimer(_object.getTimerAdd());
                                    if (_object.getLifeAdd() > 0)
                                        Life1.addLife(_object.getLifeAdd());
                                }
                                _object.readyToDeath = true;
                                break;
                            }
                        }
                        break;
                    case Stage3:
                        for (f = 0; f < Stage3.mObjCollections.size(); f++) {
                            _object = Stage3.mObjCollections.get(f);
                            if (_object.destRect.contains((int) x, (int) y)) {
                                if (!_object.readyToDeath) {
                                    GameParams.stage3TotalScore += _object.getTouchScore();
//                                    _object.isAlive = false;
                                    DebugConfig.d("Hit object, score: " + _object.getTouchScore());

                                    mMainActivity.mGameEntry.mStage3.mTimerBar.addTimer(_object.getTimerAdd());
                                    Life1.addLife(_object.getLifeAdd());
                                    _object.readyToDeath = true;
                                }
                                return true;
                            }
                        }
                        Life1.addLife(-1);
                        DebugConfig.d("no Hit!!!");
                        break;
                }
                break;
        }
        return false;
    }


    // Update object
    protected interface Events {
        int UPDATE_FISH = 0;
        int UPDATE_BOBO = 1;
        int UPDATE_POINT = 2;
        int UPDATE_SOMETHING = 3;
    }

    public GameSurfaceView(Context context) {
        super(context);
//        mMainActivity = (MainActivity)context;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
    }
    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        if (isInEditMode()) {
            mMainActivity = (MainActivity) context;
//        }
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
    }

//    public void init() {};
/*    public GameSurfaceView(MainActivity context) {
        super(context);
        mMainActivity = context;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
    }*/

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        DebugConfig.d("=== surfaceCreated ===" + MainActivity.mSurfaceView.getLeft() + ":" + MainActivity.mSurfaceView.getRight() + ":" + MainActivity.mSurfaceView.getTop() + ":" + MainActivity.mSurfaceView.getBottom());
        mMainActivity.mGameEntry.Run();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
