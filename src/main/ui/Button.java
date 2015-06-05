package main.ui;

import main.misc.Vector2;
import main.object.Animator;

@SuppressWarnings("serial")
public class Button extends main.ui.UserInterface {
	public String text = "Play";
	public Animator animator;
	
	public Button(String name, String imagePath, Vector2 position, Vector2 size) {
		super(position);
		offsetSize = size;
		stretch = true;
		path = "resources\\image\\button.png";
		
		TextLabel playLabel = new TextLabel(name, new Vector2(50, 52));
		playLabel.size = 65f;
		labels.add(playLabel);
		
		animator = new Animator(this);
	}
	
	public void step(double delta) {
		super.step(delta);
		animator.step(delta);
	}
	
	// hooked to events
	public void hoverEntered() {
		path = "resources\\image\\button_hover.png";
	}
	
	public void hoverLeft() {
		path = "resources\\image\\button.png";
	}
	
	public void click() {
		System.out.println("unset click");
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
	
	public String toString() {
		return "button";
	}
}
