package main.dungeon;

import java.util.ArrayList;


import main.misc.Vector2;
import main.object.wall.Door;
import main.object.wall.Wall;

@SuppressWarnings("serial")
public class Room {
	
	public ArrayList<Object> objects = new ArrayList<Object> ();
	public ArrayList<Vector2> doors = new ArrayList<Vector2> ();
	public boolean start = false;
	public int x;
	public int y;
	
	public Room(int gridX, int gridY) {
		x = gridX;
		y = gridY;
	}
	
	public void generate() {
		objects.add(new Wall("top", new Vector2(200, 0)));
		
		objects.add(new Wall("top", new Vector2(1060, 0)));
		
		objects.add(new Wall("bottom", new Vector2(200, 880)));
		
		objects.add(new Wall("bottom", new Vector2(1060, 880)));
		
		objects.add(new Wall("left", new Vector2(0, 200)));
		
		objects.add(new Wall("left", new Vector2(0, 640)));

		objects.add(new Wall("right", new Vector2(1720, 200)));
		
		objects.add(new Wall("right", new Vector2(1720, 640)));
		
		objects.add(new Wall("corner", new Vector2()));
		
		objects.add(new Wall("corner", new Vector2(1720, 880)) {{
			rotation = 180;
		}});
		
		objects.add(new Wall("corner2", new Vector2(0, 880)) {{
			rotation = 180;
		}});
		
		objects.add(new Wall("corner2", new Vector2(1720, 0)));
		
		//Door d4 = new Door("right", new Vector2(1720, 440));
		//objects.add(d4);
		
		//Door d1 = new Door("top", new Vector2(860, 0));
		//objects.add(d1);
		
		//Door d2 = new Door("bottom", new Vector2(860, 880));
		//objects.add(d2);
		
		//Door d3 = new Door("left", new Vector2(0, 440));
		//objects.add(d3);
	}
	
	public void evaluateDoors(boolean top, boolean left, boolean bottom, boolean right) {
		if (top) {
			doors.add(new Vector2(0, -1));
		}
		
		if (left) {
			doors.add(new Vector2(-1, 0));
		}
		
		if (bottom) {
			doors.add(new Vector2(0, 1));
		}
		
		if (right) {
			doors.add(new Vector2(1, 0));
		}
	}
}
