package main.object.wall;

import main.misc.Vector2;

public class Wall extends main.object.Object {

	public Wall(String orientation, Vector2 position) {
		super(position);
		
		collidable = true;
		stretch = true;
		
		switch(orientation) {
			case "top":
				path = "resources\\image\\wall_top.png";
				offsetSize = new Vector2(660, 200);
				break;
			case "bottom":
				path = "resources\\image\\wall_top.png";
				rotation = 180;
				offsetSize = new Vector2(660, 200);
				break;
			case "left":
				path = "resources\\image\\wall_left.png";
				offsetSize = new Vector2(200, 240);
				break;
			case "right":
				path = "resources\\image\\wall_left.png";
				rotation = 180;
				offsetSize = new Vector2(200, 240);
				break;
			case "corner":
				path = "resources\\image\\wall_corner.png";
				offsetSize = new Vector2(200, 200);
				break;
		}
		
		setImage();
	}
	
	
}
