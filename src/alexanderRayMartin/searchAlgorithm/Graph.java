package alexanderRayMartin.searchAlgorithm;

import java.util.LinkedList;
import java.util.Queue;

public class Graph {

	private int[][] adj;
	private boolean[] visited;
	private int numVertices;
	private int rows;
	private int cols;

	public Graph(int rows, int cols) { // graph constructor
		this.rows = rows;
		this.cols = cols;
		createAdj();
	}

	public void createAdj() { // creates the adj matrix
		numVertices = rows * cols;
		visited = new boolean[numVertices];
		adj = new int[numVertices][numVertices];
		for (int y = 0; y < numVertices; y++) {
			for (int x = 0; x < numVertices; x++) {
				adj[y][x] = 0;
			}
		}
	}

	public int vertices() {
		return numVertices;
	}

	public int[] getPath(int start, int stop) { // Finds shortest path
		// this algorithm checks adjacent blocks
		// and chooses the one with the shortest path
		// to add to the array

		int shortestPath = fewestEdgePath(start, stop);
		if (shortestPath == -1)
			return new int[0]; // return if no path
		int[] path = new int[shortestPath];
		int index = 0;
		int distance = -1;
		int closestLocation = 0;
		int y = start;
		boolean foundEdge;
		while (distance != 1) {
			foundEdge = false;
			for (int x = 0; x < numVertices; x++) {
				if (adj[y][x] == 1) { // checks for adjacency
					distance = fewestEdgePath(x, stop);
					if (distance != -1 && distance < shortestPath) {
						closestLocation = x;
						shortestPath = distance;
						foundEdge = true;
					}
				}
			}
			if (foundEdge) {
				path[index] = closestLocation;
				y = closestLocation;
				index++;
			}
		}
		return path;
	}

	public void addEdge(int from, int to) { // adds an edge to adj matrix
		if (from < 0 || from >= numVertices || to < 0 || to >= numVertices)
			return; // Invalid vertex
		adj[from][to] = 1;
		adj[to][from] = 1;
	}

	public void removeEdge(int from, int to) { // removes an edge from adj
												// matrix
		if (from < 0 || from >= numVertices || to < 0 || to >= numVertices)
			return; // Invalid vertex
		adj[from][to] = 0;
		adj[to][from] = 0;
	}

	public boolean isPath(int start, int stop) { // returns true if a path
													// exists
		visitHelper(start);
		if (visited[stop] == true)
			return true;
		return false;
	}

	private void visitHelper(int start) { // helper function for visit
		for (int i = 0; i < visited.length; i++)
			visited[i] = false;
		visit(start);
	}

	private void visit(int start) { // visits each element
		visited[start] = true;
		for (int i = 0; i < vertices(); i++) {
			if (adj[start][i] == 1 && visited[i] == false)
				visit(i);
		}
	}

	public int fewestEdgePath(int start, int stop) { // returns shortest path
														// length
		Queue<Integer> queue = new LinkedList<>();
		Queue<Integer> tempQueue = new LinkedList<>();
		queue.add(start);
		int current = start;
		int count = 1;
		if (!isPath(start, stop))
			return -1; // no path: -1 is escape value for other functions
		if (start == stop)
			return 0;
		for (int i = 0; i < visited.length; i++)
			visited[i] = false;
		visited[start] = true;
		while (true) {
			while (!queue.isEmpty()) {
				current = queue.remove();
				for (int i = 0; i < vertices(); i++) {
					if (adj[current][i] == 1) {
						if (i == stop)
							return count;
						if (!visited[i]) {
							tempQueue.add(i);
							visited[i] = true;
						}
					}
				}
			}
			count++;
			while (!tempQueue.isEmpty()) {
				current = tempQueue.remove();
				for (int i = 0; i < vertices(); i++) {
					if (adj[current][i] == 1) {
						if (i == stop)
							return count;
						if (!visited[i]) {
							queue.add(i);
							visited[i] = true;
						}

					}
				}
			}
			count++;
		}
	}

	public static class Error extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Error(String message) {
			super(message);
		}
	}
}
