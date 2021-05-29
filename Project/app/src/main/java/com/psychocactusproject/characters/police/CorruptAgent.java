package com.psychocactusproject.characters.police;

import android.graphics.Canvas;

import com.psychocactusproject.engine.manager.GameEngine;

public class CorruptAgent extends Police {

    public CorruptAgent(GameEngine gameEngine) {
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
    public void update(GameEngine gameEngine) {

    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public String getRoleName() {
        return "Violent Agent";
    }
}
