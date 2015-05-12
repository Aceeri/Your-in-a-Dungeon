package main.object.floor;

import main.misc.Vector2;
import main.Manager;

public class Background extends main.object.Object {
	
	public Background(Manager manager) {
		super(manager, new Vector2());
		Size = manager.screen;
		
		type = "floor";
		path = "resources/image/background.png";
		
		setImage();
	}
}
