package com.psychocactusproject.android;

import android.graphics.Color;
import android.graphics.Paint;

import com.psychocactusproject.R;
import com.psychocactusproject.engine.manager.GameEngine;
import com.psychocactusproject.engine.manager.GameEngine.GAME_LAYERS;
import com.psychocactusproject.engine.util.Point;
import com.psychocactusproject.graphics.controllers.ClickableDirectSprite;
import com.psychocactusproject.graphics.interfaces.Drawable;
import com.psychocactusproject.graphics.controllers.CustomDrawableEntity;
import com.psychocactusproject.graphics.manager.ResourceLoader;

import java.util.HashMap;
import java.util.LinkedList;

import static java.lang.Thread.sleep;

public class DebugHelper {

    // RECORDAR REVISAR Y LIMPIAR LOS EFECTOS NO DESEADOS DEL COMMIT 7d7cf63

    private GameEngine gameEngine;
    private CustomDrawableEntity debugTerminal;
    private HashMap<String, Runnable> commands;
    private String hiddenCommandInput;
    private LinkedList<String> pastCommands;
    private String commandLine;
    private String provisionalCommand;
    private int commandIndex;

    public DebugHelper(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        // this.addEnterCommandButton();
        this.addGameTerminal();
        this.commands = new HashMap<>();
        this.initializeCommands();
        this.hiddenCommandInput = "";
        this.pastCommands = new LinkedList<>();
        this.commandLine = "";
        this.provisionalCommand = "";
        this.commandIndex = -1;
    }

    /**
     * @deprecated no es necesario, lo sustituye el gameTerminal
     */
    private void addEnterCommandButton() {
        // Debug button
        Runnable enterCommandAction = () -> { this.executeCommand(); };
        ClickableDirectSprite commandDebug = new ClickableDirectSprite(gameEngine,
                R.drawable.debug_enter, "Debug enter command",
                enterCommandAction, new Point(400, 200));
        gameEngine.addGameEntity(commandDebug, GameEngine.GAME_LAYERS.FRONT);
    }

    private void addGameTerminal() {
        final Paint terminalTextPaint = new Paint();
        terminalTextPaint.setColor(Color.WHITE);
        terminalTextPaint.setTextSize(32);
        terminalTextPaint.setTypeface(ResourceLoader.getTypeface());
        final Paint terminalBackgroundPaint = new Paint();
        terminalBackgroundPaint.setColor(Color.argb(122, 53, 53, 47));
        Drawable terminalDrawable = (canvas) -> {
            canvas.drawRect(
                    0, 0,
                    this.debugTerminal.getSpriteWidth(), this.debugTerminal.getSpriteHeight(),
                    terminalBackgroundPaint);
            canvas.drawText(this.commandLine,
                    // this.debugTerminal.getPositionX() + 20, this.debugTerminal.getPositionY() + 35,
                    20, 35,
                    terminalTextPaint);
        };
        // Es creado el terminal, que solo se muestra si el modo debug está activado
        this.debugTerminal = new CustomDrawableEntity(null, terminalDrawable, "Debug Terminal",
                new Point(20, 650), new Point(820, 700));
        gameEngine.addGameEntity(this.debugTerminal, GAME_LAYERS.DEBUG);
    }

    // Inserta todos los posibles comandos
    private void initializeCommands() {
        this.commands.put("disable debug", () -> { GameEngine.DEBUGGING = false; });
        this.commands.put("pause", () -> {
            GameEngine.getInstance().pauseGame();
            try {
                sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GameEngine.getInstance().resumeGame();
            // GameEngine.getInstance().getSimpleActivity().getFragment().pauseGameAndShowPauseDialog();
        });
    }

    public void showPreviousCommand() {
        // Si existen más elementos en la lista, mueve índice
        if ((this.commandIndex + 1) < this.pastCommands.size()) {
            this.commandIndex++;
        // Si el índice resultante de buscar comando previo es al menos 0, se recoge comando
        } if (this.commandIndex >= 0) {
            this.commandLine = this.pastCommands.get(this.commandIndex);
        }
    }

    public void showNextCommand() {
        // Hace descender el índice, que como mínimo podrá ser -1
        if (this.commandIndex >= 0) {
            this.commandIndex--;
        }
        // Si el índice resultante de buscar comando previo es al menos 0, se recoge comando
        if (this.commandIndex >= 0) {
            this.commandLine = this.pastCommands.get(this.commandIndex);
        // En caso contrario, se recoge el comando que se estaba escribiendo antes
        } else {
            this.commandLine = this.provisionalCommand;
        }
    }

    public void executeCommand() {
        // Se ejecuta el comando que se haya introducido
        for (String key : this.commands.keySet()) {
            if (this.commandLine.equals(key)) {
                this.commands.get(key).run();
                break;
            }
        }
        // Si el comando no está vacío
        if (!this.commandLine.equals("")) {
            // Se busca en la lista de comandos y se borra un comando antiguo si coincide
            for (int i = 0; i < this.pastCommands.size(); i++) {
                if (this.pastCommands.get(i).equals(this.commandLine)) {
                    this.pastCommands.remove(i);
                    break;
                }
            }
            // Se introduce el comando en el historial, justo al principio
            this.pastCommands.addFirst(this.commandLine);
        }
        // Se restablece la configuración inicial
        this.commandLine = "";
        this.provisionalCommand = "";
        this.commandIndex = -1;
    }

    public void addCharacterToTerminal(char key) {
        // Si el terminal no está mostrando ya el máximo número de caracteres, inserta uno nuevo
        if (this.commandLine.length() < 38) {
            this.commandLine += key;
        }
        // Guarda el comando que se está escribiendo
        if (this.commandIndex == -1) {
            this.provisionalCommand = new String(this.commandLine);
        }
        // Guarda un registro de las últimas teclas que se hayan introducido
        this.hiddenCommandInput += key;
        this.checkHiddenCommands();
    }

    public void deleteLastCharacter() {
        if (this.commandLine.length() > 0) {
            this.commandLine = this.commandLine.substring(0, this.commandLine.length() - 1);
        }
        // Guarda el comando que se está escribiendo
        if (this.commandIndex == -1) {
            this.provisionalCommand = new String(this.commandLine);
        }
    }

    private void checkHiddenCommands() {
        // Si no estamos en modo debug y es detectado el input "debug"
        if (!GameEngine.DEBUGGING && this.hiddenCommandInput.endsWith("debug")) {
            // Es activado el modo debug y son restablecidos los parámetros de comandos
            GameEngine.DEBUGGING = true;
            this.commandLine = "";
            this.provisionalCommand = "";
        }
        // Si el historial de caracteres supera los 1000 caracteres
        if (this.hiddenCommandInput.length() > 1000) {
            // Son eliminados 900 caracteres
            this.hiddenCommandInput = this.hiddenCommandInput.substring(900);
        }
    }

    // Método estático que permite fácilmente hacer seguimiento de los logs
    public static void printMessage(String message) {
        System.out.println("JAVIER: " + message);
    }

    // Método estático que permite fácilmente hacer seguimiento de los logs
    public static void printMessage(String message, String id) {
        System.out.println(id + ": " + message);
    }
}
