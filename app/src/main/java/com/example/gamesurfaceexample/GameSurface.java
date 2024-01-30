package com.example.gamesurfaceexample;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {
    Resources res;
    Bitmap image;
    //координаты иконки
    float curX, curY, touchX, touchY;
    Paint paint;
    float dx, dy;
    float step;
    SurfaceThread surfaceThread;
    SurfaceHolder holder;
    float wImage, hImage;
    public GameSurface(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        res = getResources();
        image = BitmapFactory.decodeResource(res, R.drawable.cat_head);
        //начальные координаты иконки
        curX = curY = 100;
        paint = new Paint();
        step = 100;
        wImage = image.getWidth();
        hImage = image.getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(image, curX, curY, paint);
        curX += dx;
        curY += dy;
        checkPosition();
    }

    public void checkPosition(){
        //проверка на остановку или отскок
        if(Math.abs(touchX - curX) < 10 || Math.abs(touchY - curY) < 10){
            dx = 0;
            dy = 0;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            touchX = event.getX();
            touchY = event.getY();
            calcCoord();
        }
        return true;
    }
    //пересчет смещения картинки
    void calcCoord(){
        float x, y;
        float speed = 20;
        x = touchX - curX; //расстояние по горизонтали
        y = touchY - curY; //расстояние по вертикали
        step = (float) Math.sqrt(x * x + y * y);
        dx = x / step * speed;
        dy = y / step * speed;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        surfaceThread = new SurfaceThread(surfaceHolder, this);
        surfaceThread.isRun(true);
        surfaceThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
            boolean retry = true;
            surfaceThread.isRun(false);
            while (retry){
                try {
                    surfaceThread.join();
                    retry = true;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
    }
}
