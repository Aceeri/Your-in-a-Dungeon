package main.character.enemies;

import main.character.Player;
import main.misc.Vector2;

public class BossSpider extends Spider {
	
	public BossSpider(Vector2 p) {
		super(p);
		
		scale = 9;
		offsetSize = new Vector2(300, 300);
		
		health = 70;
		maxHealth = 70;
		
		name = "Boss Spider";
		stretch = false;
		
		range = -290;
		damage = 1;
		speed = 2;
		score = 10000;
	}
	
	// spawn little spiders
	public void ability1() {
		int amount = (int) (2 + Math.random() * 4); // amount of spiders to spawn
		
		for (int i = 0; i < amount; i++) {
			Spider littlespider = new Spider(position);
			littlespider.scale = .4;
			littlespider.health = 1;
			littlespider.maxHealth = 1;
			littlespider.damage = .3;
			littlespider.collidable = false;
			littlespider.offsetSize = littlespider.offsetSize.scalar(littlespider.scale);
			manager.playerContainer.add(littlespider);
		}
	}
	
	// charge
	public void ability2() {
		Player player = getNearestPlayer();
		Vector2 originalPosition = player.offsetPosition;
		Vector2 thisOriginalPosition = offsetPosition;
		
		
	}
}
