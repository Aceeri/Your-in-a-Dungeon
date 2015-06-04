package main.character;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

import main.misc.Vector2;
import main.object.Animator;
import main.object.Frame;
import main.object.Object;
import main.object.Projectile;
import main.object.wall.Door;
import main.ui.TextLabel;

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
	public double hitcooldown = 0;
	private double doorBox = 5;
	
	public Animator animator;
	public BufferedImage ui;
	public TextLabel healthui;
	
	public Player(Vector2 position) {
		super(position);
		offsetSize = new Vector2(50, 50);
		
		collidable = true;
		anchored = false;
		stretch = true;
		imageX = "center";
		imageY = "bottom";
		
		setSize(Size.dimension());
		setLocation(0, 0);
		
		speed = 3;
		type = "player";
		path = "resources/image/missing.png";
		
		animator = new Animator(this);
		animator.defineAnimation("idle", new Frame[] {
				new Frame("resources/image/wall_left.png", 2),
				new Frame("resources/image/wall_top.png", 2),
		});
		
		healthui = new TextLabel(health + "/" + maxHealth);
		healthui.position = new Vector2(-(offsetSize.x/4), -5);
		
		//labels.add(healthui);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (type == "player") {
			if (ui == null) {
				ui = getImage("resources\\image\\HealthUI.png");
			}
			AffineTransform uiTransform = new AffineTransform();
			uiTransform.scale(1.5 * manager.ratio.x, 1 * manager.ratio.y);
			uiTransform.translate(0, 10 * manager.ratio.y);
		}
		
		double posX = position.x;
		double posY = position.y;
		double width = image.getWidth();
		double height = image.getHeight();
		if (!stretch) {
			if (imageX == "center") {
				posX = position.x + (Size.x - image.getWidth()*manager.ratio.x*scale)/2;
			} else if (imageX == "right") {
				posX = position.x + Size.x - image.getWidth()*manager.ratio.x*scale;
			}
			
			if (imageY == "center") {
				posY = position.y + (Size.y - image.getHeight()*manager.ratio.y*scale)/2;
			} else if (imageY == "bottom") {
				posY = position.y + Size.y - image.getHeight()*manager.ratio.y*scale;
			}
		} else {
			width = Size.x;
			height = Size.y;
		}
		
		g2.setColor(Color.RED);
		g2.fillRect((int) (posX + width/2) - 35, (int) (posY - 15), 70, 3);
		g2.setColor(Color.GREEN);
		g2.fillRect((int) (posX + width/2) - 35, (int) (posY - 15), (int) (health/maxHealth * 70), 3);
	}
	
	public void step(double delta) {
		super.step(delta);
		
		healthui.text = String.format("%.0f", health) + "/" + String.format("%.0f", maxHealth);
		
		if (type == "player" && !manager.entering) {
			for (int i = 0; i < manager.wallContainer.getComponentCount(); i++) {
				Object o = (Object) manager.wallContainer.getComponent(i);
				if (o instanceof Door && o.inside(position.sub(new Vector2(doorBox, doorBox).mult(manager.ratio)), Size.add(new Vector2(doorBox*2, doorBox*2).mult(manager.ratio)))) {
					Door door = (Door) o;
					Vector2 nextRoom = door.doorVector();
					manager.enterRoom(nextRoom, manager.currentRoom.x + (int) nextRoom.y, manager.currentRoom.y + (int) nextRoom.x);
					break;
				}
			}
		}
		
		if (!velocity.equals(new Vector2()) && animator.animationPlaying != "walk") {
			animator.playAnimation("walk", false);
		}
		
		animator.step(delta);
		
		hitcooldown = hitcooldown > 0 ? hitcooldown - delta*1000*manager.fixedFps/60 : 0;
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
	
	public void takeDamage(double damage) {
		health -= damage;
	}
	
	//override these with new abilities
	public void ability1() { }
	
	public void ability2() { }
	
	public String toString() {
		return getClass().toString();
	}
}