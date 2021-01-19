import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Board {

    private int[][] board; // 2D array to represent board
    private int hamming; // hamming distance instance variable
    private int manhattan; // manhattan distance instance variable
    private int size; // stores size of the board
    private int zeroCol; // stores the index of the column containing 0
    private int zeroRow; // stores the index of the row containing 0

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        size = tiles[0].length; // get board's height
        board = new int[size][size]; // create 2D list of size x size to be the board

        int location = 0; // this will track the correct position of each tile

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                location++; // increment such that in a goal board, location = 1 and tiles[0][0] = 1; pattern
                // continues until location = size^2

                if (tiles[i][j] != location && tiles[i][j] != 0) { // if the tile is out of place and not 0
                    hamming += 1; // increment hamming distance by 1

                    int r = (tiles[i][j] - 1) / size; // get the index of the correct row
                    int c = (tiles[i][j] - 1) % size; // get the index of the correct col
                    manhattan += Math.abs(r - i) + Math.abs(c - j); // compute manhattan distance
                }

                if (tiles[i][j] == 0) { // if the tile is equal to 0, store its indexes
                    zeroRow = i;
                    zeroCol = j;
                }

                board[i][j] = tiles[i][j]; // after all these checks are complete, set board at indexes
                // i and j equal to the value at the same indexes in the initial tiles
            }
        }
    }


    // string representation of this board
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(size + "\n");
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++)
                str.append(String.format("%2d ", tileAt(row, col)));
            str.append("\n");
        }

        return str.toString();
    }


    // tile at (row, col) or 0 if blank
    public int tileAt(int row, int col) {
        if (row < 0 || col < 0 || row >= size || col >= size) { // check if the row and col are in bounds
            throw new IllegalArgumentException();
        }
        return board[row][col]; // return the board value at row and col
    }


    // return board size n
    public int size() {
        return size;
    }


    // return number of tiles out of place
    public int hamming() {
        return hamming;
    }


    // return sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }


    // is this board the goal board?
    public boolean isGoal() {
        if (hamming == 0) { // if no tiles are out of place, then it's the goal board
            return true;
        } else {
            return false;
        }
    }


    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true; // if the two are identical, return true
        if (y == null) return false; // check if null
        if (y.getClass() != this.getClass()) return false; // and check if they're of the same type

        Board that = (Board) y; // create a board out of y
        if (that.size() != this.size()) return false; // check for equal dimensions

        else {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (that.tileAt(i, j) != this.tileAt(i, j)) { // if any tiles are not equal at the same index
                        return false;
                    }
                }
            }
        }

        return true; // if all checks are passed return true
    }


    // swap elements in 2D array
    private void swap(int[][] arr, int row0, int col0, int r, int c) {
        int temp = arr[row0][col0];
        arr[row0][col0] = arr[r][c];
        arr[r][c] = temp;
    }


    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> stack = new Stack<Board>();
        // check for each valid neighbor
        // create deep copy of board
        // swap 0 with neighboring element
        // push new board onto stack
        // return the stack : contains 2, 3, up to 4 valid boards
        int[][] blocks = board;

        // check if a valid neighbor exists
        if (zeroRow + 1 != size) {
            swap(blocks, zeroRow, zeroCol, zeroRow + 1, zeroCol); // if so, swap their elements
            Board neighbor = new Board(blocks); // create a new board with the swapped elements
            stack.push(neighbor); // push it onto stack of neighbors
            swap(blocks, zeroRow + 1, zeroCol, zeroRow, zeroCol); // and swap element back into place -> saves memory
        }

        if (zeroRow - 1 >= 0) {
            swap(blocks, zeroRow, zeroCol, zeroRow - 1, zeroCol);
            Board neighbor = new Board(blocks);
            stack.push(neighbor);
            swap(blocks, zeroRow - 1, zeroCol, zeroRow, zeroCol);
        }

        if (zeroCol + 1 != size) {
            swap(blocks, zeroRow, zeroCol, zeroRow, zeroCol + 1);
            Board neighbor = new Board(blocks);
            stack.push(neighbor);
            swap(blocks, zeroRow, zeroCol + 1, zeroRow, zeroCol);
        }

        if (zeroCol - 1 >= 0) {
            swap(blocks, zeroRow, zeroCol, zeroRow, zeroCol - 1);
            Board neighbor = new Board(blocks);
            stack.push(neighbor);
            swap(blocks, zeroRow, zeroCol - 1, zeroRow, zeroCol);
        }


        return stack; // return stack of all neighbors of input board
    }

    // find total inversions in array
    private int sortInversions(int[] arr) {
        int N = arr.length;
        int inversions = 0;

        for (int i = 0; i < N - 1; i++) {
            for (int j = i + 1; j < N; j++)
                if (arr[j] < arr[i]) { // if an elements is out of place
                    inversions++; // increment the total number of inversions
                }
        }
        return inversions;
    }


    // is this board solvable?
    public boolean isSolvable() {
        int[] solvable = new int[(size * size) - 1];

        int k = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] != 0) {
                    solvable[k] = board[i][j]; // cast the board into 1D array
                    k++;
                }
            }
        }

        int inversions = sortInversions(solvable); // retrieve the total number of inversions in the board
        if (size % 2 == 1) { // if the board is of odd height and width
            return inversions % 2 == 0; // return true if there's an even number of inversions(solvable), else false
        }
        return (inversions + zeroRow) % 2 == 1; // return true if there's an odd number
        // of inversions + the row of zero(solvable), else false

    }

    // unit testing (required)
    public static void main(String[] args) {
        int[][] tiles = new int[2][2];
        tiles[0][0] = 1;
        tiles[0][1] = 0;
        tiles[1][0] = 3;
        tiles[1][1] = 2;
        /*
        tiles[1][1] = 5;
        tiles[1][2] = 6;
        tiles[2][0] = 8;
        tiles[2][1] = 7;
        tiles[2][2] = 0;
        */

        // Test  Constructor : PASSED
        Board testBoard = new Board(tiles);

        // Test toString : PASSED
        StdOut.println(testBoard.toString());
        /*
        // Test tileAt : PASSED
        // StdOut.println(testBoard.tileAt(1, 1));
        // StdOut.println(testBoard.tileAt(-1, 1));

        // Test size : PASSED
        // StdOut.println(testBoard.size());

        // Test Hamming : PASSED
        // StdOut.println(testBoard.hamming());

        // Test Manhattan : PASSED
        // StdOut.println(testBoard.manhattan());

        // Test isGoal : PASSED
        // StdOut.println(testBoard.isGoal());

        // Test equals : PASSED
        // StdOut.println(testBoard.equals(testBoard));

        // Test neighbors : PASSED
        // StdOut.println(testBoard.neighbors());
        */
        // Test isSolvable : 2 PASSED, 1 FAILED
        StdOut.println(testBoard.isSolvable());
    }
}
