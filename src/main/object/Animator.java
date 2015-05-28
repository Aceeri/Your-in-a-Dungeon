package main.object;

import java.util.HashMap;

import main.object.Object;

public class Animator {
	public Object parent;
	
	public String path = "resources\\image\\missing.png";
	public HashMap<String, Frame[]> animations = new HashMap<String, Frame[]> ();
	public Frame[] currentAnimation;
	public int currentAnimationFrame = 0;
	public Frame currentFrame;
	
	public Animator(Object object) {
		parent = object;
		path = object.path;
	}
	
	public void setDefaultImage(String defaultPath) {
		path = defaultPath;
	}
	
	public void defineAnimation(String animationName, Frame[] paths) {
		animations.put(animationName, paths);
	}
	
	public void playAnimation(String animationName) {
		if (animations.containsKey(animationName)) {
			currentAnimation = animations.get(animationName);
			currentAnimationFrame = 0;
			currentFrame = currentAnimation[currentAnimationFrame];
		}
	}
	
	public void step(double delta) {
		if (currentFrame != null) {
			parent.path = currentFrame.path;
			if (currentFrame.step(delta)) {
				currentAnimationFrame++;
				if (currentAnimationFrame < currentAnimation.length) {
					currentFrame = currentAnimation[currentAnimationFrame];
				} else {
					currentAnimation = null;
					currentAnimationFrame = 0;
					currentFrame = null;
				}
			}
		}
	}
}
