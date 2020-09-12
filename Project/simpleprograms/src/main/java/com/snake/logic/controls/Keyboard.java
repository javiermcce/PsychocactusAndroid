
package com.snake.logic.controls;

import com.snake.graphics.device.GameDevice;
import com.snake.logic.entities.Snake;
import com.snake.logic.map.SnakeTable;

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
    /*public boolean isKeyPressed(char keyCode) {
        System.out.println(Integer.valueOf(keyCode));
        return this.pressedKeys.contains(Integer.valueOf(keyCode));
    }*/
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
                checkDirection(key.getKeyCode());
            }
            @Override
            public void keyReleased(KeyEvent key) {
                pressedKeys.remove((Integer)key.getKeyCode());
            }
            
        });
    }

    public LinkedList<Character> getCharacterList() {
        LinkedList<Character> characterList = new LinkedList<>();
        this.pressedKeys.forEach((charCode) -> {
            characterList.add((char) charCode.intValue());
        });
        return characterList;
    }

    public Snake.Direction getDirectionInput() {
        for (int i = this.pressedKeys.size() - 1; i >= 0 ; i--) {
            if(this.pressedKeys.get(i) == KeyEvent.VK_W) { return Snake.Direction.UP; }
            if(this.pressedKeys.get(i) == KeyEvent.VK_A) { return Snake.Direction.LEFT; }
            if(this.pressedKeys.get(i) == KeyEvent.VK_S) { return Snake.Direction.DOWN; }
            if(this.pressedKeys.get(i) == KeyEvent.VK_D) { return Snake.Direction.RIGHT; }
        }
        return null;
    }

    public void checkDirection(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_W:
                SnakeTable.getInstance().getSnake().setFutureHeading(Snake.Direction.UP);
                break;
            case KeyEvent.VK_A:
                SnakeTable.getInstance().getSnake().setFutureHeading(Snake.Direction.LEFT);
                break;
            case KeyEvent.VK_S:
                SnakeTable.getInstance().getSnake().setFutureHeading(Snake.Direction.DOWN);
                break;
            case KeyEvent.VK_D:
                SnakeTable.getInstance().getSnake().setFutureHeading(Snake.Direction.RIGHT);
                break;
        }
    }

}