
import java.awt.MouseInfo;

public class UserInterface extends Object {
	

	public UserInterface(Manager manager, Vector2 position) {
		super(manager, position);
	}
	
	public boolean mouseInside() {
		return inside(new Vector2(MouseInfo.getPointerInfo().getLocation()));
	}
}
