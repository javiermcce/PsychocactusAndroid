package com.psychocactusproject.graphics.controllers;

import android.graphics.Canvas;

public class CustomEntity implements CustomDrawable {

    CustomDrawable drawable;

    public CustomEntity(CustomDrawable drawCall) {
        this.drawable = drawCall;
    }

    @Override
    public void draw(Canvas canvas) {
        drawable.draw(canvas);
    }
}
