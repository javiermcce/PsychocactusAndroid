package com.psychocactusproject.characters.band;

import android.view.View;

import com.psychocactusproject.R;
import com.psychocactusproject.engine.GameEngine;

import java.util.HashMap;

public class Singer extends Musician {

    protected Singer(GameEngine gameEngine, View view) {
        super(gameEngine, view);
    }

    @Override
    public String getRoleName() {
        return "Singer";
    }

    @Override
    protected AnimationResources obtainAnimationResources() {
        String characterName = "Thomasa";
        HashMap<String, int[]> animations = new HashMap();
        // Imágenes de animación de cantante por defecto
        /*
        int[] idleAnimation = new int[8];
        idleAnimation[0] = R.drawable.singer_idle_1;
        idleAnimation[1] = R.drawable.singer_idle_2;
        idleAnimation[2] = R.drawable.singer_idle_3;
        idleAnimation[3] = R.drawable.singer_idle_4;
        idleAnimation[4] = R.drawable.singer_idle_5;
        idleAnimation[5] = R.drawable.singer_idle_6;
        idleAnimation[6] = R.drawable.singer_idle_7;
        idleAnimation[7] = R.drawable.singer_idle_8;
        animations.put("Idle", idleAnimation);
        */
        // Se devuelve la información para que AnimationController la almacene e interprete
        return new AnimationResources(characterName, animations);
    }
}
