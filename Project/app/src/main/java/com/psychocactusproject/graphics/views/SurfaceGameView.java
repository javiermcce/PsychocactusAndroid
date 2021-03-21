package com.psychocactusproject.graphics.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.psychocactusproject.R;
import com.psychocactusproject.graphics.controllers.DebugDrawable;
import com.psychocactusproject.graphics.controllers.Drawable;
import com.psychocactusproject.graphics.controllers.InanimateSprite;
import com.psychocactusproject.input.TouchInputController;
import com.psychocactusproject.interaction.menu.MenuDisplay;
import com.psychocactusproject.interaction.scripts.Clickable;
import com.psychocactusproject.engine.GameClock;
import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.engine.GameEntity;
import com.psychocactusproject.engine.Hitbox;
import com.psychocactusproject.engine.GameEngine.GameDialog;
import com.psychocactusproject.engine.GameEngine.GameDialog.DIALOG_TYPE;
import static com.psychocactusproject.engine.GameEngine.BLACK_STRIPE_TYPES;
import static com.psychocactusproject.engine.GameEngine.SCENES;

import java.util.HashMap;
import java.util.List;


public class SurfaceGameView extends SurfaceView implements SurfaceHolder.Callback, GameView {

    // Si quisiera hacer bien esto de poder utilizar ambas views, deberían
    // implementar una interfaz común hecha expresamente a propósito

    private List<GameEntity> gameEntities;
    private List<Drawable> gameSprites;
    private List<DebugDrawable> debugSprites;
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

    private HashMap<SCENES, Drawable> drawableScenesMap;

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

        this.drawableScenesMap = new HashMap<>();
        this.drawableScenesMap.put(GameEngine.getCurrentScene(), this.definedGameDrawable());
    }


    /*
    * YA QUE LOS FRAGMENTOS DE CÓDIGOS HAN SIDO ESCRITOS COMO FUNCIONALES, PODRÍA APLICARSE
    * PARA "SUPERPONER" CAPAS DE DIBUJADO. CUANDO ABRAMOS LA ESCENA MENÚ O LA ESCENA AVISO,
    * ESTAS ESCENAS LLAMARÍAN TAMBIÉN A JUEGO, CON LO QUE TENEMOS ESTO SIENDO DIBUJADO DE FONDO
    * MIENTRAS DIBUJAMOS UNA NUEVA ESCENA
    * */
    public Drawable definedGameDrawable() {
        // Dibuja todos los elementos del juego por capas de prioridades
        return (canvas) -> {
            // Prioridad 3: Personajes
            synchronized (GameEntity.entitiesLock) {
                for (List<Drawable> drawableLayers : this.gameEngine.getDrawableLayers()) {
                    for (Drawable drawableEntity : drawableLayers) {
                        drawableEntity.draw(canvas);
                    }
                }
            }
            // Prioridad 2: Menús
            synchronized (GameEntity.entitiesLock) {
                for (int i = 0; i < this.gameSprites.size(); i++) {
                    if (this.gameSprites.get(i) instanceof MenuDisplay) {
                        MenuDisplay menuHolder = ((MenuDisplay) this.gameSprites.get(i));
                        if (menuHolder.isMenuOpen()) {
                            menuHolder.renderMenu(canvas);
                            if (GameEngine.DEBUGGING) {
                                Hitbox[] menuHitboxes = menuHolder.getMenu().getHitboxes();
                                // El fragmento de aquí abajo omite mostrar las hitboxes no activadas
                                // cuando realmente lo que deseo es mostrarlas desactivadas y seguir
                                // interactuando con ellas, pero de otra forma distinta
                            /*
                            Hitbox[] availableHitboxes = new Hitbox[menuHitboxes.length];
                            for (int j = 0; j < availableHitboxes.length; j++) {
                                if (menuHolder.getMenu().isAvailable(j)) {
                                    availableHitboxes[j] = menuHitboxes[j];
                                }
                            }*/
                                // Hitbox.drawHitboxes(availableHitboxes, canvas);
                                Hitbox.drawHitboxes(menuHitboxes, canvas);
                            }
                        }
                    }
                }
            }
            // Prioridad 1: Interfaz de usuario
            synchronized (GameEntity.entitiesLock) {
                if (GameEngine.DEBUGGING) {
                    for (int i = 0; i < this.debugSprites.size(); i++) {
                        this.debugSprites.get(i).debugDraw(canvas);
                    }
                }
                this.printAlerts();
                for (int i = 0; i < this.gameSprites.size(); i++) {
                    if (GameEngine.DEBUGGING && this.gameSprites.get(i) instanceof Clickable) {
                        Hitbox.drawHitboxes(((Clickable) this.gameSprites.get(i)).getHitboxes(), canvas);
                    }
                }
            }
        };
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
        if (gameEngine.hasBlackStripes() != BLACK_STRIPE_TYPES.FALSE) {
            int backgroundX = deviceWidth;
            int backgroundY = deviceHeight;
            // Si las bandas negras están arriba y abajo
            if (gameEngine.hasBlackStripes() == BLACK_STRIPE_TYPES.TOP_BOTTOM) {
                this.basicMatrix.postTranslate(0, gameEngine.getAspectRatioMargin());
                backgroundX = (int) (((double) deviceHeight / this.adaptedHeight) * this.adaptedWidth);
                backgroundY = deviceHeight;
            // Si las bandas negras están a los laterales
            } else if (gameEngine.hasBlackStripes() == BLACK_STRIPE_TYPES.LEFT_RIGHT) {
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
    public void setGameEntities(List<GameEntity> gameEntities, List<Drawable> gameSprites, List<DebugDrawable> debugSprites) {
        this.gameEntities = gameEntities;
        this.gameSprites = gameSprites;
        this.debugSprites = debugSprites;
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
        }
        // Dibuja la escena actual
        this.drawableScenesMap.get(GameEngine.getCurrentScene()).draw(this.frameCanvas);
        // Reescala el frame de juego y lo posiciona en la pantalla del dispositivo
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(this.frameBitmap,
                this.adaptedWidth, this.adaptedHeight, false);
        screen.drawBitmap(scaledBitmap, this.basicMatrix, this.basicPaint);
        // Plasma el frame obtenido tras aplicar el dibujado de los elementos
        getHolder().unlockCanvasAndPost(screen);
    }

    private void printAlerts() {
        if (GameEngine.getDialog() == null) {
            return;
        }
        GameDialog dialog = GameEngine.getDialog();
        if (dialog.getType() == DIALOG_TYPE.ALERT) {

        } else if (dialog.getType() == DIALOG_TYPE.CONFIRMATION) {

        }
    }

    private void printAlert(GameDialog dialog) {

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

    @FunctionalInterface
    public interface DrawableScene {

        public void drawScene();
    }
}
