package com.psychocactusproject.graphics.controllers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.engine.GameEntity;
import com.psychocactusproject.engine.Point;

public abstract class AbstractSprite extends GameEntity implements Dimensions {

    private int positionX;
    private int positionY;
    private final Matrix matrix = new Matrix();

    public AbstractSprite(GameEngine gameEngine) {
        this.positionX = 0;
        this.positionY = 0;
    }

    public abstract void draw(Canvas canvas);

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

    protected Matrix getMatrix() {
        return this.matrix;
    }
}
