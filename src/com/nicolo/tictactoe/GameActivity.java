package com.nicolo.tictactoe;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;

public class GameActivity extends Activity {	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		setupActionBar();
		
		// Add the fragment to the screen.
		getFragmentManager().beginTransaction().add(R.id.board_fragment, new BoardFragment()).commit();
	}
	
	public void newGame(View v){
		    // Create and commit a new fragment transaction that adds the fragment for the back of
		    // the card, uses custom animations, and is part of the fragment manager's back stack.

		    getFragmentManager()
		            .beginTransaction()

		            // Replace the default fragment animations with animator resources representing
		            // rotations when switching to the back of the card, as well as animator
		            // resources representing rotations when flipping back to the front (e.g. when
		            // the system Back button is pressed).
		            .setCustomAnimations(
		                    R.animator.card_flip_right_in, R.animator.card_flip_right_out,
		                    R.animator.card_flip_left_in, R.animator.card_flip_left_out)

		            // Replace any fragments currently in the container view with a fragment
		            // representing the next page (indicated by the just-incremented currentPage
		            // variable).
		            .replace(R.id.board_fragment, new BoardFragment())

		            // Commit the transaction.
		            .commit();
		
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
