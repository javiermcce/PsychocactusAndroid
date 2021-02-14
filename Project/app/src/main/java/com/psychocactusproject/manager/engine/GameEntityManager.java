package com.psychocactusproject.manager.engine;

import com.psychocactusproject.R;
import com.psychocactusproject.characters.band.Bass;
import com.psychocactusproject.characters.band.Drums;
import com.psychocactusproject.characters.band.Guitar;
import com.psychocactusproject.characters.band.Singer;
import com.psychocactusproject.graphics.controllers.AbstractSprite;
import com.psychocactusproject.graphics.controllers.InanimateSprite;

import java.util.LinkedList;
import java.util.List;

public class GameEntityManager {

    List<AbstractSprite> entityList;

    public GameEntityManager() {
        this.entityList = new LinkedList<>();
    }

    public void populate(GameEngine gameEngine) {
        // TEST
        InanimateSprite stage = new InanimateSprite(gameEngine, R.drawable.background_stage, "Background Stage");
        stage.resizeBitmap(GameEngine.RESOLUTION_X, GameEngine.RESOLUTION_Y);
        this.entityList.add(stage);
        this.entityList.add(new Bass(gameEngine));
        this.entityList.add(new Guitar(gameEngine));
        this.entityList.add(new Drums(gameEngine));
        this.entityList.add(new Singer(gameEngine));
        for (AbstractSprite entity : this.entityList) {
            gameEngine.addGameEntity(entity);
        }


    }
}
