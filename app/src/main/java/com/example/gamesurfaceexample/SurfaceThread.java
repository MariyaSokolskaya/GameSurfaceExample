package com.example.gamesurfaceexample;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class SurfaceThread extends Thread{
    boolean isRunning = false;
    SurfaceHolder holder;
    GameSurface gameSurface;

    public SurfaceThread(SurfaceHolder holder, GameSurface gameSurface){
        this.holder = holder;
        this.gameSurface = gameSurface;
    }

    public void isRun(boolean isRunning){
        this.isRunning = isRunning;
    }

    @Override
    public void run() {
        super.run();
        long currentTime, nextTime;
        currentTime = System.currentTimeMillis();
        while (isRunning){
            nextTime = System.currentTimeMillis();
            //новый кадр раз в 30 мс
            if(nextTime - currentTime > 30){
                Canvas canvas = null;
                canvas = holder.lockCanvas();
                synchronized (holder){
                    gameSurface.draw(canvas);
                }
                holder.unlockCanvasAndPost(canvas);
                currentTime = nextTime;
            }

        }

    }
}
