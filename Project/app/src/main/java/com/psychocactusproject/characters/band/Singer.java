package com.psychocactusproject.characters.band;

import com.psychocactusproject.R;
import com.psychocactusproject.manager.engine.GameEngine;
import com.psychocactusproject.manager.engine.GameLogic;
import com.psychocactusproject.manager.engine.Hitbox;
import com.psychocactusproject.manager.engine.Point;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Singer extends Musician {

    private final static String FATIGUE_ACTION = "Pina Colada";
    private final static String FURY_ACTION = "Scream";
    private final static String FUN_ACTION = "Pogo";
    private final static String SOLO_ACTION = "Solo";
    private final static String PLAY_ACTION = "Play";

    public Singer(GameEngine gameEngine) {
        super(gameEngine, new String[] { FATIGUE_ACTION, FURY_ACTION, FUN_ACTION, SOLO_ACTION });
        this.setPosition(new Point(668, 229));
    }

    @Override
    public String getRoleName() {
        return "Singer";
    }

    @Override
    protected AnimationResources obtainAnimationResources() {
        String characterName = "Thomasa";
        HashMap<String, int[]> animations = new HashMap<>();
        // Imágenes de animación de cantante por defecto
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
        //
        List<Hitbox> idleHitboxesList = new LinkedList<>();
        idleHitboxesList.add(new Hitbox(23, 0, 56, 15, this, 0));
        idleHitboxesList.add(new Hitbox(15, 15, 100, 39, this, 0));
        idleHitboxesList.add(new Hitbox(0, 39, 87, 93, this, 0));
        idleHitboxesList.add(new Hitbox(6, 93, 75, 100, this, 0));
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
        GameLogic.getInstance().getStateManager().pogo();
    }

    @Override
    public void fatigueAction() {
        super.fatigueAction();
        GameLogic.getInstance().getStateManager().pinyaColada();
    }

    @Override
    public void furyAction() {
        super.furyAction();
        GameLogic.getInstance().getStateManager().scream();
    }

    @Override
    public void solo() {

    }

    @Override
    public void checkAndUpdate() {

    }
}
