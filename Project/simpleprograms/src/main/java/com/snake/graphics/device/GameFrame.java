package com.snake.graphics.device;

import com.snake.graphics.render.Rendering;
import com.snake.logic.controls.Keyboard;
import com.snake.logic.controls.Mouse;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsDevice;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 *
 * @author Javier Martinez
 */
public class GameFrame extends JFrame implements GameDevice {

    // GAME FRAME VARIABLES
    private static final int NUM_BUFFERS = 3;
    private boolean running;
    private Thread animator;
    private BufferStrategy buffer;
    private boolean closing;

    // SCREEN SIZE VARIABLES
    private boolean fsMode;
    private int fsWidth, fsHeight; // FullScreen size
    private int wmWidth, wmHeight; // Windowed Size

    // FRAME COUNTER
    private long initTime;
    private int loops;
    private int detectedFPS;

    // FONTS
    private Font gameOverCreBig;
    private Font gameOverCreMedium;
    private Font gameOverCreSmall;

    // SINGLETON
    private static GameFrame instance = null;

    public static GameFrame getInstance() {
        if (instance == null) {
            instance = new GameFrame();
        }
        return instance;
    }

    // CONSTRUCTOR
    private GameFrame() {
        super("Snake");
        this.initializeIcon();
        this.running = false;
        this.closing = false;
        this.initTime = System.nanoTime();
        this.loops = 0;
        this.detectedFPS = 0;
        this.initFS(Rendering.getInstance());
    }

    // FULLSCREEN MODE
    private void initFS(Rendering render) {
        // We set its properties
        this.setUndecorated(true);
        this.setIgnoreRepaint(true);
        this.setResizable(false);
        // We start fullscreen mode
        render.getGDev().setFullScreenWindow(this);
        // We set size values
        this.setMode(true,
                this.getBounds().width, this.getBounds().height
        );
        // Throwing exception if this machine is not supported
        if (!render.getGDev().isFullScreenSupported()) {
            throw new IllegalStateException(
                    "The machine you are using is not supported. ");
        }
        // To print at output terminal
        this.showParameters(render);
        // Configures buffer
        this.initBuffer();
    }

    // GET METHODS
    public int getScreenW() {
        if (this.fsMode) {
            return this.fsWidth;
        } else {
            return this.wmWidth;
        }
    }

    public int getScreenH() {
        if (this.fsMode) {
            return this.fsHeight;
        } else {
            return this.wmHeight;
        }
    }

    // SET METHODS
    public void setMode(boolean fsMode, int width, int height) {
        if (fsMode) {
            this.fsWidth = width;
            this.fsHeight = height;
        } else {
            this.wmWidth = width;
            this.wmHeight = height;
        }
        this.fsMode = fsMode;
    }

    // CLASS METHODS
    @Override
    public void run() {
        // Interaction
        Mouse.getInstance().addMouseListeners(this);
        Keyboard.getInstance().addButtonListeners(this);
        // Loading system resources
        this.initResources();
        // Starting loop
        this.running = true;
        while(running) {
            // Printing screen as soon as it is possible
            this.printScreenFrame(fsMode);
        }
        // Finishing loop, thread and game
        this.finishOff();
    }

