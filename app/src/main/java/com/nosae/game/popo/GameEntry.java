package com.nosae.game.popo;

import android.graphics.Color;
import android.os.Message;
import android.view.View;

import lbs.Game;

import com.nosae.game.objects.AnimationMask;
import com.nosae.game.objects.Music;
import com.nosae.game.scene.Stage1;
import com.nosae.game.scene.Stage2;
import com.nosae.game.scene.Stage3;
import com.nosae.game.scene.Stage4;
import com.nosae.game.scene.Stage5;
import com.nosae.game.settings.DebugConfig;

/**
 * Created by eason on 2015/10/19.
 */
public class GameEntry extends Game {
    public Stage1 mStage1;
    public Stage2 mStage2;
    public Stage3 mStage3;
    public Stage4 mStage4;
    public Stage5 mStage5;
    public MainActivity mMainActivity;
    public boolean isForceRestart = false;
    boolean isGameOverOrBreakStageMusicPlaying;

    public GameEntry(MainActivity mMainActivity) {
        DebugConfig.d("GameEntry Constructor");
        this.mMainActivity = mMainActivity;
        totalFrames = 0;
        switch (mMainActivity.stage) {
            case 1:
                mStage1 = new Stage1(this);
                break;
            case 2:
                mStage2 = new Stage2(this);
                break;
            case 3:
                mStage3 = new Stage3(this);
                break;
            case 4:
                mStage4 = new Stage4(this);
                break;
            case 5:
                mStage5 = new Stage5(this);
                break;
        }
    }

    @Override
    public void Run() {
        DebugConfig.d("GameEntry Run()");
        if (GameParams.music != null)
            GameParams.music.Play();
        if (mStage4 != null)
            mStage4.registerListener();
        if (mStage5 != null)
            mStage5.registerListener();
        super.Run();
    }

    @Override
    public void Exit() {
        DebugConfig.d("GameEntry Exit()");
        if (GameParams.music != null && GameParams.music.player.isPlaying())
            GameParams.music.Pause();
        if (mStage4 != null)
            mStage4.unregisterListener();
        if (mStage5 != null)
            mStage5.unregisterListener();
        super.Exit();
    }

    @Override
    protected void Initialize() {
        for (int f = 0; f < 360; f++) {
            GameParams.Cosine[f] = (float) Math.cos(f * Math.PI / 180);
            GameParams.Sine[f] = (float) Math.sin(f * Math.PI / 180);
        }
        GameParams.isGameOver = false;

        GameParams.gameOverMask = new AnimationMask(R.drawable.game_over_animation, 10, 2, 13, 1, 4, 60, 6, 92, 152);
        GameParams.gameOverMask.isAlive = false;
        GameParams.breakStageMask = new AnimationMask(R.drawable.break_stage_animation, 10, 1, 10, 2, 2, 60, 6, 92, 152);
        GameParams.breakStageMask.isAlive = false;
        GameParams.loadingMask = new AnimationMask(R.drawable.waiting_popo, 10, 3, 25, 1, 2, 60, 6, 92, 152);
        GameParams.loadingMask.isAlive = true;
        super.Initialize();
    }

    @Override
    protected void LoadContent() {
        super.LoadContent();
        if (GameParams.music != null)
            GameParams.music.setMusicVolume(GameParams.musicVolumeRatio);
    }

    @Override
    protected void UnloadContent() {
        super.UnloadContent();
    }

