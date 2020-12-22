package com.psychocactusproject.graphics.manager;

import android.media.Image;

import com.psychocactusproject.engine.GameClock;
import com.psychocactusproject.engine.GameEntity;

public abstract class AnimatedEntity implements GameEntity {


    public abstract GameClock getClock();

    public abstract int getFrameNumber();

    public abstract int getCurrentAnimFrames();

    public abstract int getCurrentAnim();

    public abstract Image getFrameImage();

}
