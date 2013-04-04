package com.nicolo.tictactoe;

import java.util.ArrayList;
import java.util.List;

public class AI {
	private int ai;
	private int human;
	private int difficulty;
	private boolean myTurn;
	
	public AI(int ai, int human, int difficulty, boolean myTurn){
		this.ai = ai;
		this.human = human;
		this.difficulty = difficulty;
		this.myTurn = myTurn;
	}
	
	public boolean getTurn(){
		return myTurn;
	}
	
	public int getLetter(){
		return ai;
	}
	public void setTurn(boolean myTurn){
		this.myTurn = myTurn;
	}
	
	// Get next best move for computer. Return int[2] of {row, col} 
   public int[] play(GameBoard board) {
	   // TODO: create preference for depth, ie: difficulty
      int[] result = minimax(difficulty, ai, board); // depth, max turn
      return result;
      //return xyToPosition(result[1], result[2]);   // row, col
   }
   
   // Convert a cell defined by xy coordinates to a position for the grid view
  /* private int xyToPosition(int x, int y){
	   if (x==0){
		   if(y == 0)
			   return 0;
		   else if (y == 1)
			   return 1;
		   else 
			   return 2;
	   }else if (x == 1){
		   if(y == 0)
			   return 3;
		   else if (y == 1)
			   return 4;
		   else 
			   return 5;
	   }else{
		   if(y == 0)
			   return 6;
		   else if (y == 1)
			   return 7;
		   else 
			   return 8;
	   }
   }*/
	
	private int[] minimax (int depth, int player, GameBoard board){
		
		//Generate possible moves
		List<int[]> possibleMoves = generateMoves(board);
		int [][] cells = board.getBoard();
		
		int bestScore;
		int currentScore;
		
		if(player == ai)
			bestScore = Integer.MIN_VALUE;
		else 
			bestScore = Integer.MAX_VALUE;
		
		int bestRow = -1;
		int bestCol = -1;
		
		if(possibleMoves.isEmpty() || depth == 0){
			// Gameover or depth reached
			bestScore = evaluate(board);
		}else{
			for (int[] move : possibleMoves){
				// Try for current player
				cells[move[0]][move[1]] = player;
				if(player == ai){	// maximize for ai
					currentScore = minimax(depth-1,human, board)[0];
					if (currentScore > bestScore){
						bestScore = currentScore;
						bestRow = move[0]; 
						bestCol = move [1];
					}
				}else{	// minimize for human
					currentScore = minimax(depth-1,ai, board)[0];
					if (currentScore < bestScore){
						bestScore = currentScore;
						bestRow = move[0]; 
						bestCol = move [1];
					}
				}
				cells[move[0]][move[1]] = board.BLANK; // Undo move
			}
			
		}
		
		return new int[] {bestScore, bestRow, bestCol};
	}

	// Generate list of possible moves
	private List<int[]> generateMoves(GameBoard board){
		List<int[]> possibleMoves = new ArrayList<int[]>();
		for(int row = 0; row < board.getSize(); row++){
			for(int col = 0; col < board.getSize(); col++){
				if(board.getBoard()[row][col] == board.BLANK)
					possibleMoves.add(new int[]{row, col});
			}
		}
		return possibleMoves;
	}
	
	private int evaluate(GameBoard board){
		 int score = 0;
	      // Evaluate score for each of the 8 lines (3 rows, 3 columns, 2 diagonals)
	      score += evaluateLine(0, 0, 0, 1, 0, 2, board);  // row 0
	      score += evaluateLine(1, 0, 1, 1, 1, 2, board);  // row 1
	      score += evaluateLine(2, 0, 2, 1, 2, 2, board);  // row 2
	      score += evaluateLine(0, 0, 1, 0, 2, 0, board);  // col 0
	      score += evaluateLine(0, 1, 1, 1, 2, 1, board);  // col 1
	      score += evaluateLine(0, 2, 1, 2, 2, 2, board);  // col 2
	      score += evaluateLine(0, 0, 1, 1, 2, 2, board);  // diagonal
	      score += evaluateLine(0, 2, 1, 1, 2, 0, board);  // alternate diagonal
	      return score;
	}
	
	private int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3, GameBoard board){
		int score = 0;
		int [][] cells = board.getBoard();
		// First Cell
		if (cells[row1][col1] == ai){
			score = 1;
		} else if (cells[row1][col1] == human){
			score = -1;
		}
		
		// Second cell
		if (cells[row2][col2] == ai){
			if(score == 1){ // ai cell
				score = 10;
			} else if (score == -1){ // human cell
				return 0;
			} else{	// cell is empty
				score = 1;
			}
		}else if (cells[row2][col2] == human){
			if (score == -1){ // human cell
				score = -10;
			} else if (score == 1){ // ai cell
				return 0;
			}else{	// empty cell
				score = -1;
			}
		}
		
		// Third Cell
		if (cells[row3][col3] == ai){
			if(score > 0){ // cell 1 and 2 are ai
				score *= 10;
			} else if (score < 0){ // cell 1 and 2 are human
				return 0;
			} else{	// cell 1 and 2 are empty
				score = 1;
			}
		}else if (cells[row3][col3] == human){
			if(score < 0){ // cell 1 and 2 are human
				score *= 10;
			} else if (score > 0){ // cell 1 and 2 are ai
				return 0;
			} else{	// cell 1 and 2 are empty
				score = -1;
			}
		}
		
		return score;
	}
}
