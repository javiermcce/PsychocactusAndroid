package com.psychocactusproject.manager.engine;

import androidx.annotation.Nullable;

public class Point {

    private int x;
    private int y;

    public Point() {
        this.x = 0;
        this.y = 0;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Point) {
            Point point = (Point) object;
            return this.getX() == point.getX() && this.getY() == point.getY();
        } else {
            return false;
        }
    }
}
