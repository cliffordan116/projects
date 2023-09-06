import java.util.ArrayList;

public class Board {

	public int size, turn;	//stores size and whose turn it is. 1 if black's turn
	public char[][] board;
	public ArrayList<int[]> wpieces; // stores white and black pieces as x and y coordinates
	public ArrayList<int[]> bpieces;

	public boolean contains(ArrayList<int[]> list, int[] a) { // returns true if an array list of int arrays contains an
																// array with the same contents as array a
		for (int[] array : list) {
			if (array[0] == a[0] && array[1] == a[1]) {
				return true;
			}
		}
		return false;
	}

	public Board(int size) {	//constructor for board. This is essentially the states in the formal definition of a problem
		this.turn = 1;
		this.size = size;
		this.board = new char[size][size];
		this.wpieces = new ArrayList<>();
		this.bpieces = new ArrayList<>();
	}

	public void print() {	//prints board contents to console
		System.out.print("  ");
		for (int i = 0; i < size; i++) {
			System.out.print(Character.toString(97 + i) + " ");
		}
		System.out.println();
		for (int i = 0; i < size; i++) {
			System.out.print(i + 1 + " ");
			for (int j = 0; j < size; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.print(i + 1);
			System.out.println();
		}
		System.out.print("  ");
		for (int i = 0; i < size; i++) {
			System.out.print(Character.toString(97 + i) + " ");
		}
		System.out.println();
	}

	public void initBoard() {	//initializes pieces. This is the initial state in the formal definition
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				this.board[i][j] = ' ';
			}
		}
		this.board[size / 2][size / 2] = 'O'; // finds the middle of the board by dividing size by 2
		this.board[size / 2 - 1][size / 2 - 1] = 'O';
		this.board[size / 2 - 1][size / 2] = 'X';
		this.board[size / 2][size / 2 - 1] = 'X';
		int[] p1 = { size / 2, size / 2 };
		int[] p2 = { size / 2 - 1, size / 2 - 1 };
		int[] p3 = { size / 2, size / 2 - 1 };
		int[] p4 = { size / 2 - 1, size / 2 };
		this.wpieces.add(p1);
		this.wpieces.add(p2);
		this.bpieces.add(p3);
		this.bpieces.add(p4);
	}

	public boolean isTerminal() {	//returns true if a given state is terminal. That is, neither players have legal moves
		if (this.Actions().size() == 0) {
			if (this.turn == 1) {
				this.turn = 0;
				if (this.Actions().size() == 0) {
					return true;
				}
				this.turn = 1;
			} else {
				this.turn = 1;
				if (this.Actions().size() == 0) {
					return true;
				}
				this.turn = 0;
			}
		}
		return false;
	}

	public int toMove() {	//returns whose turn it is given a state
		return this.turn;
	}

	public int utility(int color) {	//returns a player's utility given a state. In reversi this is just based on who has more pieces
		int black = this.bpieces.size();
		int white = this.wpieces.size();
		if (black > white) {
			if (color == 1) {
				return 1;
			}
			return -1;
		} else if (white > black) {
			if (color == 1) {
				return -1;
			}
			return 1;
		}
		return 0;
	}

	public ArrayList<int[]> Actions() { // function that finds applicable actions given a state
		ArrayList<int[]> moves = new ArrayList<>();
		if (turn == 1) { // if it is dark's turn
			for (int[] bps : bpieces) {
				for (int k = 0; k < 8; k++) {	//checks all 8 possible directions
					int i = bps[0];
					int j = bps[1];
					if (k == 0) {
						i++;
					} else if (k == 1) {
						i--;
					} else if (k == 2) {
						j++;
					} else if (k == 3) {
						j--;
					} else if (k == 4) {
						i++;
						j++;
					} else if (k == 5) {
						i--;
						j++;
					} else if (k == 6) {
						i++;
						j--;
					} else {
						i--;
						j--;
					}
					while (i >= 0 && j >= 0 && i < size && j < size && board[i][j] == 'O') {	//needs to see a line of enemy pieces, followed by an empty tile
						if (k == 0) {
							i++;
						} else if (k == 1) {
							i--;
						} else if (k == 2) {
							j++;
						} else if (k == 3) {
							j--;
						} else if (k == 4) {
							i++;
							j++;
						} else if (k == 5) {
							i--;
							j++;
						} else if (k == 6) {
							i++;
							j--;
						} else {
							i--;
							j--;
						}
						if (i >= 0 && j >= 0 && i < size && j < size && board[i][j] == ' ') {
							int[] move = { i, j };
							if (!contains(moves, move)) {	//checks if legal moves array already has the move, to avoid duplicates
								moves.add(move);
							}
						}
					}

				}
			}
		} else {
			for (int[] bps : wpieces) {	//same thing for white
				for (int k = 0; k < 8; k++) {
					int i = bps[0];
					int j = bps[1];
					if (k == 0) {
						i++;
					} else if (k == 1) {
						i--;
					} else if (k == 2) {
						j++;
					} else if (k == 3) {
						j--;
					} else if (k == 4) {
						i++;
						j++;
					} else if (k == 5) {
						i--;
						j++;
					} else if (k == 6) {
						i++;
						j--;
					} else {
						i--;
						j--;
					}
					while (i >= 0 && j >= 0 && i < size && j < size && board[i][j] == 'X') {
						if (k == 0) {
							i++;
						} else if (k == 1) {
							i--;
						} else if (k == 2) {
							j++;
						} else if (k == 3) {
							j--;
						} else if (k == 4) {
							i++;
							j++;
						} else if (k == 5) {
							i--;
							j++;
						} else if (k == 6) {
							i++;
							j--;
						} else {
							i--;
							j--;
						}
						if (i >= 0 && j >= 0 && i < size && j < size && board[i][j] == ' ') {
							int[] move = { i, j };
							if (!contains(moves, move)) {
								moves.add(move);
							}
						}
					}

				}
			}
		}
		return moves;
	}

	public void Result(int[] move) { // changes state given an action. This is the transition model in the formal definition
		ArrayList<int[]> take = new ArrayList<int[]>();	//store tiles to be taken by white or black
		if (this.turn == 1) {	//if black's turn
			for (int k = 0; k < 8; k++) {	//checks all 8 possible directions
				ArrayList<int[]> temp = new ArrayList<int[]>();
				int i = move[0];
				int j = move[1];
				if (k == 0) {
					i++;
				} else if (k == 1) {
					i--;
				} else if (k == 2) {
					j++;
				} else if (k == 3) {
					j--;
				} else if (k == 4) {
					i++;
					j++;
				} else if (k == 5) {
					i--;
					j++;
				} else if (k == 6) {
					i++;
					j--;
				} else {
					i--;
					j--;
				}
				while (i >= 0 && j >= 0 && i < size && j < size && board[i][j] == 'O') {	//needs to see a line of enemy pieces, followed by a friendly piece
					int[] tile = { i, j };
					temp.add(tile);
					if (k == 0) {
						i++;
					} else if (k == 1) {
						i--;
					} else if (k == 2) {
						j++;
					} else if (k == 3) {
						j--;
					} else if (k == 4) {
						i++;
						j++;
					} else if (k == 5) {
						i--;
						j++;
					} else if (k == 6) {
						i++;
						j--;
					} else {
						i--;
						j--;
					}
				}
				if (i >= 0 && j >= 0 && i < size && j < size && board[i][j] == 'X') {
					for (int[] t : temp) {
						take.add(t);
					}
				}
			}
			for (int[] t : take) {
				int[] tile = { t[0], t[1] };
				board[t[0]][t[1]] = 'X';
				remove(wpieces, tile);
				bpieces.add(tile);
			}
			bpieces.add(move);
			board[move[0]][move[1]] = 'X';
			this.turn = 0;
		} else {	//same for white
			for (int k = 0; k < 8; k++) {
				ArrayList<int[]> temp = new ArrayList<int[]>();
				int i = move[0];
				int j = move[1];
				if (k == 0) {
					i++;
				} else if (k == 1) {
					i--;
				} else if (k == 2) {
					j++;
				} else if (k == 3) {
					j--;
				} else if (k == 4) {
					i++;
					j++;
				} else if (k == 5) {
					i--;
					j++;
				} else if (k == 6) {
					i++;
					j--;
				} else {
					i--;
					j--;
				}
				while (i >= 0 && j >= 0 && i < size && j < size && board[i][j] == 'X') {
					int[] tile = { i, j };
					temp.add(tile);
					if (k == 0) {
						i++;
					} else if (k == 1) {
						i--;
					} else if (k == 2) {
						j++;
					} else if (k == 3) {
						j--;
					} else if (k == 4) {
						i++;
						j++;
					} else if (k == 5) {
						i--;
						j++;
					} else if (k == 6) {
						i++;
						j--;
					} else {
						i--;
						j--;
					}
				}
				if (i >= 0 && j >= 0 && i < size && j < size && board[i][j] == 'O') {
					for (int[] t : temp) {
						take.add(t);
					}
				}
			}
			for (int[] t : take) {	//takes line, except for initial move tile
				int[] tile = { t[0], t[1] };
				board[t[0]][t[1]] = 'O';
				remove(bpieces, tile);
				wpieces.add(tile);
			}
			wpieces.add(move);	//takes move tile
			board[move[0]][move[1]] = 'O';
			this.turn = 1;
		}
	}
	
	public void remove(ArrayList<int[]> list, int[] array) {	//given an int array of size two, removes array from the arraylist
		for (int i = 0; i < list.size(); i++) {
			if (array[0] == list.get(i)[0] && array[1] == list.get(i)[1]) {
				list.remove(i);
				break;
			}
		}
	}

	public int getSize() {	//gets size of board
		return this.size;
	}
	
}


