package com.psychocactusproject.interaction.menu;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.psychocactusproject.engine.manager.GameEngine;
import com.psychocactusproject.engine.manager.GameEngine.SCENES;
import com.psychocactusproject.engine.screens.GameScreen;
import com.psychocactusproject.engine.screens.Scene;
import com.psychocactusproject.engine.util.Hitbox;
import com.psychocactusproject.engine.util.Point;
import com.psychocactusproject.engine.util.TextUtil;
import com.psychocactusproject.engine.util.UserInterfaceFlyweight;
import com.psychocactusproject.graphics.interfaces.Drawable;
import com.psychocactusproject.graphics.manager.MenuBitmapFlyweight;
import com.psychocactusproject.graphics.manager.ResourceLoader;
import com.psychocactusproject.input.Slidable;
import com.psychocactusproject.input.Touchable;
import com.psychocactusproject.interaction.scripts.Clickable;

import java.util.List;

import static com.psychocactusproject.input.TouchInputController.squareCollision;

import static com.psychocactusproject.interaction.menu.DialogScreen.DIALOG_TYPE.ALERT;
import static com.psychocactusproject.interaction.menu.DialogScreen.DIALOG_TYPE.CONFIRMATION;

public class DialogScreen implements Clickable, Scene {

    private static final int CONFIRMATION_ACCEPT = 0;
    private static final int CONFIRMATION_CANCEL = 1;
    private static final int ALERT_ACCEPT = 2;

    private MenuBitmapFlyweight.DialogMenuFlyweight pieces;
    private Canvas dialogCanvas;
    private Matrix dialogMatrix;
    private Matrix dialogBuildingMatrix;
    private Paint dialogPaint;
    private Paint textPaint;
    private Hitbox[] dialogHitboxes;
    private Bitmap dialogBitmap;
    //
    private int width;
    private int height;
    private final Point dialogPosition;


    public enum DIALOG_TYPE {
        CONFIRMATION, ALERT
    }

    private DIALOG_TYPE type;
    private String message;
    private String details;
    private Runnable action;

    public DialogScreen() {
        this.pieces = MenuBitmapFlyweight.getDialogMenuInstance();
        this.dialogPosition = new Point();
        this.dialogCanvas = new Canvas();
        this.dialogMatrix = new Matrix();
        this.dialogBuildingMatrix = new Matrix();
        this.dialogPaint = new Paint();
        this.textPaint = new Paint();
        this.textPaint.setTextSize(60);
    }

    @Override
    public Drawable definedDrawable() {
        return (canvas) -> {
            //synchronized (GameEntity.entitiesLock) {
            // Dibuja de fondo el juego en su estado actual
            GameScreen.getInstance().definedDrawable(true).draw(canvas);
            // Imprime la ventana de diálogo
            // REFACTORIZAR: PROHIBIDO LLAMAR CONSTRUCTORES EN EL BUCLE DE DIBUJADO
            // int left, int top, int right, int bottom
            this.dialogPaint.setColor(ResourceLoader.backgroundColor);
            canvas.drawRect(
                    new Rect(0, 0, GameEngine.RESOLUTION_X, GameEngine.RESOLUTION_Y),
                    this.dialogPaint);

            // Ahora se dibuja como tal el menú de diálogo
            // this.draw(canvas);
            // equivalente a this.draw(canvas);
            canvas.drawBitmap(this.dialogBitmap, this.dialogMatrix, null);

            if (GameEngine.DEBUGGING) {
                Hitbox.drawHitboxes(this.getHitboxes(), canvas);
            }
            //}
        };
    }

    @Override
    public Touchable definedTouchable() {
        return (point) -> {
            // Comprueba si la pantalla de diálogo ha sido creada
            DialogScreen dialog = GameEngine.getInstance().getSurfaceGameView().getDialog();
            if (dialog == null) {
                throw new IllegalStateException("La pantalla de diálogo debía estar abierta.");
            }
            // Busca si existe colisión con alguna hitbox del diálogo
            Hitbox selected = null;
            for (Hitbox hitbox : dialog.getHitboxes()) {
                if (squareCollision(point.getX(), point.getY(), hitbox)) {
                    selected = hitbox;
                }
            }
            // Si ha habido colisión, ejecuta su acción asignada
            if (selected != null) {
                Clickable clickableHolder = selected.getFather();
                clickableHolder.executeClick(selected.getIndex());
            }
        };
    }

    @Override
    public void onSceneChange(SCENES oldScene) {

    }

    @Override
    public SCENES getSceneId() {
        return SCENES.DIALOG;
    }

    @Override
    public List<Slidable> getSlidables() {
        return null;
    }

    @Override
    public int getPositionX() {
        return this.dialogPosition.getX();
    }

    @Override
    public int getPositionY() {
        return this.dialogPosition.getY();
    }

    @Override
    public Point getPosition() {
        return this.dialogPosition;
    }

    @Override
    public int getSpriteWidth() {
        return this.width;
    }

