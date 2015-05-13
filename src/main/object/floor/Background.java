package main.object.floor;

import main.misc.Vector2;
import main.Manager;

public class Background extends main.object.Object {
	
	public Background(Vector2 s) {
		super(new Vector2());
		Size = s;
		
		type = "floor";
		path = "resources/image/background.png";
		
		setImage();
	}
}
