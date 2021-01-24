package com.psychocactusproject.engine;

import android.graphics.Canvas;

public abstract class GameEntity {

    private final String characterName;

    public GameEntity() {
        this.characterName = this.obtainCharacterName();
    }

    public abstract void initialize();

    public abstract void update(long elapsedMillis, GameEngine gameEngine);

    public abstract void draw(Canvas canvas);

    public abstract String obtainCharacterName();

    public String getCharacterName() {
        return this.characterName;
    }

}
