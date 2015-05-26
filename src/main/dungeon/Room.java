package main.dungeon;

import java.util.ArrayList;

import main.misc.Vector2;

public class Room {
	
	
	// 0 -> north
	// 1 -> east
	// 2 -> south
	// 3 -> west
	public Vector2[] doors = new Vector2[3];
	public boolean start = false;
	public int x;
	public int y;
	
	public Room(int gridX, int gridY) {
		x = gridX;
		y = gridY;
	}
	
	/*public void generate(Vector2 entrance, int roomsFromStart) {
		if (entrance.x + entrance.y == 0) {
			start = true;
		}
		
		
		
		for (int i = 0; i < doors.length; i++) {
			if (!doors[i]) {
				doors[i] = Math.random() > .5;
			}
		}
	}
	
	public ArrayList<Vector2> directions() {
		ArrayList<Vector2> list = new ArrayList<Vector2> ();
		
		if (doors[0]) {
			list.add(new Vector2(x, y - 1));
		}
		if (doors[1]) {
			list.add(new Vector2(x + 1, y));
		}
		if (doors[3]) {
			list.add(new Vector2(x, y + 1));
		}
		if (doors[4]) {
			list.add(new Vector2(x - 1, y));
		}
		
		return list;
	}*/
}
