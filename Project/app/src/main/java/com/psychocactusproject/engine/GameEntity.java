package com.psychocactusproject.engine;

public abstract class GameEntity {

    public static final Object entitiesLock = new Object();

    public abstract void initialize();

    public abstract void update(GameEngine gameEngine);

    public abstract String getRoleName();

}
