package com.psychocactusproject.engine.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.psychocactusproject.interaction.scripts.Clickable;

public class Hitbox extends SpaceBox {

    // Referencia al padre en posesión de la hitbox
    private final Clickable father;
    // Índice de la acción a realizar
    private final int index;
    // Referencia a paint para el dibujado de hitboxes
    private static final Paint hitboxPaint = new Paint();

    public Hitbox(int xPercUpLeft, int yPercUpLeft,
                  int xPercDownRight, int yPercDownRight,
                  Clickable father, int index){
        super(xPercUpLeft, yPercUpLeft, xPercDownRight, yPercDownRight, father);
        if((xPercUpLeft < 0 || xPercUpLeft > 100)
                || yPercUpLeft < 0 || yPercUpLeft > 100
                || xPercDownRight < 0 || xPercDownRight > 100
                || yPercDownRight < 0 || yPercDownRight > 100){
            throw new IllegalArgumentException("Percentage given at Hitbox"
                    + "constructor is out of bounds");
        }
        this.father = father;
        this.index = index;
    }

    public Hitbox(Clickable father, int index) {
        super(0, 0, 100, 100, father);
        this.father = father;
        this.index = index;
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
