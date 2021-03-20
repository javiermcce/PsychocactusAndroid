package com.psychocactusproject.android;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

import com.psychocactusproject.R;
import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.engine.Point;
import com.psychocactusproject.graphics.controllers.ClickableDirectSprite;
import com.psychocactusproject.graphics.controllers.Drawable;
import com.psychocactusproject.graphics.controllers.DrawableEntity;

import java.util.HashMap;
import java.util.LinkedList;

public class DebugHelper {

    // RECORDAR REVISAR Y LIMPIAR LOS EFECTOS NO DESEADOS DEL COMMIT 7d7cf63

    private String commandLine;
    private GameEngine gameEngine;
    private DrawableEntity debugTerminal;
    private HashMap<String, Runnable> commands;
    private String hiddenCommandInput;

    public DebugHelper(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        // this.addEnterCommandButton();
        this.addGameTerminal();
        this.commandLine = "";
        this.commands = new HashMap<>();
        this.hiddenCommandInput = "";
        this.initializeCommands();
    }

    private void addEnterCommandButton() {
        // Debug button
        Runnable enterCommandAction = () -> { this.executeCommand(); };
        ClickableDirectSprite commandDebug = new ClickableDirectSprite(gameEngine,
                R.drawable.debug_enter, "Debug enter command",
                enterCommandAction, new Point(400, 200));
        gameEngine.addGameEntity(commandDebug, GameEngine.CHARACTER_LAYERS.FRONT);

        Drawable test = (Canvas canvas) -> {};
        DrawableEntity customEntity = new DrawableEntity(test, "debug");
        // test.draw(canvas);

    }

    private void addGameTerminal() {
        final Paint terminalTextPaint = new Paint();
        terminalTextPaint.setColor(Color.WHITE);
        terminalTextPaint.setTextSize(32);
        Typeface typeface = ResourcesCompat.getFont(gameEngine.getContext(), R.font.truetypefont);
        terminalTextPaint.setTypeface(typeface);
        final Paint terminalBackgroundPaint = new Paint();
        terminalBackgroundPaint.setColor(Color.argb(122, 53, 53, 47));
        Drawable terminalDrawable = (canvas) -> {
            canvas.drawRect(
                    this.debugTerminal.getPositionX(), this.debugTerminal.getPositionY(),
                    this.debugTerminal.getDownLeftCoordX(), this.debugTerminal.getDownLeftCoordY(),
                    terminalBackgroundPaint);
            canvas.drawText(this.commandLine,
                    this.debugTerminal.getPositionX() + 20, this.debugTerminal.getPositionY() + 35,
                    terminalTextPaint);
        };
        this.debugTerminal = new DrawableEntity(null, terminalDrawable, "Debug Terminal",
                new Point(20, 650), new Point(820, 700));
        gameEngine.addGameEntity(this.debugTerminal, GameEngine.CHARACTER_LAYERS.FRONT);
        // SUSTITUIR EN TERMINALDRAWABLE EL PARAMETRO DRAWABLE POR DEBUG
        // HACER LO MISMO CON LOS BOTONES DE DEBUG
    }

    private void initializeCommands() {
        this.commands.put("disable debug", () -> { GameEngine.DEBUGGING = false; });
    }

    public String getCommandLine() {
        return this.commandLine;
    }

    public void executeCommand() {
        for (String key : this.commands.keySet()) {
            if (this.commandLine.equals(key)) {
                this.commands.get(key).run();
                break;
            }
        }
        this.commandLine = "";
    }

    public void addCharacterToTerminal(char key) {
        if (this.commandLine.length() < 44) {
            this.commandLine += key;
        }
        hiddenCommandInput += key;
        this.checkHiddenCommands();
    }

    private void checkHiddenCommands() {
        if (!GameEngine.DEBUGGING && this.hiddenCommandInput.endsWith("debug")) {
            GameEngine.DEBUGGING = true;
            this.commandLine = "";
        }
    }

    public static void printMessage(String message) {
        System.out.println("JAVIER: " + message);
    }

    public void deleteLastCharacter() {
        if (this.commandLine.length() > 0) {
            this.commandLine = this.commandLine.substring(0, this.commandLine.length() - 1);
        }
    }
}
