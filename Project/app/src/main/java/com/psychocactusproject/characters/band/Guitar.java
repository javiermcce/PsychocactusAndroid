package com.psychocactusproject.characters.band;

import android.view.View;

import com.psychocactusproject.R;
import com.psychocactusproject.engine.GameEngine;

import java.util.HashMap;

public class Guitar extends Musician {

    protected Guitar(GameEngine gameEngine, View view) {
        super(gameEngine, view);
    }

    @Override
    public String getRoleName() {
        return "Guitar";
    }

    @Override
    protected AnimationResources obtainAnimationResources() {
        String characterName = "Björk";
        HashMap<String, int[]> animations = new HashMap();
        // Imágenes de animación de guitarrista por defecto
        /*
        int[] idleAnimation = new int[8];
        idleAnimation[0] = R.drawable.guitar_idle_1;
        idleAnimation[1] = R.drawable.guitar_idle_2;
        idleAnimation[2] = R.drawable.guitar_idle_3;
        idleAnimation[3] = R.drawable.guitar_idle_4;
        idleAnimation[4] = R.drawable.guitar_idle_5;
        idleAnimation[5] = R.drawable.guitar_idle_6;
        idleAnimation[6] = R.drawable.guitar_idle_7;
        idleAnimation[7] = R.drawable.guitar_idle_8;
        animations.put("Idle", idleAnimation);
        */
        // Se devuelve la información para que AnimationController la almacene e interprete
        return new AnimationResources(characterName, animations);
    }
}
