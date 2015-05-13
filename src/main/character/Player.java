package main.character;

import main.misc.Vector2;
import main.object.Projectile;
import main.Manager;

public class Player extends main.object.Object {
	
	public double health = 10;
	
	public double cooldown = 0;
	public double ability1Cooldown = 0;
	public double ability2Cooldown = 0;
	
	public double cooldownTime = 1000;
	public double ability1Time = 1000;
	public double ability2Time = 1000;
	
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
	
	public void step() {
		super.step();
		
		cooldown = cooldown > 0 ? cooldown - 16 : 0;
		ability1Cooldown = ability1Cooldown > 0 ? ability1Cooldown - 16 : 0;
		ability2Cooldown = ability2Cooldown > 0 ? ability2Cooldown - 16 : 0;
	}
	
	//override these with new abilities
	public void ability1() { }
	
	public void ability2() { }
}