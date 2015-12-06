package com.nosae.game.scene;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

import com.nosae.game.bobo.GameEntry;

import lbs.DrawableGameComponent;
import com.nosae.game.objects.GameObj;

import com.nosae.game.bobo.GameParams;
import com.nosae.game.bobo.R;
import com.nosae.game.bobo.Text;

/**
 * Created by eason on 2015/10/27.
 */
public class Menu_drawable extends DrawableGameComponent{
    public GameEntry mGameEntry;
    private Canvas subCanvas;

    private int frameTime;

    private Bitmap menuBitmap;
    private GameObj menuObj;

    // "Touch"
    private Text menuText;
    private Text menuShadow;

    private Text menuTitleText;
    private Text menuTitleShadow;

    public Menu_drawable(GameEntry mGameEntry) {
        this.mGameEntry = mGameEntry;
    }

    @Override
    public void Dispose() {
        super.Dispose();
    }

    @Override
    protected void Initialize() {
        menuTitleText = new Text(65, 50, 36, "BoBo", Color.YELLOW);

        menuTitleShadow = new Text(menuTitleText.x + 10, menuTitleText.y - 5, menuTitleText.size, menuTitleText.message, Color.BLACK);

        menuText = new Text(50, GameParams.scaleHeight - 70, 12, "Touch screen to start", Color.YELLOW);
        menuText.delayFrame = 20;

        menuShadow = new Text(menuText.x + 10, menuText.y - 5, menuText.size, menuText.message, Color.BLACK);

        menuBitmap = BitmapFactory.decodeResource(GameParams.res, R.drawable.menu_background);

        menuObj = new GameObj(0, 0, GameParams.scaleWidth, GameParams.scaleHeight, 0, 0, menuBitmap.getWidth(), menuBitmap.getHeight(), 0, Color.WHITE, 0);
        menuObj.paint.setAlpha(170);
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
        frameTime = (int)mGameEntry.totalFrames;

        if (frameTime - menuText.startFrame > menuText.delayFrame)
        {
            menuText.startFrame = frameTime;

            menuText.isVisible = !menuText.isVisible;
        }
        super.Update();
    }

    @Override
    protected void Draw() {
        subCanvas = mGameEntry.canvas;

        subCanvas.drawBitmap(menuBitmap, menuObj.srcRect, menuObj.destRect, menuObj.paint);

        subCanvas.drawText(menuTitleShadow.message, menuTitleShadow.x, menuTitleShadow.y, menuTitleShadow.paint);

        subCanvas.drawText(menuTitleText.message, menuTitleText.x, menuTitleText.y, menuTitleText.paint);

        if (menuText.isVisible)
        {
            subCanvas.drawText(menuShadow.message, menuShadow.x, menuShadow.y, menuShadow.paint);

            subCanvas.drawText(menuText.message, menuText.x, menuText.y, menuText.paint);
        }
        super.Draw();
    }
}
