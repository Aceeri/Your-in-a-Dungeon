package main.ui;

import main.misc.Vector2;
import main.Manager;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Graphics;
import java.util.ArrayList;

public class UserInterface extends main.object.Object {
	
	private ArrayList<String[]> displayString = new ArrayList<String[]> ();
	public boolean display = false;
	
	public UserInterface(Manager manager) {
		super(manager, new Vector2());
	}
	
	public void addString(String[] info) {
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
		//Graphics c = manager.canvas.getGraphics();
		
		int currentY = 15;
		g.setColor(Color.GREEN);
		for (int i = 0; i < displayString.size(); i++) {
			g.drawString(displayString.get(i)[0], 5, currentY);
			
			if (displayString.get(i).length <= 2) {
				g.drawString(displayString.get(i)[1], 100, currentY);
				currentY += 15;
			} else {
				//c.drawString("{ ", 90, currentY);
				//currentY += 15;
				for (int j = 1; j < displayString.get(i).length; j++) {
					g.drawString(displayString.get(i)[j], 100, currentY);
					currentY += 15;
				}
				//c.drawString("} ", 90, currentY);
				//currentY += 15;
			}
		}
	}
	
	public boolean mouseInside() {
		return inside(new Vector2(MouseInfo.getPointerInfo().getLocation()));
	}
}
