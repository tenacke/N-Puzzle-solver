
package project;
   
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class Solver {
	private final PriorityQueue<PriorityObject> path;
	private final HashMap<Puzzle, Integer> costs;
	private PriorityObject winner;
	private int moves = 0;

	// priority = moves + manhattan
	// if priority is low, it's good.
	// find a solution to the initial board
	public Solver(Puzzle root) {
		path = new PriorityQueue<>();
		costs = new HashMap<>();
		System.out.println("Starting the solver...");
		if (root == null)
			throw new IllegalArgumentException();
		solve(root);
		System.out.println("Solving is finished...");
	}

	private void solve(Puzzle root) {
		costs.put(root, 0);
		path.offer(new PriorityObject(root, 0, null));
		while (!path.isEmpty()){
			PriorityObject current = path.poll();
			if (current.board.isCompleted()){
				winner = current;
				break;
			}
			Iterable<Puzzle> adjacents = current.board.getAdjacents();
			for (Puzzle next : adjacents) {
				int cost = current.g+1;
				if (!costs.containsKey(next) || costs.get(next) < cost){
					path.offer(new PriorityObject(next, cost, current));
					costs.remove(next);
					costs.put(next, cost);
				}
			}
		}
	}

	public int getMoves() {
		return moves-1;
	}

	public Iterable<Puzzle> getSolution() {
		if (winner != null){
			Puzzle board = winner.board;
			winner = winner.prev;
			moves++;
			LinkedList<Puzzle> solution = (LinkedList<Puzzle>) getSolution();
			solution.add(board);
			return solution;
		}
		return new LinkedList<>();
	}

	private class PriorityObject implements Comparable<PriorityObject>{
		private final Puzzle board;
		private final int f;
		private final int g;
		private final PriorityObject prev;

		public PriorityObject(Puzzle board, int g, PriorityObject prev) {
			this.board = board;
			this.g = g;
			this.prev = prev;
			this.f = g + board.h();
		}

		@Override
		public int compareTo(PriorityObject o) {
			return f - o.f;
		}
	}

	// test client
	public static void main(String[] args) throws IOException {
		String path = "input\\sample\\";
//		String path = "";
		File input = new File( path + "input3.txt");
		Scanner scan = new Scanner(input);
		int size = Integer.parseInt(scan.nextLine());
		int[][] tiles = new int[size][size];
		for (int i = 0; i < size; i++) {
		    String[] line =	scan.nextLine().split(" ");
			for (int j = 0; j < size; j++) {
				tiles[i][j] = Integer.parseInt(line[j]);
			}
		}

		Puzzle initial = new Puzzle(tiles);
		// solve the puzzle here. Note that the constructor of the Solver class already calls the 
		// solve method. So just create a solver object with the Puzzle Object you created above 
		// is given as argument, as follows:
		Solver solver = new Solver(initial);  // where initial is the Puzzle object created from input file

		File output = new File("output.txt");
		output.createNewFile();
		PrintStream write = new PrintStream(output);
		Iterable<Puzzle> solution = solver.getSolution();
		write.println("Minimum number of moves = " + solver.getMoves());
		for (Puzzle board : solution)
			write.println(board);
	}
}
