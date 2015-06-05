package main.character.enemies;

import main.character.Enemy;
import main.character.Player;
import main.misc.Vector2;
import main.object.Frame;
import main.object.Projectile;

@SuppressWarnings("serial")
public class Wraith extends Enemy {

	public Wraith(Vector2 p) {
		super(p);
		
		stretch = false;
		offsetSize = new Vector2(60, 70);
		scale = 3;
		imageY = "bottom";
		imageX = "center";
		path = "resources\\image\\wraith.png";
		sight = 600;
		range = 500;
		damage = 1;
		score = 150;
		attackspeed = 500;
		projectilespeed = 20;
		
		health = 12;
		maxHealth = 12;
		
		animator.defineAnimation("attack", new Frame[] {
				new Frame("resources\\image\\wraith_attack_1.png", .2),
				new Frame("resources\\image\\wraith_attack_2.png", .2),
				new Frame("resources\\image\\wraith_attack_1.png", .1),
				new Frame("resources\\image\\wraith.png", .01),
		});
	}
	
	public void attack(Player player) {
		try {
			Thread.sleep(550);
		} catch (InterruptedException e) { }
		
		if (getParent() != null) {
			Vector2 projectileDirection = player.position.add(player.Size.scalar(.5)).sub(position.add(Size.scalar(.5))).normalize();
			
			if (projectileDirection.x > 0) {
				direction = -1;
			} else {
				direction = 1;
			}
			
			Projectile p = new Projectile(this, "resources\\image\\wraithshot.png", projectileDirection, damage, range, projectilespeed);
			manager.projectileContainer.add(p);
		}
	}
}
