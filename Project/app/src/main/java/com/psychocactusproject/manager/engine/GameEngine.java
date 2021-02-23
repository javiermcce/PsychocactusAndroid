package com.psychocactusproject.manager.engine;

import android.app.Activity;
import android.content.Context;

import com.psychocactusproject.graphics.controllers.AbstractSprite;
import com.psychocactusproject.graphics.views.GameView;
import com.psychocactusproject.graphics.views.SurfaceGameView;
import com.psychocactusproject.input.InputController;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {

    public final static boolean DEBUGGING = true;
    public final static int RESOLUTION_X = 1280;
    public final static int RESOLUTION_Y = 720;
    private UpdateThread updateThread;
    private DrawThread drawThread;
    private final List<GameEntity> gameEntities;
    private List<GameEntity> entitiesToAdd;
    private List<GameEntity> entitiesToRemove;
    private List<AbstractSprite> gameSprites;
    private InputController inputController;
    private Activity activity;
    private GameView gameView;
    private int deviceWidth;
    private int deviceHeight;
    private GameClock engineClock;
    private int adaptedWidth;
    private int adaptedHeight;
    private int aspectRatioMargin;
    private BlackStripesTypes hasBlackStripes;
    public enum BlackStripesTypes {FALSE, TOP_BOTTOM, LEFT_RIGHT};
    public GameLogic gameLogic;

    public GameEngine(Activity activity, GameView gameView) {
        this.activity = activity;
        this.gameView = gameView;
        this.gameEntities = new ArrayList<>();
        this.entitiesToAdd = new ArrayList<>();
        this.entitiesToRemove = new ArrayList<>();
        this.gameSprites = new ArrayList<>();
        this.gameView.setGameEntities(this.gameEntities, this.gameSprites);
        this.deviceWidth = gameView.getWidth();
        this.deviceHeight = gameView.getHeight();
        this.engineClock = new GameClock(1, 1);
        this.gameLogic = GameLogic.initialize();
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
                this.hasBlackStripes = BlackStripesTypes.TOP_BOTTOM;
            } else {
                // Si la relación es mayor, tenemos bandas negras a derecha e izquierda
                this.adaptedHeight = this.deviceHeight;
                this.adaptedWidth = this.deviceHeight * 16 / 9;
                this.aspectRatioMargin = (this.deviceWidth - this.adaptedWidth) / 2;
                this.hasBlackStripes = BlackStripesTypes.LEFT_RIGHT;
            }
        } else {
            // Si la relación de pantalla es de 16/9, las medidas utilizadas son las naturales
            this.adaptedWidth = this.deviceWidth;
            this.adaptedHeight = this.deviceHeight;
            this.aspectRatioMargin = 0;
            this.hasBlackStripes = BlackStripesTypes.FALSE;
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

    public int getAspectRatioMargin() {
        return this.aspectRatioMargin;
    }

    public BlackStripesTypes hasBlackStripes() {
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
            this.entitiesToAdd.add(gameEntity);
        } else {
            this.gameEntities.add(gameEntity);
            if (gameEntity instanceof AbstractSprite) {
                this.gameSprites.add((AbstractSprite) gameEntity);
            }
        }
    }

    public void removeGameEntity(GameEntity gameEntity) {
        if (this.isRunning()) {
            this.entitiesToRemove.add(gameEntity);
        } else {
            this.gameEntities.remove(gameEntity);
            if (gameEntity instanceof AbstractSprite) {
                this.gameSprites.remove(gameEntity);
            }
        }
    }

    public void updateGame(long ellapsedTime) {
        this.inputController.update();
        for (int i = 0; i < this.gameEntities.size(); i++) {
            this.gameEntities.get(i).update(ellapsedTime, this);
        }
        synchronized (GameEntity.entitiesLock) { // recordar que hay que arreglar lo del lock
            while (!entitiesToRemove.isEmpty()) {
                GameEntity entityToRemove = entitiesToRemove.remove(0);
                gameEntities.remove(entityToRemove);
                if (entityToRemove instanceof AbstractSprite) {
                    this.gameSprites.remove(entityToRemove);
                }
            }
            while (!entitiesToAdd.isEmpty()) {
                GameEntity entity = entitiesToAdd.remove(0);
                gameEntities.add(entity);
                if (entity instanceof AbstractSprite) {
                    this.gameSprites.add((AbstractSprite) entity);
                }
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

}
