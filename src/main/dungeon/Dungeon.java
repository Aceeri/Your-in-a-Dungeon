package main.dungeon;

import java.util.ArrayList;

import main.misc.Vector2;

public class Dungeon {
	
	public Room[][] rooms;
	public Vector2 maxSize;
	public boolean finished = false;
	
	public ArrayList<Room> branches = new ArrayList<Room> ();
	
	public Dungeon(Vector2 maxSize) {
		this.maxSize = maxSize;
	}
	
	/*public void generate() {
		int startX = (int) (Math.random() * maxSize.x);
		int startY = (int) (Math.random() * maxSize.y);
		
		Room start = new Room(startX, startY);
		start.generate(-1, 0);
		rooms[startX][startY] = start;
		
		Room current = start;
		while (!finished) {
			ArrayList<Vector2> open = current.directions();
			
			for (int i = 0; i < open.size(); i++) {
				Vector2 openDirection = open.get(i);
				
				if (!(openDirection.x > maxSize.x
						|| openDirection.x < 0
						|| openDirection.y > maxSize.y
						|| openDirection.y < 0)) {
					Room newRoom = new Room((int) openDirection.x, (int) openDirection.y);
					newRoom.generate(entrance, roomsFromStart);, roomsFromStart);
				}
			}
		}
	}*/
}
