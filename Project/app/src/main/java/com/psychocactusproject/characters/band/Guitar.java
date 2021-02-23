package com.psychocactusproject.characters.band;

import com.psychocactusproject.R;
import com.psychocactusproject.manager.engine.GameEngine;
import com.psychocactusproject.manager.engine.GameLogic;
import com.psychocactusproject.manager.engine.Hitbox;
import com.psychocactusproject.manager.engine.Point;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Guitar extends Musician {

    public Guitar(GameEngine gameEngine) {
        super(gameEngine, new String[] { "Smoke", "Spit", "Break Guitar", "Solo" });
        this.setPosition(new Point(489, 148));
        this.enableClickable(0);
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
        //
        List<Hitbox> idleHitboxesList = new LinkedList<>();
        idleHitboxesList.add(new Hitbox(14, 0, 55, 22, this, 0));
        idleHitboxesList.add(new Hitbox(3, 22, 58, 51, this, 0));
        idleHitboxesList.add(new Hitbox(8, 51, 63, 100, this, 0));
        idleHitboxesList.add(new Hitbox(57, 26, 100, 47, this, 0));
        Hitbox[] idleHitbox = idleHitboxesList.toArray(new Hitbox[] {});
        List<Hitbox> anotherHitboxesList = new LinkedList<>();
        //
        Hitbox[] anotherHitbox = anotherHitboxesList.toArray(new Hitbox[] {});
        Hitbox[][] hitboxes = new Hitbox[][] {idleHitbox/*, anotherHitbox*/};
        // Se devuelve la información para que AnimatedEntity la almacene e interprete
        return new AnimationResources(characterName, animations, hitboxes);
    }



    @Override
    public void onOptionSelected(String option) {
        switch (option) {
            case "Smoke":
                this.fatigueAction();
                break;
            case "Spit":
                this.furyAction();
                break;
            case "Break Guitar":
                this.funAction();
                break;
            case "Solo":
                this.solo();
                break;
            case "Play":
                this.play();
                break;
            default:
                throw new IllegalArgumentException("Se ha seleccionado una opción de menú " +
                        "que no existe.");
        }
    }

    @Override
    public void funAction() {
        super.fatigueAction();
        GameLogic.getInstance().getStateManager().breakGuitar();
    }

    @Override
    public void fatigueAction() {
        super.fatigueAction();
        GameLogic.getInstance().getStateManager().smoke();
    }

    @Override
    public void furyAction() {
        super.furyAction();
        GameLogic.getInstance().getStateManager().spit();
    }

    @Override
    public void solo() {

    }

    @Override
    public void checkAndUpdate() {

    }
}
