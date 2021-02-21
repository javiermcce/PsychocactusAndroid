package com.psychocactusproject.characters.audience;

import android.graphics.Canvas;

import com.psychocactusproject.graphics.controllers.AnimatedEntity;
import com.psychocactusproject.manager.engine.GameEngine;

public class ShotgunMan extends AnimatedEntity {

    public ShotgunMan(GameEngine gameEngine) {
        super(gameEngine);
    }

    @Override
    protected AnimationResources obtainAnimationResources() {
        return null;
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void initialize() {

    }

    @Override
    public void update(long elapsedMillis, GameEngine gameEngine) {

    }

    @Override
    public String getRoleName() {
        return "Shotgun Man";
    }
}
