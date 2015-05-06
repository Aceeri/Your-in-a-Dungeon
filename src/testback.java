import java.awt.Graphics;
import java.awt.Color;

public class testback extends Object {
	
	public testback(Vector2 position, Vector2 screen) {
		super(position, screen);
		this.Size = new Vector2(30, 30);
		
		this.collidable = true;
	}
	
	public testback(Vector2 position, Vector2 screen, Vector2 size) {
		super(position, screen);
		this.Size = new Vector2(size.x, size.y);
		
		this.collidable = true;
	}
	
	public void paintComponent(Graphics g) {
		this.paintLocation();
		
		
		g.setColor(Color.GRAY);
		g.drawString("nerd", 50, 50);
		g.fillRect((int) (this.position.x + this.offset.x), (int) (this.position.y + this.offset.y), (int) this.Size.x, (int) this.Size.y);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect((int) (this.position.x + 4 + this.offset.x), (int) (this.position.y + 4 + this.offset.y), (int) this.Size.x - 8, (int) this.Size.y - 8);
		g.setColor(Color.WHITE);
		g.fillRect((int) (this.position.x + 8 + this.offset.x), (int) (this.position.y + 8 + this.offset.y), (int) this.Size.x - 16, (int) this.Size.y - 16);
	}
}
