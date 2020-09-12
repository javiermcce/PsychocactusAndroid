package com.snake.logic.manager;

import com.snake.logic.map.SnakeTable;

public class Settings {

    // VARIABLES
    private int xMapSize;
    private int yMapSize;

    // SINGLETON
    private static Settings instance;

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public static void initializeSettings (int xMapSize, int yMapSize)
            throws IllegalStateException, IllegalArgumentException {
        if (instance == null) { throw new IllegalStateException(); }
        if (xMapSize > 80 || yMapSize > 40) { throw new IllegalArgumentException(); }
        instance = new Settings(xMapSize, yMapSize);
    }

    // CONSTRUCTOR
    private Settings () {
        this.xMapSize = 60;
        this.yMapSize = 30;
    }

    private Settings (int xMapSize, int yMapSize) {
        this.xMapSize = xMapSize;
        this.yMapSize = yMapSize;
    }

    // GET METHODS
    public int getXMapSize () {
        return this.xMapSize;
    }

    public int getYMapSize () {
        return this.yMapSize;
    }
}
