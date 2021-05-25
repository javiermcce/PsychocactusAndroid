package com.psychocactusproject.engine.util;

import android.graphics.Canvas;

import com.psychocactusproject.engine.manager.GameEngine;
import com.psychocactusproject.engine.manager.GameEntity;
import com.psychocactusproject.graphics.interfaces.Drawable;
import com.psychocactusproject.input.Slidable;

public class Slider extends GameEntity implements Drawable, Slidable {

    private final String roleName;

    public Slider(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void update(GameEngine gameEngine) {

    }

    @Override
    public String getRoleName() {
        return this.roleName;
    }

    @Override
    public void draw(Canvas canvas) {

    }
}
