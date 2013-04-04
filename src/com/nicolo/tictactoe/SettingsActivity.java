package com.nicolo.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

public class SettingsActivity extends Activity {
	private SharedPreferences sharedPref;
	private final int o_int = 1;
	private final int x_int = 2;
	
	private final int ONE = 1;
	private final int TWO = 2;
	private final int THREE = 3;
	private final int FOUR = 4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		// Show the Up button in the action bar.
		setupActionBar();
		sharedPref = getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
		
		int difficulty = sharedPref.getInt(getString(R.string.ai_difficulty_pref), getResources().getInteger(R.integer.ai_difficulty_pref_default));
		int aiSelection = sharedPref.getInt(getString(R.string.ai_selection_pref), getResources().getInteger(R.integer.ai_selection_pref_default));
		
		//Toast.makeText(SettingsActivity.this, ""+goFirst+" "+aiSelection, Toast.LENGTH_LONG).show();
		if(aiSelection == o_int){
			RadioButton rb = (RadioButton)findViewById(R.id.radio_o_ai);
			rb.setChecked(true);
		}else if(aiSelection == x_int){
			RadioButton rb = (RadioButton)findViewById(R.id.radio_x_ai);
			rb.setChecked(true);
		}
		
		if(difficulty == ONE){
			RadioButton rb = (RadioButton)findViewById(R.id.radio_lvl_1);
			rb.setChecked(true);
		}else if(difficulty == TWO){
			RadioButton rb = (RadioButton)findViewById(R.id.radio_lvl_2);
			rb.setChecked(true);
		}else if(difficulty == THREE){
			RadioButton rb = (RadioButton)findViewById(R.id.radio_lvl_3);
			rb.setChecked(true);
		}else if(difficulty == FOUR){
			RadioButton rb = (RadioButton)findViewById(R.id.radio_lvl_4);
			rb.setChecked(true);
		}
		
	}
	
	public void onRadioButtonClicked (View v){
		SharedPreferences.Editor editor = sharedPref.edit();
		boolean checked = ((RadioButton) v).isChecked();
		
		if(v.getId() == R.id.radio_o_ai){
			if(checked)
				editor.putInt(getString(R.string.ai_selection_pref), o_int);
		}else if(v.getId() == R.id.radio_x_ai){
			if(checked)
				editor.putInt(getString(R.string.ai_selection_pref), x_int);				
		}
		
		if(v.getId() == R.id.radio_lvl_1){
			editor.putInt(getString(R.string.ai_difficulty_pref), ONE);
		}else if(v.getId() == R.id.radio_lvl_2){
			editor.putInt(getString(R.string.ai_difficulty_pref),TWO);
		} else if(v.getId() == R.id.radio_lvl_3){
			editor.putInt(getString(R.string.ai_difficulty_pref), THREE);
		} else if(v.getId() == R.id.radio_lvl_4){
			editor.putInt(getString(R.string.ai_difficulty_pref), FOUR);
		} 
		
		editor.commit();
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
