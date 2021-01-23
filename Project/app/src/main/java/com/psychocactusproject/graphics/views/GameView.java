package com.psychocactusproject.graphics.views;

import android.content.Context;

import com.psychocactusproject.engine.GameEntity;

import java.util.List;

public interface GameView {

    public void draw();

    public void setGameEntities(List<GameEntity> gameEntities);

    int getWidth();

    int getHeight();

    int getPaddingLeft();

    int getPaddingRight();

    int getPaddingTop();

    int getPaddingBottom();

    Context getContext();

    void postInvalidate();
}
