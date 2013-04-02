package com.nicolo.tictactoe;


public class GameBoard {
	private int moveCount;
	private boolean active;
	final private int DRAW = -2;
	final private int WINNER = 69;
	final private int INVALID = -99;
	final public int BLANK = 0;
	final private int CONTINUE = 5;
	final private int SIZE = 3;
	
	private int [][] board = new int [SIZE][SIZE];
	
	public GameBoard(){
		moveCount = 0;
		active = true;
	}
	
	public int[][] getBoard(){
		return board;
	}
	
	public int processMove(int position, int player){
		switch (position){
			case 0: return makeMove(0, 0, player); 
			case 1: return makeMove(0, 1, player); 
			case 2: return makeMove(0, 2, player);
			case 3: return makeMove(1, 0, player); 
			case 4: return makeMove(1, 1, player); 
			case 5: return makeMove(1, 2, player); 
			case 6: return makeMove(2, 0, player); 
			case 7: return makeMove(2, 1, player); 
			case 8: return makeMove(2, 2, player); 
			default: return INVALID;
		}
		
		
	}
	
	private int makeMove(int x, int y, int player){
		
		if (!active)
			return INVALID;
		
		if(board[x][y] == BLANK){
			board[x][y] = player;
		}else{
			return INVALID;
		}
		moveCount++;
		
		//check end conditions
		
		//check columns
		for (int i = 0; i < 3; i++){
			if(board[x][i] != player)
				break;
			if(i == (SIZE-1)){
				// Report win for player
				active = false;
				return WINNER;
			}
		}
		
		//check rows
		for (int i = 0; i < 3; i++){
			if(board[i][y] != player)
				break;
			if(i == (SIZE-1)){
				// Report win for player
				active = false;
				return WINNER;
			}
		}
		
		// Check diagonal
		if (x == y){
			for (int i = 0; i < 3; i++){
				if (board[i][i] != player)
					break;
				if (i == (SIZE-1)){
					// Report win for player
					active = false;
					return WINNER;
				}
			}
		}
		
		for (int i = 0; i < 3; i++){
			if (board[i][(SIZE-1)-i] != player)
				break;
			if (i == (SIZE-1)){
				// Report win for player
				active = false;
				return WINNER;
			}
		}
		
		if(moveCount == (Math.pow(SIZE, 2)))
			return DRAW; // DRAW
		
		return CONTINUE;
	}
	public int getSize(){
		return SIZE;
	}
}
