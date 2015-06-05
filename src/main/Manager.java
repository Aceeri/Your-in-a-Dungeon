package main;

//project imports
import main.misc.Vector2;
import main.misc.Music;
import main.character.*;
import main.character.classes.Battlemage;
import main.character.classes.Deprived;
import main.character.enemies.Spider;
import main.character.enemies.Wraith;
import main.dungeon.Dungeon;
import main.dungeon.Room;
import main.object.wall.*;
import main.object.Background;
import main.object.Object;
import main.object.Projectile;
import main.ui.Button;
import main.ui.Debugger;
import main.ui.TextLabel;
import main.ui.UserInterface;


import javax.imageio.ImageIO;
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
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("serial")
public class Manager extends JPanel implements ActionListener, KeyListener, MouseListener, ComponentListener, ContainerListener, Runnable {
	
	public Window window;
	public boolean menu = true;
	public boolean gameOver = false;
	public boolean running = false;
	public boolean debounce = false;
	public boolean entering = false;
	public boolean info = false;
	public boolean fullscreen = false;
	public boolean paused = false;
	public int[] secret = new int[] { 38, 38, 40, 40, 37, 39, 37, 39 };
	public int currentSecret = 0;
	public boolean konami = false;
	
	public boolean[] keyPress = new boolean[600];
	public int lastKeyPress = 0;
	
	//highest -> lowest level containers: (highest is displayed above, lowest is displayed below)
	//	ui -> player -> wall -> projectile -> floor
	public Container uiContainer;
	public Container playerContainer;
	public Container projectileContainer;
	public Container wallContainer;
	public Container floorContainer;
	
	public Pathfinder pathfinder;
	
	public Timer gameTimer;
	public Vector2 mouse = new Vector2();
	public Player player;
	public long lastFrame = System.nanoTime();
	public double fixedFps = 60.0;
	public double fpsCap = 60.0;
	public boolean displayEnemyMovements = false;
	
	public Vector2 defaultScreen = new Vector2(1920, 1080);
	public Vector2 screen = defaultScreen;
	public Vector2 ratio = new Vector2(1, 1);
	
	public BufferedImage canvas;
	public Debugger debugger;
	public String currentTween = "r";
	public int tween = 1;
	public int cr = 0;
	public int cg = 0;
	public int cb = 0;
	public int angle = 0;
	public int counter = 0;
	public Color transition = new Color(0, 0, 0, 0);
	public Color fadeGame = new Color(255, 255, 255, 0);
	public Color screenEffect = new Color(0, 0, 0, 0);
	
	public Music backgroundMusic = new Music("resources\\sound\\Big Mine.wav");
	public Background bg;
	public Wall w1;
	public Font font;
	
	public ArrayList<Object> menuObjects = new ArrayList<Object> ();
	public HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage> ();
	
	public Dungeon currentDungeon;
	public Room currentRoom;
	public int score = 0;
	public TextLabel scoreLabel;
	
