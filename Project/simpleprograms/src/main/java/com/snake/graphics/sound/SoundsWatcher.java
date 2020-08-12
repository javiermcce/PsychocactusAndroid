
package com.snake.graphics.sound;

/**
 *
 * @author Javier Martinez
 */
public interface SoundsWatcher {
    // constants used by SoundsWatcher method, atSequenceEnd()
    public final static int STOPPED = 0;
    public final static int REPLAYED = 1;

    void atSequenceEnd(String filename, int status);
}
