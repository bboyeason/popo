package com.nosae.game.sence;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

import com.nosae.game.bobo.GameEntry;
import com.nosae.game.bobo.GameParams;
import com.nosae.game.bobo.MainActivity;
import com.nosae.game.bobo.R;
import com.nosae.game.bobo.Text;

import lbs.DrawableGameComponent;
import com.nosae.game.objects.GameObj;
import com.nosae.game.role.Bobo;
import com.nosae.game.settings.DebugConfig;

/**
 * Created by eason on 2015/10/25.
 */
public class Stage2 extends DrawableGameComponent {
    public GameEntry mGameEntry;
    private Canvas mSubCanvas;

    private GameObj mBackground;
    private Bitmap mBackGroundImage;

    private GameObj mSenceTitle;
    private Bitmap mSenceTitleImage;

    private Bobo mBoboObj;
    private Bitmap mBoboImage;
    private Bitmap mRule2Image;

    private GameObj mStaff;
    private Bitmap mStaffImage;


    private Text mFpsText;

    int f, j;
    public Stage2(GameEntry gameEntry) {
        this.mGameEntry = gameEntry;
    }

    @Override
    public void Dispose() {
        super.Dispose();
    }

    @Override
    protected void Initialize() {
        super.Initialize();
        DebugConfig.d("Stage2 Initialize()");
        if (DebugConfig.isFpsDebugOn) {
            mFpsText = new Text(GameParams.halfWidth - 50, 20, 12, "FPS", Color.BLUE);
        }
    }

    @Override
    protected void LoadContent() {
        super.LoadContent();
        DebugConfig.d("Stage2 LoadContent()");
        if (mBackground == null) {
            // Load background image
            mBackGroundImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res,
                    R.drawable.b_backimage);
            mBackground = new GameObj(0, 0, GameParams.scaleWidth / mBackGroundImage.getWidth(), GameParams.scaleHeight / mBackGroundImage.getHeight(), 0, 0, 0, 0, 0, 0, 0);
            mBackground.isAlive = true;
        }

        if (mSenceTitle == null) {
            mSenceTitleImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res, R.drawable.b_stage2title);
            mSenceTitle = new GameObj(10, 10, mSenceTitleImage.getWidth(), mSenceTitleImage.getHeight(), 0, 0, mSenceTitleImage.getWidth(), mSenceTitleImage.getHeight(), 0, 0, 0);
        }

        if (mBoboObj == null) {
            mBoboImage = (Bitmap) BitmapFactory.decodeResource(GameParams.res, R.drawable.b_role1);
            mRule2Image = (Bitmap) BitmapFactory.decodeResource(GameParams.res, R.drawable.b_role2);
            mBoboObj = new Bobo(mBoboImage, GameParams.halfWidth - mBoboImage.getWidth() / 2, GameParams.scaleHeight - mBoboImage.getHeight(), mBoboImage.getWidth(), mBoboImage.getHeight(), 0, 0, mBoboImage.getWidth(), mBoboImage.getHeight(), 0, 0, 0);
            mBoboObj.role2 = mBoboObj.new Role2(mRule2Image, mBoboObj.getX() - mRule2Image.getWidth(), GameParams.scaleHeight - mRule2Image.getHeight(), mRule2Image.getWidth(), mRule2Image.getHeight(), 0, 0, mRule2Image.getWidth(), mRule2Image.getHeight(), 0, 0, 0);
        }
    }

    @Override
    protected void UnloadContent() {
        super.UnloadContent();
    }

    @Override
    protected void Update() {
        super.Update();
        if (DebugConfig.isFpsDebugOn) {
            mFpsText.message = "actual FPS: " + (int) mGameEntry.actualFPS + " FPS (" + (int) mGameEntry.fps
                    + ") " + (int) mGameEntry.totalFrames;
        }

        if (mBoboObj != null){
            /* FIXME: Role animation */
        }
    }

    @Override
    protected void Draw() {
        super.Draw();
        mSubCanvas = mGameEntry.canvas;

        mSubCanvas = mGameEntry.canvas;
        // Draw background image
        if (mBackground.isAlive)
        {
            for (f = 0; f <= mBackground.destWidth; f++)
            {
                for (j = -1; j <= mBackground.destHeight; j++)
                {
                    mSubCanvas.drawBitmap(mBackGroundImage,
                            f * mBackGroundImage.getWidth() + mBackground.destRect.left,
                            j * mBackGroundImage.getHeight() + mBackground.destRect.top,
                            null);
                }
            }
        }

        if (DebugConfig.isFpsDebugOn) {
            mSubCanvas.drawText(mFpsText.message, mFpsText.x, mFpsText.y, mFpsText.paint);
        }

        if (mSenceTitle != null) {
            mSubCanvas.drawBitmap(mSenceTitleImage, mSenceTitle.srcRect, mSenceTitle.destRect, mSenceTitle.paint);
        }

        if (mBoboObj != null) {
            mBoboObj.draw(mSubCanvas);
        }
    }
}
