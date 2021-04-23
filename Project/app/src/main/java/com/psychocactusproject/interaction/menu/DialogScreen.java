package com.psychocactusproject.interaction.menu;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.engine.GameEngine.SCENES;
import com.psychocactusproject.engine.Hitbox;
import com.psychocactusproject.engine.Point;
import com.psychocactusproject.graphics.controllers.InanimateSprite;
import com.psychocactusproject.interaction.scripts.Clickable;

import java.util.ArrayList;
import java.util.List;

import static com.psychocactusproject.interaction.menu.DialogScreen.DIALOG_TYPE.ALERT;
import static com.psychocactusproject.interaction.menu.DialogScreen.DIALOG_TYPE.CONFIRMATION;

public class DialogScreen extends InanimateSprite implements Clickable {

    private static final int CONFIRMATION_ACCEPT = 0;
    private static final int CONFIRMATION_CANCEL = 1;
    private static final int ALERT_ACCEPT = 2;

    private final MenuFlyweight pieces;
    private final Canvas dialogCanvas;
    private final Matrix dialogMatrix;
    private final Paint dialogPaint;
    private final Paint textPaint;
    private final Hitbox[] dialogHitboxes;

    public enum DIALOG_TYPE {
        CONFIRMATION, ALERT
    }

    private final DIALOG_TYPE type;
    private final String message;
    private final String details;
    private final Runnable action;

    public DialogScreen(GameEngine gameEngine, String message, String details) {
        this(gameEngine, message, details, null);
    }

    public DialogScreen(GameEngine gameEngine, String message, String details, Runnable action) {
        super(gameEngine, "Screen dialog");
        //
        this.message = message;
        this.details = details;
        this.action = action;
        //
        int optionWidthPerc = 30;
        int optionHeightPerc = 20;
        int leftOptionX = 12;
        int middleOptionX = 35;
        int rightOptionX = 57;
        int optionY = 67;
        if (action == null) {
            this.type = ALERT;
            this.dialogHitboxes = new Hitbox[1];
            this.dialogHitboxes[0] = new Hitbox(middleOptionX, optionY,
                    middleOptionX + optionWidthPerc, optionY + optionHeightPerc,
                    this, ALERT_ACCEPT);
        } else {
            this.type = CONFIRMATION;
            this.dialogHitboxes = new Hitbox[2];
            this.dialogHitboxes[0] = new Hitbox(leftOptionX, optionY,
                    leftOptionX + optionWidthPerc, optionY + optionHeightPerc,
                    this, CONFIRMATION_ACCEPT);
            this.dialogHitboxes[1] = new Hitbox(rightOptionX, optionY,
                    rightOptionX + optionWidthPerc, optionY + optionHeightPerc,
                    this, CONFIRMATION_CANCEL);
        }
        //
        this.pieces = MenuFlyweight.getInstance(gameEngine, MenuFlyweight.DIALOG_MENU_TYPE);
        this.dialogCanvas = new Canvas();
        this.dialogMatrix = new Matrix();
        this.dialogPaint = new Paint();
        this.textPaint = new Paint();
        //
        this.dialogPaint.setColor(Color.argb(200, 150, 150, 150));
        this.textPaint.setColor(Color.WHITE);
        this.textPaint.setTypeface(GameEngine.getInstance().getTypeface());
        //
        this.setBitmap(this.buildDialogScreen());
        int xOffset = (GameEngine.RESOLUTION_X - this.getSpriteWidth()) / 2;
        int yOffset = (GameEngine.RESOLUTION_Y - this.getSpriteHeight()) / 2;
        this.setPosition(new Point(xOffset, yOffset));
    }

    public DIALOG_TYPE getType() {
        return type;
    }

    public String getMessage() {
        return this.message;
    }

    public String getDetails() {
        return this.details;
    }

    public Runnable getAction() {
        return this.action;
    }

    @Override
    public void executeClick(int index) {
        GameEngine engine = GameEngine.getInstance();
        switch (index) {
            case CONFIRMATION_ACCEPT:
                // Ejecuta la acción que fue seleccionada antes de mostrar la pantalla de diálogo
                this.getAction().run();
                // Vuelve al modo de juego
                engine.switchToScene(SCENES.GAME);
                break;
            case CONFIRMATION_CANCEL:
            case ALERT_ACCEPT:
                // Vuelve al modo de juego
                engine.switchToScene(SCENES.GAME);
                break;
            default:
                throw new IllegalStateException("No existe la opción indicada en el menú de dialogo.");
        }
    }

    @Override
    public Hitbox[] getHitboxes() {
        return this.dialogHitboxes;
    }

