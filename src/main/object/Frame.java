package main.object;

import main.misc.Vector2;

public class Frame {
	
	public double current = 0.0;
	public double max = 0;
	public String behavior = "regular";
	// regular	-- picture frames
	// tween 	-- move position over time
	
	public String path;
	public Vector2 tweenPosition = new Vector2();
	public Vector2 currentPosition = new Vector2();
	
	public Frame(String resource, double t) {
		behavior = "regular";
		max = t;
		path = resource;
	}
	
	public Frame(Vector2 originalPosition, Vector2 newPosition, double t) {
		behavior = "tween";
		max = t/1000;
		currentPosition = originalPosition;
		tweenPosition = newPosition.sub(originalPosition);
	}
	
	public boolean step(double delta) {
		current += delta;
		if (behavior == "tween") {
			currentPosition = currentPosition.add(tweenPosition.scalar(delta/max));
		}
		if (current >= max) {
			return true;
		}
		return false;
	}
}
