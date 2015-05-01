import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Player extends Object {
	public int speed = 5; //keep between 1-5
	
	public ArrayList<Vector2> forces = new ArrayList<Vector2>();
	
	public Player(Vector2 position, Vector2 screen) {
		super(position, screen);
		this.Size = new Vector2(15, 15);
		this.offset = new Vector2(175, -75);
		
		this.collidable = true;
		
		this.setSize(this.Size.dimension());
		this.setLocation(0, 0);
	}
	
	public void paintComponent(Graphics g) {
		super.paintLocation();
		
		g.setColor(Color.GRAY);
		g.fillRect((int) (this.screen.x/2 - 32), (int) (this.screen.y/2 - 32), 19, 19);
		
		g.setColor(Color.ORANGE);
		g.fillRect((int) (this.screen.x/2 - 30), (int) (this.screen.y/2 - 30), 15, 15);
	}
	
	public Vector2 getPosition() {
		return new Vector2(this.position.x - this.offset.x, this.position.y - this.offset.y);
	}
	
	public Vector2 getNextPosition() {
		return new Vector2(this.position.x - this.offset.x - this.velocity.x, this.position.y - this.offset.y - this.velocity.y);
	}
}