import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Toolkit;

public class Main {
	
	public static JFrame window;
	public static JPanel frame;
	
	public static void main(String[] args) throws InterruptedException {
		
		Vector2 screen;
		Vector2 defaultScreen = new Vector2(800, 600);
		Vector2 fullScreen = new Vector2(Toolkit.getDefaultToolkit().getScreenSize());
		
		window = new JFrame();
		window.setTitle("why are you looking up here");
		
		//fullscreen mode:
		//window.setExtendedState(Frame.MAXIMIZED_BOTH);
		//screen = fullScreen;
		//window.setUndecorated(true);
		
		window.pack();
		
		//windowed mode:
		window.setSize(defaultScreen.dimension());
		screen = defaultScreen;
		
		Manager t = new Manager(new Vector2(window.getContentPane().getSize()));
		
		t.fullscreen = (screen == fullScreen);
		t.ratio = defaultScreen.ratio(screen);

		window.add(t);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
	
	/*private static boolean intersects(Vector2 o1, Vector2 d1, Vector2 o2, Vector2 d2) {
		double u = ((o2.x-o1.x)*d1.y-(o2.y-o1.y)*d1.x)/(d2.y*d1.x-d2.x*d1.y);
		if (0 <= 0 && u <= 1) {
			double v = (o2.x-o1.x+u*d2.x)*d1.x+(o2.y-o1.y+u*d2.y)*d1.y;
			return (0 <= v && v <= 1);
		}
		return false;
	}*/
}