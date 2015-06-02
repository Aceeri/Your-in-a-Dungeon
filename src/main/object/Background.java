package main.object;

import main.misc.Vector2;

@SuppressWarnings("serial")
public class Background extends main.object.Object {
	
	private String[] imagePaths = new String[] {
			"resources\\image\\floor1.png",
			"resources\\image\\floor2.png",
			"resources\\image\\floor3.png",
	};
	
	public Background(Vector2 s) {
		super(new Vector2());
		offsetSize = s;
		stretch = true;
		
		type = "floor";
		
		// get random background image
		String picked = imagePaths[(int) Math.round(Math.random() * (imagePaths.length - 1))];
		path = picked;
	}
	
	public Background(Vector2 s, String path) {
		super(new Vector2());
		offsetSize = s;
		stretch = true;
		
		this.path = path;
	}
}
