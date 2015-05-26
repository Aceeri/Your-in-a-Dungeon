package main;

import main.misc.Vector2;
import main.Manager;

public class Main {
	
	public static Window window;
	public static Manager manager;
	
	public static Vector2 screen = new Vector2(1920, 1080);
	//public static Vector2 defaultScreen = new Vector2(800, 600);
	
	public static void main(String[] args) {
		window = new Window();
		manager = new Manager(window);
		window.manager = manager;
		
		window.setWindowed(screen);
	}
}