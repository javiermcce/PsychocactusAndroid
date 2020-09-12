package com.snake.logic.stats;

import com.snake.logic.controls.Keyboard;
import com.snake.logic.entities.Snake;
import com.snake.logic.map.SnakeTable;

import javax.swing.Timer;

public class GameUpdater {

    // VARIABLES
    private String playerName;
    private int score;
    private int difficulty;
    private int currentSpeed;
    private Timer updater;
    private int frequency;
    private boolean lost;

    // CONSTRUCTOR
    public GameUpdater() {
        this.playerName = "AAA";
        this.score = 0;
        this.difficulty = 1;
        this.currentSpeed = 1;
        this.frequency = 1000;
        this.lost = false;
        this.startGame();
    }

    public void startGame() {
        this.updater = new Timer(frequency, (action) -> {
            Snake snake = SnakeTable.getInstance().getSnake();
            if (snake.willHeadTouchBody() || snake.willSnakeTouchObstacle()) {
                this.gameLost();
            } else {
                snake.move();
            }
        });
        this.updater.start();
    }

    private void gameLost() {
        this.updater.stop();
        this.lost = true;
    }

    // GET METHODS
    public String getPlayerName() {
        return this.playerName;
    }

    public int getScore() {
        return this.score;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public int getCurrentSpeed() {
        return this.currentSpeed;
    }

    public boolean isGameLost() {
        return this.lost;
    }
}
