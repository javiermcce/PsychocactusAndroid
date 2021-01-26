package com.psychocactusproject.graphics.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.engine.GameEntity;

import java.util.List;

public class SurfaceGameView extends SurfaceView implements SurfaceHolder.Callback, GameView {

    private List<GameEntity> gameEntities;
    private boolean ready;
    private Canvas frameCanvas;
    private Matrix basicMatrix;
    private Paint basicPaint;
    private Bitmap frameBitmap;

    public SurfaceGameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getHolder().addCallback(this);
        this.frameCanvas = new Canvas();
        this.basicMatrix = new Matrix();
        this.basicPaint = new Paint();
        this.frameBitmap = Bitmap.createBitmap(
                GameEngine.RESOLUTION_X, GameEngine.RESOLUTION_Y,
                Bitmap.Config.ARGB_8888
        );
        this.frameCanvas.setBitmap(this.frameBitmap);
    }

    public SurfaceGameView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.ready = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.ready = false;
    }

    @Override
    public void setGameEntities(List<GameEntity> gameEntities) {
        this.gameEntities = gameEntities;
    }

    @Override
    public void draw() {
        if (!this.ready) {
            return;
        }
        Canvas screen = getHolder().lockCanvas();
        if (screen == null) {
            return;
        }
        screen.drawRGB(100, 100, 100);
        this.frameCanvas.drawRGB(0, 0, 0);
        synchronized (this.gameEntities) {
            int numEntities = this.gameEntities.size();
            for (int i = 0; i < numEntities; i++) {
                this.gameEntities.get(i).draw(this.frameCanvas);
            }
        }
        this.frameDrawTest();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(frameBitmap, this.getWidth(), this.getHeight(), false);
        screen.drawBitmap(scaledBitmap, this.basicMatrix, this.basicPaint);
        getHolder().unlockCanvasAndPost(screen);
    }

    private void frameDrawTest() {
        this.basicPaint.setColor(Color.GREEN);
        frameCanvas.drawRect(0, 0, GameEngine.RESOLUTION_X, GameEngine.RESOLUTION_Y, this.basicPaint);
        this.basicPaint.setColor(Color.WHITE);
        frameCanvas.drawRect(0, 0, 800, 800, this.basicPaint);
        this.basicPaint.setColor(Color.CYAN);
        frameCanvas.drawRect(0, 0, 700, 700, this.basicPaint);
        this.basicPaint.setColor(Color.MAGENTA);
        frameCanvas.drawRect(0, 0, 600, 600, this.basicPaint);
        this.basicPaint.setColor(Color.GRAY);
        frameCanvas.drawRect(0, 0, 500, 500, this.basicPaint);
        this.basicPaint.setColor(Color.GREEN);
        frameCanvas.drawRect(0, 0, 400, 400, this.basicPaint);
        this.basicPaint.setColor(Color.BLUE);
        frameCanvas.drawRect(0, 0, 300, 300, this.basicPaint);
        this.basicPaint.setColor(Color.YELLOW);
        frameCanvas.drawRect(0, 0, 200, 200, this.basicPaint);
        this.basicPaint.setColor(Color.BLACK);
        frameCanvas.drawRect(0, 0, 100, 100, this.basicPaint);
    }
}
