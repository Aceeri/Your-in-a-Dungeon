import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.Timer;

public class Manager extends JPanel implements ActionListener, KeyListener, MouseListener {
	
	public boolean running = false;
	
	public boolean[] keyPress = new boolean[255];
	public ArrayList<Player> players = new ArrayList<Player>();
	
	public Timer gameTimer;
	public Background background;
	public Player player;
	
	public Vector2 screen;
	
	public Manager(Vector2 screen) {
		this.screen = screen;
		
		this.setBackground(Color.BLACK);
		
		player = new Player(new Vector2(screen.x/2 - 30, screen.y/2 - 30), screen);
		players.add(player);
		
		background = new Background(screen);
		background.setSize(screen.dimension());
		background.setLocation(0, 0);
		
		this.add(player);
		this.add(background);
		
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
		//background.paintComponents(g);
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
		
		Vector2 collision = new Vector2();
		
		for (Object component : background.components) {
			component.paintLocation();
			boolean inside = component.inside(player.getNextPosition(), player.Size);
			
			if (inside) {
				//System.out.println("inside");
				Vector2 push = component.collide(player.getNextPosition(), player.Size, player.velocity);
				//collision = collision.add(push.div(player.speed));
				//System.out.println(push);
				if (collision.x == 0) {
					collision.x = push.div(player.speed).x;
				}
				
				if (collision.y == 0) {
					collision.y = push.div(player.speed).y;
				}
			}
		}
		
		Vector2 frameVelocity = player.velocity;
		frameVelocity = frameVelocity.sub(collision);
		
		player.offset = new Vector2(player.offset.x + frameVelocity.x, player.offset.y + frameVelocity.y);
		background.offset = player.offset;
		
		background.setSize(screen.dimension());
		background.setLocation(0, 0);
		repaint();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == 68 && !this.keyPress[68]) {// && this.position.x + 2*this.size.x < this.screen.x) {
			player.velocity.x -= player.speed;
		}
		if (code == 65 && !this.keyPress[65]) {// && this.position.x > 0) {
			player.velocity.x += player.speed;
		}
		if (code == 83 && !this.keyPress[83]) {// && this.position.y + 2*this.size.y < this.screen.y - 24) {
			player.velocity.y -= player.speed;
		}
		if (code == 87 && !this.keyPress[87]) {// && this.position.y > 0) {
			player.velocity.y += player.speed;
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
