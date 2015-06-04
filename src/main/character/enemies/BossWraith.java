package main.character.enemies;

import main.misc.Vector2;
import main.object.Projectile;

public class BossWraith extends Wraith {

	public BossWraith(Vector2 p) {
		super(p);
		
		health = 50;
		maxHealth = 50;
		scale = 3;
		score = 10000;
		
		attackspeed = 200;
		damage = 1;
	}
	
	public void ability1() {
		if (aggro != null) {
			Vector2 originalPosition = aggro.position;
			double angleToPosition = Math.atan2((originalPosition.y - position.y), (originalPosition.x - position.x));
			for (int i = 0; i < 45; i++) {
				Vector2 projectileDirection = new Vector2(Math.cos((i - 22.5)*Math.PI/180 + angleToPosition), Math.sin((i - 22.5)*Math.PI/180 + angleToPosition)).normalize();
				
				if (projectileDirection.x > 0) {
					direction = -1;
				} else {
					direction = 1;
				}
				
				Projectile p = new Projectile(this, projectileDirection, .5, range, 6);
				manager.projectileContainer.add(p);
			}
		}
	}
	
	public void ability2() {
		if (aggro != null) {
			
		}
	}
}
