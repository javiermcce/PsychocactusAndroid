package com.psychocactusproject.graphics.controllers;

import android.graphics.Canvas;

import com.psychocactusproject.interaction.menu.ContextMenu;
import com.psychocactusproject.interaction.menu.MenuDisplay;
import com.psychocactusproject.manager.android.GameFragment;
import com.psychocactusproject.manager.engine.GameEngine;
import com.psychocactusproject.manager.engine.Hitbox;

import java.util.HashMap;

public class ClickableSprite extends InanimateSprite implements MenuDisplay {

    private ContextMenu spriteMenu;
    private String[] optionNames;
    private Hitbox[] hitboxes;
    private boolean available;

    public ClickableSprite(GameEngine gameEngine, int drawableResource, String roleName, Hitbox[] hitboxes, HashMap<String, Runnable> actions) {
        super(gameEngine, drawableResource, roleName);
        this.optionNames = optionNames;
        this.hitboxes = hitboxes;
        this.available = false;
    }

    public ClickableSprite(GameEngine gameEngine, String roleName, HashMap<String, Runnable> actions) {
        super(gameEngine, roleName);
        this.optionNames = optionNames;
    }

    @Override
    public void executeClick(int index) {
        this.spriteMenu.openMenu();
        if (GameEngine.DEBUGGING) {
            GameFragment.setDebugText(this.getRoleName());
        }
    }

    @Override
    public Hitbox[] getHitboxes() {
        return this.hitboxes;
    }

    @Override
    public boolean isAvailable(int index) {
        return this.available;
    }

    @Override
    public void enableClickable(int index) {
        this.available = true;
    }

    @Override
    public void disableClickable(int index) {
        this.available = false;
    }

    @Override
    public String[] getOptionNames() {
        return this.optionNames;
    }

    @Override
    public ContextMenu.MenuOption[] getMenuOptions() {
        ContextMenu.MenuOption[] options = new ContextMenu.MenuOption[4];
        for (int i = 0; i < this.getOptionNames().length; i++) {
            options[i] = new ContextMenu.MenuOption(this.getOptionNames()[i]);
        }
        return options;
    }

    @Override
    public void onOptionSelected(String option) {

    }

    @Override
    public ContextMenu getMenu() {
        return this.spriteMenu;
    }

    @Override
    public boolean hasMenuOpen() {
        return this.spriteMenu.isMenuAvailable();
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
        this.spriteMenu.draw(canvas);
    }
}
