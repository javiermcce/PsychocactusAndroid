package com.snake.logic.entities;

public class Reward {

    public enum RewardType {
        FRUIT
    }

    private int xCoord;
    private int yCoord;
    private RewardType myType;

    public Reward(int xCoord, int yCoord, RewardType type) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.myType = type;
    }
}
