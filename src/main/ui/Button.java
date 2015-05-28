package main.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;

import main.misc.Vector2;

@SuppressWarnings("serial")
public class Button extends main.ui.UserInterface {
	public String text = "Play";
	
	public Button(String imagePath, Vector2 position, Vector2 size) {
		super(position);
		offsetSize = size;
		stretch = true;
		path = "resources\\image\\button.png";
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	public void hoverEntered() {
		path = "resources\\image\\button_hover.png";
	}
	
	public void hoverLeft() {
		path = "resources\\image\\button.png";
	}
	
	public void click() {
		// start
		System.out.println("clicked");
	}
	
	public void clickDown() {
		path = "resources\\image\\button_down.png";
	}
	
	public void clickUp() {
		if (hovering) {
			path = "resources\\image\\button_hover.png";
		} else {
			path = "resources\\image\\button.png";
		}
	}
}
