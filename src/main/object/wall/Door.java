package main.object.wall;

import main.misc.Vector2;

public class Door extends main.object.Object {
	
	public String orientation;
	
	public Door(String orientation) {
		super(new Vector2());
		offsetSize = new Vector2(202, 202);
		
		this.orientation = orientation;
		offsetPosition = new Vector2(860, 440).add(doorVector().mult(new Vector2(860, 440)));
		collidable = true;
		stretch = true;
		
		switch (orientation) {
			case "top": 
				path = "resources\\image\\door_top.png";
				break;
			case "bottom":
				path = "resources\\image\\door_top.png";
				rotation = 180;
				break;
			case "left":
				path = "resources\\image\\door_left.png";
				break;
			case "right":
				path = "resources\\image\\door_left.png";
				rotation = 180;
				break;
		}
		
		type = "wall";
	}
	
	public Vector2 doorVector() {
		switch (orientation) {
			case "top": 
				return new Vector2(0, -1);
			case "bottom":
				return new Vector2(0, 1);
			case "left":
				return new Vector2(-1, 0);
			case "right":
				return new Vector2(1, 0);
		}
		
		return null;
	}
	
	public void open() {
		collidable = false;
	}
	
	public void close() {
		collidable = true;
	}
}
