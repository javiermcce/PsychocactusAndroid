package com.psychocactusproject.engine;

import java.util.Timer;
import java.util.TimerTask;

public class GameClock {

    private final Timer timer;
    private int totalFrames;
    private int timestamp;
    private double period;

    public GameClock(int initFrames, int period) {
        this.timer = new Timer();
        this.totalFrames = initFrames;
        this.timestamp = 0;
        this.period = period;
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
        this.timer.cancel();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                timestamp = (timestamp + 1) % totalFrames;
            }
        };
        this.timer.scheduleAtFixedRate(task, 0, (long)(this.period * 1000 / totalFrames));
    }
}
