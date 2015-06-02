package main.object;

import main.misc.Vector2;
import main.character.*;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;

@SuppressWarnings("serial")
public class Projectile extends Object {
	
	private double damage;
	private double expiration;
	private Player parent;
	
	public boolean bounce = true;
	public Color color = Color.ORANGE;
	
	public Projectile(Player parent, Vector2 direction, double damage, double range, double speed) {
		super(parent.offsetPosition.add(parent.offsetSize.scalar(.5)).add(direction.mult(parent.offsetSize)));
		
		this.offsetSize = new Vector2(5, 5);
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
	
	public Vector2 checkCollision(Object[] ignoreList, double delta) {
		for (int i = 0; i < manager.getComponentCount(); i++) {
			if (manager.getComponent(i) instanceof Container && manager.getComponent(i) != manager.projectileContainer) {
				Container container = (Container) manager.getComponent(i);
				for (int j = 0; j < container.getComponentCount(); j++) {
					if (container.getComponent(j) instanceof Object) {
						Object object = (Object) container.getComponent(j);
						if (this != object && object.collidable) {
							boolean ignore = false;
							for (int k = 0; k < ignoreList.length; k++) {
								if (ignoreList[k] == object) {
									ignore = true;
									break;
								}
							}
							
							if (object != parent && !object.type.equals(parent.type) && !ignore && object.inside(position.add(velocity.scalar(speed*delta*manager.fixedFps)), Size)) {
								if (object instanceof Player) {
									Player plr = (Player) object;
									plr.takeDamage(damage);
								}
								
								expiration = 0;
							}
						}
					}
				}
			}
		}
		
		return new Vector2();
	}
	
	public void step(double delta) {
		paintLocation();
		expiration -= speed*delta*manager.fixedFps;
		
		update();
		checkCollision(new Object[] { parent }, delta);
		
		offsetPosition = offsetPosition.add(velocity.scalar(speed*delta*manager.fixedFps));
		update();
	}
	
	public void paintComponent(Graphics g) {
		if (g2 == null) {
			g2 = (Graphics2D) manager.canvas.getGraphics();
		}
		
		g2.setColor(color);
		g2.fillRect((int) position.x, (int) position.y, (int) Size.x, (int) Size.y);
	}
}
