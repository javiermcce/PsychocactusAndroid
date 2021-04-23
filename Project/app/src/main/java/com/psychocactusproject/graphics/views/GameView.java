package com.psychocactusproject.graphics.views;

import android.content.Context;

import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.engine.GameEntity;
import com.psychocactusproject.graphics.controllers.DebugDrawable;
import com.psychocactusproject.graphics.controllers.Drawable;

import java.util.List;

public interface GameView {

    public void draw();

    public void setGameEntities(List<GameEntity> gameEntities, List<Drawable> gameSprites,
                                List<DebugDrawable> debugSprites);

    public void setGameEngine(GameEngine gameEngine);

    public int getWidth();

    public int getHeight();

    public int getPaddingLeft();

    public int getPaddingRight();

    public int getPaddingTop();

    public int getPaddingBottom();

    public Context getContext();

    public void postInvalidate();
}
