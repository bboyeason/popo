package com.nosae.game.bobo;

/**
 * Created by eason on 2015/10/19.
 */
import lbs.*;
import com.nosae.game.settings.DebugConfig;

public class GameStateClass {
    // �C�����A
    public enum GameState
    {
        None,
        ready,
        Menu,
        Stage1,
        Stage2,
        Stage3,
        Stage4,
        Stage5,
    }

    public static GameState currentState;
    public static GameState oldState;

    public static void changeState(GameState newState,DrawableGameComponent nowGameClass,Game game)
    {
        if (nowGameClass != null) {
            game.Components.remove(nowGameClass);

            nowGameClass.Dispose();
        }
        currentState = newState;
        DebugConfig.d("GameStateClass => current state: " + currentState + " old state: " + oldState);
    }
}
