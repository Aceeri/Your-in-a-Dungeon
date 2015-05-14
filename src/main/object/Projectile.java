package main.object;

import main.misc.Vector2;
import main.character.*;

import java.awt.Color;
import java.awt.Graphics;

public class Projectile extends Object {
	
	private double damage;
	private double expiration;
	private Player parent;
	
	public boolean bounce = true;
	public Color color = Color.ORANGE;
	
	public Projectile(Player parent, Vector2 direction, double damage, double range, double speed) {
		super(parent.position.add(parent.Size.scalar(.5)).sub(new Vector2(2.5, 2.5)));
		
		this.Size = new Vector2(5, 5);
		this.parent = parent;
		this.velocity = direction;
		this.damage = damage;
		this.expiration = range;
		this.speed = speed;
		
		anchored = false;
		collidable = false;
		
		type = "projectile";
	}
	
	public boolean expired() {
		return expiration <= 0;
	}
	
	public void step() {
		updatePosition();
		paintLocation();
		expiration -= speed;
		
		/*if (bounce) {
			Vector2 collision = checkCollision(new Object[] { parent });
			
			if (Math.abs(collision.x) > 0) {
				velocity.x = -velocity.x;
			}
			
			if (Math.abs(collision.y) > 0) {
				velocity.y = -velocity.y;
			}
			
			offsetPosition = offsetPosition.add(velocity.scalar(this.speed));
		}*/
	}
	
	public void paintComponent(Graphics g) {
		Graphics c = manager.canvas.getGraphics();
		c.setColor(color);
		c.fillRect((int) position.x, (int) position.y, 5, 5);
	}
}
