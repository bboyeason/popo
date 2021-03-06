package lbs;

import android.os.Handler;
import android.os.Message;

import com.nosae.game.popo.GameParams;

import java.lang.ref.WeakReference;

public abstract class DrawableGameComponent {

    public static class MsgHandler extends Handler {
        private WeakReference<DrawableGameComponent> mDrawableGameComponent;

        public MsgHandler(DrawableGameComponent drawableGameComponent) {
            mDrawableGameComponent = new WeakReference<>(drawableGameComponent);
        }

        @Override
        public void handleMessage(Message msg) {
            DrawableGameComponent drawableGameComponent = mDrawableGameComponent.get();
            if (drawableGameComponent != null) {
                drawableGameComponent.handleMessage(msg);
            }
        }
    }

    public abstract void handleMessage(Message msg);

	public interface OnStageCompleteListener {
		void OnStageComplete(DrawableGameComponent stage);
	}

	public DrawableGameComponent() {

	}

	public void Dispose() {
		UnloadContent();
	}

	protected void Initialize() {
	}

	protected void LoadContent(){
	}

	protected void UnloadContent() {
		GameParams.isGameOver = false;
		GameParams.gameOverMask.isAlive = false;
	}

	protected void Update() {

	}

	protected void Draw() {

	}
}
