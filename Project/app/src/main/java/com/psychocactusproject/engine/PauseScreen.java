package com.psychocactusproject.engine;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.psychocactusproject.graphics.controllers.ClickableDirectSprite;
import com.psychocactusproject.graphics.controllers.Drawable;
import com.psychocactusproject.input.TouchInputController;

import java.util.List;

public class PauseScreen {

    private Drawable initialDrawable;
    private TouchInputController.Touchable initialTouchable;

    private List<ClickableDirectSprite> pauseEntities;
    private Bitmap lastFrameBitmap;
    private Matrix pauseMatrix;
    private Paint pausePaint;

    public PauseScreen() {
        this.pauseMatrix = new Matrix();
        this.pausePaint = new Paint();
    }

    public void setPauseEntities(List<ClickableDirectSprite> pauseEntities) {
        this.pauseEntities = pauseEntities;
    }

    public Drawable definedPauseDrawable() {
        return (canvas) -> {
            //synchronized (GameEntity.entitiesLock) {
                if (this.lastFrameBitmap == null) {
                    Bitmap lastGameFrame = GameEngine.getInstance().getSurfaceGameView().getFrameBitmap();
                    this.lastFrameBitmap = lastGameFrame.copy(
                            lastGameFrame.getConfig(), lastGameFrame.isMutable());
                }
                canvas.drawBitmap(this.lastFrameBitmap, this.pauseMatrix, this.pausePaint);

            //}
        };
    }
}
