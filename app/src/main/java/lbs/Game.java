package lbs;

import android.graphics.Canvas;

import com.nosae.game.popo.MainActivity;

import com.nosae.game.settings.DebugConfig;

public class Game {
	
	private boolean isRun;
	public GameComponentCollection Components = new GameComponentCollection();
	public Canvas canvas = null;
	public static long totalFrames;
	public int fps;
	public int actualFPS;
	public static int setFPS = 30;
	private int fpsInterval = 1000 / setFPS;
	
	private Thread gameLoop;
	private int f;
	
	public void Run()
	{
		if (!isRun)
		{
			Initialize();
			LoadContent();
			
			isRun = true;
		}
		
		gameLoop = new Thread(new GameLoop());
		gameLoop.setDaemon(true);
		gameLoop.start();
		DebugConfig.d("gameLoop.start()");
	}
	
	public void Exit()
	{
		if (gameLoop != null)
		{
			gameLoop.interrupt();
		}
	}
	
	class GameLoop implements Runnable
	{
		
		public void run() {
			long startTime;
			int delayTime;
			int sleepTime;
			
			while(!Thread.interrupted())
			{			
				startTime = System.currentTimeMillis(); 
				
				Update();
					
				Draw();
				
				delayTime = (int)(System.currentTimeMillis() - startTime);
				
				if (delayTime == 0) delayTime = 1;
				fps = 1000 / delayTime;
//				DebugConfig.d("fps: " + fps);
				sleepTime = fpsInterval - delayTime;
				if (sleepTime > 0) {
					actualFPS = 1000 / fpsInterval;
//					DebugConfig.d("actualFPS:" + actualFPS);
					try {
						Thread.sleep(sleepTime);
//                        DebugConfig.d("sleepTime: " + sleepTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
						Thread.currentThread().interrupt();
					}
				}else
				{
					actualFPS = 1000 / delayTime;
				}
			}
		}
	}
	
	protected void Initialize() {
		
	}
	
	protected void LoadContent(){
		
	}
	
	protected void UnloadContent() {
		Exit();
	}
	
	protected void Update() {
		for (f = Components.size() -1 ; f >= 0; f--)
			Components.get(f).Update();
	}
	
	protected void Draw() {
		for (f = Components.size() -1 ; f >= 0; f--)
			Components.get(f).Draw();
		
		MainActivity.mSurfaceView.getHolder().unlockCanvasAndPost(canvas);
//		GV.surface.mHolder.unlockCanvasAndPost(canvas);
		
		totalFrames++;
	}	
}
