package com.psychocactusproject.interaction.scripts;

import com.psychocactusproject.graphics.controllers.Dimensions;
import com.psychocactusproject.engine.Hitbox;

public interface Clickable extends Dimensions {

    public void executeClick(int index);

    public Hitbox[] getHitboxes();

    public boolean isAvailable(int index);

    public void enableClickable(int index);

    public void disableClickable(int index);
}
