package com.psychocactusproject.graphics.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.psychocactusproject.graphics.controllers.AbstractSprite;
import com.psychocactusproject.manager.engine.GameEntity;

import java.util.LinkedList;
import java.util.List;

public class DrawableGameView extends View implements GameView {

    private List<GameEntity> gameEntities;
    private List<AbstractSprite> gameSprites;

    public DrawableGameView(Context context) {
        super(context);
    }

    public DrawableGameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DrawableGameView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
    }

    @Override
    public void draw() {
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        synchronized (GameEntity.entitiesLock) {
            for (int i = 0; i < this.gameSprites.size(); i++) {
                this.gameSprites.get(i).draw(canvas);
            }
        }
    }

    @Override
    public void setGameEntities(List<GameEntity> gameEntities, List<AbstractSprite> gameSprites) {
        this.gameEntities = gameEntities;
        this.gameSprites = gameSprites;
    }
}
