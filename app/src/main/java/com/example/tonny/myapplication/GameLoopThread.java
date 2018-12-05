package com.example.tonny.myapplication;

import android.graphics.Canvas;
import android.view.View;

public class GameLoopThread extends Thread {
	static final double FPS = 0.4;
    private View view;
    private boolean running = false;
   
    public GameLoopThread(View view) {
          this.view = view;
    }


    public void setRunning(boolean run) {
          running = run;
    }

    @Override
    public void run() {
          long ticksPS = (int) (1000.0 / FPS);
          long startTime;
          long sleepTime;
          while (running) {
                 Canvas c = null;
                 startTime = System.currentTimeMillis();
                 try {
                        c = view.getHolder().lockCanvas();
                        synchronized (view.getHolder()) {
                               view.onDraw(c);
                        }
                 } finally {
                        if (c != null) {
                               view.getHolder().unlockCanvasAndPost(c);
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