	public Manager(Window window) {
		this.window = window;
		
		canvas = new BufferedImage((int) screen.x, (int) screen.y, BufferedImage.TYPE_INT_RGB);
		
		// create default font
		try {
			InputStream myStream = new BufferedInputStream(new FileInputStream("resources\\pixelfont.ttf"));
			font = Font.createFont(Font.TRUETYPE_FONT, myStream).deriveFont(24f);
		} catch (IOException | FontFormatException e) {
			System.out.println("Font could not be made");
		}
		
		uiContainer = new Container();
		playerContainer = new Container();
		projectileContainer = new Container();
		wallContainer = new Container();
		floorContainer = new Container();
		
		// add listeners to check for objects to be added
		uiContainer.addContainerListener(this);
		playerContainer.addContainerListener(this);
		projectileContainer.addContainerListener(this);
		wallContainer.addContainerListener(this);
		floorContainer.addContainerListener(this);
		
		pathfinder = new Pathfinder(this);
		
		backgroundMusic.loop = true;
		backgroundMusic.setVolume(1);
		backgroundMusic.play();
		
		// add containers to JPanel
		add(uiContainer);
		add(playerContainer);
		add(wallContainer);
		add(projectileContainer);
		add(floorContainer);
		
		// create default ui
		debugger = new Debugger();
		uiContainer.add(debugger);
		scoreLabel = new TextLabel("Score: 0", new Vector2(20, 40));
		scoreLabel.size = 48f;
		
		// info user interface
		debugger.addString(new String[] { "fps" });
		debugger.addString(new String[] { "key press" });
		debugger.addString(new String[] { "window size" });
		debugger.addString(new String[] { "fullscreen" });
		debugger.addString(new String[] { "characters" });
		debugger.addString(new String[] { "projectiles" });
		
		// add listeners
		addKeyListener(this);
		addMouseListener(this);
		addComponentListener(this);
		setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocus();
        
        // open menu
        menu();
        
        // start game thread
        Thread gameThread = new Thread(this);
        gameThread.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		AffineTransform at = new AffineTransform();
		
		// resize canvas to fit screen
		at.scale(screen.x/canvas.getWidth(), screen.y/canvas.getHeight());
		
		// konami code thing
		if (konami) {
			at.translate(canvas.getWidth()/2, canvas.getHeight()/2);
			at.rotate(angle*Math.PI/180);
			at.translate(-canvas.getWidth()/2, -canvas.getHeight()/2);
			angle += 1;
		}
		
		// draw canvas onto panel
		Graphics2D g2 = (Graphics2D) g;
		g2.drawRenderedImage(canvas, at);
		
		// check if player is a Battlemage and time is slowed
		if (player != null && player instanceof Battlemage && ((Battlemage) player).timeSlowed) {
			// tint screen cyan
			if (screenEffect.equals(new Color(0, 0, 0, 0))) {
				new Thread() {
					public void run() {
						for (int i = 0; i < 100; i += 5) {
							try {
								Thread.sleep(8);
								screenEffect = new Color(0, 255, 255, i);
							} catch (InterruptedException e) { };
						}
					}
				}.start();
			}
		} else {
			screenEffect = new Color(0, 0, 0, 0);
		}
		
		// other konami code thing
		if (konami) {
			if (currentTween.equals("r")) {
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
			g2.fillRect(0, 0, (int) screen.x, (int) screen.y);
		}
		
		// set screen effects
		g2.setColor(screenEffect);
		g2.fillRect(0, 0, (int) screen.x, (int) screen.y);
		
		g2.setColor(transition);
		g2.fillRect(0, 0, (int) screen.x, (int) screen.y);
		
		// check if game is over
		if (gameOver) {
			g2.setColor(fadeGame);
			g2.setFont(font.deriveFont((float) (256f*ratio.x)));
		    g2.drawString("Game Over", (int) (225*ratio.x), (int) (440*ratio.y));
		    g2.setFont(font.deriveFont((float) (72f*ratio.x)));
		    g2.drawString("score: " + score, (int) (440*ratio.x), (int) (550*ratio.y));
		}
		
		//info display
		debugger.updateString(new String[] { "key press", String.valueOf(lastKeyPress) });
		debugger.updateString(new String[] { "window size", screen.toString() });
		debugger.updateString(new String[] { "fullscreen", String.valueOf(fullscreen) });
		debugger.updateString(new String[] { "characters", String.valueOf(playerContainer.getComponentCount()) });
		debugger.updateString(new String[] { "projectiles", String.valueOf(projectileContainer.getComponentCount()) });
	}
	
	public void menu() {
		// initialize menu
		Button startButton = new Button("Start", "resources\\image\\trans.png", new Vector2(0, 600), new Vector2(700, 70)) {
			public void click() {
				if (!debounce) {
					debounce = true;
					new Thread() {
						public void run() {
							for (int i = 0; i < 255; i += 3) {
								try {
									Thread.sleep(8);
									transition = new Color(0, 0, 0, i);
								} catch (InterruptedException e) { }
							}
							
							manager.start();
							
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) { }
							
							for (int i = 255; i > 0; i -= 3) {
								try {
									Thread.sleep(6);
									transition = new Color(0, 0, 0, i);
								} catch (InterruptedException e) { }
							}
							debounce = false;
						}
					}.start();
				}
			}
		};
		menuObjects.add(startButton);
		
		Background menuBack = new Background(defaultScreen, "resources\\image\\titlescreen.png");
		menuObjects.add(menuBack);
		
		// add menu ui to screen
		for (int i = 0; i < menuObjects.size(); i++) {
			uiContainer.add(menuObjects.get(i));
		}
	}
	
