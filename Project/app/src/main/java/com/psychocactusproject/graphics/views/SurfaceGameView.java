package com.psychocactusproject.graphics.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.psychocactusproject.R;
import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.engine.GameEntity;
import com.psychocactusproject.graphics.manager.Sprite;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;

import static com.psychocactusproject.engine.GameEngine.BlackStripesTypes.FALSE;
import static com.psychocactusproject.engine.GameEngine.BlackStripesTypes.LEFT_RIGHT;
import static com.psychocactusproject.engine.GameEngine.BlackStripesTypes.TOP_BOTTOM;

public class SurfaceGameView extends SurfaceView implements SurfaceHolder.Callback, GameView {

    private List<GameEntity> gameEntities;
    private boolean ready;
    private Canvas frameCanvas;
    private Paint basicPaint;
    private Bitmap frameBitmap;
    private Matrix backgroundMatrix;
    private static Matrix basicMatrix;
    private static int deviceWidth;
    private static int deviceHeight;
    private static int adaptedWidth;
    private static int adaptedHeight;
    private static Sprite backgroundSprite;

    public SurfaceGameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getHolder().addCallback(this);
        this.frameCanvas = new Canvas();
        this.basicPaint = new Paint();
        this.backgroundMatrix = new Matrix();
        this.frameBitmap = Bitmap.createBitmap(
                GameEngine.RESOLUTION_X, GameEngine.RESOLUTION_Y,
                Bitmap.Config.ARGB_8888
        );
        this.frameCanvas.setBitmap(this.frameBitmap);
        basicMatrix = new Matrix();
        deviceWidth = this.getWidth();
        deviceHeight = this.getHeight();
    }

    public static void setAspectRatio(int deviceWidth, int deviceHeight) {
        GameEngine gameEngine = GameEngine.getInstance();
        adaptedWidth = gameEngine.getAdaptedWidth();
        adaptedHeight = gameEngine.getAdaptedHeight();
        backgroundSprite = new Sprite(gameEngine, R.drawable.background);
        if (gameEngine.hasBlackStripes() != FALSE) {
            int backgroundX = deviceWidth;
            int backgroundY = deviceHeight;
            if (gameEngine.hasBlackStripes() == TOP_BOTTOM) {
                basicMatrix.postTranslate(0, gameEngine.getAspectRatioMargin());
                backgroundX = (int) (((double) deviceHeight / adaptedHeight) * adaptedWidth);
                backgroundY = deviceHeight;
            } else if (gameEngine.hasBlackStripes() == LEFT_RIGHT) {
                basicMatrix.postTranslate(gameEngine.getAspectRatioMargin(), 0);
                backgroundX = deviceWidth;
                backgroundY = (int) (((double) deviceWidth / adaptedWidth) * adaptedHeight);
            }
            backgroundSprite.resizeBitmap(backgroundX, backgroundY);
        }
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
        backgroundSprite.draw(screen);
        this.frameCanvas.drawRGB(0, 0, 0);
        // this.frameDrawTest();
        synchronized (this.gameEntities) {
            int numEntities = this.gameEntities.size();
            for (int i = 0; i < numEntities; i++) {
                this.gameEntities.get(i).draw(this.frameCanvas);
            }
        }
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(this.frameBitmap,
                adaptedWidth, adaptedHeight, false);
        screen.drawBitmap(scaledBitmap, this.basicMatrix, this.basicPaint);
        getHolder().unlockCanvasAndPost(screen);
    }

    // Método auxiliar utilizado para comprobar que las proporciones son iguales en todos los dispositivos
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
