package com.psychocactusproject.graphics.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

import com.psychocactusproject.R;
import com.psychocactusproject.engine.GameEngine;

public class ResourceLoader {

    private static ResourceLoader instance;

    private static ResourceLoader getInstance() {
        if (instance == null) {
            instance = new ResourceLoader();
        }
        return instance;
    }

    private final Options options = new Options();
    private final Typeface typeface;

    private ResourceLoader() {
        this.options.inScaled = false;
        this.typeface = ResourcesCompat.getFont(
                GameEngine.getInstance().getContext(), R.font.truetypefont);
    }

    public static Bitmap loadBitmap(int bitmapId) {
        return BitmapFactory.decodeResource(
                GameEngine.getInstance().getContext().getResources(),
                bitmapId,
                getInstance().options);
    }

    public static Typeface getTypeface() {
        return getInstance().typeface;
    }
}
