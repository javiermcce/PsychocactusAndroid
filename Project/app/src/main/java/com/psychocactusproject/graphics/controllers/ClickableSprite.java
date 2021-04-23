package com.psychocactusproject.graphics.controllers;

import android.graphics.Canvas;

import com.psychocactusproject.engine.Point;
import com.psychocactusproject.interaction.menu.ContextMenu;
import com.psychocactusproject.interaction.menu.MenuDisplay;
import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.engine.Hitbox;

import java.util.HashMap;

public class ClickableSprite extends InanimateSprite implements MenuDisplay {

    private final ContextMenu spriteMenu;
    private final HashMap<String, Runnable> actions;
    private Hitbox[] hitboxes;
    private final Checker readyForAction;

    public ClickableSprite(GameEngine gameEngine, String roleName, HashMap<String, Runnable> actions) {
        super(gameEngine, roleName);
        this.spriteMenu = new ContextMenu(gameEngine, this);
        this.actions = actions;
        this.readyForAction = () -> { return true; };
    }

    public ClickableSprite(GameEngine gameEngine, int drawableResource, String roleName,
                           Hitbox[] hitboxes, HashMap<String, Runnable> actions) {
        super(gameEngine, drawableResource, roleName);
        this.actions = actions;
        this.spriteMenu = new ContextMenu(gameEngine, this);
        this.hitboxes = hitboxes;
        this.readyForAction = () -> { return true; };
    }

    public ClickableSprite(GameEngine gameEngine, int drawableResource, int debugDrawableResource,
                           String roleName, Hitbox[] hitboxes, HashMap<String, Runnable> actions,
                           Checker readyCheck) {
        super(gameEngine, drawableResource, debugDrawableResource, roleName);
        this.actions = actions;
        this.spriteMenu = new ContextMenu(gameEngine, this);
        this.hitboxes = hitboxes;
        this.readyForAction = readyCheck;
    }

    public ClickableSprite(GameEngine gameEngine, int drawableResource, String roleName,
                           Hitbox[] hitboxes, HashMap<String, Runnable> actions, Point position) {
        this(gameEngine, drawableResource, roleName, hitboxes, actions);
        this.setPosition(position);
    }

    public ClickableSprite(GameEngine gameEngine, int drawableResource, int debugDrawableResource,
                           String roleName, Hitbox[] hitboxes, HashMap<String, Runnable> actions,
                           Checker readyCheck, Point position) {
        this(gameEngine, drawableResource, debugDrawableResource, roleName, hitboxes, actions,
                readyCheck);
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
        return this.isSomeOptionAvailable() && this.isReadyForAction();
    }

    @Override
    public void enableClickable(int index) {
        this.spriteMenu.enableClickable(index);
    }

    @Override
    public void disableClickable(int index) {
        this.spriteMenu.disableClickable(index);
    }

    @Override
    public String[] getOptionNames() {
        return actions.keySet().toArray(new String[0]);
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
    public boolean isMenuOpen() {
        return this.spriteMenu.isShown();
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

    @Override
    public boolean isSomeOptionAvailable() {
        for (ContextMenu.MenuOption option : this.getMenu().getMenuOptions()) {
            if (option.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isReadyForAction() {
        return this.readyForAction.check();
    }
}
