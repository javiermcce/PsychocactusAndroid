package com.snake.graphics.scenes;

import com.snake.graphics.render.Scene;
import com.snake.graphics.device.GameFrame;
import com.snake.logic.controls.Keyboard;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;

public class GameScene implements Scene {

    @Override
    public void drawEnvironment(Graphics2D canvas) {
        /*
        LinkedList<Integer> list = Keyboard.getInstance().getList();
        canvas.setFont(GameFrame.getInstance().getSmallFont());
        canvas.setColor(Color.WHITE);
        for (int i = 0; i < list.size(); i++) {
            Integer element = list.get(i);
            canvas.drawString(element.toString(), i * 50 + 50, 50);
        }
        */




    }
}
