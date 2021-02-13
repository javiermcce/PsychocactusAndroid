package com.psychocactusproject.graphics.controllers;

import android.graphics.Canvas;

import com.psychocactusproject.interaction.menu.ContextMenu;
import com.psychocactusproject.interaction.menu.MenuDisplay;
import com.psychocactusproject.manager.android.GameFragment;
import com.psychocactusproject.manager.engine.GameEngine;
import com.psychocactusproject.manager.engine.Point;

public abstract class ClickableAnimation extends AnimatedEntity implements MenuDisplay {

    private ContextMenu animationMenu;

    public ClickableAnimation(GameEngine gameEngine) {
        super(gameEngine);
        this.animationMenu = new ContextMenu(gameEngine, this);
    }

    @Override
    public void executeClick(int index) {
        this.openMenu();
        if (GameEngine.DEBUGGING) {
            GameFragment.setDebugText(this.getRoleName());
        }
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
