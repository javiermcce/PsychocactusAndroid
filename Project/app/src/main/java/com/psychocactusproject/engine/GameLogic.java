package com.psychocactusproject.engine;

import com.psychocactusproject.interaction.scripts.Concert;
import com.psychocactusproject.interaction.scripts.StateManager;

public class GameLogic {

    public static GameLogic initialize(GameEngine gameEngine) {
        instance = new GameLogic(gameEngine);
        return instance;
    }

    public static GameLogic getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Se intenta obtener instancia nula de GameLogic. " +
                    "La instancia debe ser inicializada explícitamente.");
        }
        return instance;
    }

    private static GameLogic instance;


    // Incluir una referencia a gameEngine en GameLogic podría ser la manera de trabajar a partir
    // de ahora, y podría refactorizar el código que venga en base a este cambio
    private final GameEngine gameEngine;
    private final StateManager stateManager;
    private final GameEntityManager gameEntityManager;
    private final Concert concert;

    private GameLogic(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.stateManager = new StateManager();
        this.gameEntityManager = new GameEntityManager();
        this.concert = new Concert();
    }

    public StateManager getStateManager() {
        return this.stateManager;
    }

    public GameEntityManager getGameEntityManager() {
        return this.gameEntityManager;
    }

    public Concert getConcert() {
        return this.concert;
    }


    public GameEngine getGameEngine() {
        return this.gameEngine;
    }
}