    @Override
    protected void Update() {
//        DebugConfig.d("GameEntry Update()");
        if (GameParams.isGameOver
                && (mMainActivity.mRestartButton.getVisibility() == View.INVISIBLE)) {
            if (!isGameOverOrBreakStageMusicPlaying) {
                if (GameParams.music.player.isPlaying())
                    GameParams.music.Stop();
                GameParams.music = new Music(mMainActivity, R.raw.game_over, GameParams.musicVolumeRatio);
                GameParams.music.setLooping(true);
                GameParams.music.Play();
                isGameOverOrBreakStageMusicPlaying = true;
            }
            DebugConfig.d("Game Over!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        if (GameParams.breakStageMask.isAlive
                && mMainActivity.mToggleButton.getVisibility() == View.VISIBLE) {
            if (!isGameOverOrBreakStageMusicPlaying) {
                if (GameParams.music.player.isPlaying())
                    GameParams.music.Stop();
                GameParams.music = new Music(mMainActivity, R.raw.break_stage, GameParams.musicVolumeRatio);
                GameParams.music.setLooping(true);
                GameParams.music.Play();
                isGameOverOrBreakStageMusicPlaying = true;
            }
            Message m = new Message();
            m.what = Events.BREAK_STAGE;
            m.obj = View.INVISIBLE;
            MainActivity.mMsgHandler.sendMessage(m);
            DebugConfig.d("Stage Break!!!!!!!!!!!!!!!!!!!!!!!!");
        }

        if (GameStateClass.currentState != GameStateClass.oldState || isForceRestart) {
            switch (GameStateClass.currentState) {
                case None:
                    DebugConfig.d("None Stage");
                    isGameOverOrBreakStageMusicPlaying = false;
                    break;
                case Menu:
//                    mMenuDrawable = new Menu_drawable(this);
//                    Components.add(mMenuDrawable);

                    break;
                case Stage1:
                    DebugConfig.d("Start stage 1.");
                    if (mStage1 == null)
                        mStage1 = new Stage1(this);
                    Components.add(mStage1);

                    // TODO move this to somewhere before GameEntry Run()
                    if (GameParams.music == null) {
                        GameParams.music = new Music(mMainActivity, R.raw.stage1, GameParams.musicVolumeRatio);
                        GameParams.music.setLooping(true);
                        GameParams.music.Play();
                    }
                    break;

                case Stage2:
                    DebugConfig.d("Start stage 2.");
                    if (mStage2 == null)
                        mStage2 = new Stage2(this);
                    Components.add(mStage2);
                    if (GameParams.music == null) {
                        GameParams.music = new Music(mMainActivity, R.raw.stage2, GameParams.musicVolumeRatio);
                        GameParams.music.setLooping(true);
                        GameParams.music.Play();
                    }
                    break;

                case Stage3:
                    DebugConfig.d("Start stage 3.");
                    if (mStage3 == null)
                        mStage3 = new Stage3(this);
                    Components.add(mStage3);
                    if (GameParams.music == null) {
                        GameParams.music = new Music(mMainActivity, R.raw.stage3, GameParams.musicVolumeRatio);
                        GameParams.music.setLooping(true);
                        GameParams.music.Play();
                    }
                    break;

                case Stage4:
                    DebugConfig.d("Start stage 4.");
                    if (mStage4 == null)
                        mStage4 = new Stage4(this);
                    Components.add(mStage4);
                    if (GameParams.music == null) {
                        GameParams.music = new Music(mMainActivity, R.raw.stage4, GameParams.musicVolumeRatio);
                        GameParams.music.setLooping(true);
                        GameParams.music.Play();
                    }
                    break;

                case Stage5:
                    DebugConfig.d("Start stage 5.");
                    if (mStage5 == null)
                        mStage5 = new Stage5(this);
                    Components.add(mStage5);
                    if (GameParams.music == null) {
                        GameParams.music = new Music(mMainActivity, R.raw.stage5, GameParams.musicVolumeRatio);
                        GameParams.music.setLooping(true);
                        GameParams.music.Play();
                    }
                    break;
            }

            DebugConfig.d("oldState: " + GameStateClass.oldState + ", currentState: " + GameStateClass.currentState);
            GameStateClass.oldState = GameStateClass.currentState;
        }

        super.Update();
    }

    @Override
    protected void Draw() {
        canvas = MainActivity.mSurfaceView.getHolder().lockCanvas(null);
        if (canvas != null)
            canvas.drawColor(Color.BLACK);
        super.Draw();
    }
}
