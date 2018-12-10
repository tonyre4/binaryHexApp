package com.example.tonny.myapplication;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MySurfaceThread2 extends Thread {

	double FPS = 30;

	private SurfaceHolder myThreadSurfaceHolder;
	private binToHexAnimflo myThreadSurfaceView;
	private boolean myThreadRun = false;

	public MySurfaceThread2(SurfaceHolder surfaceHolder,
                            binToHexAnimflo surfaceView) {
		myThreadSurfaceHolder = surfaceHolder;
		myThreadSurfaceView = surfaceView;
	}

	public void setFPS(double F){
		FPS = F;
	}

	public void setRunning(boolean b) {
		myThreadRun = b;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// super.run();

		long ticksPS = (int) (1000.0/FPS);
		long startTime;
		long sleepTime;

		while (myThreadRun) {
			Canvas c = null;
			try {
				startTime = System.currentTimeMillis();
				c = myThreadSurfaceHolder.lockCanvas(null);
				synchronized (myThreadSurfaceHolder) {
					myThreadSurfaceView.draw(c);
				}
			} finally {
				// do this in a finally so that if an exception is thrown
				// during the above, we don't leave the Surface in an
				// inconsistent state
				if (c != null) {
					myThreadSurfaceHolder.unlockCanvasAndPost(c);
				}
			}
			sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
			try {
				if (sleepTime > 0)
					sleep(sleepTime);
				else
					sleep(10);
			} catch (Exception e) {}
		}

	}
}