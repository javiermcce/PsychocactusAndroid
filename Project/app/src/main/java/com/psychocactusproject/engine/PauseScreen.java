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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class PauseScreen {

    public enum PAUSE_LAYERS { FIRST, SECOND }
    private PAUSE_LAYERS activeLayer;
    private Drawable initialDrawable;
    private TouchInputController.Touchable initialTouchable;

    private List<ClickableDirectSprite> pauseEntities;
    private final HashMap<PAUSE_LAYERS, List<CustomClickableEntity>> optionsByLayer;
    private final Matrix pauseMatrix;
    private final Paint pausePaint;
    private final Paint backgroundPausePaint;
    private Bitmap lastFrameBitmap;

    public PauseScreen() {
        this.pauseMatrix = new Matrix();
        this.pausePaint = new Paint();
        this.pausePaint.setTextSize(128);
        // Esto da error. Además de solucionarlo, debería seguir la guía que he construido
        // en gimp para hacer la pantalla de pausa
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
            // En el primer ciclo de dibujado de pausa, se construye una copia de de la imagen de fondo
            if (this.lastFrameBitmap == null) {
                Canvas copyCanvas = new Canvas();
                this.lastFrameBitmap = Bitmap.createBitmap(
                        GameEngine.RESOLUTION_X, GameEngine.RESOLUTION_Y,
                        Bitmap.Config.ARGB_8888
                );
                copyCanvas.setBitmap(this.lastFrameBitmap);
                GameEngine.getInstance().getSurfaceGameView().definedGameDrawable(true).draw(copyCanvas);
            }
            // Dibuja la copia de la imagen de fondo
            canvas.drawBitmap(this.lastFrameBitmap, this.pauseMatrix, this.pausePaint);
            // Aplica un fondo oscurecido
            canvas.drawRect(
                    new Rect(0, 0, GameEngine.RESOLUTION_X, GameEngine.RESOLUTION_Y),
                    this.backgroundPausePaint);
            // Imprime el texto de pausa
            canvas.drawText("PAUSE", 110, 120, this.pausePaint);

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