    /**
     * PROVISIONAL: Método pensado para devolver la información sobre si una opción puede ser
     * seleccionada o no, pero aquí no tiene utilidad, ya que solo podemos elegir dos opciones,
     * siendo estas la de confirmación o negación, o ninguna; con lo que nunca hay una opción
     * no disponible
     *
     * @param index asdasd
     * @return asdasd
     */
    @Override
    public boolean isAvailable(int index) {
        return true;
    }

    @Override
    public void enableClickable(int index) {
        throw new IllegalStateException("Se ha intentado activar una opción de la pantalla de " +
                "diálogo, pero esto no está permitido activar ni desactivar las opciones");
    }

    @Override
    public void disableClickable(int index) {
        throw new IllegalStateException("Se ha intentado desactivar una opción de la pantalla de " +
                "diálogo, pero esto no está permitido activar ni desactivar las opciones");
    }

    private Bitmap buildDialogScreen() {
        //
        int piezasHorizontal = 20;
        int piezasVertical = 5;
        //
        int computedWidth = this.pieces.getTopLeftPiece().getWidth() +
                this.pieces.getTopRightPiece().getWidth() +
                (this.pieces.getCenterPiece().getWidth() * piezasHorizontal);
        int computedHeight = this.pieces.getTopLeftPiece().getHeight() +
                this.pieces.getBottomLeftPiece().getHeight() +
                (this.pieces.getCenterPiece().getHeight() * piezasVertical);
        // Pinta un fondo algo más opaco para facilitar la lectura del texto
        Bitmap dialogBitmap = Bitmap.createBitmap(
                computedWidth, computedHeight, Bitmap.Config.ARGB_8888);
        this.dialogCanvas.setBitmap(dialogBitmap);
        int border = 18;
        Paint backgroundColor = new Paint();
        backgroundColor.setColor(Color.argb(100, 171, 171, 171));
        this.dialogCanvas.drawRect(
                new Rect(border, border, computedWidth - border, computedHeight - border),
                backgroundColor
        );
        // Dibujado de la ventana de diálogo
        this.dialogMatrix.reset();
        Bitmap nextPiece;
        // Arriba izquierda
        nextPiece = this.pieces.getTopLeftPiece();
        this.dialogCanvas.drawBitmap(nextPiece, this.dialogMatrix, this.dialogPaint);
        this.dialogMatrix.postTranslate(nextPiece.getWidth(), 0);

        // Arriba centro
        nextPiece = this.pieces.getTopPiece();
        for (int i = 0; i < piezasHorizontal; i++) {
            this.dialogCanvas.drawBitmap(nextPiece, this.dialogMatrix, this.dialogPaint);
            this.dialogMatrix.postTranslate(nextPiece.getWidth(), 0);
        }
        // Arriba derecha
        nextPiece = this.pieces.getTopRightPiece();
        this.dialogCanvas.drawBitmap(nextPiece, this.dialogMatrix, this.dialogPaint);
        this.dialogMatrix.reset();
        this.dialogMatrix.postTranslate(0, nextPiece.getHeight());
        // Espacio para text
        for (int i = 0; i < piezasVertical; i++) {
            // Centro izquierda
            nextPiece = this.pieces.getLeftPiece();
            this.dialogCanvas.drawBitmap(nextPiece, this.dialogMatrix, this.dialogPaint);
            this.dialogMatrix.postTranslate(nextPiece.getWidth(), 0);
            // Cálculo de la posición de las opciones
            float[] values = new float[9];
            this.dialogMatrix.getValues(values);
            // int xCoord = (int) values[Matrix.MTRANS_X];
            int yCoord = (int) values[Matrix.MTRANS_Y];
            // Centro
            nextPiece = this.pieces.getCenterPiece();
            for (int j = 0; j < piezasHorizontal; j++) {
                this.dialogCanvas.drawBitmap(nextPiece, this.dialogMatrix, this.dialogPaint);
                this.dialogMatrix.postTranslate(nextPiece.getWidth(), 0);
            }
            // Centro derecha
            nextPiece = this.pieces.getRightPiece();
            this.dialogCanvas.drawBitmap(nextPiece, this.dialogMatrix, this.dialogPaint);
            this.dialogMatrix.reset();
            this.dialogMatrix.postTranslate(0, yCoord + nextPiece.getHeight());
        }
        // Arriba izquierda
        nextPiece = this.pieces.getBottomLeftPiece();
        this.dialogCanvas.drawBitmap(nextPiece, this.dialogMatrix, this.dialogPaint);
        this.dialogMatrix.postTranslate(nextPiece.getWidth(), 0);
        // Arriba centro
        nextPiece = this.pieces.getBottomPiece();
        for (int i = 0; i < piezasHorizontal; i++) {
            this.dialogCanvas.drawBitmap(nextPiece, this.dialogMatrix, this.dialogPaint);
            this.dialogMatrix.postTranslate(nextPiece.getWidth(), 0);
        }
        // Arriba derecha
        nextPiece = this.pieces.getBottomRightPiece();
        this.dialogCanvas.drawBitmap(nextPiece, this.dialogMatrix, this.dialogPaint);
        this.dialogMatrix.reset();

        // INSERTAR TEXTO DEL DIALOGO AQUÍ
        String[] messageWords = this.message.split(" ");
        int wordIndex = 0;
        int lines = 0;
        while (wordIndex < messageWords.length) {
            int wordsPerLine = 3;
            StringBuilder line = new StringBuilder();
            for (int internalIndex = wordIndex; internalIndex < wordIndex + wordsPerLine && internalIndex < messageWords.length; internalIndex++) {
                line.append(messageWords[internalIndex]).append(" ");
            }
            Point startPoint = new Point(0, (lines * 80) + 80);
            Point endPoint = new Point(computedWidth, ((lines + 1) * 80) + 80);
            this.drawCenteredText(startPoint, endPoint, line.toString(), 70);
            wordIndex += wordsPerLine;
            lines++;
        }
        //
        Paint optionButtonInsidePaint = new Paint();
        optionButtonInsidePaint.setColor(Color.argb(255, 174, 182, 191));
        Paint optionButtonOutsidePaint = new Paint();
        optionButtonOutsidePaint.setColor(Color.argb(255, 126, 133, 143));
        for (Hitbox optionButton : this.getHitboxes()) {
            Point relativeUpLeft = Hitbox.percentagesToRelativePoint(
                    optionButton.getXUpLeftPercentage(),
                    optionButton.getYUpLeftPercentage(),
                    computedWidth, computedHeight
            );
            Point relativeDownRight = Hitbox.percentagesToRelativePoint(
                    optionButton.getXDownRightPercentage(),
                    optionButton.getYDownRightPercentage(),
                    computedWidth, computedHeight
            );
            this.dialogCanvas.drawRect(
                    new Rect(relativeUpLeft.getX(), relativeUpLeft.getY(),
                            relativeDownRight.getX(), relativeDownRight.getY()),
                    optionButtonOutsidePaint
            );
            this.dialogCanvas.drawRect(
                    new Rect(relativeUpLeft.getX() + 5 , relativeUpLeft.getY() + 5,
                            relativeDownRight.getX() - 5, relativeDownRight.getY() - 5),
                    optionButtonInsidePaint
            );
            // Point upLeft, Point downRight, String text, Canvas canvas
            String optionText;
            switch (optionButton.getIndex()) {
                case CONFIRMATION_ACCEPT:
                    optionText = "Accept";
                    break;
                case CONFIRMATION_CANCEL:
                    optionText = "Cancel";
                    break;
                case ALERT_ACCEPT:
                    optionText = "Okay";
                    break;
                default:
                    throw new IllegalStateException("El texto para la opción de pantalla de " +
                            "diálogo encontrada no existe");
            }
            this.drawCenteredText(relativeUpLeft, relativeDownRight, optionText, 60);
        }


        // AQUI
        this.getMessage();
        this.getDetails();
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        //this.dialogCanvas.drawText();


        return dialogBitmap;
    }

