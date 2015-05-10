import java.awt.Color;
import java.awt.Graphics;
import java.util.Date;

public class Projectile extends Object {
	
	public double damage;
	public double expiration;
	public Player parent;
	
	public boolean bounce = true;
	public Color color = Color.ORANGE;
	
	public Projectile(Player parent, Vector2 direction, double damage, double expiration) {
		super(parent.manager, parent.position.add(parent.Size.div(2)));
		
		this.Size = new Vector2(5, 5);
		this.parent = parent;
		this.velocity = direction;
		this.damage = damage;
		this.expiration = expiration;
		
		anchored = false;
		collidable = false;
		
		type = "projectile";
	}
	
	public boolean expired() {
		return this.expiration <= 0;
	}
	
	public void step() {
		updatePosition();
		paintLocation();
		this.expiration -= 16;
		
		for (int i = 0; i < this.speed; i++) {
			if (bounce) {
				Vector2 collision = checkCollision(new Object[] { parent });
				
				if (Math.abs(collision.x) > 0) {
					velocity.x = -velocity.x;
				}
				
				if (Math.abs(collision.y) > 0) {
					velocity.y = -velocity.y;
				}
				
				offsetPosition = offsetPosition.add(velocity);
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.orange);
		g.fillRect((int) position.x, (int) position.y, 5, 5);
	}
}
