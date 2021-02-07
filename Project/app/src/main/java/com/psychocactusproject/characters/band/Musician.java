package com.psychocactusproject.characters.band;

import android.graphics.Canvas;

import com.psychocactusproject.interaction.menu.ContextMenu;
import com.psychocactusproject.interaction.menu.MenuDisplay;
import com.psychocactusproject.manager.engine.GameEngine;
import com.psychocactusproject.graphics.controllers.AnimationController;
import com.psychocactusproject.input.InputController;

public abstract class Musician extends AnimationController implements MenuDisplay {

    private GameEngine gameEngine;
    private ContextMenu musicianMenu;

    protected Musician(GameEngine gameEngine) {
        super(gameEngine);
        this.gameEngine = gameEngine;
        this.musicianMenu = new ContextMenu(gameEngine, this);
    }

    // Equivalente a Player según la guía

    @Override
    public void initialize() {

    }

    @Override
    public void update(long elapsedMillis, GameEngine gameEngine) {
        InputController inputController = gameEngine.getInputController();
        // aquí debería tener un gestor de movimiento que funcione como un script en un
        // hilo independiente, input controller tiene poco que ver en esto, pero lo voy a
        // mantener hasta implementar mi propio código
        // this.setPositionX();
        // this.setPositionX(this.getPositionX() + 1);
        // this.setPositionY(this.getPositionY() + 1);
    }

    @Override
    public void draw(Canvas canvas) {
        this.getMatrix().reset();
        this.getMatrix().postTranslate((float) this.getPositionX(), (float) this.getPositionY());
        canvas.drawBitmap(this.getAnimationImage(), this.getMatrix(), null);
    }

    @Override
    public ContextMenu.MenuOption[] getMenuOptions() {
        ContextMenu.MenuOption[] options = new ContextMenu.MenuOption[4];
        options[0] = new ContextMenu.MenuOption("Una prueba");
        options[1] = new ContextMenu.MenuOption("Mas pruebas");
        options[2] = new ContextMenu.MenuOption("JajajajJJJJAJjajajaj");
        options[3] = new ContextMenu.MenuOption("heeey");
        return options;
    }

    @Override
    public void onOptionSelected(ContextMenu.MenuOption option) {
        /*
        switch (option) {


        }
        */
    }
}
