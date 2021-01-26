package com.psychocactusproject.engine;

import android.app.Activity;
import android.content.Context;

import com.psychocactusproject.characters.band.Bass;
import com.psychocactusproject.graphics.views.GameView;
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
    private double pixelFactor;
    private GameClock engineClock;

    public GameEngine(Activity activity, GameView gameView) {
        this.activity = activity;
        this.gameView = gameView;
        this.gameEntities = new ArrayList();
        this.entitiesToAdd = new ArrayList();
        this.entitiesToRemove = new ArrayList();
        this.gameView.setGameEntities(this.gameEntities);
        this.width = gameView.getWidth() - gameView.getPaddingLeft() - gameView.getPaddingRight();
        this.height = gameView.getHeight() - gameView.getPaddingTop() - gameView.getPaddingBottom();
        this.pixelFactor = this.height / 400d;
        this.engineClock = new GameClock(1, 1000);
    }

    public void startGame() {
        // If game is running, then stop it
        this.stopGame();
        // Setting up game objects
        for (int i = 0; i < this.gameEntities.size(); i++) {
            this.gameEntities.get(i).initialize();
        }
        // Starting update thread
        this.updateThread = new UpdateThread(this);
        this.updateThread.start();
        // Starting drawing thread
        this.drawThread = new DrawThread(this);
        this.drawThread.start();
        // Manage controller
        this.inputController.onStart();
    }

    public void stopGame() {
        if (this.updateThread != null) {
            this.updateThread.stopUpdating();
        }
        if (this.drawThread != null) {
            this.drawThread.stopDrawing();
        }
        // Manage controller
        this.inputController.onStop();
    }

    public void pauseGame() {
        if (this.updateThread != null) {
            this.updateThread.pauseUpdate();
        }
        if (this.drawThread != null) {
            this.drawThread.pauseDraw();
        }
        // Manage controller
        this.inputController.onPause();
    }

    public void resumeGame() {
        if (this.updateThread != null) {
            this.updateThread.resumeUpdate();
        }
        if (this.drawThread != null) {
            this.drawThread.resumeDraw();
        }
        // Manage controller
        this.inputController.onResume();
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

    public InputController getInputController() {
        return this.inputController;
    }

    public void setInputController(InputController inputController) {
        this.inputController = inputController;
    }

    public double getPixelFactor() {
        return this.pixelFactor;
    }

}
