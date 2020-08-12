package com.snake.graphics.render;

import com.snake.graphics.device.GameFrame;
import com.snake.graphics.scenes.SceneUpdater;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

/**
 *
 * @author Javier Martinez
 */
public class Rendering {

    // VARIABLES
    private final GraphicsEnvironment gEnv;
    private final GraphicsDevice gDevice;
    private final GraphicsConfiguration gConfiguration;
    private Graphics gManager;
    private BufferedImage gImage;
    private Graphics2D gCanvas;
    private final int canvasW, canvasH; // Internal rendering resolutions

    // PRUEBAS
    private BufferedImage imagenFondo;

    // SINGLETON
    private static Rendering instance = null;

    public static Rendering getInstance() {
        if (instance == null) {
            instance = new Rendering();
        }
        return instance;
    }

    // CONSTRUCTOR
    private Rendering() {
        this.gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.gDevice = this.gEnv.getDefaultScreenDevice();
        this.gConfiguration = this.gDevice.getDefaultConfiguration();
        this.canvasW = 1920; this.canvasH = 1080; // Constant canvas size, 16/9 proportion
    }

    // GET METHODS
    public GraphicsEnvironment getGEnv() {
        return this.gEnv;
    }

    public GraphicsDevice getGDev() {
        return this.gDevice;
    }

    public Graphics getGraphics() {
        return this.gManager;
    }

    public int getCanvasW() {
        return this.canvasW;
    }

    public int getCanvasH() {
        return this.canvasH;
    }

    // SET METHODS
    public void setGraphics(Graphics graphics) {
        this.gManager = graphics;
    }

    // CLASS METHODS
    public void frameBuilding() {
        // Creates a new BufferedImage, which equals the screen size
        GameFrame gFrame = GameFrame.getInstance();
        this.gImage = gConfiguration.createCompatibleImage(
                gFrame.getScreenW(), gFrame.getScreenH());
        this.gCanvas =  gImage.createGraphics();
        // Cleaning buffer
        this.gCanvas.setColor(Color.GRAY);
        this.gCanvas.fillRect(0, 0, gFrame.getScreenW(), gFrame.getScreenH());
        // Creates a render image, used for constructing a proportion correct game frame
        BufferedImage renderImage = new BufferedImage(
                this.getCanvasW(), this.getCanvasH(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D renderCanvas = renderImage.createGraphics();
        renderCanvas.setColor(Color.DARK_GRAY);
        renderCanvas.fillRect(0, 0, this.getCanvasW(), this.getCanvasH());
        // Recipe design pattern (?) for scene swapping
        SceneUpdater.getInstance().getScene().drawEnvironment(renderCanvas);
        // Printing created image into canvas reference
        gCanvas.drawImage(renderImage, 0, 0,
                gFrame.getScreenW(),
                gFrame.getScreenH(),
                null);
        // Printing canvas into graphic manager
        // There should be control of the relation between width and height to match 16/9!!
        this.gManager.drawImage(gImage, 0, 0,
                gFrame.getScreenW(), gFrame.getScreenH(),  null);
    }
}
