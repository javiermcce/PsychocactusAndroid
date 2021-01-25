package com.psychocactusproject.graphics.manager;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.psychocactusproject.engine.GameEngine;

public abstract class SpriteController extends AbstractSprite {

    private final Bitmap bitmap;
    private final int imageWidth;
    private final int imageHeight;

    protected SpriteController(GameEngine gameEngine, int drawableResource) {
        super(gameEngine);
        Resources resources = gameEngine.getContext().getResources();
        Drawable drawableSprite = resources.getDrawable(drawableResource);
        this.imageWidth = (int) (drawableSprite.getIntrinsicWidth() * this.getPixelFactor());
        this.imageHeight = (int) (drawableSprite.getIntrinsicHeight() * this.getPixelFactor());
        this.bitmap = ((BitmapDrawable) drawableSprite).getBitmap();
    }

    @Override
    public void draw(Canvas canvas) {
        this.getMatrix().reset();
        this.getMatrix().postScale((float) this.getPixelFactor(), (float) this.getPixelFactor());
        this.getMatrix().postTranslate((float) this.getPositionX(), (float) this.getPositionY());
        canvas.drawBitmap(this.bitmap, this.getMatrix(), null);
    }

    protected int getImageWidth() {
        return this.imageWidth;
    }

    protected int getImageHeight() {
        return this.imageHeight;
    }
}
