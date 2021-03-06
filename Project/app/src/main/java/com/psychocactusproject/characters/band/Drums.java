package com.psychocactusproject.characters.band;

import com.psychocactusproject.R;
import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.engine.GameLogic;
import com.psychocactusproject.engine.Hitbox;
import com.psychocactusproject.engine.Point;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Drums extends Musician {

    private final static String FATIGUE_ACTION = "Sleep";
    private final static String FURY_ACTION = "Mom's Call";
    private final static String FUN_ACTION = "Throw Drumstick";
    private final static String SOLO_ACTION = "Solo";
    private final static String PLAY_ACTION = "Play";

    public Drums(GameEngine gameEngine) {
        super(gameEngine, new String[] { FATIGUE_ACTION, FURY_ACTION, FUN_ACTION, SOLO_ACTION });
        this.setPosition(new Point(608, 109));
    }

    @Override
    public String getRoleName() {
        return "Drums";
    }

    @Override
    protected AnimationResources obtainAnimationResources() {
        String characterName = "Charlie";
        HashMap<String, int[]> animations = new HashMap<>();
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
        //
        List<Hitbox> idleHitboxesList = new LinkedList<>();
        idleHitboxesList.add(new Hitbox(5, 0, 96, 51, this, 0));
        idleHitboxesList.add(new Hitbox(0, 51, 100, 100, this, 0));
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
            case FATIGUE_ACTION:
                this.fatigueAction();
                break;
            case FURY_ACTION:
                this.furyAction();
                break;
            case FUN_ACTION:
                this.funAction();
                break;
            case SOLO_ACTION:
                this.solo();
                break;
            case PLAY_ACTION:
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
        GameLogic.getInstance().getStateManager().throwStick();
    }

    @Override
    public void fatigueAction() {
        super.fatigueAction();
        GameLogic.getInstance().getStateManager().sleep();
    }

    @Override
    public void furyAction() {
        super.furyAction();
        GameLogic.getInstance().getStateManager().momsCall();
    }

    @Override
    public void solo() {
        // throw new UnsupportedOperationException();
    }

    @Override
    public void checkAndUpdate() {

    }
}
