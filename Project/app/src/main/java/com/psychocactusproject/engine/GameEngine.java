package com.psychocactusproject.engine;

import android.app.Activity;
import android.content.Context;

import com.psychocactusproject.android.DebugHelper;
import com.psychocactusproject.android.GameActivity;
import com.psychocactusproject.graphics.controllers.DebugDrawable;
import com.psychocactusproject.graphics.controllers.Drawable;
import com.psychocactusproject.graphics.views.GameView;
import com.psychocactusproject.graphics.views.SurfaceGameView;
import com.psychocactusproject.input.InputController;
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
    private final HashMap<CHARACTER_LAYERS, List<Drawable>> drawablesByLayer;
    private InputController inputController;
    private final GameActivity activity;
    private final GameView gameView;
    private final int deviceWidth;
    private final int deviceHeight;
    private GameClock engineClock;
    private int adaptedWidth;
    private int adaptedHeight;
    private int aspectRatioMargin;
    private BLACK_STRIPE_TYPES hasBlackStripes;
    public enum BLACK_STRIPE_TYPES {FALSE, TOP_BOTTOM, LEFT_RIGHT};
    public enum CHARACTER_LAYERS {BACKGROUND, UNSPECIFIED, FRONT};
    private final GameLogic gameLogic;
    private final DebugHelper debugHelper;
    //
    public static boolean DEBUGGING = false;
    public static boolean verboseDebugging = false;

    public GameEngine(GameActivity activity, GameView gameView) {
        this.activity = activity;
        this.gameView = gameView;
        this.gameEntities = new ArrayList<>();
        this.entitiesToAdd = new ArrayList<>();
        this.entitiesToRemove = new ArrayList<>();
        this.drawablesByLayer = new HashMap<>();
        for (CHARACTER_LAYERS key : CHARACTER_LAYERS.values()) {
            this.drawablesByLayer.put(key, new LinkedList<>());
        }
        this.gameDrawables = new ArrayList<>();
        this.debugDrawables = new ArrayList<>();
        this.gameView.setGameEntities(this.gameEntities, this.gameDrawables, this.debugDrawables);
        this.deviceWidth = gameView.getWidth();
        this.deviceHeight = gameView.getHeight();
        this.engineClock = new GameClock(1, 1);
        this.gameLogic = GameLogic.initialize();
        this.debugHelper = new DebugHelper(this);
        this.activity.setDebugHelper(debugHelper);
        // Se calculan los tamaños de la pantalla
        this.adjustScreenAspectRatio();
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
        ((SurfaceGameView) (this.gameView)).setAspectRatio(this.deviceWidth, this.deviceHeight, this);
    }

    public boolean equalDoubleDivisions(double a, double b, double c, double d) {
        return Math.abs(((double) a / b) - ((double) c / d)) < 0.01;
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

    public List<Drawable> getEntitiesByLayer(CHARACTER_LAYERS layer) {
        return this.drawablesByLayer.get(layer);
    }

    // Esto debe ser AbstractSprite
    public List<List<Drawable>> getDrawableLayers() {
        // Optimizar. La creación de objetos no está permitida en el bucle de dibujado.
        List<List<Drawable>> listaDeListas = new LinkedList<>();

        for (int i = 0; i < this.drawablesByLayer.size(); i++) {
            listaDeListas.add(this.drawablesByLayer.get(CHARACTER_LAYERS.values()[i]));
        }

        //return new LinkedList<>(this.entitiesByLayer.values());
        return listaDeListas;
    }

    public int getAspectRatioMargin() {
        return this.aspectRatioMargin;
    }

    public BLACK_STRIPE_TYPES hasBlackStripes() {
        return this.hasBlackStripes;
    }

    public void startGame() {
        // Si el juego está en marcha, se detiene
        this.stopGame();
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
        this.gameView.setGameEngine(this);
    }

    public void stopGame() {
        if (this.updateThread != null) {
            this.updateThread.stopUpdating();
        }
        if (this.drawThread != null) {
            this.drawThread.stopDrawing();
        }
        // Se detiene el gestor de controles
        this.inputController.stop();
    }

    public void pauseGame() {
        if (this.updateThread != null) {
            this.updateThread.pauseUpdate();
        }
        if (this.drawThread != null) {
            this.drawThread.pauseDraw();
        }
        // Se pausa el gestor de controles
        this.inputController.pause();
    }

    public void resumeGame() {
        if (this.updateThread != null) {
            this.updateThread.resumeUpdate();
        }
        if (this.drawThread != null) {
            this.drawThread.resumeDraw();
        }
        // Se reanuda el gestor de controles
        this.inputController.resume();
    }

    public void addGameEntity(GameEntity gameEntity) {
        if (this.isRunning()) {
            this.entitiesToAdd.add(new EntityToAdd(gameEntity));
        } else {
            this.doAddGameEntity(gameEntity);
        }
    }

    public void addGameEntity(GameEntity gameEntity, CHARACTER_LAYERS layer) {
        if (this.isRunning()) {
            this.entitiesToAdd.add(new EntityToAdd(gameEntity, layer));
        } else {
            this.doAddGameEntity(gameEntity, layer);
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
    private void doAddGameEntity(GameEntity gameEntity, CHARACTER_LAYERS layer) {
        this.gameEntities.add(gameEntity);
        if (gameEntity instanceof Drawable) {
            this.gameDrawables.add((Drawable) gameEntity);
            CHARACTER_LAYERS selected = layer != null ? layer : CHARACTER_LAYERS.UNSPECIFIED;
            List<Drawable> layerList = this.drawablesByLayer.get(selected);
            if (layerList == null) {
                throw new IllegalStateException("La lista a la que se intenta acceder no ha sido inicializada. " +
                        "Esta lista debe existir.");
            }
            layerList.add((Drawable) gameEntity);
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
        }
    }

    public void drawGame() {
        this.gameView.draw();
    }

    public boolean isRunning() {
        return this.updateThread != null && this.updateThread.isUpdateRunning();
    }

    public boolean isPaused() {
        return this.updateThread != null && this.updateThread.isUpdatePaused();
    }

    public Context getContext() {
        return gameView.getContext();
    }

    public Activity getActivity() {
        return this.activity;
    }

    public GameView getGameView() {
        return this.gameView;
    }

    public InputController getInputController() {
        return this.inputController;
    }

    public void setInputController(InputController inputController) {
        this.inputController = inputController;
    }

    private static class EntityToAdd {

        private final GameEntity gameEntity;
        private final CHARACTER_LAYERS layer;

        private EntityToAdd(GameEntity gameEntity, CHARACTER_LAYERS layer) {
            this.gameEntity = gameEntity;
            this.layer = layer;
        }

        private EntityToAdd(GameEntity gameEntity) {
            this.gameEntity = gameEntity;
            this.layer = CHARACTER_LAYERS.UNSPECIFIED;
        }
    }

}
