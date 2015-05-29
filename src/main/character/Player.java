package main.character;

import main.misc.Vector2;
import main.object.Object;
import main.object.Projectile;
import main.object.wall.Door;

@SuppressWarnings("serial")
public class Player extends main.object.Object {
	
	public double health = 10;
	public double maxHealth = 10;
	public double damage = 3; // damage normal attack does
	public double range = 500; // range in pixels
	public double attackspeed = 1000; // how long before next attack is ready (milliseconds)
	public double projectilespeed = 10; // how fast the projectiles move
	public double ability1speed = 3000; // time before abilities are ready (milliseconds);
	public double ability2speed = 3000;
	
	public String name = "hooman";
	
	public double cooldown = 0;
	public double ability1 = 0;
	public double ability2 = 0;
	private double doorBox = 5;
	
	public Player(Vector2 position) {
		super(position);
		offsetSize = new Vector2(50, 50);
		
		collidable = true;
		anchored = false;
		imageX = "center";
		imageY = "bottom";
		
		setSize(Size.dimension());
		setLocation(0, 0);
		
		speed = 3;
		type = "player";
		
		path = "resources/image/missing.png";
	}
	
	public void step(double delta) {
		super.step(delta);
		
		for (int i = 0; i < manager.wallContainer.getComponentCount(); i++) {
			Object o = (Object) manager.wallContainer.getComponent(i);
			if (o instanceof Door && o.inside(position.sub(new Vector2(doorBox, doorBox).mult(manager.ratio)), Size.add(new Vector2(doorBox*2, doorBox*2).mult(manager.ratio)))) {
				Door door = (Door) o;
				Vector2 nextRoom = door.doorVector();
				offsetPosition = new Vector2(900, 490).add(Size.scalar(.5)).sub(new Vector2(700, 280).mult(nextRoom));
				manager.enterRoom(manager.currentRoom.x + (int) nextRoom.y, manager.currentRoom.y + (int) nextRoom.x);
				break;
			}
		}
		
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