package com.psychocactusproject.characters.audience;

import android.graphics.Canvas;

import com.psychocactusproject.graphics.controllers.AnimatedEntity;
import com.psychocactusproject.engine.GameEngine;

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
    public void debugDraw(Canvas canvas) {

    }

    @Override
    public void initialize() {

    }

    @Override
    public void update(GameEngine gameEngine) {

    }

    @Override
    public String getRoleName() {
        return "Shotgun Man";
    }
}
