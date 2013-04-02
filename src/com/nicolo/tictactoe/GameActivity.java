package com.nicolo.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class GameActivity extends Activity {
	private GameBoard board = new GameBoard();
	final private int X = 1;
	final private int O = 0;
	private int player = X;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		// Show the Up button in the action bar.
		setupActionBar();
		
		GridView gridview = (GridView) findViewById(R.id.boardGrid);
		gridview.setAdapter(new ImageAdapter(this));

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            
	            int result = board.processMove(position, player);
	            //Toast.makeText(GameActivity.this, result, Toast.LENGTH_SHORT).show();
	            if(result != -99){
	            	ImageView imgView = (ImageView) v;
	        		
	        		if (player == X)
	        			imgView.setBackgroundResource(R.drawable.x);
	        		else
	        			imgView.setBackgroundResource(R.drawable.o);
	        		
	        		
	            	if (result == -2){
	            		Toast.makeText(GameActivity.this, R.string.draw_string, Toast.LENGTH_LONG).show();
	            	}
	            	if (result == 69){
	            		toastWinner(player);
	            		return;
	            	}
	            	
	            	switchPlayer();
	            }
	        }
	    });
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
	}
	
	public void clearBoard(View v){
		GridView gridview = (GridView) findViewById(R.id.boardGrid);
		gridview.setAdapter(new ImageAdapter(this));
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
