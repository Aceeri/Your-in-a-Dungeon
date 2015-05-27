package main.dungeon;

import java.util.ArrayList;

import main.misc.Vector2;
import main.object.wall.Wall;

public class Room {
	
	
	// 0 -> north
	// 1 -> east
	// 2 -> south
	// 3 -> west
	public Wall[] walls;
	public Vector2[] doors = new Vector2[3];
	public boolean start = false;
	public int x;
	public int y;
	
	public Room(int gridX, int gridY) {
		x = gridX;
		y = gridY;
	}
	
	public void generate() {
		
	}
}
