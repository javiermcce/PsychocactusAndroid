package com.psychocactusproject.engine;

import android.graphics.Canvas;

public interface GameEntity {

    public abstract void initialize();

    public abstract void update(long elapsedMillis, GameEngine gameEngine);

    public abstract void draw(Canvas canvas);


}
