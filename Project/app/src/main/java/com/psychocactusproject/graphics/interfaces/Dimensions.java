package com.psychocactusproject.graphics.interfaces;

import com.psychocactusproject.engine.util.Point;

public interface Dimensions {

    public int getPositionX();

    public int getPositionY();

    public Point getPosition();

    public abstract int getSpriteWidth();

    public abstract int getSpriteHeight();
}
