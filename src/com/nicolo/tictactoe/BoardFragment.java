package com.nicolo.tictactoe;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class BoardFragment extends Fragment implements OnClickListener{
	private GameBoard board;
	final private int X = 2;
	final private int O = 1;
	final private int DRAW = -2;
	final private int WINNER = 69;
	final private int INVALID = -99;

	final private int max = 2;
	final private int min = 1;

	private int player;
	private boolean aiEnabled;
	private AI ai;

	private GestureDetector gDetector;
	private View.OnTouchListener gListener;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	
	private View master;
	private Toast tShort;
	private Toast tLong;

	@SuppressLint("ShowToast")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Gesture detection
		gDetector = new GestureDetector(this.getActivity(), new MyGestureDetector());
		gListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gDetector.onTouchEvent(event);
			}
		};

		SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);

		Intent intent = getActivity().getIntent();

		aiEnabled = intent.getBooleanExtra(MainActivity.ENABLE_AI, false);

		if(aiEnabled){
			int aiLetter = sharedPref.getInt(getString(R.string.ai_selection_pref), getResources().getInteger(R.integer.ai_selection_pref_default));
			int difficulty = sharedPref.getInt(getString(R.string.ai_difficulty_pref), getResources().getInteger(R.integer.ai_difficulty_pref_default));
			int humanLetter = (aiLetter == O)? X : O;
			ai = new AI(aiLetter, humanLetter, difficulty);
		}

		
		master = inflater.inflate(R.layout.board_fragment, container, false);
		
		tShort = Toast.makeText( this.getActivity()  , "" , Toast.LENGTH_SHORT );
		tLong = Toast.makeText( this.getActivity()  , "" , Toast.LENGTH_LONG );
		
		startGame();
		
		return master;
	}
	
	@Override
	public void onPause() {
         super.onPause();
         
         if(tShort!=null)
           tShort.cancel();
         
         if(tLong != null)
        	 tLong.cancel();
	}
	
	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					 getFragmentManager()
			            .beginTransaction()
			            .setCustomAnimations(
			            		 	R.animator.card_flip_right_in, R.animator.card_flip_right_out)
				         .replace(R.id.board_fragment, new BoardFragment())
				         .commit();
					 startGame();
				}  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					// Create and commit a new fragment transaction that adds the fragment for the back of
				    // the card, uses custom animations, and is part of the fragment manager's back stack.

				    getFragmentManager()
				            .beginTransaction()

				            // Replace the default fragment animations with animator resources representing
				            // rotations when switching to the back of the card, as well as animator
				            // resources representing rotations when flipping back to the front (e.g. when
				            // the system Back button is pressed).
				            .setCustomAnimations(
				            		R.animator.card_flip_left_in, R.animator.card_flip_left_out)

				            // Replace any fragments currently in the container view with a fragment
				            // representing the next page (indicated by the just-incremented currentPage
				            // variable).
				            .replace(R.id.board_fragment, new BoardFragment())

				            // Commit the transaction.
				            .commit();
					startGame();
				}
			} catch (Exception e) {
				// nothing
			}
			return false;
		}

	}

	public void startGame(){
		board = new GameBoard();
		clearBoard();
		player = whoStart();

		if (player == X)
			tShort.setText(R.string.x_first);
		else
			tShort.setText(R.string.o_first);
			

		tShort.show();
		aiMove();
	}

	public void onClick(View v){
		int result = board.processMove(Integer.parseInt(v.getTag().toString()), player);

		if(result != INVALID){
			ImageButton imgView = (ImageButton) v;

			if (player == X)
				imgView.setImageResource(R.drawable.x);
			else
				imgView.setImageResource(R.drawable.o);


			if (result == DRAW){
				tLong.setText(R.string.draw_string);
				tLong.show();
				return;
			}
			if (result == WINNER){
				toastWinner(player);
				return;
			}

			switchPlayer();
		}
	}

	private void toastWinner(int player){
		if (player == X)
			tLong.setText(R.string.x_win);
		else
			tLong.setText(R.string.o_win);
		
		tLong.show();
	}

	private void switchPlayer(){
		if (player == X)
			player = O;
		else
			player = X;

		aiMove();

	}

	private void aiMove(){
		if(aiEnabled && (ai.getLetter() == player) && board.isActive()){
			int [] move = ai.play(board);

			TableLayout tl = (TableLayout)master.findViewById(R.id.board_table);
			TableRow tr = (TableRow)tl.getChildAt(move[1]);
			ImageButton ib = (ImageButton)tr.getChildAt(move[2]);
			ib.performClick();
		}
	}

	public void clearBoard(){
		TableLayout tl = (TableLayout)master.findViewById(R.id.board_table);
		int tlKids = tl.getChildCount();

		for(int i = 0; i < tlKids; i++){
			TableRow tr = (TableRow)tl.getChildAt(i);
			int trKids = tr.getChildCount();
			for(int j = 0; j < trKids; j++){
				ImageButton ib = (ImageButton)tr.getChildAt(j);
				ib.setImageResource(R.drawable.blank);
				ib.setOnTouchListener(gListener);
				ib.setOnClickListener(this);
			}
		}
	}

	private int whoStart(){
		return min + (int)(Math.random() * ((max - min) + 1));
	}

}
