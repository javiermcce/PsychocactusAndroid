package com.psychocactusproject.interaction.menu;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.psychocactusproject.engine.manager.GameEngine;
import com.psychocactusproject.engine.manager.GameEngine.SCENES;
import com.psychocactusproject.engine.util.Hitbox;
import com.psychocactusproject.engine.util.Point;
import com.psychocactusproject.engine.util.TextUtil;
import com.psychocactusproject.graphics.controllers.InanimateSprite;
import com.psychocactusproject.graphics.manager.MenuBitmapFlyweight;
import com.psychocactusproject.graphics.manager.ResourceLoader;
import com.psychocactusproject.interaction.scripts.Clickable;

import static com.psychocactusproject.interaction.menu.DialogScreen.DIALOG_TYPE.ALERT;
import static com.psychocactusproject.interaction.menu.DialogScreen.DIALOG_TYPE.CONFIRMATION;

public class DialogScreen extends InanimateSprite implements Clickable {

    private static final int CONFIRMATION_ACCEPT = 0;
    private static final int CONFIRMATION_CANCEL = 1;
    private static final int ALERT_ACCEPT = 2;

    private final MenuBitmapFlyweight.DialogMenuFlyweight pieces;
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
        this.pieces = MenuBitmapFlyweight.getDialogMenuInstance();
        this.dialogCanvas = new Canvas();
        this.dialogMatrix = new Matrix();
        this.dialogPaint = new Paint();
        this.textPaint = new Paint();
        //
        this.dialogPaint.setColor(Color.argb(200, 150, 150, 150));
        this.textPaint.setColor(Color.WHITE);
        this.textPaint.setTypeface(ResourceLoader.getTypeface());
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
        // Cantidad de piezas por las que está formado el marco del menú
        int piezasHorizontal = 20;
        int piezasVertical = 5;
        // Cálculo del tamaño final de la imagen generada por esta instancia
        int computedWidth = this.pieces.getTopLeftPiece().getWidth() +
                this.pieces.getTopRightPiece().getWidth() +
                (this.pieces.getCenterPiece().getWidth() * piezasHorizontal);
        int computedHeight = this.pieces.getTopLeftPiece().getHeight() +
                this.pieces.getBottomLeftPiece().getHeight() +
                (this.pieces.getCenterPiece().getHeight() * piezasVertical);
        // Crea la imagen del diálogo y la vincula con el canvas
        Bitmap dialogBitmap = Bitmap.createBitmap(
                computedWidth, computedHeight, Bitmap.Config.ARGB_8888);
        this.dialogCanvas.setBitmap(dialogBitmap);
        // Pinta un fondo más opaco sobre la imagen de diálogo para facilitar la lectura del texto
        int border = 18;
        Paint backgroundColor = new Paint();
        backgroundColor.setColor(Color.argb(100, 171, 171, 171));
        this.dialogCanvas.drawRect(
                new Rect(border, border, computedWidth - border, computedHeight - border),
                backgroundColor
        );
        // Comienza el dibujado de la ventana de diálogo
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
        // Comienza a dibujar el texto vinculado al diálogo
        TextUtil.drawCenteredText(this.dialogCanvas, this.message, computedWidth, this.textPaint);
        // Se ajustan los colores del botón (interior y exterior)
        Paint insideButtonOptionPaint = new Paint();
        insideButtonOptionPaint.setColor(Color.argb(255, 174, 182, 191));
        Paint outsideButtonOptionPaint = new Paint();
        outsideButtonOptionPaint.setColor(Color.argb(255, 126, 133, 143));
        // Son dibujados todos los botones para la instancia
        for (Hitbox optionButton : this.getHitboxes()) {
            // Texto del botón según el caso
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
            this.drawButton(optionText, optionButton, computedWidth, computedHeight, outsideButtonOptionPaint, insideButtonOptionPaint);
        }
        // Es devuelta la imagen generada
        return dialogBitmap;
    }

    public void drawButton(String optionText, Hitbox optionButton, int computedWidth, int computedHeight, Paint outsideButtonOptionPaint, Paint insideButtonOptionPaint) {
        // Posición de esquina arriba a la izquierda
        Point relativeUpLeft = Hitbox.percentagesToRelativePoint(
                optionButton.getXUpLeftPercentage(),
                optionButton.getYUpLeftPercentage(),
                computedWidth, computedHeight
        );
        // Posición de esquina abajo a la derecha
        Point relativeDownRight = Hitbox.percentagesToRelativePoint(
                optionButton.getXDownRightPercentage(),
                optionButton.getYDownRightPercentage(),
                computedWidth, computedHeight
        );
        // Son dibujados los cuadros interior y exterior del botón
        this.dialogCanvas.drawRect(
                new Rect(relativeUpLeft.getX(), relativeUpLeft.getY(),
                        relativeDownRight.getX(), relativeDownRight.getY()),
                outsideButtonOptionPaint
        );
        this.dialogCanvas.drawRect(
                new Rect(relativeUpLeft.getX() + 5 , relativeUpLeft.getY() + 5,
                        relativeDownRight.getX() - 5, relativeDownRight.getY() - 5),
                insideButtonOptionPaint
        );
        // Dibujado del texto del botón
        this.textPaint.setTextSize(60);
        TextUtil.drawCenteredLine(this.dialogCanvas, relativeUpLeft, relativeDownRight, optionText, this.textPaint);
    }
}
