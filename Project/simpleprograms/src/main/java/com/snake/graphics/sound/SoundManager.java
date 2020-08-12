
package com.snake.graphics.sound;

import java.util.HashMap;

/**
 *
 * @author Javier Martinez
 */
public class SoundManager {

    // VARIABLES
    private final HashMap<String, ClipInfo> clipsMap;

    // SINGLETON
    private static SoundManager instance = null;

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
	    return instance;
    }

    public static SoundManager getInstance(boolean other) {
	    if (other) { return new SoundManager(); }
	    if (instance == null) {
            instance = new SoundManager();
        }
	    return instance;
    }

    // CONSTRUCTOR
    private SoundManager() {
        this.clipsMap = new HashMap<>();  
        this.loadSoundsFile();
    }

    // CLASS METHODS
    private void loadSoundsFile() {
        String[] clipList = new String[]{};
        for (String sonido : clipList) {
            load(sonido, sonido + ".wav");
        }
    }

    public void load(String name, String fnm) {
        if (clipsMap.containsKey(name)) {
            System.err.println( "Error: " + name + "already stored");
        } else {
            clipsMap.put(name, new ClipInfo(name, fnm));
        }
    }

    public void close(String name) {
        ClipInfo ci = (ClipInfo) clipsMap.get(name);
        if (ci == null) {
            System.out.println( "Error: " + name + "not stored");
        } else {
            ci.close();
        }
    }

    public void play(String name, boolean toLoop) {
        ClipInfo ci = (ClipInfo) clipsMap.get(name);
        if (ci == null) {
            System.out.println( "Error: " + name + "not stored");
        } else {
            ci.play(toLoop);
        }
    }

    public void stop(String name) {
        ClipInfo ci = (ClipInfo) clipsMap.get(name);
        if (ci == null) {
            System.out.println( "Error: " + name + "not stored");
        } else {
            ci.stop();
        }
    }

    public void pause(String name) {
        ClipInfo ci = (ClipInfo) clipsMap.get(name);
        if (ci == null) {
            System.out.println( "Error: " + name + "not stored");
        } else {
            ci.pause();
        }
    }

    public void resume(String name) {
        ClipInfo ci = (ClipInfo) clipsMap.get(name);
        if (ci == null) {
          System.out.println( "Error: " + name + "not stored");
        } else {
            ci.resume();
        }
    }

    public void setWatcher(String name, SoundsWatcher sw) {
        ClipInfo ci = (ClipInfo) clipsMap.get(name);
        if (ci == null) {
            System.out.println( "Error: " + name + "not stored");
        } else {
            ci.setWatcher(sw);
        }
          
    }

    public long getClipLength(String name) {
        ClipInfo ci = (ClipInfo) clipsMap.get(name);
        if (ci == null) {
            System.out.println("Error: " + name + "not stored");
            throw new IllegalArgumentException("Clip not found.");
        } else {
            return ci.getLength();
        }
    }

    public boolean isPlaying(String name) {
        ClipInfo ci = (ClipInfo) clipsMap.get(name);
        if (ci == null) {
            System.out.println("Error: " + name + "not stored");
            throw new IllegalArgumentException("Clip not found.");
        } else {
            return ci.isPlaying();
        }
    }
}
