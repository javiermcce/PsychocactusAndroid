package com.psychocactusproject.graphics.controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.psychocactusproject.engine.util.GameClock;
import com.psychocactusproject.engine.manager.GameEngine;
import com.psychocactusproject.engine.util.Hitbox;
import com.psychocactusproject.graphics.manager.ResourceLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class AnimatedEntity extends AbstractSprite {

    private int action;
    private final int totalActions;
    private final String[] actionNames;
    private final String characterName;
    private final List<Bitmap[]> animationImages;
    private final int[] animationWidths;
    private final int[] animationHeights;
    private final int[] totalframesPerAction;
    private GameClock animationTimer;

    protected AnimatedEntity(GameEngine gameEngine) {
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
        this.animationImages = new ArrayList<>();
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
                bitmaps[j] = ResourceLoader.loadBitmap(animationCodes[j]);
                /*
                bitmaps[j] = BitmapFactory.decodeResource(
                        gameEngine.getContext().getResources(),
                        animationCodes[j],
                        options);

                 */
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
        // Tener en cuenta para el futuro: el primer número siempre será el número de frames
        // que componen la primera animación, pero el segundo es un pelín más complicado: se trata
        // del tiempo que tarda en repetirse un beat, una nota negra en un compás a un ritmo
        // determinado. Por lo tanto, dentro de GameClock debe programarse una función que
        // transforme BPM en periodos de tiempo, que son los periodos de cada animación
        this.animationTimer = new GameClock(this.totalframesPerAction[0], 1);
    }

    @Override
    protected Bitmap getSpriteImage() {
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

    // Número de frame que se está mostrando para la animación actual
    public int getFrameNumber() {
        return this.animationTimer.getTimestamp();
    }

    // Número total de frames que componen la animación actual
    public int getCurrentAnimationFrames() {
        return this.animationImages.get(this.getCurrentAction()).length;
    }

    // Animación que se está mostrando en este momento
    public int getCurrentAction() {
        return this.action;
    }

    // Reloj interno encargado de hacer avanzar la animación
    public GameClock getClock() {
        return this.animationTimer;
    }

    // Todas las animaciones de las que se dispone
    public int getTotalActions() {
        return this.totalActions;
    }

    // Nombre propio del personaje que instancia esta clase
    public String getCharacterName() {
        return this.characterName;
    }

    // Debe ser implementado por las clases descendientes para entregar todos los recursos
    protected abstract AnimationResources obtainAnimationResources();

    // Clase auxiliar utilizada durante la creación para entregar todos los recursos
    public class AnimationResources {

        protected final String characterName;
        protected final HashMap<String, int[]> animations;
        protected final Hitbox[][] hitboxes;

        public AnimationResources(String characterName, HashMap<String, int[]> animations,
                                  Hitbox[][] hitboxes) {
            this.characterName = characterName;
            this.animations = animations;
            this.hitboxes = hitboxes;
        }
    }

}
