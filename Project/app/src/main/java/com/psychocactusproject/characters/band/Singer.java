package com.psychocactusproject.characters.band;

import android.view.View;

import com.psychocactusproject.R;
import com.psychocactusproject.interaction.menu.ContextMenu;
import com.psychocactusproject.manager.engine.GameEngine;
import com.psychocactusproject.manager.engine.Hitbox;
import com.psychocactusproject.manager.engine.Point;

import java.util.HashMap;

public class Singer extends Musician {

    public Singer(GameEngine gameEngine) {
        super(gameEngine);
        this.setPosition(new Point(668, 229));
    }

    @Override
    public String getRoleName() {
        return "Singer";
    }

    @Override
    protected AnimationResources obtainAnimationResources() {
        String characterName = "Thomasa";
        HashMap<String, int[]> animations = new HashMap();
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

        Hitbox[] idleHitbox = new Hitbox[3];
        Hitbox[] anotherHitbox = new Hitbox[3];

        Hitbox[][] hitboxes = new Hitbox[][] {idleHitbox, anotherHitbox};
        // Se devuelve la información para que AnimatedEntity la almacene e interprete
        return new AnimationResources(characterName, animations, hitboxes);
    }

    @Override
    public ContextMenu.MenuOption[] getMenuOptions() {
        ContextMenu.MenuOption[] options = new ContextMenu.MenuOption[4];
        options[0] = new ContextMenu.MenuOption("Pina Colada");
        options[1] = new ContextMenu.MenuOption("Scream");
        options[2] = new ContextMenu.MenuOption("Pogo");
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
