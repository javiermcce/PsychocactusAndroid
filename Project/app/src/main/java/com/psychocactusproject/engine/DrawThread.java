package com.psychocactusproject.engine;

import java.util.Timer;
import java.util.TimerTask;

public class DrawThread extends Thread {

    private static int MAX_FPS = 60;
    private static final long DRAW_PERIODS = 1000 / MAX_FPS;

    private final GameEngine gameEngine;
    private boolean drawRunning;
    private boolean drawPaused;

    private Object lock = new Object();

    public DrawThread(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.drawRunning = true;
        this.drawPaused = false;
    }

    @Override
    public void run() {
        long currentTime;
        long ellapsedTime;
        long previousTime = System.currentTimeMillis();
        while (this.isDrawRunning()) {
            currentTime = System.currentTimeMillis();
            ellapsedTime = currentTime - previousTime;
            if (this.isDrawPaused()) {
                while (this.isDrawPaused()) {
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
            if (ellapsedTime < 15) { // Mayor a 60 fps
                try {
                    Thread.sleep(15 - ellapsedTime);
                } catch (InterruptedException e) {
                    System.err.println("Thread sleep interrupted unexpectedly");
                }
            }
            // long timeToSleep = DRAW_PERIODS - ellapsedTime;
            // revisar
            // drawGame puede costar tiempo, no tiene sentido esperar antes
            // esperaremos dentro de drawGame justo antes de mostrar el bitmap construido
            this.gameEngine.drawGame();
            previousTime = currentTime;
        }
    }

    @Override
    public void start() {
        this.drawRunning = true;
        this.drawPaused = false;
        super.start();
    }

    public void stopDrawing() {
        drawRunning = false;
        // If game is paused, it must be resumed to stop it
        this.resumeDraw();
    }

    public void pauseDraw() {
        this.drawPaused = true;
    }

    public void resumeDraw() {
        if (this.isDrawPaused()) {
            this.drawPaused = false;
            synchronized (this) {
                this.notify();
            }
        }
    }

    public boolean isDrawRunning() {
        return this.drawRunning;
    }

    public boolean isDrawPaused() {
        return this.drawPaused;
    }


}
