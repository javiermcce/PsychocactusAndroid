package com.psychocactusproject.graphics.views;

import android.content.Context;

import com.psychocactusproject.engine.GameEntity;

import java.util.List;

public interface GameView {

    public void draw();

    public void setGameEntities(List<GameEntity> gameEntities);

    public int getWidth();

    public int getHeight();

    public int getPaddingLeft();

    public int getPaddingRight();

    public int getPaddingTop();

    public int getPaddingBottom();

    public Context getContext();

    public void postInvalidate();
}
