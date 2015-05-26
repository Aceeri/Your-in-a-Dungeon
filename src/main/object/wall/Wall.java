package main.object.wall;

import main.misc.Vector2;

public class Wall extends main.object.Object {

	public Wall(Vector2 position, Vector2 size) {
		super(position);
		Size = size;
		
		collidable = true;
		stretch = true;
		path = "resources\\image\\topwall1.png";
		
		setImage();
	}
	
	
}
