package com.psychocactusproject.graphics.manager;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.psychocactusproject.engine.GameEngine;

public class Sprite extends AbstractSprite {

    private Bitmap bitmap;
    private final int imageWidth;
    private final int imageHeight;
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

    public void resizeBitmap(int sizeX, int sizeY) {
        this.bitmap = Bitmap.createScaledBitmap(this.bitmap,
                sizeX, sizeY, false);
    }
}
