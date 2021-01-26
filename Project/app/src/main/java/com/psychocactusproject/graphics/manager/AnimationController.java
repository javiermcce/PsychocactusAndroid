package com.psychocactusproject.graphics.manager;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.psychocactusproject.engine.GameClock;
import com.psychocactusproject.engine.GameEngine;

import java.util.ArrayList;
import java.util.List;

public abstract class AnimationController extends AbstractSprite {

    private int action;
    private final int totalActions;
    private final String[] actionNames;
    private final String entityName;
    private final List<Bitmap[]> animationImages;
    private final int[] animationWidths;
    private final int[] animationHeights;
    private final int[] spritesNum;
    private GameClock animationTimer;

    protected AnimationController(GameEngine gameEngine) {
        super(gameEngine);
        this.action = 0;
        List<int[]> bitmapCodes = this.obtainBitmapCodes();
        this.actionNames = this.obtainActionNames();
        this.entityName = this.obtainEntityRole();
        this.animationImages = new ArrayList();
        this.animationWidths = new int[bitmapCodes.size()];
        this.animationHeights = new int[bitmapCodes.size()];
        this.spritesNum = new int[bitmapCodes.size()];
        this.totalActions = bitmapCodes.size();
        Resources resources = gameEngine.getContext().getResources();
        Drawable drawableSprite;
        for (int i = 0 ; i < bitmapCodes.size(); i++) {
            Bitmap[] bitmaps = new Bitmap[bitmapCodes.get(i).length];
            for (int j = 0; j < bitmapCodes.get(i).length; j++) {
                drawableSprite = resources.getDrawable(bitmapCodes.get(i)[j]);
                if (j == 0) {
                    this.animationWidths[i] =
                            (int) (drawableSprite.getIntrinsicWidth() * this.getPixelFactor());
                    this.animationHeights[i] =
                            (int) (drawableSprite.getIntrinsicHeight() * this.getPixelFactor());
                    this.spritesNum[i] = bitmapCodes.get(i).length;
                }
                bitmaps[j] = ((BitmapDrawable) drawableSprite).getBitmap();
            }
            this.animationImages.add(bitmaps);
        }
        this.animationTimer = new GameClock(8, 1);

    }

    protected Bitmap getAnimationImage() {
        // provisional, falta parametrizar correctamente
        return animationImages.get(action)[animationTimer.getTimestamp()];
    }

    protected abstract List<int[]> obtainBitmapCodes();
    protected abstract String[] obtainActionNames();
    protected abstract String obtainEntityRole();

}
