package com.psychocactusproject.engine.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class UserInterfaceFlyweight {

    private static UserInterfaceFlyweight instance;

    public static UserInterfaceFlyweight getInstance() {
        if (instance == null) {
            instance = new UserInterfaceFlyweight();
        }
        return instance;
    }

    private final Paint insideButtonOptionPaint = new Paint();
    private final Paint outsideButtonOptionPaint = new Paint();

    public UserInterfaceFlyweight() {
        // Se ajustan los colores del botón (interior y exterior)
        this.insideButtonOptionPaint.setColor(Color.argb(255, 174, 182, 191));
        this.outsideButtonOptionPaint.setColor(Color.argb(255, 126, 133, 143));
    }

    public void drawButton(String optionText, SpaceBox optionButton, Paint textPaint, Canvas canvas) {
        int computedWidth = optionButton.getDimensionsFather().getSpriteWidth();
        int computedHeight = optionButton.getDimensionsFather().getSpriteHeight();
        // Posición de esquina arriba a la izquierda
        Point relativeUpLeft = SpaceBox.percentagesToRelativePoint(
                optionButton.getXUpLeftPercentage(),
                optionButton.getYUpLeftPercentage(),
                computedWidth, computedHeight
        );
        // Posición de esquina abajo a la derecha
        Point relativeDownRight = SpaceBox.percentagesToRelativePoint(
                optionButton.getXDownRightPercentage(),
                optionButton.getYDownRightPercentage(),
                computedWidth, computedHeight
        );
        // Son dibujados los cuadros interior y exterior del botón
        canvas.drawRect(
                new Rect(relativeUpLeft.getX(), relativeUpLeft.getY(),
                        relativeDownRight.getX(), relativeDownRight.getY()),
                this.outsideButtonOptionPaint
        );
        canvas.drawRect(
                new Rect(relativeUpLeft.getX() + 5 , relativeUpLeft.getY() + 5,
                        relativeDownRight.getX() - 5, relativeDownRight.getY() - 5),
                this.insideButtonOptionPaint
        );
        // Dibujado del texto del botón
        TextUtil.drawCenteredLine(canvas, relativeUpLeft, relativeDownRight, optionText, textPaint);
    }
}
