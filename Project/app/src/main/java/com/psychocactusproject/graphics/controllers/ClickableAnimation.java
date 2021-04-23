package com.psychocactusproject.graphics.controllers;

import android.graphics.Canvas;

import com.psychocactusproject.interaction.menu.ContextMenu;
import com.psychocactusproject.interaction.menu.MenuDisplay;
import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.engine.Hitbox;

public abstract class ClickableAnimation extends AnimatedEntity implements MenuDisplay {

    private final ContextMenu animationMenu;
    private final String[] optionNames;
    private final Hitbox[][] hitboxes;

    public ClickableAnimation(GameEngine gameEngine, String[] optionNames) {
        super(gameEngine);
        this.optionNames = optionNames;
        this.animationMenu = new ContextMenu(gameEngine, this);
        this.hitboxes = this.obtainAnimationResources().hitboxes;
    }

    @Override
    public Hitbox[] getHitboxes() {
        if (this.isAvailable(0)) {
            return this.getAllHitboxes()[this.getCurrentAction()];
        } else {
            return null;
        }
    }

    protected Hitbox[][] getAllHitboxes() {
        return this.hitboxes;
    }

    @Override
    public void executeClick(int index) {
        this.openMenu();
    }

    @Override
    public boolean isAvailable(int index) {
        return this.isSomeOptionAvailable() && this.isReadyForAction();
    }

    @Override
    public void enableClickable(int index) {
        this.animationMenu.enableClickable(index);
    }

    @Override
    public void disableClickable(int index) {
        this.animationMenu.disableClickable(index);
    }

    @Override
    public String[] getOptionNames() {
        return optionNames;
    }

    @Override
    public boolean isMenuOpen() {
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
    public void updateMenu() {
        this.animationMenu.onUpdate();
    }

    @Override
    public void renderMenu(Canvas canvas) {
        this.animationMenu.draw(canvas);
    }

    @Override
    public boolean isSomeOptionAvailable() {
        for (ContextMenu.MenuOption option : this.getMenu().getMenuOptions()) {
            if (option.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}
