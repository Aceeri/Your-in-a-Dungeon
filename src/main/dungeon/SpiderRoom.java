package main.dungeon;

import main.character.Enemy;
import main.character.enemies.Spider;
import main.misc.Vector2;
import main.object.wall.Wall;

public class SpiderRoom extends Room {
	
	public SpiderRoom(int gridX, int gridY) {
		super(gridX, gridY);
	}
	
	public void generate() {
		super.generate();
		
		String[] enemyList = new String[] {
				"Spider",
		};
		
		SpawnLocation topLeft = new SpawnLocation(enemyList, new Vector2(300, 300), new Vector2(1320, 840));
		topLeft.maximum = 15;
		topLeft.minimum = 7;
		topLeft.minimumScale = .8;
		topLeft.maximumScale = 1.3;
		Enemy[] enemies = topLeft.generateEnemies();
		
		for (int i = 0; i < enemies.length; i++) {
			objects.add(enemies[i]);
		}
	}
}
