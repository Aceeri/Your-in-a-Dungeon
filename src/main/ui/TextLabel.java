package main.ui;

import java.awt.Color;
import java.awt.Graphics;

import main.misc.Vector2;

public class TextLabel extends UserInterface {
	public Vector2 position = new Vector2(); // offset from top-left of parent object
	public float size = 24f;
	public Color color = Color.WHITE;
	public String text = "";
	
	public TextLabel() { }
	
	public TextLabel(String str) {
		text = str;
	}
	
	public TextLabel(String str, Vector2 p) {
		text = str;
		position = p;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (manager != null && manager.font != null) {
			g2.setFont(manager.font.deriveFont((float) (size*Math.min(manager.ratio.x, manager.ratio.y))));
			g2.setColor(color);
			g2.drawString(text, (int) (position.x*manager.ratio.x), (int) (position.y*manager.ratio.y));
		}
	}
}
