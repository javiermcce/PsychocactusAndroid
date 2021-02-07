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
import com.psychocactusproject.graphics.controllers.Sprite;
import com.psychocactusproject.manager.engine.GameEngine;

public class ContextMenu extends Sprite {

    private MenuFlyweight pieces;
    private MenuDisplay father;
    private boolean menuIsAvailable;
    private Canvas menuCanvas;
    private Matrix menuMatrix;
    private Paint menuPaint;
    private TextPaint textPaint;

    public ContextMenu(GameEngine gameEngine, MenuDisplay father) {
        super(gameEngine, father.getRoleName());
        this.father = father;
        this.pieces = MenuFlyweight.getInstance(gameEngine);
        this.menuCanvas = new Canvas();
        this.menuMatrix = new Matrix();
        this.menuPaint = new Paint();

        this.textPaint = new TextPaint();
        this.textPaint.setTextSize(42);
        this.textPaint.setColor(Color.WHITE);

        // res/font/truetypefont.ttf
        Typeface typeface = ResourcesCompat.getFont(gameEngine.getContext(), R.font.truetypefont);
        // textPaint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        this.textPaint.setTypeface(typeface);

        this.setBitmap(this.buildMenuBitmap(father.getMenuOptions()));
    }

    public void onUpdate() {
        this.setBitmap(this.buildMenuBitmap(father.getMenuOptions()));
    }

    // Construye una nueva versión del menú y actualiza los parámetros de la instancia
    public Bitmap buildMenuBitmap(MenuOption[] options) {
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
        // Sumamos el margen derecho
        maximumOptionLength += 3;
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
            // Imprime nombre de opción
            float[] values = new float[9];
            this.menuMatrix.getValues(values);
            int xCoord = (int) values[Matrix.MTRANS_X];
            int yCoord = (int) values[Matrix.MTRANS_Y];
            this.menuCanvas.drawText(options[i].optionName, xCoord, yCoord, this.textPaint);
            // Centro
            nextPiece = this.pieces.getCenterPiece();
            for (int j = 0; j < maximumOptionLength; j++) {
                this.menuCanvas.drawBitmap(nextPiece, this.menuMatrix, this.menuPaint);
                this.menuMatrix.postTranslate(nextPiece.getWidth(), 0);
            }
            // Centro derecha
            nextPiece = this.pieces.getRightPiece();
            this.menuCanvas.drawBitmap(nextPiece, this.menuMatrix, this.menuPaint);
            this.menuMatrix.reset();
            this.menuMatrix.postTranslate(0, yCoord + nextPiece.getHeight());
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

    public boolean isAvailable() {
        return this.menuIsAvailable;
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
