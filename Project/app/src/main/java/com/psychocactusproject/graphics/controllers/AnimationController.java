package com.psychocactusproject.graphics.controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.psychocactusproject.interaction.menu.MenuDisplay;
import com.psychocactusproject.manager.engine.GameClock;
import com.psychocactusproject.manager.engine.GameEngine;
import com.psychocactusproject.manager.engine.Hitbox;
import com.psychocactusproject.manager.engine.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class AnimationController extends AbstractSprite implements MenuDisplay {

    private int action;
    private final int totalActions;
    private final String[] actionNames;
    private final String characterName;
    private final List<Bitmap[]> animationImages;
    private final int[] animationWidths;
    private final int[] animationHeights;
    private final int[] totalframesPerAction;
    private GameClock animationTimer;

    protected AnimationController(GameEngine gameEngine) {
        super(gameEngine);
        this.action = 0;
        // Recursos informados por las clases descendientes
        AnimationResources resources = this.obtainAnimationResources();
        // Nombre del personaje de la animación
        this.characterName = resources.characterName;
        // HashMap con el nombre y contenido de cada animación
        this.totalActions = resources.animations.size();
        this.actionNames = new String[totalActions];
        this.totalframesPerAction = new int[totalActions];
        this.animationImages = new ArrayList();
        this.animationWidths = new int[totalActions];
        this.animationHeights = new int[totalActions];
        // Fuerza la obtención de la imagen en tamaño natural
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        // La API en uso, la 21, no permite la estructura foreach
        Set<String> keySet = resources.animations.keySet();
        Iterator keys = keySet.iterator();
        // Iteración sobre las animaciones
        for (int i = 0; keys.hasNext(); i++) {
            Object key = keys.next();
            this.actionNames[i] = (String) key;
            int[] animationCodes = resources.animations.get(key);
            Bitmap[] bitmaps = new Bitmap[animationCodes.length];
            // Iteración sobre las imágenes que componen cada animación
            for (int j = 0; j < bitmaps.length; j++) {
                bitmaps[j] = BitmapFactory.decodeResource(
                        gameEngine.getContext().getResources(),
                        animationCodes[j],
                        options);
            }
            this.animationImages.add(bitmaps);
            if (bitmaps.length < 1) {
                throw new IllegalStateException("Una animación debe estar compuesta " +
                        "por al menos una imagen.");
            }
            this.animationWidths[i] = bitmaps[0].getWidth();
            this.animationHeights[i] = bitmaps[0].getHeight();
            this.totalframesPerAction[i] = bitmaps.length;
        }
        if (keySet.size() < 1) {
            throw new IllegalStateException("Una animación debe estar compuesta " +
                    "por al menos una acción.");
        }
        this.setHitboxes(resources.hitboxes);
        // Tener en cuenta para el futuro: el primer número siempre será el número de frames
        // que componen la primera animación, pero el segundo es un pelín más complicado: se trata
        // del tiempo que tarda en repetirse un beat, una nota negra en un compás a un ritmo
        // determinado. Por lo tanto, dentro de GameClock debe programarse una función que
        // transforme BPM en periodos de tiempo, que son los periodos de cada animación
        this.animationTimer = new GameClock(this.totalframesPerAction[0], 1);
    }

    protected Bitmap getAnimationImage() {
        // provisional, falta parametrizar correctamente
        return animationImages.get(action)[animationTimer.getTimestamp()];
    }

    @Override
    public int getSpriteWidth() {
        return this.animationWidths[action];
    }

    @Override
    public int getSpriteHeight() {
        return this.animationHeights[action];
    }

    @Override
    public Hitbox[] getHitboxes() {
        return this.getAllHitboxes()[this.action];
    }

    @Override
    public Point getFatherPosition() {
        return this.getPosition();
    }

    public String getCharacterName() {
        return this.characterName;
    }

    public int getCurrentAction() {
        return this.action;
    }

    public int getTotalActions() {
        return this.totalActions;
    }

    protected abstract AnimationResources obtainAnimationResources();

    public class AnimationResources {

        private final String characterName;
        private final HashMap<String, int[]> animations;
        private final Hitbox[][] hitboxes;

        public AnimationResources(String characterName, HashMap<String, int[]> animations,
                Hitbox[][] hitboxes) {
            this.characterName = characterName;
            this.animations = animations;
            this.hitboxes = hitboxes;
        }
    }

}
