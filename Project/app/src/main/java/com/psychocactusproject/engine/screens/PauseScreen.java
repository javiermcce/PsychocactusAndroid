package com.psychocactusproject.engine.screens;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.psychocactusproject.R;
import com.psychocactusproject.engine.manager.GameEngine;
import com.psychocactusproject.engine.manager.GameEngine.SCENES;
import com.psychocactusproject.engine.util.Hitbox;
import com.psychocactusproject.engine.util.Point;
import com.psychocactusproject.engine.util.Slider;
import com.psychocactusproject.engine.util.UserInterfaceFlyweight;
import com.psychocactusproject.graphics.controllers.ClickableDirectSprite;
import com.psychocactusproject.graphics.controllers.CustomDrawableEntity;
import com.psychocactusproject.graphics.interfaces.Drawable;
import com.psychocactusproject.graphics.manager.MenuBitmapFlyweight;
import com.psychocactusproject.graphics.manager.ResourceLoader;
import com.psychocactusproject.graphics.views.SurfaceGameView;
import com.psychocactusproject.input.Slidable;
import com.psychocactusproject.input.Touchable;
import com.psychocactusproject.interaction.scripts.Clickable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.psychocactusproject.input.TouchInputController.squareCollision;

public class PauseScreen implements Scene, Clickable {

    private final MenuBitmapFlyweight.PauseMenuFlyweight pieces;
    private int randomMusicIndex;

    // Capas del menú de pausa
    public static final int MAIN_LAYER = 0;
    public static final int OPTIONS_LAYER = 1;
    // Botones de la capa principal
    public static final int RESUME_BUTTON = 0;
    public static final int RESTART_BUTTON = 1;
    public static final int OPTIONS_BUTTON = 2;
    public static final int EXIT_BUTTON = 3;
    // Botones de la capa de opciones
    public static final int CANCEL_BUTTON = 0;
    public static final int DEFAULT_BUTTON = 1;
    public static final int SAVE_BUTTON = 2;
    public static final int LANGUAGE_BUTTON = 3;
    // Variables comunes a la pantalla de pausa
    private int activeLayer;
    private List<ClickableDirectSprite> pauseEntities;
    private final HashMap<Integer, List<CustomDrawableEntity>> optionsByLayer;
    private final HashMap<Integer, List<Hitbox>> hitboxesByLayer;
    private final Matrix pauseMatrix;
    private final Paint pausePaint;
    private final Paint optionsPaint;
    private final Paint backgroundPausePaint;
    private Bitmap lastFrameBitmap;
    private Bitmap pauseBaseFrame;
    // TEMPORAL
    private Slider musicSlider;
    private Slider effectsSlider;
    private List<Slidable> slidersList;


    public PauseScreen() {
        this.pieces = MenuBitmapFlyweight.getPauseMenuInstance();
        this.pauseMatrix = new Matrix();
        this.pausePaint = new Paint();
        this.pausePaint.setColor(Color.WHITE);
        this.pausePaint.setTextSize(128);
        this.pausePaint.setTypeface(ResourceLoader.getTypeface());
        this.optionsPaint = new Paint();
        this.optionsPaint.setColor(Color.WHITE);
        this.optionsPaint.setTypeface(ResourceLoader.getTypeface());
        this.optionsPaint.setTextSize(50);
        this.backgroundPausePaint = new Paint();
        this.backgroundPausePaint.setColor(ResourceLoader.backgroundColor);
        this.optionsByLayer = new HashMap<>();
        this.hitboxesByLayer = new HashMap<>();
        this.slidersList = new LinkedList<>();
        this.createOptions();
        /*
        *
        * */
    }

    public List<CustomDrawableEntity> getOptionsByLayer(int layer) {
        return this.optionsByLayer.get(layer);
    }

    public CustomDrawableEntity[] getOptions() {
        return this.optionsByLayer.get(this.activeLayer).toArray(new CustomDrawableEntity[0]);
    }

    public List<ClickableDirectSprite> getPauseEntities() {
        return this.pauseEntities;
    }

    public void setPauseEntities(List<ClickableDirectSprite> pauseEntities) {
        this.pauseEntities = pauseEntities;
    }

    public void openPauseScreen() {
        this.activeLayer = MAIN_LAYER;
    }

