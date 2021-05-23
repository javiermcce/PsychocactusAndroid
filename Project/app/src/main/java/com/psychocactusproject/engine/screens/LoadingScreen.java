package com.psychocactusproject.engine.screens;

import com.psychocactusproject.engine.manager.GameEngine;
import com.psychocactusproject.engine.manager.GameEngine.SCENES;
import com.psychocactusproject.graphics.interfaces.Drawable;
import com.psychocactusproject.input.Touchable;

public class LoadingScreen implements Scene {

    @Override
    public Drawable definedDrawable() {
        return null;
    }

    @Override
    public Touchable definedTouchable() {
        return null;
    }

    @Override
    public void onSceneChange(SCENES oldScene) {

    }

    @Override
    public int getSceneId() {
        return GameEngine.SCENES.LOADING.ordinal();
    }
}
