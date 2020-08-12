
package com.snake.graphics.sound;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Javier Martinez
 */
public class ClipInfo implements LineListener {
    // VARIABLES
    private final String name;
    private final String direccion;
    private Clip clip = null;
    private boolean isLooping = false;
    private SoundsWatcher watcher = null;
    private final DecimalFormat df; 
    // CONSTRUCTOR
    public ClipInfo(String key, String fichero) {
        name = key;
        direccion = "/src/resources/" + "sonidos"
                            + "/" + fichero;
        df = new DecimalFormat("0.#");
        loadClip(direccion);
    }
    // CLASS METHODS
    private void loadClip(String filename) {
        try {
            Path path = Paths.get(new File("").getAbsolutePath() + filename);
            File fichero = new File(path.toString());
            AudioInputStream stream = AudioSystem.getAudioInputStream(fichero);
            AudioFormat format = stream.getFormat();
            if ( (format.getEncoding() == AudioFormat.Encoding.ULAW) ||
                    (format.getEncoding() == AudioFormat.Encoding.ALAW) ) {
                AudioFormat newFormat = 
                    new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                            format.getSampleRate(),
                            format.getSampleSizeInBits()*2,
                            format.getChannels(),
                            format.getFrameSize()*2,
                            format.getFrameRate(), true);
                stream = AudioSystem.getAudioInputStream(newFormat, stream);
                System.out.println("Converted Audio format: " + newFormat);
                format = newFormat;
            }
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Unsupported Clip File: " + filename);
                return;
            }
            clip = (Clip) AudioSystem.getLine(info);
            clip.addLineListener(this);
            clip.open(stream);
            stream.close();
        }
        
        catch (UnsupportedAudioFileException audioException) {
            System.out.println("Unsupported audio file: " + filename);
        }
        catch (LineUnavailableException noLineException) {
            System.out.println("No audio line available for : " + filename);
        }
        catch (IOException ioException) {
            System.out.println("Could not read: " + filename);
        }
        catch (Exception e) {
            System.out.println("Problem with " + filename);
            System.err.println(e.getMessage());
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println(element.toString());
            }
        }
    }
    
    @Override
    public void update(LineEvent lineEvent) {
        // when clip is stopped / reaches its end
        if (lineEvent.getType() == LineEvent.Type.STOP) {
            clip.stop();
            clip.setFramePosition(0);  // NEW
            if (!isLooping) {  // it isn't looping
              if (watcher != null)
                watcher.atSequenceEnd(name, SoundsWatcher.STOPPED);
            }
            else {      // else play it again
              clip.start();
              if (watcher != null)
                watcher.atSequenceEnd(name, SoundsWatcher.REPLAYED);
            }
        }
    }
    
    public void close() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
    
    public void play(boolean toLoop) {
        if (clip != null) {
            isLooping = toLoop;
            clip.start();
        }
    }

    public void stop() {
        if (clip != null) {
            isLooping = false;
            clip.stop();
            clip.setFramePosition(0);
        }
    }

    public void pause() {
        if (clip != null) { clip.stop(); }
    }

    public void resume() {
        if (clip != null) { clip.start(); }
    }
    
    public void setWatcher(SoundsWatcher sw) {
        watcher = sw;  
    }
    
    public boolean isPlaying() {
        return this.clip.isRunning();
    }
    
    public long getLength() {
        return this.clip.getMicrosecondLength();
    }
    
    public String getName() {
        return name;  
    }
}
