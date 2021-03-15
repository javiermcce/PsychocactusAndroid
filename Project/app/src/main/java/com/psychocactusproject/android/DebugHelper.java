package com.psychocactusproject.android;

import android.graphics.Canvas;

import com.psychocactusproject.R;
import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.engine.Hitbox;
import com.psychocactusproject.engine.Point;
import com.psychocactusproject.graphics.controllers.ClickableDirectSprite;
import com.psychocactusproject.graphics.controllers.ClickableSprite;
import com.psychocactusproject.graphics.controllers.CustomDrawable;
import com.psychocactusproject.graphics.controllers.CustomEntity;

import java.util.HashMap;

public class DebugHelper {

    // RECORDAR REVISAR Y LIMPIAR LOS EFECTOS NO DESEADOS DEL COMMIT 7d7cf63

    private String commandLine;
    private GameEngine gameEngine;

    public DebugHelper(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.addEnterCommandButton();
    }

    private void addEnterCommandButton() {
        // Debug button
        Runnable enterCommandAction = () -> { this.executeCommand(); };
        ClickableDirectSprite commandDebug = new ClickableDirectSprite(gameEngine,
                R.drawable.debug_enter, "Debug enter command",
                enterCommandAction, new Point(400, 200));
        gameEngine.addGameEntity(commandDebug, GameEngine.CHARACTER_LAYERS.FRONT);

        CustomDrawable test = (Canvas canvas) -> {};
        CustomEntity customEntity = new CustomEntity(test);
        // test.draw(canvas);
    }

    public String getCommandLine() {
        return this.commandLine;
    }

    public void executeCommand() {
        this.commandLine = "";
    }

    public void sendToTerminal(char key) {
        this.commandLine += key;
    }

    public static void printMessage(String message) {
        System.out.println("JAVIER: " + message);
    }
}
