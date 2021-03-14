package com.psychocactusproject.interaction.menu;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;

import androidx.core.content.res.ResourcesCompat;

import com.psychocactusproject.R;
import com.psychocactusproject.engine.GameLogic;
import com.psychocactusproject.graphics.controllers.InanimateSprite;
import com.psychocactusproject.interaction.scripts.Clickable;
import com.psychocactusproject.android.GameFragment;
import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.engine.Hitbox;
import com.psychocactusproject.engine.Point;

public class ContextMenu extends InanimateSprite implements Clickable {

    private MenuFlyweight pieces;
    private MenuDisplay father;
    private boolean menuIsAvailable;
    private boolean menuIsShown;
    private Canvas menuCanvas;
    private Matrix menuMatrix;
    private Paint menuPaint;
    private TextPaint textPaint;
    private Hitbox[] menuHitboxes;
    private MenuOption[] lastMenuOptions;
    private Point lastPosition;

    // Para implementar que las opciones estén disponibles o no, basta generar un bitmap en que
    // el texto se imprima con un color grisaceo
    // En cuanto a las hitboxes, sobrescribir su método para devolver un nuevo array solo con las
    // disponibles

    // MODIFICAR IMÁGENES PARA QUE SE VEA MÁS CONTRASTADO CON EL FONDO, QUITAR UN POCO DE ALPHA

    public ContextMenu(GameEngine gameEngine, MenuDisplay father) {
        super(gameEngine, father.getRoleName() + " menu");
        this.father = father;
        this.pieces = MenuFlyweight.getInstance(gameEngine);
        this.menuCanvas = new Canvas();
        this.menuMatrix = new Matrix();
        this.menuPaint = new Paint();
        this.menuIsAvailable = false;
        this.menuIsShown = false;
        this.textPaint = new TextPaint();
        this.textPaint.setTextSize(42);
        this.textPaint.setColor(Color.WHITE);
        Typeface typeface = ResourcesCompat.getFont(gameEngine.getContext(), R.font.truetypefont);
        this.textPaint.setTypeface(typeface);
        this.setBitmap(this.buildMenu(father.getMenuOptions()));
    }

    public void onUpdate() {
        this.setBitmap(this.buildMenu(father.getMenuOptions()));
    }

    // Construye una nueva versión del menú y actualiza los parámetros de la instancia
    public Bitmap buildMenu(MenuOption[] options) {
        if (options == null || options.length == 0) {
            this.menuIsAvailable = false;
            return null;
        }
        this.menuIsAvailable = true;
        int maximumOptionLength = 0;
        for (MenuOption option : options) {
            if (option.optionName.length() > maximumOptionLength) {
                maximumOptionLength = option.optionName.length();
            }
        }
        // maximumOptionLength += 3;
        this.lastMenuOptions = options;
        // Calculamos el tamaño que tendrá el eje horizontal del menú
        int computedWidth = this.pieces.getTopLeftPiece().getWidth() +
                this.pieces.getTopRightPiece().getWidth() +
                (this.pieces.getCenterPiece().getWidth() * maximumOptionLength);
        int computedHeight = this.pieces.getTopLeftPiece().getHeight() +
                this.pieces.getBottomLeftPiece().getHeight() +
                (this.pieces.getCenterPiece().getHeight() * options.length);
        Bitmap menuBitmap = Bitmap.createBitmap(
                computedWidth, computedHeight, Bitmap.Config.ARGB_8888);
        this.menuCanvas.setBitmap(menuBitmap);
        // Dibujado del menú
        this.menuMatrix.reset();
        Bitmap nextPiece;
        // Arriba izquierda
        nextPiece = this.pieces.getTopLeftPiece();
        this.menuCanvas.drawBitmap(nextPiece, this.menuMatrix, this.menuPaint);
        this.menuMatrix.postTranslate(nextPiece.getWidth(), 0);
        // Arriba centro
        nextPiece = this.pieces.getTopPiece();
        for (int i = 0; i < maximumOptionLength; i++) {
            this.menuCanvas.drawBitmap(nextPiece, this.menuMatrix, this.menuPaint);
            this.menuMatrix.postTranslate(nextPiece.getWidth(), 0);
        }
        // Arriba derecha
        nextPiece = this.pieces.getTopRightPiece();
        this.menuCanvas.drawBitmap(nextPiece, this.menuMatrix, this.menuPaint);
        this.menuMatrix.reset();
        this.menuMatrix.postTranslate(0, nextPiece.getHeight());
        // Espacio para opciones
        for (int i = 0; i < options.length; i++) {
            // Centro izquierda
            nextPiece = this.pieces.getLeftPiece();
            this.menuCanvas.drawBitmap(nextPiece, this.menuMatrix, this.menuPaint);
            this.menuMatrix.postTranslate(nextPiece.getWidth(), 0);
            // Cálculo de la posición de las opciones
            float[] values = new float[9];
            this.menuMatrix.getValues(values);
            int xCoord = (int) values[Matrix.MTRANS_X];
            int yCoord = (int) values[Matrix.MTRANS_Y];
            // Centro
            nextPiece = this.pieces.getCenterPiece();
            for (int j = 0; j < maximumOptionLength; j++) {
                this.menuCanvas.drawBitmap(nextPiece, this.menuMatrix, this.menuPaint);
                this.menuMatrix.postTranslate(nextPiece.getWidth(), 0);
            }
            // Imprime nombre de opción
            this.menuCanvas.drawText(options[i].optionName, xCoord, yCoord + 40, this.textPaint);
            // Centro derecha
            nextPiece = this.pieces.getRightPiece();
            this.menuCanvas.drawBitmap(nextPiece, this.menuMatrix, this.menuPaint);
            this.menuMatrix.reset();
            this.menuMatrix.postTranslate(0, yCoord + nextPiece.getHeight());
            // Línea separadora horizontal
            if (i != 0) {
                this.menuCanvas.drawLine(xCoord, yCoord,
                        xCoord + computedWidth -
                                this.pieces.getRightPiece().getWidth() -
                                this.pieces.getLeftPiece().getWidth(),
                        yCoord,
                        this.textPaint);
            }
        }
        // Arriba izquierda
        nextPiece = this.pieces.getBottomLeftPiece();
        this.menuCanvas.drawBitmap(nextPiece, this.menuMatrix, this.menuPaint);
        this.menuMatrix.postTranslate(nextPiece.getWidth(), 0);
        // Arriba centro
        nextPiece = this.pieces.getBottomPiece();
        for (int i = 0; i < maximumOptionLength; i++) {
            this.menuCanvas.drawBitmap(nextPiece, this.menuMatrix, this.menuPaint);
            this.menuMatrix.postTranslate(nextPiece.getWidth(), 0);
        }
        // Arriba derecha
        nextPiece = this.pieces.getBottomRightPiece();
        this.menuCanvas.drawBitmap(nextPiece, this.menuMatrix, this.menuPaint);
        this.menuMatrix.reset();
        // Devuelve imagen finalizada y la almacena para posteriores usos
        return menuBitmap;
    }


