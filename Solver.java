import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private class SearchNode implements Comparable<SearchNode> {

        Board board; // board
        int moves; // # of moves
        SearchNode previous; // previous board

        // compareTo compares the priority of each search node; priority = manhattan + moves
        public int compareTo(SearchNode other) {
            int thisManhattan = this.board.manhattan();
            int thisPriority = this.moves + thisManhattan;

            int otherManhattan = other.board.manhattan();
            int otherPriority = other.moves + otherManhattan;

            if (thisPriority > otherPriority) return 1;

            if (thisPriority < otherPriority) return -1;

            return 0;
        }

        // constructor for search node class
        private SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
        }
    }


    private Stack<Board> solutions = new Stack<Board>(); // stack to store path of solutions


    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("input is null"); // check that input exists
        if (!initial.isSolvable()) throw new IllegalArgumentException("unsolvable puzzle"); // and if unsolvable

        MinPQ<SearchNode> PQ = new MinPQ<>(); // initialize priority queue

        PQ.insert(new SearchNode(initial, 0, null)); // insert initial's search node into priority queue

        while (!PQ.min().board.isGoal()) { // iterate until board is solved
            SearchNode current = PQ.delMin(); // retrieve the optimal board from the priority queue

            for (Board board : current.board.neighbors()) { // iterate through that board's neighbors
                if (current.previous == null || !board.equals(current.previous.board))
                    PQ.insert(new SearchNode(board, current.moves + 1, current)); // add to priority queue
                
            }
        }

        SearchNode solutionPath = PQ.min(); // get the optimal board from the priority queue
        while (solutionPath.previous != null) { // while it has a preceding board
            solutions.push(solutionPath.board); // push onto stack of solutions
            solutionPath = solutionPath.previous; // and move to the next most previous board
        }
        solutions.push(solutionPath.board); // push the initial board
    }


    // return the min number of moves to solve initial board
    public int moves() {
        int solution = solutions.size() - 1; // size of stack denotes the number
        // of moves necessary to reach solution
        if (solution == -1) return 0; // since solutions.size() - 1 will equal -1 in the case of no moves(the board
            // is already the goal board, return 0
        else return solution;
    }

    // return a sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return solutions;
    }

    // test client (see below)
    public static void main(String[] args) {
        int[][] tiles = new int[3][3];
        tiles[0][0] = 1;
        tiles[0][1] = 4;
        tiles[0][2] = 3;
        tiles[1][0] = 2;
        tiles[1][1] = 5;
        tiles[1][2] = 6;
        tiles[2][0] = 8;
        tiles[2][1] = 7;
        tiles[2][2] = 0;

        Board init = new Board(tiles);

        // Test Solver :
        Solver solvedBoard = new Solver(init);
        // StdOut.println(solvedBoard);


        // Test moves :
        int testMoves = solvedBoard.moves();
        StdOut.println(testMoves);

        // Test solution :
        Iterable<Board> solved = solvedBoard.solution();
        for (Board board : solved) {
            StdOut.println(board);
        }
    }
}
