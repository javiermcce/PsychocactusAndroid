package com.psychocactusproject.engine.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.psychocactusproject.engine.manager.GameEngine;
import com.psychocactusproject.engine.manager.GameEntity;
import com.psychocactusproject.graphics.interfaces.Drawable;
import com.psychocactusproject.graphics.manager.MenuBitmapFlyweight;
import com.psychocactusproject.graphics.manager.MenuBitmapFlyweight.SliderFlyweight;
import com.psychocactusproject.input.Slidable;

public class Slider extends GameEntity implements Drawable, Slidable {

    private final String roleName;
    private final Bitmap sliderBar;
    private final Bitmap sliderPointer;
    private final Point position;
    private final Matrix sliderMatrix;
    private final Matrix pointerMatrix;
    private final Paint basicPaint;
    private final int yPointerOffset;
    // Estas posiciones son relativas al tamaño del slider, no del fondo
    private int pointerPositionX;
    private final int maxPointerPositionX;

    public Slider(String roleName, Point position) {
        this.roleName = roleName;
        SliderFlyweight sliderFlyweight = MenuBitmapFlyweight.getSliderFlyweightInstance();
        this.sliderBar = sliderFlyweight.getSliderBarBitmap();
        this.sliderPointer = sliderFlyweight.getSliderPointerBitmap();
        this.sliderMatrix = new Matrix();
        this.pointerMatrix = new Matrix();
        this.basicPaint = new Paint();
        this.position = position;
        this.yPointerOffset = 52;
        this.maxPointerPositionX = this.getSpriteWidth() - this.sliderPointer.getWidth();
        this.setPointerPositionByPercentage(0.8); // esto tendrá que ser sustituido por el valor ubicado en ajustes
        this.updateMatrix();
        this.updatePointerMatrix();
    }

    @Override
    public void initialize() {

    }

    @Override
    public void update(GameEngine gameEngine) {

    }

    @Override
    public String getRoleName() {
        return this.roleName;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.sliderBar, this.sliderMatrix, this.basicPaint);
        canvas.drawBitmap(this.sliderPointer, this.pointerMatrix, this.basicPaint);
    }

    @Override
    public int getPositionX() {
        return this.position.getX();
    }

    @Override
    public int getPositionY() {
        return this.position.getY();
    }

    @Override
    public Point getPosition() {
        return this.position;
    }

    public int getPointerPositionX() {
        return this.getPositionX() + this.pointerPositionX;
    }

    public int getPointerPositionY() {
        return this.getPositionY() + this.yPointerOffset;
    }

    public float getPointerPercentage() {
        return (float) this.pointerPositionX / this.maxPointerPositionX;
    }

    public void setPointerPositionByPercentage(double percentage) {
        this.pointerPositionX = (int) (percentage * this.maxPointerPositionX);
        this.updatePointerMatrix();
    }

    public void setPositionX(int x) {
        this.position.setX(x);
        this.updateMatrix();
    }

    public void setPositionY(int y) {
        this.position.setY(y);
        this.updateMatrix();
    }

    public void setPosition(int x, int y) {
        this.setPositionX(x);
        this.setPositionY(y);
    }

    public void setPointerPositionX(int x) {
        x -= (this.sliderPointer.getWidth() / 2);
        if (x < 0) {
            this.pointerPositionX = 0;
        } else if (x >= this.maxPointerPositionX) {
            this.pointerPositionX = maxPointerPositionX;
        } else {
            this.pointerPositionX = x;
        }
        this.updatePointerMatrix();
    }

    private void updateMatrix() {
        this.sliderMatrix.reset();
        this.sliderMatrix.postTranslate(this.getPositionX(), this.getPositionY());
        this.updatePointerMatrix();
    }

    private void updatePointerMatrix() {
        this.pointerMatrix.reset();
        this.pointerMatrix.postTranslate(this.getPointerPositionX(), this.getPointerPositionY());
    }

    @Override
    public int getSpriteWidth() {
        return this.sliderBar.getWidth();
    }

    @Override
    public int getSpriteHeight() {
        return this.sliderBar.getHeight();
    }

    @Override
    public void updatePointer(int x) {
        // La posición del puntero sería el resultado de restar la posición que se está deslizando
        // menos la posición del slider en la pantalla
        this.setPointerPositionX(x - this.getPositionX());
    }
}
