import javax.swing.JComponent;

public class Object extends JComponent {
	
	public Vector2 screen;
	public Vector2 position;
	public Vector2 offset;
	public Vector2 velocity;
	public Vector2 Size;
	
	public boolean collidable = false;
	public boolean anchored = true;
	public boolean backgroundObject = false;
	
	public Object(Vector2 position, Vector2 screen) {
		this.position = position;
		this.offset = new Vector2(0, 0);
		this.velocity = new Vector2(0, 0);
		this.screen = screen;
		
		this.paintLocation();
	}
	
	public void paintLocation() {
		this.setLocation(0, 0);
		this.setSize(screen.dimension());
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
		
		System.out.println(direction);
		
		return direction;
	}
	
	public Vector2 getNextPosition() {
		return new Vector2(this.position.x + this.velocity.x, this.position.y + this.velocity.y);
	}
	
	public Vector2 getNextPosition(Vector2 velocity) {
		return new Vector2(this.position.x + velocity.x, this.position.y + velocity.y);
	}
}