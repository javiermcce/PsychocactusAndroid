package com.snake.graphics.scenes;

import com.snake.graphics.render.Scene;

public class SceneUpdater {

    // VARIABLES
    private Scene scene;

    // SINGLETON
    private static SceneUpdater sceneUpdater;

    public static SceneUpdater getInstance() {
        if (sceneUpdater == null) {
            sceneUpdater = new SceneUpdater();
        }
        return sceneUpdater;
    }

    // CONSTRUCTOR
    private SceneUpdater() {
        this.scene = new GameScene();
    }

    // GET METHODS
    public Scene getScene() {
        return this.scene;
    }

    /*
    // LISTA DE ESTADOS
    public static enum Estados {
        JUEGO, PAUSA, INICIO, DEBUG
    }


    // VARIABLES
    private Estados estado;
    private boolean partidaAcabada;
    private int turnosRestantes;

    // SINGLETON
    private static StateUpdater instance = null;
    public static StateUpdater getInstance() {
        if (instance == null) instance = new StateUpdater();
        return instance;
    }

    // CONSTRUCTOR
    private StateUpdater() {
        this.estado = Estados.DEBUG;
        this.partidaAcabada = false;
        this.turnosRestantes = 0;
    }

    // GET METHODS
    public boolean enPausa() {
        return this.estado == Estados.PAUSA;
    }
    public boolean enJuego() {
        return this.estado == Estados.JUEGO;
    }
    public boolean enInicio() {
        return this.estado == Estados.INICIO;
    }
    public boolean quedanTurnos() {
        return this.turnosRestantes > 0;
    }
    public int getTurnosRestantes() {
        return this.turnosRestantes;
    }
    public Estados getState() {
        return this.estado;
    }
    // SET METHODS
    public void setEsperandoTurno() {
        this.turnosRestantes = 3;
    }

    public void cambiarEstado(Estados estado) {
        switch(estado) {
            case JUEGO:
                this.entrarEnJuego();
                break;
            case PAUSA:
                this.entrarEnPausa();
                break;
            default:
                throw new IllegalStateException(
                        "No es posible entrar a otro estado.");
        }
    }
    private void entrarEnJuego() {
        Estados anterior = this.estado;
        this.estado = Estados.JUEGO;

    }
    private void entrarEnPausa() {
        Estados anterior = this.estado;
        this.estado = Estados.PAUSA;
    }
    public void alternarPausa() {

    }

    public void finalizarPartida() {
        this.partidaAcabada = true;
    }
    */
}
