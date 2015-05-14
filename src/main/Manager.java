package main;

//project imports
import main.misc.Vector2;
import main.misc.Music;
import main.character.*;
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
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.Timer;

public class Manager extends JPanel implements ActionListener, KeyListener, MouseListener, ComponentListener, ContainerListener {
	
	public boolean running = false;
	public boolean info = false;
	public boolean fullscreen = false;
	
	public JFrame window;
	
	public boolean[] keyPress = new boolean[255];
	public int lastKeyPress = 0;
	
	//highest -> lowest level containers: (highest is displayed above, lowest is displayed below)
	//	ui -> wall -> player -> projectile -> floor
	public Container uiContainer;
	public Container playerContainer;
	public Container projectileContainer;
	public Container wallContainer;
	public Container floorContainer;
	
	public ArrayList<Vector2[]> vectorContainer;
	
	public Timer gameTimer;
	public Player player;
	public long lastFrame = System.currentTimeMillis();
	public long lastFps = 60;
	
	public Vector2 defaultScreen = new Vector2(1440, 900);
	public Vector2 screen = new Vector2(1440, 900);
	public Vector2 ratio = new Vector2(1, 1);
	
	public BufferedImage canvas;
	public UserInterface ui;
	
	public Music backgroundMusic = new Music("resources/sound/Again_and_Again.wav");
	
	public Manager(JFrame window) {
		this.window = window;
		//this.screen = screen;
		canvas = new BufferedImage((int) screen.x, (int) screen.y, BufferedImage.TYPE_INT_ARGB);
		
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
		
		player = new Battlemage(new Vector2(screen.x/2 - 30, screen.y/2 - 30));
		//player = new Player(new Vector2(screen.x/2 - 30, screen.y/2 - 30));
		//Enemy e = new Enemy(new Vector2(100, 100));
		
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
		//addContainerListener(uiContainer);
		playerContainer.add(player);
		//playerContainer.add(e);
		
		ui = new UserInterface();
		uiContainer.add(ui);
		ui.addString(new String[] { "fps" });
		ui.addString(new String[] { "key press" });
		ui.addString(new String[] { "window size" });
		ui.addString(new String[] { "fullscreen" });
		ui.addString(new String[] { "characters" });
		ui.addString(new String[] { "projectiles" });
		ui.addString(new String[] { "collisions" });
		
		vectorContainer = new ArrayList<Vector2[]> ();
		
		//create walls
		testback t1 = new testback(new Vector2(200, 200), new Vector2(50, 500));
		wallContainer.add(t1);
		
		testback t2 = new testback(new Vector2(200, 200), new Vector2(500, 50));
		wallContainer.add(t2);
		
		addKeyListener(this);
		addMouseListener(this);
		addComponentListener(this);
		setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocus();
        
		gameTimer = new Timer(16, this);
		gameTimer.start();
	}
	
	public void paintComponent(Graphics g) throws java.lang.ArithmeticException {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int) screen.x, (int) screen.y);
		g.drawImage((Image) canvas, 0, 0, (int) screen.x, (int) screen.y, null);
		
		for (int i = 0; i < vectorContainer.size(); i++) {
			new Vector2().drawVector(g, vectorContainer.get(i)[0], vectorContainer.get(i)[1]);
		}
		
		long now = System.currentTimeMillis();
		
		//info display
		if ((now - lastFrame) != 0) {
			ui.updateString(new String[] { "fps", String.valueOf(1000/(now - lastFrame)) });
		}
		ui.updateString(new String[] { "key press", String.valueOf(lastKeyPress) });
		ui.updateString(new String[] { "window size", screen.toString() });
		ui.updateString(new String[] { "fullscreen", String.valueOf(fullscreen) });
		ui.updateString(new String[] { "characters", String.valueOf(playerContainer.getComponentCount()) });
		ui.updateString(new String[] { "projectiles", String.valueOf(projectileContainer.getComponentCount()) });
		
		lastFrame = now;
	}
	
	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < getComponentCount(); i++) {	
			if (getComponent(i) instanceof Container) {
				Container container = (Container) getComponent(i);
				container.setSize(screen.dimension());
				container.setLocation(0, 0);
				for (int j = 0; j < container.getComponentCount(); j++) {
					Object object = (Object) container.getComponent(j);
					object.step();
					if (object instanceof Projectile) {
						Projectile projectile = (Projectile) object;
						if (projectile.expired()) {
							container.remove(object);
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
		
		//movement
		//	68 -> D
		//	65 -> A
		//  83 -> W
		//	87 -> S
		if (code == 68 && !this.keyPress[68]) {
			player.velocity.x += 1;
		}
		if (code == 65 && !this.keyPress[65]) {
			player.velocity.x -= 1;
		}
		if (code == 83 && !this.keyPress[83]) {
			player.velocity.y += 1;
		}
		if (code == 87 && !this.keyPress[87]) {
			player.velocity.y -= 1;
		}
		
		//attacking
		//	37 -> left
		//	38 -> up
		//	39 -> right
		//	40 -> down
		
		//abilities
		//	81 -> Q
		//	69 -> E
		if (code == 81) {
			player.ability1();
		}
		if (code == 69) {
			player.ability2();
		}
		
		//toggle info
		if (code == 67) {
			ui.display = !ui.display;
		}
		
		this.keyPress[e.getKeyCode()] = true;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == 68 && this.keyPress[68]) {
			player.velocity.x -= 1;
		}
		if (code == 65 && this.keyPress[65]) {
			player.velocity.x += 1;
		}
		if (code == 83 && this.keyPress[83]) {
			player.velocity.y -= 1;
		}
		if (code == 87 && this.keyPress[87]) {
			player.velocity.y += 1;
		}
		
		this.keyPress[e.getKeyCode()] = false;
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
			Vector2 currentScreen = new Vector2(window.getContentPane().getSize());
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
