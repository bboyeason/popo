package com.nosae.game.popo;

import com.nosae.game.objects.Music;
import com.nosae.game.scene.Stage1;
import com.nosae.game.scene.Stage2;
import com.nosae.game.scene.Stage3;
import com.nosae.game.scene.Stage4;
import com.nosae.game.settings.DebugConfig;
import android.app.Activity;
import android.app.Service;
import android.os.*;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;


public class MainActivity extends Activity {
    private final static String TAG = "PoPo";
//    private static Handler mHandler;
    protected static Handler mMsgHandler;
//    private static HandlerThread mHandlerThread;

    public static SurfaceView mSurfaceView;


    ToggleButton mToggleButton;
    public Button mRestartButton;

    public GameEntry mGameEntry;


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

        DebugConfig.setTag(TAG);
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
                    Stage1.isGameOver = false;
                    GameStateClass.changeState(GameStateClass.GameState.None, mGameEntry.mStage1, mGameEntry);
                    // FIXME don't use sleep
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    GameStateClass.changeState(GameStateClass.GameState.Stage1, null, mGameEntry);
                } else if (GameStateClass.currentState == GameStateClass.GameState.Stage2) {
                    Stage2.isGameOver = false;
                    GameStateClass.changeState(GameStateClass.GameState.None, mGameEntry.mStage2, mGameEntry);
                    // FIXME don't use sleep
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    GameStateClass.changeState(GameStateClass.GameState.Stage2, null, mGameEntry);
                } else if (GameStateClass.currentState == GameStateClass.GameState.Stage3) {
                    Stage3.isGameOver = false;
                    GameStateClass.changeState(GameStateClass.GameState.None, mGameEntry.mStage3, mGameEntry);
                    // FIXME don't use sleep
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    GameStateClass.changeState(GameStateClass.GameState.Stage3, null, mGameEntry);
                } else if (GameStateClass.currentState == GameStateClass.GameState.Stage4) {
                    Stage4.isGameOver = false;
                    GameStateClass.changeState(GameStateClass.GameState.None, mGameEntry.mStage4, mGameEntry);
                    // FIXME don't use sleep
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    GameStateClass.changeState(GameStateClass.GameState.Stage4, null, mGameEntry);
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
    }

    @Override
    protected void onPause() {
        DebugConfig.d("MainActivity onPause()");
        GameParams.vibrator.cancel();
        mToggleButton.setChecked(true);

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
            Stage1.mHandler.removeMessages(Events.CREATEFISH);
            Stage1.mHandler.removeMessages(Events.CREATESTAR);
        }
        if (Stage2.mHandler != null) {
            Stage2.onOff = false;
            Stage2.mHandler.removeMessages(Events.CREATEFISH);
            Stage2.mHandler.removeMessages(Events.CREATESTAR);
        }
        if (Stage3.mHandler != null) {
            Stage3.onOff = false;
            Stage3.mHandler.removeMessages(Events.CREATESTAR);
        }
        if (Stage4.mHandler != null) {
            Stage4.onOff = false;
            Stage4.mHandler.removeMessages(Events.CREATESTAR);
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
    }

    private void Initialize() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        GameParams.vibrator = (Vibrator) getSystemService(
                Service.VIBRATOR_SERVICE);

        GameParams.res = getResources();

        mGameEntry = new GameEntry(this);
        GameStateClass.oldState = GameStateClass.GameState.None;
        if (GameParams.isClearStage1) {
            if (GameParams.isClearStage2) {
                if (GameParams.isClearStage3)
                    GameStateClass.currentState = GameStateClass.GameState.Stage4;
                else
                    GameStateClass.currentState = GameStateClass.GameState.Stage3;
            } else {
                GameStateClass.currentState = GameStateClass.GameState.Stage2;
            }
        } else {
            GameStateClass.currentState = GameStateClass.GameState.Stage1;
        }


//        Looper looper = mHandlerThread.getLooper();
        mMsgHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                DebugConfig.d("Handle message: " + msg.what);
                switch (msg.what){
                    case 1:
//                        if (Stage1.isGameOver && mRestartButton.getVisibility() == View.INVISIBLE) {
                            mRestartButton.setVisibility(View.VISIBLE);
//                        }
                        break;
                }
            }
        };
    }
}
