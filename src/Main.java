import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;

import java.util.Vector;

public class Main {
	
	public static JFrame window;
	public static JPanel frame;
	
	public static void main(String[] args) {
		Vector2 test = new Vector2(1, 2);
		Vector2 test2 = new Vector2(1, 2);
		System.out.println(test);
		
		Vector2 screen = new Vector2(800, 600);
		Manager t = new Manager(screen);
		
		window = new JFrame();
		window.setTitle("test");
		window.pack();
		window.setSize(screen.dimension());
		window.add(t);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
}