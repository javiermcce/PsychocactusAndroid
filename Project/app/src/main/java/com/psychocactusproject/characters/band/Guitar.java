package com.psychocactusproject.characters.band;

import android.view.View;

import com.psychocactusproject.R;
import com.psychocactusproject.interaction.menu.ContextMenu;
import com.psychocactusproject.manager.engine.GameEngine;
import com.psychocactusproject.manager.engine.Hitbox;
import com.psychocactusproject.manager.engine.Point;

import java.util.HashMap;

public class Guitar extends Musician {

    public Guitar(GameEngine gameEngine) {
        super(gameEngine);
        this.setPosition(new Point(489, 148));
    }

    @Override
    public String getRoleName() {
        return "Guitar";
    }

    @Override
    protected AnimationResources obtainAnimationResources() {
        String characterName = "Björk";
        HashMap<String, int[]> animations = new HashMap();
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
    public ContextMenu.MenuOption[] getMenuOptions() {
        ContextMenu.MenuOption[] options = new ContextMenu.MenuOption[4];
        options[0] = new ContextMenu.MenuOption("Smoke");
        options[1] = new ContextMenu.MenuOption("Spit");
        options[2] = new ContextMenu.MenuOption("Break Guitar");
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
}
