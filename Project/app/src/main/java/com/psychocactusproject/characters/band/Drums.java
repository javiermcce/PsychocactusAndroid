package com.psychocactusproject.characters.band;

import android.view.View;

import com.psychocactusproject.R;
import com.psychocactusproject.manager.engine.GameEngine;
import com.psychocactusproject.manager.engine.Point;

import java.util.HashMap;

public class Drums extends Musician {

    public Drums(GameEngine gameEngine) {
        super(gameEngine);
        this.setPosition(new Point(608, 109));
    }

    @Override
    public String getRoleName() {
        return "Drums";
    }

    @Override
    protected AnimationResources obtainAnimationResources() {
        String characterName = "Charlie";
        HashMap<String, int[]> animations = new HashMap();
        // Imágenes de animación de batería por defecto
        int[] idleAnimation = new int[1];
        idleAnimation[0] = R.drawable.drums_idle_1;/*
        idleAnimation[1] = R.drawable.drums_idle_2;
        idleAnimation[2] = R.drawable.drums_idle_3;
        idleAnimation[3] = R.drawable.drums_idle_4;
        idleAnimation[4] = R.drawable.drums_idle_5;
        idleAnimation[5] = R.drawable.drums_idle_6;
        idleAnimation[6] = R.drawable.drums_idle_7;
        idleAnimation[7] = R.drawable.drums_idle_8;*/
        animations.put("Idle", idleAnimation);
        // Se devuelve la información para que AnimationController la almacene e interprete
        return new AnimationResources(characterName, animations);
    }
}
