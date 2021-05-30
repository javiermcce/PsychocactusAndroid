package com.psychocactusproject.engine.util;

import java.util.Timer;
import java.util.TimerTask;

public class GameClock {

    private final Timer timer;
    private int totalFrames;
    private int timestamp;
    private double period;
    private boolean usingReverse;
    private boolean reversing;
    private boolean firstFractionCall;

    // totalFrames -> número total de ciclos
    // period -> expresado en segundos

    /**
     *
     * @param totalFrames tiempo que dura un ciclo completo
     * @param period expresado en segundos
     */
    public GameClock(int totalFrames, double period) {
        this.timer = new Timer();
        this.totalFrames = totalFrames;
        this.timestamp = 0;
        this.period = period;
        this.usingReverse = false;
        this.reversing = false;
        this.firstFractionCall = false;
        this.updateTask();
    }

    public GameClock(int totalFrames, double period, boolean usingReverse) {
        this.timer = new Timer();
        this.totalFrames = totalFrames;
        this.timestamp = 0;
        this.period = period;
        this.usingReverse = usingReverse;
        this.reversing = false;
        this.updateTask();
    }
    
    public int getTimestamp() {
        return this.timestamp;
    }

    public int getTotalFrames() {
        return this.totalFrames;
    }

    public void updateClock(double period, int totalFrames) {
        this.period = period;
        this.totalFrames = totalFrames;
        this.timestamp = 0;
        this.updateTask();
    }

    /**
     * El objetivo es llamar tantas veces como la velocidad de proceso permita, pero que solo la
     * primera vez de cada fracción devolverá verdadero, con lo que garantizamos un máximo de
     * acciones por ciclo
     *
     * <p>Se debe tener en cuenta que si el reloj es actualizado más rápido de la
     * velocidad con la que se hace una llamada, se devolverá verdadero menos fracciones por ciclo
     * a este método,
     * @return verdadero si este método es llamado por primera vez durante una fracción
     */
    public boolean isFirstFractionCall() {
        if (this.firstFractionCall) {
            this.firstFractionCall = false;
            return true;
        }
        return this.firstFractionCall;
    }

    private void updateTask() {
        // Tengo que revisar la implementación del timer, porque parece ser que no puedo
        // asignar una nueva tarea sin destrozar por completo el objeto

        //this.timer.cancel();
        //this.timer.purge();
        TimerTask regularTask = new TimerTask() {
            @Override
            public void run() {
                timestamp = (timestamp + 1) % totalFrames;
                firstFractionCall = true;
            }
        };
        TimerTask reversingTask = new TimerTask() {
            @Override
            public void run() {
                firstFractionCall = true;
                if (!reversing) {
                    timestamp++;
                    if (timestamp >= totalFrames - 1) {
                        reversing = true;
                    }
                } else {
                    timestamp--;
                    if (timestamp <= 0) {
                        reversing = false;
                    }
                }
            }
        };
        long periodArgument = (long)(this.period * 1000 / totalFrames);
        TimerTask selectedTask;
        if (!usingReverse) {
            selectedTask = regularTask;
        } else {
            selectedTask = reversingTask;
        }
        this.timer.scheduleAtFixedRate(selectedTask, 0, periodArgument);
    }


    /**
     * Schedules the specified task for execution after the specified delay.
     *
     * @param task la tarea que se desea ejecutar
     * @param delay el tiempo en milisegundos antes de ejecutar la tarea
     */
    public static void scheduleTask(long delay, Runnable task) {

        Timer loadingTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                task.run();
            }
        };
        loadingTimer.schedule(timerTask, delay);
    }
}