    @Override
    public void executeClick(int index) {
        switch (activeLayer) {
            case MAIN_LAYER:
                switch (index) {
                    case RESUME_BUTTON:
                        GameEngine.getInstance().resumeGame();
                        break;
                    case RESTART_BUTTON:
                        GameEngine.getInstance().restartGame();
                        break;
                    case OPTIONS_BUTTON:
                        this.activeLayer = OPTIONS_LAYER;
                        break;
                    case EXIT_BUTTON:
                        GameEngine.getInstance().stopGame();
                        break;
                }
                break;
            case OPTIONS_LAYER:
                switch (index) {
                    case CANCEL_BUTTON:
                        this.activeLayer = MAIN_LAYER;
                        // GameEngine.getInstance().resumeGame();
                        break;
                    case DEFAULT_BUTTON:
                        // GameEngine.getInstance().restartGame();
                        break;
                    case SAVE_BUTTON:
                        this.applyChanges();
                        this.activeLayer = MAIN_LAYER;
                        // this.activeLayer = OPTIONS_LAYER;
                        break;
                    case EXIT_BUTTON:
                        // GameEngine.getInstance().stopGame();
                        break;
                }
                break;
        }
    }

    private void applyChanges() {
        // lee estado actual
        // ajusta
    }

    @Override
    public Hitbox[] getHitboxes() {
        return this.hitboxesByLayer.get(activeLayer).toArray(new Hitbox[0]);
    }

    @Override
    public boolean isAvailable(int index) {
        return true;
    }

    @Override
    public void enableClickable(int index) {
        throw new IllegalStateException("Se ha intentado activar una opción de la pantalla de " +
                "pausa, pero no está permitido activar ni desactivar las opciones");
    }

    @Override
    public void disableClickable(int index) {
        throw new IllegalStateException("Se ha intentado desactivar una opción de la pantalla de " +
                "pausa, pero no está permitido activar ni desactivar las opciones");
    }

    @Override
    public int getPositionX() {
        return 0;
    }

    @Override
    public int getPositionY() {
        return 0;
    }

    @Override
    public Point getPosition() {
        return new Point(0, 0);
    }

    @Override
    public int getSpriteWidth() {
        return GameEngine.RESOLUTION_X;
    }

    @Override
    public int getSpriteHeight() {
        return GameEngine.RESOLUTION_Y;
    }

    @Override
    public Drawable definedDrawable() {
        return (canvas) -> {
            // En el primer ciclo de dibujado de pausa...
            if (this.lastFrameBitmap == null) {
                this.buildBackgroundBitmap();
            }
            // Dibuja la copia de la imagen de fondo
            canvas.drawBitmap(this.lastFrameBitmap, this.pauseMatrix, this.pausePaint);
            // Aplica un fondo oscurecido
            canvas.drawRect(
                    new Rect(0, 0, GameEngine.RESOLUTION_X, GameEngine.RESOLUTION_Y),
                    this.backgroundPausePaint);
            switch (activeLayer) {
                case MAIN_LAYER:
                    this.drawMainPauseScreen(canvas);
                    break;
                case OPTIONS_LAYER:
                    this.drawOptionsPauseScreen(canvas);
                    break;
                default:
                    break;
            }
            if (GameEngine.DEBUGGING) {
                Hitbox.drawHitboxes(this.getHitboxes(), canvas);
            }
        };
    }

    @Override
    public Touchable definedTouchable() {
        return (point) -> {
            // Comprueba si hay una colisión con alguna hitbox
            Hitbox selected = checkPauseHitboxes(point.getX(), point.getY());
            // Si ha habido colisión, ejecuta su acción asignada
            if (selected != null) {
                this.executeClick(selected.getIndex());
            }
        };
    }

    @Override
    public void onSceneChange(SCENES oldScene) {
        this.activeLayer = MAIN_LAYER;
    }

    @Override
    public SCENES getSceneId() {
        return GameEngine.SCENES.PAUSE_MENU;
    }

    @Override
    public List<Slidable> getSlidables() {
        if (this.activeLayer == PauseScreen.OPTIONS_LAYER) {
            return this.slidersList;
        } else {
            return null;
        }
    }

    private void buildBackgroundBitmap() {
        // Se construye una copia de de la imagen de fondo
        Canvas copyCanvas = new Canvas();
        this.lastFrameBitmap = Bitmap.createBitmap(
                GameEngine.RESOLUTION_X, GameEngine.RESOLUTION_Y,
                Bitmap.Config.ARGB_8888
        );
        copyCanvas.setBitmap(this.lastFrameBitmap);
        SurfaceGameView.getInstance().getGameScreen().definedDrawable(true).draw(copyCanvas);
        // Se construye el marco de pausa
        Canvas pauseFrameCanvas = new Canvas();
        this.pauseBaseFrame = Bitmap.createBitmap(
                GameEngine.RESOLUTION_X, GameEngine.RESOLUTION_Y, Bitmap.Config.ARGB_8888);
        pauseFrameCanvas.setBitmap(this.pauseBaseFrame);
        this.pauseMatrix.reset();
        this.pauseMatrix.postTranslate(640, 0);
        Bitmap nextPiece;
        // Barra vertical
        nextPiece = this.pieces.getVerticalBarPiece();
        pauseFrameCanvas.drawBitmap(nextPiece, this.pauseMatrix, this.pausePaint);
        this.pauseMatrix.reset();
        this.pauseMatrix.postTranslate(0, nextPiece.getHeight() - 15);
        // Barra horizontal
        nextPiece = this.pieces.getHorizontalBarPiece();
        pauseFrameCanvas.drawBitmap(nextPiece, this.pauseMatrix, this.pausePaint);
        this.pauseMatrix.postTranslate(nextPiece.getWidth(), 0);
        this.pauseMatrix.reset();
        // Selecciona un músico aleatorio, para dibujarlo constantemente mientras dure la pausa
        this.randomMusicIndex = (int) (Math.random() * 5);
    }

