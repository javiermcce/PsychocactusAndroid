package com.psychocactusproject.engine;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {

    private List<GameEntity> gameEntities;
    private UpdateThread updateThread;
    private DrawThread drawThread;
    private List<GameEntity> entities;
    private List<GameEntity> entitiesToAdd;
    private List<GameEntity> entitiesToRemove;
    private Activity activity;

    public GameEngine(Activity activity) {
        this.activity = activity;
        this.gameEntities = new ArrayList();
        this.entities = new ArrayList();
        this.entitiesToAdd = new ArrayList();
        this.entitiesToRemove = new ArrayList();
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
    }

    public void stopGame() {
        if (this.updateThread != null) {
            this.updateThread.stopUpdating();
        }
        if (this.drawThread != null) {
            this.drawThread.stopDrawing();
        }
    }

    public void pauseGame() {
        if (this.updateThread != null) {
            this.updateThread.pauseUpdate();
        }
        if (this.drawThread != null) {
            this.drawThread.pauseDraw();
        }
    }

    public void resumeGame() {
        if (this.updateThread != null) {
            this.updateThread.resumeUpdate();
        }
        if (this.drawThread != null) {
            this.drawThread.resumeDraw();
        }
    }

    public void addGameEntity(GameEntity gameEntity) {
        if (this.isRunning()) {
            this.entitiesToAdd.add(gameEntity);
        } else {
            this.entities.add(gameEntity);
        }
        // Falta revisar
    }

    public void removeGameEntity(GameEntity gameEntity) {
        this.entitiesToRemove.add(gameEntity);
        // Falta revisar
    }

    public void updateGame(long ellapsedTime) {
        for (int i = 0; i < this.gameEntities.size(); i++) {
            this.gameEntities.get(i).initialize();
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

    public void drawGame(long ellapsedTime) {
        // this.activity.runOnUiThread(drawRunnable);
    }

    public boolean isRunning() {
        return this.updateThread != null && this.updateThread.isUpdateRunning();
    }

    public boolean isPaused() {
        return this.updateThread != null && this.updateThread.isUpdatePaused();
    }
}
