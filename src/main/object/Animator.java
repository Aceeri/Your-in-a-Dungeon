package main.object;

import java.util.HashMap;

import main.misc.Vector2;
import main.object.Object;

public class Animator {
	public Object parent;
	
	public String path = "resources\\image\\missing.png";
	public HashMap<String, Frame[]> animations = new HashMap<String, Frame[]> ();
	public Frame[] currentAnimation;
	public int currentAnimationFrame = 0;
	public Frame currentFrame;
	public boolean loop = false;
	
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
	
	public void playAnimation(String animationName, boolean loop) {
		if (animations.containsKey(animationName)) {
			currentAnimation = animations.get(animationName);
			currentAnimationFrame = 0;
			currentFrame = currentAnimation[currentAnimationFrame];
			this.loop = loop;
		}
	}
	
	public void stopAnimation() {
		currentAnimation = null;
		currentAnimationFrame = 0;
		currentFrame = null;
		loop = false;
	}
	
	public void tweenPosition(Vector2 newPosition, double time) {
		currentAnimation = new Frame[] {
				new Frame(parent.offsetPosition, newPosition, time)
		};
		currentAnimationFrame = 0;
		currentFrame = currentAnimation[currentAnimationFrame];
	}
	
	public void step(double delta) {
		if (currentFrame != null) {
			switch (currentFrame.behavior) {
				case "regular":
					parent.path = currentFrame.path;
					break;
				case "tween":
					parent.offsetPosition = currentFrame.currentPosition;
					break;
			}
			
			if (currentFrame.step(delta)) {
				currentAnimationFrame++;
				if (currentAnimationFrame < currentAnimation.length) {
					currentFrame = currentAnimation[currentAnimationFrame];
				} else {
					if (loop) {
						currentAnimationFrame = 0;
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
}
