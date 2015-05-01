import javax.swing.JPanel;
import javax.swing.JComponent;

import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

public class Background extends JPanel {
	
	public Vector2 screen;
	public boolean test = false;
	
	public ArrayList<Object> components = new ArrayList<Object> ();
	
	public Vector2 offset = new Vector2(0, 0);
	
	public Background(Vector2 screen) {
		this.screen = screen;
		this.setBackground(Color.BLACK);
		this.setSize(screen.dimension());
		this.setLocation(0, 0);
		
		testback a = new testback(new Vector2(0, 170), screen, new Vector2(screen.x/2, 30));
		a.collidable = true;
		this.addObject(a);
		
		testback b = new testback(new Vector2(0, 200), screen, new Vector2(30, screen.y/2));
		b.collidable = true;
		this.addObject(b);
		
		testback c = new testback(new Vector2(0, screen.y/2 + 200), screen, new Vector2(screen.x/2, 30));
		c.collidable = true;
		this.addObject(c);
		
		testback d = new testback(new Vector2(screen.x/2 - 30, 200), screen, new Vector2(30, screen.y/2));
		d.collidable = true;
		this.addObject(d);
	}
	
	public void addObject(Object o) {
		this.add(o);
		this.components.add(o);
	}
	
	public void paintComponent(Graphics g) {
		for (Object o : this.components) {
			o.offset = this.offset;
		}
	}
}
