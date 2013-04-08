package com.nicolo.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class GameActivity extends Activity implements OnGestureListener{
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		// Setup gesture detector
		gDetector = new GestureDetector(this,this);
		
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
	
	private void startGame(){
		board = new GameBoard();
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

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent start, MotionEvent finish, float velocityX,
			float velocityY) {
		((TableLayout)findViewById(R.id.board_table)).setBackgroundColor(Color.CYAN);
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		return gDetector.onTouchEvent(e);
	}
	

}
