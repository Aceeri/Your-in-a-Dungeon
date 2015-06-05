package main.dungeon;

import java.util.ArrayList;

import main.character.Enemy;
import main.character.enemies.*;
import main.misc.Vector2;

public class SpawnLocation {
	private Vector2 position;
	private Vector2 size;
	private String[] enemies;
	
	public int minimum = 2;
	public int maximum = 4;
	
	public double minimumScale = 1;
	public double maximumScale = 1;

	public SpawnLocation(String[] enemies, Vector2 position, Vector2 size) {
		this.position = position;
		this.size = size;
		this.enemies = enemies;
	}
	
	public Enemy[] generateEnemies() {
		ArrayList<Enemy> enemyList = new ArrayList<Enemy> ();
		int maxEnemies = (int) (Math.round(minimum + Math.random()*maximum));
		
		// spawn a random amount of enemies between given vars
		for (int i = 0; i < maxEnemies; i++) {
			String newEnemy = enemies[(int) (Math.round(Math.random()*(enemies.length - 1)))];
			Enemy enemy = evaluateString(newEnemy);
			double x = position.x + Math.random()*(size.x - enemy.offsetSize.x - position.x);
			double y = position.y + Math.random()*(size.y - enemy.offsetSize.y - position.y);
			double scale = minimumScale + Math.random()*(maximumScale - minimumScale);
			
			enemy.offsetPosition = new Vector2(x, y);
			enemy.scale = scale;
			enemy.offsetSize = enemy.offsetSize.scalar(scale);
			enemy.damage *= scale;
			if (Math.random() > .5) {
				enemy.direction = -1;
			}
			
			enemyList.add(enemy);
		}
		
		Enemy[] convertedList = new Enemy[enemyList.size()];
		for (int i = 0; i < enemyList.size(); i++) {
			convertedList[i] = enemyList.get(i);
		}
		
		return convertedList;
	}
	
	// evaluate strings to enemy classes
	public Enemy evaluateString(String enemyName) {
		switch (enemyName) {
			case "Wraith":
				return new Wraith(new Vector2());
			case "Spider":
				return new Spider(new Vector2());
		}
		
		return null;
	}
}
