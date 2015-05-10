
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.Timer;

import org.omg.CORBA.SystemException;

import java.util.Random;

public class Manager extends JPanel implements ActionListener, KeyListener, MouseListener {
	
	public boolean running = false;
	public boolean info = false;
	public boolean fullscreen = false;
	
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
	
	public Vector2 screen;
	public Vector2 ratio;
	
	public Music background = new Music("resources/sound/Again_and_Again.wav");
	public double counter = 0;
	
	public Manager(Vector2 screen) {
		this.screen = screen;
		
		this.setBackground(Color.BLACK);
		
		uiContainer = new Container();
		playerContainer = new Container();
		projectileContainer = new Container();
		wallContainer = new Container();
		floorContainer = new Container();
		
		player = new Player(this, new Vector2(screen.x/2 - 30, screen.y/2 - 30));
		player.speed = 2;
		//Enemy e = new Enemy(this, new Vector2(100, 100));
		
		background.loop = true;
		background.setVolume(1);
		background.play();
		
		//add containers to JPanel
		this.add(uiContainer);
		this.add(wallContainer);
		this.add(playerContainer);
		this.add(projectileContainer);
		this.add(floorContainer);
		playerContainer.add(player);
		//playerContainer.add(e);
		
		vectorContainer = new ArrayList<Vector2[]> ();
		
		//create walls
		testback a = new testback(this, new Vector2(0, 0), new Vector2(screen.x, 30));
		a.collidable = true;
		wallContainer.add(a);
		
		testback b = new testback(this, new Vector2(0, 30), new Vector2(30, screen.y - 90));
		b.collidable = true;
		wallContainer.add(b);
		
		/*testback c = new testback(this, new Vector2(0, -30), new Vector2(screen.x, 30));
		c.scalePosition = new Vector2(1, 0);
		c.collidable = true;
		wallContainer.add(c);
		
		testback d = new testback(this, new Vector2(-30, 30), new Vector2(30, screen.y - 30));
		d.scalePosition = new Vector2(0, 1);
		d.collidable = true;
		wallContainer.add(d);*/
		
		testback pillar = new testback(this, new Vector2(325, 200), new Vector2(30, 30));
		pillar.collidable = true;
		pillar.anchored = true;
		wallContainer.add(pillar);
		
		pillar = new testback(this, new Vector2(355, 200), new Vector2(30, 30));
		pillar.collidable = true;
		pillar.anchored = true;
		wallContainer.add(pillar);
		
		pillar = new testback(this, new Vector2(385, 200), new Vector2(30, 30));
		pillar.collidable = true;
		pillar.anchored = true;
		wallContainer.add(pillar);
		
		pillar = new testback(this, new Vector2(415, 200), new Vector2(30, 30));
		pillar.collidable = true;
		pillar.anchored = true;
		wallContainer.add(pillar);
		
		
		addKeyListener(this);
		addMouseListener(this);
		setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocus();
        
		gameTimer = new Timer(16, this);
		gameTimer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < vectorContainer.size(); i++) {
			new Vector2().drawVector(g, vectorContainer.get(i)[0], vectorContainer.get(i)[1]);
		}
		
		
		//info display
		long now = System.currentTimeMillis();
		
		if (info) {
			g.setColor(Color.GREEN);
			g.drawString("fps:", 5, 15);
			g.drawString("key press:", 5, 30);
			g.drawString("window size:", 5, 45);
			g.drawString("fullscreen:", 5, 60);
			g.drawString("characters:", 5, 75);
			g.drawString("projectiles:", 5, 90);
			
			g.drawString(String.valueOf(1000/(now - lastFrame)), 100, 15);
			g.drawString(String.valueOf(lastKeyPress), 100, 30);
			g.drawString(screen.toString(), 100, 45);
			g.drawString(String.valueOf(fullscreen), 100, 60);
			g.drawString(String.valueOf(playerContainer.getComponentCount()), 100, 75);
			g.drawString(String.valueOf(projectileContainer.getComponentCount()), 100, 90);
		}
		
		//g.translate(500, 500);
		
		lastFrame = now;
	}
	
	public void actionPerformed(ActionEvent e) {
		this.screen = new Vector2(Toolkit.getDefaultToolkit().getScreenSize());
		
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
		if (code >= 37 && code <= 40) {
			Vector2 direction = new Vector2();
			switch (code) {
				case 37:
					direction = new Vector2(-1, -.8);
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
			
			Projectile p = new Projectile(player, direction, .25, 2000);
			p.speed = 3;
			this.projectileContainer.add(p);
		}
		
		//abilities
		//	81 -> Q
		//	69 -> E
		if (code == 81) {
			for (double i = 0; i < 360; i += 10) {
				Projectile p = new Projectile(player, new Vector2(Math.cos(i*Math.PI/180), Math.sin(i*Math.PI/180)), 1, 2000);
				p.speed = 5;
				this.projectileContainer.add(p);
			}
		}
		if (code == 69) {
			
		}
		
		//toggle info
		if (code == 67) {
			info = !info;
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
	public void keyTyped(KeyEvent arg0) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
