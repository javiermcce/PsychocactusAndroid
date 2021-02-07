package com.psychocactusproject.graphics.controllers;

import android.graphics.Matrix;

import com.psychocactusproject.interaction.menu.MenuDisplay;
import com.psychocactusproject.interaction.scripts.Clickable;
import com.psychocactusproject.manager.engine.GameEngine;
import com.psychocactusproject.manager.engine.GameEntity;
import com.psychocactusproject.manager.engine.Point;
import com.psychocactusproject.manager.engine.Hitbox;

public abstract class AbstractSprite extends GameEntity implements Clickable {

    private int positionX;
    private int positionY;
    private final Matrix matrix = new Matrix();
    private Hitbox[][] hitboxes;

    public AbstractSprite(GameEngine gameEngine) {
        this.positionX = 0;
        this.positionY = 0;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public Point getPosition() {
        return new Point(this.positionX, this.positionY);
    }

    public abstract int getSpriteWidth();

    public abstract int getSpriteHeight();

    public abstract Hitbox[] getHitboxes();

    protected Hitbox[][] getAllHitboxes() {
        return this.hitboxes;
    }

    protected void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    protected void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    protected void setPosition(Point position) {
        this.positionX = position.getX();
        this.positionY = position.getY();
    }

    protected void setHitboxes(Hitbox[][] hitboxes) {
        this.hitboxes = hitboxes;
    }

    protected Matrix getMatrix() {
        return this.matrix;
    }
}
