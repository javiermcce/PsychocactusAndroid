package com.psychocactusproject.graphics.controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.psychocactusproject.manager.engine.GameEngine;
import com.psychocactusproject.manager.engine.GameEntity;

public class Sprite extends AbstractSprite {

    private Bitmap bitmap;
    private int imageWidth;
    private int imageHeight;
    private final String roleName;

    public Sprite(GameEngine gameEngine, int drawableResource, String roleName) {
        super(gameEngine);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        this.bitmap = BitmapFactory.decodeResource(
                gameEngine.getContext().getResources(),
                drawableResource,
                options);
        this.imageWidth = bitmap.getWidth();
        this.imageHeight = bitmap.getHeight();
        this.roleName = roleName;
    }

    public Sprite(GameEngine gameEngine, String roleName) {
        super(gameEngine);
        this.roleName = roleName;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void update(long elapsedMillis, GameEngine gameEngine) {

    }

    @Override
    public void draw(Canvas canvas) {
        this.getMatrix().reset();
        this.getMatrix().postTranslate((float) this.getPositionX(), (float) this.getPositionY());
        canvas.drawBitmap(this.bitmap, this.getMatrix(), null);
    }

    @Override
    public String getRoleName() {
        return this.roleName;
    }

    @Override
    public int getSpriteWidth() {
        return this.imageWidth;
    }

    @Override
    public int getSpriteHeight() {
        return this.imageHeight;
    }

    protected void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        if (bitmap != null) {
            this.imageWidth = bitmap.getWidth();
            this.imageHeight = bitmap.getHeight();
        } else {
            this.imageWidth = 0;
            this.imageHeight = 0;
        }
    }

    public void resizeBitmap(int sizeX, int sizeY) {
        if (sizeX <= 0 || sizeY <= 0) {
            throw new IllegalStateException("Se ha intentado reescalar una imagen con tamaños " +
                    "ilegales: Width = " + sizeX + " , Height = " + sizeY);
        }
        this.bitmap = Bitmap.createScaledBitmap(this.bitmap,
                sizeX, sizeY, false);
        this.imageWidth = bitmap.getWidth();
        this.imageHeight = bitmap.getHeight();
    }
}