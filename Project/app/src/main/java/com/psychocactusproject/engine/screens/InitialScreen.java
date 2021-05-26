package com.psychocactusproject.engine.screens;

import com.psychocactusproject.engine.manager.GameEngine;
import com.psychocactusproject.engine.manager.GameEngine.SCENES;
import com.psychocactusproject.graphics.controllers.ClickableDirectSprite;
import com.psychocactusproject.graphics.interfaces.Drawable;
import com.psychocactusproject.input.Slidable;
import com.psychocactusproject.input.Touchable;

import java.util.List;

public class InitialScreen implements Scene {

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

    @Override
    public Touchable definedTouchable() {
        return null;
    }

    @Override
    public void onSceneChange(SCENES oldScene) {

    }

    @Override
    public SCENES getSceneId() {
        return GameEngine.SCENES.INITIAL_SCREEN;
    }

    @Override
    public List<Slidable> getSlidables() {
        return null;
    }
}
