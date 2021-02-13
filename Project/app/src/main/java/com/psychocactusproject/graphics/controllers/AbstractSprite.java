package com.psychocactusproject.graphics.controllers;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.psychocactusproject.manager.engine.GameEngine;
import com.psychocactusproject.manager.engine.GameEntity;
import com.psychocactusproject.manager.engine.Hitbox;
import com.psychocactusproject.manager.engine.Point;

public abstract class AbstractSprite extends GameEntity implements Dimensions {

    private int positionX;
    private int positionY;
    private final Matrix matrix = new Matrix();
    private Hitbox[][] hitboxes;

    public AbstractSprite(GameEngine gameEngine) {
        this.positionX = 0;
        this.positionY = 0;
    }

    @Override
    public int getPositionX() {
        return positionX;
    }

    @Override
    public int getPositionY() {
        return positionY;
    }

    @Override
    public Point getPosition() {
        return new Point(this.positionX, this.positionY);
    }

    public abstract Hitbox[] getHitboxes();

    protected Hitbox[][] getAllHitboxes() {
        return this.hitboxes;
    }

    // Imagen que muestra el estado actual del sprite
    protected abstract Bitmap getSpriteImage();

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
