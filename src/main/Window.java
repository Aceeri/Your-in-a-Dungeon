package main;

import java.awt.Frame;
import java.io.FileInputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import main.misc.Vector2;

public class Window {
	
	public boolean fullscreen = false;
	public Vector2 minimumSize = new Vector2(400, 300);
	public Manager manager;
	public JFrame frame;
	private JFrame holder;
	private String icon = "resources\\image\\dungen.png";
	
	public Window() {
		super();
		newFrame();
	}
	
	public void setFullscreen() {
		newFrame();
		
		// removes borders and extends to entire screen
		frame.setUndecorated(true);
		frame.pack();
		frame.add(manager);
		frame.setVisible(true);
		
		fullscreen = true;
		
		if (holder != null) {
			holder.dispose(); // disposes of old window
		}
		
		System.out.println("Fullscreen mode");
	}
	
	public void setWindowed() {
		newFrame();
		
		// keeps borders and maximizes to screen
		Vector2 screen = new Vector2();
		frame.setLocation(screen.scalar(.1).point()); // set minimized screen location and size
		frame.setSize(screen.scalar(.85).dimension());
		frame.pack();
		frame.add(manager);
		frame.setVisible(true);
		
		fullscreen = false;
		
		if (holder != null) {
			holder.dispose(); // disposes of old window
		}
		
		manager.resolutionChange();
		
		System.out.println("Windowed mode");
	}
	
	private void newFrame() {
		// create default JPanel
		holder = frame;
		
		frame = new JFrame();
		frame.setTitle("You're in a Dungeon");
		frame.setMinimumSize(minimumSize.dimension());
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		try {
			frame.setIconImage(ImageIO.read(new FileInputStream(icon)));
        } catch(Exception e) {
        	System.out.println("missing logo");
        }
	}
}
