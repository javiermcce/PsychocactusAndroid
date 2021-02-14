package com.psychocactusproject.characters.band;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.psychocactusproject.graphics.controllers.ClickableAnimation;
import com.psychocactusproject.graphics.views.SurfaceGameView;
import com.psychocactusproject.interaction.menu.ContextMenu;
import com.psychocactusproject.manager.android.GameFragment;
import com.psychocactusproject.manager.engine.GameEngine;
import com.psychocactusproject.input.InputController;

public abstract class Musician extends ClickableAnimation {

    private GameEngine gameEngine;
    private boolean available;

    protected Musician(GameEngine gameEngine, String[] optionNames) {
        super(gameEngine, optionNames);
        this.gameEngine = gameEngine;
        this.available = false;
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
    public boolean isAvailable() {
        return this.available;
    }

    @Override
    public void draw(Canvas canvas) {
        this.getMatrix().reset();
        this.getMatrix().postTranslate((float) this.getPositionX(), (float) this.getPositionY());
        Paint usedPaint = this.available ? SurfaceGameView.getColorFilter() : null;
        canvas.drawBitmap(this.getSpriteImage(), this.getMatrix(), usedPaint);
    }

    @Override
    public ContextMenu.MenuOption[] getMenuOptions() {
        ContextMenu.MenuOption[] options = new ContextMenu.MenuOption[4];
        for (int i = 0; i < this.getOptionNames().length; i++) {
            options[i] = new ContextMenu.MenuOption(this.getOptionNames()[i]);
        }
        return options;
    }

    protected void enableMusician() {
        this.available = true;
    }

    protected void disableMusician() {
        this.available = false;
    }
}
