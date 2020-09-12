package com.snake.logic.entities;

import com.snake.logic.map.Point;
import com.snake.logic.map.SnakeTable;

import java.util.LinkedList;

public class Snake {

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private Direction lastDirection;
    private Direction willingToGo;
    private LinkedList<Point> snakeBody;
    private boolean eating;

    public Snake() {
        this.snakeBody = new LinkedList<>();
        // después tendrá que ser random
        this.lastDirection = Direction.UP;
        this.eating = false;
        // solo debug

        this.snakeBody.addFirst(new Point(4, 5));
        this.snakeBody.addFirst(new Point(4, 6));
        this.snakeBody.addFirst(new Point(4, 7));
        this.snakeBody.addFirst(new Point(5, 7));
        this.snakeBody.addFirst(new Point(5, 8));
        this.snakeBody.addFirst(new Point(5, 9));
        this.snakeBody.addFirst(new Point(4, 9));
        this.snakeBody.addFirst(new Point(3, 9));
        this.snakeBody.addFirst(new Point(3, 8));
    }

    public Point move() {
        // Reset intention
        Direction headDirection;
        if (this.willingToGo != null) {
            headDirection = willingToGo;
            this.lastDirection = willingToGo;
        } else {
            headDirection = this.lastDirection;
        }
        this.willingToGo = null;
        // Updating head
        Point destination = this.computeNextDestination(headDirection);
        this.snakeBody.addFirst(destination);
        // Elimination of queue
        if (!this.eating) {
            this.snakeBody.removeLast();
        }
        // Returning destination point
        return destination;
    }

    public Point computeNextDestination(Direction direction) {
        // Current point coordinates
        int destXCoord = this.snakeBody.getFirst().getX();
        int destYCoord = this.snakeBody.getFirst().getY();
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
            destXCoord = SnakeTable.getInstance().getXSize() - 1;
        } else if (destXCoord >= SnakeTable.getInstance().getXSize()) {
            destXCoord = 0;
        }
        if (destYCoord < 0) {
            destYCoord = SnakeTable.getInstance().getYSize() - 1;
        } else if (destYCoord >= SnakeTable.getInstance().getYSize()) {
            destYCoord = 0;
        }
        // Destination point creation
        return new Point(destXCoord, destYCoord);
    }

    public boolean willHeadTouchBody() {
        // Head will only be touching body if size is greater than two
        if (this.snakeBody.size() < 4) {
            return false;
        }
        // Checks destination
        Point destination = this.computeNextDestination(this.lastDirection);
        // Iterates to check it
        int tailIndex = this.eating ? this.snakeBody.size() : this.snakeBody.size() - 1;
        for (int i = 0; i < tailIndex; i++) {
            if (destination.equals(this.snakeBody.get(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean willSnakeTouchObstacle() {
        return false;
    }

    public int getSnakeSize() {
        return this.snakeBody.size();
    }

    public Point getBodyPart(int index) {
        return this.snakeBody.get(index);
    }

    public Point getHead() {
        return this.snakeBody.getFirst();
    }

    public Direction whereIsHeading() {
        return this.lastDirection;
    }

    public void startEating() {
        this.eating = true;
    }

    public void stopEating() {
        this.eating = false;
    }

    public boolean isEating () {
        return this.eating;
    }

    public void setLastDirection(Direction lastDirection) {
        this.lastDirection = lastDirection;
    }

    public void setFutureHeading(Direction futureHeading) {
        // Assigns if direction is not yet set
        if (this.willingToGo == null) {
            switch (futureHeading) {
                case UP:
                    if (this.lastDirection != Direction.DOWN) { this.willingToGo = futureHeading; }
                    break;
                case LEFT:
                    if (this.lastDirection != Direction.RIGHT) { this.willingToGo = futureHeading; }
                    break;
                case DOWN:
                    if (this.lastDirection != Direction.UP) { this.willingToGo = futureHeading; }
                    break;
                case RIGHT:
                    if (this.lastDirection != Direction.LEFT) { this.willingToGo = futureHeading; }
                    break;
            }
        }
    }
}
