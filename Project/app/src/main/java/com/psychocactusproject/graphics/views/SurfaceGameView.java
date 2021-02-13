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

import com.psychocactusproject.R;
import com.psychocactusproject.graphics.controllers.AbstractSprite;
import com.psychocactusproject.graphics.controllers.InanimateSprite;
import com.psychocactusproject.interaction.menu.MenuDisplay;
import com.psychocactusproject.manager.engine.GameEngine;
import com.psychocactusproject.manager.engine.Hitbox;
import com.psychocactusproject.manager.engine.Point;

import java.util.LinkedList;
import java.util.List;

import static com.psychocactusproject.manager.engine.GameEngine.BlackStripesTypes.FALSE;
import static com.psychocactusproject.manager.engine.GameEngine.BlackStripesTypes.LEFT_RIGHT;
import static com.psychocactusproject.manager.engine.GameEngine.BlackStripesTypes.TOP_BOTTOM;

public class SurfaceGameView extends SurfaceView implements SurfaceHolder.Callback, GameView {

    private List<AbstractSprite> gameEntities;
    private boolean ready;
    private final Canvas frameCanvas;
    private final Paint basicPaint;
    private final Bitmap frameBitmap;
    private final Matrix basicMatrix;
    // Medidas de la pantalla física adaptadas a las medidas del videojuego
    private int adaptedWidth;
    private int adaptedHeight;
    // Medidas naturales de la pantalla física
    private int deviceWidth;
    private int deviceHeight;
    // InanimateSprite que imita las bandas negras. Será mostrado si la relación de pantalla no es de 16/9
    private InanimateSprite backgroundSprite;

    // DEBUG
    public static List<Point> inputMovePoints = new LinkedList<>();

    public SurfaceGameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        // Permite conocer el estado del surface creado en fragment_game
        this.getHolder().addCallback(this);
        // Las llamadas de dibujado requieren la información que contienen Paint, Canvas y Matrix
        this.frameCanvas = new Canvas();
        this.basicPaint = new Paint();
        this.basicMatrix = new Matrix();
        // Sobre este canvas y bitmap se dibujará en su totalidad el juego, a una resolución fija
        this.frameBitmap = Bitmap.createBitmap(
                GameEngine.RESOLUTION_X, GameEngine.RESOLUTION_Y,
                Bitmap.Config.ARGB_8888
        );
        this.frameCanvas.setBitmap(this.frameBitmap);
    }

    public void setAspectRatio(int deviceWidth, int deviceHeight, GameEngine gameEngine) {
        // Se informan a la clase los parámetros de tamaño natural de la pantalla
        this.deviceWidth = deviceWidth;
        this.deviceHeight = deviceHeight;
        // Se obtiene el tamaño adaptado de la pantalla calculado por el motor
        this.adaptedWidth = gameEngine.getAdaptedWidth();
        this.adaptedHeight = gameEngine.getAdaptedHeight();
        // Se da de alta el sprite para el fondo de pantalla
        this.backgroundSprite = new InanimateSprite(gameEngine, R.drawable.background_black_bars, "Background Bars Image", null);
        // Si la pantalla no tendrá bandas negras, porque la relación de pantalla es de 16/9
        if (gameEngine.hasBlackStripes() != FALSE) {
            int backgroundX = deviceWidth;
            int backgroundY = deviceHeight;
            // Si las bandas negras están arriba y abajo
            if (gameEngine.hasBlackStripes() == TOP_BOTTOM) {
                this.basicMatrix.postTranslate(0, gameEngine.getAspectRatioMargin());
                backgroundX = (int) (((double) deviceHeight / this.adaptedHeight) * this.adaptedWidth);
                backgroundY = deviceHeight;
            // Si las bandas negras están a los laterales
            } else if (gameEngine.hasBlackStripes() == LEFT_RIGHT) {
                this.basicMatrix.postTranslate(gameEngine.getAspectRatioMargin(), 0);
                backgroundX = deviceWidth;
                backgroundY = (int) (((double) deviceWidth / this.adaptedWidth) * this.adaptedHeight);
            }
            // Si la pantalla va a mostrar el fondo se debe ajustar a su tamaño
            this.backgroundSprite.resizeBitmap(backgroundX, backgroundY);
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
    public void setGameEntities(List<AbstractSprite> gameEntities) {
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
        // Dibuja el fondo y sobrescribe el anterior frame
        this.backgroundSprite.draw(screen);
        // DEBUG: dibuja en el frame de juego unas figuras equivalentes en todos los dispositivos
        if (GameEngine.DEBUGGING) {
            this.frameCanvas.drawRGB(0, 0, 0);
            this.frameDrawTest();
        }
        // Dibuja todos los elementos del juego por capas de prioridades
        // Prioridad 3: Personajes
        synchronized (this.gameEntities) {
            for (int i = 0; i < this.gameEntities.size(); i++) {
                this.gameEntities.get(i).draw(this.frameCanvas);
                if (GameEngine.DEBUGGING) {
                    Hitbox.drawHitboxes(this.gameEntities.get(i).getHitboxes(), frameCanvas);
                }
            }
        }
        // Prioridad 2: Menús
        synchronized (this.gameEntities) {
            for (int i = 0; i < this.gameEntities.size(); i++) {
                if (this.gameEntities.get(i) instanceof MenuDisplay) {
                    MenuDisplay menu = ((MenuDisplay) this.gameEntities.get(i));
                    menu.renderMenu(frameCanvas);
                    if (GameEngine.DEBUGGING) {
                        Hitbox.drawHitboxes(menu.getMenu().getHitboxes(), frameCanvas);
                    }
                }
            }
        }
        // Prioridad 1: Interfaz de usuario

        // Reescala el frame de juego y lo posiciona en la pantalla del dispositivo
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(this.frameBitmap,
                this.adaptedWidth, this.adaptedHeight, false);
        screen.drawBitmap(scaledBitmap, this.basicMatrix, this.basicPaint);
        // DEBUG: Dibuja los puntos recorridos por la acción táctil de arrastrar
        if (GameEngine.DEBUGGING) {
            Paint basicPaint2 = new Paint();
            basicPaint2.setColor(Color.WHITE);
            synchronized (inputMovePoints) {
                for (Point punto : inputMovePoints) {
                    Rect rect = new Rect(punto.getX() - 2, punto.getY() - 2, punto.getX() + 2, punto.getY() + 2);
                    screen.drawRect(rect, basicPaint2);
                }
            }
        }
        // Plasma el frame obtenido tras aplicar el dibujado de los elementos
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
        this.basicPaint.setColor(Color.YELLOW);
        frameCanvas.drawRect(0, 0, 300, 300, this.basicPaint);
        this.basicPaint.setColor(Color.BLUE);
        frameCanvas.drawRect(0, 0, 200, 200, this.basicPaint);
        this.basicPaint.setColor(Color.BLACK);
        frameCanvas.drawRect(0, 0, 100, 100, this.basicPaint);
    }
}
