package com.nosae.game.bobo;

import com.nosae.game.sence.Stage1;
import com.nosae.game.sence.Stage2;
import com.nosae.game.settings.DebugConfig;
import android.app.Activity;
import android.app.Service;
import android.graphics.Rect;
import android.os.*;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;


public class MainActivity extends Activity {
    private final static String TAG = "BoBo";
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

        mRestartButton = (Button) findViewById(R.id.restartButton);
        mRestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                }
                mRestartButton.setVisibility(View.INVISIBLE);
            }

        });
        mToggleButton = (ToggleButton) findViewById(R.id.toggleButton);
//        mToggleButton.setVisibility(View.INVISIBLE);
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                mToggleButton.setChecked(isChecked);
                if (mGameEntry != null) {
                    if (isChecked) {
//                        Stage1.isRunning = true;

                        Stage1.onOff = false;
                        Stage1.mHandler.removeMessages(com.nosae.game.bobo.Events.CREATEFISH);
                        mGameEntry.Exit();


                    } else {
//                        Stage1.isRunning = false;

                        mGameEntry.Run();

                        Stage1.onOff = true;
                        Message msg = new Message();
                        msg.what = com.nosae.game.bobo.Events.CREATEFISH;
                        Stage1.mHandler.sendMessageDelayed(msg, 150);
                    }
                }
            }
        });
//        mSurfaceView = new GameSurfaceView(this);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceViewTest);

        Initialize();


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
        if (mGameEntry != null)
        {
            /*
            if (GameStateClass.currentState != GameState.Menu_drawable)
            {
                if (GV.music != null)
                    GV.music.Pause();
            }
            */


            mGameEntry.Exit();

        }

        super.onPause();
    }

    @Override
    protected void onStop() {
        DebugConfig.d("MainActivity onStop()");
//            mToggleButton.setChecked(true);
        if (Stage1.mHandler != null) {
            Stage1.onOff = false;
            Stage1.mHandler.removeMessages(com.nosae.game.bobo.Events.CREATEFISH);
        }
        if (Stage2.mHandler != null) {
            Stage2.onOff = false;
            Stage2.mHandler.removeMessages(com.nosae.game.bobo.Events.CREATEFISH);
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
    }

    private void Initialize() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        GameParams.vibrator = (Vibrator) getSystemService(
                Service.VIBRATOR_SERVICE);

        GameParams.res = getResources();
        // acquire screen size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        GameParams.scaleWidth = dm.widthPixels;
        GameParams.scaleHeight = dm.heightPixels;
        GameParams.halfWidth = GameParams.scaleWidth >> 1;
        GameParams.halfHeight = GameParams.scaleHeight >> 1;
        GameParams.density = dm.density;
        GameParams.densityDpi = dm.densityDpi;
        GameParams.screenRect = new Rect(0, 0, GameParams.scaleWidth, GameParams.scaleHeight);
        GameParams.screenRectBoundary = new Rect( 0 - GameParams.boundary, 0 - GameParams.boundary, GameParams.scaleWidth + GameParams.boundary, GameParams.scaleHeight + GameParams.boundary);
        DebugConfig.d("Screen size: " + GameParams.screenRect.width() + " x " + GameParams.screenRect.height());
        DebugConfig.d("Screen size: " + dm.widthPixels + " x " + dm.heightPixels + ", density: " + dm.density + ", density dpi: " + dm.densityDpi);

        mGameEntry = new GameEntry(this);
        GameStateClass.currentState = GameStateClass.GameState.Stage1;
        GameStateClass.oldState = GameStateClass.GameState.None;



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


/*    @Override
    public boolean onCreateOptionsMenu(Menu_drawable menu) {
        // Inflate the Menu_drawable; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

}
