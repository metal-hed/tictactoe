package com.nicolo.tictactoe;


public class GameBoard {
	private int [][] board = {{-1,-1,-1},{-1,-1,-1},{-1,-1,-1}};
	private int moveCount;
	private boolean active;
	
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
			default: return -99;
		}
		
		
	}
	
	private int makeMove(int x, int y, int player){
		
		if (!active)
			return -99;
		
		if(board[x][y] == -1){
			board[x][y] = player;
		}else{
			return -99;
		}
		moveCount++;
		
		//check end conditions
		
		//check columns
		for (int i = 0; i < 3; i++){
			if(board[x][i] != player)
				break;
			if(i == 2){
				// Report win for player
				active = false;
				return 69;
			}
		}
		
		//check rows
		for (int i = 0; i < 3; i++){
			if(board[i][y] != player)
				break;
			if(i == 2){
				// Report win for player
				active = false;
				return 69;
			}
		}
		
		// Check diagonal
		if (x == y){
			for (int i = 0; i < 3; i++){
				if (board[i][i] != player)
					break;
				if (i == 2){
					// Report win for player
					active = false;
					return 69;
				}
			}
		}
		
		for (int i = 0; i < 3; i++){
			if (board[i][2-i] != player)
				break;
			if (i == 2){
				// Report win for player
				active = false;
				return 69;
			}
		}
		
		if(moveCount == 9)
			return -2; // DRAW
		
		return 3; //Safe guard shouldn't reach
	}

}
