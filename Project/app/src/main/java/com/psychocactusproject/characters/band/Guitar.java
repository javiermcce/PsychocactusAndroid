package com.psychocactusproject.characters.band;

import com.psychocactusproject.R;
import com.psychocactusproject.manager.engine.GameEngine;
import com.psychocactusproject.manager.engine.Hitbox;
import com.psychocactusproject.manager.engine.Point;

import java.util.HashMap;

public class Guitar extends Musician {

    public Guitar(GameEngine gameEngine) {
        super(gameEngine, new String[] { "Smoke", "Spit", "Break Guitar", "Solo" });
        this.setPosition(new Point(489, 148));
    }

    @Override
    public String getRoleName() {
        return "Guitar";
    }

    @Override
    protected AnimationResources obtainAnimationResources() {
        String characterName = "Björk";
        HashMap<String, int[]> animations = new HashMap<>();
        // Imágenes de animación de guitarrista por defecto
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

        Hitbox[] idleHitbox = new Hitbox[3];
        Hitbox[] anotherHitbox = new Hitbox[3];

        Hitbox[][] hitboxes = new Hitbox[][] {idleHitbox, anotherHitbox};
        // Se devuelve la información para que AnimatedEntity la almacene e interprete
        return new AnimationResources(characterName, animations, hitboxes);
    }



    @Override
    public void onOptionSelected(String option) {
        switch (option) {
            case "Smoke":
                break;
            case "Spit":
                break;
            case "Break Guitar":
                break;
            case "Solo":
                break;
            default:
                throw new IllegalArgumentException("Se ha seleccionado una opción de menú " +
                        "que no existe.");
        }
    }
}
