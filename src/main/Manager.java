package main;

//project imports
import main.misc.Vector2;
import main.misc.Music;
import main.character.*;
import main.character.classes.Battlemage;
import main.object.wall.*;
import main.object.floor.*;
import main.object.Object;
import main.object.Projectile;
import main.ui.UserInterface;








//default java imports
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Timer;

@SuppressWarnings("serial")
public class Manager extends JPanel implements ActionListener, KeyListener, MouseListener, ComponentListener, ContainerListener {
	
	public boolean running = false;
	public boolean info = false;
	public boolean fullscreen = false;
	
	public Window window;
	
	public boolean[] keyPress = new boolean[255];
	public int lastKeyPress = 0;
	
	//highest -> lowest level containers: (highest is displayed above, lowest is displayed below)
	//	ui -> wall -> player -> projectile -> floor
	public Container uiContainer;
	public Container playerContainer;
	public Container projectileContainer;
	public Container wallContainer;
	public Container floorContainer;
	
	public Pathfinder pathfinder;
	
	public Timer gameTimer;
	public Player player;
	public long lastFrame = System.nanoTime();
	public double fixedFps = 60.0;
	public boolean displayEnemyMovements = false;
	
	public Vector2 defaultScreen = new Vector2(1920, 1080);
	public Vector2 screen = new Vector2(1920, 1080);
	public Vector2 ratio = new Vector2(1, 1);
	
	public BufferedImage canvas;
	public UserInterface ui;
	public String currentTween = "r";
	public int tween = 1;
	public int cr = 0;
	public int cg = 0;
	public int cb = 0;
	public int angle = 0;
	
	public Music backgroundMusic = new Music("resources/sound/Again_and_Again.wav");
	
	public Manager(Window window) {
		this.window = window;
		
		canvas = new BufferedImage((int) screen.x, (int) screen.y, BufferedImage.TYPE_INT_RGB);
		
		setBackground(Color.BLACK);
		
		uiContainer = new Container();
		playerContainer = new Container();
		projectileContainer = new Container();
		wallContainer = new Container();
		floorContainer = new Container();
		
		uiContainer.addContainerListener(this);
		playerContainer.addContainerListener(this);
		projectileContainer.addContainerListener(this);
		wallContainer.addContainerListener(this);
		floorContainer.addContainerListener(this);
		
		pathfinder = new Pathfinder(this);
		
		player = new Battlemage(new Vector2(500, 500));
		
		backgroundMusic.loop = true;
		backgroundMusic.setVolume(1);
		//backgroundMusic.play();
		
		Background bg = new Background(screen);
		floorContainer.add(bg);
		
		//add containers to JPanel
		add(uiContainer);
		add(wallContainer);
		add(playerContainer);
		add(projectileContainer);
		add(floorContainer);
		playerContainer.add(player);
		playerContainer.add(new Enemy(new Vector2(100, 500)));
		playerContainer.add(new Enemy(new Vector2(400, 100)));
		playerContainer.add(new Enemy(new Vector2(700, 600)));
		
		ui = new UserInterface();
		uiContainer.add(ui);
		
		//info user interface
		ui.addString(new String[] { "fps" });
		ui.addString(new String[] { "key press" });
		ui.addString(new String[] { "window size" });
		ui.addString(new String[] { "fullscreen" });
		ui.addString(new String[] { "characters" });
		ui.addString(new String[] { "projectiles" });
		
		//create walls
		testback t1 = new testback(new Vector2(200, 200), new Vector2(50, 500));
		wallContainer.add(t1);
		
		t1 = new testback(new Vector2(200, 200), new Vector2(500, 50));
		wallContainer.add(t1);
		
		t1 = new testback(new Vector2(300, 300), new Vector2(50, 800));
		wallContainer.add(t1);
		
		addKeyListener(this);
		addMouseListener(this);
		addComponentListener(this);
		setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocus();
        
		gameTimer = new Timer(0, this);
		gameTimer.start();
	}
	
	public void paintComponent(Graphics g) throws java.lang.ArithmeticException {
		super.paintComponent(g);
		AffineTransform at = new AffineTransform();
		at.scale(screen.x/canvas.getWidth(), screen.y/canvas.getHeight());
		
		//supar rotut
		/*at.translate(canvas.getWidth()/2, canvas.getHeight()/2);
		at.rotate(angle*Math.PI/180);
		at.translate(-canvas.getWidth()/2, -canvas.getHeight()/2);
		angle += 1;*/
		
		//player.rotation -= 1;
		
		Graphics2D g2 = (Graphics2D) g;
		g2.drawRenderedImage(canvas, at);
		
		//supar seizur
		/*if (currentTween.equals("r")) {
			cr += 5*tween;
			if ((tween == 1 && cr >= 255) || (tween == -1 && cr <= 0)) {
				currentTween = "g";
			}
		} else if (currentTween.equals("g")) {
			cg += 5*tween;
			if ((tween == 1 && cg >= 255) || (tween == -1 && cg <= 0)) {
				currentTween = "b";
			}
		} else if (currentTween.equals("b")) {
			cb += 5*tween;
			if ((tween == 1 && cb >= 255) || (tween == -1 && cb <= 0)) {
				tween = -tween;
				currentTween = "r";
			}
		}
		g2.setColor(new Color(cr, cg, cb, 150));
		g2.fillRect(0, 0, (int) screen.x, (int) screen.y);*/
		
		//info display
		ui.updateString(new String[] { "key press", String.valueOf(lastKeyPress) });
		ui.updateString(new String[] { "window size", screen.toString() });
		ui.updateString(new String[] { "fullscreen", String.valueOf(fullscreen) });
		ui.updateString(new String[] { "characters", String.valueOf(playerContainer.getComponentCount()) });
		ui.updateString(new String[] { "projectiles", String.valueOf(projectileContainer.getComponentCount()) });
	}
	
