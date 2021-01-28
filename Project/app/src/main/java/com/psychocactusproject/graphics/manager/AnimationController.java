package com.psychocactusproject.graphics.manager;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.psychocactusproject.R;
import com.psychocactusproject.engine.GameClock;
import com.psychocactusproject.engine.GameEngine;

import java.io.File;
import java.net.URI;
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
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        for (int i = 0 ; i < bitmapCodes.size(); i++) {
            Bitmap[] bitmaps = new Bitmap[bitmapCodes.get(i).length];
            for (int j = 0; j < bitmapCodes.get(i).length; j++) {
                bitmaps[j] = BitmapFactory.decodeResource(
                        gameEngine.getContext().getResources(),
                        bitmapCodes.get(i)[j],
                        options);
                if (j == 0) {
                    this.animationWidths[i] = bitmaps[j].getWidth();
                    this.animationHeights[i] = bitmaps[j].getHeight();
                    this.spritesNum[i] = bitmapCodes.get(i).length;
                }
            }
            this.animationImages.add(bitmaps);
        }
        this.animationTimer = new GameClock(8, 1);
    }

    protected Bitmap getAnimationImage() {
        // provisional, falta parametrizar correctamente
        return animationImages.get(action)[animationTimer.getTimestamp()];
    }

    protected int getAnimationWidth() {
        return this.animationWidths[action];
    }

    protected int getAnimationHeight() {
        return this.animationHeights[action];

    }

    protected abstract List<int[]> obtainBitmapCodes();
    protected abstract String[] obtainActionNames();
    protected abstract String obtainEntityRole();

}
