package com.psychocactusproject.characters.police;

import com.psychocactusproject.graphics.controllers.AnimatedEntity;
import com.psychocactusproject.engine.GameEngine;

public abstract class Police extends AnimatedEntity {

    public enum PatrolPositions {
        AT_DOOR, AFTER_DOOR, THIRD_POSITION, FOURTH_POSITION,
        FIFTH_POSITION, SIXTH_POSITION, BEFORE_DOOR
    }

    public Police(GameEngine gameEngine) {
        super(gameEngine);
    }
}