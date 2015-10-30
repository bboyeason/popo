package com.nosae.game.bobo;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.nosae.game.objects.Quiz;
import com.nosae.game.sence.Stage1;
import com.nosae.game.sence.Stage2;
import com.nosae.game.settings.DebugConfig;

/**
 * Created by eason on 2015/10/10.
 */
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    public SurfaceHolder mSurfaceHolder;
    private static Handler mMsgHandler;
    private MainActivity mMainActivity;
    private int f;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mMainActivity.mToggleButton.isChecked())
            return false;
        final float x = event.getX();
        final float y = event.getY();
        final int action = event.getAction();
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
                            if (Stage1.mFishCollections.get(f).destRect.contains((int) x, (int) y)) {
                                DebugConfig.d("Hit!!");
                                if (!Stage1.mFishCollections.get(f).readyToDeath && Stage1.mFishCollections.get(f).getScore() > 0) {
                                    GameParams.vibrator.vibrate(50);
                                    mMainActivity.mGameEntry.mStage1.mTotalScore -= Stage1.mFishCollections.get(f).getScore();
                                }

                                mMainActivity.mGameEntry.mStage1.mTimerBar.addTime(Stage1.mFishCollections.get(f).getTimerAdd());
//                              mMainActivity.mGameEntry.mStage1.mTotalScore += Stage1.mFishCollections.get(f).getScore();
                                if (mMainActivity.mGameEntry.mStage1.mTotalScore < 0) {
                                    mMainActivity.mGameEntry.mStage1.mTotalScore = 0;
                                    mMainActivity.mGameEntry.mStage1.mBoboObj.isAlive = false;
                                }
//                              Stage1.mFishCollections.get(f).isAlive = false;
                                Stage1.mFishCollections.get(f).readyToDeath = true;
//                              Stage1.mFishCollections.remove(Stage1.mFishCollections.get(f));
                                break;
                            }
                        }
                        break;
                    case Stage2:
//                        Quiz.isQuizHit = true;
                        // TODO when hit, new quiz
                        Stage2.isNewQuiz = true;
                        break;
                }


                break;
        }
//        DebugConfig.d("Total score: " + mMainActivity.mGameEntry.mStage1.mTotalScore);
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
