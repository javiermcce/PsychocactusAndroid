package com.psychocactusproject.interaction.scripts;

import com.psychocactusproject.graphics.controllers.Dimensions;
import com.psychocactusproject.manager.engine.Hitbox;

public interface Clickable extends Dimensions {

    public void executeClick(int index);

    public Hitbox[] getHitboxes();

}
