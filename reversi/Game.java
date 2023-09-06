import java.util.ArrayList;
import java.util.Scanner;

public class Game {

	//color the player is playing, size of the board, search algorithm the ai uses, search limit if playing against heuristic search
	public static int p, size, game, depth;

	public static void doTurn(int game, Board state, ArrayList<int[]> actions) {	//picks which search algorithm to use for ai
		if (game == 1) {
			System.out.println("Picking a move Randomly...");
			state.Result(actions.get((int) (Math.random() * actions.size())));
		} else if (game == 2) {
			System.out.println("Picking a move using miniMax search...");
			state.Result(minimaxSearch(state));
		} else if (game == 3) {
			System.out.println("Picking a move using miniMax search with alpha-beta pruning...");
			state.Result(alphabetaSearch(state));
		} else {
			System.out.println("Picking a move using Heuristic miniMax search with alpha-beta pruning...");
			state.Result(HeuristicabSearch(state));
		}
	}

	public static void playerTurn(int player, Board state, ArrayList<int[]> actions) {	//uses while loop until user picks a legal move
		Scanner sc = new Scanner(System.in);
		boolean bool = false;
		int[] move = new int[2];
		while (!bool) {
			System.out.println("Your move: ");
			String input = sc.nextLine();
			if (input.length() == 2) {
				move[1] = (int) input.charAt(0) - 97;
				move[0] = (int) input.charAt(1) - 49;
				for (int[] m : actions) {
					if (move[0] == m[0] && move[1] == m[1]) {
						bool = true;
					}
				}
			}
			if (!bool) {
				System.out.println("Illegal move");
			}
		}
		state.Result(move);
	}

	public static void initGame() {	//initializes game. (board size, opponent, player color, depth cutoff)
		Scanner sc = new Scanner(System.in);
		boolean bbool = false;
		System.out.println(
				"Choose your game:\r\n" + "1. Small 4x4 Reversi\r\n" + "2. Standard 8x8 Reversi\r\n" + "Your choice? ");
		while (!bbool) {
			String binp = sc.nextLine();
			if (binp.equals("1")) {
				size = 4;
				bbool = true;
			} else if (binp.equals("2")) {
				size = 8;
				bbool = true;
			} else {
				System.out.println("Invalid input");
			}
		}
		System.out.println("Choose your opponent:\r\n" + "1. An agent that plays randomly\r\n"
				+ "2. An agent that uses MINIMAX\r\n" + "3. An agent that uses MINIMAX with alpha-beta pruning\r\n"
				+ "4. An agent that uses H-MINIMAX with a fixed depth cutoff and a-b pruning\r\n" + "Your choice? ");
		boolean obool = false;
		while (!obool) {
			String oinp = sc.nextLine();
			if (oinp.equals("1") || oinp.equals("2") || oinp.equals("3") || oinp.equals("4")) {
				game = Integer.parseInt(oinp);
				obool = true;
			} else {
				System.out.println("Invalid input");
			}
		}
		if (game == 4) {
			System.out.println("Please specify search limit for heuristic search (depth cutoff): ");
			boolean dbool = false;
			while (!dbool) {
				String dinp = sc.nextLine();
				int d = Integer.parseInt(dinp);
				if (d > 1 && d <= 10) {
					depth = Integer.parseInt(dinp);
					dbool = true;
				} else {
					System.out.println("Invalid input. Enter a number from 2-10");
				}
			}
		}
		System.out.println("Do you want to play DARK (X) or LIGHT (O)? ");
		boolean pbool = false;
		while (!pbool) {
			String pinp = sc.nextLine();
			if (pinp.toLowerCase().equals("x")) {
				p = 1;
				pbool = true;
			} else if (pinp.toLowerCase().equals("o")) {
				p = 0;
				pbool = true;
			} else {
				System.out.println("Invalid input");
			}
		}
	}

	public static Board copyBoard(Board board) {	//creates a duplicate state of a state
		Board newBoard = new Board(board.getSize());
		newBoard.turn = board.turn;
		for (int i = 0; i < newBoard.size; i++) {
			for (int j = 0; j < newBoard.size; j++) {
				newBoard.board[i][j] = board.board[i][j];
				if (newBoard.board[i][j] == 'X') {
					int[] tile = { i, j };
					newBoard.bpieces.add(tile);
				} else if (newBoard.board[i][j] == 'O') {
					int[] tile = { i, j };
					newBoard.wpieces.add(tile);
				}
			}
		}
		return newBoard;
	}

	public static int eval(Board board) {	//evaluation function used in the Heuristic miniMax Search. Same as utility. More friendly pieces is good
		return board.utility(board.toMove());
	}

	public static boolean isCutoff(Board board, int d) {	//cutoff function used in Heuristic search. If the state is terminal or the specified depth is reached, returns true
		return board.isTerminal() || d == depth;
	}

	public static int[] HeuristicabSearch(Board board) {	//Heuristic miniMax Search with alpha-beta pruning. Taken from the AIMA textbook
		int depth = 0;
		Board newBoard = copyBoard(board);
		int[] temp = maxHabValue(newBoard, (int) Double.NEGATIVE_INFINITY, (int) Double.POSITIVE_INFINITY, depth + 1);
		int[] move = { temp[0], temp[1] };
		return move;
	}

	public static int[] maxHabValue(Board board, int alpha, int beta, int depth) {
		int[] temp = { -10, -10, -10 };
		if (isCutoff(board, depth)) {
			temp[2] = eval(board);
			return temp;
		}
		int v = (int) Double.NEGATIVE_INFINITY;
		for (int[] a : board.Actions()) {
			Board newBoard = copyBoard(board);
			newBoard.Result(a);
			int[] t = minHabValue(newBoard, alpha, beta, depth + 1);
			if (t[2] >= v) {
				v = t[2];
				temp[0] = a[0];
				temp[1] = a[1];
				alpha = Math.max(alpha, v);
			}
			if (v >= beta) {
				return temp;
			}
		}
		return temp;
	}

