package com.psychocactusproject.characters.band;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.psychocactusproject.graphics.controllers.ClickableAnimation;
import com.psychocactusproject.graphics.views.SurfaceGameView;
import com.psychocactusproject.interaction.menu.ContextMenu;
import com.psychocactusproject.interaction.scripts.TurnChecker;
import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.input.InputController;
import com.psychocactusproject.engine.GameLogic;

public abstract class Musician extends ClickableAnimation implements TurnChecker {

    private GameEngine gameEngine;

    // Lógica de juego
    private final int RAGE_DURATION = 3;
    private final int EXHAUST_DURATION = 3;
    private final int RESCUEING_LOST_COMPANION = 5;
    private final int MAX_FATIGUE = 3;
    private final int MAX_FURY = 3;
    // protected final String[] skillNames; // los skill names corresponden a getOptionNames()
    protected int fatigue;
    protected int fury;
    protected int rage;
    protected int exhaust;
    protected boolean arrested;
    protected boolean dead;
    private int timesExhausted;
    private int turnsRemainingRescueing;
    private int hitByStick;
    // Debug
    private Paint basicPaint;
    

    protected Musician(GameEngine gameEngine, String[] optionNames) {
        super(gameEngine, optionNames);
        this.gameEngine = gameEngine;
        this.basicPaint = new Paint();
        this.basicPaint.setTextSize(30);
        this.basicPaint.setFakeBoldText(true);
        this.basicPaint.setColor(Color.WHITE);
    }

    private static String[] debugArrayAppend(String[] regularArray) {
        String[] debugArray = new String[regularArray.length + 1];
        for (int i = 0; i < regularArray.length; i++) {
            debugArray[i] = regularArray[i];
        }
        debugArray[debugArray.length - 1] = "Debug musician";
        return debugArray;
    }

    @Override
    public void initialize() {
        this.fatigue = 0;
        this.fury = 0;
        this.rage = 0;
        this.exhaust = 0;
        this.arrested = false;
        this.dead = false;
        this.turnsRemainingRescueing = 0;
        this.hitByStick = 0;
    }

    public void InitializeStates() {
    }

    @Override
    public void update(GameEngine gameEngine) {

    }

    @Override
    public void draw(Canvas canvas) {
        this.getMatrix().reset();
        this.getMatrix().postTranslate((float) this.getPositionX(), (float) this.getPositionY());
        // Devuelve un Paint que brilla si el personaje puede ser usado
        Paint usedPaint = this.isAvailable(0) ? SurfaceGameView.getColorFilter() : null;
        canvas.drawBitmap(this.getSpriteImage(), this.getMatrix(), usedPaint);
    }

    @Override
    public void debugDraw(Canvas canvas) {
        if (GameEngine.DEBUGGING && this.debuggingMusician()) {
            canvas.drawText("fatigue: " + this.getFatigueValue(), this.getPositionX(), this.getPositionY(), this.basicPaint);
            canvas.drawText("fury: " + this.getFuryValue(), this.getPositionX(), this.getPositionY() + 30, this.basicPaint);
            canvas.drawText("rage remaining: " + this.rageRemaining(), this.getPositionX(), this.getPositionY() + 60, this.basicPaint);
            canvas.drawText("exhaust remaining: " + this.exhaustRemaining(), this.getPositionX(), this.getPositionY() + 90, this.basicPaint);
            // estados adicionales, solo activados si es seleccionado el botón de debug extendido
            if (GameEngine.verboseDebugging) {
                canvas.drawText("arrested: " + this.isArrested(), this.getPositionX(), this.getPositionY() + 120, this.basicPaint);
                canvas.drawText("dead: " + this.isDead(), this.getPositionX(), this.getPositionY() + 150, this.basicPaint);
            }
        }
    }

    protected abstract boolean debuggingMusician();

    @Override
    public ContextMenu.MenuOption[] createMenuOptions() {
        ContextMenu.MenuOption[] options = new ContextMenu.MenuOption[this.getOptionNames().length];
        for (int i = 0; i < this.getOptionNames().length; i++) {
            options[i] = new ContextMenu.MenuOption(this.getOptionNames()[i]);
        }
        return options;
    }

    @Override
    public boolean isReadyForAction() {
        // IMPLEMENTAR!!!
        return true;
    }
    
    // Lógica de juego

    private void awake() {

    }

    public boolean isReady() {
        return !isArrested() && !isDead() && !isRescueing() && !isStunnedByStick();
    }

    public void play() {
        GameLogic logic = GameLogic.getInstance();
        for (Musician each : logic.getGameEntityManager().getAllMusicians()) {
            if(!each.isReady()) {
                throw new IllegalStateException("Se intenta ejecutar la acción Play. " +
                        "No todos los músicos están listos.");
            }
        }
        if (logic.getStateManager().noBatteryCharge()) {
            throw new IllegalStateException(
                    "Se intenta ejecutar la acción Play. No queda batería.");
        }
        logic.getConcert().proceedNextTurn();
        logic.getConcert().substractFun(4);
        logic.getStateManager().substractBatteryCharge();
        logic.getStateManager().damageAmplifier(logic.getGameEntityManager().getRandomMusician());
    }

    public abstract void solo();

    public void fatigueAction() {
        this.fatigue -= 2;
        this.fury += 2;
        if (this.fatigue < 0) {
            this.fatigue = 0;
        }
        if (this.fury >= MAX_FURY) {
            induceRage();
        }
    }

    public void furyAction() {
        this.fury -= 2;
        GameLogic.getInstance().getConcert().substractFun(2);
        if (this.fury < 0) {
            this.fury = 0;
        }
    }

    public void funAction() {
        this.fatigue += 2;
        GameLogic logic = GameLogic.getInstance();
        for  (Musician each : logic.getGameEntityManager().getAllMusicians()) {
            if (each.isReady()) {
                logic.getConcert().addFun(1);
            }
        }
        if (this.fatigue >= MAX_FATIGUE) {
            this.induceExhaust();
        }
    }

    public void induceExhaust() {
        this.fatigue = 0;
        this.exhaust = EXHAUST_DURATION;
    }

    public void induceRage() {
        this.fury = 0;
        this.rage = RAGE_DURATION;
    }

    public void punch() {
        this.fury += 2;
        if(this.fury >= MAX_FURY) {
            this.induceRage();
        }
    }

    public boolean isExhausted() {
        return this.exhaust > 0;
    }

    public int exhaustRemaining() {
        return this.exhaust;
    }

    public boolean isEnraged() {
        return this.rage > 0;
    }

    public int rageRemaining() {
        return this.rage;
    }

    public boolean isRescueing() {
        return this.turnsRemainingRescueing == 0;
    }

    public boolean isStunnedByStick() {
        return this.hitByStick == 0;
    }

    public boolean isDead() {
        return this.dead;
    }

    public boolean isArrested() {
        return this.arrested;
    }

    public int getFuryValue() {
        return this.fury;
    }

    public int getFatigueValue() {
        return this.fatigue;
    }

    public void arrest() {
        this.arrested = true;
    }

    @Override
    public void checkAndUpdate() {

    }
}
