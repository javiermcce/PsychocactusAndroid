package com.psychocactusproject.characters.band;

import android.view.View;

import com.psychocactusproject.R;
import com.psychocactusproject.interaction.menu.ContextMenu;
import com.psychocactusproject.manager.engine.GameEngine;
import com.psychocactusproject.manager.engine.Hitbox;
import com.psychocactusproject.manager.engine.Point;

import java.util.HashMap;

public class Bass extends Musician {

    public Bass(GameEngine gameEngine) {
        super(gameEngine);
        this.setPosition(new Point(765, 133));
    }

    @Override
    public String getRoleName() {
        return "Bass";
    }

    @Override
    public ContextMenu.MenuOption[] getMenuOptions() {
        ContextMenu.MenuOption[] options = new ContextMenu.MenuOption[4];
        options[0] = new ContextMenu.MenuOption("Puke");
        options[1] = new ContextMenu.MenuOption("Taunt");
        options[2] = new ContextMenu.MenuOption("Dose");
        options[3] = new ContextMenu.MenuOption("Solo");
        return options;
    }

    @Override
    public void onOptionSelected(ContextMenu.MenuOption option) {
        ContextMenu.MenuOption[] options = this.getMenuOptions();
        if (option.optionName.equals(options[0].optionName)) {

        } else if (option.optionName.equals(options[1].optionName)) {

        } else if (option.optionName.equals(options[2].optionName)) {

        } else if (option.optionName.equals(options[3].optionName)) {

        }
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

        Hitbox[] idleHitbox = new Hitbox[3];
        idleHitbox[0] = new Hitbox(57, 0, 100, 24, this, 0);
        idleHitbox[1] = new Hitbox(0, 24, 100, 50, this, 0);
        idleHitbox[2] = new Hitbox(57, 50, 100, 100, this, 0);
        Hitbox[] anotherHitbox = new Hitbox[3];

        Hitbox[][] hitboxes = new Hitbox[][] {idleHitbox, anotherHitbox};
        // Se devuelve la información para que AnimationController la almacene e interprete
        return new AnimationResources(characterName, animations, hitboxes);
    }
}
