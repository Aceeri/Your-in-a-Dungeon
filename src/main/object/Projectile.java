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
	
	public void step(double delta) {
		updatePosition();
		paintLocation();
		expiration -= speed;
		
		Vector2 collision = checkCollision(new Object[] { parent }, delta);
		//BOUNCING IS SIN
		//HEATHENS SHALTH PERISH BEFORE EXPIRATION
		//FUCK YOU ALL
		//CD PROJEKKT RED BEST GAME STUDIO
		//why are you reading these comments
		
		if (Math.abs(collision.x) + Math.abs(collision.y) > 0) {
			expiration = 0;
		}
		
		offsetPosition = offsetPosition.add(velocity.scalar(speed*delta*manager.fixedFps));
	}
	
	public void paintComponent(Graphics g) {
		Graphics c = manager.canvas.getGraphics();
		c.setColor(color);
		c.fillRect((int) position.x, (int) position.y, 5, 5);
	}
}
