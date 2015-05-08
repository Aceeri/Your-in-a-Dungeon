import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Player extends Object {
	
	public int speed = 3; //keep between 1-5
	public double health = 10;
	public String name;
	public String type;
	
	public Vector2 collision = new Vector2();
	
	public Manager manager;
	
	public Color color = Color.ORANGE;
	
	public Player(Manager manager, Vector2 position) {
		super(manager, position);
		
		this.type = "player";
		
		this.manager = manager;
		this.Size = new Vector2(15, 15);
		this.collidable = true;
		
		this.setSize(this.Size.dimension());
		this.setLocation(0, 0);
	}
	
	public void step() {
		this.updatePosition();
		//System.out.println("Player velocity: " + this.velocity + " " + this.collision);
		this.position = this.position.sub(this.velocity.sub(this.collision));
		this.collision = new Vector2();
	}
	
	public void paintComponent(Graphics g) {
		this.paintLocation();
		
		g.setColor(Color.GRAY);
		g.fillRect((int) (this.position.x), (int) (this.position.y), 19, 19);
		
		g.setColor(color);
		g.fillRect((int) (this.position.x + 2), (int) (this.position.y + 2), 15, 15);
	}
	
	public Vector2 getNextPosition() {
		return new Vector2(this.position.x - this.velocity.x, this.position.y - this.velocity.y);
	}
}