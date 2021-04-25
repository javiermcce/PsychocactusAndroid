package com.psychocactusproject.graphics.controllers;

import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.engine.Hitbox;
import com.psychocactusproject.engine.Point;
import com.psychocactusproject.interaction.scripts.Clickable;

public class ClickableDirectSprite extends InanimateSprite implements Clickable {

    private boolean available;
    private Runnable directAction;
    private Hitbox[] hitboxes;

    public ClickableDirectSprite(GameEngine gameEngine, int drawableResource, String roleName, Runnable directAction) {
        super(gameEngine, drawableResource, roleName);
        this.directAction = directAction;
        this.available = true;
        this.hitboxes = new Hitbox[1];
        this.hitboxes[0] = new Hitbox(this, 0);
    }

    public ClickableDirectSprite(GameEngine gameEngine, int drawableResource, String roleName, Runnable directAction, Point position) {
        this(gameEngine, drawableResource, roleName, directAction);
        this.setPosition(position);
    }

    @Override
    public void executeClick(int index) {
        this.directAction.run();
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
        if (index == 0) {
            this.available = true;
        } else {
            throw new IllegalArgumentException("La clase ClickableDirectSprite solo permite " +
                    "activarse con índice igual a 0");
        }
    }

    @Override
    public void disableClickable(int index) {
        if (index == 0) {
            this.available = false;
        } else {
            throw new IllegalArgumentException("La clase ClickableDirectSprite solo permite " +
                    "activarse con índice igual a 0");
        }
    }
}
