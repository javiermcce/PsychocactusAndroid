package com.psychocactusproject.engine.util;

public class Square implements SquareInterface {

    // Posición absoluta
    protected final int xUpLeft;
    protected final int yUpLeft;
    protected final int xDownRight;
    protected final int yDownRight;

    public Square(int xUpLeft, int yUpLeft, int xDownRight, int yDownRight) {
        this.xUpLeft = xUpLeft;
        this.yUpLeft = yUpLeft;
        this.xDownRight = xDownRight;
        this.yDownRight = yDownRight;
    }

    @Override
    public int getUpLeftX() {
        return this.xUpLeft;
    }

    @Override
    public int getUpLeftY() {
        return this.yUpLeft;
    }

    @Override
    public int getDownRightX() {
        return this.xDownRight;
    }

    @Override
    public int getDownRightY() {
        return this.yDownRight;
    }

    @Override
    public Point getUpLeftPoint() {
        return new Point(getUpLeftX(), getUpLeftY());
    }

    @Override
    public Point getDownRightPoint() {
        return new Point(getDownRightX(), getDownRightY());
    }
}
