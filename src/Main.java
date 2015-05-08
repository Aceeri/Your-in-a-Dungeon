import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.util.Vector;

public class Main {
	
	public static JFrame window;
	public static JPanel frame;
	
	public static void main(String[] args) throws InterruptedException {
		Vector2 defaultScreen = new Vector2(800, 600);
		Vector2 screen = new Vector2(Toolkit.getDefaultToolkit().getScreenSize());
		
		window = new JFrame();
		window.setTitle("why are you looking up here");
		window.setExtendedState(Frame.MAXIMIZED_BOTH);
		window.setUndecorated(true);
		window.pack();
		
		//fix offset of window
		screen.y -= 7;
		screen.x -= 16;
		Manager t = new Manager(screen);
		
		System.out.println(screen.scale(defaultScreen));
		
		window.add(t);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
}