	public void actionPerformed(ActionEvent e) {
		long now = System.nanoTime();
		double delta = ((System.nanoTime()) - lastFrame) / 1000000;
		delta = delta/1000;
		lastFrame = now;
		
		ui.updateString(new String[] { "fps", String.format("%.0f", 1/delta) });
		
		for (int i = 0; i < getComponentCount(); i++) {	
			if (getComponent(i) instanceof Container) {
				Container container = (Container) getComponent(i);
				container.setSize(screen.dimension());
				container.setLocation(0, 0);
				
				for (int j = 0; j < container.getComponentCount(); j++) {
					Object object = (Object) container.getComponent(j);
					object.step(delta);
					if (object instanceof Projectile) {
						Projectile projectile = (Projectile) object;
						if (projectile.expired()) {
							container.remove(object);
						}
					}
					
					if (displayEnemyMovements) {
						if (object instanceof Enemy) {
							Enemy enemy = (Enemy) object;
							enemy.drawPath(canvas);
						}
					}
				}
			}
		}
		
		int code = (keyPress[37] ? 37 : keyPress[38] ? 38 : keyPress[39] ? 39 : keyPress[40] ? 40 : 0);
		if (code != 0) {
			Vector2 direction = new Vector2();
			switch (code) {
				case 37:
					direction = new Vector2(-1, 0);
					break;
				case 38:
					direction = new Vector2(0, -1);
					break;
				case 39:
					direction = new Vector2(1, 0);
					break;
				case 40:
					direction = new Vector2(0, 1);
					break;
			}
			player.attack(direction);
		}
		
		repaint();
	}

	
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		lastKeyPress = code;
		
		if (!keyPress[code]) {
			switch (code) {
				//movement
				//	68 -> D
				//	65 -> A
				//  83 -> W
				//	87 -> S
				case 68:
					player.velocity.x += 1;
					break;
				case 65:
					player.velocity.x -= 1;
					break;
				case 83:
					player.velocity.y += 1;
					break;
				case 87:
					player.velocity.y -= 1;
					break;
			}
		}
		
		switch(code) {
			//attacking
			//	37 -> left
			//	38 -> up
			//	39 -> right
			//	40 -> down
				
			//abilities
			//	81 -> Q
			//	69 -> E
			case 81:
				player.ability1();
				break;
			case 69:
				player.ability2();
				break;
			
			//toggle info
			case 67:
				ui.display = !ui.display;
				break;
			
			//toggle pathfinding drawing
			case 86:
				displayEnemyMovements = !displayEnemyMovements;
				break;
				
			//toggle fullscreen
			case 122:
				if (window.fullscreen) {
					window.setWindowed(screen);
				} else {
					window.setFullscreen();
				}
				break;
		}
		
		keyPress[e.getKeyCode()] = true;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (keyPress[code]) {
			switch (code) {
				case 68:
					player.velocity.x -= 1;
					break;
				case 65:
					player.velocity.x += 1;
					break;
				case 83:
					player.velocity.y -= 1;
					break;
				case 87:
					player.velocity.y += 1;
					break;
			}
		}
		
		keyPress[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent arg0) { }

	@Override
	public void mouseClicked(MouseEvent e) { }

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) { }

	@Override
	public void mouseReleased(MouseEvent e) { }

	@Override
	public void componentHidden(ComponentEvent e) { }

	@Override
	public void componentMoved(ComponentEvent e) { }

	@Override
	public void componentResized(ComponentEvent e) {
		if (window != null) {
			screen = screen.div(ratio);
			Vector2 currentScreen = new Vector2(window.frame.getContentPane().getSize());
			ratio = currentScreen.div(defaultScreen);
			screen = screen.mult(ratio);
		}
	}

	@Override
	public void componentShown(ComponentEvent e) { }

	@Override
	public void componentAdded(ContainerEvent e) {
		if (e.getChild() instanceof Object) {
			Object addedObject = (Object) e.getChild();
			addedObject.manager = this;
		}
	}

	@Override
	public void componentRemoved(ContainerEvent e) { }
}
