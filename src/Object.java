import java.awt.Toolkit;

import javax.swing.JComponent;

public class Object extends JComponent {
	
	public Vector2 scalePosition;
	public Vector2 offsetPosition;
	public Vector2 position;
	public Vector2 offset;
	public Vector2 velocity;
	public Vector2 Size;
	public Vector2 windowSizeOffset;
	
	public Manager manager;
	
	public boolean collidable = false;
	public boolean anchored = true;
	public boolean backgroundObject = false;
	
	public Object(Manager manager, Vector2 position) {
		this.position = position;
		this.offset = new Vector2(0, 0);
		this.velocity = new Vector2(0, 0);
		this.manager = manager;
		
		this.paintLocation();
	}
	
	public void paintLocation() {
		this.setLocation(0, 0);
		this.setSize(manager.screen.dimension());
	}
	
	public boolean inside(Vector2 p2, Vector2 s2) {
		if ((this.position.x < p2.x + s2.x) 
				&& (this.position.x + this.Size.x > p2.x)
				&& (this.position.y < p2.y + s2.y)
				&& (this.position.y + this.Size.y > p2.y)) {
			return true;
		}
		
		return false;
	}
	
	public Vector2 collide(Vector2 p, Vector2 s, Vector2 v) {
		if (!collidable) {
			return new Vector2(0, 0);
		} else {
			Vector2 side = this.side(p, s);
			Vector2 push = new Vector2(v.x*Math.abs(side.x), v.y*Math.abs(side.y));
			return push.mult(v.dot(side));
		}
	}
	
	public Vector2 side(Vector2 position, Vector2 size) {
		Vector2 center = position.add(size.div(2));
		Vector2 direction = new Vector2();
		
		if (center.y > this.position.y + this.Size.y) {
			//bottom
			direction.y = 1;
		} else if (center.y < this.position.y) {
			//top
			direction.y = -1;
		}
		
		if (center.x < this.position.x) {
			//left
			direction.x = -1;
		} else if (center.x > this.position.x + this.Size.x ) {
			//right
			direction.x = 1;
		}
		
		return direction;
	}
	
	public void step() {
		this.updatePosition();
	}
	
	public void updatePosition() throws NullPointerException {
		this.position = this.scalePosition.multVect(manager.screen).add(this.offsetPosition);
		System.out.println(this.position);
	}
	
	public Vector2 getNextPosition() {
		return new Vector2(this.position.x + this.velocity.x, this.position.y + this.velocity.y);
	}
	
	public Vector2 getNextPosition(Vector2 velocity) {
		return new Vector2(this.position.x + velocity.x, this.position.y + velocity.y);
	}
}