package com.psychocactusproject.interaction.menu;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.psychocactusproject.R;
import com.psychocactusproject.engine.GameEngine;

public class MenuFlyweight {

    private static MenuFlyweight instance;

    public static MenuFlyweight getInstance(GameEngine gameEngine) {
        if (instance == null) {
            instance = new MenuFlyweight(gameEngine);
        }
        return instance;
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

    private MenuFlyweight(GameEngine gameEngine) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Resources resources = gameEngine.getContext().getResources();
        this.bottomPiece = BitmapFactory.decodeResource(resources,
                R.drawable.menu_bottom, options);
        this.bottomLeftPiece = BitmapFactory.decodeResource(resources,
                R.drawable.menu_bottom_left, options);
        this.bottomRightPiece = BitmapFactory.decodeResource(resources,
                R.drawable.menu_bottom_right, options);
        this.centerPiece = BitmapFactory.decodeResource(resources,
                R.drawable.menu_center, options);
        this.leftPiece = BitmapFactory.decodeResource(resources,
                R.drawable.menu_left, options);
        this.rightPiece = BitmapFactory.decodeResource(resources,
                R.drawable.menu_right, options);
        this.topPiece = BitmapFactory.decodeResource(resources,
                R.drawable.menu_top, options);
        this.topLeftPiece = BitmapFactory.decodeResource(resources,
                R.drawable.menu_top_left, options);
        this.topRightPiece = BitmapFactory.decodeResource(resources,
                R.drawable.menu_top_right, options);
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
