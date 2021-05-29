package com.psychocactusproject.graphics.interfaces;

import android.graphics.Canvas;

@FunctionalInterface
public interface DebugDrawable {

    public abstract void debugDraw(Canvas canvas);
}
