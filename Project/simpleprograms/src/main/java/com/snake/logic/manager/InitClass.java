
package com.snake.logic.manager;

import com.snake.logic.controls.Keyboard;
import com.snake.logic.controls.Mouse;
import com.snake.graphics.device.GameFrame;
import com.snake.graphics.render.Rendering;
import com.snake.graphics.sound.SoundManager;

/**
 *
 * @author Javier Martinez
 */
public class InitClass {

    public static void main(String[] args) {

        // CREATING ROOT INSTANCES FOR THE GAME
        Rendering.getInstance();
        Mouse.getInstance();
        Keyboard.getInstance();
        SoundManager.getInstance();

        // INITIALIZING GAME
        GameFrame.getInstance().initialize();
    }
}
