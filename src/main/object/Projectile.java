package main.object;

import main.misc.Vector2;
import main.character.*;

import java.awt.Color;
import java.awt.Container;

@SuppressWarnings("serial")
public class Projectile extends Object {
	
	private double damage;
	private double expiration;
	private Object parent;
	
	public boolean bounce = true;
	public Color color = Color.ORANGE;
	
	public Projectile(Object parent, String path, Vector2 direction, double damage, double range, double speed) {
		super(parent.offsetPosition.add(parent.offsetSize.scalar(.5)).add(direction.mult(parent.offsetSize)));
		
		this.offsetSize = new Vector2(5, 5);
		this.parent = parent;
		this.velocity = direction;
		this.damage = damage;
		this.expiration = range;
		this.speed = speed;
		this.path = path;
		
		anchored = false;
		collidable = false;
		
		type = "projectile";
	}
	
	public boolean expired() {
		// return true if projectile has lasted longer than range
		return expiration <= 0;
	}
	
	// go through all objects to see if colliding to anything that isn't the parent or a friendly
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
							
							// damage if not friendly and not inanimate
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
}
