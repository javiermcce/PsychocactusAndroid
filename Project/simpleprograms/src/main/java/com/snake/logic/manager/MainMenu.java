
package com.snake.logic.manager;

/**
 *
 * @author Javier Martinez
 */
public class MainMenu {
    
    // SINGLETON
    private static MainMenu instance = null;

    public static MainMenu getInstance() {
        if (instance == null) instance = new MainMenu();
        return instance;
    }

    // CONSTRUCTOR
    private MainMenu() {
       
    }
}
