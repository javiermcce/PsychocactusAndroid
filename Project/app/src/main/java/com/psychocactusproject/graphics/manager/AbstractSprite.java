package com.psychocactusproject.graphics.manager;

import android.graphics.Matrix;

import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.engine.GameEntity;
import com.psychocactusproject.engine.Point;

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
