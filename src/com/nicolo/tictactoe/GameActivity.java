package com.nicolo.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class GameActivity extends Activity {
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		 // Gesture detection
        gDetector = new GestureDetector(this, new MyGestureDetector());
        gListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gDetector.onTouchEvent(event);
            }
        };
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
		
		Intent intent = getIntent();
		
		aiEnabled = intent.getBooleanExtra(MainActivity.ENABLE_AI, false);

		if(aiEnabled){
			int aiLetter = sharedPref.getInt(getString(R.string.ai_selection_pref), getResources().getInteger(R.integer.ai_selection_pref_default));
			int difficulty = sharedPref.getInt(getString(R.string.ai_difficulty_pref), getResources().getInteger(R.integer.ai_difficulty_pref_default));
			int humanLetter = (aiLetter == O)? X : O;
			ai = new AI(aiLetter, humanLetter, difficulty);
		}
		
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
	                    Toast.makeText(GameActivity.this, "Left Swipe", Toast.LENGTH_SHORT).show();
	                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	                    startGame();
	                }
	            } catch (Exception e) {
	                // nothing
	            }
	            return false;
        }

    }
	
	private void startGame(){
		board = new GameBoard();
		clearBoard();
		player = whoStart();

		if (player == X)
			Toast.makeText(GameActivity.this, R.string.x_first, Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(GameActivity.this, R.string.o_first, Toast.LENGTH_SHORT).show();
		
		aiMove();
	}
	
	public void processClick(View v){
		 int result = board.processMove(Integer.parseInt(v.getTag().toString()), player);
         
         if(result != INVALID){
         	ImageButton imgView = (ImageButton) v;
     		
     		if (player == X)
     			imgView.setImageResource(R.drawable.x);
     		else
     			imgView.setImageResource(R.drawable.o);
     		
     		
         	if (result == DRAW){
         		Toast.makeText(GameActivity.this, R.string.draw_string, Toast.LENGTH_LONG).show();
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
			Toast.makeText(GameActivity.this, R.string.x_win, Toast.LENGTH_LONG).show();
		else
			Toast.makeText(GameActivity.this, R.string.o_win, Toast.LENGTH_LONG).show();
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
			
			TableLayout tl = (TableLayout)findViewById(R.id.board_table);
			TableRow tr = (TableRow)tl.getChildAt(move[1]);
			ImageButton ib = (ImageButton)tr.getChildAt(move[2]);
			ib.performClick();
		}
	}
	
	public void newGame(View v){
		clearBoard();
		startGame();
	}
	
	private void clearBoard(){
		TableLayout tl = (TableLayout)findViewById(R.id.board_table);
		int tlKids = tl.getChildCount();

		for(int i = 0; i < tlKids; i++){
			TableRow tr = (TableRow)tl.getChildAt(i);
			int trKids = tr.getChildCount();
			for(int j = 0; j < trKids; j++){
				ImageButton ib = (ImageButton)tr.getChildAt(j);
				ib.setImageResource(R.drawable.blank);
				ib.setOnTouchListener(gListener);
			}
		}
	}
	
	private int whoStart(){
		return min + (int)(Math.random() * ((max - min) + 1));
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}	

}
