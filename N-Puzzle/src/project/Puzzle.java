
package project;


import java.util.Stack;

public class Puzzle {
	private final int[][] tiles;
	private final int size;

	public Puzzle(int[][] tiles) {
		size = tiles.length;
		this.tiles = copy(tiles);
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(tiles.length).append("\n");
		for (int[] tile : tiles) {
			for (int i : tile) {
				str.append(" ").append(i);
			}
			str.append("\n");
		}
		return str.toString();

	}

	public int dimension() {
		return size;
	}


	// sum of Manhattan distances between tiles and goal
	// The Manhattan distance between a board and the goal board is the sum
	// of the Manhattan distances (sum of the vertical and horizontal distance)
	// from the tiles to their goal positions.
	public int h() {
		int h = 0;
		for (int i = 0; i < size; i++){
			for (int j = 0; j < size; j++){
				int tile = tiles[i][j];
				if (tile != 0){
					h += Math.abs(((tile-1) / size)-i) + Math.abs(((tile-1)%size) - j);
				}
			}
		}
		return h;
	}

	public boolean isCompleted() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (tiles[i][j] != size * i + j + 1 && tiles[i][j] != 0) {
					return false;
				}
			}
		}
		return true;
	}


	// Returns any kind of collection that implements iterable.
	// For this implementation, I choose stack.
	public Iterable<Puzzle> getAdjacents() {
		Stack<Puzzle> adjacents = new Stack<>();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				int tile = tiles[i][j];
				if (tile == 0){
					for (int k = 0; k < 2; k++) {
						try {
							int[][] adjacent = copy(tiles);
							adjacent[i][j] = adjacent[i+2*k-1][j];
							adjacent[i+2*k-1][j] = tile;
							adjacents.push(new Puzzle(adjacent));
						}catch (IndexOutOfBoundsException ignored){}
					}
					for (int k = 0; k < 2; k++) {
						try {
							int[][] adjacent = copy(tiles);
							adjacent[i][j] = adjacent[i][j+2*k-1];
							adjacent[i][j+2*k-1] = tile;
							adjacents.push(new Puzzle(adjacent));
						}catch (IndexOutOfBoundsException ignored){}
					}
				}
			}
		}
		return adjacents;
	}

	private int[][] copy(int[][] source) {
		int[][] copy = new int[size][size];
		for (int i = 0; i < size; i++) {
			System.arraycopy(source[i], 0, copy[i], 0, size);
		}
		return copy;
	}


	// You can use this main method to see your Puzzle structure.
	// Actual solving operations will be conducted in Solver.main method
	public static void main(String[] args) {
		int[][] array = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
		Puzzle board = new Puzzle(array);
		System.out.println(board);
		System.out.println(board.dimension());
		System.out.println(board.h());
		System.out.println(board.isCompleted());
		Iterable<Puzzle> itr = board.getAdjacents();
		for (Puzzle neighbor : itr) {
			System.out.println(neighbor);
			System.out.println(neighbor.equals(board));
		}
	}
}

