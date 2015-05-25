package main.object.wall;

import main.misc.Vector2;
import main.object.Object;

import java.awt.Graphics;
import java.awt.Color;

@SuppressWarnings("serial")
public class testback extends Object {
	
	public testback(Vector2 position) {
		super(position);
		Size = new Vector2(30, 30);
		
		collidable = true;
		anchored = true;
		type = "wall";
	}
	
	public testback(Vector2 position, Vector2 size) {
		super(position);
		Size = new Vector2(size.x, size.y);
		
		collidable = true;
		anchored = true;
		type = "wall";
	}
	
	public void paintComponent(Graphics g) {
		
		Graphics c = manager.canvas.getGraphics();
		
		c.setColor(Color.GRAY);
		c.fillRect((int) (position.x), (int) (position.y), (int) Size.x, (int) this.Size.y);
		c.setColor(Color.LIGHT_GRAY);
		c.fillRect((int) (position.x + 4), (int) (position.y + 4), (int) Size.x - 8, (int) this.Size.y - 8);
		c.setColor(Color.WHITE);
		c.fillRect((int) (position.x + 8), (int) (position.y + 8), (int) Size.x - 16, (int) this.Size.y - 16);
	}
}
