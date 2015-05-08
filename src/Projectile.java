import java.awt.Color;
import java.awt.Graphics;
import java.util.Date;

public class Projectile extends Object {
	
	public double damage;
	public double expiration;
	public Player parent;
	
	public double speed = 1;
	
	public boolean bounce = true;
	public Color color = Color.ORANGE;
	
	public Projectile(Player parent, Vector2 direction, double damage, double expiration) {
		super(parent.manager, parent.position.add(parent.Size.div(2)));
		
		this.Size = new Vector2(5, 5);
		this.parent = parent;
		this.velocity = direction;
		this.damage = damage;
		this.expiration = expiration;
	}
	
	public boolean expired() {
		return this.expiration <= 0;
	}
	
	public void paintComponent(Graphics g) {
		super.paintLocation();
		
		g.setColor(Color.orange);
		g.fillRect((int) this.position.x, (int) this.position.y, 5, 5);
	}
}
