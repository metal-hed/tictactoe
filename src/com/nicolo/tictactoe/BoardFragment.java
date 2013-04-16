package com.nicolo.tictactoe;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class BoardFragment extends Fragment implements OnClickListener {
	private GameBoard board;
	
	private final int X = 2;
	private final int O = 1;
	private final int DRAW = -2;
	private final int WINNER = 69;
	private final int INVALID = -99;
	private final int max = 2;
	private final int min = 1;
	private final int SWIPE_MIN_DISTANCE = 120;
	private final int SWIPE_MAX_OFF_PATH = 250;
	private final int SWIPE_THRESHOLD_VELOCITY = 200;
	private int player;
	
	private AI ai;

	private GestureDetector gDetector;
	private View.OnTouchListener gListener;
	
	private View master;
	private Toast tShort;
	private Toast tLong;
	
	private boolean aiEnabled;
	private static boolean firstGame = true;

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
	
	
	@Override
	public void onResume() {
		super.onResume();
		if(board==null)
			startGame();
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
				}
			} catch (Exception e) {
				// nothing
			}
			
			return true;
		}

	}
	
	public void animateGameStatus(int id){
		ImageView iv = (ImageView)master.findViewById(R.id.animation_view);
		iv.setAlpha(0f);
		switch (id){
			case R.drawable.new_game: iv.setImageResource(R.drawable.new_game); break;
			case R.drawable.victory: iv.setImageResource(R.drawable.victory); break;
			case R.drawable.draw: iv.setImageResource(R.drawable.draw); break;
			default:
		}
		
		
		ObjectAnimator fadeIn = ObjectAnimator.ofFloat(iv, "alpha", 0f,1f);
		fadeIn.setInterpolator(new OvershootInterpolator());
		fadeIn.setDuration(350);
		
		ObjectAnimator zoomInX = ObjectAnimator.ofFloat(iv,"scaleX",0.1f,3f);
		zoomInX.setInterpolator(new OvershootInterpolator());
		zoomInX.setDuration(350);
		
		ObjectAnimator zoomInY = ObjectAnimator.ofFloat(iv,"scaleY",0.1f,3f);
		zoomInY.setInterpolator(new OvershootInterpolator());
		zoomInY.setDuration(350);
		
		ObjectAnimator fadeOut = ObjectAnimator.ofFloat(iv, "alpha", 1f,0f);
		fadeOut.setInterpolator(new AccelerateInterpolator());
		fadeOut.setDuration(350);
		
		ObjectAnimator zoomOutX = ObjectAnimator.ofFloat(iv,"scaleX",3f,0.1f);
		zoomOutX.setInterpolator(new AccelerateInterpolator());
		zoomOutX.setDuration(350);
		
		ObjectAnimator zoomOutY = ObjectAnimator.ofFloat(iv,"scaleY",3f,0.1f);
		zoomOutY.setInterpolator(new AccelerateInterpolator());
		zoomOutY.setDuration(350);
		
		AnimatorSet zoomFade = new AnimatorSet();
		zoomFade.play(fadeIn).with(zoomInX).with(zoomInY);
		zoomFade.play(fadeOut).with(zoomOutX).with(zoomOutY).after(fadeIn);
		iv.bringToFront();
		zoomFade.start();
	}
	
	public void startGame(){
		((GameActivity)getActivity()).newGame();
		if(firstGame == false)
			animateGameStatus(R.drawable.new_game);
		
		board = new GameBoard();
		board.setActive(true);
		clearBoard();
		player = whoStart();

		if (player == X)
			tShort.setText(R.string.x_first);
		else
			tShort.setText(R.string.o_first);
			
		firstGame = false;
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
				//tLong.show();
				animateGameStatus(R.drawable.draw);
				((GameActivity)getActivity()).endGame();
				return;
			}
			if (result == WINNER){
				animateGameStatus(R.drawable.victory);
				toastWinner(player);
				return;
			}

			switchPlayer();
		}
	}

	private void toastWinner(int player){
		if (player == X)
			tShort.setText(R.string.x_win);
		else
			tShort.setText(R.string.o_win);
		
		tShort.show();
		((GameActivity)getActivity()).endGame();
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
	
	public boolean isActive(){
		return board.isActive();
	}
	
}
