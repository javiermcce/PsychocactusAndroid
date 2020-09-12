package com.snake.graphics.map;

import com.snake.graphics.device.GameFrame;
import com.snake.graphics.render.Renderable;
import com.snake.graphics.render.Rendering;
import com.snake.logic.controls.Keyboard;
import com.snake.logic.entities.Snake;
import com.snake.logic.map.Point;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class SnakeTable implements Renderable {

    // VARIABLES
    private int unitSize;
    private int margin;
    private int strokeSize;
    private Stroke borderStroke;
    private Point upLeftCorner;
    private int xTableFrameSize;
    private int yTableFrameSize;

    int i1 = 0, i2 = 0;

    public SnakeTable() {
        // Basic game scene settings
        this.unitSize = 20;
        this.margin = 2;
        this.strokeSize = 5;
        // Calculates game table size and corner position
        this.xTableFrameSize = com.snake.logic.map.SnakeTable.getInstance().getXSize() * unitSize;// + this.strokeSize * 2;
        this.yTableFrameSize = com.snake.logic.map.SnakeTable.getInstance().getYSize() * unitSize;// + this.strokeSize * 2;

        this.borderStroke = new BasicStroke(this.strokeSize);
    }


    @Override
    public BufferedImage getRenderedImage() {
        // Game image creation
        BufferedImage tableImage = new BufferedImage(
                this.xTableFrameSize, this.yTableFrameSize,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D tableCanvas = tableImage.createGraphics();
        // Game content
        // el game content debería ir en la clase apropiada
        Rectangle shape = new Rectangle();
        tableCanvas.setColor(Color.GREEN);
        tableCanvas.setStroke(this.borderStroke);
        Keyboard keys = Keyboard.getInstance();

        if (keys.isKeyPressed(KeyEvent.VK_W)) { i2--; }
        if (keys.isKeyPressed(KeyEvent.VK_A)) { i1--; }
        if (keys.isKeyPressed(KeyEvent.VK_S)) { i2++; }
        if (keys.isKeyPressed(KeyEvent.VK_D)) { i1++; }

        shape.x = i1;
        shape.y = i2;
        shape.width = 400;
        shape.height = 50;
        tableCanvas.draw(shape);

        com.snake.logic.map.SnakeTable lTable = com.snake.logic.map.SnakeTable.getInstance();
        Snake lSnake = lTable.getSnake();

        tableCanvas.setColor(Color.GREEN);
        Rectangle squarePart = new Rectangle();
        squarePart.setBounds(0,0,this.unitSize - this.margin, this.unitSize - this.margin);
        for (int i = 0; i < lSnake.getSnakeSize() ; i++) {
            squarePart.setLocation(lSnake.getBodyPart(i).getX() * this.unitSize
                    ,lSnake.getBodyPart(i).getY() * this.unitSize);
            tableCanvas.fill(squarePart);
        }

        if (lTable.getGameUpdater().isGameLost()) {
            tableCanvas.setFont(GameFrame.getInstance().getBigFont());
            tableCanvas.drawString("THE GAME IS LOST" , 800, 50);
        }






        return tableImage;
    }

    @Override
    public int getImageWSize() {
        return this.xTableFrameSize;
    }

    @Override
    public int getImageHSize() {
        return this.yTableFrameSize;
    }
}
