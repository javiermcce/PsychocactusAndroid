package com.psychocactusproject.characters.band;

import android.graphics.Canvas;

import com.psychocactusproject.R;
import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.graphics.manager.AnimationController;
import com.psychocactusproject.input.InputController;

public abstract class Musician extends AnimationController {

    protected Musician(GameEngine gameEngine) {
        super(gameEngine);
    }

    // Equivalente a Player según la guía

    @Override
    public void update(long elapsedMillis, GameEngine gameEngine) {
        InputController inputController = gameEngine.getInputController();
        // aquí debería tener un gestor de movimiento que funcione como un script en un
        // hilo independiente, input controller tiene poco que ver en esto, pero lo voy a
        // mantener hasta implementar mi propio código
        //this.setPositionX();
    }

    @Override
    public void initialize() {

    }

    @Override
    public void draw(Canvas canvas) {

    }
}
