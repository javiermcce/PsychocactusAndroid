package com.psychocactusproject.graphics.controllers;

import android.graphics.Matrix;

import com.psychocactusproject.manager.engine.GameEngine;
import com.psychocactusproject.manager.engine.GameEntity;
import com.psychocactusproject.manager.engine.Point;

public abstract class AbstractSprite extends GameEntity {

    private int positionX;
    private int positionY;
    private final Matrix matrix = new Matrix();

    public AbstractSprite(GameEngine gameEngine) {
        this.positionX = 0;
        this.positionY = 0;
    }

    protected int getPositionX() {
        return positionX;
    }

    protected int getPositionY() {
        return positionY;
    }

    protected Point getPosition() {
        return new Point(this.positionX, this.positionY);
    }

    public abstract int getSpriteWidth();

    public abstract int getSpriteHeight();

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

    protected Matrix getMatrix() {
        return this.matrix;
    }
}
