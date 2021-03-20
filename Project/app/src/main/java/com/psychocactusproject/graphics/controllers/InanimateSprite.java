package com.psychocactusproject.graphics.controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.psychocactusproject.engine.GameEngine;

public class InanimateSprite extends AbstractSprite {

    public static final int NOT_SPECIFIED = -1;
    private Bitmap bitmap;
    private Bitmap debugBitmap;
    private int imageWidth;
    private int imageHeight;
    private final String roleName;
    private BitmapFactory.Options options;

    public InanimateSprite(GameEngine gameEngine, String roleName) {
        super(gameEngine);
        this.roleName = roleName;
    }

    public InanimateSprite(GameEngine gameEngine, int drawableResource, String roleName) {
        this(gameEngine, roleName);
        this.options = new BitmapFactory.Options();
        this.options.inScaled = false;
        if (drawableResource != InanimateSprite.NOT_SPECIFIED) {
            this.bitmap = BitmapFactory.decodeResource(
                    gameEngine.getContext().getResources(),
                    drawableResource,
                    options);
            this.imageWidth = bitmap.getWidth();
            this.imageHeight = bitmap.getHeight();
        }
    }

    public InanimateSprite(GameEngine gameEngine, int drawableResource, int debugDrawableResource,
                           String roleName) {
        this(gameEngine, drawableResource, roleName);
        if (debugDrawableResource != InanimateSprite.NOT_SPECIFIED) {
            this.debugBitmap = BitmapFactory.decodeResource(
                    gameEngine.getContext().getResources(),
                    debugDrawableResource,
                    options);
            if (this.imageWidth == 0 && this.imageHeight == 0) {
                this.imageWidth = debugBitmap.getWidth();
                this.imageHeight = debugBitmap.getHeight();
            }
        }
    }

    @Override
    public void initialize() {

    }

    @Override
    public void update(GameEngine gameEngine) {

    }

    @Override
    public void draw(Canvas canvas) {
        if (this.bitmap != null) {
            this.getMatrix().reset();
            this.getMatrix().postTranslate((float) this.getPositionX(), (float) this.getPositionY());
            canvas.drawBitmap(this.bitmap, this.getMatrix(), null);
        }
    }

    @Override
    public void debugDraw(Canvas canvas) {
        if (this.debugBitmap != null) {
            this.getMatrix().reset();
            this.getMatrix().postTranslate((float) this.getPositionX(), (float) this.getPositionY());
            canvas.drawBitmap(this.debugBitmap, this.getMatrix(), null);
        }
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

    @Override
    public Bitmap getSpriteImage() {
        return this.bitmap;
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
