package com.psychocactusproject.graphics.controllers;

import android.graphics.Canvas;

import com.psychocactusproject.interaction.menu.ContextMenu;
import com.psychocactusproject.interaction.menu.MenuDisplay;
import com.psychocactusproject.manager.android.GameFragment;
import com.psychocactusproject.manager.engine.GameEngine;
import com.psychocactusproject.manager.engine.Hitbox;

public abstract class ClickableAnimation extends AnimatedEntity implements MenuDisplay {

    private ContextMenu animationMenu;
    private final String[] optionNames;
    private final Hitbox[][] hitboxes;

    public ClickableAnimation(GameEngine gameEngine, String[] optionNames) {
        super(gameEngine);
        this.animationMenu = new ContextMenu(gameEngine, this);
        this.optionNames = optionNames;
        this.hitboxes = this.obtainAnimationResources().hitboxes;
    }

    @Override
    public Hitbox[] getHitboxes() {
        return this.getAllHitboxes()[this.getCurrentAction()];
    }

    protected Hitbox[][] getAllHitboxes() {
        return this.hitboxes;
    }

    @Override
    public void executeClick(int index) {
        this.openMenu();
        if (GameEngine.DEBUGGING) {
            GameFragment.setDebugText(this.getRoleName());
        }
    }

    @Override
    public String[] getOptionNames() {
        return optionNames;
    }

    @Override
    public boolean hasMenuOpen() {
        return this.animationMenu.isShown();
    }

    @Override
    public void openMenu() {
        this.animationMenu.openMenu();
    }

    @Override
    public void closeMenu() {
        this.animationMenu.closeMenu();
    }

    @Override
    public ContextMenu getMenu() {
        return this.animationMenu;
    }

    @Override
    public void renderMenu(Canvas canvas) {
        this.animationMenu.draw(canvas);
    }
}
