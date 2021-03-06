package com.psychocactusproject.characters.band;

import com.psychocactusproject.R;
import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.engine.GameLogic;
import com.psychocactusproject.engine.Hitbox;
import com.psychocactusproject.engine.Point;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Guitar extends Musician {

    private final static String FATIGUE_ACTION = "Smoke";
    private final static String FURY_ACTION = "Spit";
    private final static String FUN_ACTION = "Break Guitar";
    private final static String SOLO_ACTION = "Solo";
    private final static String PLAY_ACTION = "Play";
    // Debug
    private static boolean debugMusician = false;

    public Guitar(GameEngine gameEngine) {
        super(gameEngine, new String[] { FATIGUE_ACTION, FURY_ACTION, FUN_ACTION, SOLO_ACTION });
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
    protected boolean debuggingMusician() {
        return Guitar.debugMusician;
    }

    public static void debugDrumsSwitch() {
        Guitar.debugMusician = !Guitar.debugMusician;
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
