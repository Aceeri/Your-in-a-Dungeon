package main;

import main.misc.Vector2;
import main.Manager;

import javax.swing.JFrame;
import java.awt.Frame;
import java.awt.Toolkit;

public class Main {
	
	public static JFrame window;
	public static Manager manager;
	
	public static Vector2 screen = new Vector2(1440, 900);
	public static Vector2 defaultScreen = new Vector2(1440, 900);
	public static Vector2 fullScreen = new Vector2(Toolkit.getDefaultToolkit().getScreenSize());
	
	public static void main(String[] args) throws InterruptedException {
		manager = new Manager(window);
		
		setWindowed();
		//setFullscreen();
	}
	
	public static void setFullscreen() {
		window = new JFrame();
		window.setTitle("You're in a Dungeon");
		window.setUndecorated(true);
		window.pack();
		window.setMinimumSize(new Vector2(400, 300).dimension());
		window.setExtendedState(Frame.MAXIMIZED_BOTH);
		window.add(manager);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		
		manager.fullscreen = true;
		manager.window = window;
	}
	
	public static void setWindowed(Vector2 screen) {
		window = new JFrame();
		window.setTitle("You're in a Dungeon");
		window.pack();
		window.setLocation(screen.scalar(.1).point());
		window.setMinimumSize(new Vector2(400, 300).dimension());
		window.setSize(screen.scalar(.85).dimension());
		window.setExtendedState(Frame.MAXIMIZED_BOTH);
		window.add(manager);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		
		manager.fullscreen = false;
		manager.window = window;
	}
	
	public static void setWindowed() {
		setWindowed(screen);
	}
	
	/*private static boolean intersects(Vector2 o1, Vector2 d1, Vector2 o2, Vector2 d2) {
		double u = ((o2.x-o1.x)*d1.y-(o2.y-o1.y)*d1.x)/(d2.y*d1.x-d2.x*d1.y);
		if (0 <= 0 && u <= 1) {
			double v = (o2.x-o1.x+u*d2.x)*d1.x+(o2.y-o1.y+u*d2.y)*d1.y;
			return (0 <= v && v <= 1);
		}
		return false;
	}*/
}