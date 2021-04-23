package com.psychocactusproject.interaction.scripts;

import com.psychocactusproject.characters.band.Musician;
import com.psychocactusproject.engine.GameEntityManager;
import com.psychocactusproject.engine.GameEntityManager.MusicianTypes;
import com.psychocactusproject.engine.GameLogic;

import java.util.LinkedList;
import java.util.List;

public class StateManager {

    // 
    private final static int MAX_CHARGE_LOAD = 4;
    private final static int PUKES_BEFORE_OPEN = 3;
    private final static int MAX_AMP_ENDURANCE = 2;
    private final static int MAX_FIGHTS = 5;
    private final static int MAX_CABLE_ENDURANCE = 6;
    private final static int TURNS_TO_END_FIGHT = 4;
    private final static int MAX_FISSURE_DAMAGE = 5;
    private int batteryCharges;
    private boolean alcoholOnStage;
    private boolean alcoholOnFloor;
    private boolean fireOnStage;
    private boolean fireOnFloor;
    private int holeDamage;
    private int[] ampsEndurance;
    private int[] fightsInPlace;
    private int corpsesOnFloor;
    private int cableEndurance;
    private int fissureDamage;

    private List<TurnChecker> updatableEntities;

    public StateManager() {
        this.updatableEntities = new LinkedList<>();
        this.ampsEndurance = new int[MusicianTypes.values().length];
        for (int i = 0; i < this.ampsEndurance.length; i++) {
            this.ampsEndurance[i] = MAX_AMP_ENDURANCE;
        }
        this.fightsInPlace = new int[MAX_FIGHTS];
        for (int i = 0; i < MAX_FIGHTS; i++) {
            this.fightsInPlace[i] = -1;
        }
    }

    public void addUpdatableEntity(TurnChecker entity) {
        boolean success = this.updatableEntities.add(entity);
        if (!success) {
            throw new IllegalStateException("No ha habido éxito al intentar insertar una entidad en StateManager.");
        }
    }

    public void removeUpdatableEntity(TurnChecker entity) {
        boolean success = this.updatableEntities.remove(entity);
        if (!success) {
            throw new IllegalStateException("No ha habido éxito al intentar borrar una entidad de StateManager.");
        }
    }

    public boolean bandReady() {
        for (Musician each : GameLogic.getInstance().getGameEntityManager().getAllMusicians()) {
            if (!each.isReady()) {
                return false;
            }
        }
        return true;
    }

    public boolean chargeOverload() {
        return this.batteryCharges == MAX_CHARGE_LOAD;
    }

    public boolean cableBroken() {
        return this.cableEndurance == MAX_CABLE_ENDURANCE;
    }

    public boolean noBatteryCharge() {
        return this.batteryCharges == 0;
    }

    public void addBatteryCharge() {
        this.batteryCharges++;
        if (this.batteryCharges > MAX_CHARGE_LOAD) {
            this.batteryCharges = MAX_CHARGE_LOAD;
        }
    }

    public void substractBatteryCharge() {
        this.batteryCharges--;
        if (this.batteryCharges < 0) {
            throw new IllegalStateException("Se ha intentado restar una unidad de carga de batería. " +
                    "La batería está vacía.");
        }
    }

    public void damageAmplifier(Musician musician) {
        int musicianIndex = GameEntityManager.musicianToOrdinal(musician);
        this.ampsEndurance[musicianIndex]--;
        if (this.ampsEndurance[musicianIndex] <= 0) {
            this.ampsEndurance[musicianIndex] = 0;
            musician.induceExhaust();
        }
    }

    public boolean isAmplifierDamaged(Musician musician) {
        return this.ampsEndurance[GameEntityManager.musicianToOrdinal(musician)] <= 0;
    }

    public boolean holeOpen() {
        return this.holeDamage == PUKES_BEFORE_OPEN;
    }

    public void takeLastCorpse() {
        if (this.corpsesOnFloor == 0) {
            throw new IllegalStateException("Se ha intentado sacar un cuerpo. No hay más cuerpos en el suelo.");
        }
        this.corpsesOnFloor--;
    }

    public boolean corpseOnConcert() {
        return this.corpsesOnFloor > 0;
    }

    public void damageWalls() {
        this.fissureDamage++;
        if (this.fissureDamage >= MAX_FISSURE_DAMAGE) {
            this.fissureDamage = MAX_FISSURE_DAMAGE;
        }
    }

    public void updateEntities() {
        for (TurnChecker each : this.updatableEntities) {
            each.checkAndUpdate();
        }
    }

    // Bass
    public void puke() {
        this.holeDamage++;
        if (this.holeDamage > PUKES_BEFORE_OPEN) {
            this.holeDamage = PUKES_BEFORE_OPEN;
        }
    }

    public void schizophrenia() {

    }

    public void dose() {

    }

    // Guitar
    public void smoke() {
        if (this.alcoholOnStage) {
            this.fireOnStage = true;
        }
    }

    public void spit() {
        for(int i = 0; i < MAX_FIGHTS; i++) {
            if(this.fightsInPlace[i] == -1) {
                this.fightsInPlace[i] = TURNS_TO_END_FIGHT;
                return;
            }
        }
    }

    public void breakGuitar() {

    }

    // Singer
    public void pinyaColada() {
        this.alcoholOnStage = true;
    }

    public void scream() {
        this.damageWalls();
    }

    public void pogo() {

    }

    // Drums
    public void sleep() {

    }

    public void momsCall() {

    }

    public void throwStick() {

    }
}
