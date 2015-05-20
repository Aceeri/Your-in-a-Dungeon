package main.character;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Manager;
import main.object.Object;
import main.misc.Vector2;

class Node { 
	public Vector2 position;
	public int x;
	public int y;
	public double f;
	public double h;
	public double g;
	public double gTotal = 0;
	
	public Node parent;
	
	public Node(Vector2 p, Vector2 gP, double h, double g) {
		position = p;
		x = (int) gP.x;
		y = (int) gP.y;
		f = h + g;
	}
}

public class Pathfinder {
	private Manager manager;
	private Node[][] nodeMap;
	
	public Pathfinder(Manager m) {
		manager = m;
	}
	
	public boolean intersectingBox(Vector2 position, Vector2 size) {
		for (int i = 0; i < manager.wallContainer.getComponentCount(); i++) {
			Object object = (Object) manager.wallContainer.getComponent(i);
			if (object.inside(position, size)) {
				return true;
			}
		}
		return false;
	}
	
	public Vector2[][] route(Vector2 from, Vector2 to, double nodeSize) {
		Vector2[][] test = new Vector2[nodeMap.length][nodeMap[0].length];
		Node startNode = nodeMap[(int) (Math.round(from.x/nodeSize))][(int) (Math.round(from.y/nodeSize))];
		
		
		
		Node[][] nodeMap = createMap(from, to, nodeSize);
		
		ArrayList<Node> closedSet = new ArrayList<Node>();
		ArrayList<Node> openSet = new ArrayList<Node>();
		ArrayList<Node> navigatedSet = new ArrayList<Node>();
		
		// Add starting position to open set
		openSet.add(startNode);
		
		while (!openSet.isEmpty()) {
			Node currentNode = openSet.get(0);
			
			// Get node with lowest fScore
			for (int i = 0; i < openSet.size(); i++) {
				Node node = openSet.get(i);
				if (node.f < currentNode.f) {
					currentNode = node;
				}
			}
			
			openSet.remove(currentNode);
			closedSet.add(currentNode);
			
			Node[] surrounding = new Node[] {
					nodeMap[currentNode.x - 1][currentNode.y],
					nodeMap[currentNode.x + 1][currentNode.y],
					nodeMap[currentNode.y - 1][currentNode.y],
					nodeMap[currentNode.y + 1][currentNode.y],
					
					nodeMap[currentNode.x - 1][currentNode.y + 1],
					nodeMap[currentNode.x + 1][currentNode.y + 1],
					nodeMap[currentNode.x - 1][currentNode.y - 1],
					nodeMap[currentNode.x + 1][currentNode.y - 1]
			};
			
			for (int i = 0; i < surrounding.length; i++) {
				surrounding[i].parent = currentNode;
				
				if (!closedSet.contains(surrounding[i]) && intersectingBox(surrounding[i].position.sub(new Vector2(nodeSize/2, nodeSize/2)), new Vector2(nodeSize, nodeSize))) {
					closedSet.add(surrounding[i]);
				} else if (!closedSet.contains(surrounding[i]) && !openSet.contains(surrounding[i])) {
					surrounding[i].gTotal = currentNode.gTotal + surrounding[i].h;
					openSet.add(surrounding[i]);
				}
			}
		}
		
		for (int i = 0; i < nodeMap.length; i++) {
			for (int j = 0; j < nodeMap[0].length; j++) {
				test[i][j] = nodeMap[i][j].position;
			}
		}
		return test;
	}
	
	public Node[][] createMap(Vector2 from, Vector2 to, double nodeSize) {
		int startX = (int) (Math.round(from.x/nodeSize));
		int startY = (int) (Math.round(from.y/nodeSize));
		int endX = (int) (Math.round(to.x/nodeSize));
		int endY = (int) (Math.round(to.y/nodeSize));
		
		Vector2 mapSize = new Vector2((int) Math.ceil(manager.defaultScreen.x/nodeSize), (int) Math.ceil(manager.defaultScreen.y/nodeSize));
		Node[][] nodeMap = new Node[(int) mapSize.x][(int) mapSize.y];
		
		for (int i = 0; i < mapSize.x; i ++) {
			for (int j = 0; j < mapSize.y; j ++) {
				double g = Math.sqrt(((i - startX)*nodeSize*(i - startX)*nodeSize) + ((j - startY)*nodeSize*(j - startY)*nodeSize));
				double h = (i - endX)*nodeSize + (j - endY)*nodeSize;
				nodeMap[i][j] = new Node(new Vector2(i*nodeSize, j*nodeSize), new Vector2(i, j), h, g);
			}
		}
		
		return nodeMap;
	}
	
	public Vector2[] nodeToVectorSet(ArrayList<Node> set) {
		Vector2[] list = new Vector2[set.size()];
		
		for (int i = 0; i < set.size(); i++) {
			list[i] = set.get(i).position;
		}
		
		return list;
	}
}
