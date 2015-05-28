package main.ui;

import main.misc.Vector2;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Graphics;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class UserInterface extends main.object.Object {
	
	public boolean previousHovering = false;
	public boolean hovering = false;
	
	public UserInterface() {
		super(new Vector2());
	}
	
	public UserInterface(Vector2 p) {
		super(p);
	}
	
	public void step(double delta) {
		paintLocation();
		if (hovering != previousHovering) {
			if (hovering) {
				hoverEntered();
			} else {
				hoverLeft();
			}
			
			previousHovering = hovering;
		}
		update();
	}
	
	// Override
	public void click() { }
	
	// Override
	public void clickDown() { }
	
	// Override
	public void clickUp() { }
	
	// Override
	public void hoverEntered() { }
	
	// Override
	public void hoverLeft() { }
}
