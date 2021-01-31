package com.psychocactusproject.graphics.controllers;

import android.media.Image;

import com.psychocactusproject.manager.engine.GameClock;
import com.psychocactusproject.manager.engine.GameEntity;

public abstract class AnimatedEntity extends GameEntity {


    public abstract GameClock getClock();

    public abstract int getFrameNumber();

    public abstract int getCurrentAnimFrames();

    public abstract int getCurrentAnim();

    public abstract Image getFrameImage();

}
