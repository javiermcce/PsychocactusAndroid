package com.psychocactusproject.manager.engine;

import com.psychocactusproject.interaction.scripts.Concert;
import com.psychocactusproject.interaction.scripts.StateManager;

public class GameLogic {

    public static GameLogic initialize() {
        if (instance != null) {
            throw new IllegalStateException("Se intenta inicializar GameLogic. " +
                    "La instancia ya ha sido inicializada.");
        }
        instance = new GameLogic();
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

    private StateManager stateManager;
    private GameEntityManager gameEntityManager;
    private Concert concert;

    private GameLogic() {
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


}
