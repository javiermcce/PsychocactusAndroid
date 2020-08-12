package com.snake.logic.stats;

public class Point {

    private final int xCoord;
    private final int yCoord;

    public Point(int xCoord, int yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public int getX() {
        return xCoord;
    }

    public int getY() {
        return yCoord;
    }
}
