package com.psychocactusproject.graphics.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
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
import com.psychocactusproject.interaction.scripts.Clickable;
import com.psychocactusproject.engine.GameClock;
import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.engine.GameEntity;
import com.psychocactusproject.engine.Hitbox;
import com.psychocactusproject.engine.Point;

import java.util.LinkedList;
import java.util.List;

import static com.psychocactusproject.engine.GameEngine.BLACK_STRIPE_TYPES.FALSE;
import static com.psychocactusproject.engine.GameEngine.BLACK_STRIPE_TYPES.LEFT_RIGHT;
import static com.psychocactusproject.engine.GameEngine.BLACK_STRIPE_TYPES.TOP_BOTTOM;

public class SurfaceGameView extends SurfaceView implements SurfaceHolder.Callback, GameView {

    // Si quisiera hacer bien esto de poder utilizar ambas views, deberían
    // implementar una interfaz común hecha expresamente a propósito

    private List<GameEntity> gameEntities;
    private List<AbstractSprite> gameSprites;
    private GameEngine gameEngine;
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
    // Efectos de imagen
    private static int filterLevels = 20;
    private static Paint[] colorFilters;
    private static GameClock filterClock;
    // DEBUG
    public static final List<Point> inputMovePoints = new LinkedList<>();

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
        filterLevels = 20;
        colorFilters = generateColorFilters();
        filterClock = new GameClock(filterLevels, 0.6, true);
    }

    public void setAspectRatio(int deviceWidth, int deviceHeight, GameEngine gameEngine) {
        // Se informan a la clase los parámetros de tamaño natural de la pantalla
        this.deviceWidth = deviceWidth;
        this.deviceHeight = deviceHeight;
        // Se obtiene el tamaño adaptado de la pantalla calculado por el motor
        this.adaptedWidth = gameEngine.getAdaptedWidth();
        this.adaptedHeight = gameEngine.getAdaptedHeight();
        // Se da de alta el sprite para el fondo de pantalla
        this.backgroundSprite = new InanimateSprite(gameEngine, R.drawable.background_black_bars, "Background Bars Image");
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
    public void setGameEntities(List<GameEntity> gameEntities, List<AbstractSprite> gameSprites) {
        this.gameEntities = gameEntities;
        this.gameSprites = gameSprites;
    }

    @Override
    public void setGameEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
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
        synchronized (GameEntity.entitiesLock) {
            for (List<GameEntity> entityLayers : this.gameEngine.getEntityLayers()) {
                for (GameEntity gameEntity : entityLayers) {
                    if (gameEntity instanceof AbstractSprite) {
                        AbstractSprite sprite = (AbstractSprite) gameEntity;
                        sprite.draw(this.frameCanvas);
                    }
                }
            }
            /*
            for (int i = 0; i < this.gameSprites.size(); i++) {
                // QUEDA PENDIENTE ORDENAR POR CAPAS
                this.gameSprites.get(i).draw(this.frameCanvas);
                if (GameEngine.DEBUGGING && this.gameSprites.get(i) instanceof Clickable) {
                    Hitbox.drawHitboxes(((Clickable) this.gameSprites.get(i)).getHitboxes(), frameCanvas);
                }
            }*/
        }
        // Prioridad 2: Menús
        synchronized (GameEntity.entitiesLock) {
            for (int i = 0; i < this.gameSprites.size(); i++) {
                if (this.gameSprites.get(i) instanceof MenuDisplay) {
                    MenuDisplay menu = ((MenuDisplay) this.gameSprites.get(i));
                    menu.renderMenu(frameCanvas);
                    if (GameEngine.DEBUGGING) {
                        Hitbox.drawHitboxes(menu.getMenu().getHitboxes(), frameCanvas);
                    }
                }
            }
        }
        // Prioridad 1: Interfaz de usuario
        synchronized (GameEntity.entitiesLock) {
            for (int i = 0; i < this.gameSprites.size(); i++) {
                this.gameSprites.get(i).debugDraw(this.frameCanvas);
            }
            for (int i = 0; i < this.gameSprites.size(); i++) {
                if (GameEngine.DEBUGGING && this.gameSprites.get(i) instanceof Clickable) {
                    Hitbox.drawHitboxes(((Clickable) this.gameSprites.get(i)).getHitboxes(), frameCanvas);
                }
            }
        }
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

    private static Paint generateColorFilter(float contrast, float brightness) {
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, brightness,
                        0, contrast, 0, 0, brightness,
                        0, 0, contrast, 0, brightness,
                        0, 0, 0, 1, 0
                });
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        return paint;
    }

    private static Paint[] generateColorFilters() {
        float minContrast = 0.7f, maxContrast = 1;
        float minBrigtness = -30, maxBrightness = 20;
        Paint[] filters = new Paint[filterLevels];
        float contrastSlope = (maxContrast - minContrast) / filterLevels;
        float brightnessSlope = (maxBrightness - minBrigtness) / filterLevels;
        for (int i = 0; i < filterLevels; i++) {
            float contrastLevelValue = (contrastSlope * i) + minContrast;
            float brightnessLevelValue = (brightnessSlope * i) + minBrigtness;
            filters[i] = generateColorFilter(contrastLevelValue, brightnessLevelValue);
        }
        return filters;
    }

    public static Paint getColorFilter() {
        return colorFilters[filterClock.getTimestamp()];
    }
}
