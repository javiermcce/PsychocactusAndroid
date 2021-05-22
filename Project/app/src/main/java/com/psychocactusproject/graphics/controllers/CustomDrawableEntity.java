package com.psychocactusproject.graphics.controllers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.psychocactusproject.engine.manager.GameEngine;
import com.psychocactusproject.engine.manager.GameEntity;
import com.psychocactusproject.engine.util.Point;
import com.psychocactusproject.graphics.interfaces.DebugDrawable;
import com.psychocactusproject.graphics.interfaces.Dimensions;
import com.psychocactusproject.graphics.interfaces.Drawable;
import com.psychocactusproject.graphics.views.SurfaceGameView;

public class CustomDrawableEntity extends GameEntity implements Dimensions, Drawable, DebugDrawable {

    private final Drawable drawable;
    private final Drawable debugDrawable;
    private final String roleName;
    private Runnable initializeRun;
    private Runnable updateRun;
    private final Point upLeftCoord;
    private final Point downRightCoord;
    private Bitmap customBitmap;
    private Canvas customCanvas;
    private Paint customPaint;
    private Matrix customMatrix;

    // Tengo que hacer que AbstractSprite herede de aquí, y le pase todos los parámetros,
    // incluidos los de run

    public CustomDrawableEntity(Drawable drawCall, String roleName) {
        this(drawCall, null, roleName);
    }

    public CustomDrawableEntity(Drawable drawCall, Drawable debugCall, String roleName) {
        this(drawCall, debugCall, roleName, new Point(-1, -1), new Point(-1, -1));
    }

    public CustomDrawableEntity(Drawable drawCall, String roleName,
                                Point upLeftCoord, Point downRightCoord) {
        this(drawCall, null, roleName, upLeftCoord, downRightCoord);
    }

    public CustomDrawableEntity(Drawable drawCall, Drawable debugCall, String roleName,
                                Point upLeftCoord, Point downRightCoord) {
        this.initializeRun = () -> {};
        this.updateRun = () -> {};
        this.drawable = drawCall;
        this.debugDrawable = debugCall;
        this.roleName = roleName;
        this.upLeftCoord = upLeftCoord;
        this.downRightCoord = downRightCoord;
        this.createCustomBitmap();
    }

    public CustomDrawableEntity(Drawable drawCall, String roleName,
                                Point upLeftCoord, int width, int height) {
        this(drawCall, null, roleName,
                upLeftCoord, new Point(upLeftCoord.getX() + width, upLeftCoord.getY() + height));
    }

    public CustomDrawableEntity(Drawable drawCall, Drawable debugCall, String roleName,
                                Point upLeftCoord, int width, int height) {
        this(drawCall, debugCall, roleName,
                upLeftCoord, new Point(upLeftCoord.getX() + width, upLeftCoord.getY() + height));
    }

    @Override
    public void draw(Canvas canvas) {
        if (this.drawable != null) {
            // Se ejecuta la orden de dibujado sobre el propio canvas de esta clase
            this.drawable.draw(this.customCanvas);
            // Se imprime el resultado sobre la imagen global
            this.drawOnView(canvas);
        }
    }

    @Override
    public void debugDraw(Canvas canvas) {
        if (this.debugDrawable != null) {
            // Se ejecuta la orden de dibujado sobre el propio canvas de esta clase
            this.debugDrawable.draw(this.customCanvas);
            // Se imprime el resultado sobre la imagen global
            this.drawOnView(canvas);
        }
    }

    @Override
    public void initialize() {
        this.initializeRun.run();
    }

    @Override
    public void update(GameEngine gameEngine) {
        this.updateRun.run();
    }

    @Override
    public String getRoleName() {
        return this.roleName;
    }

    @Override
    public int getPositionX() {
        return this.upLeftCoord.getX();
    }

    @Override
    public int getPositionY() {
        return this.upLeftCoord.getY();
    }

    @Override
    public Point getPosition() {
        return this.upLeftCoord;
    }

    @Override
    public int getSpriteWidth() {
        return this.downRightCoord.getX() - this.upLeftCoord.getX();
    }

    @Override
    public int getSpriteHeight() {
        return this.downRightCoord.getY() - this.upLeftCoord.getY();
    }

    public int getDownLeftCoordX() {
        return this.downRightCoord.getX();
    }

    public int getDownLeftCoordY() {
        return this.downRightCoord.getY();
    }

    public void setPositionX(int position) {
        this.upLeftCoord.setX(position);
    }

    public void setPositionY(int position) {
        this.upLeftCoord.setY(position);
    }

    public void setDownLeftCoordX(int position) {
        this.downRightCoord.setX(position);
    }

    public void setDownLeftCoordY(int position) {
        this.downRightCoord.setY(position);
    }


    public void setInitialize(Runnable initializer) {
        this.initializeRun = initializer;
    }

    public void setUpdate(Runnable updater) {
        this.updateRun = updater;
    }

    private void createCustomBitmap() {
        this.customBitmap = Bitmap.createBitmap(
                this.getSpriteWidth(), this.getSpriteHeight(),
                Bitmap.Config.ARGB_8888
        );
        this.customCanvas = new Canvas();
        this.customCanvas.setBitmap(this.customBitmap);
        this.customPaint = new Paint();
        this.customMatrix = new Matrix();
    }

    public void drawOnView(Canvas canvas) {
        // Se posiciona en la imagen global y se dibuja
        this.customMatrix.reset();
        this.customMatrix.postTranslate((float) this.getPositionX(), (float) this.getPositionY());
        canvas.drawBitmap(this.customBitmap, this.customMatrix, this.customPaint);
        // Se limpia la imagen
        SurfaceGameView.clearCanvas(this.customCanvas);
    }
}
