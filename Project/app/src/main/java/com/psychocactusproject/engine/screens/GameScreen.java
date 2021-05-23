package com.psychocactusproject.engine.screens;

import com.psychocactusproject.android.DebugHelper;
import com.psychocactusproject.engine.manager.GameEngine;
import com.psychocactusproject.engine.manager.GameEngine.SCENES;
import com.psychocactusproject.engine.manager.GameEntity;
import com.psychocactusproject.engine.manager.GameEntityManager;
import com.psychocactusproject.engine.manager.GameLogic;
import com.psychocactusproject.engine.util.Hitbox;
import com.psychocactusproject.graphics.interfaces.Drawable;
import com.psychocactusproject.graphics.views.SurfaceGameView;
import com.psychocactusproject.input.Touchable;
import com.psychocactusproject.interaction.menu.ContextMenu;
import com.psychocactusproject.interaction.menu.MenuDisplay;
import com.psychocactusproject.interaction.scripts.Clickable;

import java.util.List;

import static com.psychocactusproject.input.TouchInputController.hitboxCollision;

public class GameScreen implements Scene {

    public static GameScreen getInstance() {
        return SurfaceGameView.getInstance().getGameScreen();
    }

    @Override
    public Drawable definedDrawable() {
        return this.definedDrawable(false);
    }

    @Override
    public Touchable definedTouchable() {
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
                    GameEngine.getInstance().closeAllMenus();
                    // Abre el nuevo menú
                    clickableHolder.executeClick(selected.getIndex());
                    // Se trata de un menú
                } else if (clickableHolder instanceof ContextMenu) {
                    ContextMenu clickedMenu = (ContextMenu) clickableHolder;
                    ContextMenu.MenuOption option = clickedMenu.getMenuOptions()[selected.getIndex()];
                    if (option.isAvailable()) {
                        // Cierra los menús ya abiertos
                        GameEngine.getInstance().closeAllMenus();
                        // Ejecuta la acción de la instancia Clickable seleccionada
                        clickableHolder.executeClick(selected.getIndex());
                    } else {
                        String alertMessage = "The '" + option.getOptionName() + "' action of "
                                + clickedMenu.getFatherRole() + " cannot be performed.";
                        // String reason = clickedMenu.guessUnavailableReason(selected.getIndex());
                        SurfaceGameView.getInstance().showAlertDialog(alertMessage);
                        // this.gameEngine.showAlertDialog(alertMessage, reason);
                        DebugHelper.printMessage("Aquí debería decir 'la acción "
                                + option.getOptionName() + " no se encuentra disponible'");
                    }
                } else {
                    clickableHolder.executeClick(0);
                }
                // Si no es seleccionada ninguna hitbox, cierra menús
            } else {
                GameEngine.getInstance().closeAllMenus();
            }
        };
    }

    @Override
    public void onSceneChange(SCENES oldScene) {

    }

    @Override
    public int getSceneId() {
        return GameEngine.SCENES.GAME.ordinal();
    }

    public Drawable definedDrawable(boolean isSnapshotRender) {
        // Dibuja todos los elementos del juego por capas de prioridades
        return (canvas) -> {
            GameEngine gameEngine = GameEngine.getInstance();
            // Prioridad 3: Personajes
            for (GameEngine.GAME_LAYERS layer : GameEngine.GAME_LAYERS.values()) {
                // Si es llamado como snapshot se detiene al llegar a los objetos de interfaz
                if (isSnapshotRender && layer == GameEngine.GAME_LAYERS.USER_INTERFACE) {
                    return;
                }
                for (Drawable drawableEntity : gameEngine.getEntitiesByLayer(layer)) {
                    drawableEntity.draw(canvas);
                }
            }
            // Prioridad 2: Menús
            for (int i = 0; i < gameEngine.getGameDrawables().size(); i++) {
                if (gameEngine.getGameDrawables().get(i) instanceof MenuDisplay) {
                    MenuDisplay menuHolder = ((MenuDisplay) gameEngine.getGameDrawables().get(i));
                    if (menuHolder.isMenuOpen()) {
                        menuHolder.renderMenu(canvas);
                        if (GameEngine.DEBUGGING) {
                            Hitbox[] menuHitboxes = menuHolder.getMenu().getHitboxes();
                            // El fragmento de aquí abajo omite mostrar las hitboxes no activadas
                            // cuando realmente lo que deseo es mostrarlas desactivadas y seguir
                            // interactuando con ellas, pero de otra forma distinta
                        /*
                        Hitbox[] availableHitboxes = new Hitbox[menuHitboxes.length];
                        for (int j = 0; j < availableHitboxes.length; j++) {
                            if (menuHolder.getMenu().isAvailable(j)) {
                                availableHitboxes[j] = menuHitboxes[j];
                            }
                        }*/
                            // Hitbox.drawHitboxes(availableHitboxes, canvas);
                            Hitbox.drawHitboxes(menuHitboxes, canvas);
                        }
                    }
                }
            }
            // Prioridad 1: Interfaz de usuario
            if (GameEngine.DEBUGGING) {
                for (int i = 0; i < gameEngine.getGameDrawables().size(); i++) {
                    gameEngine.getDebugDrawables().get(i).debugDraw(canvas);
                }
            }
            for (int i = 0; i < gameEngine.getGameDrawables().size(); i++) {
                if (GameEngine.DEBUGGING && gameEngine.getGameDrawables().get(i) instanceof Clickable) {
                    Hitbox.drawHitboxes(((Clickable) gameEngine.getGameDrawables().get(i)).getHitboxes(), canvas);
                }
            }
        };
    }



    private Hitbox checkGameHitboxes(int xTouch, int yTouch) {
        List<GameEntity> gameEntitiesList = GameEngine.getInstance().getGameEntities();
        // Prioridad nivel 1: menús
        for (GameEntity entity : gameEntitiesList) {
            if (entity instanceof MenuDisplay) {
                MenuDisplay menuHolder = ((MenuDisplay) entity);
                if (menuHolder.isMenuOpen()) {
                    ContextMenu.MenuOption[] entityOptions = ((MenuDisplay) entity).getMenu().getMenuOptions();
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
        for (GameEntity entity : gameEntitiesList) {
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
}
