package com.psychocactusproject.engine.screens;

import com.psychocactusproject.graphics.controllers.ClickableDirectSprite;
import com.psychocactusproject.graphics.interfaces.Drawable;
import com.psychocactusproject.input.TouchInputController.Touchable;

import java.util.List;

public class InitialScreen implements ScreenModel {

    private Drawable initialDrawable;
    private Touchable initialTouchable;

    private List<ClickableDirectSprite> initialEntities;

    public void setInitialEntities(List<ClickableDirectSprite> initialEntities) {
        this.initialEntities = initialEntities;
    }

    public Drawable definedDrawable() {
        return (canvas -> {
            //synchronized (GameEntity.entitiesLock) {
            //}
        });
    }
}
