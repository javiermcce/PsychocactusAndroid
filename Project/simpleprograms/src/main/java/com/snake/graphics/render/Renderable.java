package com.snake.graphics.render;

import java.awt.image.BufferedImage;

public interface Renderable {

    public BufferedImage getRenderedImage();

    public int getImageWSize();

    public int getImageHSize();
}
