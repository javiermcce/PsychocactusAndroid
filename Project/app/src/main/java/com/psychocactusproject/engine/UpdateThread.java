package com.psychocactusproject.engine;

public class UpdateThread extends Thread {

    private final GameEngine gameEngine;
    private boolean updateRunning;
    private boolean updatePaused;

    public UpdateThread(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.updateRunning = true;
        this.updatePaused = false;
    }

    @Override
    public void run() {
        long previousTime;
        long currentTime;
        long ellapsedTime;
        previousTime = System.currentTimeMillis();
        while (this.isUpdateRunning()) {
            currentTime = System.currentTimeMillis();
            ellapsedTime = currentTime - previousTime;
            if (this.isUpdatePaused()) {
                while (this.isUpdatePaused()) {
                    try {
                        synchronized (this) {
                            this.wait();
                        }
                    } catch (InterruptedException e) {
                        System.err.println("Thread wait interrupted unexpectedly");
                    }
                }
                currentTime = System.currentTimeMillis();
            }
            this.gameEngine.updateGame(ellapsedTime);
            previousTime = currentTime;
        }
    }

    @Override
    public void start() {
        this.updateRunning = true;
        this.updatePaused = false;
        super.start();
    }

    public void stopUpdating() {
        updateRunning = false;
        // If game is paused, it must be resumed to stop it
        this.resumeUpdate();
    }

    public void pauseUpdate() {
        this.updatePaused = true;
    }

    public void resumeUpdate() {
        if (this.isUpdatePaused()) {
            this.updatePaused = false;
            synchronized (this) {
                this.notify();
            }
        }
    }


    public boolean isUpdateRunning() {
        return this.updateRunning;
    }

    public boolean isUpdatePaused() {
        return this.updatePaused;
    }
}
