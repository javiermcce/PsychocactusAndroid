package com.psychocactusproject.input;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.psychocactusproject.R;
import com.psychocactusproject.android.DebugHelper;
import com.psychocactusproject.engine.GameEngine;
import com.psychocactusproject.engine.GameEngine.BLACK_STRIPE_TYPES;
import com.psychocactusproject.engine.GameEngine.SCENES;
import com.psychocactusproject.engine.GameEntity;
import com.psychocactusproject.engine.GameEntityManager;
import com.psychocactusproject.engine.GameLogic;
import com.psychocactusproject.engine.Hitbox;
import com.psychocactusproject.engine.Point;
import com.psychocactusproject.graphics.controllers.ClickableDirectSprite;
import com.psychocactusproject.graphics.controllers.CustomClickableEntity;
import com.psychocactusproject.graphics.views.SurfaceGameView;
import com.psychocactusproject.interaction.menu.ContextMenu;
import com.psychocactusproject.interaction.menu.ContextMenu.MenuOption;
import com.psychocactusproject.interaction.menu.DialogScreen;
import com.psychocactusproject.interaction.menu.MenuDisplay;
import com.psychocactusproject.interaction.scripts.Clickable;

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
        this.touchableScenesMap.put(SCENES.GAME, this.definedGameTouchable());
        this.touchableScenesMap.put(SCENES.DIALOG, this.definedDialogTouchable());
        this.touchableScenesMap.put(SCENES.PAUSE_MENU, this.definedPauseTouchable());
        // this.touchableScenesMap.put(SCENES.GAME, this.definedGameTouchable());
    }

    public Touchable definedGameTouchable() {
        return (point) -> {
            // Comprueba si hay una colisión con alguna hitbox
            Hitbox selected = checkGameHitboxes(point.getX(), point.getY());
            // this.closeAllMenus();
            // Si ha habido colisión, ejecuta su acción asignada
            if (selected != null) {
                Clickable clickableHolder = selected.getFather();
                // Se trata de un personaje
                if (clickableHolder instanceof MenuDisplay) {
                    // Cierra los menús ya abiertos
                    closeAllMenus();
                    // Abre el nuevo menú
                    clickableHolder.executeClick(selected.getIndex());
                // Se trata de un menú
                } else if (clickableHolder instanceof ContextMenu) {
                    ContextMenu clickedMenu = (ContextMenu) clickableHolder;
                    MenuOption option = clickedMenu.getMenuOptions()[selected.getIndex()];
                    if (option.isAvailable()) {
                        // Cierra los menús ya abiertos
                        closeAllMenus();
                        // Ejecuta la acción de la instancia Clickable seleccionada
                        clickableHolder.executeClick(selected.getIndex());
                    } else {
                        String alertMessage = "The '" + option.getOptionName() + "' action of "
                                + clickedMenu.getFatherRole() + " cannot be performed.";
                        // String reason = clickedMenu.guessUnavailableReason(selected.getIndex());
                        GameEngine.getInstance().getSurfaceGameView().showAlertDialog(alertMessage);
                        // this.gameEngine.showAlertDialog(alertMessage, reason);
                        DebugHelper.printMessage("Aquí debería decir 'la acción "
                                + option.getOptionName() + " no se encuentra disponible'");
                    }
                } else {
                    clickableHolder.executeClick(0);
                }
            // Si no es seleccionada ninguna hitbox, cierra menús
            } else {
                closeAllMenus();
            }
        };
    }

    public Touchable definedDialogTouchable() {

        return (point) -> {
            // Comprueba si la pantalla de diálogo ha sido creada
            DialogScreen dialog = GameEngine.getInstance().getSurfaceGameView().getDialog();
            if (dialog == null) {
                throw new IllegalStateException("La pantalla de diálogo debía estar abierta.");
            }
            // Busca si existe colisión con alguna hitbox del diálogo
            Hitbox selected = null;
            for (Hitbox hitbox : dialog.getHitboxes()) {
                if (hitboxCollision(point.getX(), point.getY(), hitbox)) {
                    selected = hitbox;
                }
            }
            // Si ha habido colisión, ejecuta su acción asignada
            if (selected != null) {
                Clickable clickableHolder = selected.getFather();
                clickableHolder.executeClick(selected.getIndex());
            }
        };
    }

    public Touchable definedPauseTouchable() {
        return (point) -> {
            // Comprueba si hay una colisión con alguna hitbox
            Hitbox selected = checkPauseHitboxes(point.getX(), point.getY());
            // this.closeAllMenus();
            // Si ha habido colisión, ejecuta su acción asignada
            if (selected != null) {
                SurfaceGameView gameView = GameEngine.getInstance().getSurfaceGameView();

                for (CustomClickableEntity menuOption : gameView.getPauseScreen().getOptions()) {
                    continue;
                }
                GameEngine.getInstance().resumeGame();

                /*
                Clickable clickableHolder = selected.getFather();
                // Se trata de un personaje
                if (clickableHolder instanceof MenuDisplay) {
                    // Cierra los menús ya abiertos
                    closeAllMenus();
                    // Abre el nuevo menú
                    clickableHolder.executeClick(selected.getIndex());
                    // Se trata de un menú
                } else if (clickableHolder instanceof ContextMenu) {
                    ContextMenu clickedMenu = (ContextMenu) clickableHolder;
                    MenuOption option = clickedMenu.getMenuOptions()[selected.getIndex()];
                    if (option.isAvailable()) {
                        // Cierra los menús ya abiertos
                        closeAllMenus();
                        // Ejecuta la acción de la instancia Clickable seleccionada
                        clickableHolder.executeClick(selected.getIndex());
                    } else {
                        String alertMessage = "The '" + option.getOptionName() + "' action of "
                                + clickedMenu.getFatherRole() + " cannot be performed.";
                        // String reason = clickedMenu.guessUnavailableReason(selected.getIndex());
                        GameEngine.getInstance().getSurfaceGameView().showAlertDialog(alertMessage);
                        // this.gameEngine.showAlertDialog(alertMessage, reason);
                        DebugHelper.printMessage("Aquí debería decir 'la acción "
                                + option.getOptionName() + " no se encuentra disponible'");
                    }
                }

                 */
                // Si no es seleccionada ninguna hitbox, cierra menús
            } else {
                GameEngine.getInstance().resumeGame();
            }
        };
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

    private void closeAllMenus() {
        // Cierra los menús
        for (GameEntity entity : this.gameEntities) {
            // Los que tienen menu, los cierran
            if (entity instanceof MenuDisplay) {
                ((MenuDisplay) entity).closeMenu();
            }
        }
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

    private Hitbox checkGameHitboxes(int xTouch, int yTouch) {
        // Prioridad nivel 1: menús
        for (GameEntity entity : this.gameEntities) {
            if (entity instanceof MenuDisplay) {
                MenuDisplay menuHolder = ((MenuDisplay) entity);
                if (menuHolder.isMenuOpen()) {
                    MenuOption[] entityOptions = ((MenuDisplay) entity).getMenu().getMenuOptions();
                    Hitbox[] hitboxesCheck = menuHolder.getMenu().getHitboxes();
                    for (int i = 0; i < entityOptions.length; i++) {
                        // la idea es quitar este filtro, y sí seleccionar la hitbox esté
                        // activada o no, controlar la respuesta desde un fragmento
                        // superior de código
                        Hitbox hitbox = hitboxesCheck[i];
                        if (hitbox != null && hitboxCollision(xTouch, yTouch, hitbox)) {
                            return hitbox;
                        }
                    }
                }
            }
        }
        // Prioridad nivel 2: interfaz
        GameEntityManager entityManager = GameLogic.getInstance().getGameEntityManager();
        // Pause menu
        Hitbox[] pauseHitboxes = entityManager.getPauseButton().getHitboxes();
        for (Hitbox hitbox : pauseHitboxes) {
            if (hitboxCollision(xTouch, yTouch, hitbox)) {
                return hitbox;
            }
        }
        // Prioridad nivel 3: personajes
        for (GameEntity entity : this.gameEntities) {
            if (entity instanceof Clickable) {
                if (((Clickable) entity).isAvailable(0)) {
                    Hitbox[] hitboxesCheck = ((Clickable) entity).getHitboxes();
                    if (hitboxesCheck != null) {
                        for (Hitbox hitbox : hitboxesCheck) {
                            if (hitbox != null && hitboxCollision(xTouch, yTouch, hitbox)) {
                                return hitbox;
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * Utilizar tanto para la pantalla de inicio como para el menú de pausa dentro del juego
     *
     * @param xTouch
     * @param yTouch
     * @return
     */
    private Hitbox checkPauseHitboxes(int xTouch, int yTouch) {
        // Prioridad nivel 3: personajes
        /*for (MenuEntity menu : this.menuEntities) {
            Hitbox[] hitboxesCheck = menu.getHitboxes();
            if (hitboxesCheck != null) {
                for (Hitbox hitbox : hitboxesCheck) {
                    if (hitbox != null && hitboxCollision(xTouch, yTouch, hitbox)) {
                        return hitbox;
                    }
                }
            }
        }

         */return null;
    }

    private boolean hitboxCollision(int xTouch, int yTouch, Hitbox hitbox)  {
        return(xTouch > hitbox.getUpLeftX()
                && xTouch < hitbox.getDownRightX()
                && yTouch > hitbox.getUpLeftY()
                && yTouch < hitbox.getDownRightY());
    }

    @FunctionalInterface
    public interface Touchable {

        public void search(Point point);
    }
}
