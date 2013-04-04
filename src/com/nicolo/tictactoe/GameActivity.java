package com.nicolo.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class GameActivity extends Activity {
	private GameBoard board = new GameBoard();
	final private int X = 2;
	final private int O = 1;
	final private int DRAW = -2;
	final private int WINNER = 69;
	final private int INVALID = -99;
	// TODO: Create preference for who goes first
	private int player = X;
	private boolean aiEnabled;
	private AI ai;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Intent intent = getIntent();
		aiEnabled = intent.getBooleanExtra(MainActivity.ENABLE_AI, false);
		if(aiEnabled)
			ai = new AI(O,X,false);
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
		
		if(aiEnabled)
			ai.setTurn(!ai.getTurn());
			
		if(aiEnabled && ai.getTurn() && board.isActive()){
			int [] move = ai.play(board);
			
			TableLayout tl = (TableLayout)findViewById(R.id.board_table);
			TableRow tr = (TableRow)tl.getChildAt(move[1]);
			ImageButton ib = (ImageButton)tr.getChildAt(move[2]);
			ib.performClick();
			
		}
	}
	
	public void clearBoard(View v){
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
		
		board = new GameBoard();
		Toast.makeText(GameActivity.this, R.string.new_game, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
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
