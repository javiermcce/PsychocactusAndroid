package com.psychocactusproject.engine;

import java.util.Timer;
import java.util.TimerTask;

public class GameClock {

    private final Timer timer;
    private int totalFrames;
    private int timestamp;
    private double period;
    private boolean usingReverse;
    private boolean reversing;

    // totalFrames -> número total de ciclos
    // period -> expresado en segundos
    public GameClock(int totalFrames, double period) {
        this.timer = new Timer();
        this.totalFrames = totalFrames;
        this.timestamp = 0;
        this.period = period;
        this.usingReverse = false;
        this.reversing = false;
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

    public void updateClock(double period, int totalFrames) {
        this.period = period;
        this.totalFrames = totalFrames;
        this.timestamp = 0;
        this.updateTask();
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
            }
        };
        TimerTask reversingTask = new TimerTask() {
            @Override
            public void run() {
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
}
