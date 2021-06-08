package com.psychocactusproject.engine.manager;

import android.content.Context;

import com.psychocactusproject.android.DebugHelper;
import com.psychocactusproject.android.SimpleActivity;
import com.psychocactusproject.engine.screens.LoadingScreen;
import com.psychocactusproject.engine.util.GameClock;
import com.psychocactusproject.graphics.interfaces.DebugDrawable;
import com.psychocactusproject.graphics.interfaces.Drawable;
import com.psychocactusproject.graphics.views.SurfaceGameView;
import com.psychocactusproject.input.InputController;
import com.psychocactusproject.interaction.menu.MenuDisplay;
import com.psychocactusproject.interaction.scripts.TurnChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GameEngine {

    public final static int RESOLUTION_X = 1280;
    public final static int RESOLUTION_Y = 720;
    private UpdateThread updateThread;
    private DrawThread drawThread;
    private final List<GameEntity> gameEntities;
    private final List<EntityToAdd> entitiesToAdd;
    private final List<GameEntity> entitiesToRemove;
    private final List<Drawable> gameDrawables;
    private final List<DebugDrawable> debugDrawables;
    private final HashMap<GAME_LAYERS, List<Drawable>> drawablesByLayer;
    private List<List<Drawable>> drawableLayersOrdered;
    private InputController inputController;
    private final SimpleActivity activity;
    private final SurfaceGameView surfaceGameView;
    private final int deviceWidth;
    private final int deviceHeight;
    private final GameClock engineClock;
    private int adaptedWidth;
    private int adaptedHeight;
    private int aspectRatioMargin;
    private BLACK_STRIPE_TYPES hasBlackStripes;
    private String gameLanguage;
    // Scenes
    private SCENES pendingSceneChange;

    public enum SCENES { INITIAL_SCREEN, DIALOG, GAME, PAUSE_MENU, LOADING }
    public enum BLACK_STRIPE_TYPES { FALSE, TOP_BOTTOM, LEFT_RIGHT }
    public enum GAME_LAYERS { BACKGROUND, OBJECTS, CHARACTERS, FRONT, USER_INTERFACE, DEBUG }
    private SCENES currentScene = SCENES.LOADING;

    // Resources
    private final GameLogic gameLogic;
    private final DebugHelper debugHelper;
    //
    //
    public static boolean DEBUGGING = false;
    public static boolean verboseDebugging = false;

    public GameEngine(SimpleActivity activity, SurfaceGameView surfaceGameView) {
        //
        this.activity = activity;
        this.surfaceGameView = surfaceGameView;
        //
        this.gameEntities = new ArrayList<>();
        this.entitiesToAdd = new ArrayList<>();
        this.entitiesToRemove = new ArrayList<>();
        this.drawablesByLayer = new HashMap<>();
        for (GAME_LAYERS key : GAME_LAYERS.values()) {
            this.drawablesByLayer.put(key, new LinkedList<>());
        }
        this.gameDrawables = new ArrayList<>();
        this.debugDrawables = new ArrayList<>();
        this.surfaceGameView.setGameEntities(this.gameDrawables, this.debugDrawables);
        //
        this.deviceWidth = activity.getWindow().getDecorView().getWidth();
        this.deviceHeight = activity.getWindow().getDecorView().getHeight();
        this.engineClock = new GameClock(1, 1);
        this.gameLogic = GameLogic.initialize(this);
        this.debugHelper = new DebugHelper(this);
        this.activity.setDebugHelper(debugHelper);
        this.pendingSceneChange = null;
        // Se calculan los tamaños de la pantalla
        this.adjustScreenAspectRatio();
    }

    public int getAdaptedWidth() {
        return this.adaptedWidth;
    }

    public int getAdaptedHeight() {
        return this.adaptedHeight;
    }

    public int getDeviceWidth() {
        return deviceWidth;
    }

    public int getDeviceHeight() {
        return deviceHeight;
    }

    public List<GameEntity> getGameEntities() {
        return this.gameEntities;
    }

    public List<Drawable> getEntitiesByLayer(GAME_LAYERS layer) {
        return this.drawablesByLayer.get(layer);
    }

    public List<Drawable> getGameDrawables() {
        return gameDrawables;
    }

    public List<DebugDrawable> getDebugDrawables() {
        return debugDrawables;
    }

    public List<List<Drawable>> getDrawableLayers() {
        if (this.drawableLayersOrdered == null) {
            this.drawableLayersOrdered = new LinkedList<>();
            for (int i = 0; i < this.drawablesByLayer.size(); i++) {
                this.drawableLayersOrdered.add(this.drawablesByLayer.get(GAME_LAYERS.values()[i]));
            }
        }
        return this.drawableLayersOrdered;
    }

    public int getAspectRatioMargin() {
        return this.aspectRatioMargin;
    }

    public BLACK_STRIPE_TYPES hasBlackStripes() {
        return this.hasBlackStripes;
    }

    public void adjustScreenAspectRatio() {
        if (!equalDoubleDivisions(this.deviceWidth, this.deviceHeight, 16, 9)) {
            if ((float) this.deviceWidth / this.deviceHeight < 16 / 9.) {
                // Si la relación es menor, tenemos bandas negras arriba y abajo
                this.adaptedWidth = this.deviceWidth;
                this.adaptedHeight = this.deviceWidth * 9 / 16;
                this.aspectRatioMargin = (this.deviceHeight - this.adaptedHeight) / 2;
                this.hasBlackStripes = BLACK_STRIPE_TYPES.TOP_BOTTOM;
            } else {
                // Si la relación es mayor, tenemos bandas negras a derecha e izquierda
                this.adaptedHeight = this.deviceHeight;
                this.adaptedWidth = this.deviceHeight * 16 / 9;
                this.aspectRatioMargin = (this.deviceWidth - this.adaptedWidth) / 2;
                this.hasBlackStripes = BLACK_STRIPE_TYPES.LEFT_RIGHT;
            }
        } else {
            // Si la relación de pantalla es de 16/9, las medidas utilizadas son las naturales
            this.adaptedWidth = this.deviceWidth;
            this.adaptedHeight = this.deviceHeight;
            this.aspectRatioMargin = 0;
            this.hasBlackStripes = BLACK_STRIPE_TYPES.FALSE;
        }
        // Después de calcular las medidas, se ajustan los parámetros de dibujado
        ((SurfaceGameView) (this.surfaceGameView)).setAspectRatio(this.deviceWidth, this.deviceHeight, this);
    }

    public boolean equalDoubleDivisions(double a, double b, double c, double d) {
        return Math.abs(((double) a / b) - ((double) c / d)) < 0.01;
    }

    public void startGame() {
        // Si el juego está en marcha, se detiene
        // this.stopGame();
        // Se crean e insertan los objetos en el motor
        this.gameLogic.getGameEntityManager().populate(this);
        // Se ajustan los objetos por primera vez
        for (int i = 0; i < this.gameEntities.size(); i++) {
            this.gameEntities.get(i).initialize();
        }
        // Se inicia el thread de actualización de estado
        this.updateThread = new UpdateThread(this);
        this.updateThread.start();
        // Se inicia el thread de dibujado
        this.drawThread = new DrawThread(this);
        this.drawThread.start();
        // Se inicia el gestor de controles
        this.inputController.start();
        //
        this.surfaceGameView.setGameEngine(this);
        //
        this.loadSceneTransition(SCENES.INITIAL_SCREEN);
    }

    public void loadSceneTransition(SCENES scene) {
        LoadingScreen.nextScene = scene;
        this.switchToScene(SCENES.LOADING);
    }

    public void resumeGame() {
        this.switchToScene(SCENES.GAME);
    }

    public void pauseGame() {
        this.switchToScene(SCENES.PAUSE_MENU);
    }

    public void showDialog() {
        this.switchToScene(SCENES.DIALOG);
    }

    public void restartGame() {
        // TEMPORAL, HASTA QUE SE IMPLEMENTE
        resumeGame();
    }

    /**
     * Vuelve al menú principal
     */
    public void openMainMenu() {
        this.switchToScene(SCENES.INITIAL_SCREEN);
    }

    /**
     * Cierra por completo la app
     */
    public void stopGameApp() {
        this.switchToScene(SCENES.INITIAL_SCREEN);
    }

    public void closeAllMenus() {
        // Cierra los menús
        for (GameEntity entity : this.gameEntities) {
            // Los que tienen menu, los cierran
            if (entity instanceof MenuDisplay) {
                ((MenuDisplay) entity).closeMenu();
            }
        }
    }

    public void addGameEntity(GameEntity gameEntity) {
        if (this.isRunning()) {
            this.entitiesToAdd.add(new EntityToAdd(gameEntity));
        } else {
            this.doAddGameEntity(gameEntity);
        }
    }

    public void addGameEntity(GameEntity gameEntity, GAME_LAYERS layer) {
        if (this.isRunning()) {
            this.entitiesToAdd.add(new EntityToAdd(gameEntity, layer));
        } else {
            this.doAddGameEntity(gameEntity, layer);
        }
    }

    public void addGameEntities(GameEntity[] gameEntities, GAME_LAYERS layer) {
        if (this.isRunning()) {
            for (GameEntity gameEntity : gameEntities) {
                this.entitiesToAdd.add(new EntityToAdd(gameEntity, layer));
            }
        } else {
            for (GameEntity gameEntity : gameEntities) {
                this.doAddGameEntity(gameEntity, layer);
            }
        }
    }

    public void removeGameEntity(GameEntity gameEntity) {
        if (this.isRunning()) {
            this.entitiesToRemove.add(gameEntity);
        } else {
            this.doRemoveGameEntity(gameEntity);
        }
    }

    // Método auténtico por el cual finalmente se añade
    private void doAddGameEntity(GameEntity gameEntity, GAME_LAYERS layer) {
        this.gameEntities.add(gameEntity);
        if (gameEntity instanceof Drawable) {
            this.gameDrawables.add((Drawable) gameEntity);
            // Si la entidad pertenece a alguna capa de entidad de juego, se suma al resto
            if (layer != null) {
                List<Drawable> layerList = this.drawablesByLayer.get(layer);
                if (layerList == null) {
                    throw new IllegalStateException("La lista a la que se intenta acceder no ha sido inicializada. " +
                            "Esta lista debe existir.");
                }
                layerList.add((Drawable) gameEntity);
            }
        }
        if (gameEntity instanceof TurnChecker) {
            this.gameLogic.getStateManager().addUpdatableEntity((TurnChecker) gameEntity);
        }
        if (gameEntity instanceof DebugDrawable) {
            this.debugDrawables.add((DebugDrawable) gameEntity);
        }
    }

    private void doAddGameEntity(GameEntity gameEntity) {
        this.doAddGameEntity(gameEntity, null);
    }

    // Método auténtico por el cual finalmente se borra
    private void doRemoveGameEntity(GameEntity gameEntity) {
        this.gameEntities.remove(gameEntity);
        if (gameEntity instanceof Drawable) {
            Drawable gameDrawable = (Drawable) gameEntity;
            this.gameDrawables.remove(gameDrawable);
            // Busca de entre las listas de layers y elimina si existe coincidencia
            for (List<Drawable> layer : this.drawablesByLayer.values()) {
                for (Drawable listedEntity : layer) {
                    if (gameDrawable.equals(listedEntity)) {
                        layer.remove(listedEntity);
                    }
                }
            }
        }
        if (gameEntity instanceof TurnChecker) {
            this.gameLogic.getStateManager().removeUpdatableEntity((TurnChecker) gameEntity);
        }
        if (gameEntity instanceof DebugDrawable) {
            this.debugDrawables.remove((DebugDrawable) gameEntity);
        }

    }

    public void updateGame(long ellapsedTime) {
        this.inputController.update();
        for (int i = 0; i < this.gameEntities.size(); i++) {
            this.gameEntities.get(i).update(this);
        }
        synchronized (GameEntity.entitiesLock) { // recordar que hay que arreglar lo del lock
            while (!entitiesToRemove.isEmpty()) {
                GameEntity entityToRemove = entitiesToRemove.remove(0);
                this.doRemoveGameEntity(entityToRemove);
            }
            while (!entitiesToAdd.isEmpty()) {
                GameEntity entityToAdd = entitiesToAdd.remove(0).gameEntity;
                this.doAddGameEntity(entityToAdd);
            }
            // El cambio de escena está sincronizado, para no ser ejecutado mientras nos
            // encontramos en medio de un ciclo de dibujado
            if (this.pendingSceneChange != null) {
                this.doSwitchToScene();
            }
        }
    }

    public void drawGame() {
        this.surfaceGameView.draw();
    }

    public boolean isRunning() {
        return this.updateThread != null && this.updateThread.isUpdateRunning();
    }

    public boolean isPaused() {
        return this.getCurrentScene().equals(SCENES.PAUSE_MENU);
        // return this.updateThread != null && this.updateThread.isUpdatePaused();
    }

    public Context getContext() {
        return this.surfaceGameView.getContext();
    }

    public SimpleActivity getSimpleActivity() {
        return this.activity;
    }

    public SurfaceGameView getSurfaceGameView() {
        return this.surfaceGameView;
    }

    public InputController getInputController() {
        return this.inputController;
    }

    public void setInputController(InputController inputController) {
        this.inputController = inputController;
    }

    public SCENES getCurrentScene() {
        return this.currentScene;
    }

    private void setCurrentScene(SCENES scene) {
        this.currentScene = scene;
    }

    private void switchToScene(SCENES scene) {
        this.pendingSceneChange = scene;
    }

    private void doSwitchToScene() {
        SCENES scene = this.pendingSceneChange;
        SCENES oldScene = this.getCurrentScene();
        this.getSurfaceGameView().onSceneChange(oldScene, scene);
        switch (oldScene) {
            case GAME:
                // la idea es que al final del fragmento ejecutable correspondiente, siempre se
                // acuda a este método para hacer efectivo el cambio de escena
                break;
            case DIALOG:
                this.getSurfaceGameView().clearDialog();
                break;
            case PAUSE_MENU:
                this.getSurfaceGameView().getPauseScreen().clearScreen();
                break;
            case LOADING:
                break;
            case INITIAL_SCREEN:
                this.getSurfaceGameView().getLoadingScreen().clearScreen();
                break;
            default:
                throw new IllegalStateException("Se ha seleccionado la escena " + scene.name()
                        + ", pero no es válida.");
        }
        this.currentScene = scene;
        this.pendingSceneChange = null;
    }

    public static GameEngine getInstance() {
        return GameLogic.getInstance().getGameEngine();
    }

    private static class EntityToAdd {

        private final GameEntity gameEntity;
        private final GAME_LAYERS layer;

        private EntityToAdd(GameEntity gameEntity, GAME_LAYERS layer) {
            this.gameEntity = gameEntity;
            this.layer = layer;
        }

        private EntityToAdd(GameEntity gameEntity) {
            this.gameEntity = gameEntity;
            this.layer = GAME_LAYERS.OBJECTS;
        }
    }

}
