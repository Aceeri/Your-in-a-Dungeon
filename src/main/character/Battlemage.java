package main.character;

import main.misc.Vector2;
import main.object.Projectile;

public class Battlemage extends Player {

	public Battlemage(Vector2 position) {
		super(position);
		
		health = 15;
		speed = 5; //don't fucking go over 8
		damage = 5;
		range = 600;
		attackspeed = 750;
		projectilespeed = 15;
		
		ability1speed = 5000;
		ability2speed = 5000;
	}
	
	//ability overrides
	public void ability1() {
		if (ability1 <= 0) {
			//circle of projectiles
			for (double i = 0; i < 360; i += 10) {
				Projectile p = new Projectile(this, new Vector2(Math.cos(i*Math.PI/180), Math.sin(i*Math.PI/180)), 3, 500, 5);
				p.speed = 5;
				manager.projectileContainer.add(p);
			}
			
			ability1 += ability1speed;
		}
	}
	
	public void ability2() {
		if (ability2 <= 0) {
			//blink (shortrange teleport)
			offsetPosition = offsetPosition.add(velocity.scalar(150));
			
			ability2 += ability2speed;
		}
	}
}
