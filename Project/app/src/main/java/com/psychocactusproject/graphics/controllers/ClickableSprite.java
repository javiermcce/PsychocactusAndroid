package com.psychocactusproject.graphics.controllers;

import android.graphics.Canvas;

import com.psychocactusproject.engine.Point;
import com.psychocactusproject.interaction.menu.ContextMenu;
import com.psychocactusproject.interaction.menu.MenuDisplay;
import com.psychocactusproject.android.GameFragment;
import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.engine.Hitbox;

import java.util.HashMap;

public class ClickableSprite extends InanimateSprite implements MenuDisplay {

    private final ContextMenu spriteMenu;
    private final HashMap<String, Runnable> actions;
    private Hitbox[] hitboxes;
    private boolean available;

    public ClickableSprite(GameEngine gameEngine, String roleName, HashMap<String, Runnable> actions) {
        super(gameEngine, roleName);
        this.spriteMenu = new ContextMenu(gameEngine, this);
        this.actions = actions;
    }

    public ClickableSprite(GameEngine gameEngine, int drawableResource, String roleName,
                           Hitbox[] hitboxes, HashMap<String, Runnable> actions) {
        super(gameEngine, drawableResource, roleName);
        this.actions = actions;
        this.spriteMenu = new ContextMenu(gameEngine, this);
        this.hitboxes = hitboxes;
        this.available = true;
    }

    public ClickableSprite(GameEngine gameEngine, int drawableResource, int debugDrawableResource,
                           String roleName, Hitbox[] hitboxes, HashMap<String, Runnable> actions) {
        super(gameEngine, drawableResource, debugDrawableResource, roleName);
        this.actions = actions;
        this.spriteMenu = new ContextMenu(gameEngine, this);
        this.hitboxes = hitboxes;
        this.available = true;
    }

    public ClickableSprite(GameEngine gameEngine, int drawableResource, String roleName,
                           Hitbox[] hitboxes, HashMap<String, Runnable> actions, Point position) {
        this(gameEngine, drawableResource, roleName, hitboxes, actions);
        this.setPosition(position);
    }

    public ClickableSprite(GameEngine gameEngine, int drawableResource, int debugDrawableResource,
                           String roleName, Hitbox[] hitboxes, HashMap<String, Runnable> actions,
                           Point position) {
        this(gameEngine, drawableResource, debugDrawableResource, roleName, hitboxes, actions);
        this.setPosition(position);
    }

    @Override
    public void executeClick(int index) {
        this.spriteMenu.openMenu();
    }

    @Override
    public Hitbox[] getHitboxes() {
        if (this.isAvailable(0)) {
            return this.hitboxes;
        } else {
            return null;
        }
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
        return actions.keySet().toArray(new String[0]);
    }

    @Override
    public ContextMenu.MenuOption[] getMenuOptions() {
        ContextMenu.MenuOption[] options = new ContextMenu.MenuOption[this.getOptionNames().length];
        for (int i = 0; i < this.getOptionNames().length; i++) {
            options[i] = new ContextMenu.MenuOption(this.getOptionNames()[i]);
        }
        return options;
    }

    @Override
    public void onOptionSelected(String option) {
        for (String each : this.getOptionNames()) {
            if (each.equals(option)) {
                this.actions.get(option).run();
            }
        }
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
    public void updateMenu() {
        this.spriteMenu.onUpdate();
    }

    @Override
    public void renderMenu(Canvas canvas) {
        this.spriteMenu.draw(canvas);
    }
}
