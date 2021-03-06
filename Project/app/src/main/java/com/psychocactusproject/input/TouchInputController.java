package com.psychocactusproject.input;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.psychocactusproject.R;
import com.psychocactusproject.graphics.views.SurfaceGameView;
import com.psychocactusproject.interaction.menu.MenuDisplay;
import com.psychocactusproject.interaction.scripts.Clickable;
import com.psychocactusproject.android.GameFragment;
import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.engine.GameEngine.BlackStripesTypes;
import com.psychocactusproject.engine.GameEntity;
import com.psychocactusproject.engine.Hitbox;
import com.psychocactusproject.engine.Point;

import java.util.List;

public class TouchInputController extends InputController {

    private int xCoordinate;
    private int yCoordinate;
    private boolean pressing;
    private int moveCountdown;
    private boolean moving;
    private boolean clickPending;
    private int deviceWidth;
    private int deviceHeight;
    private int aspectRatioMargin;
    private BlackStripesTypes hasBlackStripes;
    private List<GameEntity> gameEntities;
    private TextView textView;
    // DEBUG
    private boolean drawingPoints;

    public TouchInputController(GameEngine gameEngine, View view) {
        view.findViewById(R.id.gameView).setOnTouchListener(this);
        this.textView = view.findViewById(R.id.txt_debug);
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
        this.drawingPoints = false;
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
                    // DEBUG: dibuja puntos en pantalla cuando un deslizamiento es detectado
                    if (GameEngine.DEBUGGING && this.drawingPoints) {
                        synchronized (SurfaceGameView.inputMovePoints) {
                            SurfaceGameView.inputMovePoints.add(new Point((int) event.getX(), (int) event.getY()));
                        }
                    }
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
    public void update() {
        Point click = getPendingClick();
        if (click != null) {
            // Comprueba si hay una colisión con alguna hitbox
            Hitbox selected = this.checkHitboxes(click.getX(), click.getY());
            // Cierra los menús
            for (GameEntity entity : this.gameEntities) {
                // Los que tienen menu, los cierran
                if (entity instanceof MenuDisplay) {
                    ((MenuDisplay) entity).closeMenu();
                }
            }
            if (GameEngine.DEBUGGING) {
                GameFragment.setDebugText("nada");
            }
            // Si ha habido colisión, ejecuta su acción asignada
            if (selected != null) {
                Clickable sprite = selected.getFather();
                sprite.executeClick(selected.getIndex());
            }
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

    private Hitbox checkHitboxes(int xTouch, int yTouch) {
        // Prioridad nivel 1: interfaz
            /*
            for (Hitbox hitbox : ) {

            }*/
        // Prioridad nivel 2: menús
        for (GameEntity entity : this.gameEntities) {
            if (entity instanceof MenuDisplay) {
                MenuDisplay menuHolder = ((MenuDisplay) entity);
                if (menuHolder.hasMenuOpen()) {
                    Hitbox[] hitboxesCheck = menuHolder.getMenu().getHitboxes();
                    if (hitboxesCheck != null) {
                        for (Hitbox hitbox : hitboxesCheck) {
                            if (hitbox != null && hitboxCollision(xTouch, yTouch,
                                    hitbox.getUpLeftPoint(),
                                    hitbox.getDownRightPoint())) {
                                return hitbox;
                            }
                        }
                    }
                }
            }
        }
        // Prioridad nivel 3: personajes
        for (GameEntity entity : this.gameEntities) {
            if (entity instanceof Clickable) {
                Hitbox[] hitboxesCheck = ((Clickable) entity).getHitboxes();
                if (hitboxesCheck != null) {
                    for (Hitbox hitbox : hitboxesCheck) {
                        if (hitbox != null && hitboxCollision(xTouch, yTouch,
                                hitbox.getUpLeftPoint(),
                                hitbox.getDownRightPoint())) {
                            return hitbox;
                        }
                    }
                }
            }
        }

        return null;
    }

    private boolean hitboxCollision(int xTouch, int yTouch, Point upLeft, Point downRight){
        return(xTouch > upLeft.getX()
                && xTouch < downRight.getX()
                && yTouch > upLeft.getY()
                && yTouch < downRight.getY());
    }
}