    public boolean isMenuAvailable() {
        return this.menuIsAvailable;
    }

    @Override
    public boolean isAvailable(int index) {
        // aquí tendría que devolver opción por opción si está disponible o no
        return this.menuIsAvailable;
    }

    @Override
    public void enableClickable(int index) {

    }

    @Override
    public void disableClickable(int index) {

    }

    // también tendría que sobrescribir el método de dibujado, ya que el menú se
    // dibuja entero aunque alguna opción no esté disponible

    public boolean isShown() {
        return this.menuIsShown;
    }

    public void openMenu() {
        this.menuIsShown = true;
    }

    public void closeMenu() {
        this.menuIsShown = false;
        this.menuHitboxes = null;
    }

    @Override
    public Hitbox[] getHitboxes() {
        return this.menuHitboxes;
    }

    @Override
    public void draw(Canvas canvas) {
        if (this.isMenuAvailable() && this.isShown()) {
            if (this.hasMoved() || this.menuHitboxes == null) {
                this.updateByFatherPosition();
            }
            super.draw(canvas);
        }
    }

    private boolean hasMoved() {
        if (this.lastPosition == null) {
            this.lastPosition = this.father.getPosition();
            return true;
        } else {
            if (!this.lastPosition.equals(this.father.getPosition())) {
                this.lastPosition = this.father.getPosition();
                return true;
            } else {
                return false;
            }
        }
    }

    private void updateByFatherPosition() {
        // Coloca la imagen del menú
        Point menuPosition = this.father.getPosition();
        menuPosition.set(
                menuPosition.getX() + this.father.getSpriteWidth() + 20,
                menuPosition.getY() - 20);
        this.setPosition(menuPosition);
        // Coloca las hitboxes en relación a la posición ajustada
        this.menuHitboxes = new Hitbox[this.lastMenuOptions.length];
        int xMarginPerc = (int)((float) this.pieces.getLeftPiece().getWidth() /
                this.getSpriteWidth() * 100);
        int yMarginPerc = (int)((float) this.pieces.getTopPiece().getHeight() /
                this.getSpriteHeight() * 100);
        int yFragmentPerc = (100 - (yMarginPerc * 2)) / this.lastMenuOptions.length;
        for (int i = 0; i  < this.lastMenuOptions.length; i++) {
            int xPercUpLeft = xMarginPerc;
            int xPercDownRight = 100 - xMarginPerc;
            int yPercUpLeft = yMarginPerc + i * yFragmentPerc;
            int yPercDownRight = yMarginPerc + (i + 1) * yFragmentPerc;
            this.menuHitboxes[i] = new Hitbox(xPercUpLeft, yPercUpLeft,
                    xPercDownRight, yPercDownRight,
                    this, i);
        }
    }

    @Override
    public void executeClick(int index) {
        if (GameEngine.DEBUGGING) {
            System.out.println(this.getRoleName() + ": " + index);
            GameFragment.setDebugText(this.getRoleName() + ": " + this.lastMenuOptions[index].optionName);
        }
        this.father.onOptionSelected(this.lastMenuOptions[index].optionName);
        GameLogic.getInstance().getStateManager().updateEntities();
    }

    public static class MenuOption {

        public boolean available;
        public String optionName;

        public MenuOption(String option) {
            this.available = true;
            this.optionName = option;
        }

        public MenuOption(String option, boolean available) {
            this.available = available;
            this.optionName = option;
        }
    }
}
