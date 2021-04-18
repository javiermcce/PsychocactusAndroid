package com.psychocactusproject.graphics.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.engine.GameEntity;
import com.psychocactusproject.graphics.controllers.DebugDrawable;
import com.psychocactusproject.graphics.controllers.Drawable;

import java.util.List;

/**
 * Precisamente ahora que he parametrizado los bloques de instrucciones que corren para dibujar
 * e interpretar el juego, podría fácilmente refactorizar los métodos que compartan ambas clases
 * y volver a PODER hacer uso de esta, dando sentido a la arquitectura propuesta por el libro...
 * Al menos dando la opción de poder crear un nuevo layout que utilice esta clase en lugar de
 * la otra, y siendo este esfuerzo meramente teórico (no le daría uso, más allá de probar que es
 * posible trabajar así)
 *
 */

public class DrawableGameView extends View implements GameView {

    private List<GameEntity> gameEntities;
    private List<Drawable> gameSprites;
    private GameEngine gameEngine;

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
    public void setGameEntities(List<GameEntity> gameEntities, List<Drawable> gameSprites, List<DebugDrawable> debugSprites) {
        this.gameEntities = gameEntities;
        this.gameSprites = gameSprites;
    }

    @Override
    public void setGameEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }
}
