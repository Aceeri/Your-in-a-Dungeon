package main.object;

import main.misc.Vector2;
import main.object.wall.Door;
import main.ui.TextLabel;
import main.Manager;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

@SuppressWarnings("serial")
public class Object extends JComponent {
	public Manager manager;
	public String type = "object";
	public ArrayList<TextLabel> labels = new ArrayList<TextLabel> ();
	
	public Vector2 scalePosition = new Vector2(0, 0); // dependent on screen size (e.g. (1, 0) will be displayed at the right of the screen)
	public Vector2 offsetPosition = new Vector2(0, 0);
	public Vector2 position = new Vector2(0, 0); // totaled position of scale and offset
	public Vector2 scaleSize = new Vector2(0, 0);
	public Vector2 offsetSize = new Vector2(0, 0);
	public Vector2 Size = new Vector2(0, 0);
	public Vector2 velocity = new Vector2(0, 0); // how much the object moves every 1 delta
	
	public double rotation = 0; // displayed rotation (in degrees)
	public double speed = 0;
	public double scale = 1; // scale of object's image
	
	// where the image is drawn from
	public String imageX; // "left", "right", "center"
	public String imageY; // "top", "bottom", "center"
	
	public boolean collidable = false;
	public boolean anchored = false;
	public boolean stretch = false;
	
	protected String path = "resources\\image\\missing.png";
	protected String previousPath = path;
	protected BufferedImage image;
	
	public Object(Vector2 p) {
		offsetPosition = p;
		paintLocation();
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) manager.canvas.getGraphics();
		
		//positioning and size
		AffineTransform objectTransform = new AffineTransform();
		
		if (previousPath != path) {
			if (manager.images.containsKey("resources\\image\\missing.png")) {
				image = manager.images.get("resources\\image\\missing.png");
			} else {
				try {
					File file = new File(path);
					if (file.exists()) {
						image = ImageIO.read(file);
						manager.images.put(path, image);
					} else {
						image = ImageIO.read(new File("resources\\image\\missing.png"));
						stretch = true;
					}
					previousPath = path;
				} catch (java.io.IOException e) { }
			}
		}
		
		double posX = position.x;
		double posY = position.y;
		if (!stretch) {
			if (imageX == "center") {
				posX = position.x + (Size.x - image.getWidth()*scale)/2;
			} else if (imageX == "right") {
				posX = position.x + Size.x - image.getWidth()*scale;
			}
			
			if (imageY == "center") {
				posY = position.y + (Size.y - image.getHeight()*scale)/2;
			} else if (imageY == "bottom") {
				posY = position.y + Size.y - image.getHeight()*scale;
			}
		}
		objectTransform.translate(posX, posY);
		
		//rotation
		objectTransform.scale(scale, scale);
		
		objectTransform.translate(Size.x*scale/2, Size.y*scale/2);
		objectTransform.rotate(rotation*Math.PI/180);
		objectTransform.translate(-Size.x*scale/2, -Size.y*scale/2);
		
		if (stretch) {
			objectTransform.scale(Size.x/image.getWidth(), Size.y/image.getHeight());
		}
		
		g2.drawRenderedImage(image, objectTransform);
		
		for (int i = 0; i < labels.size(); i++) {
			TextLabel label = labels.get(i);
			g2.setFont(manager.font.deriveFont((float) (label.size*Math.min(manager.ratio.x, manager.ratio.y))));
			g2.setColor(label.color);
			g2.drawString(label.text, (int) (position.x + label.position.x*manager.ratio.x), (int) (position.y + label.position.y*manager.ratio.y));
		}
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
							
							if (!ignore && object.inside(position.add(velocity.scalar(speed)), Size)) {
								Vector2 normal = object.side(position, Size);
								if (normal.x != 0) {
									totalCollision.x = -normal.x;
								}
								
								if (normal.y != 0) {
									totalCollision.y = -normal.y;
								}
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
		
		if (!anchored) {
			offsetPosition = offsetPosition.add(velocity.add(checkCollision(delta)).scalar(speed*delta*manager.fixedFps));
		}
		
		update();
	}
	
	public void update() {
		try {
			Size = scaleSize.mult(manager.screen).add(offsetSize.mult(manager.ratio)).round();
			position = scalePosition.mult(manager.screen).add(offsetPosition.mult(manager.ratio)).round();
		} catch (java.lang.NullPointerException e) {
			System.out.println("missing manager: " + this);
			manager = (Manager) getParent().getParent();
			Size = scaleSize.mult(manager.screen).add(offsetSize.mult(manager.ratio)).round();
			position = scalePosition.mult(manager.screen).add(offsetPosition.mult(manager.ratio)).round();
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