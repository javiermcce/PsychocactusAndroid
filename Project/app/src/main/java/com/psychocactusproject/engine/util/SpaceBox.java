package com.psychocactusproject.engine.util;

import com.psychocactusproject.graphics.interfaces.Dimensions;

public class SpaceBox implements SquareInterface {

    // Referencia al padre en posesión de la hitbox
    private final Dimensions dimensionsFather;

    // Posición relativa respecto a la ubicación del padre, en porcentajes
    protected final int xUpLeft;
    protected final int yUpLeft;
    protected final int xDownRight;
    protected final int yDownRight;

    public SpaceBox(int xUpLeft, int yUpLeft, int xDownRight, int yDownRight, Dimensions father) {
        this.dimensionsFather = father;
        this.xUpLeft = xUpLeft;
        this.yUpLeft = yUpLeft;
        this.xDownRight = xDownRight;
        this.yDownRight = yDownRight;
    }

    @Override
    public int getUpLeftX(){
        return this.dimensionsFather.getPositionX() +
                this.dimensionsFather.getSpriteWidth() * this.xUpLeft / 100;
    }

    @Override
    public int getUpLeftY(){
        return this.dimensionsFather.getPositionY() +
                this.dimensionsFather.getSpriteHeight() * this.yUpLeft / 100;
    }

    @Override
    public int getDownRightX(){
        return this.dimensionsFather.getPositionX() +
                this.dimensionsFather.getSpriteWidth() * this.xDownRight / 100;
    }

    @Override
    public int getDownRightY(){
        return this.dimensionsFather.getPositionY() +
                this.dimensionsFather.getSpriteHeight() * this.yDownRight / 100;
    }

    @Override
    public Point getUpLeftPoint(){
        return new Point(getUpLeftX(), getUpLeftY());
    }

    @Override
    public Point getDownRightPoint(){
        return new Point(getDownRightX(), getDownRightY());
    }

    public int getXUpLeftPercentage() {
        return this.xUpLeft;
    }

    public int getYUpLeftPercentage() {
        return this.yUpLeft;
    }

    public int getXDownRightPercentage() {
        return this.xDownRight;
    }

    public int getYDownRightPercentage() {
        return this.yDownRight;
    }

    public static Point percentagesToRelativePoint(int xPercentage, int yPercentage, int elementWidth, int elementHeight) {
        int xCoord = elementWidth * xPercentage / 100;
        int yCoord = elementHeight * yPercentage / 100;
        return new Point(xCoord, yCoord);
    }

    public Dimensions getDimensionsFather() {
        return this.dimensionsFather;
    }
}
