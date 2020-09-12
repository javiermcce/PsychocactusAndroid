package com.snake.graphics.entities;

import com.snake.graphics.render.Renderable;
import com.snake.logic.map.Point;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Snake implements Renderable {

    // Logic
    com.snake.logic.entities.Snake snakeLogic;
    // Render values
    //int snakeWidth


    public Snake(com.snake.logic.entities.Snake snakeLogic) {
        this.snakeLogic = snakeLogic;
    }


    public void drawOnImage(Graphics2D canvas) {
        // esto realmente pinta en lo que es el canvas que le das, que puede ser tanto el background
        // como podria ser una pequeña imagen que se posicionará en otro fragmento de código, así
        // que no rallarse
        canvas.setColor(Color.BLACK);
        for (int i = 0; i < snakeLogic.getSnakeSize(); i++) {
            Point bodyPart = snakeLogic.getBodyPart(i);
            canvas.fillRect(bodyPart.getX() + 5, bodyPart.getY() + 5,
                    bodyPart.getX() + 45, bodyPart.getY() + 45);
        }
    }

    @Override
    public BufferedImage getRenderedImage() {
        return null;
    }

    @Override
    public int getImageWSize() {
        return 0;
    }

    @Override
    public int getImageHSize() {
        return 0;
    }
}
