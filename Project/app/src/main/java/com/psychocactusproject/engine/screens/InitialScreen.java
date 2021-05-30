package com.psychocactusproject.engine.screens;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.psychocactusproject.R;
import com.psychocactusproject.engine.manager.GameEngine;
import com.psychocactusproject.engine.manager.GameEngine.SCENES;
import com.psychocactusproject.engine.util.Hitbox;
import com.psychocactusproject.engine.util.Point;
import com.psychocactusproject.graphics.controllers.ClickableDirectSprite;
import com.psychocactusproject.graphics.interfaces.Drawable;
import com.psychocactusproject.graphics.manager.ResourceLoader;
import com.psychocactusproject.input.Slidable;
import com.psychocactusproject.input.Touchable;
import com.psychocactusproject.interaction.scripts.Clickable;

import java.util.HashMap;
import java.util.List;

public class InitialScreen implements Scene, Clickable {


    // Capas del menú de inicio
    public static final int MAIN_LAYER = 0;
    public static final int OPTIONS_LAYER = 1;
    private final HashMap<Integer, List<Hitbox>> hitboxesByLayer;
    private final Matrix initialMatrix;
    private final Paint initialPaint;
    private Bitmap lastFrameBitmap;
    private int activeLayer;

    public InitialScreen() {
        this.initialMatrix = new Matrix();
        this.initialPaint = new Paint();
        this.hitboxesByLayer = new HashMap<>();
    }

    public void setInitialEntities(List<ClickableDirectSprite> initialEntities) {
        // this.initialEntities = initialEntities;
    }

    public Drawable definedDrawable() {
        return (canvas) -> {
            // En el primer ciclo de dibujado de pausa...
            if (this.lastFrameBitmap == null) {
                this.buildBackgroundBitmap();
            }
            canvas.drawBitmap(this.lastFrameBitmap, this.initialMatrix, this.initialPaint);
            // Dibuja el inicio según la capa aplicada
            switch (activeLayer) {
                case MAIN_LAYER:
                    //this.drawMainInitialScreen(canvas);
                    break;
                case OPTIONS_LAYER:
                    //this.drawOptionsInitialScreen(canvas);
                    break;
                default:
                    break;
            }
            if (GameEngine.DEBUGGING) {
                Hitbox.drawHitboxes(this.getHitboxes(), canvas);
            }
        };
    }

    private void buildBackgroundBitmap() {
        // Se dibuja el fondo y el título
        Canvas initialFrameCanvas = new Canvas();
        this.lastFrameBitmap = Bitmap.createBitmap(
                GameEngine.RESOLUTION_X, GameEngine.RESOLUTION_Y, Bitmap.Config.ARGB_8888);
        initialFrameCanvas.setBitmap(this.lastFrameBitmap);
        this.initialMatrix.reset();
        Bitmap stageBackground = ResourceLoader.loadBitmap(R.drawable.background_stage_amplified);
        initialFrameCanvas.drawBitmap(stageBackground, this.initialMatrix, this.initialPaint);
        Bitmap startTitle = ResourceLoader.loadBitmap(R.drawable.start_title);
        this.initialMatrix.postTranslate(500, 25);
        initialFrameCanvas.drawBitmap(startTitle, this.initialMatrix, this.initialPaint);
        this.initialMatrix.reset();
    }

    @Override
    public Touchable definedTouchable() {
        return (point) -> {
            GameEngine.getInstance().resumeGame();
        };

    }

    @Override
    public void onSceneChange(SCENES oldScene) {

    }

    @Override
    public SCENES getSceneId() {
        return GameEngine.SCENES.INITIAL_SCREEN;
    }

    @Override
    public List<Slidable> getSlidables() {
        return null;
    }

    @Override
    public void clearScreen() {

    }

    @Override
    public void executeClick(int index) {

    }

    @Override
    public Hitbox[] getHitboxes() {
        return this.hitboxesByLayer.get(activeLayer).toArray(new Hitbox[0]);
    }

    @Override
    public boolean isAvailable(int index) {
        return true;
    }

    @Override
    public void enableClickable(int index) {
        throw new IllegalStateException("Se ha intentado activar una opción de la pantalla de " +
                "inicio, pero no está permitido activar ni desactivar las opciones");
    }

    @Override
    public void disableClickable(int index) {
        throw new IllegalStateException("Se ha intentado desactivar una opción de la pantalla de " +
                "inicio, pero no está permitido activar ni desactivar las opciones");
    }

    @Override
    public int getPositionX() {
        return 0;
    }

    @Override
    public int getPositionY() {
        return 0;
    }

    @Override
    public Point getPosition() {
        return new Point(0, 0);
    }

    @Override
    public int getSpriteWidth() {
        return GameEngine.RESOLUTION_X;
    }

    @Override
    public int getSpriteHeight() {
        return GameEngine.RESOLUTION_Y;
    }
}
