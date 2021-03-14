package com.psychocactusproject.characters.police;

import android.graphics.Canvas;

import com.psychocactusproject.engine.GameEngine;

public class PartyAgent extends Police {

    public PartyAgent(GameEngine gameEngine) {
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
