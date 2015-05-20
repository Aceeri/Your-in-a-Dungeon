package main.character;

import java.awt.Color;

import main.misc.Vector2;

public class Node { 
	public Vector2 position;
	public int x;
	public int y;
	public double f;
	public Color color = Color.RED;
	
	public Node parent;
	
	public Node(Vector2 p, Vector2 gP, double h) {
		position = p;
		x = (int) gP.x;
		y = (int) gP.y;
		f = h;
	}
}