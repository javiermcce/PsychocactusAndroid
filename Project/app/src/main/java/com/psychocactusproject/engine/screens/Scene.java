package com.psychocactusproject.engine.screens;

import com.psychocactusproject.engine.manager.GameEngine;
import com.psychocactusproject.engine.manager.GameEngine.SCENES;
import com.psychocactusproject.graphics.interfaces.Drawable;
import com.psychocactusproject.input.Slidable;
import com.psychocactusproject.input.Touchable;

import java.util.List;

public interface Scene {

    Drawable definedDrawable();

    Touchable definedTouchable();

    void onSceneChange(GameEngine.SCENES oldScene);

    SCENES getSceneId();

    List<Slidable> getSlidables();

    void clearScreen();
}
