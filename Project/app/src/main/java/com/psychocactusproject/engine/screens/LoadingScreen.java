package com.psychocactusproject.engine.screens;

import com.psychocactusproject.engine.manager.GameEngine;
import com.psychocactusproject.engine.manager.GameEngine.SCENES;
import com.psychocactusproject.graphics.interfaces.Drawable;
import com.psychocactusproject.input.Slidable;
import com.psychocactusproject.input.Touchable;

import java.util.List;

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
    public SCENES getSceneId() {
        return GameEngine.SCENES.LOADING;
    }

    @Override
    public List<Slidable> getSlidables() {
        return null;
    }
}
