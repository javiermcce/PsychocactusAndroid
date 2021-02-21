package com.psychocactusproject.characters.police;

import android.graphics.Canvas;

import com.psychocactusproject.manager.engine.GameEngine;

public class ViolentAgent extends Police {

    public ViolentAgent(GameEngine gameEngine) {
        super(gameEngine);
    }

    @Override
    protected AnimationResources obtainAnimationResources() {
        return null;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void update(long elapsedMillis, GameEngine gameEngine) {

    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public String getRoleName() {
        return "Violent Agent";
    }
}
