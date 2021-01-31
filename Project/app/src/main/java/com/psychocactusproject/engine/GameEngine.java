package com.psychocactusproject.engine;

import android.app.Activity;
import android.content.Context;

import com.psychocactusproject.graphics.views.GameView;
import com.psychocactusproject.graphics.views.SurfaceGameView;
import com.psychocactusproject.input.InputController;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {

    public final static int RESOLUTION_X = 1280;
    public final static int RESOLUTION_Y = 720;
    private UpdateThread updateThread;
    private DrawThread drawThread;
    private List<GameEntity> gameEntities;
    private List<GameEntity> entitiesToAdd;
    private List<GameEntity> entitiesToRemove;
    private InputController inputController;
    private Activity activity;
    private GameView gameView;
    private int width;
    private int height;
    private GameClock engineClock;
    private int adaptedWidth;
    private int adaptedHeight;
    private int aspectRatioMargin;
    private BlackStripesTypes hasBlackStripes;
    public enum BlackStripesTypes {FALSE, TOP_BOTTOM, LEFT_RIGHT};

    public GameEngine(Activity activity, GameView gameView) {
        this.activity = activity;
        this.gameView = gameView;
        this.gameEntities = new ArrayList();
        this.entitiesToAdd = new ArrayList();
        this.entitiesToRemove = new ArrayList();
        this.gameView.setGameEntities(this.gameEntities);
        this.width = gameView.getWidth() - gameView.getPaddingLeft() - gameView.getPaddingRight();
        this.height = gameView.getHeight() - gameView.getPaddingTop() - gameView.getPaddingBottom();
        this.engineClock = new GameClock(1, 1);
    }

    public void adjustScreenAspectRatio(int deviceWidth, int deviceHeight) {
        if (!equalDoubleDivisions(deviceWidth, deviceHeight, 16, 9)) {
            if ((float) deviceWidth / deviceHeight < 16 / 9.) {
                // Si la relación es menor, tenemos bandas negras arriba y abajo
                this.adaptedWidth = deviceWidth;
                this.adaptedHeight = deviceWidth * 9 / 16;
                this.aspectRatioMargin = (deviceHeight - this.adaptedHeight) / 2;
                this.hasBlackStripes = BlackStripesTypes.TOP_BOTTOM;
            } else {
                // Si la relación es mayor, tenemos bandas negras a derecha e izquierda
                this.adaptedHeight = deviceHeight;
                this.adaptedWidth = deviceHeight * 16 / 9;
                this.aspectRatioMargin = (deviceWidth - this.adaptedWidth) / 2;
                this.hasBlackStripes = BlackStripesTypes.LEFT_RIGHT;
            }
        } else {
            // Si la relación de pantalla es de 16/9, las medidas utilizadas son las naturales
            this.adaptedWidth = deviceWidth;
            this.adaptedHeight = deviceHeight;
            this.aspectRatioMargin = 0;
            this.hasBlackStripes = BlackStripesTypes.FALSE;
        }
        // Después de calcular las medidas, se ajustan los parámetros de dibujado
        ((SurfaceGameView) (this.gameView)).setAspectRatio(deviceWidth, deviceHeight, this);
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

    public int getAspectRatioMargin() {
        return this.aspectRatioMargin;
    }

    public BlackStripesTypes hasBlackStripes() {
        return this.hasBlackStripes;
    }

    public void startGame() {
        // Si el juego está en marcha, se detiene
        this.stopGame();
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
        }
        // Falta revisar
    }

    public void removeGameEntity(GameEntity gameEntity) {
        this.entitiesToRemove.add(gameEntity);
        // Falta revisar
    }

    public void updateGame(long ellapsedTime) {
        for (int i = 0; i < this.gameEntities.size(); i++) {
            this.gameEntities.get(i).update(ellapsedTime, this);
        }
        synchronized (gameEntities) {
            while (!entitiesToRemove.isEmpty()) {
                gameEntities.remove(entitiesToRemove.remove(0));
            }
            while (!entitiesToAdd.isEmpty()) {
                gameEntities.add(entitiesToAdd.remove(0));
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
