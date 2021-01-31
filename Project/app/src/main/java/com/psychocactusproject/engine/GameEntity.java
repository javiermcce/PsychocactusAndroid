package com.psychocactusproject.engine;

import android.graphics.Canvas;

public abstract class GameEntity {

    public GameEntity() {
    }

    public abstract void initialize();

    public abstract void update(long elapsedMillis, GameEngine gameEngine);

    public abstract void draw(Canvas canvas);

    public abstract String getRoleName();

}
