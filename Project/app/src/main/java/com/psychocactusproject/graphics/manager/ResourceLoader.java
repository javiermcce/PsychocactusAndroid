package com.psychocactusproject.graphics.manager;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Typeface;
import android.view.ContextThemeWrapper;

import androidx.core.content.res.ResourcesCompat;

import com.psychocactusproject.R;
import com.psychocactusproject.android.GameActivity;

public class ResourceLoader {

    private static ResourceLoader instance;

    public static ResourceLoader getInstance() {
        if (instance == null) {
            throw new IllegalStateException(
                    "La instancia de ResourceLoader debe ser inicializada previamente.");
        }
        return instance;
    }

    public static void initializeResourceLoader(GameActivity activity) {
        instance = new ResourceLoader(activity);
    }

    private final GameActivity activity;
    private final Options options = new Options();
    private final Typeface typeface;

    private ResourceLoader(GameActivity activity) {
        this.options.inScaled = false;
        this.activity = activity;
        this.typeface = ResourcesCompat.getFont(
                this.activity.getWindow().getContext(), R.font.truetypefont);
    }

    public static GameActivity getGameActivity() {
        return getInstance().activity;
    }

    public static Context getGameContext() {
        return getInstance().activity.getWindow().getContext();
    }

    public static Resources getGameResources() {
        return getInstance().activity.getResources();
    }

    public static Options getGameBitmapOptions() {
        return getInstance().options;
    }

    public static Bitmap loadBitmap(int bitmapId) {
        return BitmapFactory.decodeResource(
                getInstance().activity.getWindow().getContext().getResources(),
                bitmapId,
                getInstance().options);
    }

    public static Typeface getTypeface() {
        return getInstance().typeface;
    }
}