    private void initializeIcon() {
        try {
            super.setIconImage(
                    ImageIO.read(new File(
                            Paths.get(new File("").getAbsolutePath()
                                    + "\\simpleprograms\\src\\main\\java\\com\\snake\\graphics\\resources\\icon\\"
                                    + "snake-icon.png").toString()
                    ))
            );
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void printScreenFrame(boolean fsMode) {
        // Frames printed
        this.loops++;
        if (System.nanoTime() - this.initTime > 1000000000) {
            this.initTime = System.nanoTime();
            this.setFPSCount(loops);
            this.loops = 0;
        }
        // Gathers fullscreen machine resources
        Rendering render = Rendering.getInstance();
        render.setGraphics(buffer.getDrawGraphics());
        // Building frame into the buffer
        render.frameBuilding();
        // Revoke machine resources
        render.getGraphics().dispose();
        // Printing into screen
        this.printFrame();
        // Fixes ocasional desync when using some OS
        Toolkit.getDefaultToolkit().sync();
    }

    private void printFrame() {
        if (!buffer.contentsLost()) { // VolatileImage could be lost
            buffer.show(); // Showing image at screen
        } else {
            System.err.println("An image was damaged. "
                    + "Time: " + System.currentTimeMillis());
        }
    }

    @Override
    public void initialize() {
        if (this.animator == null || !running) {
            this.animator = new Thread(this);
            this.animator.start();
        }
    }

    @Override
    public void finishOff() {
        if (!this.closing) {
            this.closing = true;
            this.revokeResources();
            System.exit(0);
        }
    }

    public void revokeResources() {
        // Turns off fullscreen mode
        GraphicsDevice gDevice = Rendering.getInstance().getGDev();
        Window window = gDevice.getFullScreenWindow();
        if (window != null) {
            try {
                window.dispose();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        gDevice.setFullScreenWindow(null);
    }

    private void showParameters(Rendering render) {
        // Show details of graphics device
        if (render.getGDev() == null) {
            throw new IllegalStateException(
                    "Device has not been initialized yet.");
        }
        DisplayMode dMode = render.getGDev().getDisplayMode();
        System.out.println("Current screen mode: \n" +
                " - Width and Height -> " + dMode.getWidth()
                + "," + dMode.getHeight() + "\n" +
                " - BitDepth and RefreshRate -> " + dMode.getBitDepth()
                + "," + dMode.getRefreshRate());
    }

    private void initBuffer() {
        // Buffer is created when resources are available
        try {
            EventQueue.invokeAndWait( () -> {
                createBufferStrategy(NUM_BUFFERS);
            });
        } catch (InterruptedException | InvocationTargetException e) {
            System.out.println("Buffer could not start.");
            System.exit(0);
        }
        this.buffer = this.getBufferStrategy();
    }
    // GET METHODS
    @Override
    public boolean isRunning() {
        return this.running;
    }

    public boolean isClosing() {
        return this.closing;
    }

    public int getFPSCount() {
        return this.detectedFPS;
    }

    public Font getBigFont() {
        return this.gameOverCreBig;
    }

    public Font getMediumFont() {
        return this.gameOverCreMedium;
    }

    public Font getSmallFont() {
        return this.gameOverCreSmall;
    }

    // SET METHODS
    @Override
    public synchronized void setRunning(boolean value) {
        this.running = value;
    }

    private void setFPSCount(int updatesNum) {
        this.detectedFPS = updatesNum;
    }

    // DATA LOADING METHODS
    public void initResources() {
        this.loadFont();
    }

    private void loadFont() {
        String fontLocation = (new File("").getAbsolutePath() +
                "\\simpleprograms\\src\\main\\java\\com\\snake\\graphics\\resources\\fonts\\" + "gameovercre.ttf");
        File file = new File(fontLocation);
        try {
            Rendering render = Rendering.getInstance();
            this.gameOverCreBig = Font.createFont(Font.TRUETYPE_FONT, file);
            if (!render.getGEnv().registerFont(gameOverCreBig)) {
                boolean found = false;
                for (Font singleFont : render.getGEnv().getAllFonts()) {
                    if (singleFont.getFontName().equals(
                            this.gameOverCreBig.getFontName())) {
                        found = true;
                    }
                }
                if (!found) {
                    throw new IllegalStateException(
                            "Font could not be registered.");
                } else {
                    System.out.println("Font already loaded.");
                }
            }
        } catch (FontFormatException | IOException e) {
            throw new IllegalStateException("Font could not be loaded at location: "
                    + fontLocation
                    + "\".");
        }
        int fontOneSize = (int) (32 * (this.getScreenW() * 0.001));
        int fontTwoSize = (int) (20 * (this.getScreenW() * 0.001));
        int fontThreeSize = (int) (12 * (this.getScreenW() * 0.001));
        this.gameOverCreBig = new Font(
                "gameovercre", Font.PLAIN, fontOneSize);
        this.gameOverCreMedium = new Font(
                "gameovercre", Font.PLAIN, fontTwoSize);
        this.gameOverCreSmall = new Font(
                "gameovercre", Font.PLAIN, fontThreeSize);
    }
}