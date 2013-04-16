package com.nicolo.tictactoe;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import com.nicolo.tictactoe.CancelGameDialogFragment.CancelGameDialogListener;

public class GameActivity extends Activity implements CancelGameDialogListener{	
	
	private CancelGameDialogFragment exitDialog;
	private ObjectAnimator shake;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		setupActionBar();
		exitDialog = new CancelGameDialogFragment();
		// Add the fragment to the screen.
		getFragmentManager().beginTransaction().add(R.id.board_fragment, new BoardFragment()).commit();
		
	}
	
	@Override
	public void onStart(){
		super.onStart();
		if (shake == null){
			initShakeAnimation();
		}
	}
	
	private void initShakeAnimation(){
		LinearLayout ll= (LinearLayout)findViewById(R.id.infoPane);
		shake = ObjectAnimator.ofFloat(ll, "translationX", 0f,10f);
		shake.setInterpolator(new LinearInterpolator());
		shake.setDuration(75);
		shake.setStartDelay(1500);
		shake.setRepeatMode(ObjectAnimator.REVERSE);
		shake.setRepeatCount(3);
		shake.addListener(new AnimatorListener(){
			@Override
			public void onAnimationCancel(Animator animation) {}

			@Override
			public void onAnimationEnd(Animator animation) {
				shake.start();
			}

			@Override
			public void onAnimationRepeat(Animator animation) {}

			@Override
			public void onAnimationStart(Animator animation) {}
			
		});
	}
	
	public void animate(){
		shake.start();
	}
	
	private void stopAnimate(){
		shake.cancel();
	}
	
	@Override
	public void onBackPressed(){
		if(getBoardFragment().isActive())
			exitDialog.show(getFragmentManager(), "Exit_Dialog");
		else
			NavUtils.navigateUpFromSameTask(this);
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
			if(getBoardFragment().isActive())
				exitDialog.show(getFragmentManager(), "Exit_Dialog");
			else
				NavUtils.navigateUpFromSameTask(this);
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		NavUtils.navigateUpFromSameTask(this); // User clicked exit from dialog
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// Do nothing, user clicked cancel from dialog
	}	
	
	private BoardFragment getBoardFragment(){
		return (BoardFragment) getFragmentManager().findFragmentById(R.id.board_fragment);
	}

	public void newGame() {
		stopAnimate();		
	}

	public void endGame() {
		animate();
	}

}
