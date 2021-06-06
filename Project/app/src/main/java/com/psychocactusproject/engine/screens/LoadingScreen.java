package com.psychocactusproject.engine.screens;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.psychocactusproject.engine.manager.GameEngine;
import com.psychocactusproject.engine.manager.GameEngine.SCENES;
import com.psychocactusproject.engine.util.GameClock;
import com.psychocactusproject.graphics.interfaces.Drawable;
import com.psychocactusproject.graphics.manager.MenuBitmapFlyweight;
import com.psychocactusproject.graphics.manager.ResourceLoader;
import com.psychocactusproject.input.Slidable;
import com.psychocactusproject.input.Touchable;

import java.util.List;

import com.psychocactusproject.graphics.manager.MenuBitmapFlyweight.LoadingScreenFlyweight;

public class LoadingScreen implements Scene {

    public static SCENES nextScene;
    private GameClock textClock;
    private GameClock faceRotateClock;
    private int selectedRandomFace;
    private Bitmap loadingFace;
    private LoadingScreenFlyweight loadingFlyweight;
    private Matrix textMatrix;
    private Matrix rotatingFaceMatrix;
    private Paint loadingPaint;
    private boolean loadingConstructed;
    private Bitmap blackBackground;
    private Matrix basicMatrix;

    public LoadingScreen() {
        this.loadingFlyweight = MenuBitmapFlyweight.getLoadingScreenFlyweight();
        this.textMatrix = new Matrix();
        this.rotatingFaceMatrix = new Matrix();
        // esto es necesario porque al principio, cuando aún no ha rotado, puede
        // aparecer en el lugar incorrecto
        this.rotatingFaceMatrix.postTranslate(
                GameEngine.RESOLUTION_X - 160, GameEngine.RESOLUTION_Y - 150);
        this.basicMatrix = new Matrix();
        this.loadingPaint = new Paint();
        this.loadingPaint.setTypeface(ResourceLoader.getTypeface());
        this.loadingPaint.setTextSize(64);
        this.loadingPaint.setColor(Color.WHITE);
        this.selectedRandomFace = -1;
        this.loadingConstructed = false;
        GameClock.scheduleTask(2500, () -> {
            // Faltaría usar nextScene para dirigir la actividad
            // GameEngine.getInstance().openMainMenu();
        });
    }

    @Override
    public Drawable definedDrawable() {
        return (canvas) -> {
            // Al iniciar por primera vez, construye la imagen de carga
            if (!this.loadingConstructed) {
                // Se dibuja un fondo negro
                Canvas loadingFrameCanvas = new Canvas();
                this.blackBackground = Bitmap.createBitmap(
                        GameEngine.RESOLUTION_X, GameEngine.RESOLUTION_Y, Bitmap.Config.ARGB_8888);
                loadingFrameCanvas.setBitmap(this.blackBackground);
                loadingFrameCanvas.drawRGB(0,0,0);
                // Se inicializan los recursos de la nueva pantalla de carga
                this.textClock = new GameClock(4, 1);
                this.faceRotateClock = new GameClock(8, 2);
                this.selectedRandomFace = (int) (Math.random() * 5);
                this.loadingFace = this.loadingFlyweight.getRandomFace(this.selectedRandomFace);
                this.loadingConstructed = true;
            }
            // Una vez por ciclo de reloj, rota la imagen de la esquina
            if (this.faceRotateClock.isFirstFractionCall()) {
                // Reinicia la matrix
                this.rotatingFaceMatrix.reset();
                // Calcula los grados de rotación deseados
                float degrees = (float) (this.faceRotateClock.getTimestamp() * 360
                        / this.faceRotateClock.getTotalFrames());
                // Rota la imagen pivotando sobre el centro de la imagen
                this.rotatingFaceMatrix.postRotate(degrees,
                        (float) this.loadingFace.getWidth() / 2,
                        (float) this.loadingFace.getHeight() / 2);
                // Desplaza la imagen al punto deseado
                this.rotatingFaceMatrix.postTranslate(
                        GameEngine.RESOLUTION_X - 160, GameEngine.RESOLUTION_Y - 150);
            }
            // Dibuja el fondo negro
            canvas.drawBitmap(this.blackBackground, this.basicMatrix, this.loadingPaint);
            // Dibuja la cara seleccionada con los ajustes calculados
            canvas.drawBitmap(this.loadingFace, this.rotatingFaceMatrix, this.loadingPaint);
            // Dibuja el texto de 'cargando'
            String loadingText = "Loading";
            int dots = this.textClock.getTimestamp();
            for (int i = 0; i < dots; i++) {
                loadingText += '.';
            }
            canvas.drawText(loadingText,
                    // 300, 300,
                    GameEngine.RESOLUTION_X - 530, GameEngine.RESOLUTION_Y - 60,
                    loadingPaint);
        };
    }

    /**
     * No hace nada. No debe hacer nada.
     * @return
     */
    @Override
    public Touchable definedTouchable() {
        return point -> {};
    }

    @Override
    public void onSceneChange(SCENES oldScene) {

    }

    @Override
    public SCENES getSceneId() {
        return GameEngine.SCENES.LOADING;
    }

    @Override
    public List<Slidable> getSlidables() {
        return null;
    }

    @Override
    public void clearScreen() {
        // quitar el reloj
        // quitar el índice de la cara
        this.loadingConstructed = false;
        this.textClock = null;
        this.selectedRandomFace = -1;
        this.rotatingFaceMatrix.reset();
        this.textMatrix.reset();
    }
}
