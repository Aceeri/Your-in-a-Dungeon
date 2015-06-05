package main.character.classes;

import main.character.Player;
import main.misc.Vector2;
import main.object.Frame;
import main.object.Projectile;

@SuppressWarnings("serial")
public class Battlemage extends Player {
	
	public boolean timeSlowed = false;
	private double timeExpiration = 0;

	public Battlemage(Vector2 position) {
		super(position);
		
		health = 15;
		maxHealth = 15;
		speed = 6;
		damage = 5;
		range = 800;
		attackspeed = 550;
		projectilespeed = 9;
		path = "resources\\image\\btlmgbrownhairstage1.png";
		scale = 2;
		projectilePath = "resources\\image\\fireball2.png";
		
		ability1speed = 5000;
		ability2speed = 5000;
		
		animator.defineAnimation("walk", new Frame[] {
				new Frame("resources\\image\\btlmgbrownhairstage1.png", .2),
				new Frame("resources\\image\\btlmgbrownhairstage2.png", .2)
		});
		
		animator.defineAnimation("attack", new Frame[] {
				new Frame("resources\\image\\btlmgattack.png", .3),
				new Frame("resources\\image\\btlmgbrownhairstage1.png", .01)
		});
	}
	
	public void step(double delta) {
		super.step(delta);
		
		// update time to check if it should still be slowed
		if (timeSlowed) {
			timeExpiration -= delta*1000;
			if (timeExpiration <= 0) {
				manager.fixedFps += 40;
				timeSlowed = false;
			}
		}
	}
	
	// circle of lightning
	public void ability1() {
		if (ability1 <= 0) {
			animator.playAnimation("attack", false);
			
			for (double i = 0; i < 360; i += 10) {
				Projectile p = new Projectile(this, "resources\\image\\bightninglolt.png", new Vector2(Math.cos(i*Math.PI/180), Math.sin(i*Math.PI/180)), 1, 500, 5);
				manager.projectileContainer.add(p);
			}
			
			ability1 += ability1speed;
		}
	}
	
	// slow time ability
	public void ability2() {
		if (ability2 <= 0) {
			animator.playAnimation("attack", false);
			
			timeSlowed = true;
			timeExpiration = 4000;
			manager.fixedFps -= 40;
			
			ability2 += ability2speed;
		}
	}
}