	public void start() {
		// remove all objects from the menu
		for (int i = 0; i < menuObjects.size(); i++) {
			uiContainer.remove(menuObjects.get(i));
		}
		menuObjects.clear();
		
		// score displaying
		uiContainer.add(scoreLabel);
		
		// add dungeon
		currentDungeon = new Dungeon(this, "resources\\dungeons\\floor1.txt");
		currentDungeon.generate();
		
		// add player
		player = new Battlemage(new Vector2(500, 500));
		playerContainer.add(player);
		
		// enter starting room
		enterRoom(new Vector2(), (int) currentDungeon.start.x, (int) currentDungeon.start.y);
		
		// reset key values to prevent player velocity problems
		player.velocity = new Vector2();
		keyPress = new boolean[600];
	}
	
	public void gameOver() {
		// fade into game over screen
		new Thread() {
			public void run() {
				gameOver = true;
				for (int i = 0; i < 255; i += 3) {
					try {
						Thread.sleep(8);
						transition = new Color(0, 0, 0, i);
					} catch (InterruptedException e) { }
				}
				
				
				for (int i = 0; i < 255; i += 3) {
					try {
						Thread.sleep(25);
						fadeGame = new Color(255, 255, 255, i);
					} catch (InterruptedException e) { }
				}
			}
		}.start();
	}
	
	public void enterRoom(Vector2 entrance, int x, int y) {
		new Thread() {
			public void run() {
				entering = true;
				currentRoom = currentDungeon.rooms[x][y];
				
				System.out.println(currentRoom.type);
				switch (currentRoom.type) {
					case "boss":
						backgroundMusic.fadeToNewSong("resources\\sound\\Colosseum.wav");
					case "end":
						backgroundMusic.fadeToNewSong("resources\\sound\\Ending.wav");
					default:
						backgroundMusic.fadeToNewSong("resources\\sound\\Garrison.wav");	
				}
				
				// fade black
				if (!entrance.equals(new Vector2())) {
					for (int i = 0; i < 255; i += 5) {
						try {
							Thread.sleep(6);
							transition = new Color(0, 0, 0, i);
						} catch (InterruptedException e) { }
					}
				}
				
				// remove all objects from frame and set positions
				floorContainer.removeAll();
				wallContainer.removeAll();
				projectileContainer.removeAll();
				playerContainer.removeAll();
				
				if (currentRoom.type.equals("end") ){
					uiContainer.removeAll();
					
					TextLabel s = new TextLabel(scoreLabel.text);
					s.size = 128f;
					s.color = Color.WHITE;
					
					s.position = new Vector2(defaultScreen.x/2 - 350, 700);
					uiContainer.add(s);
					
					Background winBack = new Background(defaultScreen, "resources\\image\\winscreen.png");
					uiContainer.add(winBack);
					
					if (!entrance.equals(new Vector2())) {
						for (int i = 255; i > 0; i -= 3) {
							try {
								Thread.sleep(10);
								transition = new Color(0, 0, 0, i);
							} catch (InterruptedException e) { }
						}
					}
				} else {
					player.offsetPosition = new Vector2(900, 490).add(player.Size.scalar(.5)).sub(new Vector2(700, 280).mult(entrance));
					playerContainer.add(player);
					
					// add rooms objects and enemies
					for (int i = 0; i < currentRoom.objects.size(); i++) {
						Object o = (Object) currentRoom.objects.get(i);
						if (o.type == "floor") {
							floorContainer.add(o);
						} else if (o.type == "wall") {
							wallContainer.add(o);
						} else if (o.type == "enemy") {
							playerContainer.add(o);
						}
					}
					
					// check if entered from elsewhere
					if (!entrance.equals(new Vector2())) {
						for (int i = 255; i > 0; i -= 5) {
							try {
								Thread.sleep(4);
								transition = new Color(0, 0, 0, i);
							} catch (InterruptedException e) { }
						}
					}
				}
				
				entering = false;
			}
		}.start();
	}
	
