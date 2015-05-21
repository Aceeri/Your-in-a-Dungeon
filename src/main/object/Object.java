package main.object;

import main.misc.Vector2;
import main.Manager;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
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
	
	public double rotation = 0; // rotation (in degrees)
	
	public double speed = 0;
	public String type = "object";
	
	public Manager manager;
	
	public boolean collidable = false;
	public boolean anchored = false;
	public boolean nodes = false;
	
	protected String path = "resources/image/missing.png";
	protected BufferedImage image;
	
	public int callCount = 0;
	
	public Object(Vector2 position) {
		offsetPosition = position;
		offset = new Vector2(0, 0);
		velocity = new Vector2(0, 0);
		
		Size = new Vector2(0, 0);
		
		paintLocation();
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) manager.canvas.getGraphics();
		
		//positioning and size
		AffineTransform objectTransform = new AffineTransform();
		objectTransform.translate(position.x, position.y);
		objectTransform.scale(Size.x/image.getWidth(), Size.y/image.getHeight());
		
		//rotation
		objectTransform.translate(image.getWidth()/2, image.getHeight()/2);
		objectTransform.rotate(rotation*Math.PI/180);
		objectTransform.translate(-image.getWidth()/2, -image.getHeight()/2);
		
		g2.drawRenderedImage(image, objectTransform);
		//g2.drawRenderedImage(image, (int) position.x, (int) position.y, (int) Size.x, (int) Size.y, null);
	}
	
	public void setImage() {
		try {
			image = ImageIO.read(new File(path));
		} catch (java.io.IOException e) { }
	}
	
	public void paintLocation() {
		setLocation(0, 0);
		setSize(manager == null ? new Vector2().dimension() : manager.screen.dimension());
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
	
	public Vector2 side(Vector2 o2, Vector2 d2) {
		double dx = (position.x + Size.x/2) - (o2.x + d2.x/2);
		double dy = (position.y + Size.y/2) - (o2.y + d2.y/2);
		double width = (Size.x + d2.x)/2;
		double height = (Size.y + d2.y)/2;
		double crossWidth = width*dy;
		double crossHeight = height*dx;

		if (Math.abs(dx) <= width && Math.abs(dy) <= height) {
			if (crossWidth > crossHeight) {
				if (crossWidth > -crossHeight) { //bottom
					return new Vector2(0, 1);
				} else { //left
					return new Vector2(-1, 0);
				}
			} else {
				if (crossWidth > -crossHeight) { //right
					return new Vector2(1, 0);
				} else { //top
					return new Vector2(0, -1);
				}
			}
		}
		
		return new Vector2();
	}
	
	public Vector2 checkCollision(Object[] ignoreList, double delta) {
		Vector2 totalCollision = new Vector2();
		
		for (int i = 0; i < manager.getComponentCount(); i++) {
			if (manager.getComponent(i) instanceof Container && manager.getComponent(i) != manager.projectileContainer) {
				Container container = (Container) manager.getComponent(i);
				for (int j = 0; j < container.getComponentCount(); j++) {
					if (container.getComponent(j) instanceof Object) {
						Object object = (Object) container.getComponent(j);
						if (this != object && object.collidable) {
							boolean ignore = false;
							for (int k = 0; k < ignoreList.length; k++) {
								if (ignoreList[k] == object) {
									ignore = true;
									break;
								}
							}
							
							if (!ignore && object.inside(position.add(velocity.scalar(speed*delta*manager.fixedFps)), Size)) {
								Vector2 normal = object.side(position, Size);
								totalCollision = totalCollision.sub(normal);
							}
						}
					}
				}
			}
		}
		
		return totalCollision;
	}
	
	//default per-frame function
	public void step(double delta) {
		paintLocation();
		
		if (!anchored && collidable) {
			offsetPosition = offsetPosition.add(velocity.add(checkCollision(delta)).scalar(speed*delta*manager.fixedFps));
		}
		
		updatePosition();
	}
	
	public void updatePosition() {
		try {
			position = scalePosition.mult(manager.defaultScreen).add(offsetPosition).round();
		} catch (java.lang.NullPointerException e) {
			System.out.println("missing manager: " + this);
			manager = (Manager) getParent().getParent();
			position = scalePosition.mult(manager.defaultScreen).add(offsetPosition).round();
		}
	}
	
	public Vector2 checkCollision(double delta) {
		return checkCollision(new Object[] { }, delta);
	}
	
	private boolean intersects(Vector2 o1, Vector2 d1, Vector2 o2, Vector2 d2) {
		double u = ((o2.x-o1.x)*d1.y-(o2.y-o1.y)*d1.x)/(d2.y*d1.x-d2.x*d1.y);
		if (0 <=u && u <= 1) {
			double v = (o2.x-o1.x+u*d2.x)*d1.x+(o2.y-o1.y+u*d2.y)*d1.y;
			return 0 <= v && v<= d1.x*d1.x+d1.y*d1.y;
		}
		return false;
	}
	
	public boolean intersects(Vector2 o2, Vector2 d2) {
		//checks if vector intersects sides of rectangle, or if they are inside the rectangle
		if (intersects(position, position.add(new Vector2(Size.x, 0)), o2, d2)
				|| intersects(position, position.add(new Vector2(0, Size.y)), o2, d2)
				|| intersects(position.add(Size), position.add(new Vector2(-Size.x, 0)), o2, d2)
				|| intersects(position.add(Size), position.add(new Vector2(0, -Size.y)), o2, d2)
				|| inside(o2)
				|| inside(d2)) {
			return true;
		}
		return false;
	}
	
	public boolean intersectsAnyWall(Vector2 o2, Vector2 d2) {
		for (int i = 0; i < manager.wallContainer.getComponentCount(); i++) {
			Object wall = (Object) manager.wallContainer.getComponent(i);
			if (wall.intersects(o2, d2)) {
				return true;
			}
		}
		return false;
	}
}