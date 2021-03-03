package com.psychocactusproject.manager.engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.psychocactusproject.interaction.scripts.Clickable;

public class Hitbox {

    // Posición relativa respecto a la ubicación del padre, en porcentajes
    private final int xUpLeft;
    private final int yUpLeft;
    private final int xDownRight;
    private final int yDownRight;
    // Referencia al padre en posesión de la hitbox
    private final Clickable father;
    // Índice de la acción a realizar
    private final int index;
    // Referencia a paint para el dibujado de hitboxes
    private static Paint hitboxPaint = new Paint();

    public Hitbox(int xPercUpLeft, int yPercUpLeft,
                  int xPercDownRight, int yPercDownRight,
                  Clickable father, int index){
        if((xPercUpLeft < 0 || xPercUpLeft > 100)
                || yPercUpLeft < 0 || yPercUpLeft > 100
                || xPercDownRight < 0 || xPercDownRight > 100
                || yPercDownRight < 0 || yPercDownRight > 100){
            throw new IllegalArgumentException("Percentage given at Hitbox"
                    + "constructor is out of bounds");
        }
        this.xUpLeft = xPercUpLeft;
        this.yUpLeft = yPercUpLeft;
        this.xDownRight = xPercDownRight;
        this.yDownRight = yPercDownRight;
        this.father = father;
        this.index = index;
    }

    public Hitbox(Clickable father, int index) {
        this.xUpLeft = 0;
        this.yUpLeft = 0;
        this.xDownRight = 100;
        this.yDownRight = 100;
        this.father = father;
        this.index = index;
    }

    public int getUpLeftX(){
        return this.father.getPositionX() +
                this.father.getSpriteWidth() * xUpLeft / 100;
    }

    public int getUpLeftY(){
        return this.father.getPositionY() +
                this.father.getSpriteHeight() * yUpLeft / 100;
    }

    public int getDownRightX(){
        return this.father.getPositionX() +
                this.father.getSpriteWidth() * xDownRight / 100;
    }

    public int getDownRightY(){
        return this.father.getPositionY() +
                this.father.getSpriteHeight() * yDownRight / 100;
    }

    public Point getUpLeftPoint(){
        return new Point(getUpLeftX(), getUpLeftY());
    }

    public Point getDownRightPoint(){
        return new Point(getDownRightX(), getDownRightY());
    }

    public Clickable getFather() {
        return this.father;
    }

    public int getIndex() {
        return this.index;
    }

    public static void drawHitboxes(Hitbox[] hitboxes, Canvas canvas) {
        hitboxPaint.setColor(Color.RED);
        hitboxPaint.setStyle(Paint.Style.STROKE);
        hitboxPaint.setStrokeWidth(2);
        if (hitboxes != null) {
            for (Hitbox hitbox : hitboxes) {
                if (hitbox != null) {
                    canvas.drawRect(hitbox.getUpLeftX(), hitbox.getUpLeftY(),
                            hitbox.getDownRightX(),
                            hitbox.getDownRightY(),
                            hitboxPaint
                    );
                }
            }
        }
    }

    public static void drawHitbox(Hitbox hitbox, Canvas canvas) {
        Paint hitboxPaint = new Paint();
        hitboxPaint.setColor(Color.RED);
        hitboxPaint.setStyle(Paint.Style.STROKE);
        hitboxPaint.setStrokeWidth(2);
        if (hitbox != null) {
            canvas.drawRect(hitbox.getUpLeftX(), hitbox.getUpLeftY(),
                    hitbox.getDownRightX(),
                    hitbox.getDownRightY(),
                    hitboxPaint
            );
        }
    }
}
