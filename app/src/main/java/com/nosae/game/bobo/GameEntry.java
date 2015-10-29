package com.nosae.game.bobo;

import android.graphics.Color;
import android.os.Message;
import android.view.View;

import lbs.Game;
import objects.Music;
import sence.Stage1;
import sence.Stage2;
import settings.DebugConfig;

/**
 * Created by eason on 2015/10/19.
 */
public class GameEntry extends Game {
    public Stage1 mStage1;
    public Stage2 mStage2;
    public MainActivity mMainActivity;
    public boolean isForceRestart = false;

    public GameEntry(MainActivity mMainActivity) {
        DebugConfig.d("GameEntry Constructor");
        this.mMainActivity = mMainActivity;
    }

    @Override
    public void Run() {
        DebugConfig.d("GameEntry Run()");
        super.Run();
    }

    @Override
    public void Exit() {
        DebugConfig.d("GameEntry Exit()");
        if (GameParams.music != null && GameParams.music.player.isPlaying())
            GameParams.music.Stop();
        super.Exit();
    }

    @Override
    protected void Initialize() {
        for (int f = 0; f < 360; f++) {
            GameParams.Cosine[f] = (float) Math.cos(f * Math.PI / 180);
            GameParams.Sine[f] = (float) Math.sin(f * Math.PI / 180);
        }

        super.Initialize();
    }

    @Override
    protected void LoadContent() {
        super.LoadContent();
    }

    @Override
    protected void UnloadContent() {
        super.UnloadContent();
    }

    @Override
    protected void Update() {
//        DebugConfig.d("GameEntry Update()");
        if (Stage1.isGameOver && (mMainActivity.mRestartButton.getVisibility() == View.INVISIBLE)) {
            Message m = new Message();
            m.what = 1;
            mMainActivity.mMsgHandler.sendMessage(m);
        }

        if (mStage1 != null && mStage1.mTotalScore >= GameParams.stage1BreakScore
                && GameStateClass.currentState != GameStateClass.GameState.Stage2) {
//                    GameStateClass.currentState = GameStateClass.GameState.Stage2;
            GameStateClass.changeState(GameStateClass.GameState.Stage2, mStage1, this);
        }
//        if (isForceRestart)
//            GameStateClass.changeState(GameStateClass.GameState.Stage1, mStage1, this);


        if (GameStateClass.currentState != GameStateClass.oldState || isForceRestart) {
            switch (GameStateClass.currentState) {
                case None:
                    DebugConfig.d("None Stage");

//                    bee.UnloadContent();

                    break;
                case Menu:
//                    mMenuDrawable = new Menu_drawable(this);
//                    Components.add(mMenuDrawable);

                    break;
                case Stage1:
                    DebugConfig.d("new Stage1");
                    mStage1 = new Stage1(this);
                    Components.add(mStage1);

                    if (GameParams.music == null) {
//                        GameParams.music.player.release();
                        GameParams.music = new Music(mMainActivity, R.raw.stage1, 1);

                        GameParams.music.Play();
                    }

                    break;

                case Stage2:
//                    Components.remove(mStage1);
                    // TODO quit fish thread
                    // TODO animation for stage switch
                    mStage2 = new Stage2(this);
                    Components.add(mStage2);
            }

            GameStateClass.oldState = GameStateClass.currentState;
            DebugConfig.d("oldState: " + GameStateClass.oldState + " currentState: " + GameStateClass.currentState);
        }

        super.Update();
    }

    @Override
    protected void Draw() {
//        canvas = GV.surface.mHolder.lockCanvas(null);
        canvas = mMainActivity.mSurfaceView.getHolder().lockCanvas(null);

        // 清空
        canvas.drawColor(Color.BLACK);
        super.Draw();
    }
}
