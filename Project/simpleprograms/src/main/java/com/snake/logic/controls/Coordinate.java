
package com.snake.logic.controls;

/**
 *
 * @author Javier Martinez
 */
public class Coordinate {

    // VARIABLES
    private int xCoord;
    private int yCoord;

    // CONSTRUCTOR
    public Coordinate(int x, int y) {
        this.xCoord = x;
        this.yCoord = y;
    }

    // GET METHODS
    public int getX() { return this.xCoord; }
    public int getY() { return this.yCoord; }

    // SET METHODS
    public void setLocation(int x, int y, int z) {
        this.xCoord = x;
        this.yCoord = y;
    }

    // CLASS METHODS
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Coordinate)) {
            return false;
        }
        Coordinate coord = (Coordinate) o;
        return (coord.xCoord == this.xCoord &&
                coord.yCoord == this.yCoord);
    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + this.xCoord;
        hash = 43 * hash + this.yCoord;
        return hash;
    }
    @Override
    public String toString() {
        return "{ x: " + this.xCoord + "  y: " + this.yCoord + " }";
    }
}
