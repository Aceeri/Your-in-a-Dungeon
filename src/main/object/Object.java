package main.object;

import main.misc.Vector2;
import main.Manager;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
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
	
	protected String path = "resources/image/missing.png";
	protected BufferedImage image;
	
	public Object(Manager manager, Vector2 position) {
		offsetPosition = position;
		offset = new Vector2(0, 0);
		velocity = new Vector2(0, 0);
		this.manager = manager;
		
		Size = new Vector2(0, 0);
		
		paintLocation();
	}
	
	public void paintComponent(Graphics g) {
		manager.canvas.getGraphics().drawImage(image, (int) position.x, (int) position.y, (int) Size.x, (int) Size.y, null);
	}
	
	public void setImage() {
		try {
			image = ImageIO.read(new File(path));
		} catch (java.io.IOException e) { }
	}
	
	public void paintLocation() {
		setLocation(0, 0);
		setSize(manager.screen.dimension());
	}
	
	//rectangle inside
	public boolean inside(Vector2 p2, Vector2 s2) {
		if ((position.x < p2.x + s2.x) 
				&& (position.x + Size.x > p2.x)
				&& (position.y < p2.y + s2.y)
				&& (position.y + Size.y > p2.y)) {
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
		if (point.x > position.x
				&& point.x < position.x + Size.x
				&& point.y > position.y
				&& point.y < position.y + Size.y) {
			return true;
		}
		return false;
	}
	
	public Vector2 side(Vector2 p2, Vector2 s2) {
		/*
		 *inaccurate when x and y values greatly differ
		*/
		
		/*Vector2 d = new Vector2((position.x + Size.x/2) - (o2.x + d2.x/2), (position.y + Size.y/2) - (o2.y + d2.y/2));
		Vector2 size = new Vector2((Size.x + d2.x)/2, (Size.y + d2.y)/2);
		Vector2 cross = new Vector2(size.x*d.y, size.y*d.x);
		//double dx = (position.x + Size.x/2) - (o2.x + d2.x/2);
		//double dy = (position.y + Size.y/2) - (o2.y + d2.y/2);
		//double width = (Size.x + d2.x)/2;
		//double height = (Size.y + d2.y)/2;
		//double crossWidth = width*dy;
		//double crossHeight = height*dx;
		
		double ratio = Size.y/Size.x;
		System.out.println("ratio: " + ratio*Size.x/20);
		
		Vector2 direction = new Vector2();
		if (Math.abs(d.x) <= size.x && Math.abs(d.y) <= size.y) {
	        if (cross.x > cross.y) {
	        	if (cross.x > -cross.y) { //top
	        		direction.y = 1;
	        	} else { //right
	        		direction.x = -1;
	        	}
	        } else {
	        	if (cross.x > -cross.y) { //left
	        		direction.x = 1;
	        	} else { //bottom
	        		direction.y = -1;
	        	}
	        }
	    }
		
		return direction;*/
		
		/*
		 * SAT collision (Separating Axis Theorem)
		 */
		Vector2 p1 = position;
		Vector2 s1 = Size;
		
		Vector2 c1 = p1.add(s1.scalar(1/2));
		Vector2 c2 = p2.add(s2.scalar(1/2));
		Vector2 cd = c1.sub(c2);
		
		Vector2 ratio = s2.div(s1);
		
		if (p1.x < p2.x + s2.x 
				&& p1.x + s1.x > p2.x 
				&& p1.y < p2.y + s2.y 
				&& p1.y + s1.y > p2.y) {
			if (Math.abs(cd.x * ratio.x) > Math.abs(cd.y * ratio.y)) {
				if (cd.x < 0) {
					return new Vector2(-1, 0);
				} else {
					return new Vector2(1, 0);
				}
			} else {
				if (cd.y < 0) {
					return new Vector2(0, -1);
				} else {
					return new Vector2(0, 1);
				}
			}
		}
		return new Vector2(0, 0);
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
		this.position = this.scalePosition.mult(manager.screen).add(this.offsetPosition).round();
	}
	
	public Vector2 getNextPosition() {
		return position.add(velocity.scalar(speed));
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
							Vector2 objectCollision = object.side(this.position.add(this.velocity), this.Size);
							
							if (Math.abs(objectCollision.x) > 0) {
								collision.x = -objectCollision.x;
							}
							
							if (Math.abs(objectCollision.y) > 0) {
								collision.y = -objectCollision.y;
							}
						}
					}
				}
			}
		}
		
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