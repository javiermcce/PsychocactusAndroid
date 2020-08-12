package com.snake.logic.map;

import com.snake.logic.entities.Reward;
import com.snake.logic.entities.Snake;

import java.util.LinkedList;

public class SnakeTable {

    private int xMapSize;
    private int yMapSize;
    private Snake snake;
    private LinkedList<Reward> rewards;

    public SnakeTable(int xSize, int ySize) {
        this.xMapSize = xSize;
        this.yMapSize = ySize;
        this.snake = new Snake(xSize, ySize);
    }
}
