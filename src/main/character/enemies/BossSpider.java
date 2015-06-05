package main.character.enemies;

import main.character.Enemy;
import main.character.Player;
import main.dungeon.SpawnLocation;
import main.misc.Vector2;

public class BossSpider extends Spider {
	
	private int chargeTime = 0;
	private SpawnLocation spiders;
	
	public BossSpider(Vector2 p) {
		super(p);
		
		scale = 9;
		offsetSize = new Vector2(300, 300);
		
		health = 150;
		maxHealth = 150;
		
		name = "Boss Spider";
		stretch = false;
		
		range = -270;
		damage = 5;
		speed = 5;
		score = 20000;
		
		ability1 = 5000;
		ability2 = 20000;
		ability1speed = 8500;
		ability2speed = 5000;
		
		spiders = new SpawnLocation(new String[] { "Spider" }, new Vector2(300, 300), new Vector2(1320, 840));
		spiders.minimum = 1;
		spiders.maximum = 6;
	}
	
	public void step(double delta) {
		super.step(delta);
		if (chargeTime > 0) {
			chargeTime -= delta;
		}
	}
	
	// spawn little spiders
	public void ability1() {
		Enemy[] spiderList = spiders.generateEnemies();
		for (int i = 0; i < spiderList.length; i++) {
			Spider s = (Spider) spiderList[i];
			s.health = 1;
			s.maxHealth = 1;
			s.damage = .5;
			s.score = 25;
			manager.playerContainer.add(s);
		}
	}
	
	// move faster towards player
	public void ability2() {
		chargeTime = 120;
		
		while (chargeTime > 0) {
			try {
				Thread.sleep(5);
				speed = 8;
			} catch (InterruptedException e) { }
		}
		
		speed = 5;
		chargeTime = 0;
	}
}
