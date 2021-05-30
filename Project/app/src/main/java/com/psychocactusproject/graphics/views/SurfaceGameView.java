package com.psychocactusproject.graphics.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.psychocactusproject.R;
import com.psychocactusproject.engine.screens.GameScreen;
import com.psychocactusproject.engine.screens.InitialScreen;
import com.psychocactusproject.engine.screens.LoadingScreen;
import com.psychocactusproject.engine.screens.PauseScreen;;
import com.psychocactusproject.engine.screens.Scene;
import com.psychocactusproject.graphics.controllers.ClickableDirectSprite;
import com.psychocactusproject.graphics.interfaces.DebugDrawable;
import com.psychocactusproject.graphics.interfaces.Drawable;
import com.psychocactusproject.graphics.controllers.InanimateSprite;
import com.psychocactusproject.interaction.menu.DialogScreen;
import com.psychocactusproject.engine.util.GameClock;
import com.psychocactusproject.engine.manager.GameEngine;
import com.psychocactusproject.engine.manager.GameEntity;

import static com.psychocactusproject.engine.manager.GameEngine.BLACK_STRIPE_TYPES;
import static com.psychocactusproject.engine.manager.GameEngine.SCENES;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Provisional: Me tengo que resignar a que esta clase centralice lo que tenga que ver con
 * renderizar, visualizar o organizar layouts escritos en java
 */
public class SurfaceGameView extends SurfaceView implements SurfaceHolder.Callback {

    public static SurfaceGameView getInstance() {
        return GameEngine.getInstance().getSurfaceGameView();
    }

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
    // InanimateSprite que imita las bandas negras. Será mostrado si la relación de pantalla no es de 16/9
    private InanimateSprite backgroundSprite;
    // Efectos de imagen
    private static int filterLevels = 20;
    private static Paint[] colorFilters;
    private static GameClock filterClock;
    //
    private HashMap<SCENES, Scene> gameScenes;
    private InitialScreen initialScreen;
    private GameScreen gameScreen;
    private PauseScreen pauseScreen;
    private DialogScreen dialogScreen;
    private LoadingScreen loadingScreen;
    //
    private final List<ClickableDirectSprite> pauseEntities;
    private final List<ClickableDirectSprite> initialEntities;
    //
    private final HashMap<SCENES, Drawable> drawableScenesMap;

    /**
     * PROVISIONAL: Este constructor es llamado nada más crearse la vista
     * @param context
     * @param attributeSet
     */
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

        this.pauseEntities = new ArrayList<>();
        this.initialEntities = new ArrayList<>();
        this.initialScreen = new InitialScreen();
        this.gameScreen = new GameScreen();
        this.pauseScreen = new PauseScreen();
        this.dialogScreen = new DialogScreen();
        this.loadingScreen = new LoadingScreen();
        this.gameScenes = new HashMap<SCENES, Scene>() {
            {
                put(SCENES.GAME, gameScreen); put(SCENES.PAUSE_MENU, pauseScreen);
                put(SCENES.DIALOG, dialogScreen); put(SCENES.LOADING, loadingScreen);
                put(SCENES.INITIAL_SCREEN, initialScreen);
            }
        };
        this.pauseScreen.setPauseEntities(this.pauseEntities);
        this.initialScreen.setInitialEntities(this.initialEntities);
        //
        this.drawableScenesMap = new HashMap<>();
        this.drawableScenesMap.put(SCENES.GAME, this.gameScreen.definedDrawable());
        this.drawableScenesMap.put(SCENES.DIALOG, this.dialogScreen.definedDrawable());
        this.drawableScenesMap.put(SCENES.INITIAL_SCREEN, this.initialScreen.definedDrawable());
        this.drawableScenesMap.put(SCENES.PAUSE_MENU, this.pauseScreen.definedDrawable());
    }


    public InitialScreen getInitialScreen() {
        return this.initialScreen;
    }

    public GameScreen getGameScreen() {
        return this.gameScreen;
    }

    public PauseScreen getPauseScreen() {
        return this.pauseScreen;
    }

    // Esto se está agravando ya. Creo que lo más coherente con diferencia es mandar todas
    // las escenas o a GameEngine o a GameLogic, teniendo la primera opción bastante más papeletas
    public Scene getCurrentScene() {
        return this.gameScenes.get(this.gameEngine.getCurrentScene());
    }

    public void onSceneChange(SCENES oldScene, SCENES scene) {
        this.gameScenes.get(scene).onSceneChange(oldScene);
    }

    /*
    * YA QUE LOS FRAGMENTOS DE CÓDIGOS HAN SIDO ESCRITOS COMO FUNCIONALES, PODRÍA APLICARSE
    * PARA "SUPERPONER" CAPAS DE DIBUJADO. CUANDO ABRAMOS LA ESCENA MENÚ O LA ESCENA AVISO,
    * ESTAS ESCENAS LLAMARÍAN TAMBIÉN A JUEGO, CON LO QUE TENEMOS ESTO SIENDO DIBUJADO DE FONDO
    * MIENTRAS DIBUJAMOS UNA NUEVA ESCENA
    * */

    public void setAspectRatio(int deviceWidth, int deviceHeight, GameEngine gameEngine) {
        // Se informan a la clase los parámetros de tamaño natural de la pantalla
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

    public void setGameEntities(List<Drawable> gameSprites, List<DebugDrawable> debugSprites) {
        this.gameSprites = gameSprites;
        this.debugSprites = debugSprites;
    }

    public void setGameEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public void showConfirmationDialog(String message, Runnable action) {
        this.showConfirmationDialog(message, null, action);
    }

    public void showConfirmationDialog(String message, String details, Runnable action) {
        this.dialogScreen.initializeDialog(message, details, action);
        this.gameEngine.showDialog();
    }

    public void showAlertDialog(String message) {
        this.showAlertDialog(message, null);
    }

    public void showAlertDialog(String message, String details) {
        this.dialogScreen.initializeDialog(message, details);
        this.gameEngine.showDialog();
    }

    public DialogScreen getDialog() {
        return this.dialogScreen;
    }

    public void clearDialog() {
        // this.dialogScreen = null;
    }

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
        synchronized (GameEntity.entitiesLock) {
            // Dibuja la escena actual
            this.drawableScenesMap.get(GameEngine.getInstance().getCurrentScene()).draw(this.frameCanvas);
        }
        // Reescala el frame de juego y lo posiciona en la pantalla del dispositivo
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(this.frameBitmap,
                this.adaptedWidth, this.adaptedHeight, false);
        screen.drawBitmap(scaledBitmap, this.basicMatrix, this.basicPaint);
        // Plasma el frame obtenido tras aplicar el dibujado de los elementos
        getHolder().unlockCanvasAndPost(screen);
    }

    public Bitmap getFrameBitmap() {
        return this.frameBitmap;
    }

    public static void clearCanvas(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
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
