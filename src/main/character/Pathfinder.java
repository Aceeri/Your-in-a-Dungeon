package main.character;

import java.util.ArrayList;

import main.Manager;
import main.object.Object;
import main.misc.Vector2;

public class Pathfinder {
	private Manager manager;
	
	public Pathfinder(Manager m) {
		manager = m;
	}
	
	// check if two boxes intersect
	public boolean intersectingBox(Vector2 position, double size) {
		for (int i = 0; i < manager.wallContainer.getComponentCount(); i++) {
			Object object = (Object) manager.wallContainer.getComponent(i);
			if (!(object instanceof Player) && object != null && object.inside(position, new Vector2(size, size))) {
				return true;
			}
		}
		return false;
	}
	
	// returns a list of positions for something to follow to get to the destination
	public Node[] route(Vector2 from, Vector2 to, double nodeSize) {
		nodeSize = nodeSize*Math.max(manager.ratio.x, manager.ratio.y);
		Node[][] nodeMap = createMap(from, to, nodeSize);
		
		Node startNode = null;
		double startDistance = -1;
		Node endNode = null;
		double endDistance = -1;
		
		// find nodes that are closest to the start and end
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
		
		// simple return if start node is where the end node is, just return no positions
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
			
			openSet.remove(currentNode);
			closedSet.add(currentNode);
			navigatedSet.add(currentNode);
			
			Node[] surrounding;
			try {
				// all surrounding nodes
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
			
			// check if reached end node
			if (currentNode == endNode) {
				ArrayList<Node> path = new ArrayList<Node>();
				path.add(endNode);
				currentNode = endNode;
				
				// recreate path
				int tries = 0;
				while (true) {
					tries++;
					
					// if taking too long, quit
					if (tries > 100) {
						return new Node[] { };
					}
					
					currentNode = currentNode.parent;
					path.add(currentNode);
					
					if (currentNode.equals(startNode)) {
						Node[] sortedPath = new Node[path.size()];
						for (int i = 0; i < path.size(); i++) {
							sortedPath[i] = path.get(path.size() - i - 1);
						}
						return sortedPath;
					}
				}
			}
			
			// check if surrounding node is in closed or open sets or if intersecting
			for (int i = 0; i < surrounding.length; i++) {
				if (!closedSet.contains(surrounding[i])
						&& surrounding[i].x == 0
						|| surrounding[i].y == 0
						|| surrounding[i].x == nodeMap.length - 1
						|| surrounding[i].y == nodeMap[0].length - 1
						|| intersectingBox(surrounding[i].position, nodeSize)) {
					closedSet.add(surrounding[i]);
				} else if (!closedSet.contains(surrounding[i]) && !openSet.contains(surrounding[i])) {
					surrounding[i].parent = currentNode;
					openSet.add(surrounding[i]);
				}
			}
			
			// break if too long
			if (counter > 200) {
				break;
			}
		}
		
		return new Node[] { };
	}
	
	// returns a two dimensional array of nodes according to the manager's canvas size
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
	
	// returns positions in node set as vectors
	public Vector2[] nodeToVectorSet(ArrayList<Node> set) {
		Vector2[] list = new Vector2[set.size()];
		
		for (int i = 0; i < set.size(); i++) {
			list[i] = set.get(i).position;
		}
		
		return list;
	}
}
