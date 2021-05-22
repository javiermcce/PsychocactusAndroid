package com.psychocactusproject.input;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.psychocactusproject.R;
import com.psychocactusproject.engine.manager.GameEngine;
import com.psychocactusproject.engine.manager.GameEngine.BLACK_STRIPE_TYPES;
import com.psychocactusproject.engine.manager.GameEngine.SCENES;
import com.psychocactusproject.engine.manager.GameEntity;
import com.psychocactusproject.engine.util.Hitbox;
import com.psychocactusproject.engine.util.Point;
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
    private HashMap<SCENES, com.psychocactusproject.input.Touchable> touchableScenesMap;


    public TouchInputController(GameEngine gameEngine, View view) {
        view.findViewById(R.id.gameView).setOnTouchListener(this);
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
        this.touchableScenesMap.put(SCENES.GAME,
                SurfaceGameView.getInstance().getGameScreen().definedTouchable());
        this.touchableScenesMap.put(SCENES.DIALOG,
                SurfaceGameView.getInstance().getDialog().definedTouchable());
        this.touchableScenesMap.put(SCENES.PAUSE_MENU,
                SurfaceGameView.getInstance().getPauseScreen().definedTouchable());
        // this.touchableScenesMap.put(SCENES.GAME, this.definedGameTouchable());
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
                // La acción continúa
                return true;
            case MotionEvent.ACTION_MOVE:
                if (moveCountdown <= 0) {
                    this.moving = true;
                }
                this.moveCountdown--;
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

    public static boolean hitboxCollision(int xTouch, int yTouch, Hitbox hitbox)  {
        return(xTouch > hitbox.getUpLeftX()
                && xTouch < hitbox.getDownRightX()
                && yTouch > hitbox.getUpLeftY()
                && yTouch < hitbox.getDownRightY());
    }
}
