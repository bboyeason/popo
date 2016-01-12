package com.nosae.game.popo;

import com.nosae.game.objects.GameObj;
import com.nosae.game.objects.Music;
import com.nosae.game.settings.DebugConfig;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import java.lang.ref.WeakReference;

import lbs.DrawableGameComponent;


public class MainActivity extends Activity implements DrawableGameComponent.OnStageCompleteListener{

    public static Handler mMsgHandler;
    public static SurfaceView mSurfaceView;

    public ToggleButton mToggleButton;
    public Button mRestartButton;

    public GameEntry mGameEntry;
    protected int stage;

    static class MsgHandler extends Handler {
        private WeakReference<Activity> mActivity;
        MsgHandler(Activity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Activity activity = mActivity.get();
            if (activity != null) {
                ((MainActivity)activity).handleMessage(msg);
            }
        }
    }

    private void handleMessage(Message msg) {
        DebugConfig.d("Handle message: " + msg.what);
        switch (msg.what) {
            case Events.RESTART_STAGE:
                mRestartButton.setVisibility(View.VISIBLE);
                mToggleButton.setVisibility(View.INVISIBLE);
                break;
            case Events.BREAK_STAGE:
                if (msg.obj != null)
                mToggleButton.setVisibility((Integer) msg.obj);
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMsgHandler = new MsgHandler(this);
        Initialize();
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceViewTest);

        mRestartButton = (Button) findViewById(R.id.restartButton);
        mRestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Music.playSound();
                if (GameStateClass.currentState == GameStateClass.GameState.Stage1) {
                    GameStateClass.changeState(GameStateClass.GameState.None, mGameEntry.mStage1, mGameEntry);
                    // FIXME don't use sleep
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    GameStateClass.changeState(GameStateClass.GameState.Stage1, null, mGameEntry);
                } else if (GameStateClass.currentState == GameStateClass.GameState.Stage2) {
                    GameStateClass.changeState(GameStateClass.GameState.None, mGameEntry.mStage2, mGameEntry);
                    // FIXME don't use sleep
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    GameStateClass.changeState(GameStateClass.GameState.Stage2, null, mGameEntry);
                } else if (GameStateClass.currentState == GameStateClass.GameState.Stage3) {
                    GameStateClass.changeState(GameStateClass.GameState.None, mGameEntry.mStage3, mGameEntry);
                    // FIXME don't use sleep
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    GameStateClass.changeState(GameStateClass.GameState.Stage3, null, mGameEntry);
                } else if (GameStateClass.currentState == GameStateClass.GameState.Stage4) {
                    GameStateClass.changeState(GameStateClass.GameState.None, mGameEntry.mStage4, mGameEntry);
                    // FIXME don't use sleep
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    GameStateClass.changeState(GameStateClass.GameState.Stage4, null, mGameEntry);
                } else if (GameStateClass.currentState == GameStateClass.GameState.Stage5) {
                    GameStateClass.changeState(GameStateClass.GameState.None, mGameEntry.mStage5, mGameEntry);
                    // FIXME don't use sleep
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    GameStateClass.changeState(GameStateClass.GameState.Stage5, null, mGameEntry);
                }
                mRestartButton.setVisibility(View.INVISIBLE);
                mToggleButton.setVisibility(View.VISIBLE);
            }

        });
        mToggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mToggleButton.getLayoutParams();
        layoutParams.rightMargin = 30;
        layoutParams.topMargin = (int) (20 * GameParams.density);
        mToggleButton.setLayoutParams(layoutParams);
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mToggleButton.setPivotX(mToggleButton.getWidth() >> 1);
                mToggleButton.setPivotY(mToggleButton.getHeight() >> 1);
                Animation amScale = new ScaleAnimation(1.0f, 0.7f, 1.0f, 0.7f, mToggleButton.getWidth() / 2, mToggleButton.getHeight() / 2);
                amScale.setDuration(100);
                mToggleButton.startAnimation(amScale);
                mToggleButton.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mToggleButton.setEnabled(true);
                    }
                }, 500);
                Music.playSound();
                if (mGameEntry != null) {
                    if (isChecked) {
                        mGameEntry.Exit();
                    } else {
                        GameParams.loadingMask.isAlive = true;
                        GameParams.loadingMask.state = GameObj.State.step1;
                        mGameEntry.Run();
                    }
                }
            }
        });
//        mSurfaceView = new GameSurfaceView(this);
    }

    @Override
    protected void onStart() {
        DebugConfig.d("MainActivity onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        DebugConfig.d("MainActivity onResume()");
        super.onResume();
        if (mGameEntry.mStage1 != null)
            mGameEntry.mStage1.registerOnStageCompleteListener(this);
        if (mGameEntry.mStage2 != null)
            mGameEntry.mStage2.registerOnStageCompleteListener(this);
        if (mGameEntry.mStage3 != null)
            mGameEntry.mStage3.registerOnStageCompleteListener(this);
        if (mGameEntry.mStage4 != null)
            mGameEntry.mStage4.registerOnStageCompleteListener(this);
        if (mGameEntry.mStage5 != null)
            mGameEntry.mStage5.registerOnStageCompleteListener(this);
    }

    @Override
    protected void onPause() {
        DebugConfig.d("MainActivity onPause()");
        GameParams.vibrator.cancel();
        mToggleButton.setChecked(true);
        if (mGameEntry.mStage1 != null)
            mGameEntry.mStage1.unregisterOnStageCompleteListener(this);
        if (mGameEntry.mStage2 != null)
            mGameEntry.mStage2.unregisterOnStageCompleteListener(this);
        if (mGameEntry.mStage3 != null)
            mGameEntry.mStage3.unregisterOnStageCompleteListener(this);
        if (mGameEntry.mStage4 != null)
            mGameEntry.mStage4.unregisterOnStageCompleteListener(this);
        if (mGameEntry.mStage5 != null)
            mGameEntry.mStage5.unregisterOnStageCompleteListener(this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        DebugConfig.d("MainActivity onStop()");
        if (GameParams.msgHandler != null) {
            GameParams.onOff = false;
            GameParams.msgHandler.removeMessages(Events.CREATE_FISH);
            GameParams.msgHandler.removeMessages(Events.CREATE_OBJECT);
            GameParams.msgHandler.removeMessages(Events.CREATE_CAKE);
        }
        if (GameParams.music != null) {
            GameParams.music = null;
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        DebugConfig.d("MainActivity onDestroy()");
        super.onDestroy();
    }

    private void Initialize() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        GameParams.vibrator = (Vibrator) getSystemService(
                Service.VIBRATOR_SERVICE);

        GameParams.res = getResources();

        Intent intent = getIntent();
        stage = intent.getIntExtra(GameParams.STAGE, 0);
        DebugConfig.d("MainActivity start stage: " + stage);

        mGameEntry = new GameEntry(this);
        GameStateClass.oldState = GameStateClass.GameState.None;
        switch (stage) {
            case 1:
                GameStateClass.currentState = GameStateClass.GameState.Stage1;
                break;
            case 2:
                GameStateClass.currentState = GameStateClass.GameState.Stage2;
                break;
            case 3:
                GameStateClass.currentState = GameStateClass.GameState.Stage3;
                break;
            case 4:
                GameStateClass.currentState = GameStateClass.GameState.Stage4;
                break;
            case 5:
                GameStateClass.currentState = GameStateClass.GameState.Stage5;
                break;
        }
    }

    @Override
    public void OnStageComplete(DrawableGameComponent stage) {
        DebugConfig.d(stage.toString() + " is completed.");
        finish();
    }
}
