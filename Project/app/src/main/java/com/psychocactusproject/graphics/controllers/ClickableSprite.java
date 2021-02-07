package com.psychocactusproject.graphics.controllers;

import android.graphics.Canvas;

import com.psychocactusproject.interaction.menu.ContextMenu;
import com.psychocactusproject.interaction.menu.MenuDisplay;
import com.psychocactusproject.interaction.scripts.Clickable;
import com.psychocactusproject.manager.engine.GameEngine;
import com.psychocactusproject.manager.engine.Hitbox;

public class ClickableSprite extends InanimateSprite implements MenuDisplay, Clickable {

    private ContextMenu spriteMenu;

    public ClickableSprite(GameEngine gameEngine, int drawableResource, String roleName, Hitbox[] hitboxes) {
        super(gameEngine, drawableResource, roleName, hitboxes);
    }

    public ClickableSprite(GameEngine gameEngine, String roleName) {
        super(gameEngine, roleName);
    }

    @Override
    public ContextMenu.MenuOption[] getMenuOptions() {
        return new ContextMenu.MenuOption[0];
    }

    @Override
    public void onOptionSelected(ContextMenu.MenuOption option) {

    }

    @Override
    public ContextMenu getMenu() {
        return this.spriteMenu;
    }

    @Override
    public boolean hasMenuOpen() {
        return this.spriteMenu.isAvailable();
    }

    @Override
    public void openMenu() {
        this.spriteMenu.openMenu();
    }

    @Override
    public void closeMenu() {
        this.spriteMenu.closeMenu();
    }

    @Override
    public void renderMenu(Canvas canvas) {

    }
}
