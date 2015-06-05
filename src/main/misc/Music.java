package main.misc;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Music {
	
	private String path;
	private File file;
	private Clip clip;
	private FloatControl volumeControl;
	private BooleanControl muteControl;
	
	public boolean loop = false;
	public boolean mute = false;
	public float volume = 0;
	
	public Music(String p) {
		setMusic(p);
	}
	
	public void setMusic(String p) {
		path = p;
		file = new File(path);
		if (!file.exists()) {
			throw new IllegalArgumentException("No music at path: " + path);
		}
		
		try {
			// get audio from file and set controls for volume and mute
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
	        clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	        muteControl = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
		}
		catch (Exception e) {
			System.out.println("Error while setting music");
		}
	}
	
	public void play() {
		try {
			// start from beginning and play
	        if (loop) {
		        clip.setLoopPoints(0, -1);
		        clip.loop(Clip.LOOP_CONTINUOUSLY);
	        } else {
	        	clip.setFramePosition(0);
	        	clip.start();
	        }
	    } catch (IllegalArgumentException e) {
	    	// clip could not play, stop and try again
	    	stop();
	    	clip.setFramePosition(0);
	        play();
	    }
	}
	
	public void stop() {
		clip.stop();
	}
	
	public void setVolume(double volume) {
		// convert to decibels
		float decibels = (float) (((Math.log(volume) / Math.log(10.0f) * 20.0f))); 
		
		// clamp
		if (decibels > volumeControl.getMaximum()) {
			decibels = volumeControl.getMaximum();
		} else if (decibels < volumeControl.getMinimum()) {
			decibels = volumeControl.getMinimum();
		}
		
		volumeControl.setValue(decibels);
	}
	
	public void fadeToNewSong(String pathToNewSong) {
		Music song = this;
		if (!pathToNewSong.equals(path)) {
			new Thread() {
				public void run() {
					// start at current volume and iterate down then play new song for smoother transition
					
					float previousVolume = volumeControl.getValue();
					for (float i = volumeControl.getValue(); i > -30; i -= .1) {
						try {
							Thread.sleep(8);
							volumeControl.setValue(i);
						} catch (InterruptedException e) { }
					}
					
					song.stop();
					setMusic(pathToNewSong);
					play();
					volumeControl.setValue(previousVolume);
				}
			}.start();
		}
	}
	
	public void mute() {
		muteControl.setValue(true);
	}
	
	public void unmute() {
		muteControl.setValue(false);
	}
}
