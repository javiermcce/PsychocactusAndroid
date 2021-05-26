package com.psychocactusproject.input;

import android.view.View;

public abstract class InputController implements View.OnTouchListener {

    public double horizontalFactor;
    public double verticalFactor;

    public void start() {

    }

    public void stop() {

    }

    public void pause() {

    }

    public void resume() {

    }

    public void update() {

    }

    protected abstract Slidable searchSlidables();
}
