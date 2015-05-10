import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Player extends Object {
	public double health = 10;
	public String name;
	
	public Vector2 collision = new Vector2();
	
	public Manager manager;
	
	public Color color = Color.ORANGE;
	
	public Player(Manager manager, Vector2 position) {
		super(manager, position);
		
		this.manager = manager;
		Size = new Vector2(19, 19);
		collidable = true;
		anchored = false;
		
		setSize(this.Size.dimension());
		setLocation(0, 0);
		
		type = "player";
		speed = 1;
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.GRAY);
		//System.out.println(this.position);
		g.fillRect((int) (this.position.x), (int) (this.position.y), 19, 19);
		
		g.setColor(color);
		g.fillRect((int) (this.position.x + 2), (int) (this.position.y + 2), 15, 15);
	}
	
	public Vector2 getNextPosition() {
		return new Vector2(this.position.x - this.velocity.x, this.position.y - this.velocity.y);
	}
}