package com.psychocactusproject.characters.band;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.psychocactusproject.graphics.controllers.ClickableAnimation;
import com.psychocactusproject.graphics.views.SurfaceGameView;
import com.psychocactusproject.interaction.menu.ContextMenu;
import com.psychocactusproject.interaction.scripts.TurnChecker;
import com.psychocactusproject.manager.engine.GameEngine;
import com.psychocactusproject.input.InputController;
import com.psychocactusproject.manager.engine.GameLogic;

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
    }

    // Equivalente a Player según la guía

    @Override
    public void initialize() {
        fatigue = 0;
        fury = 0;
        rage = 0;
        exhaust = 0;
        arrested = false;
        dead = false;
        turnsRemainingRescueing = 0;
        hitByStick = 0;
    }

    public void InitializeStates() {
    }

    @Override
    public void update(long elapsedMillis, GameEngine gameEngine) {
        InputController inputController = gameEngine.getInputController();
        // aquí debería tener un gestor de movimiento que funcione como un script en un
        // hilo independiente, input controller tiene poco que ver en esto, pero lo voy a
        // mantener hasta implementar mi propio código
        // this.setPositionX();
        // this.setPositionX(this.getPositionX() + 1);
        // this.setPositionY(this.getPositionY() + 1);
    }

    @Override
    public void draw(Canvas canvas) {
        this.getMatrix().reset();
        this.getMatrix().postTranslate((float) this.getPositionX(), (float) this.getPositionY());
        // Devuelve un Paint que brilla si el personaje puede ser usado
        Paint usedPaint = this.isAvailable(0) ? SurfaceGameView.getColorFilter() : null;
        canvas.drawBitmap(this.getSpriteImage(), this.getMatrix(), usedPaint);
        if (GameEngine.DEBUGGING) {
            canvas.drawText("fatigue: ", this.getPositionX(), this.getPositionY(), this.basicPaint);
            canvas.drawText("fury: ", this.getPositionX(), this.getPositionY(), this.basicPaint);
            canvas.drawText("rage remaining: ", this.getPositionX(), this.getPositionY(), this.basicPaint);
            canvas.drawText("exhaust remaining: ", this.getPositionX(), this.getPositionY(), this.basicPaint);
            // estados adicionales, solo activados si es seleccionado el botón de debug extendido
            if (GameEngine.verboseDebugging) {
                canvas.drawText("arrested: ", this.getPositionX(), this.getPositionY(), this.basicPaint);
                canvas.drawText("dead: ", this.getPositionX(), this.getPositionY(), this.basicPaint);
            }
        }
    }

    @Override
    public ContextMenu.MenuOption[] getMenuOptions() {
        ContextMenu.MenuOption[] options = new ContextMenu.MenuOption[4];
        for (int i = 0; i < this.getOptionNames().length; i++) {
            options[i] = new ContextMenu.MenuOption(this.getOptionNames()[i]);
        }
        return options;
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
        fatigue -= 2;
        fury += 2;
        if (fatigue < 0) {
            fatigue = 0;
        }
        if (fury >= MAX_FURY) {
            induceRage();
        }
    }

    public void furyAction() {
        fury -= 2;
        GameLogic.getInstance().getConcert().substractFun(2);
        if (fury < 0) {
            fury = 0;
        }
    }

    public void funAction() {
        fatigue += 2;
        GameLogic logic = GameLogic.getInstance();
        for  (Musician each : logic.getGameEntityManager().getAllMusicians()) {
            if (each.isReady()) {
                logic.getConcert().addFun(1);
            }
        }
        if (fatigue >= MAX_FATIGUE) {
            induceExhaust();
        }
    }

    public void induceExhaust() {
        fatigue = 0;
        exhaust = EXHAUST_DURATION;
    }

    public void induceRage() {
        fury = 0;
        rage = RAGE_DURATION;
    }

    public void punch() {
        fury += 2;
        if(fury >= MAX_FURY) {
            induceRage();
        }
    }

    public boolean isExhausted() {
        return exhaust > 0;
    }

    public boolean isEnraged() {
        return rage > 0;
    }

    public boolean isRescueing() {
        return turnsRemainingRescueing == 0;
    }

    public boolean isStunnedByStick() {
        return hitByStick == 0;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isArrested() {
        return arrested;
    }

    public int getFuryValue() {
        return fury;
    }

    public int getFatigueValue() {
        return fatigue;
    }

    public void arrest() {
        arrested = true;
    }

    public abstract void checkAndUpdate();
}
