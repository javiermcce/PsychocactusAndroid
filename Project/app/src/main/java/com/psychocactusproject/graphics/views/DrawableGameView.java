package com.psychocactusproject.graphics.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.psychocactusproject.engine.GameEntity;

import java.util.List;

public class DrawableGameView extends View implements GameView {

    private List<GameEntity> gameEntities;

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
        synchronized (this.gameEntities) {
            for (int i = 0; i < this.gameEntities.size(); i++) {
                gameEntities.get(i).draw(canvas);
            }
        }
    }


    @Override
    public void setGameEntities(List<GameEntity> gameEntities) {
        this.gameEntities = gameEntities;
    }
}