	public static int[] minHabValue(Board board, int alpha, int beta, int depth) {
		int[] temp = { -10, -10, -10 };
		if (isCutoff(board, depth)) {
			temp[2] = eval(board);//
			return temp;
		}
		int v = (int) Double.POSITIVE_INFINITY;
		for (int[] a : board.Actions()) {
			Board newBoard = copyBoard(board);
			newBoard.Result(a);
			int[] t = maxHabValue(newBoard, alpha, beta, depth + 1);
			if (t[2] >= v) {
				v = t[2];
				temp[0] = a[0];
				temp[1] = a[1];
				beta = Math.min(beta, v);
			}
			if (v <= alpha) {
				return temp;
			}
		}
		return temp;
	}

	public static int[] alphabetaSearch(Board board) {	//miniMax search with alpha-beta pruning. Taken from AIMA textbook
		Board newBoard = copyBoard(board);
		int[] temp = maxabValue(newBoard, (int) Double.NEGATIVE_INFINITY, (int) Double.POSITIVE_INFINITY);
		int[] move = { temp[0], temp[1] };
		return move;
	}

	public static int[] maxabValue(Board board, int alpha, int beta) {
		int p = board.toMove();
		int[] temp = { -10, -10, -10 };
		if (board.isTerminal()) {
			temp[2] = board.utility(p);//
			return temp;
		}
		int v = (int) Double.NEGATIVE_INFINITY;
		for (int[] a : board.Actions()) {
			Board newBoard = copyBoard(board);
			newBoard.Result(a);
			int[] t = minabValue(newBoard, alpha, beta);
			if (t[2] >= v) {
				v = t[2];
				temp[0] = a[0];
				temp[1] = a[1];
				alpha = Math.max(alpha, v);
			}
			if (v >= beta) {
				return temp;
			}
		}
		return temp;
	}

	public static int[] minabValue(Board board, int alpha, int beta) {
		int p = board.toMove();
		int[] temp = { -10, -10, -10 };
		if (board.isTerminal()) {
			temp[2] = board.utility(p);//
			return temp;
		}
		int v = (int) Double.POSITIVE_INFINITY;
		for (int[] a : board.Actions()) {
			Board newBoard = copyBoard(board);
			newBoard.Result(a);
			int[] t = maxabValue(newBoard, alpha, beta);
			if (t[2] >= v) {
				v = t[2];
				temp[0] = a[0];
				temp[1] = a[1];
				beta = Math.min(beta, v);
			}
			if (v <= alpha) {
				return temp;
			}
		}
		return temp;
	}

	public static int[] minimaxSearch(Board board) {	//MiniMax search algorithm. Taken from AIMA textbook
		Board newBoard = copyBoard(board);
		int[] temp = maxValue(newBoard);
		int[] move = { temp[0], temp[1] };
		return move;
	}

	public static int[] maxValue(Board board) {
		int p = board.toMove();
		int[] temp = { -10, -10, -10 };
		if (board.isTerminal()) {
			temp[2] = board.utility(p);//
			return temp;
		}
		int v = (int) Double.NEGATIVE_INFINITY;
		for (int[] a : board.Actions()) {
			Board newBoard = copyBoard(board);
			newBoard.Result(a);
			int[] t = minValue(newBoard);
			if (t[2] >= v) {
				v = t[2];
				temp[0] = a[0];
				temp[1] = a[1];
			}
		}
		return temp;
	}

	public static int[] minValue(Board board) {
		int p = board.toMove();
		int[] temp = { -10, -10, -10 };
		if (board.isTerminal()) {
			temp[2] = board.utility(p);//
			return temp;
		}
		int v = (int) Double.POSITIVE_INFINITY;//
		for (int[] a : board.Actions()) {//
			Board newBoard = copyBoard(board);
			newBoard.Result(a);
			int[] t = maxValue(newBoard);
			if (t[2] <= v) {// fix
				v = t[2];
				temp[0] = a[0];
				temp[1] = a[1];
			}
		}
		return temp;
	}

	public static void endMsg(Board board) {	//End message printed when a terminal state is reached. Prints out who won
		if (board.bpieces.size() > board.wpieces.size()) {
			System.out.println("Black Wins!");
		} else if (board.bpieces.size() < board.wpieces.size()) {
			System.out.println("White Wins!");
		} else {
			System.out.println("Tie!");
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		initGame();	//initialize game parameters
		Scanner sc = new Scanner(System.in);
		ArrayList<int[]> actions;	//stores applicable actions
		Board state = new Board(size);
		state.initBoard();	//initial state
		state.print();
		while (true) {	//while loop for playing the game
			actions = state.Actions();
			if (actions.size() != 0) {	//if no legal moves, skips turn and switches whose turn it is
				if (p == 1) {
					playerTurn(p, state, actions);
				} else {
					doTurn(game, state, actions);
				}
			} else {
				System.out.println("No legal Moves!");
				state.turn = 0;
			}
			state.print();
			System.out.println();
			actions = state.Actions();
			if (state.isTerminal()) {	//break out of loop if state is terminal
				endMsg(state);
				break;
			}
			if (actions.size() != 0) {
				if (p == 0) {
					playerTurn(p, state, actions);
				} else {
					doTurn(game, state, actions);
				}
			} else {
				System.out.println("No legal Moves!");
				state.turn = 1;
			}
			state.print();
			if (state.isTerminal()) {//break out of loop if state is terminal
				endMsg(state);
				break;
			}
		}
		sc.close();
	}
}
