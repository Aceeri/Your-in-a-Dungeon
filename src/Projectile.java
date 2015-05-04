import java.awt.Color;
import java.awt.Graphics;
import java.util.Date;

public class Projectile extends Object {
	
	public double damage;
	public double expiration;
	public Player parent;
	
	public double speed = 1;
	
	public Projectile(Player parent, Vector2 direction, double damage, double expiration) {
		super(parent.position.add(direction.mult(parent.Size.x)).add(parent.Size.div(2)), parent.screen);
		
		this.parent = parent;
		this.velocity = direction;
		this.damage = damage;
		this.expiration = new Date().getTime() + expiration;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public boolean expired() {
		return this.expiration < new Date().getTime();
	}
	
	public void paintComponent(Graphics g) {
		super.paintLocation();
		
		g.setColor(Color.orange);
		g.fillRect((int) this.position.x, (int) this.position.y, 5, 5);
	}
}
