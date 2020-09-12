package com.snake.graphics.scenes;

import com.snake.graphics.device.GameFrame;
import com.snake.graphics.render.Rendering;
import com.snake.graphics.render.Scene;
import com.snake.logic.controls.Keyboard;
import com.snake.logic.entities.Snake;
import com.snake.logic.map.Point;
import com.snake.logic.map.SnakeTable;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class GameScene implements Scene {

    // VARIABLES

    private int strokeSize;
    private Stroke borderStroke;
    private Point upLeftCorner;
    private com.snake.graphics.map.SnakeTable gTable;

    // CONSTRUCTOR
    public GameScene() {
        // Graphic objects
        this.gTable = new com.snake.graphics.map.SnakeTable();
        // Basic game scene settings
        this.strokeSize = 5;
        // Calculates game table size and corner position
        int xCornerCoord = (Rendering.getInstance().getCanvasW() - gTable.getImageWSize()) / 2;
        int yCornerCoord = (Rendering.getInstance().getCanvasH() - gTable.getImageHSize()) / 2;
        this.upLeftCorner = new Point(xCornerCoord, yCornerCoord);
        this.borderStroke = new BasicStroke(this.strokeSize);
    }

    @Override
    public void drawEnvironment(Graphics2D canvas) {
        // Snake table reference
        SnakeTable table = SnakeTable.getInstance();
        Snake snake = table.getSnake();
        // Debug
        LinkedList<Integer> intList = Keyboard.getInstance().getList();
        LinkedList<Character> charList = Keyboard.getInstance().getCharacterList();
        canvas.setFont(GameFrame.getInstance().getSmallFont());
        canvas.setColor(Color.WHITE);
        for (int i = 0; i < intList.size(); i++) {
            Integer element = intList.get(i);
            canvas.drawString(element.toString(), i * 50 + 50, 50);
        }
        for (int i = 0; i < charList.size(); i++) {
            Character element = charList.get(i);
            canvas.drawString(element.toString(), i * 50 + 50, 100);
        }
        // Game table border
        canvas.setStroke(borderStroke);
        canvas.setColor(Color.WHITE);
        canvas.drawRect(
                upLeftCorner.getX() - this.strokeSize,
                upLeftCorner.getY() - this.strokeSize,
                gTable.getImageWSize() + this.strokeSize * 2,
                gTable.getImageHSize() + this.strokeSize * 2);
        // Game score and info
        canvas.setFont(GameFrame.getInstance().getBigFont());
        String scoreValue = Integer.valueOf(table.getStats().getScore()).toString();
        String leftZeros = new String();
        for (int i = 0; i < 6 - scoreValue.length(); i++) {
            leftZeros += '0';
        }
        scoreValue = leftZeros + scoreValue;
        canvas.drawString(scoreValue,
                upLeftCorner.getX() + 70 ,upLeftCorner.getY() - 30);
        canvas.drawString(table.getStats().getPlayerName(),
                upLeftCorner.getX() + 350 ,upLeftCorner.getY() - 30);


        
        
        // Drawing result
        canvas.drawImage(gTable.getRenderedImage(),
                upLeftCorner.getX(), upLeftCorner.getY(),null);


    }
}
