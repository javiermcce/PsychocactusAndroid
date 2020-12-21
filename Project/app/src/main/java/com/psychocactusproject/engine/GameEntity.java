package com.psychocactusproject.engine;

import android.graphics.Canvas;

public abstract class GameEntity {

    public abstract void initialize();

    public abstract void update();

    public abstract void draw(Canvas canvas);


}
