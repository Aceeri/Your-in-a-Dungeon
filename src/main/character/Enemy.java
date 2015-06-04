package main.character;

import main.misc.Vector2;

import java.awt.Color;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class Enemy extends Player {
	private Node[] route;
	
	public double sight = 400;
	public int score = 5;
	public Player aggro;
	
	public Enemy(Vector2 position) {
		super(position);
		
		type = "enemy";
		name = "xX1ll3g4l4l13n420swagXx";
	}
	
	public void step(double delta) {
		super.step(delta);
		
		Player nearestPlayer = getNearestPlayer();
		
		for (int i = 0; i < manager.playerContainer.getComponentCount(); i++) {
			Player player = (Player) manager.playerContainer.getComponent(i);
			if (player instanceof Enemy) {
				Enemy enemy = (Enemy) player;
				if (enemy.aggro != null && enemy.position.distance(position) < sight) {
					aggro = enemy.aggro;
				}
			}
		}
		
		if (nearestPlayer != null && offsetPosition.distance(nearestPlayer.offsetPosition) - offsetSize.distance(nearestPlayer.offsetSize) < range) {
			velocity = new Vector2();
			if (cooldown <= 0) {
				cooldown += attackspeed;
				new Thread() {
					public void run() {
						animator.playAnimation("attack", false);
						attack(nearestPlayer);
					}
				}.start();
			}
			
			if (ability1 <= 0) {
				ability1 += ability1speed;
				new Thread() {
					public void run() {
						ability1();
					}
				}.start();
			}
			
			if (ability2 <= 0) {
				ability2 += ability2speed;
				new Thread() {
					public void run() {
						ability2();
					}
				}.start();
			}
		} else if (nearestPlayer != null && (offsetPosition.distance(nearestPlayer.offsetPosition) - offsetSize.distance(nearestPlayer.offsetSize) < sight || aggro != null)) {
			aggro = nearestPlayer;
			nearestPlayer.update();
			update();
			if (route == null || (route.length > 0 && route[route.length - 1].position.distance(nearestPlayer.position) > 30)) {
				route = manager.pathfinder.route(position.add(Size.scalar(.5)), nearestPlayer.position.add(Size.scalar(.5)), 30);
			}
			
			if (route.length > 0) {
				Node closest = null;
				double distance = -1;
				for (int i = 0; i < route.length; i++) {
					if (route[i].position.distance(position) > Math.max(Size.x, Size.y) && (route[i].position.distance(position) < distance || distance == -1 || (route[i] == route[route.length - 1] && route[i].position.distance(position) - 50 < distance))) {
						closest = route[i];
						distance = closest.position.distance(position);
					}
				}
				
				if (closest != null) {
					velocity = closest.position.sub(position.add(Size.scalar(.5))).normalize();
				}
			}
		} else {
			velocity = new Vector2();
		}
	}
	
	public void attack(Player player) { }
	
	public Player getNearestPlayer() {
		Player current = null;
		double distance = -1;
		
		for (int i = 0; i < manager.playerContainer.getComponentCount(); i++) {
			Player plr = (Player) manager.playerContainer.getComponent(i);
			double toPlayer = position.distance(plr.position);
			if (plr.type == "player" && (position.distance(plr.position) < distance || distance == -1)) {
				distance = toPlayer;
				current = plr;
			}
		}
		
		return current;
	}
	
	public double getDistanceToNearestPlayer() {
		double distance = -1;
		for (int i = 0; i < manager.playerContainer.getComponentCount(); i++) {
			Player plr = (Player) manager.playerContainer.getComponent(i);
			double toPlayer = position.distance(plr.position);
			if (plr.type == "player" && (toPlayer < distance || distance == -1)) {
				distance = toPlayer;
			}
		}
		
		return distance;
	}
	
	public void takeDamage(double damage) {
		super.takeDamage(damage);
		Player player = getNearestPlayer();
		aggro = player;
	}
	
	public void drawPath(BufferedImage canvas) {
		if (route != null && route.length > 0) {
			for (int i = 0; i + 1 < route.length; i++) {
				route[i].position.drawVector(canvas, route[i + 1].position);
			}
		}
	}
}
