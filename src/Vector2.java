import java.awt.Dimension;

public class Vector2 {
	
	public double x = 0;
	public double y = 0;
	
	public Vector2() { }
	
	public Vector2(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	//Basic math
	public Vector2 add(Vector2 v) {
		return new Vector2(this.x + v.x, this.y + v.y);
	}
	
	public Vector2 sub(Vector2 v) {
		return new Vector2(this.x - v.x, this.y - v.y);
	}
	
	public Vector2 mult(double k) {
		return new Vector2(this.x*k, this.y*k);
	}
	
	public Vector2 mult(int k) {
		return new Vector2(this.x*k, this.y*k);
	}
	
	public Vector2 div(double k) {
		return new Vector2(this.x/k, this.y/k);
	}
	
	public Vector2 div(int k) {
		return new Vector2(this.x/k, this.y/k);
	}
	
	
	
	//Rounding of vector positions
	public Vector2 ceil() {
		return new Vector2(Math.ceil(this.x), Math.ceil(this.y));
	}
	
	public Vector2 floor() {
		return new Vector2(Math.floor(this.x), Math.floor(this.y));
	}
	
	public Vector2 round() {
		return new Vector2(Math.round(this.x), Math.round(this.y));
	}
	
	
	public double magnitude() {
		return Math.sqrt(this.dot(this));
	}
	
	public double dot(Vector2 v) {
		return this.x*v.x + this.y*v.y;
	}
	
	//Angle to Origin
	public double angle() {
		return Math.atan2(this.y, this.x);
	}
	
	//Angle to Vector
	public double angle(Vector2 v) {
		return Math.atan2(v.y-this.y, v.x-this.x);
	}
	
	public Vector2 normalize() {
		return this.div(this.magnitude());
	}
	
	
	
	public Dimension dimension() {
		return new Dimension((int) this.x, (int) this.y);
	}
	
	public String toString() {
		return "<" + this.x + ", " + this.y +">";
	}
	
}
