package com.psychocactusproject.input;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.psychocactusproject.R;
import com.psychocactusproject.engine.manager.GameEngine;
import com.psychocactusproject.engine.manager.GameEngine.BLACK_STRIPE_TYPES;
import com.psychocactusproject.engine.manager.GameEngine.SCENES;
import com.psychocactusproject.engine.manager.GameEntity;
import com.psychocactusproject.engine.screens.Scene;
import com.psychocactusproject.engine.util.Point;
import com.psychocactusproject.engine.util.SpaceBox;
import com.psychocactusproject.engine.util.Square;
import com.psychocactusproject.engine.util.SquareInterface;
import com.psychocactusproject.graphics.views.SurfaceGameView;

import java.util.HashMap;
import java.util.List;

public class TouchInputController extends InputController implements View.OnKeyListener {

    private int xCoordinate;
    private int yCoordinate;
    private boolean pressing;
    private int moveCountdown;
    private boolean moving;
    private boolean clickPending;
    private int deviceWidth;
    private int deviceHeight;
    private int aspectRatioMargin;
    private BLACK_STRIPE_TYPES hasBlackStripes;
    private List<GameEntity> gameEntities;
    private HashMap<SCENES, Touchable> touchableScenesMap;
    private Slidable focusedSlidable;


    public TouchInputController(GameEngine gameEngine, View view) {
        view.findViewById(R.id.simpleGameView).setOnTouchListener(this);
        this.xCoordinate = 0;
        this.yCoordinate = 0;
        this.pressing = false;
        this.moving = false;
        this.clickPending = false;
        this.deviceWidth = gameEngine.getDeviceWidth();
        this.deviceHeight = gameEngine.getDeviceHeight();
        this.aspectRatioMargin = gameEngine.getAspectRatioMargin();
        this.hasBlackStripes = gameEngine.hasBlackStripes();
        this.gameEntities = gameEngine.getGameEntities();
        this.touchableScenesMap = new HashMap<>();
        SurfaceGameView gameView = SurfaceGameView.getInstance();
        this.touchableScenesMap.put(SCENES.LOADING, gameView.getLoadingScreen().definedTouchable());
        this.touchableScenesMap.put(SCENES.INITIAL_SCREEN, gameView.getInitialScreen().definedTouchable());
        this.touchableScenesMap.put(SCENES.GAME, gameView.getGameScreen().definedTouchable());
        this.touchableScenesMap.put(SCENES.DIALOG, gameView.getDialog().definedTouchable());
        this.touchableScenesMap.put(SCENES.PAUSE_MENU, gameView.getPauseScreen().definedTouchable());
    }

    public int getX() {
        return this.xCoordinate;
    }

    public int getY() {
        return this.yCoordinate;
    }

    private void setXCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    private void setYCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        this.adjustTouchPosition((int) event.getX(), (int) event.getY());
        int provX = this.getX();
        int provY = this.getY();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                this.pressing = true;
                this.clickPending = false;
                this.moveCountdown = 2;
                // Si se encuentra, es capturado un objeto slidable
                this.focusedSlidable = this.searchSlidables();
                // La acción continúa
                return true;
            case MotionEvent.ACTION_MOVE:
                if (moveCountdown <= 0) {
                    this.moving = true;
                }
                this.moveCountdown--;
                if (this.focusedSlidable != null) {
                    this.focusedSlidable.updatePointer(this.getX());
                }
                // La acción continúa
                return true;
            case MotionEvent.ACTION_UP:
                // Si ha presionado y ha levantado (ha hecho click) sin mover por la pantalla,
                // hay un click pendiente para tener en cuenta
                if (!this.moving) {
                    this.clickPending = true;
                }
                this.pressing = false;
                this.moving = false;
                // Es liberado cualquier objeto slidable
                this.focusedSlidable = null;
                // La acción ha terminado
                return false;
        }
        return false;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void update() {
        Point click = getPendingClick();
        if (click != null) {
            this.touchableScenesMap.get(GameEngine.getInstance().getCurrentScene()).search(click);
        }
    }

    @Override
    protected Slidable searchSlidables() {
        Scene currentScene = GameEngine.getInstance().getSurfaceGameView().getCurrentScene();
        if (currentScene.getSlidables() == null) {
            return null;
        }
        for (Slidable slidable : currentScene.getSlidables()) {
            Square slidableSurface = new Square(
                    slidable.getPositionX(), slidable.getPositionY(),
                    slidable.getPositionX() + slidable.getSpriteWidth(),
                    slidable.getPositionY() + slidable.getSpriteHeight());
            if (squareCollision(this.getX(), this.getY(), slidableSurface)) {
                return slidable;
            }
        }
        return null;
    }

    public Point getPendingClick() {
        // Si hay un click por tener en cuenta, el thread de update debería recogerlo para
        // realizar las acciones pertinentes
        if (this.clickPending) {
            this.clickPending = false;
            return new Point(this.xCoordinate, this.yCoordinate);
        } else {
            return null;
        }
    }

    public void adjustTouchPosition(int xTouch, int yTouch) {
        int adjustedXTouch = xTouch;
        int adjustedYTouch = yTouch;
        int borderlessDeviceWidth = this.deviceWidth;
        int borderlessDeviceHeight = this.deviceHeight;
        switch (this.hasBlackStripes) {
            case LEFT_RIGHT:
                adjustedXTouch = xTouch - this.aspectRatioMargin;
                borderlessDeviceWidth = this.deviceWidth - (this.aspectRatioMargin * 2);
                break;
            case TOP_BOTTOM:
                adjustedYTouch = yTouch - this.aspectRatioMargin;
                borderlessDeviceHeight = this.deviceHeight - (this.aspectRatioMargin * 2);
                break;
        }
        this.setXCoordinate((int)((double) adjustedXTouch / borderlessDeviceWidth * GameEngine.RESOLUTION_X));
        this.setYCoordinate((int)((double) adjustedYTouch / borderlessDeviceHeight * GameEngine.RESOLUTION_Y));
    }
/*
    public static boolean squareCollision(int xTouch, int yTouch, SquareInterface square)  {
        return(xTouch > square.getUpLeftX()
                && xTouch < square.getDownRightX()
                && yTouch > square.getUpLeftY()
                && yTouch < square.getDownRightY());
    }*/

    public static boolean squareCollision(int xTouch, int yTouch, SquareInterface space)  {
        return(xTouch > space.getUpLeftX()
                && xTouch < space.getDownRightX()
                && yTouch > space.getUpLeftY()
                && yTouch < space.getDownRightY());
    }
}
