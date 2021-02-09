package com.psychocactusproject.input;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.psychocactusproject.R;
import com.psychocactusproject.graphics.controllers.AbstractSprite;
import com.psychocactusproject.graphics.views.SurfaceGameView;
import com.psychocactusproject.interaction.menu.MenuDisplay;
import com.psychocactusproject.manager.engine.GameEngine;
import com.psychocactusproject.manager.engine.Hitbox;
import com.psychocactusproject.manager.engine.Point;
import com.psychocactusproject.manager.engine.GameEngine.BlackStripesTypes;

import java.util.List;

public class TouchInputController extends InputController {

    private int xCoordinate;
    private int yCoordinate;
    private boolean pressing;
    private int moveCount;
    private boolean moving;
    private boolean clickPending;
    private int deviceWidth;
    private int deviceHeight;
    private int aspectRatioMargin;
    private BlackStripesTypes hasBlackStripes;
    private List<AbstractSprite> gameEntities;
    private TextView textView;

    public TouchInputController(GameEngine gameEngine, View view) {
        view.findViewById(R.id.gameView).setOnTouchListener(this);
        this.textView = view.findViewById(R.id.txt_score);
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
                this.moveCount = 2;
                // La acción continúa
                return true;
            case MotionEvent.ACTION_MOVE:
                if (moveCount <= 0) {
                    this.moving = true;
                    // TEST
                    synchronized (SurfaceGameView.puntos) {
                        SurfaceGameView.puntos.add(new Point((int) event.getX(), (int) event.getY()));
                    }
                }
                this.moveCount--;
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
            for (AbstractSprite entity : this.gameEntities) {
                // Los que tienen menu, los cierran
                if (entity instanceof MenuDisplay) {
                    ((MenuDisplay) entity).closeMenu();
                }
            }
            Hitbox selected = this.checkHitboxes(click.getX(), click.getY());
            if (selected != null) {
                AbstractSprite sprite = selected.getFather();
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
        for (AbstractSprite entity : this.gameEntities) {
            if (entity instanceof MenuDisplay) {
                MenuDisplay menuHolder = ((MenuDisplay) entity);
                if (menuHolder.hasMenuOpen()) {
                    for (Hitbox hitbox : menuHolder.getMenu().getHitboxes()) {
                        if (hitbox != null && hitboxCollision(xTouch, yTouch,
                                hitbox.getUpLeftPoint(),
                                hitbox.getDownRightPoint())) {
                            return hitbox;
                        }
                    }
                }
            }
        }
        // Prioridad nivel 3: personajes
        for (AbstractSprite entity : this.gameEntities) {
            for(Hitbox hitbox : entity.getHitboxes()){
                if(hitbox != null && hitboxCollision(xTouch, yTouch,
                        hitbox.getUpLeftPoint(),
                        hitbox.getDownRightPoint())){
                    return hitbox;
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
