package com.psychocactusproject.characters.band;

import com.psychocactusproject.R;
import com.psychocactusproject.manager.engine.GameEngine;
import com.psychocactusproject.manager.engine.GameLogic;
import com.psychocactusproject.manager.engine.Hitbox;
import com.psychocactusproject.manager.engine.Point;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Bass extends Musician {

    // Lógica de juego
    private static final int NIRVANA_DURATION = 3;
    private int nirvana;

    public Bass(GameEngine gameEngine) {
        super(gameEngine, new String[] { "Puke", "Schizophrenia", "Dose", "Solo" });
        this.setPosition(new Point(765, 133));
        this.enableClickable(0);
    }

    @Override
    public String getRoleName() {
        return "Bass";
    }

    @Override
    public void onOptionSelected(String option) {
        switch (option) {
            case "Puke":
                this.fatigueAction();
                break;
            case "Schizophrenia":
                this.furyAction();
                break;
            case "Dose":
                this.funAction();
                break;
            case "Solo":
                this.solo();
                break;
            case "Play":
                this.play();
            default:
                throw new IllegalArgumentException("Se ha seleccionado una opción de menú " +
                        "que no existe.");
        }
    }

    @Override
    protected AnimationResources obtainAnimationResources() {
        String characterName = "Ronaldo";
        HashMap<String, int[]> animations = new HashMap<>();
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
        List<Hitbox> idleHitboxList = new LinkedList<>();
        idleHitboxList.add(new Hitbox(57, 0, 100, 24, this, 0));
        idleHitboxList.add(new Hitbox(0, 24, 100, 50, this, 0));
        idleHitboxList.add(new Hitbox(57, 50, 100, 100, this, 0));
        Hitbox[] idleHitbox = idleHitboxList.toArray(new Hitbox[] {});
        List<Hitbox> anotherHitboxList = new LinkedList<>();
        //
        Hitbox[] anotherHitbox = anotherHitboxList.toArray(new Hitbox[] {});
        Hitbox[][] hitboxes = new Hitbox[][] {idleHitbox/*, anotherHitbox*/};
        // Se devuelve la información para que AnimatedEntity la almacene e interprete
        return new AnimationResources(characterName, animations, hitboxes);
    }
    
    //
    @Override
    public void funAction() {
        super.fatigueAction();
        GameLogic.getInstance().getStateManager().dose();
    }

    @Override
    public void fatigueAction() {
        super.fatigueAction();
        GameLogic.getInstance().getStateManager().puke();
    }

    @Override
    public void furyAction() {
        super.furyAction();
        GameLogic.getInstance().getStateManager().schizophrenia();
    }

    @Override
    public void solo() {
        // throw new UnsupportedOperationException();
    }

    public boolean isAtNirvana() {
        return this.nirvana > 0;
    }

    @Override
    public void checkAndUpdate() {

    }
}