    private void drawCenteredText(Point upLeft, Point downRight, String text, int fontSize) {
        this.textPaint.setTextSize(fontSize);
        Rect bounds = new Rect();
        this.textPaint.getTextBounds(text, 0, text.length(), bounds);
        int availableWidth = downRight.getX() - upLeft.getX();
        int textWidth = bounds.right - bounds.left;
        int textHeight = bounds.bottom - bounds.top;
        //
        if (textWidth > availableWidth) {
            throw new IllegalArgumentException("No hay suficiente espacio para el texto " +
                    "que se desea dibujar.");
        }
        // Tamaño de los bordes / 2, + /*coordenada del botón*/ offsetX
        int textPositionX = ((availableWidth - textWidth) / 2) + upLeft.getX() - 5;
        // Media de altura de las posiciones dadas como margen + /*tamaño de letra / 2*/ offsetY
        int textPositionY = ((upLeft.getY() + downRight.getY()) / 2) + /*(textHeight / 2)*/ 20;

        Paint redPaint = new Paint(Color.RED);
        this.dialogCanvas.drawText(text, textPositionX, textPositionY, this.textPaint);
        /*
        this.dialogCanvas.drawRect(new Rect(textPositionX - 5, textPositionY - 5, textPositionX + 5, textPositionY + 5), this.textPaint);
        this.dialogCanvas.drawRect(new Rect(textPositionX, textPositionY,
                textPositionX + textWidth, textPositionY + textHeight), redPaint);

         */
    }
}
