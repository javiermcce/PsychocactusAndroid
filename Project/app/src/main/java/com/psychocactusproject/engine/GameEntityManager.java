package com.psychocactusproject.engine;

import com.psychocactusproject.R;
import com.psychocactusproject.characters.audience.ShotgunMan;
import com.psychocactusproject.characters.band.Bass;
import com.psychocactusproject.characters.band.Drums;
import com.psychocactusproject.characters.band.Guitar;
import com.psychocactusproject.characters.band.Musician;
import com.psychocactusproject.characters.band.Singer;
import com.psychocactusproject.characters.barry.Barry;
import com.psychocactusproject.characters.police.Police;
import com.psychocactusproject.graphics.controllers.AbstractSprite;
import com.psychocactusproject.graphics.controllers.ClickableSprite;
import com.psychocactusproject.graphics.controllers.InanimateSprite;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GameEntityManager {

    private List<AbstractSprite> entityManagerList;
    private Musician[] musicians;
    private Police[] police;
    private Barry barry;
    private ShotgunMan shotgunMan;

    public enum MusicianTypes {
        BASS, GUITAR, SINGER, DRUMS
    }


    public GameEntityManager() {
        this.entityManagerList = new LinkedList<>();
    }

    public void populate(GameEngine gameEngine) {
        // Escenario
        InanimateSprite stage = new InanimateSprite(gameEngine, R.drawable.background_stage, "Background Stage");
        stage.resizeBitmap(GameEngine.RESOLUTION_X, GameEngine.RESOLUTION_Y);
        this.entityManagerList.add(stage);
        // Músicos
        this.musicians = new Musician[4];
        this.musicians[0] = new Drums(gameEngine);
        this.musicians[1] = new Bass(gameEngine);
        this.musicians[2] = new Guitar(gameEngine);
        this.musicians[3] = new Singer(gameEngine);
        this.entityManagerList.addAll(Arrays.asList(musicians));
        /*
        // Policía
        this.police = new Police[3];
        this.police[0] = new CorruptAgent(gameEngine);
        this.police[1] = new PartyAgent(gameEngine);
        this.police[2] = new ViolentAgent(gameEngine);
        // this.entityList.addAll(Arrays.asList(this.police));
        // Barry
        this.barry = new Barry(gameEngine);
        // this.entityList.add(this.barry);
        // Público
        this.shotgunMan = new ShotgunMan(gameEngine);
        // this.entityList.add(this.shotgunMan);
         */
        // Debug
        // Con este boton se podrá acceder a todas las opciones de debug, con acciones indicadas a través de un hashmap runnable
        // A saber si esto es un poco un invento raro
        // Debug button
        Hitbox[] debugGeneralHitbox = new Hitbox[1];
        HashMap<String, Runnable> actions = new HashMap<>();
        actions.put("Verbose debug", () -> { GameEngine.verboseDebugging = !GameEngine.verboseDebugging; });
        ClickableSprite generalDebug = new ClickableSprite(gameEngine,
                InanimateSprite.NOT_SPECIFIED, R.drawable.debug_button, "Debug button",
                debugGeneralHitbox, actions, () -> { return GameEngine.DEBUGGING; });
        debugGeneralHitbox[0] = new Hitbox(generalDebug, 0);
        this.entityManagerList.add(generalDebug);
        // Debug button musicians
        Hitbox[] debugMusiciansHitbox = new Hitbox[1];
        HashMap<String, Runnable> musicianActions = new HashMap<>();
        musicianActions.put("Debug Singer", () -> { Singer.debugSingerSwitch(); });
        musicianActions.put("Debug Bass", () -> { Bass.debugBassSwitch(); });
        musicianActions.put("Debug Guitar", () -> { Guitar.debugGuitarSwitch(); });
        musicianActions.put("Debug Drums", () -> { Drums.debugDrumsSwitch(); });
        ClickableSprite musiciansDebug = new ClickableSprite(gameEngine,
                InanimateSprite.NOT_SPECIFIED, R.drawable.debug_button_musicians, "Debug musicians",
                debugMusiciansHitbox, musicianActions, () -> { return GameEngine.DEBUGGING; },
                new Point(0, generalDebug.getSpriteHeight()));
        debugMusiciansHitbox[0] = new Hitbox(musiciansDebug, 0);
        this.entityManagerList.add(musiciansDebug);
        // Inserción en el motor
        for (GameEntity entity : this.entityManagerList) {
            gameEngine.addGameEntity(entity, GameEngine.GAME_LAYERS.UNSPECIFIED);
        }
    }

    public Musician getRandomMusician() {
        return getAllMusicians()[(int) (Math.random() * this.musicians.length)];
    }

    public Musician[] getAllMusicians() {
        return this.musicians;
    }

    public Police[] getAllPolice() {
        return this.police;
    }

    public Barry getBarry() {
        return this.barry;
    }

    public ShotgunMan getShotgunMan() {
        return this.shotgunMan;
    }

    public static MusicianTypes getMusicianType(Musician musician) {
        if (musician instanceof Bass) {
            return MusicianTypes.BASS;
        } else if (musician instanceof Guitar) {
            return MusicianTypes.GUITAR;
        } else if (musician instanceof Singer) {
            return MusicianTypes.SINGER;
        } else if (musician instanceof Drums) {
            return MusicianTypes.DRUMS;
        } else {
            throw new IllegalArgumentException("El musico no corresponde a ninguna categoría conocida.");
        }
    }

    public static int musicianToOrdinal(Musician musician) {
        return GameEntityManager.getMusicianType(musician).ordinal();
    }
}