    /**
     * Utilizar tanto para la pantalla de inicio como para el menú de pausa dentro del juego
     *
     * @param xTouch
     * @param yTouch
     * @return
     */
    private Hitbox checkPauseHitboxes(int xTouch, int yTouch) {
        // El método getHitboxes ya devuelve la capa correcta de pausa
        for (Hitbox pauseHitbox : this.getHitboxes()) {
            if (squareCollision(xTouch, yTouch, pauseHitbox)) {
                return pauseHitbox;
            }
        }
        return null;
    }

    private void createOptions() {
        //
        for (int key : new int[] {MAIN_LAYER, OPTIONS_LAYER}) {
            this.optionsByLayer.put(key, new LinkedList<>());
            this.hitboxesByLayer.put(key, new LinkedList<>());
        }
        this.createMainPauseScreen();
        this.createOptionsPauseScreen();

    }

    // Considerar la opción de tener también un hash de bitmaps
    // qué pasa con los sliders, que se deben generar según el usuario interactua??
    // considerar opción de PARA ESE CASO generar una nueva imagen cada vez que cambia el slider
    private void createMainPauseScreen() {
        // Tamaños y posiciones
        int optionWidthPerc = 20;
        int optionHeightPerc = 16;
        int leftOptionX = 16;
        int rightOptionX = 65;
        int optionY = 52;
        int yMargin = 25;
        //
        List<Hitbox> hitboxList = this.hitboxesByLayer.get(MAIN_LAYER);
        hitboxList.add(new Hitbox(leftOptionX, optionY,
                leftOptionX + optionWidthPerc, optionY + optionHeightPerc,
                this, RESUME_BUTTON));
        hitboxList.add(new Hitbox(rightOptionX, optionY,
                rightOptionX + optionWidthPerc, optionY + optionHeightPerc,
                this, RESTART_BUTTON));
        hitboxList.add(new Hitbox(leftOptionX, optionY + yMargin,
                leftOptionX + optionWidthPerc, optionY + yMargin + optionHeightPerc,
                this, OPTIONS_BUTTON));
        hitboxList.add(new Hitbox(rightOptionX, optionY + yMargin,
                rightOptionX + optionWidthPerc, optionY + yMargin + optionHeightPerc,
                this, EXIT_BUTTON));
    }

    private void createOptionsPauseScreen() {
        // Tamaños y posiciones
        int optionWidthPerc = 20;
        int optionHeightPerc = 12;
        int leftOptionX = 16;
        int rightOptionX = 65;
        int optionY = 52;
        int yMargin = 25;


        int languageWidthPerc = 24;

        int bottomButtonsY = 80;
        int cancelButtonX = 10;
        int defaultButtonX = 40;
        int saveButtonX = 70;
        int optionsColumnX = 60;
        int languageButtonY = 12;
        //
        List<Hitbox> hitboxList = this.hitboxesByLayer.get(OPTIONS_LAYER);
        hitboxList.add(new Hitbox(cancelButtonX, bottomButtonsY,
                cancelButtonX + optionWidthPerc, bottomButtonsY + optionHeightPerc,
                this, CANCEL_BUTTON));
        hitboxList.add(new Hitbox(defaultButtonX, bottomButtonsY,
                defaultButtonX + optionWidthPerc, bottomButtonsY + optionHeightPerc,
                this, DEFAULT_BUTTON));
        hitboxList.add(new Hitbox(saveButtonX, bottomButtonsY,
                saveButtonX + optionWidthPerc, bottomButtonsY + optionHeightPerc,
                this, SAVE_BUTTON));
        hitboxList.add(new Hitbox(optionsColumnX, languageButtonY,
                optionsColumnX + languageWidthPerc, languageButtonY + optionHeightPerc,
                this, LANGUAGE_BUTTON));

        // de qué tipo quiero que sea la lista de opciones? debe existir acaso?
        // basta con una lista de drawables? centralizo gestión de botones y sliders?

        // POSIBLEMENTE LO QUE QUIERO ES UNA LISTA DE OBJETOS ¡¡¡VITAMIN POWER!!! QUE SEAN:
        // - POSITIONABLES
        // - DRAWABLES
        // - GAMEABLES
        // - TOUCHABLES
        // Y ESTO ADEMÁS LO QUIERO APLICAR A TODOS LOS ÁMBITOS DEL JUEGO

        List<CustomDrawableEntity> options = this.optionsByLayer.get(this.activeLayer);
        this.effectsSlider = new Slider("Effects volume scroller", new Point(740, 220));
        this.musicSlider = new Slider("Music volume scroller", new Point(740 , 350));
        this.slidersList.add(effectsSlider);
        this.slidersList.add(musicSlider);
        // aquí el problema
        // options.addAll(new Slider[] {effectsSlider, musicSlider});
    }

