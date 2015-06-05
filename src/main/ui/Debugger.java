package main.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import main.misc.Vector2;

public class Debugger extends main.object.Object {
	private ArrayList<String[]> displayString = new ArrayList<String[]> ();
	public boolean display = false;
	
	public Debugger() {
		super(new Vector2());
	}
	
	public void addString(String[] info) {
		// add key to debugger
		displayString.add(info);
	}
	
	public void updateString(String[] info) {
		for (int i = 0; i < displayString.size(); i++) {
			if (displayString.get(i)[0].equals(info[0])) {
				displayString.set(i, info);
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		// display in list form with information
		
		if (display) {
			int currentY = 15;
			g.setColor(Color.GREEN);
			for (int i = 0; i < displayString.size(); i++) {
				g.drawString(displayString.get(i)[0], 5, currentY);
				
				if (displayString.get(i).length <= 2) {
					if (displayString.get(i).length > 1) {
						g.drawString(displayString.get(i)[1], 100, currentY);
					}
					currentY += 15;
				} else {
					for (int j = 1; j < displayString.get(i).length; j++) {
						g.drawString(displayString.get(i)[j], 100, currentY);
						currentY += 15;
					}
				}
			}
		}
	}
}
