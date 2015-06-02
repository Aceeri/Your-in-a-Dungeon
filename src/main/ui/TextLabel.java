package main.ui;

import java.awt.Color;

import main.misc.Vector2;

public class TextLabel {
	public Vector2 position = new Vector2(0, 0); // offset from top-left of parent object
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
}
