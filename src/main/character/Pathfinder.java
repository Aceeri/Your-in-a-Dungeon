package main.character;

import java.awt.Color;
import java.util.ArrayList;

import main.Manager;
import main.object.Object;
import main.misc.Vector2;

public class Pathfinder {
	private Manager manager;
	
	public Pathfinder(Manager m) {
		manager = m;
	}
	
	public boolean intersectingBox(Vector2 position, double size) {
		for (int i = 0; i < manager.wallContainer.getComponentCount(); i++) {
			Object object = (Object) manager.wallContainer.getComponent(i);
			if (!(object instanceof Player) && object != null && object.inside(position, new Vector2(size, size))) {
				return true;
			}
		}
		return false;
	}
	
	public Node[] route(Vector2 from, Vector2 to, double nodeSize) {
		nodeSize = nodeSize*Math.max(manager.ratio.x, manager.ratio.y);
		Node[][] nodeMap = createMap(from, to, nodeSize);
		
		Node startNode = null;
		double startDistance = -1;
		Node endNode = null;
		double endDistance = -1;
		for (int i = 0; i < nodeMap.length; i++) {
			for (int j = 0; j < nodeMap[0].length; j++) {
				Node node = nodeMap[i][j];
				if (startDistance == -1 || (from.distance(node.position) < startDistance && !intersectingBox(node.position, nodeSize))) {
					startNode = node;
					startDistance = from.distance(node.position);
				}
				
				if (endDistance == -1 || (to.distance(node.position) < endDistance && !intersectingBox(node.position, nodeSize))) {
					endNode = node;
					endDistance = to.distance(node.position);
				}
			}
		}
		endNode.color = Color.YELLOW;
		
		ArrayList<Node> nodes = new ArrayList<Node>();
		for (int i = 0; i < nodeMap.length; i++) {
			for (int j = 0; j < nodeMap[0].length; j++) {
				nodes.add(nodeMap[i][j]);
			}
		}
		
		if (startNode == endNode) {
			return new Node[] { };
		}
		
		ArrayList<Node> closedSet = new ArrayList<Node>();
		ArrayList<Node> openSet = new ArrayList<Node>();
		ArrayList<Node> navigatedSet = new ArrayList<Node>();
		
		// Add starting position to open set
		openSet.add(startNode);
		
		int counter = 0;
		while (!openSet.isEmpty()) {
			counter++;
			Node currentNode = openSet.get(0);
			
			// Get node with lowest fScore
			for (int i = 0; i < openSet.size(); i++) {
				Node node = openSet.get(i);
				if (Math.abs(node.f) <= Math.abs(currentNode.f)) {
					currentNode = node;
				}
			}
			
			currentNode.color = Color.GREEN;
			openSet.remove(currentNode);
			closedSet.add(currentNode);
			navigatedSet.add(currentNode);
			
			Node[] surrounding;
			try {
				surrounding = new Node[] {
						nodeMap[currentNode.x - 1][currentNode.y],
						nodeMap[currentNode.x + 1][currentNode.y],
						nodeMap[currentNode.x][currentNode.y - 1],
						nodeMap[currentNode.x][currentNode.y + 1],
						
						nodeMap[currentNode.x - 1][currentNode.y - 1],
						nodeMap[currentNode.x + 1][currentNode.y - 1],
						nodeMap[currentNode.x - 1][currentNode.y + 1],
						nodeMap[currentNode.x + 1][currentNode.y + 1]
				};
			} catch (java.lang.ArrayIndexOutOfBoundsException e) {
				return new Node[] { };
			}
			
			if (currentNode == endNode) {
				endNode.color = Color.YELLOW;
				ArrayList<Node> path = new ArrayList<Node>();
				path.add(endNode);
				currentNode = endNode;
				int tries = 0;
				while (true) {
					tries++;
					if (tries > 100) {
						return new Node[] { };
					}
					
					currentNode = currentNode.parent;
					path.add(currentNode);
					currentNode.color = Color.YELLOW;
					
					if (currentNode.equals(startNode)) {
						Node[] sortedPath = new Node[path.size()];
						for (int i = 0; i < path.size(); i++) {
							sortedPath[i] = path.get(path.size() - i - 1);
						}
						return sortedPath;
					}
				}
			}
			
			for (int i = 0; i < surrounding.length; i++) {
				if (!closedSet.contains(surrounding[i])
						&& surrounding[i].x == 0
						|| surrounding[i].y == 0
						|| surrounding[i].x == nodeMap.length - 1
						|| surrounding[i].y == nodeMap[0].length - 1
						|| intersectingBox(surrounding[i].position, nodeSize)) {
					closedSet.add(surrounding[i]);
					surrounding[i].color = Color.BLUE;
				} else if (!closedSet.contains(surrounding[i]) && !openSet.contains(surrounding[i])) {
					surrounding[i].parent = currentNode;
					openSet.add(surrounding[i]);
					surrounding[i].color = Color.CYAN;
				}
			}
			
			
			if (counter > 200) {
				break;
			}
		}
		
		return new Node[] { };
	}
	
	public Node[][] createMap(Vector2 from, Vector2 to, double nodeSize) {
		int endX = (int) (Math.round(to.x/(nodeSize)));
		int endY = (int) (Math.round(to.y/(nodeSize)));
		
		Vector2 mapSize = new Vector2((int) Math.ceil(manager.defaultScreen.x/nodeSize), (int) Math.ceil(manager.defaultScreen.y/nodeSize));
		Node[][] nodeMap = new Node[(int) mapSize.x][(int) mapSize.y];
		
		for (int i = 0; i < (int) mapSize.x; i ++) {
			for (int j = 0; j < (int) mapSize.y; j ++) {
				double h = Math.abs(endX - i) + Math.abs(endY - j);
				nodeMap[i][j] = new Node(new Vector2(i*nodeSize, j*nodeSize), new Vector2(i, j), h);
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
