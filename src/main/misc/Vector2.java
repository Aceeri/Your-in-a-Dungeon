package main.misc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class Vector2 {
	public double x = 0;
	public double y = 0;
	
	//Constructors
	public Vector2() { }
	
	public Vector2(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2(Dimension d) {
		this.x = d.width;
		this.y = d.height;
	}
	
	public Vector2(Point p) {
		this.x = p.x;
		this.y = p.y;
	}
	
	
	//Basic math
	public Vector2 add(Vector2 v) {
		return new Vector2(this.x + v.x, this.y + v.y);
	}
	
	public Vector2 sub(Vector2 v) {
		return new Vector2(this.x - v.x, this.y - v.y);
	}
	
	public Vector2 scalar(double k) {
		return new Vector2(this.x*k, this.y*k);
	}
	
	public Vector2 scalar(int k) {
		return new Vector2(this.x*k, this.y*k);
	}
	
	public Vector2 div(Vector2 v) {
		return new Vector2(this.x/v.x, this.y/v.y);
	}
	
	public Vector2 mult(Vector2 v) {
		return new Vector2(this.x*v.x, this.y*v.y);
	}
	
	
	//Rounding/absolute value of vector positions
	public Vector2 ceil() {
		return new Vector2(Math.ceil(this.x), Math.ceil(this.y));
	}
	
	public Vector2 floor() {
		return new Vector2(Math.floor(this.x), Math.floor(this.y));
	}
	
	public Vector2 round() {
		return new Vector2(Math.round(this.x), Math.round(this.y));
	}
	
	public Vector2 abs() {
		return new Vector2(Math.abs(this.x), Math.abs(this.y));
	}

	
	//Vector length
	public double magnitude() {
		return Math.sqrt(this.dot(this));
	}
	
	public double dot(Vector2 v) {
		return this.x*v.x + this.y*v.y;
	}
	
	public Vector2 cross() {
		return new Vector2(-this.y, this.x);
	}
	
	public Vector2 normalize() {
		return this.scalar(1/this.magnitude());
	}
	
	public double distance(Vector2 v) {
		return Math.sqrt((this.x - v.x)*(this.x - v.x) + (this.y - v.y)*(this.y - v.y));
	}
	
	
	//Return values
	public Dimension dimension() {
		return new Dimension((int) this.x, (int) this.y);
	}
	
	public Point point() {
		return new Point((int) this.x, (int) this.y);
	}
	
	public String toString() {
		return "<" + this.x + ", " + this.y +">";
	}
	
	public void drawVector(Graphics g, Vector2 v1, Vector2 v2) {
		g.setColor(Color.RED);
		
		for (int i = 0; i < v1.distance(v2); i ++) {
			g.fillRect((int) (v1.x + (v2.x*i/v1.distance(v2))) - 1 , (int) (v1.y + (v2.y*i/v1.distance(v2))) - 1, 3, 3);
		}
	}
	
}
