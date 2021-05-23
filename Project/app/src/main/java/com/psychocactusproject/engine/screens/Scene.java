package com.psychocactusproject.engine.screens;

import com.psychocactusproject.engine.manager.GameEngine;
import com.psychocactusproject.graphics.interfaces.Drawable;
import com.psychocactusproject.input.Touchable;

public interface Scene /* extends Clickable */ {

    Drawable definedDrawable();

    Touchable definedTouchable();

    void onSceneChange(GameEngine.SCENES oldScene);

    int getSceneId();
}
