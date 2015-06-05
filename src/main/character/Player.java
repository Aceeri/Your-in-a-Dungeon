package main.character;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import main.misc.Music;
import main.misc.Vector2;
import main.object.Animator;
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
	
	public String name = ""; // name displayed over character
	public String projectilePath = ""; // default projectile image
	
	public double cooldown = 0; // attack debounces
	public double ability1 = 0;
	public double ability2 = 0;
	public double hitcooldown = 0;
	private double doorBox = 5; // distance to doors before moving rooms
	
	public Animator animator;
	public BufferedImage ui;
	public TextLabel healthui;
	private Music attackSound = new Music("resources\\sound\\Attack.wav");
	
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
		
		attackSound.setVolume(.25);
		
		animator = new Animator(this);
		
		// update health ui
		healthui = new TextLabel(health + "/" + maxHealth);
		healthui.position = new Vector2(-(offsetSize.x/4), -5);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// get top left of image
		double posX = position.x;
		double posY = position.y;
		double width = image.getWidth()*scale*manager.ratio.x;
		double height = image.getHeight()*scale*manager.ratio.y;
		if (!stretch) {
			if (imageX == "center") {
				posX = position.x + (Size.x - width)/2;
			} else if (imageX == "right") {
				posX = position.x + Size.x - width;
			}
			
			if (imageY == "center") {
				posY = position.y + (Size.y - height)/2;
			} else if (imageY == "bottom") {
				posY = position.y + Size.y - height;
			}
		} else {
			width = Size.x;
			height = Size.y;
		}
		
		double sizeX = 70*manager.ratio.x;
		double sizeY = 6*manager.ratio.y;
		
		// display health bar for character
		g2.setColor(Color.GRAY);
		g2.fillRect((int) (posX), (int) (posY - 10*manager.ratio.y), (int) (sizeX), (int) (sizeY));
		g2.setColor(Color.GREEN);
		g2.fillRect((int) (posX), (int) (posY - 10*manager.ratio.y), (int) (health/maxHealth * sizeX), (int) (sizeY));
		
		g2.setFont(manager.font.deriveFont((float) (24f*Math.min(manager.ratio.x, manager.ratio.y))));
		g2.setColor(Color.WHITE);
		g2.drawString(name, (int) (posX), (int) (posY - 18f * manager.ratio.y));
		
		if (manager.keyPress[9]) {
			g2.setColor(Color.GREEN);
			g2.drawString("(" + String.format("%.0f", health) + "/" + String.format("%.0f", maxHealth) + ")", (int) (posX + g2.getFontMetrics(g2.getFont()).stringWidth(name) + 3), (int) (posY - 18f * manager.ratio.y));
		}
	}
	
	public void step(double delta) {
		super.step(delta);
		
		if (health < maxHealth) {
			health += delta/2;
			if (health > maxHealth) {
				health = maxHealth;
			}
		}
		
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
		
		// play animation step if playing
		animator.step(delta);
		
		// remove debounces on attacks
		hitcooldown = hitcooldown > 0 ? hitcooldown - delta*1000*manager.fixedFps/60 : 0;
		cooldown = cooldown > 0 ? cooldown - delta*1000*manager.fixedFps/60 : 0;
		ability1 = ability1 > 0 ? ability1 - delta*1000*manager.fixedFps/60 : 0;
		ability2 = ability2 > 0 ? ability2 - delta*1000*manager.fixedFps/60 : 0;
	}
	
	public void attack(Vector2 direction) {
		if (cooldown <= 0) {
			animator.playAnimation("attack", false);
			attackSound.play();
			
			Projectile p = new Projectile(this, projectilePath, direction, damage, range, projectilespeed);
			
			// make player face direction of projectile
			if (direction.x > 0) {
				this.direction = -1;
			} else {
				this.direction = 1;
			}
			
			// add to panel
			manager.projectileContainer.add(p);
			
			// set cooldown debounce
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