	public void run() {
		running = true;
		
		// start game drawing and updating
		while (running) {
			try {
				Thread.sleep(1);
				step();
			} catch (InterruptedException e) { }
		}
	}
	
	public void step() {
		// get mouse location on screen
		mouse = new Vector2(MouseInfo.getPointerInfo().getLocation()).sub(new Vector2(window.frame.getLocation()).add(new Vector2(window.frame.getInsets())));
		
		// get delta and frames per second
		mouse = new Vector2(MouseInfo.getPointerInfo().getLocation()).sub(new Vector2(window.frame.getLocation()).add(new Vector2(window.frame.getInsets())));
		long now = System.nanoTime();
		double delta = ((System.nanoTime()) - lastFrame) / 1000000;
		delta = delta/1000;
		
		// check if time changed surpasses limit on fps
		boolean skip = false;
		if (1/delta > fpsCap) {
			skip = true;
		}
		
		if (!skip) {
			lastFrame = now;
			debugger.updateString(new String[] { "fps", String.format("%.0f", 1/delta) });
			scoreLabel.text = "Score: " + score;
			
			ArrayList<Player> characters = new ArrayList<Player>();
			
			for (int i = 0; i < getComponentCount(); i++) {
				if (getComponent(i) instanceof Container) {
					Container container = (Container) getComponent(i);
					
					// fix container size and location
					container.setSize(screen.dimension());
					container.setLocation(0, 0);
					
					for (int j = 0; j < container.getComponentCount(); j++) {
						Object object = (Object) container.getComponent(j);
						
						// stop all components
						if (!paused) {
							// call step method for all objects
							object.step(delta);
						}
						
						if (object instanceof Projectile) {
							Projectile projectile = (Projectile) object;
							if (projectile.expired()) {
								container.remove(object);
							}
						}
						
						// check if the object is a player and remove if health is less than 0
						if (object instanceof Player) {
							Player plr = (Player) object;
							
							if (plr.health <= 0) {
								playerContainer.remove(plr);
								
								// check if the character is the player being removed else add score
								if (plr.type == "player") {
									gameOver();
								} else {
									Enemy enemy = (Enemy) plr;
									score += enemy.score;
								}
							} else {
								// add player to character array list to assign ZOrder
								characters.add(plr);
							}
						}
						
						// check if mouse is hovering
						if (object instanceof UserInterface) {
							UserInterface ui = (UserInterface) object;
							if (ui.inside(mouse) && !ui.hovering) {
								ui.hovering = true;
							} else if (!ui.inside(mouse)) {
								ui.hovering = false;
							}
						}
						
						// display pathfinder movements
						if (displayEnemyMovements) {
							if (object instanceof Enemy) {
								Enemy enemy = (Enemy) object;
								enemy.drawPath(canvas);
							}
						}
					}
				}
			}
			
			// Sort character list according to y position to assign z order
			for (int i = 0; i + 1 < characters.size(); i++) {
				if (characters.get(i).position.y < characters.get(i + 1).position.y) {
					Player temp = characters.get(i);
					characters.set(i, characters.get(i + 1));
					characters.set(i + 1, temp);
					i = 0;
				}
			}
			
			// assign ZOrder to characters
			for (int i = 0; i < characters.size(); i++) {
				if (characters.get(i).getParent() == playerContainer) {
					try {
						playerContainer.setComponentZOrder(characters.get(i), i);
					} catch (java.lang.IllegalArgumentException e) {
						System.out.println("ZOrder error");
					}
				}
			}
			
			// check if player is pressing any attack buttons
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
				
				if (!paused && player != null) {
					player.attack(direction);
				}
			}
			
			// repaint the canvas for new frame
			repaint();
		}
	}
	
	public BufferedImage getImage(String path) {
		BufferedImage image;
		
		// check if manager's image list already has path, if not make new image and add to list
		if (images.containsKey(path)) {
			return images.get(path);
		} else {
			try {
				File file = new File(path);
				if (file.exists()) {
					image = ImageIO.read(file);
					images.put(path, image);
					return image;
				} else {
					return ImageIO.read(new File("resources\\image\\missing.png"));
				}
			} catch (java.io.IOException e) {
				return null;
			}
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		lastKeyPress = code;
		
		// konami code thing ( up -> up -> down -> down -> left -> right -> left -> right)
		if (secret[currentSecret] == code) {
			currentSecret++;
			if (currentSecret == secret.length) {
				currentSecret = 0;
				konami = !konami;
				angle = 0;
			}
		} else {
			currentSecret = 0;
		}
		
		if (player != null) {
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
				
				//toggle pathfinding drawing
				case 86:
					displayEnemyMovements = !displayEnemyMovements;
					break;
				
				//pause
				case 80:
					if (paused) {
						fixedFps = 60;
					} else {
						fixedFps = 0;
					}
					paused = !paused;
					break;
			}
		}
		
		//key presses (all)
		switch(code) {
			//toggle info
			case 67:
				debugger.display = !debugger.display;
				break;
			
			//toggle fullscreen
			case 122:
				if (window.fullscreen) {
					window.setWindowed();
				} else {
					window.setFullscreen();
				}
				break;
				
			//exits fullscreen
			case 27:
				if (window.fullscreen) {
					window.setWindowed();
				}
		}
		
		keyPress[e.getKeyCode()] = true;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		// player loses velocity because of no movement key presses
		if (player != null && keyPress[code]) {
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
	public void mouseClicked(MouseEvent e) {
		for (int i = 0; i < uiContainer.getComponentCount(); i++) {
			if (uiContainer.getComponent(i) instanceof UserInterface) {
				UserInterface ui = (UserInterface) uiContainer.getComponent(i);
				if (ui.inside(mouse)) {
					//ui.click();
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) {
		for (int i = 0; i < uiContainer.getComponentCount(); i++) {
			if (uiContainer.getComponent(i) instanceof UserInterface) {
				UserInterface ui = (UserInterface) uiContainer.getComponent(i);
				if (ui.inside(mouse)) {
					ui.clickDown();
					ui.click();
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (int i = 0; i < uiContainer.getComponentCount(); i++) {
			if (uiContainer.getComponent(i) instanceof UserInterface) {
				UserInterface ui = (UserInterface) uiContainer.getComponent(i);
				if (ui.inside(mouse)) {
					ui.clickUp();
				}
			}
		}
	}

	@Override
	public void componentHidden(ComponentEvent e) { }

	@Override
	public void componentMoved(ComponentEvent e) { }

	@Override
	public void componentResized(ComponentEvent e) {
		resolutionChange();
	}
	
	public void resolutionChange() {
		if (window != null) {
			screen = screen.div(ratio);
			Vector2 currentScreen = new Vector2(window.frame.getContentPane().getSize());
			ratio = currentScreen.div(defaultScreen);
			screen = screen.mult(ratio);
			canvas = new BufferedImage((int) screen.x, (int) screen.y, BufferedImage.TYPE_INT_RGB);
			
			// update components graphics to new bufferedimage
			Graphics2D newG2 = (Graphics2D) canvas.getGraphics();
			for (int i = 0; i < getComponentCount(); i++) {	
				if (getComponent(i) instanceof Container) {
					Container container = (Container) getComponent(i);
					
					for (int j = 0; j < container.getComponentCount(); j++) {
						Object object = (Object) container.getComponent(j);
						object.g2 = newG2;
					}
				}
			}
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

	@Override
	public void actionPerformed(ActionEvent arg0) { }
}
