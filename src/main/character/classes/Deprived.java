package main.character.classes;

import main.character.Player;
import main.misc.Vector2;

public class Deprived extends Player {

	public Deprived(Vector2 position) {
		super(position);
		
		health = 10;
		maxHealth = 10;
		speed = 4;
		projectilespeed = 5;
		attackspeed = 1000;
		damage = 3;
	}
}
