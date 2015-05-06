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
import java.util.ArrayList;

import javax.swing.Timer;

public class Manager extends JPanel implements ActionListener, KeyListener, MouseListener {
	
	public boolean running = false;
	
	public boolean[] keyPress = new boolean[255];
	public ArrayList<Player> players = new ArrayList<Player>();
	public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	
	
	//highest -> lowest level containers: (highest is displayed above, lowest is displayed below)
	//	ui -> wall -> player -> projectile -> floor
	public Container uiContainer;
	public Container playerContainer;
	public Container projectileContainer;
	public Container wallContainer;
	public Container floorContainer;
	
	public Timer gameTimer;
	public Player player;
	
	public Vector2 screen;
	
	public Manager(Vector2 screen) {
		this.screen = screen;
		
		this.setBackground(Color.BLACK);
		
		player = new Player(new Vector2(screen.x/2 - 30, screen.y/2 - 30), screen);
		players.add(player);
		
		uiContainer = new Container();
		playerContainer = new Container();
		projectileContainer = new Container();
		wallContainer = new Container();
		floorContainer = new Container();
		
		//add containers to JPanel
		this.add(uiContainer);
		this.add(wallContainer);
		this.add(playerContainer);
		this.add(projectileContainer);
		this.add(floorContainer);
		playerContainer.add(player);
		
		//create walls
		testback a = new testback(new Vector2(0, 0), screen, new Vector2(screen.x, 30));
		a.collidable = true;
		wallContainer.add(a);
		
		testback b = new testback(new Vector2(0, 30), screen, new Vector2(30, screen.y - 90));
		b.collidable = true;
		wallContainer.add(b);
		
		testback c = new testback(new Vector2(0, screen.y - 60), screen, new Vector2(screen.x, 30));
		c.collidable = true;
		wallContainer.add(c);
		
		testback d = new testback(new Vector2(screen.x - 30, 30), screen, new Vector2(30, screen.y - 90));
		d.collidable = true;
		wallContainer.add(d);
		
		testback pillar1 = new testback(new Vector2(150, 150), screen, new Vector2(80, 30));
		pillar1.collidable = true;
		wallContainer.add(pillar1);
		
		testback pillar2 = new testback(new Vector2(screen.x - 230, 150), screen, new Vector2(80, 30));
		pillar2.collidable = true;
		wallContainer.add(pillar2);
		
		testback pillar3 = new testback(new Vector2(150, screen.y - 230), screen, new Vector2(80, 30));
		pillar3.collidable = false;
		wallContainer.add(pillar3);
		
		testback pillar4 = new testback(new Vector2(screen.x - 230, screen.y - 230), screen, new Vector2(80, 30));
		pillar4.collidable = true;
		wallContainer.add(pillar4);
		
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
	}
	
	public boolean Vector2Inside(Vector2 Vector2, Vector2 position, Vector2 size) {
		if (Vector2.x < position.x + size.x
			&& Vector2.x > position.x
			&& Vector2.y < position.y + size.y
			&& Vector2.y > position.y) {
				return true;
		}
		return false;
	}
	
	public void actionPerformed(ActionEvent e) {
		player.paintLocation();
		
		//reset size and location
		playerContainer.setSize(screen.dimension());
		playerContainer.setLocation(0, 0);
		projectileContainer.setSize(screen.dimension());
		projectileContainer.setLocation(0, 0);
		wallContainer.setSize(screen.dimension());
		wallContainer.setLocation(0, 0);
		floorContainer.setSize(screen.dimension());
		floorContainer.setLocation(0, 0);
		
		Vector2 collision = new Vector2();
		
		for (int i = 0; i < this.wallContainer.getComponentCount(); i++) {
			Object component = (Object) this.wallContainer.getComponent(i);
			component.screen = this.screen;
			
			boolean inside = component.inside(player.getNextPosition(), player.Size);
			
			if (inside) {
				Vector2 push = component.collide(player.getNextPosition(), player.Size, player.velocity);
				
				if (collision.x == 0) {
					collision.x = push.div(player.speed).x;
				}
				
				if (collision.y == 0) {
					collision.y = push.div(player.speed).y;
				}
			}
		}
		
		for (int i = 0; i < this.projectileContainer.getComponentCount(); i++) {
			Projectile projectile = (Projectile) this.projectileContainer.getComponent(i);
			
			projectile.position = projectile.position.add(projectile.velocity.mult(projectile.speed));
			
			if (projectile.expired()) {
				this.projectileContainer.remove(i);
			}
			
			for (int j = 0; j < this.wallContainer.getComponentCount(); j++) {
				Object component = (Object) this.wallContainer.getComponent(j);
				boolean inside = component.inside(projectile.position, new Vector2(5, 5));
				
				if (inside) {
					if (projectile.bounce) {
						//projectile.position.sub(projectile.velocity.mult(projectile.speed))
						Vector2 newVelocity = component.collide(projectile.position.sub(projectile.velocity.mult(projectile.speed)), new Vector2(5, 5), projectile.velocity);
						projectile.velocity = projectile.velocity.add(newVelocity.mult(2));
					} else {
						this.projectileContainer.remove(i);
					}
				}
			}
			
			//for (int j = 0; j < this.)
		}
		
		Vector2 frameVelocity = player.velocity;
		frameVelocity = frameVelocity.sub(collision);
		
		player.position = new Vector2(player.position.x - frameVelocity.x, player.position.y - frameVelocity.y);
		player.offset = new Vector2();
		
		repaint();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		//movement
		//	68 -> A
		//	65 -> D
		//  83 -> W
		//	87 -> S
		if (code == 68 && !this.keyPress[68]) {
			player.velocity.x -= player.speed;
		}
		if (code == 65 && !this.keyPress[65]) {
			player.velocity.x += player.speed;
		}
		if (code == 83 && !this.keyPress[83]) {
			player.velocity.y -= player.speed;
		}
		if (code == 87 && !this.keyPress[87]) {
			player.velocity.y += player.speed;
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
					direction = new Vector2(-1, -1);
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
			
			Projectile p = new Projectile(player, direction, 1, 2000);
			p.speed = 15;
			this.projectileContainer.add(p);
		}
		
		//abilities
		//	81 -> Q
		//	69 -> E
		if (code == 81) {
			for (int i = 0; i < 360; i++) {
				Projectile p = new Projectile(player, new Vector2(Math.cos(i*Math.PI/180), Math.sin(i*Math.PI/180)), 1, 50000);
				p.speed = 15;
				this.projectileContainer.add(p);
			}
		}
		
		this.keyPress[e.getKeyCode()] = true;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == 68 && this.keyPress[68]) {
			player.velocity.x += player.speed;
		}
		if (code == 65 && this.keyPress[65]) {
			player.velocity.x -= player.speed;
		}
		if (code == 83 && this.keyPress[83]) {
			player.velocity.y += player.speed;
		}
		if (code == 87 && this.keyPress[87]) {
			player.velocity.y -= player.speed;
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
