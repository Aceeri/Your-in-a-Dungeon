package main.dungeon;

import main.character.Enemy;
import main.character.Player;
import main.character.enemies.BossSpider;
import main.character.enemies.BossWraith;
import main.misc.Vector2;
import main.object.Projectile;

public class BossRoom extends Room {
	
	public String[] bosses = new String[] {
			//"Wraith",
			"Spider"
	};

	public BossRoom(int gridX, int gridY) {
		super(gridX, gridY);
		
		String random = (bosses[(int) (Math.random()*(bosses.length))]);
		System.out.println("Boss: " + random);
		
		Enemy boss = new Enemy(new Vector2(880, 440));
		switch (random) {
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
