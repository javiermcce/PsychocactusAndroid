package com.snake.logic.map;

import com.snake.logic.entities.Reward;
import com.snake.logic.entities.Snake;
import com.snake.logic.manager.Settings;
import com.snake.logic.stats.GameUpdater;

import java.util.LinkedList;

public class SnakeTable {

    // VARIABLES
    private int xMapSize;
    private int yMapSize;
    private Snake snake;
    private LinkedList<Reward> rewards;
    private GameUpdater gameUpdater;

    // SINGLETON
    private static SnakeTable instance;

    public static SnakeTable getInstance() {
        if (instance == null) {
            instance = new SnakeTable(
                    Settings.getInstance().getXMapSize(),
                    Settings.getInstance().getYMapSize()
            );
        }
        return instance;
    }

    // CONSTRUCTOR
    public SnakeTable(int xSize, int ySize) {
        this.xMapSize = xSize;
        this.yMapSize = ySize;
        this.snake = new Snake();
        this.gameUpdater = new GameUpdater();
    }

    // GET METHODS
    public int getXSize() {
        return xMapSize;
    }

    public int getYSize() {
        return yMapSize;
    }

    public GameUpdater getStats() {
        return this.gameUpdater;
    }

    public Snake getSnake() { return this.snake; }

    public GameUpdater getGameUpdater() {
        return this.gameUpdater;
    }

}
