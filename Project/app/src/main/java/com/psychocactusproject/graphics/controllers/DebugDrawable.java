package com.psychocactusproject.graphics.controllers;

import android.graphics.Canvas;

@FunctionalInterface
public interface DebugDrawable {

    public abstract void debugDraw(Canvas canvas);
}
