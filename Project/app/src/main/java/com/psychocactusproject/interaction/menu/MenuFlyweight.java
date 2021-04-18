package com.psychocactusproject.interaction.menu;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.psychocactusproject.R;
import com.psychocactusproject.engine.GameEngine;

public class MenuFlyweight {

    public static final int CONTEXT_MENU_TYPE = 0;
    public static final int DIALOG_MENU_TYPE = 1;
    private static MenuFlyweight contextMenuInstance;
    private static MenuFlyweight dialogMenuInstance;

    public static MenuFlyweight getInstance(GameEngine gameEngine, int type) {
        switch (type) {
            case CONTEXT_MENU_TYPE:
                if (contextMenuInstance == null) {
                    contextMenuInstance = new MenuFlyweight(gameEngine, type);
                }
                return contextMenuInstance;
            case DIALOG_MENU_TYPE:
                if (dialogMenuInstance == null) {
                    dialogMenuInstance = new MenuFlyweight(gameEngine, type);
                }
                return dialogMenuInstance;
            default:
                throw new IllegalArgumentException("Se ha llamado a un tipo de instancia de menú " +
                        "que no existe.");
        }
    }

    private Bitmap bottomPiece;
    private Bitmap bottomLeftPiece;
    private Bitmap bottomRightPiece;
    private Bitmap centerPiece;
    private Bitmap leftPiece;
    private Bitmap rightPiece;
    private Bitmap topPiece;
    private Bitmap topLeftPiece;
    private Bitmap topRightPiece;

    private MenuFlyweight(GameEngine gameEngine, int type) {
        if (type == CONTEXT_MENU_TYPE) {
            this.contextMenuConstructor(gameEngine);
        } else if (type == DIALOG_MENU_TYPE) {
            this.dialogMenuConstructor(gameEngine);
        } else {
            throw new IllegalArgumentException("Se ha llamado a un tipo de instancia de menú " +
                    "que no existe.");
        }
    }

    public void contextMenuConstructor(GameEngine gameEngine) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Resources resources = gameEngine.getContext().getResources();
        this.bottomPiece = BitmapFactory.decodeResource(resources,
                R.drawable.context_bottom, options);
        this.bottomLeftPiece = BitmapFactory.decodeResource(resources,
                R.drawable.context_bottom_left, options);
        this.bottomRightPiece = BitmapFactory.decodeResource(resources,
                R.drawable.context_bottom_right, options);
        this.centerPiece = BitmapFactory.decodeResource(resources,
                R.drawable.context_center, options);
        this.leftPiece = BitmapFactory.decodeResource(resources,
                R.drawable.context_left, options);
        this.rightPiece = BitmapFactory.decodeResource(resources,
                R.drawable.context_right, options);
        this.topPiece = BitmapFactory.decodeResource(resources,
                R.drawable.context_top, options);
        this.topLeftPiece = BitmapFactory.decodeResource(resources,
                R.drawable.context_top_left, options);
        this.topRightPiece = BitmapFactory.decodeResource(resources,
                R.drawable.context_top_right, options);
    }

    public void dialogMenuConstructor(GameEngine gameEngine) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Resources resources = gameEngine.getContext().getResources();
        this.bottomPiece = BitmapFactory.decodeResource(resources,
                R.drawable.dialog_bottom, options);
        this.bottomLeftPiece = BitmapFactory.decodeResource(resources,
                R.drawable.dialog_bottom_left, options);
        this.bottomRightPiece = BitmapFactory.decodeResource(resources,
                R.drawable.dialog_bottom_right, options);
        this.centerPiece = BitmapFactory.decodeResource(resources,
                R.drawable.dialog_center, options);
        this.leftPiece = BitmapFactory.decodeResource(resources,
                R.drawable.dialog_left, options);
        this.rightPiece = BitmapFactory.decodeResource(resources,
                R.drawable.dialog_right, options);
        this.topPiece = BitmapFactory.decodeResource(resources,
                R.drawable.dialog_top, options);
        this.topLeftPiece = BitmapFactory.decodeResource(resources,
                R.drawable.dialog_top_left, options);
        this.topRightPiece = BitmapFactory.decodeResource(resources,
                R.drawable.dialog_top_right, options);
    }
    
    public Bitmap getBottomPiece() {
        return bottomPiece;
    }

    public Bitmap getBottomLeftPiece() {
        return bottomLeftPiece;
    }

    public Bitmap getBottomRightPiece() {
        return bottomRightPiece;
    }

    public Bitmap getCenterPiece() {
        return centerPiece;
    }

    public Bitmap getLeftPiece() {
        return leftPiece;
    }

    public Bitmap getRightPiece() {
        return rightPiece;
    }

    public Bitmap getTopPiece() {
        return topPiece;
    }

    public Bitmap getTopLeftPiece() {
        return topLeftPiece;
    }

    public Bitmap getTopRightPiece() {
        return topRightPiece;
    }
}
