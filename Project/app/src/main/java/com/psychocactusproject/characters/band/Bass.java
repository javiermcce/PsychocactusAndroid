package com.psychocactusproject.characters.band;

import android.view.View;

import com.psychocactusproject.R;
import com.psychocactusproject.engine.GameEngine;

import java.util.HashMap;

public class Bass extends Musician {

    public Bass(GameEngine gameEngine, View view) {
        super(gameEngine, view);
    }

    @Override
    public String getRoleName() {
        return "Bass";
    }

    @Override
    protected AnimationResources obtainAnimationResources() {
        String characterName = "Ronaldo";
        HashMap<String, int[]> animations = new HashMap();
        // Imágenes de animación de bajista por defecto
        int[] idleAnimation = new int[8];
        idleAnimation[0] = R.drawable.bass_idle_1;
        idleAnimation[1] = R.drawable.bass_idle_2;
        idleAnimation[2] = R.drawable.bass_idle_3;
        idleAnimation[3] = R.drawable.bass_idle_4;
        idleAnimation[4] = R.drawable.bass_idle_5;
        idleAnimation[5] = R.drawable.bass_idle_6;
        idleAnimation[6] = R.drawable.bass_idle_7;
        idleAnimation[7] = R.drawable.bass_idle_8;
        animations.put("Idle", idleAnimation);
        /*
        int[] list = new int[8];
        list[0] = R.drawable.bass_idle_1;
        list[1] = R.drawable.bass_idle_2;
        list[2] = R.drawable.bass_idle_3;
        list[3] = R.drawable.bass_idle_4;
        list[4] = R.drawable.bass_idle_5;
        list[5] = R.drawable.bass_idle_6;
        list[6] = R.drawable.bass_idle_7;
        list[7] = R.drawable.bass_idle_8;
        animations.put("action", list);
        * */
        // Se devuelve la información para que AnimationController la almacene e interprete
        return new AnimationResources(characterName, animations);
    }
}
