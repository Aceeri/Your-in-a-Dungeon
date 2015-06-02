package main.dungeon;

import java.util.ArrayList;





import main.character.Player;
import main.misc.Vector2;
import main.object.Background;
import main.object.wall.Door;
import main.object.wall.Wall;

@SuppressWarnings("serial")
public class Room {
	
	public ArrayList<Player> characters = new ArrayList<Player> ();
	public ArrayList<Object> objects = new ArrayList<Object> ();
	public ArrayList<Door> doors = new ArrayList<Door> (); 
	public int x;
	public int y;
	public String type = "regular";
	
	public Room(int gridX, int gridY) {
		x = gridX;
		y = gridY;
	}
	
	public void generate() {
		objects.add(new Background(new Vector2(1920, 1080)));
		
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
		
		for (int i = 0; i < doors.size(); i++) {
			objects.add(doors.get(i));
		}
	}
	
	public void evaluateDoors(boolean top, boolean left, boolean bottom, boolean right) {
		if (top) {
			doors.add(new Door("top"));
		} else {
			objects.add(new Wall("middle", new Vector2(860, 0)));
		}
		
		if (bottom) {
			doors.add(new Door("bottom"));
		} else {
			objects.add(new Wall("middle", new Vector2(860, 880)) {{
				rotation = 180;
			}});
		}
		
		if (left) {
			doors.add(new Door("left"));
		} else {
			objects.add(new Wall("middle2", new Vector2(0, 440)));
		}
		
		if (right) {
			doors.add(new Door("right"));
		} else {
			objects.add(new Wall("middle2", new Vector2(1720, 440)) {{
				rotation = 180;
			}});
		}
	}
}
