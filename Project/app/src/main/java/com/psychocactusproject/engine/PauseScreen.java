package com.psychocactusproject.engine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.psychocactusproject.graphics.controllers.ClickableDirectSprite;
import com.psychocactusproject.graphics.controllers.CustomClickableEntity;
import com.psychocactusproject.graphics.interfaces.Drawable;
import com.psychocactusproject.graphics.manager.ResourceLoader;
import com.psychocactusproject.input.TouchInputController;
import com.psychocactusproject.graphics.manager.MenuBitmapFlyweight;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class PauseScreen {

    private final MenuBitmapFlyweight.PauseMenuFlyweight pieces;
    private int randomMusicIndex;

    public enum PAUSE_LAYERS { FIRST, SECOND }
    private PAUSE_LAYERS activeLayer;
    private Drawable initialDrawable;
    private TouchInputController.Touchable initialTouchable;
    private List<ClickableDirectSprite> pauseEntities;
    private final HashMap<PAUSE_LAYERS, List<CustomClickableEntity>> optionsByLayer;
    private final Matrix pauseMatrix;
    private final Matrix backgroundPauseMatrix;
    private final Paint pausePaint;
    private final Paint backgroundPausePaint;
    private Bitmap lastFrameBitmap;
    private Bitmap pauseBaseFrame;
    private Canvas pauseFrameCanvas;

    public PauseScreen() {
        this.pieces = MenuBitmapFlyweight.getPauseMenuInstance();
        this.pauseMatrix = new Matrix();
        this.backgroundPauseMatrix = new Matrix();
        this.pausePaint = new Paint();
        this.pausePaint.setColor(Color.WHITE);
        this.pausePaint.setTextSize(128);
        this.pausePaint.setTypeface(ResourceLoader.getTypeface());
        this.backgroundPausePaint = new Paint();
        this.backgroundPausePaint.setColor(Color.argb(100, 20, 20, 50));
        this.optionsByLayer = new HashMap<>();
        this.createOptions();
    }

    public List<CustomClickableEntity> getOptionsByLayer(PAUSE_LAYERS layer) {
        return this.optionsByLayer.get(layer);
    }

    public CustomClickableEntity[] getOptions() {
        return this.optionsByLayer.get(this.activeLayer).toArray(new CustomClickableEntity[0]);
    }

    public List<ClickableDirectSprite> getPauseEntities() {
        return this.pauseEntities;
    }

    public void setPauseEntities(List<ClickableDirectSprite> pauseEntities) {
        this.pauseEntities = pauseEntities;
    }

    public void openPauseScreen() {
        this.activeLayer = PAUSE_LAYERS.FIRST;
    }

    public Drawable definedPauseDrawable() {
        return (canvas) -> {
            // En el primer ciclo de dibujado de pausa...
            if (this.lastFrameBitmap == null) {
                // Se construye una copia de de la imagen de fondo
                Canvas copyCanvas = new Canvas();
                this.lastFrameBitmap = Bitmap.createBitmap(
                        GameEngine.RESOLUTION_X, GameEngine.RESOLUTION_Y,
                        Bitmap.Config.ARGB_8888
                );
                copyCanvas.setBitmap(this.lastFrameBitmap);
                GameEngine.getInstance().getSurfaceGameView().definedGameDrawable(true).draw(copyCanvas);
                // Se construye el marco de pausa
                this.pauseFrameCanvas = new Canvas();
                this.pauseBaseFrame = Bitmap.createBitmap(
                        GameEngine.RESOLUTION_X, GameEngine.RESOLUTION_Y, Bitmap.Config.ARGB_8888);
                this.pauseFrameCanvas.setBitmap(this.pauseBaseFrame);
                this.pauseMatrix.reset();
                this.pauseMatrix.postTranslate(640, 0);
                Bitmap nextPiece;
                // Barra vertical
                nextPiece = this.pieces.getVerticalBarPiece();
                this.pauseFrameCanvas.drawBitmap(nextPiece, this.pauseMatrix, this.pausePaint);
                this.pauseMatrix.postTranslate(0, nextPiece.getHeight());
                // Barra horizontal
                this.pauseMatrix.reset();
                this.pauseMatrix.postTranslate(0, 345);
                nextPiece = this.pieces.getHorizontalBarPiece();
                this.pauseFrameCanvas.drawBitmap(nextPiece, this.pauseMatrix, this.pausePaint);
                this.pauseMatrix.postTranslate(nextPiece.getWidth(), 0);
                this.pauseMatrix.reset();
                this.randomMusicIndex = (int) (Math.random() * 5);
            }
            // Dibuja la copia de la imagen de fondo
            canvas.drawBitmap(this.lastFrameBitmap, this.pauseMatrix, this.pausePaint);
            // Aplica un fondo oscurecido
            canvas.drawRect(
                    new Rect(0, 0, GameEngine.RESOLUTION_X, GameEngine.RESOLUTION_Y),
                    this.backgroundPausePaint);
            // Imprime el texto de pausa
            canvas.drawText("PAUSE", 120, 210, this.pausePaint);
            // Imprime el marco del menú de pausa
            canvas.drawBitmap(this.pauseBaseFrame, this.pauseMatrix, this.pausePaint);
            canvas.drawCircle(
                    GameEngine.RESOLUTION_X / 4f * 3, GameEngine.RESOLUTION_Y / 4f,
                    100, this.pausePaint);
            this.pauseMatrix.reset();
            this.pauseMatrix.postTranslate(850, 65);
            canvas.drawBitmap(
                    MenuBitmapFlyweight.getPauseMenuInstance().getRandomFace(randomMusicIndex),
                    this.pauseMatrix, this.pausePaint);
            this.pauseMatrix.reset();

        };
    }

    private void createOptions() {
        for (PAUSE_LAYERS key : PAUSE_LAYERS.values()) {
            this.optionsByLayer.put(key, new LinkedList<>());
        }
    }

    public void clearLastGameFrame() {
        this.lastFrameBitmap = null;
    }
}
