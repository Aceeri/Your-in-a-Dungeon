package main.character.classes;

import main.character.Player;
import main.misc.Vector2;
import main.object.Projectile;

@SuppressWarnings("serial")
public class Battlemage extends Player {
	
	public boolean timeSlowed = false;
	private double timeExpiration = 0;

	public Battlemage(Vector2 position) {
		super(position);
		
		health = 15;
		maxHealth = 15;
		speed = 5; //don't fucking go over 8
		damage = 5;
		range = 800;
		attackspeed = 500;
		projectilespeed = 6;
		
		ability1speed = 5000;
		ability2speed = 5000;
	}
	
	public void step(double delta) {
		super.step(delta);
		if (timeSlowed) {
			timeExpiration -= delta*1000;
			if (timeExpiration <= 0) {
				manager.fixedFps += 40;
				timeSlowed = false;
			}
		}
	}
	
	//ability overrides
	public void ability1() {
		if (ability1 <= 0) {
			//circle of projectiles
			for (double i = 0; i < 360; i += 10) {
				Projectile p = new Projectile(this, new Vector2(Math.cos(i*Math.PI/180), Math.sin(i*Math.PI/180)), 3, 500, 5);
				manager.projectileContainer.add(p);
			}
			
			ability1 += ability1speed;
		}
	}
	
	public void ability2() {
		if (ability2 <= 0) {
			//TIME POTATO
			timeSlowed = true;
			timeExpiration = 4000;
			manager.fixedFps -= 40;
			
			ability2 += ability2speed;
		}
	}
}
