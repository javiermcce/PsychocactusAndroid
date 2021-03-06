package com.psychocactusproject.interaction.scripts;

import com.psychocactusproject.characters.band.Musician;
import com.psychocactusproject.engine.GameLogic;

import java.util.LinkedList;

public class Concert implements TurnChecker {
    
    public final int TOTAL_TURNS = 20;
    public final int TURNS_PER_PHASE = 5;
    public final int MAX_FUN_VALUE = 10;
    public final int INITIAL_FUN = 5;
    private int fun;
    private int remainingTurns;
    private int phase;
    private boolean awaitingInput;
    private LinkedList<StateAnimations> pendingAnimations;
    public enum StateAnimations {
        
    }

    public Concert() {
        this.fun = INITIAL_FUN;
        this.remainingTurns = TOTAL_TURNS;
        this.phase = 0;
    }

    public boolean funIsZero() {
        return this.fun == 0;
    }

    public boolean funIsHigh() {
        return this.fun >= 9;
    }

    public boolean funAtMaxValue() {
        return this.fun == MAX_FUN_VALUE;
    }

    public void addFun(int value) {
        setFun(value, true);
    }

    public void substractFun(int value) {
        setFun(value, false);
    }

    private void setFun(int value, boolean adding) {
        if (value <= 0) {
            throw new IllegalArgumentException("El parámetro obtenido ha sido de " + value + ". " +
                    "No puede ser menor que 0.");
        }
        if (adding) {
            this.fun += value;
        } else {
            this.fun -= value;
        }
        if (this.fun <= 0) {
            this.fun = 0;
        }
        if (this.fun >= MAX_FUN_VALUE) {
            this.fun = MAX_FUN_VALUE;
        }
    }

    public boolean isAwaitingInput() {
        return this.awaitingInput;
    }

    public StateAnimations getNextAnimation() {
        return this.pendingAnimations.pollFirst();
    }

    public void proceedNextTurn() {
        this.remainingTurns--;
        if(this.remainingTurns % TURNS_PER_PHASE == 0) {
            this.phase++;
        }
    }

    public boolean checkLossConditions() {
        boolean gameLost = false;
        boolean noMusicianReady = true;
        for (Musician each :GameLogic.getInstance().getGameEntityManager().getAllMusicians())
        {
            noMusicianReady = noMusicianReady && !each.isReady();
        }
        gameLost = gameLost || noMusicianReady;
        gameLost = gameLost || funIsZero();
        return gameLost;
    }

    public void checkAndUpdate() {

    }
}
