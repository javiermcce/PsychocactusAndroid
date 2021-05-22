package com.psychocactusproject.engine.screens;

import com.psychocactusproject.graphics.interfaces.Drawable;
import com.psychocactusproject.input.Touchable;

public interface Scene {

    Drawable definedDrawable();

    Touchable definedTouchable();
}
