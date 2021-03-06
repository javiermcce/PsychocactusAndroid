package com.psychocactusproject.graphics.controllers;

import com.psychocactusproject.engine.Point;

public interface Dimensions {

    public int getPositionX();

    public int getPositionY();

    public Point getPosition();

    public abstract int getSpriteWidth();

    public abstract int getSpriteHeight();
}
