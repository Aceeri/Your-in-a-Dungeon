package main.object;

import main.misc.Vector2;
import main.Manager;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

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
	
	public int callCount = 0;
	
	public Object(Vector2 position) {
		offsetPosition = position;
		offset = new Vector2(0, 0);
		velocity = new Vector2(0, 0);
		
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
	
	public float[] SweptAABB(Object o2) {
		float normalx, normaly;
		
		float b1x = (float) position.x;
		float b1y = (float) position.y;
		float b1w = (float) Size.x;
		float b1h = (float) Size.y;
		
		float b2x = (float) o2.position.x;
		float b2y = (float) o2.position.y;
		float b2w = (float) o2.Size.x;
		float b2h = (float) o2.Size.y;
		
		float xInvEntry, yInvEntry;
	    float xInvExit, yInvExit;
	    
	    if (velocity.x > 0.0f)
	    {
	        xInvEntry = b2x - (b1x + b1w);
	        xInvExit = (b2x + b2w) - b1x;
	    }
	    else 
	    {
	        xInvEntry = (b2x + b2w) - b1x;
	        xInvExit = b2x - (b1x + b1w);
	    }
	
	    if (velocity.y > 0.0f)
	    {
	        yInvEntry = b2y - (b1y + b1h);
	        yInvExit = (b2y + b2h) - b1y;
	    }
	    else
	    {
	        yInvEntry = (b2y + b2h) - b1y;
	        yInvExit = b2y - (b1y + b1h);
	    }
	    
	    // find time of collision and time of leaving for each axis (if statement is to prevent divide by zero)
	    float xEntry, yEntry;
	    float xExit, yExit;
	    
	    if (velocity.x == 0.0f) {
	        xEntry = -Float.POSITIVE_INFINITY;
	        xExit = Float.POSITIVE_INFINITY;
	    } else {
	        xEntry = xInvEntry / (float) (velocity.x * speed);
	        xExit = xInvExit / (float) (velocity.x * speed);
	    }

	    if (velocity.y == 0.0f) {
	        yEntry = -Float.POSITIVE_INFINITY;
	        yExit = Float.POSITIVE_INFINITY;
	    } else {
	        yEntry = yInvEntry / (float) (velocity.y * speed);
	        yExit = yInvExit / (float) (velocity.y * speed);
	    }
	    
	    // find the earliest/latest times of collision
	    float entryTime = Math.max(xEntry, yEntry);
	    float exitTime = Math.min(xExit, yExit);
	    
		// if there was no collision
		if (entryTime > exitTime || xEntry < 0 && yEntry < 0 || xEntry > 1 || yEntry > 1) {
		    return new float[] { 1, 0, 0 };
		} else {
			//if there was a collision, calculate normal of collided surface
			if (xEntry > yEntry) {
				if (xInvEntry < 0.0f) {
				    normalx = 1;
				    normaly = 0;
				} else {
				    normalx = -1;
				    normaly = 0;
				}
			} else {
				if (yInvEntry < 0) {
				    normalx = 0;
				    normaly = 1;
				} else {
				    normalx = 0;
				    normaly = -1;
				}
			}
			
			// return the time of collision
			return new float[] { entryTime, normalx, normaly };
		}
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
	
	public Vector2 checkCollision(Object[] ignoreList) {
		ArrayList<float[]> collisions = new ArrayList<float[]>();
		Vector2 totalCollision = new Vector2();
		
		for (int i = 0; i < manager.getComponentCount(); i++) {
			if (manager.getComponent(i) instanceof Container && manager.getComponent(i) != manager.projectileContainer) {
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
						
						System.out.println(this + " " + object.side(position, Size));
						
						
						
						/*
						 * Swept AABB collision
						 */
						/*Vector2 bpPosition = new Vector2(velocity.x > 0 ? position.x : position.x + Size.x, velocity.y > 0 ? position.y : position.y + Size.y);
						Vector2 bpSize = new Vector2(velocity.x*speed + (velocity.x > 0 ? Size.x : -Size.x), velocity.y*speed + (velocity.y > 0 ? Size.y : -Size.y));
						//Vector2 bpSize = velocity.scalar(speed).add(Size);
						System.out.println(velocity + " : " + bpPosition + " || " + bpSize);
						//Vector2 bpSize = new Vector2(position.x + Size.x + velocity.x*speed, position.y + Size.y + velocity.x*speed);
						
						if (!ignore && this != object && object.collidable && object.inside(bpPosition, bpSize)) {
							float[] test = SweptAABB(object);
							collisions.add(test);
							
							totalCollision = totalCollision.add(new Vector2(test[1], test[2]));
							//totalCollision = totalCollision.sub(new Vector2(velocity.x*(1 - test[0]), velocity.y*(1 - test[0])));
						}*/
					}
				}
			}
		}
		
		String[] updateString = new String[collisions.size() + 1];
		updateString[0] = "collisions";
		float total = 0;
		for (int i = 0; i < collisions.size(); i++) {
			total += collisions.get(i)[1] + collisions.get(i)[2];
			updateString[i + 1] = collisions.get(i)[0] + " " + collisions.get(i)[1] + " " + collisions.get(i)[2];
		}
		
		if (total != 0) {
			manager.ui.updateString(updateString);
		}
		
		return totalCollision;
	}
	
	//default per-frame function
	public void step() {
		paintLocation();
		
		if (!anchored && collidable) {
//			if (velocity.magnitude() > 0) {
				Vector2 offsetVector = checkCollision();
				if (offsetVector.magnitude() > 0) {
					System.out.println(velocity + " " + offsetVector);
				}
				offsetPosition = offsetPosition.add(velocity.add(offsetVector).scalar(speed));
//			}
		}
		
		updatePosition();
	}
	
	public void updatePosition() {
		try {
			position = scalePosition.mult(manager.screen).add(offsetPosition).round();
		} catch (java.lang.NullPointerException e) {
			System.out.println("missing manager: " + this);
			manager = (Manager) getParent().getParent();
			position = scalePosition.mult(manager.screen).add(offsetPosition).round();
		}
	}
	
	public Vector2 getNextPosition() {
		return position.add(velocity.scalar(speed));
	}
	
	public Vector2 getNextPosition(Vector2 velocity) {
		return position.add(velocity);
	}
	
	/*public Vector2 checkCollision(Object[] ignoreList) {
		Vector2 collision = new Vector2();
		
		for (int i = 0; i < manager.getComponentCount(); i++) {
			if (manager.getComponent(i) instanceof Container && manager.getComponent(i) != manager.projectileContainer) {
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
	}*/
	
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