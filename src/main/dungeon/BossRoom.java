package main.dungeon;

import main.character.Enemy;
import main.character.Player;
import main.character.enemies.BossSpider;
import main.character.enemies.BossWraith;
import main.misc.Vector2;
import main.object.Projectile;

public class BossRoom extends Room {

	public BossRoom(String bossName, int gridX, int gridY) {
		super(gridX, gridY);
		
		System.out.println("Boss: " + bossName);
		
		Enemy boss = new Enemy(new Vector2(880, 440));
		
		switch (bossName) {
			case "Wraith": 
				boss = new BossWraith(new Vector2(880, 440));
				break;
			case "Spider":
				boss = new BossSpider(new Vector2(880, 440));
				break;
		}
		
		objects.add(boss);
		
		type = "boss";
	}
}
