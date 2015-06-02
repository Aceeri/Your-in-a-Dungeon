package main;

import main.Manager;
import main.misc.Vector2;

public class Main {
	
	public static Window window;
	public static Manager manager;
	
	public static void main(String[] args) {
		window = new Window();
		manager = new Manager(window);
		window.manager = manager;
		
		//window.setWindowed();
		window.setFullscreen();
	}
}