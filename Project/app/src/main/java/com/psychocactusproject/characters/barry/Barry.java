package com.psychocactusproject.characters.barry;

import android.graphics.Canvas;

import com.psychocactusproject.graphics.controllers.AnimatedEntity;
import com.psychocactusproject.engine.GameEngine;

public class Barry extends AnimatedEntity {

    private int busyTurns;
    private static final int CHARGE_BATTERY_COST = 1;
    private static final int FIX_AMP_COST = 1;
    private static final int FIX_BROKEN_AMP_COST = 4;
    private static final int FACING_VIOLENT_POLICE_COST = 5;
    private static final int RESTORING_STOLEN_ITEM_COST = 3;
    private static final int FIX_WALL_COST = 3;
    private static final int REPLACE_CABLE_COST = 3;
    private static final int FIX_HOLE_COST = 2;
    private static final int TAKE_OUT_FANS_COST = 3;
    private static final int HAPPY_HOUR_COST = 2;

    public Barry(GameEngine gameEngine) {
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
    public void debugDraw(Canvas canvas) {

    }

    @Override
    public String getRoleName() {
        return "Barry";
    }
}
