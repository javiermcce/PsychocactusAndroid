package com.psychocactusproject.engine.manager;

import com.psychocactusproject.engine.manager.GameEngine;

public class UpdateThread extends Thread {

    private final GameEngine gameEngine;
    private boolean updateRunning;
    private boolean updatePaused;

    private Object lock = new Object();

    public UpdateThread(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.updateRunning = true;
        this.updatePaused = false;
    }

    /*
    * SI QUIERO QUE LA CLASE UPDATE THREAD SEA COHERENTE DEBERÍA OBTENER LOS ESTADOS DE
    * CADA UNO DE LOS OBJETOS EN EL PROPIO HILO, QUE UPDATETHREAD TUVIESE LA ÚNICA REFERENCIA
    * A GAMELOGIC, Y QUE LOS OBJETOS MANIFESTASEN SU ESTADO PERO NO ACTUASEN PARA CAMBIAR
    * SU COMPORTAMIENTO. GAME LOGIC POR OTRA PARTE DEJARÍA DE SER SINGLETON
    * */

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

    /**
     * ATENCIÓN, AHORA YA NO SE RECURRE A ANDROID PARA PAUSAR EL JUEGO
     * DE HECHO, LA PARTIDA SE PIERDE CUANDO SE MINIMIZA LA APP Y SE VUELVE A SACAR
     * @return ATENCIÓN, NO HACE LO QUE DEBERÍA
     * @deprecated
     */
    public boolean isUpdatePaused() {
        return this.updatePaused;
    }
}
