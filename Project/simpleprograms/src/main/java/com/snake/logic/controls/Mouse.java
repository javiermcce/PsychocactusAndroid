
package com.snake.logic.controls;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;

/**
 *
 * @author Javier Martinez
 */
public class Mouse {
    // SINGLETON
    private static Mouse instance = null;
    public static Mouse getInstance() {
        if (instance == null) {
            instance = new Mouse();
        }
        return instance;        
    }
    // VARIABLES
    private Coordinate lastPosition;
    private Coordinate lastClick;
    private boolean sliding;
    // CONSTRUCTOR
    private Mouse() {}
    // GET METHODS
    public Coordinate getLastClick() {
        if (this.lastClick == null) { return new Coordinate(0, 0); }
        else { return this.lastClick; }
    }
    /* LISTENERS */
    public void addMouseListeners(Component mode) {
        mode.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent click) {
                pressScreen(click.getX(), click.getY());
            }
            @Override
            public void mouseReleased(MouseEvent click) {
                if (!sliding) {
                    releaseScreen(click.getX(), click.getY());
                } else {
                    sliding = false;
                }
            }
            @Override
            public void mouseClicked(MouseEvent click) {
                screenPressed(click.getX(), click.getY());
            }
        });
        
        mode.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent movement) {
                sliding = false;
                screenMovement(movement.getX(), movement.getY());
            }
            @Override
            public void mouseDragged(MouseEvent movement) {
                sliding = true;
                screenDragging(movement.getX(), movement.getY());
            }
        });
	mode.addMouseWheelListener(new MouseAdapter() {
	    @Override
	    public void mouseWheelMoved(MouseWheelEvent wheel) {
		
	    }
	});
    }
    /* MANAGERS */
    private void pressScreen(int xCoord, int yCoord) {
        this.lastPosition = new Coordinate(xCoord, yCoord);
    }
    private void releaseScreen(int xCoord, int yCoord) {
        this.lastClick = new Coordinate(xCoord, yCoord);
    }
    private void screenPressed(int xCoord, int yCoord) {
        
    }
    private void screenMovement(int xCoord, int yCoord) {
        this.lastPosition = new Coordinate(xCoord, yCoord);
    }
    private void screenDragging(int xCoord, int yCoord) {
        
    }
    private void screenZooming(MouseWheelEvent wheel) {
        
    }
}
