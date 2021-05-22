package com.psychocactusproject.engine.screens;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.psychocactusproject.engine.manager.GameEngine;
import com.psychocactusproject.engine.util.Hitbox;
import com.psychocactusproject.graphics.controllers.ClickableDirectSprite;
import com.psychocactusproject.graphics.controllers.CustomClickableEntity;
import com.psychocactusproject.graphics.interfaces.Drawable;
import com.psychocactusproject.graphics.manager.ResourceLoader;
import com.psychocactusproject.graphics.manager.MenuBitmapFlyweight;
import com.psychocactusproject.graphics.views.SurfaceGameView;
import com.psychocactusproject.input.Touchable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class PauseScreen implements Scene {

    private final MenuBitmapFlyweight.PauseMenuFlyweight pieces;
    private int randomMusicIndex;
    private int randomCompleteMusicIndex;

    public enum PAUSE_LAYERS { FIRST, SECOND }
    private PAUSE_LAYERS activeLayer;
    private Drawable initialDrawable;
    private Touchable initialTouchable;
    private List<ClickableDirectSprite> pauseEntities;
    private final HashMap<PAUSE_LAYERS, List<CustomClickableEntity>> optionsByLayer;
    private final Matrix pauseMatrix;
    private final Matrix backgroundPauseMatrix;
    private final Paint pausePaint;
    private final Paint backgroundPausePaint;
    private Bitmap lastFrameBitmap;
    private Bitmap pauseBaseFrame;
    private Canvas pauseFrameCanvas;

    public PauseScreen() {
        this.pieces = MenuBitmapFlyweight.getPauseMenuInstance();
        this.pauseMatrix = new Matrix();
        this.backgroundPauseMatrix = new Matrix();
        this.pausePaint = new Paint();
        this.pausePaint.setColor(Color.WHITE);
        this.pausePaint.setTextSize(128);
        this.pausePaint.setTypeface(ResourceLoader.getTypeface());
        this.backgroundPausePaint = new Paint();
        this.backgroundPausePaint.setColor(Color.argb(100, 20, 20, 50));
        this.optionsByLayer = new HashMap<>();
        this.createOptions();
    }

    public List<CustomClickableEntity> getOptionsByLayer(PAUSE_LAYERS layer) {
        return this.optionsByLayer.get(layer);
    }

    public CustomClickableEntity[] getOptions() {
        return this.optionsByLayer.get(this.activeLayer).toArray(new CustomClickableEntity[0]);
    }

    public List<ClickableDirectSprite> getPauseEntities() {
        return this.pauseEntities;
    }

    public void setPauseEntities(List<ClickableDirectSprite> pauseEntities) {
        this.pauseEntities = pauseEntities;
    }

    public void openPauseScreen() {
        this.activeLayer = PAUSE_LAYERS.FIRST;
    }

    @Override
    public Drawable definedDrawable() {
        return (canvas) -> {
            // En el primer ciclo de dibujado de pausa...
            if (this.lastFrameBitmap == null) {
                // Se construye una copia de de la imagen de fondo
                Canvas copyCanvas = new Canvas();
                this.lastFrameBitmap = Bitmap.createBitmap(
                        GameEngine.RESOLUTION_X, GameEngine.RESOLUTION_Y,
                        Bitmap.Config.ARGB_8888
                );
                copyCanvas.setBitmap(this.lastFrameBitmap);
                SurfaceGameView.getInstance().getGameScreen().definedDrawable(true).draw(copyCanvas);
                // Se construye el marco de pausa
                this.pauseFrameCanvas = new Canvas();
                this.pauseBaseFrame = Bitmap.createBitmap(
                        GameEngine.RESOLUTION_X, GameEngine.RESOLUTION_Y, Bitmap.Config.ARGB_8888);
                this.pauseFrameCanvas.setBitmap(this.pauseBaseFrame);
                this.pauseMatrix.reset();
                this.pauseMatrix.postTranslate(640, 0);
                Bitmap nextPiece;
                // Barra vertical
                nextPiece = this.pieces.getVerticalBarPiece();
                this.pauseFrameCanvas.drawBitmap(nextPiece, this.pauseMatrix, this.pausePaint);
                this.pauseMatrix.postTranslate(0, nextPiece.getHeight());
                // Barra horizontal
                this.pauseMatrix.reset();
                this.pauseMatrix.postTranslate(0, 345);
                nextPiece = this.pieces.getHorizontalBarPiece();
                this.pauseFrameCanvas.drawBitmap(nextPiece, this.pauseMatrix, this.pausePaint);
                this.pauseMatrix.postTranslate(nextPiece.getWidth(), 0);
                this.pauseMatrix.reset();
                this.randomMusicIndex = (int) (Math.random() * 5);
                this.randomCompleteMusicIndex = (int) (Math.random() * 5) + 5;
            }
            // Dibuja la copia de la imagen de fondo
            canvas.drawBitmap(this.lastFrameBitmap, this.pauseMatrix, this.pausePaint);
            // Aplica un fondo oscurecido
            canvas.drawRect(
                    new Rect(0, 0, GameEngine.RESOLUTION_X, GameEngine.RESOLUTION_Y),
                    this.backgroundPausePaint);
            // Imprime el texto de pausa
            canvas.drawText("PAUSE", 120, 210, this.pausePaint);
            // Imprime el marco del menú de pausa
            canvas.drawBitmap(this.pauseBaseFrame, this.pauseMatrix, this.pausePaint);
            this.pauseMatrix.reset();
            this.pauseMatrix.postTranslate(850, 65);
            /*
            canvas.drawBitmap(
                    MenuBitmapFlyweight.getPauseMenuInstance().getRandomFace(randomMusicIndex),
                    this.pauseMatrix, this.pausePaint);

             */
            //this.pauseMatrix.postTranslate(0, 300);
            canvas.drawBitmap(
                    MenuBitmapFlyweight.getPauseMenuInstance().getRandomFace(randomCompleteMusicIndex),
                    this.pauseMatrix, this.pausePaint);
            // Restablece la posición de la matriz para utilizarla a la siguiente vuelta
            this.pauseMatrix.reset();

        };
    }

    @Override
    public Touchable definedTouchable() {
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

    private void createOptions() {
        for (PAUSE_LAYERS key : PAUSE_LAYERS.values()) {
            this.optionsByLayer.put(key, new LinkedList<>());
        }
    }

    public void clearLastGameFrame() {
        this.lastFrameBitmap = null;
    }
}
