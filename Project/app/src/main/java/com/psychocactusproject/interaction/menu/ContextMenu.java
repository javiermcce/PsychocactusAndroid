package com.psychocactusproject.interaction.menu;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.psychocactusproject.graphics.controllers.Sprite;
import com.psychocactusproject.manager.engine.GameEngine;

public final class ContextMenu extends Sprite {

    private MenuFlyweight pieces;
    private MenuDisplay father;
    private boolean menuIsAvailable;
    private Canvas menuCanvas;
    private Matrix menuMatrix;
    private Paint menuPaint;

    public ContextMenu(GameEngine gameEngine, MenuDisplay father) {
        super(gameEngine, father.getRoleName());
        this.father = father;
        this.setBitmap(this.buildMenuBitmap(father.getMenuOptions()));
        this.pieces = MenuFlyweight.getInstance(gameEngine);
        this.menuCanvas = new Canvas();
        this.menuMatrix = new Matrix();
        this.menuPaint = new Paint();
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
        nextPiece = this.pieces.getTopLeftPiece();
        this.menuCanvas.drawBitmap(nextPiece, this.menuMatrix, this.menuPaint);
        this.menuMatrix.postTranslate(nextPiece.getWidth(), 0);
        nextPiece = this.pieces.getTopPiece();
        this.menuCanvas.drawBitmap(nextPiece, this.menuMatrix, this.menuPaint);
        this.menuMatrix.postTranslate(nextPiece.getWidth(), 0);
        // {...}
        // String a dibujar en cada altura...
        // {...}
        return menuBitmap;
    }

    public boolean isAvailable() {
        return this.menuIsAvailable;
    }

    public class MenuOption {

        public boolean available;
        public String optionName;

        public MenuOption(boolean available, String option) {
            this.available = available;
            this.optionName = option;
        }
    }
}
