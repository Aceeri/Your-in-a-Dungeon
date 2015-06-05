package main.ui;

import main.misc.Vector2;

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
		// fire userinterfaces that use hover
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
	
	public void click() { }
	
	public void clickDown() { }
	
	public void clickUp() { }
	
	public void hoverEntered() { }
	
	public void hoverLeft() { }
}