    @Override
    public int getSpriteHeight() {
        return this.height;
    }

    public void initializeDialog(String message, String details) {
        this.initializeDialog(message, details, null);
    }

    public void initializeDialog(String message, String details, Runnable action) {
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
        //
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
        //
        this.dialogPaint.setColor(Color.argb(150, 150, 150, 150));
        this.textPaint.setColor(Color.WHITE);
        this.textPaint.setTypeface(ResourceLoader.getTypeface());
        //
        this.buildDialogScreen();
        this.dialogMatrix.reset();
        int xOffset = (GameEngine.RESOLUTION_X - this.getSpriteWidth()) / 2;
        int yOffset = (GameEngine.RESOLUTION_Y - this.getSpriteHeight()) / 2;
        this.dialogPosition.set(xOffset, yOffset);
        this.dialogMatrix.postTranslate(xOffset, yOffset);
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
                "diálogo, pero no está permitido activar ni desactivar las opciones");
    }

    @Override
    public void disableClickable(int index) {
        throw new IllegalStateException("Se ha intentado desactivar una opción de la pantalla de " +
                "diálogo, pero no está permitido activar ni desactivar las opciones");
    }

    private void buildDialogScreen() {
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
        this.width = computedWidth;
        this.height = computedHeight;
        // Crea la imagen del diálogo y la vincula con el canvas
        this.dialogBitmap = Bitmap.createBitmap(
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
        this.dialogBuildingMatrix.reset();
        Bitmap nextPiece;
        // Arriba izquierda
        nextPiece = this.pieces.getTopLeftPiece();
        this.dialogCanvas.drawBitmap(nextPiece, this.dialogBuildingMatrix, this.dialogPaint);
        this.dialogBuildingMatrix.postTranslate(nextPiece.getWidth(), 0);

        // Arriba centro
        nextPiece = this.pieces.getTopPiece();
        for (int i = 0; i < piezasHorizontal; i++) {
            this.dialogCanvas.drawBitmap(nextPiece, this.dialogBuildingMatrix, this.dialogPaint);
            this.dialogBuildingMatrix.postTranslate(nextPiece.getWidth(), 0);
        }
        // Arriba derecha
        nextPiece = this.pieces.getTopRightPiece();
        this.dialogCanvas.drawBitmap(nextPiece, this.dialogBuildingMatrix, this.dialogPaint);
        this.dialogBuildingMatrix.reset();
        this.dialogBuildingMatrix.postTranslate(0, nextPiece.getHeight());
        // Espacio para text
        for (int i = 0; i < piezasVertical; i++) {
            // Centro izquierda
            nextPiece = this.pieces.getLeftPiece();
            this.dialogCanvas.drawBitmap(nextPiece, this.dialogBuildingMatrix, this.dialogPaint);
            this.dialogBuildingMatrix.postTranslate(nextPiece.getWidth(), 0);
            // Cálculo de la posición de las opciones
            float[] values = new float[9];
            this.dialogBuildingMatrix.getValues(values);
            int yCoord = (int) values[Matrix.MTRANS_Y];
            // Centro
            nextPiece = this.pieces.getCenterPiece();
            for (int j = 0; j < piezasHorizontal; j++) {
                this.dialogCanvas.drawBitmap(nextPiece, this.dialogBuildingMatrix, this.dialogPaint);
                this.dialogBuildingMatrix.postTranslate(nextPiece.getWidth(), 0);
            }
            // Centro derecha
            nextPiece = this.pieces.getRightPiece();
            this.dialogCanvas.drawBitmap(nextPiece, this.dialogBuildingMatrix, this.dialogPaint);
            this.dialogBuildingMatrix.reset();
            this.dialogBuildingMatrix.postTranslate(0, yCoord + nextPiece.getHeight());
        }
        // Arriba izquierda
        nextPiece = this.pieces.getBottomLeftPiece();
        this.dialogCanvas.drawBitmap(nextPiece, this.dialogBuildingMatrix, this.dialogPaint);
        this.dialogBuildingMatrix.postTranslate(nextPiece.getWidth(), 0);
        // Arriba centro
        nextPiece = this.pieces.getBottomPiece();
        for (int i = 0; i < piezasHorizontal; i++) {
            this.dialogCanvas.drawBitmap(nextPiece, this.dialogBuildingMatrix, this.dialogPaint);
            this.dialogBuildingMatrix.postTranslate(nextPiece.getWidth(), 0);
        }
        // Arriba derecha
        nextPiece = this.pieces.getBottomRightPiece();
        this.dialogCanvas.drawBitmap(nextPiece, this.dialogBuildingMatrix, this.dialogPaint);
        this.dialogBuildingMatrix.reset();
        // Comienza a dibujar el texto vinculado al diálogo
        TextUtil.drawCenteredText(this.dialogCanvas, this.message, computedWidth, this.textPaint);
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
            UserInterfaceFlyweight.getInstance().drawButton(optionText, optionButton,
                    this.textPaint, this.dialogCanvas);
        }
    }
}
