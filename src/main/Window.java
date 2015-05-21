package main;

import java.awt.Frame;

import javax.swing.JFrame;

import main.misc.Vector2;

public class Window {
	
	public boolean fullscreen = false;
	public Manager manager;
	public JFrame frame;
	
	public Window() {
		super();
		
		frame = new JFrame();
		frame.setTitle("You're in a Dungeon");
		frame.setMinimumSize(new Vector2(400, 300).dimension());
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void setManager(Manager m) {
		manager = m;
	}
	
	public void setFullscreen() {
		JFrame holder = frame;
		
		frame = new JFrame();
		frame.setTitle("You're in a Dungeon");
		frame.setUndecorated(true);
		frame.pack();
		frame.setMinimumSize(new Vector2(400, 300).dimension());
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.add(manager);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		fullscreen = true;
		
		if (holder != null) {
			holder.dispose(); // disposes of old window
		}
	}
	
	public void setWindowed(Vector2 screen) {
		JFrame holder = frame;
		
		frame = new JFrame();
		frame.setTitle("You're in a Dungeon");
		frame.pack();
		frame.setLocation(screen.scalar(.1).point());
		frame.setMinimumSize(new Vector2(400, 300).dimension());
		frame.setSize(screen.scalar(.85).dimension());
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.add(manager);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		fullscreen = false;
		
		if (holder != null) {
			holder.dispose(); // disposes of old window
		}
	}
}
