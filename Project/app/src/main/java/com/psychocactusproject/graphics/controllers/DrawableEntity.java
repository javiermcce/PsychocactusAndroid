package com.psychocactusproject.graphics.controllers;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.engine.GameEntity;
import com.psychocactusproject.engine.Point;

public class DrawableEntity extends GameEntity implements Dimensions, Drawable, DebugDrawable {

    private final Drawable drawable;
    private final Drawable debugDrawable;
    private final String roleName;
    private Runnable initializeRun;
    private Runnable updateRun;
    private final Point upLeftCoord;
    private final Point downRightCoord;

    // Tengo que hacer que AbstractSprite herede de aquí, y le pase todos los parámetros,
    // incluidos los de run

    public DrawableEntity(Drawable drawCall, String roleName) {
        this(drawCall, null, roleName);
    }

    public DrawableEntity(Drawable drawCall, Drawable debugCall, String roleName) {
        this(drawCall, debugCall, roleName, new Point(-1, -1), new Point(-1, -1));
    }

    public DrawableEntity(Drawable drawCall, String roleName,
                          Point upLeftCoord, Point downRightCoord) {
        this(drawCall, null, roleName, upLeftCoord, downRightCoord);
    }

    public DrawableEntity(Drawable drawCall, Drawable debugCall, String roleName,
                          Point upLeftCoord, Point downRightCoord) {
        this.initializeRun = () -> {};
        this.updateRun = () -> {};
        this.drawable = drawCall;
        this.debugDrawable = debugCall;
        this.roleName = roleName;
        this.upLeftCoord = upLeftCoord;
        this.downRightCoord = downRightCoord;
    }

    public void setInitialize(Runnable initializer) {
        this.initializeRun = initializer;
    }

    public void setUpdate(Runnable updater) {
        this.updateRun = updater;
    }

    @Override
    public void draw(Canvas canvas) {
        if (this.drawable != null){
            this.drawable.draw(canvas);
        }
    }

    @Override
    public void debugDraw(Canvas canvas) {
        if (this.debugDrawable != null) {
            this.debugDrawable.draw(canvas);
        }
    }

    @Override
    public void initialize() {
        this.initializeRun.run();
    }

    @Override
    public void update(GameEngine gameEngine) {
        this.updateRun.run();
    }

    @Override
    public String getRoleName() {
        return this.roleName;
    }

    @Override
    public int getPositionX() {
        return this.upLeftCoord.getX();
    }

    @Override
    public int getPositionY() {
        return this.upLeftCoord.getY();
    }

    public int getDownLeftCoordX() {
        return this.downRightCoord.getX();
    }

    public int getDownLeftCoordY() {
        return this.downRightCoord.getY();
    }

    public void setPositionX(int position) {
        this.upLeftCoord.setX(position);
    }

    public void setPositionY(int position) {
        this.upLeftCoord.setY(position);
    }

    public void setDownLeftCoordX(int position) {
        this.downRightCoord.setX(position);
    }

    public void setDownLeftCoordY(int position) {
        this.downRightCoord.setY(position);
    }

    @Override
    public Point getPosition() {
        return this.upLeftCoord;
    }

    @Override
    public int getSpriteWidth() {
        return this.downRightCoord.getX() - this.upLeftCoord.getX();
    }

    @Override
    public int getSpriteHeight() {
        return this.downRightCoord.getY() - this.upLeftCoord.getY();
    }
}
