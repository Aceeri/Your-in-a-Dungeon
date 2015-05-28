package main.object;

public class Frame {
	
	public double waited = 0.0;
	public int wait = 0;
	public String behavior = "regular";
	public String path = "resources\\image\\missing.png";
	
	public Frame(int waitTime, String resource) {
		wait = waitTime;
		path = resource;
	}
	
	public boolean step(double delta) {
		waited += delta;
		if (waited >= wait) {
			return true;
		}
		return false;
	}
}
