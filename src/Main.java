import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {
	
	public static JFrame window;
	public static JPanel frame;
	
	public static void main(String[] args) {
		Vector2 screen = new Vector2(800, 600);
		
		window = new JFrame();
		window.setTitle("test");
		window.pack();
		window.setSize(screen.dimension());
		
		//fix offset of window
		screen.y -= 7;
		screen.x -= 16;
		Manager t = new Manager(screen);
		
		window.add(t);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
}