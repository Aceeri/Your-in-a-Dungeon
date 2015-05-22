package main.character;

import java.awt.Color;
import java.awt.Graphics;

import main.misc.Vector2;
import main.object.Projectile;

@SuppressWarnings("serial")
public class Player extends main.object.Object {
	
	public double health = 10;
	public double maxHealth = 10;
	public double damage = 3; //damage normal attack does
	public double range = 500; //range in pixels
	public double attackspeed = 1000; //how long before next attack is ready (milliseconds)
	public double projectilespeed = 10; //how fast the projectiles move
	public double ability1speed = 3000; //time before abilities are ready (milliseconds);
	public double ability2speed = 3000;
	
	public String name = "hooman";
	
	public double cooldown = 0;
	public double ability1 = 0;
	public double ability2 = 0;
	
	public Player(Vector2 position) {
		super(position);
		
		Size = new Vector2(60, 60);
		collidable = true;
		anchored = false;
		
		setSize(this.Size.dimension());
		setLocation(0, 0);
		
		speed = 3;
		type = "player";
		path = "resources/image/player.png";
		
		setImage();
		System.out.println(toString());
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics c = manager.canvas.getGraphics();
		c.setColor(Color.GRAY);
		c.fillRect((int) position.x, (int) position.y - 10, (int) Size.x, 5);
		c.setColor(Color.GREEN);
		c.fillRect((int) position.x, (int) position.y - 10, (int) (Size.x * health/maxHealth), 5);
	}
	
	public void step(double delta) {
		super.step(delta);
		
		cooldown = cooldown > 0 ? cooldown - delta*1000*manager.fixedFps/60 : 0;
		ability1 = ability1 > 0 ? ability1 - delta*1000*manager.fixedFps/60 : 0;
		ability2 = ability2 > 0 ? ability2 - delta*1000*manager.fixedFps/60 : 0;
	}
	
	public void attack(Vector2 direction) {
		if (cooldown <= 0) {
			Projectile p = new Projectile(this, direction, damage, range, projectilespeed);
			manager.projectileContainer.add(p);
			cooldown += attackspeed;
		}
	}
	
	//override these with new abilities
	public void ability1() { }
	
	public void ability2() { }
	
	public String toString() {
		return getClass() + "";
	}
}