package com.snake.logic.entities;

import com.snake.logic.stats.Point;

import java.util.LinkedList;

public class Snake {

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private int xMapSize;
    private int yMapSize;
    private LinkedList<Point> snakeBody;

    public Snake(int xSize, int ySize) {
        this.xMapSize = xSize;
        this.yMapSize = ySize;
        this.snakeBody = new LinkedList<>();

        // solo debug
        this.snakeBody.addFirst(new Point(4, 5));
        this.snakeBody.addFirst(new Point(4, 6));
        this.snakeBody.addFirst(new Point(4, 7));
        this.snakeBody.addFirst(new Point(5, 7));
        this.snakeBody.addFirst(new Point(5, 8));
    }

    public Point move(Direction direction) {
        return this.move(direction, false);
    }

    public Point move(Direction direction, boolean eating) {
        // Destination point coordinates
        int destXCoord = (int) this.snakeBody.getFirst().getX();
        int destYCoord = (int) this.snakeBody.getFirst().getY();
        // Destination coord change
        switch (direction) {
            case UP:
                destYCoord--;
                break;
            case DOWN:
                destYCoord++;
                break;
            case LEFT:
                destXCoord--;
                break;
            case RIGHT:
                destXCoord++;
                break;
        }
        // Border corrections to cross map
        if (destXCoord < 0) {
            destXCoord = this.xMapSize - 1;
        } else if (destXCoord >= this.xMapSize) {
            destXCoord = 0;
        }
        if (destYCoord < 0) {
            destYCoord = this.yMapSize - 1;
        } else if (destYCoord >= this.yMapSize) {
            destYCoord = 0;
        }
        // Destination point creation
        Point destination = new Point(destXCoord, destYCoord);
        this.snakeBody.addFirst(destination);
        // Elimination of queue
        if (!eating) {
            this.snakeBody.removeLast();
        }
        // Returning destination point
        return destination;
    }

    public boolean willHeadTouchBody(Point destination, boolean eating) {
        // Head will only be touching body if size is greater than two
        if (this.snakeBody.size() < 2) {
            return false;
        }
        // Iterates to check it
        int tailIndex = eating ? this.snakeBody.size() : this.snakeBody.size() - 1;
        for (int i = 0; i < tailIndex; i++) {
            if (destination.equals(this.snakeBody.get(i))) {
                return true;
            }
        }
        return false;
    }

    public int getSnakeSize() {
        return this.snakeBody.size();
    }

    public Point getBodyPart(int index) {
        return this.snakeBody.get(index);
    }
}
