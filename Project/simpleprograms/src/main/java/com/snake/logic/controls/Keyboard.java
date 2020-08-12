
package com.snake.logic.controls;

import com.snake.graphics.device.GameDevice;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

/**
 *
 * @author Javier Martinez
 */
public class Keyboard {
    // SINGLETON
    private static Keyboard instance = null;
    public static Keyboard getInstance() {
        if (instance == null) { 
            instance = new Keyboard(); 
        }
        return instance;
    }
    // VARIABLES
    private LinkedList<Integer> pressedKeys;
    // CONSTRUCTOR
    private Keyboard() {
        this.pressedKeys = new LinkedList<>();
    }
    // GET METHODS
    public int getLastPressed() {
        return !this.pressedKeys.isEmpty() ? this.pressedKeys.getLast() : -1;
    }
    public boolean isKeyPressed(int keyCode) {
        return this.pressedKeys.contains(keyCode);
    }
    public LinkedList<Integer> getList() {
        return this.pressedKeys;
    }
    /* LISTENERS */
    public void addButtonListeners(Component mode) {
        mode.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent key) {
                if (!isKeyPressed(key.getKeyCode())) {
                    pressedKeys.add(key.getKeyCode());
                }
                if ((key.getKeyCode() == KeyEvent.VK_C) && key.isControlDown()) {
                    GameDevice screen = (GameDevice) mode;
                    screen.setRunning(false);
                }
            }
            @Override
            public void keyReleased(KeyEvent key) {
                pressedKeys.remove((Integer)key.getKeyCode());
            }
            
        });/*
        // Prepare eventual shutdown
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                GameDevice screen = GameFrame.getInstance();
                screen.setRunning(false);
                screen.finishOff();
            }
        });*/
    }
}