import java.awt.Container;
import java.awt.Toolkit;

import javax.swing.JComponent;

public class Object extends JComponent {
	
	public Vector2 scalePosition = new Vector2(0, 0);
	public Vector2 offsetPosition = new Vector2(0, 0);
	public Vector2 position = new Vector2(0, 0);
	public Vector2 offset;
	public Vector2 velocity;
	public Vector2 Size;
	
	public double speed = 0;
	public String type = "object";
	
	public Manager manager;
	
	public boolean collidable = false;
	public boolean anchored = false;
	public boolean backgroundObject = false;
	
	public Object(Manager manager, Vector2 position) {
		this.offsetPosition = position;
		this.offset = new Vector2(0, 0);
		this.velocity = new Vector2(0, 0);
		this.manager = manager;
		
		this.paintLocation();
	}
	
	public void paintLocation() {
		this.setLocation(0, 0);
		this.setSize(manager.screen.dimension());
	}
	
	//rectangle inside
	public boolean inside(Vector2 p2, Vector2 s2) {
		if ((this.position.x < p2.x + s2.x) 
				&& (this.position.x + this.Size.x > p2.x)
				&& (this.position.y < p2.y + s2.y)
				&& (this.position.y + this.Size.y > p2.y)) {
			return true;
		}
		
		return false;
	}
	
	//object inside
	public boolean inside(Object o) {
		return inside(o.position, o.Size);
	}
	
	//point inside
	public boolean inside(Vector2 point) {
		if (point.x > this.position.x
				&& point.x < this.position.x + this.Size.x
				&& point.y > this.position.y
				&& point.y < this.position.y + this.Size.y) {
			return true;
		}
		return false;
	}
	
	public Vector2 side(Vector2 o2, Vector2 d2) {
		double dx = (position.x + Size.x/2) - (o2.x + d2.x/2);
		double dy = (position.y + Size.y/2) - (o2.y + d2.y/2);
		double width = (Size.x + d2.x)/2;
		double height = (Size.y + d2.y)/2;
		double crossWidth = width*dy;
		double crossHeight = height*dx;
		
		Vector2 direction = new Vector2();
		if (Math.abs(dx) <= width && Math.abs(dy) <= height) {
	        if (crossWidth > crossHeight) {
	        	if (crossWidth > -crossHeight) {
	        		direction.y = 1;
	        		//System.out.println("bottom");
	        	} else {
	        		direction.x = -1;
	        		//System.out.println("left");
	        	}
	        } else {
	        	if (crossWidth > -crossHeight) {
	        		direction.x = 1;
	        		//System.out.println("right");
	        	} else {
	        		direction.y = -1;
	        		//System.out.println("top");
	        	}
	        }
	    }
		
		return direction;
	}
	
	//default per-frame function
	public void step() {
		this.paintLocation();
			
		if (!anchored) {
			for (int i = 0; i < this.speed; i++) {
				Vector2 collision = this.collidable ? this.checkCollision() : new Vector2(0, 0); //if collidable then get collided, else ignore
				this.offsetPosition = this.offsetPosition.add(this.velocity.add(collision));
			}
		}
		
		updatePosition();
	}
	
	public void updatePosition() {
		this.position = this.scalePosition.scalar(manager.screen).add(this.offsetPosition).round();
	}
	
	public Vector2 getNextPosition() {
		return position.add(velocity.mult(speed));
	}
	
	public Vector2 getNextPosition(Vector2 velocity) {
		return position.add(velocity);
	}
	
	public Vector2 checkCollision(Object[] ignoreList) {
		Vector2 collision = new Vector2();
		
		for (int i = 0; i < manager.getComponentCount(); i++) {
			if (manager.getComponent(i) instanceof Container) {
				Container container = (Container) manager.getComponent(i);
				for (int j = 0; j < container.getComponentCount(); j++) {
					if (container.getComponent(j) instanceof Object) {
						Object object = (Object) container.getComponent(j);
						boolean ignore = false;
						for (int k = 0; k < ignoreList.length; k++) {
							if (ignoreList[k] == object) {
								ignore = true;
								break;
							}
						}
						if (!ignore && this != object && object.collidable && !this.anchored) {
							Vector2 objectCollision = object.side(this.position.add(this.velocity.normalize()), this.Size);
							collision = collision.sub(objectCollision);
							if (Math.abs(objectCollision.x) > 0) {
								collision.x = -objectCollision.x;
								//System.out.println(objectCollision);
							}
							
							if (Math.abs(objectCollision.y) > 0) {
								collision.y = -objectCollision.y;
								//System.out.println(objectCollision);
							}
						}
					}
				}
			}
		}
		//System.out.println(collision);
		return collision;
	}
	
	public Vector2 checkCollision() {
		return checkCollision(new Object[] { });
	}
	
	/*private boolean intersects(Vector2 o1, Vector2 d1, Vector2 o2, Vector2 d2) {
		double u = ((o2.x-o1.x)*d1.y-(o2.y-o1.y)*d1.x)/(d2.y*d1.x-d2.x*d1.y);
		if (0 <= u && u <= 1) {
			double v = (o2.x-o1.x+u*d2.x)*d1.x+(o2.y-o1.y+u*d2.y)*d1.y;
			return (0 <= v && v <= 1);
		}
		return false;
	}
	
	public boolean rectIntersect(Vector2 o2, Vector2 d2) {
		if (intersects(position, new Vector2(Size.x, 0), o2, d2)
				|| intersects(position, new Vector2(0, Size.y), o2, d2)
				|| intersects(position.add(Size), new Vector2(-Size.x, 0), o2, d2)
				|| intersects(position.add(Size), new Vector2(0, -Size.y), o2, d2)) {
			return true;
		}
		return false;
	}*/
}