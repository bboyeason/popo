package com.nosae.game.sence;

import com.nosae.game.bobo.GameEntry;
import com.nosae.game.settings.DebugConfig;

import lbs.DrawableGameComponent;
import lbs.GameComponentCollection;

/**
 * Created by eason on 2015/11/9.
 */
public class Stage3 extends DrawableGameComponent {
    private final GameEntry mGameEntry;

    public Stage3(GameEntry gameEntry) {
        DebugConfig.d("Stage3 Constructor");
        this.mGameEntry = gameEntry;
    }


}
