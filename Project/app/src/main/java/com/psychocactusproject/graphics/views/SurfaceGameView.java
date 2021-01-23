package com.psychocactusproject.graphics.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.psychocactusproject.engine.GameEntity;

import java.util.List;

public class SurfaceGameView extends SurfaceView implements SurfaceHolder.Callback, GameView {

    private List<GameEntity> gameEntities;
    private boolean ready;

    public SurfaceGameView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public SurfaceGameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getHolder().addCallback(this);
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
        Canvas canvas = getHolder().lockCanvas();
        if (canvas == null) {
            return;
        }
        canvas.drawRGB(0, 0, 0);
        synchronized (this.gameEntities) {
            int numEntities = this.gameEntities.size();
            for (int i = 0; i < numEntities; i++) {
                this.gameEntities.get(i).draw(canvas);
            }
        }
        getHolder().unlockCanvasAndPost(canvas);
    }
}
