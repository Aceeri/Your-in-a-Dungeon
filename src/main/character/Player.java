package main.character;

import main.misc.Vector2;
import main.Manager;

public class Player extends main.object.Object {
	public double health = 10;
	public String name;
	
	public Player(Vector2 position) {
		super(position);
		
		Size = new Vector2(20, 20);
		collidable = true;
		anchored = false;
		
		setSize(this.Size.dimension());
		setLocation(0, 0);
		
		type = "player";
		path = "resources/image/player.png";
		
		setImage();
	}
}