import java.awt.Graphics;
import java.awt.Color;

public class testback extends Object {
	
	public testback(Manager manager, Vector2 position) {
		super(manager, position);
		this.Size = new Vector2(30, 30);
		
		this.collidable = true;
		this.type = "wall";
	}
	
	public testback(Manager manager, Vector2 position, Vector2 size) {
		super(manager, position);
		this.Size = new Vector2(size.x, size.y);
		
		this.collidable = true;
		this.type = "wall";
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect((int) (position.x + offset.x), (int) (position.y + offset.y), (int) Size.x, (int) this.Size.y);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect((int) (position.x + 4 + offset.x), (int) (position.y + 4 + offset.y), (int) Size.x - 8, (int) this.Size.y - 8);
		g.setColor(Color.WHITE);
		g.fillRect((int) (position.x + 8 + offset.x), (int) (position.y + 8 + offset.y), (int) Size.x - 16, (int) this.Size.y - 16);
	}
}
