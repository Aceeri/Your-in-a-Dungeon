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
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
	        clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	        muteControl = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void play() {
		try {
	        if (loop) {
	        	clip.setLoopPoints(0, -1);
	        	clip.loop(Clip.LOOP_CONTINUOUSLY);
	        } else {
	        	clip.start();
	        }
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
	}
	
	public void stop() {
		clip.stop();
	}
	
	public void setVolume(double volume) {
		//convert to decibels
		float decibels = (float) (((Math.log(volume) / Math.log(10.0f) * 20.0f))); 
		
		//clamp
		if (decibels > volumeControl.getMaximum()) {
			decibels = volumeControl.getMaximum();
		} else if (decibels < volumeControl.getMinimum()) {
			decibels = volumeControl.getMinimum();
		}
		
		volumeControl.setValue(decibels);
	}
	
	public void fadeToNewSong(String pathToNewSong) {
		System.out.println("New Song: " + pathToNewSong);
		new Thread() {
			public void run() {
				float previousVolume = volumeControl.getValue();
				for (float i = volumeControl.getValue(); i > -30; i -= .1) {
					try {
						Thread.sleep(10);
						volumeControl.setValue(i);
					} catch (InterruptedException e) { }
				}
				setMusic(pathToNewSong);
				play();
				volumeControl.setValue(previousVolume);
			}
		}.start();
	}
	
	public void mute() {
		muteControl.setValue(true);
	}
	
	public void unmute() {
		muteControl.setValue(false);
	}
}
