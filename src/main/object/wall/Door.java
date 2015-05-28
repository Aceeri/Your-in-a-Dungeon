package main.object.wall;

import main.misc.Vector2;

public class Door extends main.object.Object {
	
	public String orientation;
	
	public Door(String orientation, Vector2 position) {
		super(position.sub(new Vector2(1, 1)));
		offsetSize = new Vector2(202, 202);
		
		this.orientation = orientation;
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
	}
	
	public void open() {
		collidable = false;
	}
	
	public void close() {
		collidable = true;
	}
}
