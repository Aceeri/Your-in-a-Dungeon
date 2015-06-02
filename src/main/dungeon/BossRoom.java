package main.dungeon;

import main.character.Player;
import main.character.enemies.BossWraith;
import main.misc.Vector2;
import main.object.Projectile;

public class BossRoom extends Room {

	public BossRoom(int gridX, int gridY) {
		super(gridX, gridY);
		
		BossWraith boss = new BossWraith(new Vector2(880, 440));
		objects.add(boss);
		
		type = "boss";
	}
}
