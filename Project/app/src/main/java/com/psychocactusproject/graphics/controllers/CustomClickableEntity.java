package com.psychocactusproject.graphics.controllers;

import com.psychocactusproject.engine.util.Hitbox;
import com.psychocactusproject.engine.util.Point;
import com.psychocactusproject.graphics.interfaces.Drawable;
import com.psychocactusproject.interaction.scripts.Clickable;

public class CustomClickableEntity extends CustomDrawableEntity implements Clickable  {

    private boolean available;
    private Runnable customAction;
    private Hitbox[] hitboxes;

    public CustomClickableEntity(Drawable drawCall, Drawable debugCall, String roleName,
                                 Point upLeftCoord, int width, int height,
                                 Runnable customAction) {
        super(drawCall, debugCall, roleName, upLeftCoord, width, height);
        this.customAction = customAction;
        this.hitboxes = new Hitbox[] {new Hitbox(this, 0)};
    }

    public CustomClickableEntity(Drawable drawCall, Drawable debugCall, String roleName,
                                 Point upLeftCoord, int width, int height,
                                 Runnable customAction, Hitbox[] hitboxes) {
        super(drawCall, debugCall, roleName, upLeftCoord, width, height);
        this.customAction = customAction;
        this.hitboxes = hitboxes;
    }

    public CustomClickableEntity(Drawable drawCall, String roleName,
                                 Point upLeftCoord, int width, int height,
                                 Runnable customAction) {
        this(drawCall, null, roleName, upLeftCoord, width, height, customAction);
    }

    public CustomClickableEntity(Drawable drawCall, String roleName,
                                 Point upLeftCoord, int width, int height,
                                 Runnable customAction, Hitbox[] hitboxes) {
        this(drawCall, null, roleName, upLeftCoord, width, height, customAction, hitboxes);
    }

    @Override
    public void executeClick(int index) {
        this.customAction.run();
    }

    @Override
    public Hitbox[] getHitboxes() {
        return this.hitboxes;
    }

    @Override
    public boolean isAvailable(int index) {
        return this.available;
    }

    @Override
    public void enableClickable(int index) {
        if (index == 0) {
            this.available = true;
        } else {
            throw new IllegalArgumentException("La clase ClickableDirectSprite solo permite " +
                    "activarse con índice igual a 0");
        }
    }

    @Override
    public void disableClickable(int index) {
        if (index == 0) {
            this.available = false;
        } else {
            throw new IllegalArgumentException("La clase ClickableDirectSprite solo permite " +
                    "activarse con índice igual a 0");
        }
    }
}
