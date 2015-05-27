package main.ui;

import main.misc.Vector2;

@SuppressWarnings("serial")
public class Button extends UserInterface {
	
	private String path;
	
	public Button(String path, Vector2 position, Vector2 size) {
		this.path = path;
		offsetSize = size;
		stretch = true;
		setImage();
	}
	
	public void clicked() {
		System.out.println("clicked");
	}
	
	public void hover() {
		System.out.println("hover");
	}
}
