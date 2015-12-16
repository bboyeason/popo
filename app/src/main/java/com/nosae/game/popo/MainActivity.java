package com.nosae.game.popo;

import com.nosae.game.objects.Music;
import com.nosae.game.scene.Stage1;
import com.nosae.game.scene.Stage2;
import com.nosae.game.scene.Stage3;
import com.nosae.game.scene.Stage4;
import com.nosae.game.scene.Stage5;
import com.nosae.game.settings.DebugConfig;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import lbs.DrawableGameComponent;


public class MainActivity extends Activity implements DrawableGameComponent.OnStageCompleteListener{
//    private static Handler mHandler;
    protected static Handler mMsgHandler;
//    private static HandlerThread mHandlerThread;

    public static SurfaceView mSurfaceView;


    ToggleButton mToggleButton;
    public Button mRestartButton;

    public GameEntry mGameEntry;
    protected int stage;

/*    static class MsgHandler extends Handler {
        private WeakReference<Activity> mActivity;
        MsgHandler(Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Activity activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }

    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mHandler = new Handler();
//        if (mHandlerThread == null) {
//            mHandlerThread = new HandlerThread("sdfasdfasd",
//                    android.os.Process.THREAD_PRIORITY_BACKGROUND);
//            mHandlerThread.start();
//            DebugConfig.d("Create message handler thread.");
//        }
        Initialize();
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceViewTest);

        mRestartButton = (Button) findViewById(R.id.restartButton);
        mRestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Music.playSound();
//                mGameEntry.isForceRestart = true;
//                mGameEntry.Exit();
//                mGameEntry.Run();
                if (GameStateClass.currentState == GameStateClass.GameState.Stage1) {
                    GameParams.isGameOver = false;
                    GameStateClass.changeState(GameStateClass.GameState.None, mGameEntry.mStage1, mGameEntry);
                    // FIXME don't use sleep
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    GameStateClass.changeState(GameStateClass.GameState.Stage1, null, mGameEntry);
                } else if (GameStateClass.currentState == GameStateClass.GameState.Stage2) {
                    GameParams.isGameOver = false;
                    GameStateClass.changeState(GameStateClass.GameState.None, mGameEntry.mStage2, mGameEntry);
                    // FIXME don't use sleep
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    GameStateClass.changeState(GameStateClass.GameState.Stage2, null, mGameEntry);
                } else if (GameStateClass.currentState == GameStateClass.GameState.Stage3) {
                    GameParams.isGameOver = false;
                    GameStateClass.changeState(GameStateClass.GameState.None, mGameEntry.mStage3, mGameEntry);
                    // FIXME don't use sleep
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    GameStateClass.changeState(GameStateClass.GameState.Stage3, null, mGameEntry);
                } else if (GameStateClass.currentState == GameStateClass.GameState.Stage4) {
                    GameParams.isGameOver = false;
                    GameStateClass.changeState(GameStateClass.GameState.None, mGameEntry.mStage4, mGameEntry);
                    // FIXME don't use sleep
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    GameStateClass.changeState(GameStateClass.GameState.Stage4, null, mGameEntry);
                } else if (GameStateClass.currentState == GameStateClass.GameState.Stage5) {
                    GameParams.isGameOver = false;
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
                Music.playSound();
                if (mGameEntry != null) {
                    if (isChecked) {
                        mGameEntry.Exit();
                    } else {
                        mGameEntry.Run();
                    }
                    switch (GameStateClass.currentState) {
                        case Stage1:
                            Stage1.FishGeneration(!isChecked);
                            break;
                        case Stage2:
                            Stage2.FishGeneration(!isChecked);
                            break;
                        case Stage3:
                            Stage3.ObjectGeneration(!isChecked);
                            break;
                        case Stage4:
                            Stage4.ObjectGeneration(!isChecked);
                            break;
                        case Stage5:
                            Stage5.ObjectGeneration(!isChecked);
                            break;
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
//        mToggleButton.setChecked(false);
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
//        if (mGameEntry != null)
//        {
//            mGameEntry.Exit();
//        }

        super.onPause();
    }

    @Override
    protected void onStop() {
        DebugConfig.d("MainActivity onStop()");
//            mToggleButton.setChecked(true);
        if (Stage1.mHandler != null) {
            Stage1.onOff = false;
            Stage1.mHandler.removeMessages(Events.CREATE_FISH);
            Stage1.mHandler.removeMessages(Events.CREATE_OBJECT);
        }
        if (Stage2.mHandler != null) {
            Stage2.onOff = false;
            Stage2.mHandler.removeMessages(Events.CREATE_FISH);
            Stage2.mHandler.removeMessages(Events.CREATE_OBJECT);
        }
        if (Stage3.mHandler != null) {
            Stage3.onOff = false;
            Stage3.mHandler.removeMessages(Events.CREATE_OBJECT);
        }
        if (Stage4.mHandler != null) {
            Stage4.onOff = false;
            Stage4.mHandler.removeMessages(Events.CREATE_FISH);
            Stage4.mHandler.removeMessages(Events.CREATE_OBJECT);
        }
        if (Stage5.mHandler != null) {
            Stage5.onOff = false;
            Stage5.mHandler.removeMessages(Events.CREATE_FISH);
            Stage5.mHandler.removeMessages(Events.CREATE_OBJECT);
            Stage5.mHandler.removeMessages(Events.CREATE_CAKE);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        DebugConfig.d("MainActivity onDestroy()");
        super.onDestroy();
//        if (mHandlerThread != null) {
//            mHandlerThread.interrupt();
//            mHandlerThread.quit();
//            mHandlerThread = null;
//        }
        if (Stage1.mHandlerThread != null) {
            DebugConfig.d("mHandlerThread " + Stage1.mHandlerThread.getThreadId());
            Stage1.mHandlerThread.interrupt();
            Stage1.mHandlerThread.quit();
            Stage1.mHandlerThread = null;
        }
        if (Stage2.mHandlerThread != null) {
            DebugConfig.d("mHandlerThread " + Stage2.mHandlerThread.getThreadId());
            Stage2.mHandlerThread.interrupt();
            Stage2.mHandlerThread.quit();
            Stage2.mHandlerThread = null;
        }
        if (Stage3.mHandlerThread != null) {
            DebugConfig.d("mHandlerThread " + Stage3.mHandlerThread.getThreadId());
            Stage3.mHandlerThread.interrupt();
            Stage3.mHandlerThread.quit();
            Stage3.mHandlerThread = null;
        }
        if (Stage4.mHandlerThread != null) {
            DebugConfig.d("mHandlerThread " + Stage4.mHandlerThread.getThreadId());
            Stage4.mHandlerThread.interrupt();
            Stage4.mHandlerThread.quit();
            Stage4.mHandlerThread = null;
        }
        if (Stage5.mHandlerThread != null) {
            DebugConfig.d("mHandlerThread " + Stage5.mHandlerThread.getThreadId());
            Stage5.mHandlerThread.interrupt();
            Stage5.mHandlerThread.quit();
            Stage5.mHandlerThread = null;
        }
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

//        Looper looper = mHandlerThread.getLooper();
        mMsgHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                DebugConfig.d("Handle message: " + msg.what);
                switch (msg.what){
                    case 1:
                        mRestartButton.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };
    }

    @Override
    public void OnStageComplete(DrawableGameComponent stage) {
        DebugConfig.d(stage.toString() + " is completed.");
        finish();
    }
}
