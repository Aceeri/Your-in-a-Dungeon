package main.dungeon;

import main.character.Enemy;
import main.misc.Vector2;

public class SpiderRoom extends Room {
	
	public SpiderRoom(int gridX, int gridY) {
		super(gridX, gridY);
	}
	
	public void generate() {
		super.generate();
		
		String[] enemyList = new String[] {
				"Spider",
		};
		
		SpawnLocation topLeft = new SpawnLocation(enemyList, new Vector2(500, 500), new Vector2(1120, 640));
		topLeft.maximum = 8;
		topLeft.minimum = 3;
		topLeft.minimumScale = .8;
		topLeft.maximumScale = 1.3;
		Enemy[] enemies = topLeft.generateEnemies();
		
		for (int i = 0; i < enemies.length; i++) {
			objects.add(enemies[i]);
		}
	}
}
