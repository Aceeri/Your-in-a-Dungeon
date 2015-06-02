package main.character.enemies;

import main.character.Enemy;
import main.character.Player;
import main.misc.Vector2;
import main.object.Frame;

public class Spider extends Enemy {
	
	public Spider(Vector2 p) {
		super(p);
		
		offsetSize = new Vector2(100, 100);
		
		name = "Spider";
		path = "resources\\image\\spider.png";
		imageY = "center";
		imageX = "center";
		sight = 300;
		range = 35;
		damage = 1;
		score = 100;
		
		animator.defineAnimation("walk", new Frame[] {
				new Frame("resources\\image\\spider.png", .2),
				new Frame("resources\\image\\spider_walk_1.png", .2),
				new Frame("resources\\image\\spider_walk_2.png", .2),
				new Frame("resources\\image\\spider_walk_3.png", .2),
		});
		
		animator.defineAnimation("attack", new Frame[] {
				new Frame("resources\\image\\spiderattack.png", .5),
				new Frame("resources\\image\\spider.png", 0),
		});
	}
	
	public void attack(Player player) {
		player.takeDamage(damage);
	}
}
