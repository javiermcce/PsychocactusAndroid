package com.snake.graphics.device;

/**
 *
 * @author Javier Martinez
 */
public interface GameDevice extends Runnable {

    public boolean isRunning();

    public void setRunning(boolean value);

    public void initialize();

    public void finishOff();
}
