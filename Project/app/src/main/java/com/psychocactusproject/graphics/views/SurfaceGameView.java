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
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.psychocactusproject.R;
import com.psychocactusproject.engine.InitialScreen;
import com.psychocactusproject.engine.PauseScreen;;
import com.psychocactusproject.engine.GameEngine.GAME_LAYERS;
import com.psychocactusproject.graphics.controllers.ClickableDirectSprite;
import com.psychocactusproject.graphics.interfaces.DebugDrawable;
import com.psychocactusproject.graphics.interfaces.Drawable;
import com.psychocactusproject.graphics.controllers.InanimateSprite;
import com.psychocactusproject.interaction.menu.DialogScreen;
import com.psychocactusproject.interaction.menu.MenuDisplay;
import com.psychocactusproject.interaction.scripts.Clickable;
import com.psychocactusproject.engine.GameClock;
import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.engine.GameEntity;
import com.psychocactusproject.engine.Hitbox;
import static com.psychocactusproject.engine.GameEngine.BLACK_STRIPE_TYPES;
import static com.psychocactusproject.engine.GameEngine.SCENES;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Provisional: Me tengo que resignar a que esta clase centralice lo que tenga que ver con
 * renderizar, visualizar o organizar layouts escritos en java
 */
public class SurfaceGameView extends SurfaceView implements SurfaceHolder.Callback {

    private List<Drawable> gameSprites;
    private List<DebugDrawable> debugSprites;
    private GameEngine gameEngine;
    private boolean ready;
    private final Canvas frameCanvas;
    private final Paint basicPaint;
    private final Bitmap frameBitmap;
    private final Matrix basicMatrix;
    private final Paint dialogPaint;
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

    private InitialScreen initialScreen;
    private PauseScreen pauseScreen;
    private DialogScreen activeDialog;
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
        this.dialogPaint = new Paint();
        this.dialogPaint.setColor(Color.argb(100, 20, 20, 50));
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
        this.pauseScreen = new PauseScreen();
        this.initialScreen = new InitialScreen();
        this.pauseScreen.setPauseEntities(this.pauseEntities);
        this.initialScreen.setInitialEntities(this.initialEntities);

        this.drawableScenesMap = new HashMap<>();
        this.drawableScenesMap.put(SCENES.GAME, this.definedGameDrawable());
        this.drawableScenesMap.put(SCENES.DIALOG, this.definedDialogDrawable());
        this.drawableScenesMap.put(SCENES.INITIAL_SCREEN, this.initialScreen.definedInitialDrawable());
        this.drawableScenesMap.put(SCENES.PAUSE_MENU, this.pauseScreen.definedPauseDrawable());
    }


    public InitialScreen getInitialScreen() {
        return this.initialScreen;
    }

    public PauseScreen getPauseScreen() {
        return this.pauseScreen;
    }

    /*
    * YA QUE LOS FRAGMENTOS DE CÓDIGOS HAN SIDO ESCRITOS COMO FUNCIONALES, PODRÍA APLICARSE
    * PARA "SUPERPONER" CAPAS DE DIBUJADO. CUANDO ABRAMOS LA ESCENA MENÚ O LA ESCENA AVISO,
    * ESTAS ESCENAS LLAMARÍAN TAMBIÉN A JUEGO, CON LO QUE TENEMOS ESTO SIENDO DIBUJADO DE FONDO
    * MIENTRAS DIBUJAMOS UNA NUEVA ESCENA
    * */
    public Drawable definedGameDrawable() {
        return this.definedGameDrawable(false);
    }

    public Drawable definedGameDrawable(boolean isSnapshotRender) {
        // Dibuja todos los elementos del juego por capas de prioridades
        return (canvas) -> {
            // Prioridad 3: Personajes
            for (GAME_LAYERS layer : GAME_LAYERS.values()) {
                // Si es llamado como snapshot se detiene al llegar a los objetos de interfaz
                if (isSnapshotRender && layer == GAME_LAYERS.USER_INTERFACE) {
                    return;
                }
                for (Drawable drawableEntity : this.gameEngine.getEntitiesByLayer(layer)) {
                    drawableEntity.draw(canvas);
                }
            }
            // Prioridad 2: Menús
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
            // Prioridad 1: Interfaz de usuario
            if (GameEngine.DEBUGGING) {
                for (int i = 0; i < this.debugSprites.size(); i++) {
                    this.debugSprites.get(i).debugDraw(canvas);
                }
            }
            for (int i = 0; i < this.gameSprites.size(); i++) {
                if (GameEngine.DEBUGGING && this.gameSprites.get(i) instanceof Clickable) {
                    Hitbox.drawHitboxes(((Clickable) this.gameSprites.get(i)).getHitboxes(), canvas);
                }
            }
        };
    }

    public Drawable definedDialogDrawable() {
        return (canvas) -> {
            //synchronized (GameEntity.entitiesLock) {
                // Dibuja de fondo el juego en su estado actual
                definedGameDrawable().draw(canvas);
                // Imprime la ventana de diálogo
                // REFACTORIZAR: PROHIBIDO LLAMAR CONSTRUCTORES EN EL BUCLE DE DIBUJADO
                // int left, int top, int right, int bottom
                canvas.drawRect(
                        new Rect(0, 0, GameEngine.RESOLUTION_X, GameEngine.RESOLUTION_Y),
                        this.dialogPaint);

                // Ahora se dibuja como tal el menú de diálogo
                this.activeDialog.draw(canvas);
                if (GameEngine.DEBUGGING) {
                    Hitbox.drawHitboxes(this.activeDialog.getHitboxes(), canvas);
                }
            //}
        };
    }

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
        this.activeDialog = new DialogScreen(this.gameEngine, message, details, action);
        this.gameEngine.addGameEntity(this.activeDialog, GameEngine.GAME_LAYERS.FRONT);
        this.gameEngine.switchToScene(GameEngine.SCENES.DIALOG);
    }

    public void showAlertDialog(String message) {
        this.showAlertDialog(message, null);
    }

    public void showAlertDialog(String message, String details) {
        this.activeDialog = new DialogScreen(this.gameEngine, message, details);
        this.gameEngine.addGameEntity(this.activeDialog, GameEngine.GAME_LAYERS.FRONT);
        this.gameEngine.switchToScene(GameEngine.SCENES.DIALOG);
    }

    public DialogScreen getDialog() {
        return this.activeDialog;
    }

    public void clearDialog() {
        this.gameEngine.removeGameEntity(this.activeDialog);
        this.activeDialog = null;
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