    private void drawMainPauseScreen(Canvas canvas) {
        // Imprime el texto de pausa
        canvas.drawText("PAUSE", 120, 190, this.pausePaint);
        // Imprime el marco del menú de pausa
        canvas.drawBitmap(this.pauseBaseFrame, this.pauseMatrix, this.pausePaint);
        this.pauseMatrix.reset();
        this.pauseMatrix.postTranslate(850, 32);
        canvas.drawBitmap(
                MenuBitmapFlyweight.getPauseMenuInstance().getRandomFace(randomMusicIndex),
                this.pauseMatrix, this.pausePaint);
        // Restablece la posición de la matriz para utilizarla a la siguiente vuelta
        this.pauseMatrix.reset();
        // Dibujado de los botones
        Hitbox[] hitboxes = this.getHitboxes();
        for (int button : new int[] { RESUME_BUTTON, RESTART_BUTTON, OPTIONS_BUTTON, EXIT_BUTTON }) {
            String optionText;
            switch (button) {
                case RESUME_BUTTON:
                    optionText = "Resume";
                    break;
                case RESTART_BUTTON:
                    optionText = "Restart";
                    break;
                case OPTIONS_BUTTON:
                    optionText = "Options";
                    break;
                case EXIT_BUTTON:
                    optionText = "Exit";
                    break;
                default:
                    throw new IllegalStateException("El botón no está definido.");
            }
            // String optionText, SpaceBox optionButton, Paint textPaint, Canvas canvas
            UserInterfaceFlyweight.getInstance().drawButton(
                    optionText, hitboxes[button], this.optionsPaint, canvas);
        }
    }

    private void drawOptionsPauseScreen(Canvas canvas) {
        // URGENTE CONSTRUIR UNA IMAGEN QUE SIRVA COMO CACHE PARA LOS CICLOS DE DIBUJADO
        // Dibujado de los botones
        Hitbox[] hitboxes = this.getHitboxes();
        UserInterfaceFlyweight uiUtil = UserInterfaceFlyweight.getInstance();
        for (int button : new int[] { CANCEL_BUTTON, DEFAULT_BUTTON, SAVE_BUTTON, LANGUAGE_BUTTON }) {
            String optionText;
            switch (button) {
                case CANCEL_BUTTON:
                    optionText = "Cancel";
                    break;
                case DEFAULT_BUTTON:
                    optionText = "Default";
                    break;
                case SAVE_BUTTON:
                    optionText = "Save";
                    break;
                case LANGUAGE_BUTTON:
                    optionText = "English";
                    break;
                default:
                    throw new IllegalStateException("El botón no está definido.");
            }
            // String optionText, SpaceBox optionButton, Paint textPaint, Canvas canvas
            uiUtil.drawButton(
                    optionText, hitboxes[button], this.optionsPaint, canvas);
        }
        // Textos
        /*
        canvas.drawText("Language", 300, 200, this.optionsPaint);
        canvas.drawText("Effects", 300, 300, this.optionsPaint);
        canvas.drawText("Music", 300, 400, this.optionsPaint);
        */
        // String optionText, SpaceBox tagSpace, int backgroundImage,
        //                                Paint textPaint, Canvas canvas
        uiUtil.drawCenteredTag("Language", new Point(220, 80),
                R.drawable.option_tag_background, this.optionsPaint, canvas);
        uiUtil.drawCenteredTag("Effects", new Point(220, 210),
                R.drawable.option_tag_background, this.optionsPaint, canvas);
        uiUtil.drawCenteredTag("Music", new Point(220, 340),
                R.drawable.option_tag_background, this.optionsPaint, canvas);
        //
        this.musicSlider.draw(canvas);
        this.effectsSlider.draw(canvas);
    }

    public void clearLastGameFrame() {
        this.lastFrameBitmap = null;
    }
}
